package fr.noobeclair.hashcode.bean.hashcode2018;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import fr.noobeclair.hashcode.bean.Config;
import fr.noobeclair.hashcode.solve.StatsConstants;

public class H2018Config extends Config {
	
	private static final List<Integer> CSV_CST = Arrays.asList(StatsConstants.CF_STRAT, StatsConstants.SCORE, StatsConstants.TIME_TOTAL, StatsConstants.ITEM0_TOTAL, StatsConstants.IN_0,
			StatsConstants.IN_1,
			StatsConstants.CF_TTFC, StatsConstants.CF_NTFCT, StatsConstants.CF_NDFCT, StatsConstants.CF_LTFCT, StatsConstants.CF_LDFCT, StatsConstants.CF_NAT, StatsConstants.CF_NBT,
			StatsConstants.CF_NAD, StatsConstants.CF_NBD, StatsConstants.CF_LAT, StatsConstants.CF_LBT, StatsConstants.CF_LAD, StatsConstants.CF_LBD);
	private static final String CSV_PATH = "src/main/resources/out/2018/global-stats.csv";
	
	private Double timeToFinishCoef;
	
	private AdjustMethod nearTravelAdjustFct;
	private AdjustMethod longTravelAdjustFct;
	private AdjustMethod nearDistAdjustFct;
	private AdjustMethod longDistAdjustFct;
	
	private Double nearATravelMethodCst;
	private Double nearBTravelMethodCst;
	private Double nearADistMethodCst;
	private Double nearBDistMethodCst;
	
	private Double longATravelMethodCst;
	private Double longBTravelMethodCst;
	private Double longADistMethodCst;
	private Double longBDistMethodCst;
	
	private List<CarStrategy> carStrategies;
	
	public enum CarStrategy {
		AGGRESSIVE, // all below
		NEAR_FIRST, // prefer nearest trip with longest distance
		LONG_FIRST, // prefer longest trip with nearest distance
		QUICK_FIRST, // may prefer trips that ends shortly when approching end
	}
	
	public enum AdjustMethod {
		EXP, LN, LINEAR, POW, INV
	}
	
	public H2018Config(Double coefBonus, Double nearBonus, Double distanceBonus) {
		super(CSV_CST, CSV_PATH);
		this.timeToFinishCoef = 1D;
		this.nearTravelAdjustFct = AdjustMethod.EXP;
		this.nearATravelMethodCst = 1D;
		this.nearBTravelMethodCst = 0D;
		this.nearDistAdjustFct = AdjustMethod.EXP;
		this.nearADistMethodCst = 1D;
		this.nearBDistMethodCst = 0D;
		this.longTravelAdjustFct = AdjustMethod.EXP;
		this.longATravelMethodCst = 1D;
		this.longBTravelMethodCst = 0D;
		this.longDistAdjustFct = AdjustMethod.EXP;
		this.longADistMethodCst = 1D;
		this.longBDistMethodCst = 0D;
		this.carStrategies = new ArrayList<>();
	}
	
	public H2018Config(Double timeToFinishCoef, AdjustMethod nearTravelAdjustFct, AdjustMethod nearDistAdjustFct, AdjustMethod longTravelAdjustFct, AdjustMethod longDistAdjustFct,
			Double nearATravelMethodCst, Double nearBTravelMethodCst, Double nearADistMethodCst, Double nearBDistMethodCst, Double longATravelMethodCst, Double longBTravelMethodCst,
			Double longADistMethodCst, Double longBDistMethodCst, List<CarStrategy> carStrategies) {
		super(CSV_CST, CSV_PATH);
		this.timeToFinishCoef = timeToFinishCoef;
		this.nearTravelAdjustFct = nearTravelAdjustFct;
		this.longTravelAdjustFct = longTravelAdjustFct;
		this.nearDistAdjustFct = nearDistAdjustFct;
		this.longDistAdjustFct = longDistAdjustFct;
		this.nearATravelMethodCst = nearATravelMethodCst;
		this.nearBTravelMethodCst = nearBTravelMethodCst;
		this.nearADistMethodCst = nearADistMethodCst;
		this.nearBDistMethodCst = nearBDistMethodCst;
		this.longATravelMethodCst = longATravelMethodCst;
		this.longBTravelMethodCst = longBTravelMethodCst;
		this.longADistMethodCst = longADistMethodCst;
		this.longBDistMethodCst = longBDistMethodCst;
		this.carStrategies = carStrategies;
	}
	
	public Double adjustNearTravel(Double t) {
		if (CollectionUtils.containsAny(carStrategies, Arrays.asList(CarStrategy.AGGRESSIVE, CarStrategy.NEAR_FIRST))) {
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
		if (CollectionUtils.containsAny(carStrategies, Arrays.asList(CarStrategy.AGGRESSIVE, CarStrategy.NEAR_FIRST))) {
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
		if (CollectionUtils.containsAny(carStrategies, Arrays.asList(CarStrategy.AGGRESSIVE, CarStrategy.NEAR_FIRST))) {
			switch (longTravelAdjustFct) {
			case EXP:
				return Math.exp(t + longBTravelMethodCst);
			case LN:
				return Math.log(t + longBTravelMethodCst);
			case LINEAR:
				return longATravelMethodCst * t + longBTravelMethodCst;
			case POW:
				return Math.pow(t, longATravelMethodCst);
			case INV:
				return 1 / (longATravelMethodCst * t + longBTravelMethodCst);
			default:
				return t;
			}
		}
		return t;
	}
	
	public Double adjustLongDistance(Double t) {
		if (CollectionUtils.containsAny(carStrategies, Arrays.asList(CarStrategy.AGGRESSIVE, CarStrategy.LONG_FIRST))) {
			switch (longDistAdjustFct) {
			case EXP:
				return Math.exp(t + longBDistMethodCst);
			case LN:
				return Math.log(t + longBDistMethodCst);
			case LINEAR:
				return longADistMethodCst * t + longBDistMethodCst;
			case POW:
				return Math.pow(t, longADistMethodCst);
			case INV:
				return 1 / (longADistMethodCst * t + longBDistMethodCst);
			default:
				return t;
			}
		}
		return t;
	}
	
	public Double adjustTimeWhenFinishRatio(Double t, Double d, Integer turn, Integer maxturn) {
		Double ratio = 0D;
		if (CollectionUtils.containsAny(carStrategies, Arrays.asList(CarStrategy.AGGRESSIVE, CarStrategy.LONG_FIRST))) {
			Double diff = maxturn - (t + d + turn);
			if (diff > 0) {
				ratio = diff * timeToFinishCoef;
			}
		}
		return ratio;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Conf[ttfc=");
		builder.append(timeToFinishCoef);
		builder.append(",ntfct=");
		builder.append(nearTravelAdjustFct);
		builder.append(",ltfct=");
		builder.append(longTravelAdjustFct);
		builder.append(",ndfct=");
		builder.append(nearDistAdjustFct);
		builder.append(",ldfct=");
		builder.append(longDistAdjustFct);
		builder.append(",nAt=");
		builder.append(nearATravelMethodCst);
		builder.append(",nBt=");
		builder.append(nearBTravelMethodCst);
		builder.append(",nAd=");
		builder.append(nearADistMethodCst);
		builder.append(",nBd=");
		builder.append(nearBDistMethodCst);
		builder.append(",lAt=");
		builder.append(longATravelMethodCst);
		builder.append(",lBt=");
		builder.append(longBTravelMethodCst);
		builder.append(",lAd=");
		builder.append(longADistMethodCst);
		builder.append(",lBd=");
		builder.append(longBDistMethodCst);
		if (CollectionUtils.isNotEmpty(this.carStrategies)) {
			builder.append(",st=");
			builder.append(Arrays.toString(this.carStrategies.toArray()));
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
	
	public AdjustMethod getLongTravelAdjustFct() {
		return longTravelAdjustFct;
	}
	
	public AdjustMethod getNearDistAdjustFct() {
		return nearDistAdjustFct;
	}
	
	public AdjustMethod getLongDistAdjustFct() {
		return longDistAdjustFct;
	}
	
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
	
	public Double getLongATravelMethodCst() {
		return longATravelMethodCst;
	}
	
	public Double getLongBTravelMethodCst() {
		return longBTravelMethodCst;
	}
	
	public Double getLongADistMethodCst() {
		return longADistMethodCst;
	}
	
	public Double getLongBDistMethodCst() {
		return longBDistMethodCst;
	}
	
	public List<CarStrategy> getCarStrategies() {
		return carStrategies;
	}
	
}
