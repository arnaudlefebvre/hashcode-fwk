package fr.noobeclair.hashcode.worker;

import java.util.ArrayList;
import java.util.List;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.bean.config.Config;
import fr.noobeclair.hashcode.bean.config.WorkerConfig;
import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.out.OutWriter;
import fr.noobeclair.hashcode.score.ScoreCalculator;
import fr.noobeclair.hashcode.solve.ConfigSolver;
import fr.noobeclair.hashcode.utils.ProgressBar;
import fr.noobeclair.hashcode.utils.ProgressBar.Builder;

public class MultipleConfFileSolverWorker<T extends BeanContainer, V extends Config, S extends ConfigSolver<T, V>, W extends WorkerConfig>
		extends MultipleConfWorker<T, V, S, W> {

	/**
	 * execOrder, 0 : apply each solver and move to next file 1 : run each file and
	 * move next solver
	 */
	protected Integer execOrder = 0;

	public MultipleConfFileSolverWorker() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MultipleConfFileSolverWorker(List<InOut> files, InReader<T> reader, OutWriter<T> writer,
			ScoreCalculator<T> scorer) {
		super(files, reader, writer, scorer);
		// TODO Auto-generated constructor stub
	}

	public MultipleConfFileSolverWorker(OutWriter<T> writer, InReader<T> reader, ScoreCalculator<T> scorer,
			List<S> solvers, V config, W wconfig) {
		super(writer, reader, scorer, solvers, config, wconfig);
		// TODO Auto-generated constructor stub
	}

	public MultipleConfFileSolverWorker(final InReader<T> reader, final OutWriter<T> writer,
			final ScoreCalculator<T> scorer, final List<S> solvers, final List<InOut> files, V config, W wconfig) {
		super(writer, reader, scorer, solvers, config, wconfig);
		this.files = files;
	}

	public MultipleConfFileSolverWorker(final InReader<T> reader, final OutWriter<T> writer,
			final ScoreCalculator<T> scorer, final List<S> solvers, V config, W wconfig) {
		super(writer, reader, scorer, solvers, config, wconfig);
		this.files = new ArrayList<>();
	}

	public MultipleConfFileSolverWorker(final InReader<T> reader, final OutWriter<T> writer,
			final ScoreCalculator<T> scorer, final Integer execOrder, V config, W wconfig) {
		super(writer, reader, scorer, new ArrayList<>(), config, wconfig);
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
	protected Builder buildBar() {
		return ProgressBar.builder(approxEnd);
	}

}
