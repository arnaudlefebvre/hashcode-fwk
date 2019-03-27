package fr.noobeclair.hashcode.score.hashcode2018;

import java.math.BigDecimal;

import fr.noobeclair.hashcode.bean.hashcode2018.H2018BeanContainer;
import fr.noobeclair.hashcode.score.ScoreCalculator;
import fr.noobeclair.hashcode.utils.dto.SolverResultDto;

public class H2018ScoreCalculator extends ScoreCalculator<H2018BeanContainer> {
	
	@Override
	protected SolverResultDto run(H2018BeanContainer in, SolverResultDto currentResult) {
		if (in != null) {
			currentResult.setScore(new BigDecimal(in.score));
		} else {
			currentResult.setScore(BigDecimal.ZERO);
		}
		return currentResult;
	}
	
}
