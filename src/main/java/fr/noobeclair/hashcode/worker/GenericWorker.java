package fr.noobeclair.hashcode.worker;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.out.OutWriter;
import fr.noobeclair.hashcode.score.ScoreCalculator;
import fr.noobeclair.hashcode.solve.Solver;
import fr.noobeclair.hashcode.utils.dto.WorkerResultDto;

public abstract class GenericWorker<T extends BeanContainer> {
	
	protected OutWriter<T> writer;
	protected InReader<T> reader;
	protected Solver<T> solver;
	protected ScoreCalculator<T> scorer;
	protected T data;
	
	protected GenericWorker(InReader<T> reader, Solver<T> solver, OutWriter<T> writer) {
		this.reader = reader;
		this.writer = writer;
		this.solver = solver;
		this.data = null;
	}
	
	protected GenericWorker(InReader<T> reader, Solver<T> solver, ScoreCalculator<T> scorer, OutWriter<T> writer) {
		this.reader = reader;
		this.writer = writer;
		this.solver = solver;
		this.scorer = scorer;
		this.data = null;
	}
	
	public abstract WorkerResultDto run();
	
	public OutWriter<T> getWriter() {
		return writer;
	}
	
	public void setWriter(OutWriter<T> writer) {
		this.writer = writer;
	}
	
	public InReader<T> getReader() {
		return reader;
	}
	
	public void setReader(InReader<T> reader) {
		this.reader = reader;
	}
	
	public Solver<T> getSolver() {
		return solver;
	}
	
	public void setSolver(Solver<T> solver) {
		this.solver = solver;
	}
	
	public ScoreCalculator<T> getScorer() {
		return scorer;
	}
	
	public void setScorer(ScoreCalculator<T> scorer) {
		this.scorer = scorer;
	}
	
}