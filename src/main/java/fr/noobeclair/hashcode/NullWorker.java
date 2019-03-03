package fr.noobeclair.hashcode;

import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.out.OutWriter;
import fr.noobeclair.hashcode.solve.Solver;

public class NullWorker extends GenericWorker {

	protected NullWorker(OutWriter writer, InReader reader, Solver solver) {
		super(writer, reader, solver);
	}

	@Override
	public Boolean configure(String[] args) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run() {
		
		this.writer.write(this.solver.run(this.reader.read(null)),null);
		
	}

}
