package fr.noobeclair.hashcode.solve;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.interfaces.Step;
import fr.noobeclair.hashcode.utils.Utils;

public abstract class AbstractStep<T extends BeanContainer> implements Step<T> {
	
	@Override
	public T run(T solver) {
		long start = System.currentTimeMillis();
		logger.info("-- AbstractStep {} start : {}", id);
		try {
			return runStep(solver);
		} finally {
			logger.info("--AbstractStep {} End. Total Time : {}s --", id, Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
		}
	}
	
	protected static final Logger logger = LogManager.getLogger(AbstractStep.class);
	protected String id;
	protected T solver;
	
	public AbstractStep(String id, T solver) {
		super();
		this.id = id;
		this.solver = solver;
	}
	
	protected abstract T runStep(T solver);
	
	protected void halt(Long milli) {
		if (milli > 0L) {
			try {
				Thread.sleep(milli);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
	}
	
}
