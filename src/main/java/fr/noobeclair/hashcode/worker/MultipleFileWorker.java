package fr.noobeclair.hashcode.worker;

import java.util.ArrayList;
import java.util.List;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.out.OutWriter;
import fr.noobeclair.hashcode.score.ScoreCalculator;
import fr.noobeclair.hashcode.solve.Solver;

public class MultipleFileWorker<T extends BeanContainer, S extends Solver<T>> extends MultipleWorker<T, S> {
	
	public MultipleFileWorker(final InReader<T> reader, final OutWriter<T> writer, final ScoreCalculator<T> scorer, final Solver<T> solver, final List<InOut> files) {
		super();
		this.reader = reader;
		this.writer = writer;
		this.scorer = scorer;
		this.solvers = new ArrayList<>();
		this.solvers.add(solver);
		this.files = files;
	}
	
	public MultipleFileWorker(final InReader<T> reader, final OutWriter<T> writer, final ScoreCalculator<T> scorer, final Solver<T> solver) {
		super();
		this.reader = reader;
		this.writer = writer;
		this.scorer = scorer;
		this.solvers = new ArrayList<>();
		this.solvers.add(solver);
		this.files = new ArrayList<>();
	}
	
	public void addFiles(final List<InOut> files) {
		this.files = files;
	}
	
	public void addFile(final String in, final String out) {
		this.files.add(new InOut(in, out));
	}
	
}
