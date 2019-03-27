package fr.noobeclair.hashcode.score;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.utils.Utils;
import fr.noobeclair.hashcode.utils.dto.SolverResultDto;

public abstract class ScoreCalculator<T extends BeanContainer> {
	
	protected static final Logger logger = LogManager.getLogger(ScoreCalculator.class);
	
	public ScoreCalculator() {
	}
	
	protected abstract SolverResultDto run(T in, SolverResultDto currentResult);
	
	public SolverResultDto score(T in, SolverResultDto currentResult) {
		long start = System.currentTimeMillis();
		logger.debug("-- Score start : ");
		try {
			if (in != null) {
				return run(in, currentResult);
			}
			logger.error(" <###----- !!!!!! -----#> in null !");
			currentResult.setScore(BigDecimal.ZERO);
			return currentResult;
		} finally {
			logger.debug("--Score End. Total Time : {}s --", Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
		}
	}
	
}
