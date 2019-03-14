package fr.noobeclair.hashcode;

import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.out.OutWriter;
import fr.noobeclair.hashcode.solve.Solver;

public class SimpleWorker extends GenericWorker {
	
	protected SimpleWorker(InReader reader, Solver solver, OutWriter writer, String in, String out) {
		super(reader, solver, writer);
		this.in = in;
		this.out = out;
	}
	
	protected String in;
	protected String out;
	
	@Override
	public Boolean configure(String[] args) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void run() {
		this.writer.write(this.solver.solve(this.reader.read(in)), out);
		
	}
	
	public void runSteps() {
		this.writer.write(this.solver.solveSteps(this.reader.read(in)), out);
		
	}
	
}
