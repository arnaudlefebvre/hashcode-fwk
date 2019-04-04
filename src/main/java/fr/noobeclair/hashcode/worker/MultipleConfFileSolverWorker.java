package fr.noobeclair.hashcode.worker;

import java.util.ArrayList;
import java.util.List;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.bean.config.Config;
import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.out.OutWriter;
import fr.noobeclair.hashcode.score.ScoreCalculator;
import fr.noobeclair.hashcode.solve.ConfigSolver;
import fr.noobeclair.hashcode.utils.ProgressBar;
import fr.noobeclair.hashcode.utils.ProgressBar.Builder;

public class MultipleConfFileSolverWorker<T extends BeanContainer, V extends Config, S extends ConfigSolver<T, V>> extends MultipleConfWorker<T, V, S> {
	
	/**
	 * execOrder, 0 : apply each solver and move to next file 1 : run each file and
	 * move next solver
	 */
	protected Integer execOrder = 0;
	
	public MultipleConfFileSolverWorker(final InReader<T> reader, final OutWriter<T> writer, final ScoreCalculator<T> scorer, final List<ConfigSolver<T, V>> solvers, final List<InOut> files,
			V config) {
		super();
		this.reader = reader;
		this.writer = writer;
		this.scorer = scorer;
		this.solvers = solvers;
		this.files = files;
		this.config = config;
	}
	
	public MultipleConfFileSolverWorker(final InReader<T> reader, final OutWriter<T> writer, final ScoreCalculator<T> scorer, final List<ConfigSolver<T, V>> solvers, V config) {
		super();
		this.reader = reader;
		this.writer = writer;
		this.scorer = scorer;
		this.solvers = solvers;
		this.files = new ArrayList<>();
		this.config = config;
	}
	
	public MultipleConfFileSolverWorker(final InReader<T> reader, final OutWriter<T> writer, final ScoreCalculator<T> scorer, final Integer execOrder, V config) {
		super();
		this.reader = reader;
		this.writer = writer;
		this.scorer = scorer;
		this.solvers = new ArrayList<>();
		this.files = new ArrayList<>();
		this.execOrder = execOrder;
		this.config = config;
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
	protected Builder buildBar() {
		return ProgressBar.builder(approxEnd);
	}
	
}
