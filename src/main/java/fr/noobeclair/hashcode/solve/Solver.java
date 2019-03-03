package fr.noobeclair.hashcode.solve;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.utils.Utils;

public abstract class Solver {
	
	private static final Logger logger = LogManager.getLogger(InReader.class);
	
	private String name;
	
	private Solver() {
		//useless constructor
	}
	
	public BeanContainer solve(BeanContainer data) {
		long start = System.currentTimeMillis();
		logger.debug("-- Solve start : {}",name);
		try {
			return run(data);
		} finally {
			logger.debug("--Solve End. Total Time : {}s --",Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
		}
	}
	
	public abstract BeanContainer run(BeanContainer data);

}
