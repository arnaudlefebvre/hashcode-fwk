package fr.noobeclair.hashcode.score.hashcode2018;

import java.math.BigDecimal;

import fr.noobeclair.hashcode.bean.hashcode2018.H2018BeanContainer;
import fr.noobeclair.hashcode.score.ScoreCalculator;

public class H2018ScoreCalculator extends ScoreCalculator<H2018BeanContainer> {
	
	@Override
	protected BigDecimal run(H2018BeanContainer in) {
		if (in != null) {
			return new BigDecimal(in.score);
		}
		return BigDecimal.ZERO;
	}
	
}
