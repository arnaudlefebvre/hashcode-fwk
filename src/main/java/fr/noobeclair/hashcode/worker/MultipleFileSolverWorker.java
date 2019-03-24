package fr.noobeclair.hashcode.worker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

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
	
	public static final Long DEF_PG_END = 1000L;
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
		bar = ProgressBar.builder(approxEnd).withMaxWidth(100).withOption(ProgressBarOption.ALL).build();		
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
		writeStats();
		return result;
	}
	
	public void approx() {
		total = 0L;
		approxEnd = 0L;
		for (InOut io : files) {
			approxEnd = approxEnd + (count(io.in) * solvers.size());
		}
	}
	
	public void writeStats() {
		final String newLine = System.getProperty("line.separator").toString();
		File file = new File("src/main/resources/out/2018/global-stats.csv");
		try {
			// Set the third parameter to true to specify you want to append to file.
			FileUtils.write(file, outStats.stream().collect(Collectors.joining(newLine)), true);
		} catch (IOException e) {
			System.out.println("Problem occurs when writing : src/main/resources/out/2018/global-stats.csv");
			e.printStackTrace();
		}
	}
	
	public Map<String, BigDecimal> runFileFirst() {
		final Map<String, BigDecimal> result = new TreeMap<>();
		for (final InOut io : files) {
			for (final Solver<T, V> solver : this.solvers) {
				final SimpleWorker<T, V> sw = new SimpleWorker<>(reader, solver, scorer, writer, io);
				try {
					runSolverForFile(result, solver, io, sw);
				} catch (final Exception e) {
					logger.error(" <###----- !!!!!! -----#> Something went wrong running this worker : {}", sw, e);
					result.put(solver.getClass().getSimpleName() + ":" + solver.getAdditionnalInfo() + "--" + io.in, BigDecimal.ZERO);
				}
			}
		}
		return result;
	}
	
	private Long count(String f) {
		Long lines = 0L;
		try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
			while (reader.readLine() != null)
				lines++;
		} catch (Exception e) {
			logger.error("Erreur de lecture du fichier {}", f);
		}
		return lines;
	}
	
	public Map<String, BigDecimal> runSolverFirst() {
		final Map<String, BigDecimal> result = new TreeMap<>();
		for (final Solver<T, V> solver : this.solvers) {
			for (final InOut io : files) {
				final SimpleWorker<T, V> sw = new SimpleWorker<>(reader, solver, scorer, writer, io);
				try {
					runSolverForFile(result, solver, io, sw);
				} catch (final Exception e) {
					logger.error(" <###----- !!!!!! -----#> Something went wrong running this worker : {}", sw, e);
					result.put(solver.getClass().getSimpleName() + ":" + solver.getAdditionnalInfo() + "--" + io.in, BigDecimal.ZERO);
				}
			}
		}
		
		return result;
	}
	
	private void runSolverForFile(final Map<String, BigDecimal> result, final Solver<T, V> solver, final InOut io, final SimpleWorker<T, V> sw) {
		BigDecimal b = sw.run();
		solvefileStats = sw.solver.getStats();
		result.put(solver.getClass().getSimpleName() + ":" + solver.getAdditionnalInfo() + "--" + io.in, b);
		outStats.add(outSepLine(io.in, solver, solvefileStats, ";"));
		// updateEnd(io.in, solvefileStats.get(StatsConstants.ITEM0_TOTAL));
		total = total + Long.parseLong(solvefileStats.get(StatsConstants.ITEM0_TOTAL));
		bar.show(System.out, total, solver.getClass().getSimpleName() + ">" + io.in.substring(io.in.lastIndexOf("/"), io.in.length()));
	}
	
	private String outSepLine(String in, Solver<T, V> solver, Map<Integer, String> stats, String sep) {
		StringBuilder s = new StringBuilder();
		// fichier, solver, strats, score, temps, nb ride, nb turn, bonus, conf
		s.append(in).append(sep);
		
		s.append(solver.getClass().getSimpleName()).append(sep);
		s.append(stats.get(StatsConstants.CF_STRAT)).append(sep);
		s.append(stats.get(StatsConstants.SCORE)).append(sep);
		s.append(stats.get(StatsConstants.TIME_TOTAL)).append(sep);
		s.append(stats.get(StatsConstants.ITEM0_TOTAL)).append(sep);
		s.append(stats.get(StatsConstants.IN_0)).append(sep);
		s.append(stats.get(StatsConstants.IN_1)).append(sep);
		s.append(stats.get(StatsConstants.CF_TTFC)).append(sep);
		s.append(stats.get(StatsConstants.CF_NTFCT)).append(sep);
		s.append(stats.get(StatsConstants.CF_LTFCT)).append(sep);
		s.append(stats.get(StatsConstants.CF_NDFCT)).append(sep);
		s.append(stats.get(StatsConstants.CF_LDFCT)).append(sep);
		s.append(stats.get(StatsConstants.CF_NAT)).append(sep);
		s.append(stats.get(StatsConstants.CF_NBT)).append(sep);
		s.append(stats.get(StatsConstants.CF_NAD)).append(sep);
		s.append(stats.get(StatsConstants.CF_NBD)).append(sep);
		s.append(stats.get(StatsConstants.CF_LAT)).append(sep);
		s.append(stats.get(StatsConstants.CF_LBT)).append(sep);
		s.append(stats.get(StatsConstants.CF_LAD)).append(sep);
		s.append(stats.get(StatsConstants.CF_LBD)).append(sep);
		
		return s.toString();
	}
	
}
