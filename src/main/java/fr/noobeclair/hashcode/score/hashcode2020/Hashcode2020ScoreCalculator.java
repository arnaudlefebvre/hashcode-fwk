package fr.noobeclair.hashcode.score.hashcode2020;

import java.math.BigDecimal;

import fr.noobeclair.hashcode.bean.hashcode2020.HashCode2020BeanContainer;
import fr.noobeclair.hashcode.score.ScoreCalculator;
import fr.noobeclair.hashcode.utils.dto.SolverResultDto;

public class Hashcode2020ScoreCalculator extends ScoreCalculator<HashCode2020BeanContainer> {
	
	public Hashcode2020ScoreCalculator() {
		
	}
	
	@Override
	protected SolverResultDto run(HashCode2020BeanContainer in, SolverResultDto currentResult) {
		BigDecimal result = new BigDecimal("0");
		// TODO : calculer le score
		currentResult.setScore(result);
		return currentResult;
	}

}
