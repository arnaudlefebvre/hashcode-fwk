package fr.noobeclair.hashcode.solve.hashcode2018;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections4.CollectionUtils;

import fr.noobeclair.hashcode.Main;
import fr.noobeclair.hashcode.bean.hashcode2018.Car;
import fr.noobeclair.hashcode.bean.hashcode2018.CityMap;
import fr.noobeclair.hashcode.bean.hashcode2018.H2018BeanContainer;
import fr.noobeclair.hashcode.bean.hashcode2018.H2018Config;
import fr.noobeclair.hashcode.bean.hashcode2018.Ride;
import fr.noobeclair.hashcode.solve.Solver;
import fr.noobeclair.hashcode.solve.StatsConstants;

public class H2018Solver extends Solver<H2018BeanContainer, H2018Config> {
	
	public List<Ride> arides;
	public List<Ride> drides;
	public List<Ride> srides;
	public Set<Car> cars;
	public CityMap map;
	public Integer bonus;
	public Integer maxTurn;
	private Integer curScore;
	private Integer totalScore;
	
	public H2018Solver() {
		super();
	}
	
	public H2018Solver(H2018Config config) {
		super();
		this.config = config;
	}
	
	public H2018Solver(Long timeout, H2018Config config) {
		super(timeout);
		this.config = config;
	}
	
	@Override
	public H2018BeanContainer run(H2018BeanContainer data) {
		logger.debug("H2018Solver START : {} cars, {} rides, {} turns {},  ({})", data.getCars().size(), data.getAvailableRides().size(), data.getMaxTurn(), this.getAdditionnalInfo());
		arides = data.getAvailableRides();
		stats.put(StatsConstants.ITEM0_TOTAL, "" + arides.size());
		stats.put(StatsConstants.ITEM1_TOTAL, "" + data.getCars().size());
		stats.put(StatsConstants.IN_0, "" + data.getMaxTurn());
		stats.put(StatsConstants.IN_1, "" + data.getBonus());
		drides = data.getDoneRides();
		srides = data.getSelectedRides();
		bonus = data.getBonus();
		maxTurn = data.getMaxTurn();
		cars = new TreeSet<>(data.getCars());
		for (Car c : cars) {
			c.setStrategy(Car.STRAT_ALL);
			
		}
		
		totalScore = 0;
		List<Car> availableCars = null;
		int j = maxTurn + data.getAvailableRides().size();
		int ct = 0;
		// ProgressBar bar = new ProgressBar(j, 100, "|", "|", "=", "=>", "Done!");
		for (ct = 0; ct < maxTurn; ct++) {
			if (arides.isEmpty()) {
				break;
			}
			int progress = ct + arides.size();
			final int i = ct;
			
			availableCars = new ArrayList<>();
			for (Car c : cars) {
				Integer score = c.finish(i);
				if (score > 0 || ct == 0) {
					// logger.info("Score {}, total {}", score, totalScore);
					totalScore = totalScore + score;
					availableCars.add(c);
				}
				
			}
			// logger.info(" ----> CARSavailableCars {}", availableCars);
			// logger.info("------------------------- CARS -----------------------");
			int idxCar = 0;
			for (Car car : availableCars) {
				if (arides.isEmpty()) {
					break;
				}
				Ride r = getPerfectForCar(car, arides, ct);
				arides.remove(r);
				car.setAvailable(false);
				car.setRide(r, ct, curScore);
				cars.add(car);
				srides.add(r);
				progress = ct + arides.size();
				// bar.show(System.out, progress);
				idxCar = idxCar + 1;
			}
			progress = ct + arides.size();
			// bar.show(System.out, progress);
			// logger.info("TURN {}, {} {}", ct, Main.CR, this.toSMintring());
			// logger.info("------------------------ NEW TURN ---------------------------");
		}
		// bar.end(System.out);
		// logger.info("H2018Solver END, score : {}. ({})", ct,
		// this.getAdditionnalInfo());
		this.data.score = this.totalScore;
		this.data.selectedRides = srides;
		stats.put(StatsConstants.SCORE, "" + this.data.score);
		stats.put(StatsConstants.ITEM0_SCORED, "" + this.data.selectedRides.size());
		addConfigStats();
		return this.data;
	}
	
	private Ride getPerfectForCar(Car c, List<Ride> arides, int ct) {
		curScore = 0;
		Integer max = Integer.MIN_VALUE;
		Ride selected = null;
		int score = 0;
		for (Ride r : arides) {
			score = c.getScoreForRide(r, ct, bonus, maxTurn, config);
			
			if (max < score) {
				max = score;
				selected = r;
			}
			// logger.info("Score : car {}, current ride {}, score {}, max {}, selected {}",
			// c, r, score, max, selected);
		}
		curScore = max;
		if (selected == null) {
			logger.info("Score : car {},  score {}, max {}, selected {}", c, score, max, selected);
		}
		return selected;
	}
	
	@Override
	public String toString() {
		return "H2018Solver [totalScore=" + totalScore + ", arides=[" + arides + "]," + Main.CR + " drides=[" + drides + "]," + Main.CR + " srides=[" + srides + "]," + Main.CR + "cars=[" + cars
				+ "], map=" + map + ", bonus=" + bonus + ", maxTurn=" + maxTurn + "]";
	}
	
	public String toSMintring() {
		return "H2018Solver [totalScore=" + totalScore + ", arides=[" + arides.size() + "]," + Main.CR + " drides=[" + drides.size() + "]," + Main.CR + " srides=[" + srides.size() + "]," + Main.CR
				+ "cars=[" + cars + "], map=" + map + ", bonus=" + bonus + ", maxTurn=" + maxTurn + "]";
	}
	
	@Override
	public String getAdditionnalInfo() {
		return config.toString();
	}
	
	private void addConfigStats() {
		if (CollectionUtils.isNotEmpty(config.getCarStrategies())) {
			stats.put(StatsConstants.CF_STRAT, Arrays.toString(config.getCarStrategies().toArray()));
			stats.put(StatsConstants.CF_TTFC, "" + config.getTimeToFinishCoef());
			stats.put(StatsConstants.CF_NTFCT, "" + config.getNearTravelAdjustFct());
			stats.put(StatsConstants.CF_LTFCT, "" + config.getNearTravelAdjustFct());
			stats.put(StatsConstants.CF_NDFCT, "" + config.getNearDistAdjustFct());
			stats.put(StatsConstants.CF_LDFCT, "" + config.getLongDistAdjustFct());
			stats.put(StatsConstants.CF_NAT, "" + config.getNearATravelMethodCst());
			stats.put(StatsConstants.CF_NBT, "" + config.getNearBTravelMethodCst());
			stats.put(StatsConstants.CF_NAD, "" + config.getNearADistMethodCst());
			stats.put(StatsConstants.CF_NBD, "" + config.getNearBDistMethodCst());
			stats.put(StatsConstants.CF_LAT, "" + config.getLongATravelMethodCst());
			stats.put(StatsConstants.CF_LBT, "" + config.getLongBTravelMethodCst());
			stats.put(StatsConstants.CF_LAD, "" + config.getLongADistMethodCst());
			stats.put(StatsConstants.CF_LBD, "" + config.getLongBDistMethodCst());
		}
	}
	
}
