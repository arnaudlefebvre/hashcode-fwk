package fr.noobeclair.hashcode;

import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.out.OutWriter;
import fr.noobeclair.hashcode.solve.Solver;

public class NullWorker extends GenericWorker {

	protected NullWorker(InReader reader, Solver solver, OutWriter writer) {
		super(reader, solver, writer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Boolean configure(String[] args) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run() {
		
		this.writer.write(this.solver.solve(this.reader.read(null)),null);
		
	}

}
