package fr.noobeclair.hashcode.score;

import java.math.BigDecimal;

import fr.noobeclair.hashcode.bean.CustomBeanContainer;
import fr.noobeclair.hashcode.utils.dto.SolverResultDto;

public class CustomScoreCalculator extends ScoreCalculator<CustomBeanContainer> {
	
	public CustomScoreCalculator() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected SolverResultDto run(CustomBeanContainer in, SolverResultDto currentResult) {
		currentResult.setScore(BigDecimal.ZERO);
		return currentResult;
	}
	
}
