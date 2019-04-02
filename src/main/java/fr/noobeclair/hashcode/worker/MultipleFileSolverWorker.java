package fr.noobeclair.hashcode.worker;

import java.util.ArrayList;
import java.util.List;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.out.OutWriter;
import fr.noobeclair.hashcode.score.ScoreCalculator;
import fr.noobeclair.hashcode.solve.Solver;

public class MultipleFileSolverWorker<T extends BeanContainer, S extends Solver<T>> extends MultipleWorker<T, S> {
	
	protected List<Solver<T>> solvers;
	protected List<InOut> files;
	
	public MultipleFileSolverWorker(final InReader<T> reader, final OutWriter<T> writer, final ScoreCalculator<T> scorer, final List<Solver<T>> solvers, final List<InOut> files) {
		super();
		this.reader = reader;
		this.writer = writer;
		this.scorer = scorer;
		this.solvers = solvers;
		this.files = files;
	}
	
	public MultipleFileSolverWorker(final InReader<T> reader, final OutWriter<T> writer, final ScoreCalculator<T> scorer, final List<Solver<T>> solvers) {
		super();
		this.reader = reader;
		this.writer = writer;
		this.scorer = scorer;
		this.solvers = solvers;
		this.files = new ArrayList<>();
	}
	
	public MultipleFileSolverWorker(final InReader<T> reader, final OutWriter<T> writer, final ScoreCalculator<T> scorer, final WORK_ORDER execOrder) {
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
	
}
