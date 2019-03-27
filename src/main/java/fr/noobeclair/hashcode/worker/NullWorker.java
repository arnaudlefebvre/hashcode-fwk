package fr.noobeclair.hashcode.worker;

import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.out.OutWriter;
import fr.noobeclair.hashcode.solve.ConfigSolver;
import fr.noobeclair.hashcode.utils.dto.WorkerResultDto;

public class NullWorker extends GenericConfWorker {
	
	protected NullWorker(InReader reader, ConfigSolver solver, OutWriter writer) {
		super(reader, solver, writer);
		
	}
	
	@Override
	public WorkerResultDto run() {
		
		return new WorkerResultDto();
	}
	
}
