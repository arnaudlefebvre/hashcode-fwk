package fr.noobeclair.hashcode.solve;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.interfaces.Step;
import fr.noobeclair.hashcode.utils.Utils;

public abstract class AbstractStep implements Step {
	
	protected static final Logger logger = LogManager.getLogger(AbstractStep.class);
	protected String id;
	
	public AbstractStep(String id) {
		super();
		this.id = id;
	}

	protected abstract BeanContainer runStep(BeanContainer data);
	
	public BeanContainer run(BeanContainer data) {
		long start = System.currentTimeMillis();
		logger.info("-- AbstractStep {} start : {}",id);
		try {
			return runStep(data);
		} finally {
			logger.info("--AbstractStep {} End. Total Time : {}s --",id,Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
		}
	}
}
