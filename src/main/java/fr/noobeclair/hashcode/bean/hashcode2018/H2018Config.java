package fr.noobeclair.hashcode.bean.hashcode2018;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import fr.noobeclair.hashcode.annotation.ConfGenerable;
import fr.noobeclair.hashcode.annotation.ConfStratEnabled;
import fr.noobeclair.hashcode.annotation.ConfStrategy;
import fr.noobeclair.hashcode.annotation.CsvExport;
import fr.noobeclair.hashcode.annotation.CsvField;
import fr.noobeclair.hashcode.bean.config.AbstractFactory.TYPE;
import fr.noobeclair.hashcode.bean.config.Config;

// ConfStrategies = { @ConfStrategies (name, fiels = {@ConfField {min, max,
// step, excludes})}
@ConfStratEnabled(enabled = { "LONG" })
@CsvExport
public class H2018Config extends Config {
	
	@ConfStrategy(id = "LONG")
	@ConfGenerable(type = TYPE.DOUBLE, min = "1", max = "2", step = "1")
	@CsvField
	protected Double timeToFinishCoef;
	
	@ConfStrategy(id = "LONG")
	// @ConfGenerable(type = TYPE.BOOLEAN)
	@CsvField
	protected Boolean addQuickStrat = true;
	
	@ConfStrategy(id = "NEAR", includes = { "NEAR_FIRST" })
	@ConfStrategy(id = "NEAR_EXP", includes = { "NEAR_FIRST" })
	@ConfStrategy(id = "LONG", includes = { "LONG_FIRST" })
	@ConfStrategy(id = "LONG_EXP", includes = { "LONG_FIRST" })
	@ConfStrategy(id = "QUICK", includes = { "QUICK_FIRST" })
	@ConfGenerable(type = TYPE.ENUM, eClass = CarStrategy.class, excludes = { "AGGRESSIVE", "QUICK_FIRST" })
	@CsvField
	protected CarStrategy carStrategy;
	
	protected List<CarStrategy> carStrategies;
	
	public enum CarStrategy {
		AGGRESSIVE, // all below
		NEAR_FIRST, // prefer nearest trip with longest distance
		LONG_FIRST, // prefer longest trip with nearest distance
		QUICK_FIRST, // may prefer trips that ends shortly when approching end
	}
	
	public enum AdjustMethod {
		EXP, LN, LINEAR, POW, INV
	}
	
	@ConfStrategy(id = "NEAR", includes = { "LINEAR", "INV" })
	@ConfStrategy(id = "NEAR_EXP", includes = { "EXP" })
	@ConfStrategy(id = "LONG", includes = { "LINEAR", "INV" })
	//@ConfStrategy(id = "LONG", includes = { "LINEAR" })
	@ConfStrategy(id = "LONG_EXP", includes = { "EXP" })
	@ConfStrategy(id = "QUICK")
	@ConfGenerable(type = TYPE.ENUM, eClass = AdjustMethod.class, excludes = { "LN", "POW" })
	@CsvField
	// Méthode pour ajustement coup de transport avant le début de la course
	protected AdjustMethod nearTravelAdjustFct;
	@ConfStrategy(id = "NEAR", includes = { "LINEAR", "INV" })
	@ConfStrategy(id = "NEAR_EXP", includes = { "EXP" })
	@ConfStrategy(id = "LONG", includes = { "LINEAR", "INV" })
	//@ConfStrategy(id = "LONG", includes = { "LINEAR" })
	@ConfStrategy(id = "LONG_EXP", includes = { "LINEAR", "INV" })
	@ConfStrategy(id = "QUICK")
	@ConfGenerable(type = TYPE.ENUM, eClass = AdjustMethod.class, excludes = { "LN", "POW" })
	@CsvField
	// Méthode pour ajustement gain de la course
	protected AdjustMethod nearDistAdjustFct;
	
	@ConfStrategy(id = "NEAR", min = "1", max = "31", step = "10")
	@ConfStrategy(id = "NEAR_EXP", min = "1", max = "2", step = "1")
	@ConfStrategy(id = "LONG", min = "0.25", max = "1.5", step = "0.25")
	@ConfStrategy(id = "LONG_EXP", min = "1", max = "2", step = "1")
	@ConfStrategy(id = "QUICK")
	@ConfGenerable(type = TYPE.DOUBLE, min = "0", max = "1", step = "1")
	@CsvField
	// coef pour l'ajustement du coup de transport avant le début de la course (sauf exp, pow, ln)
	protected Double nearATravelMethodCst;
	// @ConfStrategy(id = "NEAR")
	@ConfStrategy(id = "NEAR_EXP")
	@ConfStrategy(id = "LONG")
	@ConfStrategy(id = "LONG_EXP")
	@ConfStrategy(id = "QUICK")
	@ConfGenerable(type = TYPE.DOUBLE, min = "0", max = "0", step = "1")
	@CsvField
	// constante poru ajustement du coup de transport
	protected Double nearBTravelMethodCst;
	// @ConfStrategy(id = "NEAR", min = "1", max = "31", step = "10")
	@ConfStrategy(id = "NEAR_EXP", min = "1", max = "2", step = "1")
	@ConfStrategy(id = "LONG", min = "0.5", max = "2.5", step = "1")
	@ConfStrategy(id = "LONG_EXP", min = "1", max = "3", step = "1")
	@ConfStrategy(id = "QUICK")
	@ConfGenerable(type = TYPE.DOUBLE, min = "1", max = "16", step = "5")
	@CsvField
	// coef pour l'ajustement du gain (sauf exp, pow, ln)
	protected Double nearADistMethodCst;
	// @ConfStrategy(id = "NEAR")
	@ConfStrategy(id = "NEAR_EXP")
	@ConfStrategy(id = "LONG")
	@ConfStrategy(id = "LONG_EXP")
	@ConfStrategy(id = "QUICK")
	@ConfGenerable(type = TYPE.DOUBLE, min = "0", max = "0", step = "1")
	@CsvField
	// constant pour l'ajustement du gain
	protected Double nearBDistMethodCst;
	
	public H2018Config() {
		this.timeToFinishCoef = 1D;
		this.nearTravelAdjustFct = AdjustMethod.EXP;
		this.nearATravelMethodCst = 1D;
		this.nearBTravelMethodCst = 0D;
		this.nearDistAdjustFct = AdjustMethod.EXP;
		this.nearADistMethodCst = 1D;
		this.nearBDistMethodCst = 0D;
		this.carStrategies = Arrays.asList(CarStrategy.NEAR_FIRST);
	}
	
	public H2018Config(Double coefBonus, Double nearBonus, Double distanceBonus) {
		this.timeToFinishCoef = 1D;
		this.nearTravelAdjustFct = AdjustMethod.EXP;
		this.nearATravelMethodCst = 1D;
		this.nearBTravelMethodCst = 0D;
		this.nearDistAdjustFct = AdjustMethod.EXP;
		this.nearADistMethodCst = 1D;
		this.nearBDistMethodCst = 0D;
		this.carStrategies = new ArrayList<>();
	}
	
	public H2018Config(Double timeToFinishCoef, AdjustMethod nearTravelAdjustFct, AdjustMethod nearDistAdjustFct,
			AdjustMethod longTravelAdjustFct, AdjustMethod longDistAdjustFct, Double nearATravelMethodCst,
			Double nearBTravelMethodCst, Double nearADistMethodCst, Double nearBDistMethodCst,
			Double longATravelMethodCst, Double longBTravelMethodCst, Double longADistMethodCst,
			Double longBDistMethodCst, List<CarStrategy> carStrategies) {
		this.timeToFinishCoef = timeToFinishCoef;
		this.nearTravelAdjustFct = nearTravelAdjustFct;
		this.nearDistAdjustFct = nearDistAdjustFct;
		this.nearATravelMethodCst = nearATravelMethodCst;
		this.nearBTravelMethodCst = nearBTravelMethodCst;
		this.nearADistMethodCst = nearADistMethodCst;
		this.nearBDistMethodCst = nearBDistMethodCst;
		this.carStrategies = carStrategies;
	}
	
	public Double adjustNearTravel(Double t) {
		if (CollectionUtils.containsAny(getCarStrategies(),
				Arrays.asList(CarStrategy.AGGRESSIVE, CarStrategy.NEAR_FIRST))) {
			switch (nearTravelAdjustFct) {
			case EXP:
				return Math.exp(t + nearBTravelMethodCst);
			case LN:
				return Math.log(t + nearBTravelMethodCst);
			case LINEAR:
				return nearATravelMethodCst * t + nearBTravelMethodCst;
			case POW:
				return Math.pow(t, nearATravelMethodCst);
			case INV:
				return 1 / (nearATravelMethodCst * t + nearBTravelMethodCst);
			default:
				return t;
			}
		}
		return t;
	}
	
	public Double adjustNearDist(Double t) {
		if (CollectionUtils.containsAny(getCarStrategies(),
				Arrays.asList(CarStrategy.AGGRESSIVE, CarStrategy.NEAR_FIRST))) {
			switch (nearDistAdjustFct) {
			case EXP:
				return Math.exp(t + nearBDistMethodCst);
			case LN:
				return Math.log(t + nearBDistMethodCst);
			case LINEAR:
				return nearADistMethodCst * t + nearBDistMethodCst;
			case POW:
				return Math.pow(t, nearADistMethodCst);
			case INV:
				return 1 / (nearADistMethodCst * t + nearBDistMethodCst);
			default:
				return t;
			}
		}
		return t;
	}
	
	public Double adjustLongTravel(Double t) {
		if (CollectionUtils.containsAny(getCarStrategies(),
				Arrays.asList(CarStrategy.AGGRESSIVE, CarStrategy.NEAR_FIRST))) {
			switch (nearTravelAdjustFct) {
			case EXP:
				return Math.exp(t + nearBTravelMethodCst);
			case LN:
				return Math.log(t + nearBTravelMethodCst);
			case LINEAR:
				return nearATravelMethodCst * t + nearBTravelMethodCst;
			case POW:
				return Math.pow(t, nearATravelMethodCst);
			case INV:
				return 1 / (nearATravelMethodCst * t + nearBTravelMethodCst);
			default:
				return t;
			}
		}
		return t;
	}
	
	public Double adjustLongDistance(Double t) {
		if (CollectionUtils.containsAny(getCarStrategies(),
				Arrays.asList(CarStrategy.AGGRESSIVE, CarStrategy.LONG_FIRST))) {
			switch (nearDistAdjustFct) {
			case EXP:
				return Math.exp(t + nearBDistMethodCst);
			case LN:
				return Math.log(t + nearBDistMethodCst);
			case LINEAR:
				return nearADistMethodCst * t + nearBDistMethodCst;
			case POW:
				return Math.pow(t, nearADistMethodCst);
			case INV:
				return 1 / (nearADistMethodCst * t + nearBDistMethodCst);
			default:
				return t;
			}
		}
		return t;
	}
	
	public Double adjustTimeWhenFinishRatio(Double t, Double d, Integer turn, Integer maxturn, Integer tripst) {
		Double ratio = 0D;
		if (CollectionUtils.containsAny(carStrategies, Arrays.asList(CarStrategy.AGGRESSIVE, CarStrategy.LONG_FIRST))
				|| addQuickStrat) {
			Double diff = maxturn - (t + d + turn);
			if (diff > 0) {
				ratio = diff * timeToFinishCoef;
			}
			//			diff = tripst - (t + turn);
			//			Double res = (t + turn);
			//			if (diff > 0) {
			//				if (ratio > 0) {
			//					ratio = ratio * (1.0 - (res / maxturn));
			//				} else {
			//					ratio = (1.0 - (res / maxturn));
			//				}
			//			}
		}
		return ratio;
	}
	
	public String toString2() {
		StringBuilder builder = new StringBuilder();
		builder.append("Conf[ttfc=");
		builder.append(timeToFinishCoef);
		builder.append(",ntfct=");
		builder.append(nearTravelAdjustFct);
		// builder.append(",ltfct=");
		// builder.append(longTravelAdjustFct);
		builder.append(",ndfct=");
		builder.append(nearDistAdjustFct);
		// builder.append(",ldfct=");
		// builder.append(longDistAdjustFct);
		builder.append(",nAt=");
		builder.append(nearATravelMethodCst);
		builder.append(",nBt=");
		builder.append(nearBTravelMethodCst);
		builder.append(",nAd=");
		builder.append(nearADistMethodCst);
		builder.append(",nBd=");
		builder.append(nearBDistMethodCst);
		// builder.append(",lAt=");
		// builder.append(longATravelMethodCst);
		// builder.append(",lBt=");
		// builder.append(longBTravelMethodCst);
		// builder.append(",lAd=");
		// builder.append(longADistMethodCst);
		// builder.append(",lBd=");
		// builder.append(longBDistMethodCst);
		if (CollectionUtils.isNotEmpty(getCarStrategies())) {
			builder.append(",st=");
			builder.append(Arrays.toString(getCarStrategies().toArray()));
		}
		builder.append("]");
		return builder.toString();
	}
	
	public Double getTimeToFinishCoef() {
		return timeToFinishCoef;
	}
	
	public AdjustMethod getNearTravelAdjustFct() {
		return nearTravelAdjustFct;
	}
	
	// public AdjustMethod getLongTravelAdjustFct() {
	// return longTravelAdjustFct;
	// }
	
	public AdjustMethod getNearDistAdjustFct() {
		return nearDistAdjustFct;
	}
	//
	// public AdjustMethod getLongDistAdjustFct() {
	// return longDistAdjustFct;
	// }
	
	public Double getNearATravelMethodCst() {
		return nearATravelMethodCst;
	}
	
	public Double getNearBTravelMethodCst() {
		return nearBTravelMethodCst;
	}
	
	public Double getNearADistMethodCst() {
		return nearADistMethodCst;
	}
	
	public Double getNearBDistMethodCst() {
		return nearBDistMethodCst;
	}
	
	public List<CarStrategy> getCarStrategies() {
		List<CarStrategy> res = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(this.carStrategies)) {
			res.addAll(this.carStrategies);
		}
		if (this.carStrategy != null && CollectionUtils.isNotEmpty(this.carStrategies)
				&& !this.carStrategies.contains(this.carStrategy)) {
			res.add(this.carStrategy);
		}
		return res;
	}
	
	@Override
	protected String showAll() {
		return ReflectionToStringBuilder.toStringExclude(this,
				Arrays.asList("statisticKeysToWriteToCSV", "csvStatsPath", "csvSeparator", "progressBar"));
	};
	
	public CarStrategy getCarStrategy() {
		return carStrategy;
	}
	
}
