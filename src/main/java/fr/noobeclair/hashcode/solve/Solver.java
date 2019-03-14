package fr.noobeclair.hashcode.solve;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.utils.Utils;

public abstract class Solver<T extends BeanContainer>  {
	
	protected static final Logger logger = LogManager.getLogger(Solver.class);
	
	protected String name;
	protected T data;
	
	public Solver() {
		
	}
	
	public BeanContainer solve(T data) {
		long start = System.currentTimeMillis();
		logger.info("-- Solve start : {}",name);
		try {
			return run(data);
		} finally {
			logger.info("--Solve End. Total Time : {}s --",Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
		}
	}		
	
	protected abstract T run(T data);
	
	
	public T solveSteps(T data) {
		long start = System.currentTimeMillis();
		logger.info("-- Solve start : {}",name);
		try {
			return runSteps(data);
		} finally {
			logger.info("--Solve End. Total Time : {}s --",Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
		}
	}		
	
	protected abstract T runSteps(T data);

	protected T getData() {
		return data;
	}

	protected void setData(T data) {
		this.data = data;
	}		

}
