package fr.noobeclair.hashcode.worker;

import java.math.BigDecimal;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.bean.Config;
import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.out.OutWriter;
import fr.noobeclair.hashcode.score.ScoreCalculator;
import fr.noobeclair.hashcode.solve.Solver;

public class SimpleWorker<T extends BeanContainer, V extends Config> extends GenericWorker<T, V> {
	
	protected InOut inOut;
	
	public SimpleWorker(final InReader<T> reader, final Solver<T, V> solver, final OutWriter<T> writer, final String in, final String out) {
		super(reader, solver, writer);
		this.inOut = new InOut(in, out);
	}
	
	public SimpleWorker(final InReader<T> reader, final Solver<T, V> solver, final ScoreCalculator<T> scorer, final OutWriter<T> writer, final String in, final String out) {
		super(reader, solver, scorer, writer);
		this.inOut = new InOut(in, out);
	}
	
	public SimpleWorker(final InReader<T> reader, final Solver<T, V> solver, final OutWriter<T> writer, final InOut inOut) {
		super(reader, solver, writer);
		this.inOut = inOut;
	}
	
	public SimpleWorker(final InReader<T> reader, final Solver<T, V> solver, final ScoreCalculator<T> scorer, final OutWriter<T> writer, final InOut inOut) {
		super(reader, solver, scorer, writer);
		this.inOut = inOut;
	}
	
	@Override
	public BigDecimal run() {
		if (scorer == null) {
			this.writer.write(this.solver.solve(this.reader.read(inOut.in)), inOut.out);
			return BigDecimal.ZERO;
		} else {
			return this.scorer.score(this.writer.write(this.solver.solve(this.reader.read(inOut.in)), inOut.out));
		}
		
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
		builder.append("SimpleWorker [");
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
	
}
