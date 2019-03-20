package fr.noobeclair.hashcode.worker;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.bean.Config;
import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.out.OutWriter;
import fr.noobeclair.hashcode.score.ScoreCalculator;
import fr.noobeclair.hashcode.solve.Solver;

public class MultipleFileSolverWorker<T extends BeanContainer, V extends Config, S extends Solver<T, V>> extends MultipleWorker<T, V, S> {
	
	protected List<Solver<T, V>> solvers;
	protected List<InOut> files;
	
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
	
	public MultipleFileSolverWorker(final InReader<T> reader, final OutWriter<T> writer, final ScoreCalculator<T> scorer) {
		super();
		this.reader = reader;
		this.writer = writer;
		this.scorer = scorer;
		this.solvers = new ArrayList<>();
		this.files = new ArrayList<>();
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
		final Map<String, BigDecimal> result = new TreeMap<>();
		for (final InOut io : files) {
			for (final Solver<T, V> solver : this.solvers) {
				final SimpleWorker<T, V> sw = new SimpleWorker<>(reader, solver, scorer, writer, io);
				try {
					result.put(solver.getClass().getSimpleName() + ":" + solver.getAdditionnalInfo() + "--" + io.in, sw.run());
				} catch (final RuntimeException e) {
					logger.error(" <###----- !!!!!! -----#> Something went wrong running this worker : {}", sw, e);
					result.put(solver.getClass().getSimpleName() + ":" + solver.getAdditionnalInfo() + "--" + io.in, BigDecimal.ZERO);
				}
			}
		}
		if (files.isEmpty()) {
			logger.error(" <###----- !!!!!! -----#> No file - No run ... !");
		}
		if (solvers.isEmpty()) {
			logger.error(" <###----- !!!!!! -----#> No solver - No run ... !");
		}
		return result;
	}
	
}
