package fr.noobeclair.hashcode.score;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.utils.Utils;

public abstract class ScoreCalculator<T extends BeanContainer> {
	
	protected static final Logger logger = LogManager.getLogger(ScoreCalculator.class);
	
	public ScoreCalculator() {		
	}
	
	protected abstract BigDecimal run(T in);
	
	public BigDecimal score(T in) {
		
		long start = System.currentTimeMillis();
		logger.info("-- Score start : ");
		try {
			if (in != null) {
				return run(in);
			}
			logger.error(" <###----- !!!!!! -----#> in null !");
			return BigDecimal.ZERO;
		} finally {
			logger.info("--Score End. Total Time : {}s --", Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
		}
	}
	
}
