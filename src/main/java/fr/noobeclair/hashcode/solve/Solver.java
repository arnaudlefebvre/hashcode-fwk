package fr.noobeclair.hashcode.solve;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.interfaces.Step;
import fr.noobeclair.hashcode.utils.Utils;

public abstract class Solver {
	
	protected static final Logger logger = LogManager.getLogger(Solver.class);
	
	protected String name;
	protected List<Step> steps;
	protected BeanContainer data;
	
	public Solver() {
		this.steps = new ArrayList<>();
	}
	
	public BeanContainer solve(BeanContainer data) {
		long start = System.currentTimeMillis();
		logger.info("-- Solve start : {}",name);
		try {
			return run(data);
		} finally {
			logger.info("--Solve End. Total Time : {}s --",Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
		}
	}		
	
	protected abstract BeanContainer run(BeanContainer data);
	
	
	public BeanContainer solveSteps(BeanContainer data) {
		long start = System.currentTimeMillis();
		logger.info("-- Solve start : {}",name);
		try {
			return runSteps(data);
		} finally {
			logger.info("--Solve End. Total Time : {}s --",Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
		}
	}		
	
	protected abstract BeanContainer runSteps(BeanContainer data);

}
