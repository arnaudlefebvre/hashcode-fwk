package fr.noobeclair.hashcode.worker;

import java.util.ArrayList;
import java.util.List;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.bean.Config;
import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.out.OutWriter;
import fr.noobeclair.hashcode.score.ScoreCalculator;
import fr.noobeclair.hashcode.solve.ConfigSolver;
import fr.noobeclair.hashcode.utils.ProgressBar;
import fr.noobeclair.hashcode.utils.ProgressBar.Builder;

//This class is only a list of SimpleConfWorker that are run one after the other and return a map of solver-score
public class MultipleConfSolverWorker<T extends BeanContainer, V extends Config, S extends ConfigSolver<T, V>> extends MultipleConfWorker<T, V, S> {
	
	public MultipleConfSolverWorker(final InReader<T> reader, final OutWriter<T> writer, final ScoreCalculator<T> scorer, final String in, final String out, V config) {
		super();
		this.solvers = new ArrayList<>();
		this.reader = reader;
		this.writer = writer;
		this.scorer = scorer;
		this.files = new ArrayList<>();
		this.files.add(new InOut(in, out));
		this.config = config;
	}
	
	public MultipleConfSolverWorker(final InReader<T> reader, final OutWriter<T> writer, final ScoreCalculator<T> scorer, final InOut inOut, V config) {
		super();
		this.solvers = new ArrayList<>();
		this.reader = reader;
		this.writer = writer;
		this.scorer = scorer;
		this.files = new ArrayList<>();
		this.files.add(inOut);
		this.config = config;
	}
	
	public void addSolver(final ConfigSolver<T, V> solver) {
		this.solvers.add(solver);
	}
	
	public void addSolvers(final List<ConfigSolver<T, V>> solvers) {
		for (ConfigSolver<T, V> s : solvers) {
			this.solvers.add(s);
		}
	}
	
	@Override
	protected Builder buildBar() {
		return ProgressBar.builder(approxEnd);
	}
	
	public InOut getInOut() {
		return this.files.get(0);
	}
	
	public void setInOut(final InOut inOut) {
		this.files = new ArrayList<>();
		this.files.add(inOut);
	}
}
