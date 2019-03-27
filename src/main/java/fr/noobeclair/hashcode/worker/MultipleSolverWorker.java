package fr.noobeclair.hashcode.worker;

import java.util.ArrayList;
import java.util.List;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.out.OutWriter;
import fr.noobeclair.hashcode.score.ScoreCalculator;
import fr.noobeclair.hashcode.solve.Solver;

//This class is only a list of SimpleConfWorker that are run one after the other and return a map of solver-score
public class MultipleSolverWorker<T extends BeanContainer, S extends Solver<T>> extends MultipleWorker<T, S> {
	
	protected List<SimpleWorker<T>> solvers;
	
	public MultipleSolverWorker(final InReader<T> reader, final OutWriter<T> writer, final ScoreCalculator<T> scorer, final String in, final String out) {
		super();
		this.solvers = new ArrayList<>();
		this.reader = reader;
		this.writer = writer;
		this.scorer = scorer;
		this.files = new ArrayList<>();
		this.files.add(new InOut(in, out));
	}
	
	public MultipleSolverWorker(final InReader<T> reader, final OutWriter<T> writer, final ScoreCalculator<T> scorer, final InOut inOut) {
		super();
		this.solvers = new ArrayList<>();
		this.reader = reader;
		this.writer = writer;
		this.scorer = scorer;
		this.files = new ArrayList<>();
		this.files.add(inOut);
	}
	
	public void addSolver(final Solver<T> solver) {
		this.solvers.add(new SimpleWorker<>(reader, solver, scorer, writer, getInOut().in, getInOut().out + "-" + solver.getName()));
	}
	
	public void addSolvers(final List<Solver<T>> solvers) {
		for (Solver<T> s : solvers) {
			this.solvers.add(new SimpleWorker<>(reader, s, scorer, writer, getInOut().in, getInOut().out + "-" + s.getName()));
		}
	}
	
	public List<SimpleWorker<T>> getSolvers() {
		return solvers;
	}
	
	public void setSolvers(final List<SimpleWorker<T>> solvers) {
		this.solvers = solvers;
	}
	
	public InOut getInOut() {
		return this.files.get(0);
	}
	
	public void setInOut(final InOut inOut) {
		this.files = new ArrayList<>();
		this.files.add(inOut);
	}
	
}
