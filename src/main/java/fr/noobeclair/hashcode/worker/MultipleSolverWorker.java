package fr.noobeclair.hashcode.worker;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.out.OutWriter;
import fr.noobeclair.hashcode.score.ScoreCalculator;
import fr.noobeclair.hashcode.solve.Solver;

//This class is only a list of SimpleWorker that are run one after the other and return a map of solver-score
public class MultipleSolverWorker<T extends BeanContainer> extends MultipleWorker<T>  {
	
	protected List<SimpleWorker<T>> solvers;
	protected InOut inOut;
	
	public MultipleSolverWorker(InReader<T> reader, OutWriter<T> writer, ScoreCalculator<T> scorer, String in, String out) {
		super();
		this.solvers = new ArrayList<SimpleWorker<T>>();
		this.reader = reader;
		this.writer = writer;
		this.scorer = scorer;
		this.inOut = new InOut(in, out);
	}
	
	public MultipleSolverWorker(InReader<T> reader, OutWriter<T> writer, ScoreCalculator<T> scorer, InOut inOut) {
		super();
		this.solvers = new ArrayList<SimpleWorker<T>>();
		this.reader = reader;
		this.writer = writer;
		this.scorer = scorer;
		this.inOut = inOut;
	}
	
	public void addSolver(Solver<T> solver) {
		this.solvers.add(new SimpleWorker<T>(reader, solver, scorer, writer, inOut.in, inOut.out+"-"+solver.getClass()));
	}
	
	@Override
	public Map<String, BigDecimal> run() {
		Map<String, BigDecimal> result = new TreeMap<String, BigDecimal>();
		for (SimpleWorker<T> sw : solvers) {
			try {
				result.put(sw.getSolver().getClass().getSimpleName(),sw.run());
			} catch (RuntimeException e) {
				logger.error("Something went wrong running this worker : {}",e.getMessage());
				result.put(sw.getSolver().getClass().getSimpleName(), BigDecimal.ZERO);
			}
		}
		if (solvers.isEmpty()) {
			logger.error("No solver - No run ... !");
		}
		return result;
	}

	public List<SimpleWorker<T>> getSolvers() {
		return solvers;
	}

	public void setSolvers(List<SimpleWorker<T>> solvers) {
		this.solvers = solvers;
	}

	public InOut getInOut() {
		return inOut;
	}

	public void setInOut(InOut inOut) {
		this.inOut = inOut;
	}

	

}
