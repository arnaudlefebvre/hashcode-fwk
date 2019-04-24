package fr.noobeclair.hashcode.solve.hashcode2018;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections4.CollectionUtils;

import fr.noobeclair.hashcode.MainRunner;
import fr.noobeclair.hashcode.annotation.CsvExport;
import fr.noobeclair.hashcode.annotation.CsvField;
import fr.noobeclair.hashcode.bean.hashcode2018.Car;
import fr.noobeclair.hashcode.bean.hashcode2018.CityMap;
import fr.noobeclair.hashcode.bean.hashcode2018.H2018BeanContainer;
import fr.noobeclair.hashcode.bean.hashcode2018.H2018Config;
import fr.noobeclair.hashcode.bean.hashcode2018.Ride;
import fr.noobeclair.hashcode.solve.ConfigSolver;
import fr.noobeclair.hashcode.solve.StatsConstants;
import fr.noobeclair.hashcode.utils.FixedStack;

@CsvExport
public class H2018Solver extends ConfigSolver<H2018BeanContainer, H2018Config> {

	public List<Ride> arides;
	public List<Ride> drides;
	public List<Ride> srides;
	public Set<Car> cars;
	public CityMap map;
	public Integer bonus;
	public Integer maxTurn;
	private Integer curScore;
	@CsvField
	private Integer totalScore;

	public H2018Solver() {
		super();
	}

	public H2018Solver(String name, H2018Config config) {
		super(name);
		this.config = config;
	}

	public H2018Solver(String name, Long timeout, H2018Config config) {
		super(name, timeout);
		this.config = config;
	}

	@Override
	public H2018BeanContainer runWithStat(H2018BeanContainer data) {
		logger.debug("H2018Solver START : {} cars, {} rides, {} turns {},  ({})", data.getCars().size(),
				data.getAvailableRides().size(), data.getMaxTurn());
		arides = data.getAvailableRides();
		drides = data.getDoneRides();
		srides = data.getSelectedRides();
		bonus = data.getBonus();
		maxTurn = data.getMaxTurn();
		cars = new TreeSet<>(data.getCars());
		BigDecimal totalstep = new BigDecimal(arides.size());
		this.totalItem = totalstep.longValue();
		for (Car c : cars) {
			c.setStrategy(Car.STRAT_ALL);

		}
		if (arides.isEmpty()) {
			throw new RuntimeException("No Rides to Process ! " + this);
		}
		totalScore = 0;
		List<Car> availableCars = null;
		int ct = 0;
		for (ct = 0; ct < maxTurn; ct++) {
			if (arides.isEmpty()) {
				break;
			}

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
				idxCar = idxCar + 1;
			}
			BigDecimal step = new BigDecimal(ct).divide(new BigDecimal(maxTurn), 2, RoundingMode.HALF_UP)
					.multiply(totalstep);
			showBar(step.longValue(),
					"" + step.divide(totalstep, 2, RoundingMode.HALF_DOWN).multiply(new BigDecimal("100")) + "%");
		}

		this.data.score = this.totalScore;
		this.data.selectedRides = srides;
		this.data.cars = cars;
		stats.put(StatsConstants.SCORE, "" + this.data.score);
		stats.put(StatsConstants.ITEM0_SCORED, "" + this.data.selectedRides.size());
		addConfigStats();
		return this.data;
	}

	private Integer getMin(FixedStack<Ride> arides) {
		int min = Integer.MAX_VALUE;
		List<Ride> rides = arides.toList();
		if (rides.size() != arides.size()) {
			return Integer.MIN_VALUE;
		}
		for (Ride r : rides) {
			int score = r.score;
			if (r.score < min) {
				min = r.score;
			}
		}
		return min;
	}

	private Ride getRideForCar(Car c, List<Ride> arides, int ct) {
		Car copy = new Car(c);
		return null;
	}

	private List<Ride> listBestMatchesForCar(Car c, List<Ride> arides, int ct) {
		// Seuil calculé par rappot à la diagonale ? racine de (long² + larg ²)

		curScore = 0;
		Integer max = Integer.MIN_VALUE;
		FixedStack<Ride> rides = new FixedStack<>(5);
		Ride selected = null;
		int score = 0;
		for (Ride r : arides) {
			score = c.getScoreForRide(r, ct, bonus, maxTurn, config, c.pos);

			if (score > max) {
				// max = score;
				r.setScore(score);
				rides.push(r);
				rides.sort();
				max = getMin(rides);
			}
			// logger.info("Score : car {}, current ride {}, score {}, max {}, selected {}",
			// c, r, score, max, selected);
		}
		curScore = max;
		if (selected == null) {
			logger.info("Score : car {},  score {}, max {}, selected {}", c, score, max, selected);
		}
		return rides.toList();
	}

	private Ride getPerfectForCar(Car c, List<Ride> arides, int ct) {
		curScore = 0;
		Integer max = Integer.MIN_VALUE;
		Ride selected = null;
		int score = 0;
		for (Ride r : arides) {
			score = c.getScoreForRide(r, ct, bonus, maxTurn, config, c.pos);

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
		return "H2018Solver [totalScore=" + totalScore + ", arides=[" + arides + "]," + MainRunner.CR + " drides=["
				+ drides + "]," + MainRunner.CR + " srides=[" + srides + "]," + MainRunner.CR + "cars=[" + cars
				+ "], map=" + map + ", bonus=" + bonus + ", maxTurn=" + maxTurn + "]";
	}

	public String toSMintring() {
		return "H2018Solver [totalScore=" + totalScore + ", arides=[" + arides.size() + "]," + MainRunner.CR
				+ " drides=[" + drides.size() + "]," + MainRunner.CR + " srides=[" + srides.size() + "],"
				+ MainRunner.CR + "cars=[" + cars + "], map=" + map + ", bonus=" + bonus + ", maxTurn=" + maxTurn + "]";
	}

	@Override
	protected void addConfigStats() {
		if (CollectionUtils.isNotEmpty(config.getCarStrategies())) {
			stats.put(StatsConstants.CF_STRAT, Arrays.toString(config.getCarStrategies().toArray()));
			stats.put(StatsConstants.CF_TTFC, "" + config.getTimeToFinishCoef());
			stats.put(StatsConstants.CF_NTFCT, "" + config.getNearTravelAdjustFct());
			// stats.put(StatsConstants.CF_LTFCT, "" + config.getLongTravelAdjustFct());
			// stats.put(StatsConstants.CF_NDFCT, "" + config.getNearDistAdjustFct());
			// stats.put(StatsConstants.CF_LDFCT, "" + config.getLongDistAdjustFct());
			// stats.put(StatsConstants.CF_NAT, "" + config.getNearATravelMethodCst());
			// stats.put(StatsConstants.CF_NBT, "" + config.getNearBTravelMethodCst());
			// stats.put(StatsConstants.CF_NAD, "" + config.getNearADistMethodCst());
			// stats.put(StatsConstants.CF_NBD, "" + config.getNearBDistMethodCst());
			// stats.put(StatsConstants.CF_LAT, "" + config.getLongATravelMethodCst());
			// stats.put(StatsConstants.CF_LBT, "" + config.getLongBTravelMethodCst());
			// stats.put(StatsConstants.CF_LAD, "" + config.getLongADistMethodCst());
			// stats.put(StatsConstants.CF_LBD, "" + config.getLongBDistMethodCst());
		}
	}

}
