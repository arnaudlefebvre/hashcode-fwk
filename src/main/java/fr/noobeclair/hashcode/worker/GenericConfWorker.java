package fr.noobeclair.hashcode.worker;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.bean.config.Config;
import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.out.OutWriter;
import fr.noobeclair.hashcode.score.ScoreCalculator;
import fr.noobeclair.hashcode.solve.ConfigSolver;
import fr.noobeclair.hashcode.utils.dto.WorkerResultDto;

/**
 * Abstract Generic Configurable Worker that encapsulate :
 * 1 reader/writer
 * 1 solver
 * 1 optionnal solver
 * 
 * @author arnaud
 *
 * @param <T
 *            extends BeanContainer>
 * @param <
 *            V extends Config>
 */
public abstract class GenericConfWorker<T extends BeanContainer, V extends Config> extends GenericWorker<T> {
	
	protected OutWriter<T> writer;
	protected InReader<T> reader;
	protected ConfigSolver<T, V> solver;
	protected ScoreCalculator<T> scorer;
	
	public GenericConfWorker(InReader<T> reader, ConfigSolver<T, V> solver, OutWriter<T> writer) {
		super(reader, null, writer);
		this.solver = solver;
		
	}
	
	protected GenericConfWorker(InReader<T> reader, ConfigSolver<T, V> solver, ScoreCalculator<T> scorer, OutWriter<T> writer) {
		super(reader, null, writer);
		this.solver = solver;
		this.scorer = scorer;
		
	}
	
	@Override
	public abstract WorkerResultDto run();
	
	@Override
	public OutWriter<T> getWriter() {
		return writer;
	}
	
	@Override
	public void setWriter(OutWriter<T> writer) {
		this.writer = writer;
	}
	
	@Override
	public InReader<T> getReader() {
		return reader;
	}
	
	@Override
	public void setReader(InReader<T> reader) {
		this.reader = reader;
	}
	
	@Override
	public ConfigSolver<T, V> getSolver() {
		return solver;
	}
	
	public void setSolver(ConfigSolver<T, V> solver) {
		this.solver = solver;
	}
	
	@Override
	public ScoreCalculator<T> getScorer() {
		return scorer;
	}
	
	@Override
	public void setScorer(ScoreCalculator<T> scorer) {
		this.scorer = scorer;
	}
	
}