package fr.noobeclair.hashcode.worker;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.noobeclair.hashcode.annotation.CsvExport;
import fr.noobeclair.hashcode.annotation.CsvField;
import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.bean.config.Config;
import fr.noobeclair.hashcode.bean.config.WorkerConfig;
import fr.noobeclair.hashcode.constants.GlobalConstants;
import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.out.OutWriter;
import fr.noobeclair.hashcode.score.ScoreCalculator;
import fr.noobeclair.hashcode.solve.ConfigSolver;
import fr.noobeclair.hashcode.solve.StatsConstants;
import fr.noobeclair.hashcode.utils.ProgressBar.Builder;
import fr.noobeclair.hashcode.utils.dto.SolverResultDto;
import fr.noobeclair.hashcode.utils.dto.WorkerResultDto;

public abstract class MultipleConfWorker<T extends BeanContainer, V extends Config, S extends ConfigSolver<T, V>, W extends WorkerConfig>
		extends AbstractMultipleWorker<T> {

	protected static final Logger logger = LogManager.getLogger(MultipleConfWorker.class);

	protected List<S> solvers;
	protected Map<Integer, String> solvefileStats = new TreeMap<>();
	protected List<String> outStats = new ArrayList<>();
	protected V config;
	protected W workerConfig;

	private String csvPath;
	private WorkerConfig.FLUSH_CSV_STATS csvFlushMode = null;
	private boolean toStatsCsv = false;
	private boolean isCsvColsAdded = false;
	private boolean isCsvColsflushed = false;

	private static final String CRLF = System.getProperty("line.separator").toString();

	public MultipleConfWorker() {

	}

	public MultipleConfWorker(List<InOut> files, InReader<T> reader, OutWriter<T> writer, ScoreCalculator<T> scorer) {
		super(files, reader, writer, scorer);
		// TODO Auto-generated constructor stub
	}

	public MultipleConfWorker(OutWriter<T> writer, InReader<T> reader, ScoreCalculator<T> scorer, List<S> solvers,
			V config, W wconfig) {
		super();
		this.writer = writer;
		this.reader = reader;
		this.scorer = scorer;
		this.solvers = solvers;
		this.config = config;
		this.workerConfig = wconfig;
	}

	@Override
	protected void prepare() {
		total = 0L;
		approxEnd = 0L;
		for (InOut io : files) {
			approxEnd = approxEnd + (fr.noobeclair.hashcode.utils.FileUtils.countLines(io.in) * solvers.size());
		}
		getBar();
		checkFlushModeAndPath();
		toStatsCsv = (this.workerConfig != null
				&& CollectionUtils.isNotEmpty(this.workerConfig.getStatisticKeysToWriteToCSV())
				&& StringUtils.isNotEmpty(this.workerConfig.getCsvStatsPath()));
	}

	@Override
	protected WorkerResultDto solve() {
		WorkerResultDto result = null;
		if (AbstractMultipleWorker.WORK_ORDER.SOLVER == this.execOrder) {
			result = runSolverFirst();
		} else {
			result = runFileFirst();
		}
		if (files.isEmpty()) {
			logger.error("No file - No run ... !");
		}
		if (solvers.isEmpty()) {
			logger.error("No solver - No run ... !");
		}
		flushStats(WorkerConfig.FLUSH_CSV_STATS.END);
		barEnd();
		return result;
	}

	protected abstract Builder buildBar();

	private void getBar() {
		if (workerConfig != null && workerConfig.withProgressBar()) {
			bar = addbarOpt(buildBar()).build();
		}
	}

	protected WorkerResultDto runFileFirst() {
		WorkerResultDto result = new WorkerResultDto();
		for (final InOut io : files) {
			for (final ConfigSolver<T, V> solver : this.solvers) {
				final SimpleConfWorker<T, V> sw = new SimpleConfWorker<>(reader, solver, scorer, writer, io, bar);
				try {
					runStatSolverForFile(result, solver, io, sw);
				} catch (final Exception e) {
					logger.error("Something went wrong running this worker : {}", sw, e);
					result.addResult(new SolverResultDto(BigDecimal.ZERO, solver.getName(), io.in, -1L, -1L, -1L));
				}
			}
			flushStats(WorkerConfig.FLUSH_CSV_STATS.EACH_GROUP);
		}
		return result;
	}

	protected WorkerResultDto runSolverFirst() {
		WorkerResultDto result = new WorkerResultDto();
		for (final ConfigSolver<T, V> solver : this.solvers) {
			for (final InOut io : files) {
				final SimpleConfWorker<T, V> sw = new SimpleConfWorker<>(reader, solver, scorer, writer, io, bar);
				try {
					runStatSolverForFile(result, solver, io, sw);
				} catch (final Exception e) {
					logger.error("Something went wrong running this worker : {}", sw, e);
					result.addResult(new SolverResultDto(BigDecimal.ZERO, solver.getName(), io.in, -1L, -1L, -1L));
				}
			}
			flushStats(WorkerConfig.FLUSH_CSV_STATS.EACH_GROUP);
		}
		return result;
	}

	private void runStatSolverForFile(final WorkerResultDto result, final ConfigSolver<T, V> solver, final InOut io,
			final SimpleConfWorker<T, V> sw) {
		result.addResult(runSolverForFile(sw.solver, io));
		try {
			handleStats(sw, io);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			logger.error("Cannot write state output for solver {}", solver, e);
			throw new RuntimeException(e);
		}
		barShow(solver.getName() + "#" + io.in.substring(io.in.lastIndexOf("/"), io.in.length()), true);
	}

	private void handleStats(final SimpleConfWorker<T, V> sw, final InOut io)
			throws IllegalArgumentException, IllegalAccessException {
		ConfigSolver<T, V> solver = sw.solver;
		solvefileStats = solver.getStats();
		if (this.toStatsCsv) {
			if (!isCsvColsAdded) {
				isCsvColsAdded = true;
				outStats.add(buildCsvCols(io.in, solver));
			}
			outStats.add(buildCsvLine(io.in, solver));
			String time = solvefileStats.get(StatsConstants.ITEM0_TOTAL);
			if (StringUtils.isNotEmpty(time)) {
				total = total + Long.parseLong(time);
			}
		}
		flushStats(WorkerConfig.FLUSH_CSV_STATS.EACH_RUN);
	}

	protected void flushStats(WorkerConfig.FLUSH_CSV_STATS level) {
		if (this.csvFlushMode != null && this.csvFlushMode == level) {
			File file = new File(this.csvPath);
			try {
				if (level == WorkerConfig.FLUSH_CSV_STATS.EACH_RUN) {
					StringBuilder out = new StringBuilder(outStats.get(0));
					out.append(CRLF);
					if (!isCsvColsflushed) {
						isCsvColsflushed = true;
						out.append(outStats.get(1)).append(CRLF);
					}
					FileUtils.write(file, out, Charset.forName("UTF-8"), true);
					outStats = new ArrayList<String>(1);
				} else {
					FileUtils.write(file, outStats.stream().collect(Collectors.joining(CRLF)), Charset.forName("UTF-8"),
							true);
					outStats = new ArrayList<String>(outStats.size());
				}

			} catch (IOException e) {
				logger.error("Problem occurs when writing : {} on level {} with {}", this.csvPath, level, e);
			}
		}

	}

	private String buildCsvCols(String in, ConfigSolver<T, V> solver)
			throws IllegalArgumentException, IllegalAccessException {
		StringBuilder s = new StringBuilder();
		if (this.workerConfig != null) {
			s.append("Resources").append(this.workerConfig.getCsvSeparator());
			s.append(getCsvCol(solver)).append(this.workerConfig.getCsvSeparator());
			s.append(getCsvCol(solver.getConfig())).append(this.workerConfig.getCsvSeparator());
		}
		return s.toString();
	}

	private String buildCsvLine(String in, ConfigSolver<T, V> solver)
			throws IllegalArgumentException, IllegalAccessException {
		StringBuilder s = new StringBuilder();
		if (this.workerConfig != null) {
			s.append(in).append(this.workerConfig.getCsvSeparator());
			s.append(getCsvExport(solver)).append(this.workerConfig.getCsvSeparator());
			s.append(getCsvExport(solver.getConfig())).append(this.workerConfig.getCsvSeparator());
		}
		return s.toString();
	}

	private String getCsvCol(Object o) throws IllegalArgumentException, IllegalAccessException {
		HashMap<Integer, String> classLine = new HashMap<>();
		int i = 0;
		boolean ordered = false;
		if (o.getClass().isAnnotationPresent(CsvExport.class)) {
			for (Field f : o.getClass().getDeclaredFields()) {
				if (f.isAnnotationPresent(CsvField.class)) {
					String name = f.getName();
					CsvField ann = f.getAnnotation(CsvField.class);
					if (StringUtils.isNotBlank(ann.name())) {
						name = ann.name();
					}
					if (ann.order() != -1) {
						ordered = true;
						i = ann.order();
					} else {
						i = i + 1;
						if (ordered) {
							throw new RuntimeException("If one field is ordered, all must be " + f.getName());
						}
					}
					classLine.put(i, name);
				}

			}
		}
		return classLine.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey)).map(Map.Entry::getValue)
				.collect(Collectors.joining(this.workerConfig.getCsvSeparator()));
	}

	private String getCsvExport(Object o) throws IllegalArgumentException, IllegalAccessException {
		HashMap<Integer, String> classLine = new HashMap<>();
		int i = 0;
		boolean ordered = false;
		if (o.getClass().isAnnotationPresent(CsvExport.class)) {
			for (Field f : o.getClass().getDeclaredFields()) {
				if (f.isAnnotationPresent(CsvField.class)) {
					CsvField ann = f.getAnnotation(CsvField.class);
					if (ann.order() != -1) {
						ordered = true;
						i = ann.order();
					} else {
						i = i + 1;
						if (ordered) {
							throw new RuntimeException("If one field is ordered, all must be " + f.getName());
						}
					}
					f.setAccessible(true);
					classLine.put(i, "" + f.get(o));
				}
			}
		}
		return classLine.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey)).map(Map.Entry::getValue)
				.collect(Collectors.joining(this.workerConfig.getCsvSeparator()));
	}

//	private String buildCsvStatsLine(String in, ConfigSolver<T, V> solver, Map<Integer, String> stats, String sep) {
//		StringBuilder s = new StringBuilder();
//		// fichier, solver name, then stats according to config if it exists
//		s.append(in).append(sep);
//		s.append(solver.getName()).append(sep);
//		if (this.workerConfig != null && CollectionUtils.isNotEmpty(this.workerConfig.getStatisticKeysToWriteToCSV())) {
//			for (Integer i : this.workerConfig.getStatisticKeysToWriteToCSV()) {
//				s.append(stats.get(i)).append(sep);
//			}
//		}
//		return s.toString();
//	}

	private void checkFlushModeAndPath() {
		if (this.workerConfig != null && this.workerConfig.getFlushOpt() != null) {
			this.csvFlushMode = this.workerConfig.getFlushOpt();
			if (this.csvFlushMode == WorkerConfig.FLUSH_CSV_STATS.END) {
				outStats = new ArrayList<String>(approxEnd.intValue());
			} else if (this.csvFlushMode == WorkerConfig.FLUSH_CSV_STATS.EACH_GROUP) {
				outStats = new ArrayList<String>(Math.max(this.solvers.size(), this.files.size()));
			} else {
				outStats = new ArrayList<String>(1);
			}
		}

		if (this.workerConfig != null && this.workerConfig.getCsvStatsPath() != null) {
			this.csvPath = this.workerConfig.getCsvStatsPath();
		}
		if (this.csvPath != null) {
			fr.noobeclair.hashcode.utils.FileUtils.rm(this.csvPath);
		}
	}

	protected Builder addbarOpt(Builder b) {
		if (this.workerConfig != null) {
			return b.withMaxWidth(this.workerConfig.getBarMaxWidth()).withOptions(this.workerConfig.getBarOpts())
					.withBarMsgSize(this.workerConfig.getBarMsgWidth())
					.withRefreshTime(this.workerConfig.getBarRefreshTime());
		}
		return b.withMaxWidth(GlobalConstants.BAR_MAX_WIDTH).withOptions(GlobalConstants.BAR_OPTS)
				.withBarMsgSize(GlobalConstants.BAR_MSG_WIDTH).withRefreshTime(GlobalConstants.BAR_REFRESH_TIME);
	}

	public void setWorkerConfig(W workerConfig) {
		this.workerConfig = workerConfig;
	}

	public void setSolvers(List<S> solvers) {
		this.solvers = solvers;
	}

	public void setConfig(V config) {
		this.config = config;
	}

}
