package fr.noobeclair.hashcode.worker;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.solve.Solver;

public abstract class MultipleWorker<T extends BeanContainer, S extends Solver<T>> extends AbstractMultipleWorker<T> {
	
	protected static final Logger logger = LogManager.getLogger(MultipleWorker.class);
	protected List<Solver<T>> solvers;
	
	public MultipleWorker() {
		
	}
	
	@Override
	protected void prepare() {
		throw new UnsupportedOperationException("Not supported yet");
	}
	
	@Override
	protected Map<String, BigDecimal> solve() {
		Map<String, BigDecimal> result = new TreeMap<>();
		if (1 == this.execOrder) {
			result = runSolverFirst();
		} else {
			result = runFileFirst();
		}
		if (files.isEmpty()) {
			logger.error(" <###----- !!!!!! -----#> No file - No run ... !");
		}
		if (solvers.isEmpty()) {
			logger.error(" <###----- !!!!!! -----#> No solver - No run ... !");
		}
		return result;
	}
	
	protected Map<String, BigDecimal> runFileFirst() {
		final Map<String, BigDecimal> result = new TreeMap<>();
		for (final InOut io : files) {
			for (final Solver<T> solver : this.solvers) {
				try {
					result.putAll(runSolverForFile(solver, io));
				} catch (final Exception e) {
					logger.error(" <###----- !!!!!! -----#> Something went wrong running this worker : {}", solver.getName(), e);
					result.put(solver.getName() + ":" + solver.getAdditionnalInfo() + "--" + io.in, BigDecimal.ZERO);
				}
			}
		}
		return result;
	}
	
	protected Map<String, BigDecimal> runSolverFirst() {
		final Map<String, BigDecimal> result = new TreeMap<>();
		for (final Solver<T> solver : this.solvers) {
			for (final InOut io : files) {
				try {
					result.putAll(runSolverForFile(solver, io));
				} catch (final Exception e) {
					logger.error(" <###----- !!!!!! -----#> Something went wrong running this worker : {}", solver.getName(), e);
					result.put(solver.getName() + ":" + solver.getAdditionnalInfo() + "--" + io.in, BigDecimal.ZERO);
				}
			}
		}
		return result;
	}
	
}
