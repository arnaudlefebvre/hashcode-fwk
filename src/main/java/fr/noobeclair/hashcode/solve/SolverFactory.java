package fr.noobeclair.hashcode.solve;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.bean.Config;

public class SolverFactory<S extends Solver<T, V>, T extends BeanContainer, V extends Config> {
	
	protected static final Logger logger = LogManager.getLogger(SolverFactory.class);
	
	private Class<S> solverClass;
	private Class<T> beanClass;
	private Class<V> confClass;
	
	public SolverFactory(Class<S> solverClass, Class<T> beanClass, Class<V> confClass) {
		super();
		this.solverClass = solverClass;
		this.beanClass = beanClass;
		this.confClass = confClass;
	}
	
	public List<S> createFromConfs(List<V> configs) {
		return createFromConfs(configs, Solver.DISABLE_TIMEOUT);
	}
	
	public List<S> createFromConfs(List<V> configs, long timeout) {
		List<S> res = new ArrayList<>();
		try {
			for (V c : configs) {
				S solver = solverClass.newInstance();
				solver.build(c, timeout);
				res.add(solver);
			}
			return res;
		} catch (Exception e) {
			logger.error("Erreur while instanciating a solver ", e);
			return res;
		}
	}
}
