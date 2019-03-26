package fr.noobeclair.hashcode.worker;

import java.math.BigDecimal;

import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.out.OutWriter;
import fr.noobeclair.hashcode.solve.ConfigSolver;

public class NullWorker extends GenericConfWorker {
	
	protected NullWorker(InReader reader, ConfigSolver solver, OutWriter writer) {
		super(reader, solver, writer);
		
	}
	
	@Override
	public BigDecimal run() {
		
		return BigDecimal.ZERO;
	}
	
}
