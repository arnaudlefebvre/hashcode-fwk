package fr.noobeclair.hashcode.worker;

import java.math.BigDecimal;

import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.out.OutWriter;
import fr.noobeclair.hashcode.solve.Solver;

public class NullWorker extends GenericWorker {
	
	protected NullWorker(InReader reader, Solver solver, OutWriter writer) {
		super(reader, solver, writer);
		
	}
	
	@Override
	public BigDecimal run() {
		
		this.writer.write(this.solver.solve(this.reader.read(null)), null);
		return BigDecimal.ZERO;
	}
	
}
