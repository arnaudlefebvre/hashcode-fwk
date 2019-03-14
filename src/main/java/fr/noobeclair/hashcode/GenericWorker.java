package fr.noobeclair.hashcode;

import java.util.List;

import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.out.OutWriter;
import fr.noobeclair.hashcode.solve.Solver;

public abstract class GenericWorker implements Runnable {
	
	
	//Beware, maybe only solve needs to be multi threaded, first we assume that no...
	protected OutWriter writer;
	protected InReader reader;
	protected Solver solver;

	protected GenericWorker (InReader reader, Solver solver, OutWriter writer) {
		this.reader = reader;
		this.writer = writer;
		this.solver = solver;
				
	}

	public abstract Boolean configure(String[] args) throws Exception;

	public abstract void run();
	
	

}