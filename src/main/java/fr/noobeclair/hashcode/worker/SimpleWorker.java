package fr.noobeclair.hashcode.worker;

import java.math.BigDecimal;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.out.OutWriter;
import fr.noobeclair.hashcode.score.ScoreCalculator;
import fr.noobeclair.hashcode.solve.Solver;

public class SimpleWorker<T extends BeanContainer> extends GenericWorker<T> {
	
	protected InOut inOut;
	
	public SimpleWorker(InReader<T> reader, Solver<T> solver, OutWriter<T> writer, String in, String out) {
		super(reader, solver, writer);
		this.inOut = new InOut(in, out);		
	}
	
	public SimpleWorker(InReader<T> reader, Solver<T> solver, ScoreCalculator<T> scorer,OutWriter<T> writer, String in, String out) {
		super(reader, solver,scorer, writer);
		this.inOut = new InOut(in, out);		
	}	
	
	public SimpleWorker(InReader<T> reader, Solver<T> solver, OutWriter<T> writer, InOut inOut) {
		super(reader, solver, writer);
		this.inOut = inOut;		
	}
	
	public SimpleWorker(InReader<T> reader, Solver<T> solver, ScoreCalculator<T> scorer,OutWriter<T> writer, InOut inOut) {
		super(reader, solver,scorer, writer);
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

	public void setInOut(InOut inOut) {
		this.inOut = inOut;
	}
	
}
