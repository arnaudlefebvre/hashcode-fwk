package fr.noobeclair.hashcode.worker;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.bean.Config;
import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.out.OutWriter;
import fr.noobeclair.hashcode.score.ScoreCalculator;
import fr.noobeclair.hashcode.solve.ConfigSolver;
import fr.noobeclair.hashcode.utils.dto.WorkerResultDto;

public abstract class GenericConfWorker<T extends BeanContainer, V extends Config> {
	
	protected OutWriter<T> writer;
	protected InReader<T> reader;
	protected ConfigSolver<T, V> solver;
	protected ScoreCalculator<T> scorer;
	
	protected GenericConfWorker(InReader<T> reader, ConfigSolver<T, V> solver, OutWriter<T> writer) {
		this.reader = reader;
		this.writer = writer;
		this.solver = solver;
		
	}
	
	protected GenericConfWorker(InReader<T> reader, ConfigSolver<T, V> solver, ScoreCalculator<T> scorer, OutWriter<T> writer) {
		this.reader = reader;
		this.writer = writer;
		this.solver = solver;
		this.scorer = scorer;
		
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
	
	public ConfigSolver<T, V> getSolver() {
		return solver;
	}
	
	public void setSolver(ConfigSolver<T, V> solver) {
		this.solver = solver;
	}
	
	public ScoreCalculator<T> getScorer() {
		return scorer;
	}
	
	public void setScorer(ScoreCalculator<T> scorer) {
		this.scorer = scorer;
	}
	
}