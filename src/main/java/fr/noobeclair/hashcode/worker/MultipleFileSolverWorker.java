package fr.noobeclair.hashcode.worker;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.bean.Config;
import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.out.OutWriter;
import fr.noobeclair.hashcode.score.ScoreCalculator;
import fr.noobeclair.hashcode.solve.Solver;
import fr.noobeclair.hashcode.solve.StatsConstants;
import fr.noobeclair.hashcode.utils.ProgressBar;
import fr.noobeclair.hashcode.utils.ProgressBar.ProgressBarOption;

public class MultipleFileSolverWorker<T extends BeanContainer, V extends Config, S extends Solver<T, V>> extends MultipleWorker<T, V, S> {
	
	protected List<Solver<T, V>> solvers;
	protected List<InOut> files;
	protected Map<Integer, String> solvefileStats = new TreeMap<>();
	protected List<String> outStats = new ArrayList<>();
	protected List<String> seenFiles = new ArrayList<>();
	
	private Long total = 0L;
	private Long approxEnd = 0L;
	private ProgressBar bar;
	
	private Config.FLUSH_CSV_STATS csvFlushMode = null;
	private String csvPath;
	
	private static final String CRLF = System.getProperty("line.separator").toString();
	/**
	 * execOrder, 0 : apply each solver and move to next file 1 : run each file and
	 * move next solver
	 */
	protected Integer execOrder = 0;
	
	public MultipleFileSolverWorker(final InReader<T> reader, final OutWriter<T> writer, final ScoreCalculator<T> scorer, final List<Solver<T, V>> solvers, final List<InOut> files) {
		super();
		this.reader = reader;
		this.writer = writer;
		this.scorer = scorer;
		this.solvers = solvers;
		this.files = files;
	}
	
	public MultipleFileSolverWorker(final InReader<T> reader, final OutWriter<T> writer, final ScoreCalculator<T> scorer, final List<Solver<T, V>> solvers) {
		super();
		this.reader = reader;
		this.writer = writer;
		this.scorer = scorer;
		this.solvers = solvers;
		this.files = new ArrayList<>();
	}
	
	public MultipleFileSolverWorker(final InReader<T> reader, final OutWriter<T> writer, final ScoreCalculator<T> scorer, final Integer execOrder) {
		super();
		this.reader = reader;
		this.writer = writer;
		this.scorer = scorer;
		this.solvers = new ArrayList<>();
		this.files = new ArrayList<>();
		this.execOrder = execOrder;
	}
	
	public void addFiles(final List<InOut> files) {
		this.files = files;
	}
	
	public void addFile(final String in, final String out) {
		this.files.add(new InOut(in, out));
	}
	
	public void addSolver(final S solver) {
		this.solvers.add(solver);
	}
	
	public void addSolver(final List<S> solvers) {
		this.solvers.addAll(solvers);
	}
	
	@Override
	public Map<String, BigDecimal> run() {
		Map<String, BigDecimal> result = new TreeMap<>();
		approx();
		checkFlushModeAndPath();
		if (1 == this.execOrder) {
			result = runSolverFirst();
		} else {
			result = runFileFirst();
		}
		if (files.isEmpty()) {
			logger.error(" <###----- !!!!!! -----#> No file - No run ... !");
		}
		if (solvers.isEmpty()) {
			logger.error(" <###----- !!!!!! -----#> No solver - No run ... !");
		}
		flushStats(Config.FLUSH_CSV_STATS.END);
		bar.end(System.out);
		return result;
	}
	
	private void approx() {
		total = 0L;
		approxEnd = 0L;
		for (InOut io : files) {
			approxEnd = approxEnd + (fr.noobeclair.hashcode.utils.FileUtils.countLines(io.in) * solvers.size());
		}
		bar = ProgressBar.builder(approxEnd).withMaxWidth(100).withOption(ProgressBarOption.ALL).withAutoRefreshTime(false).build();
	}
	
	public Map<String, BigDecimal> runFileFirst() {
		final Map<String, BigDecimal> result = new TreeMap<>();
		for (final InOut io : files) {
			for (final Solver<T, V> solver : this.solvers) {
				final SimpleProgressWorker<T, V> sw = new SimpleProgressWorker<>(reader, solver, scorer, writer, io, bar);
				try {
					runSolverForFile(result, solver, io, sw);
				} catch (final Exception e) {
					logger.error(" <###----- !!!!!! -----#> Something went wrong running this worker : {}", sw, e);
					result.put(solver.getClass().getSimpleName() + ":" + solver.getAdditionnalInfo() + "--" + io.in, BigDecimal.ZERO);
				}
			}
			flushStats(Config.FLUSH_CSV_STATS.EACH_GROUP);
		}
		return result;
	}
	
	public Map<String, BigDecimal> runSolverFirst() {
		final Map<String, BigDecimal> result = new TreeMap<>();
		for (final Solver<T, V> solver : this.solvers) {
			for (final InOut io : files) {
				final SimpleProgressWorker<T, V> sw = new SimpleProgressWorker<>(reader, solver, scorer, writer, io, bar);
				try {
					runSolverForFile(result, solver, io, sw);
				} catch (final Exception e) {
					logger.error(" <###----- !!!!!! -----#> Something went wrong running this worker : {}", sw, e);
					result.put(solver.getClass().getSimpleName() + ":" + solver.getAdditionnalInfo() + "--" + io.in, BigDecimal.ZERO);
				}
			}
			flushStats(Config.FLUSH_CSV_STATS.EACH_GROUP);
		}
		return result;
	}
	
	private void runSolverForFile(final Map<String, BigDecimal> result, final Solver<T, V> solver, final InOut io, final SimpleProgressWorker<T, V> sw) {
		BigDecimal b = sw.run();
		result.put(solver.getClass().getSimpleName() + ":" + solver.getAdditionnalInfo() + "--" + io.in, b);
		handleStats(sw, io);
		bar.show(System.out, total, ">" + io.in.substring(io.in.lastIndexOf("/"), io.in.length()), true);
	}
	
	private void handleStats(final SimpleWorker<T, V> sw, final InOut io) {
		Solver<T, V> solver = sw.solver;
		if (solver.getConfig() != null && CollectionUtils.isNotEmpty(solver.getConfig().getStatisticKeysToWriteToCSV())) {
			solvefileStats = solver.getStats();
			outStats.add(buildCsvStatsLine(io.in, solver, solvefileStats, solver.getConfig().getCsvSeparator()));
			total = total + Long.parseLong(solvefileStats.get(StatsConstants.ITEM0_TOTAL));
		} else {
			total = total + 1;
		}
		flushStats(Config.FLUSH_CSV_STATS.EACH_RUN);
	}
	
	private String buildCsvStatsLine(String in, Solver<T, V> solver, Map<Integer, String> stats, String sep) {
		StringBuilder s = new StringBuilder();
		// fichier, solver name, then stats according to config if it exists
		s.append(in).append(sep);
		s.append(solver.getClass().getSimpleName()).append(sep);
		if (solver.getConfig() != null && CollectionUtils.isNotEmpty(solver.getConfig().getStatisticKeysToWriteToCSV())) {
			for (Integer i : solver.getConfig().getStatisticKeysToWriteToCSV()) {
				s.append(stats.get(i)).append(sep);
			}
		}
		return s.toString();
	}
	
	private void checkFlushModeAndPath() {
		for (final Solver<T, V> solver : this.solvers) {
			if (solver.getConfig() != null && solver.getConfig().getFlushOpt() != null) {
				if (this.csvFlushMode != null && solver.getConfig().getFlushOpt() != this.csvFlushMode) {
					// this is not normal. either get this one either throw an exception
					// as procces can be long, it would be better to stop before
					throw new RuntimeException("Some solvers config does not have the same flush option. Please check your config : Either all the same, Either one set others null");
				} else {
					this.csvFlushMode = solver.getConfig().getFlushOpt();
					if (this.csvFlushMode == Config.FLUSH_CSV_STATS.END) {
						outStats = new ArrayList<String>(approxEnd.intValue());
					} else if (this.csvFlushMode == Config.FLUSH_CSV_STATS.EACH_GROUP) {
						outStats = new ArrayList<String>(Math.max(this.solvers.size(), this.files.size()));
					} else {
						outStats = new ArrayList<String>(1);
					}
				}
			}
			if (solver.getConfig() != null && solver.getConfig().getCsvStatsPath() != null) {
				if (this.csvPath != null && !solver.getConfig().getCsvStatsPath().equals(this.csvPath)) {
					// this is not normal. either get this one either throw an exception
					// as procces can be long, it would be better to stop before
					throw new RuntimeException("Some solvers config does not have the same csv path. Please check your config : Either all the same, Either one set others null");
				} else {
					this.csvPath = solver.getConfig().getCsvStatsPath();
				}
			}
			if (this.csvPath != null) {
				fr.noobeclair.hashcode.utils.FileUtils.rm(this.csvPath);
			}
		}
	}
	
	private void flushStats(Config.FLUSH_CSV_STATS level) {
		if (this.csvFlushMode != null && this.csvFlushMode == level) {
			File file = new File(this.csvPath);
			try {
				if (level == Config.FLUSH_CSV_STATS.EACH_RUN) {
					FileUtils.write(file, outStats.get(0) + CRLF, Charset.forName("UTF-8"), true);
					outStats = new ArrayList<String>(1);
				} else {
					FileUtils.write(file, outStats.stream().collect(Collectors.joining(CRLF)), Charset.forName("UTF-8"), true);
					outStats = new ArrayList<String>(outStats.size());
				}
				
			} catch (IOException e) {
				logger.error(" <###----- !!!!!! -----#> Problem occurs when writing : {} on level {} with {}", this.csvPath, level, e);
			}
		}
		
	}
	
}
