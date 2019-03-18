package fr.noobeclair.hashcode.score.hashcode2019;

import java.math.BigDecimal;

import fr.noobeclair.hashcode.bean.hashcode2019.HashCode2019BeanContainer;
import fr.noobeclair.hashcode.bean.hashcode2019.Slide;
import fr.noobeclair.hashcode.score.ScoreCalculator;

public class Hashcode2019ScoreCalculator extends ScoreCalculator<HashCode2019BeanContainer> {

	public Hashcode2019ScoreCalculator() {
		
	}

	@Override
	protected BigDecimal run(HashCode2019BeanContainer in) {
		BigDecimal result = new BigDecimal("0");
		Slide last = null;
		for (Slide s : in.getSlideshow().getSlides()) {
			if (last != null) {
				result = result.add(last.score(s));
			}
			last = s;
		}
		return result;
	}

}
