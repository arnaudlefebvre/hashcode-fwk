package fr.noobeclair.hashcode;

import java.util.List;

import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.out.OutWriter;
import fr.noobeclair.hashcode.solve.Solver;

public abstract class GenericWorker implements Runnable {
	
	private OutWriter writer;
	private InReader reader;
	private Solver solver;

	private GenericWorker(OutWriter writer, InReader reader, Solver solver) {
		this.reader = reader;
		this.writer = writer;
		this.solver = solver;
				
	}

	public abstract Boolean configure(String[] args) throws Exception;

	public abstract void run();
	
	

}