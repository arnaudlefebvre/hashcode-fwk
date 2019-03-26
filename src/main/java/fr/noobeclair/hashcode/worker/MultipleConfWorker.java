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
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.bean.Config;
import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.out.OutWriter;
import fr.noobeclair.hashcode.score.ScoreCalculator;
import fr.noobeclair.hashcode.solve.ConfigSolver;
import fr.noobeclair.hashcode.solve.StatsConstants;
import fr.noobeclair.hashcode.utils.ProgressBar.Builder;

public abstract class MultipleConfWorker<T extends BeanContainer, V extends Config, S extends ConfigSolver<T, V>> extends AbstractMultipleWorker<T> {
	
	protected static final Logger logger = LogManager.getLogger(MultipleConfWorker.class);
	
	protected List<ConfigSolver<T, V>> solvers;
	protected Map<Integer, String> solvefileStats = new TreeMap<>();
	protected List<String> outStats = new ArrayList<>();
	protected V config;
	
	private String csvPath;
	private Config.FLUSH_CSV_STATS csvFlushMode = null;
	private boolean toStatsCsv = false;
	
	private static final String CRLF = System.getProperty("line.separator").toString();
	
	public MultipleConfWorker() {
		
	}
	
	public MultipleConfWorker(OutWriter<T> writer, InReader<T> reader, ScoreCalculator<T> scorer, List<ConfigSolver<T, V>> solvers, V config) {
		super();
		this.writer = writer;
		this.reader = reader;
		this.scorer = scorer;
		this.solvers = solvers;
		this.config = config;
	}
	
	@Override
	protected void prepare() {
		total = 0L;
		approxEnd = 0L;
		for (InOut io : files) {
			approxEnd = approxEnd + (fr.noobeclair.hashcode.utils.FileUtils.countLines(io.in) * solvers.size());
		}
		getBar();
		toStatsCsv = (this.config != null && CollectionUtils.isNotEmpty(this.config.getStatisticKeysToWriteToCSV()) && StringUtils.isNotEmpty(this.config.getCsvStatsPath()));
	}
	
	@Override
	protected Map<String, BigDecimal> solve() {
		Map<String, BigDecimal> result = new TreeMap<>();
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
		barEnd();
		return result;
	}
	
	protected abstract Builder buildBar();
	
	private void getBar() {
		if (config != null && config.withProgressBar()) {
			bar = addbarOpt(buildBar()).build();
		}
	}
	
	protected Map<String, BigDecimal> runFileFirst() {
		final Map<String, BigDecimal> result = new TreeMap<>();
		for (final InOut io : files) {
			for (final ConfigSolver<T, V> solver : this.solvers) {
				final SimpleConfWorker<T, V> sw = new SimpleConfWorker<>(reader, solver, scorer, writer, io, bar);
				try {
					runStatSolverForFile(result, solver, io, sw);
				} catch (final Exception e) {
					logger.error(" <###----- !!!!!! -----#> Something went wrong running this worker : {}", sw, e);
					result.put(solver.getName() + ":" + solver.getAdditionnalInfo() + "--" + io.in, BigDecimal.ZERO);
				}
			}
			flushStats(Config.FLUSH_CSV_STATS.EACH_GROUP);
		}
		return result;
	}
	
	protected Map<String, BigDecimal> runSolverFirst() {
		final Map<String, BigDecimal> result = new TreeMap<>();
		for (final ConfigSolver<T, V> solver : this.solvers) {
			for (final InOut io : files) {
				final SimpleConfWorker<T, V> sw = new SimpleConfWorker<>(reader, solver, scorer, writer, io, bar);
				try {
					runStatSolverForFile(result, solver, io, sw);
				} catch (final Exception e) {
					logger.error(" <###----- !!!!!! -----#> Something went wrong running this worker : {}", sw, e);
					result.put(solver.getName() + ":" + solver.getAdditionnalInfo() + "--" + io.in, BigDecimal.ZERO);
				}
			}
			flushStats(Config.FLUSH_CSV_STATS.EACH_GROUP);
		}
		return result;
	}
	
	private void runStatSolverForFile(final Map<String, BigDecimal> result, final ConfigSolver<T, V> solver, final InOut io, final SimpleConfWorker<T, V> sw) {
		result.putAll(runSolverForFile(sw.solver, io));
		handleStats(sw, io);
		barShow(solver.getName() + "#" + io.in.substring(io.in.lastIndexOf("/"), io.in.length()), true);
	}
	
	private void handleStats(final SimpleConfWorker<T, V> sw, final InOut io) {
		ConfigSolver<T, V> solver = sw.solver;
		solvefileStats = solver.getStats();
		if (this.toStatsCsv) {
			outStats.add(buildCsvStatsLine(io.in, solver, solvefileStats, solver.getConfig().getCsvSeparator()));
			String time = solvefileStats.get(StatsConstants.ITEM0_TOTAL);
			if (StringUtils.isNotEmpty(time)) {
				total = total + Long.parseLong(time);
			}
		}
		flushStats(Config.FLUSH_CSV_STATS.EACH_RUN);
	}
	
	protected void flushStats(Config.FLUSH_CSV_STATS level) {
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
	
	private String buildCsvStatsLine(String in, ConfigSolver<T, V> solver, Map<Integer, String> stats, String sep) {
		StringBuilder s = new StringBuilder();
		// fichier, solver name, then stats according to config if it exists
		s.append(in).append(sep);
		s.append(solver.getName()).append(sep);
		if (solver.getConfig() != null && CollectionUtils.isNotEmpty(solver.getConfig().getStatisticKeysToWriteToCSV())) {
			for (Integer i : solver.getConfig().getStatisticKeysToWriteToCSV()) {
				s.append(stats.get(i)).append(sep);
			}
		}
		return s.toString();
	}
	
	private void checkFlushModeAndPath() {
		for (final ConfigSolver<T, V> solver : this.solvers) {
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
}
