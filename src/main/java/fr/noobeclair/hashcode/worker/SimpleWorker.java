package fr.noobeclair.hashcode.worker;

import org.apache.commons.lang3.StringUtils;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.out.OutWriter;
import fr.noobeclair.hashcode.score.ScoreCalculator;
import fr.noobeclair.hashcode.solve.Solver;
import fr.noobeclair.hashcode.utils.dto.WorkerResultDto;

/**
 * Simple Worker that handles and runs :
 * 1 reader/writer
 * 1 solver
 * 1 optionnal solver
 * 
 * @author arnaud
 *
 * @param <T
 *            extends BeanContainer>
 */
public class SimpleWorker<T extends BeanContainer> extends GenericWorker<T> {
	
	protected InOut inOut;
	
	public SimpleWorker(final InReader<T> reader, final Solver<T> solver, final OutWriter<T> writer, final String in, final String out) {
		super(reader, solver, writer);
		this.inOut = new InOut(in, out);
	}
	
	public SimpleWorker(final InReader<T> reader, final Solver<T> solver, final ScoreCalculator<T> scorer, final OutWriter<T> writer, final String in, final String out) {
		super(reader, solver, scorer, writer);
		this.inOut = new InOut(in, out);
	}
	
	public SimpleWorker(final InReader<T> reader, final Solver<T> solver, final OutWriter<T> writer, final InOut inOut) {
		super(reader, solver, writer);
		this.inOut = inOut;
	}
	
	public SimpleWorker(final InReader<T> reader, final Solver<T> solver, final ScoreCalculator<T> scorer, final OutWriter<T> writer, final InOut inOut) {
		super(reader, solver, scorer, writer);
		this.inOut = inOut;
	}
	
	@Override
	public WorkerResultDto run() {
		WorkerResultDto result = new WorkerResultDto();
		if (scorer == null) {
			this.writer.write(this.solver.solve(this.reader.read(inOut.in)), getOut(this.solver));
			result.addResult(this.solver.getResultInfo());
		} else {
			result.addResult(this.scorer.score(this.writer.write(this.solver.solve(this.reader.read(inOut.in)), getOut(this.solver)), this.solver.getResultInfo()));
		}
		return result;
	}
	
	public InOut getInOut() {
		return inOut;
	}
	
	public void setInOut(final InOut inOut) {
		this.inOut = inOut;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("SimpleConfWorker [");
		if (solver != null) {
			builder.append("solver=").append(solver).append(", ");
		}
		if (inOut != null) {
			builder.append("inOut=").append(inOut).append(", ");
		}
		if (writer != null) {
			builder.append("writer=").append(writer).append(", ");
		}
		if (reader != null) {
			builder.append("reader=").append(reader).append(", ");
		}
		
		if (scorer != null) {
			builder.append("scorer=").append(scorer);
		}
		builder.append("]");
		return builder.toString();
	}
	
	protected String getOut(final Solver<T> solver) {
		if (StringUtils.isNotEmpty(inOut.out)) {
			return inOut.out + "#" + solver.getName() + ".out";
		}
		return inOut.in + "#" + solver.getName() + ".out";
		
	}
}
