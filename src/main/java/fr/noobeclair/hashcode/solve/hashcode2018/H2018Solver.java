package fr.noobeclair.hashcode.solve.hashcode2018;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import fr.noobeclair.hashcode.Main;
import fr.noobeclair.hashcode.bean.hashcode2018.Car;
import fr.noobeclair.hashcode.bean.hashcode2018.CityMap;
import fr.noobeclair.hashcode.bean.hashcode2018.H2018BeanContainer;
import fr.noobeclair.hashcode.bean.hashcode2018.H2018Config;
import fr.noobeclair.hashcode.bean.hashcode2018.Ride;
import fr.noobeclair.hashcode.solve.Solver;
import fr.noobeclair.hashcode.utils.ProgressBar;

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
	private H2018Config conf;
	
	public H2018Solver(H2018Config conf) {
		super();
		this.conf = conf;
	}
	
	public H2018Solver(Long timeout, H2018Config conf) {
		super(timeout);
		this.conf = conf;
	}
	
	@Override
	public H2018BeanContainer run(H2018BeanContainer data) {
		// logger.info(data);
		arides = data.getAvailableRides();
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
		ProgressBar bar = new ProgressBar(j, 100, "|", "|", "=", "=>", "Done!");
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
				bar.show(System.out, progress);
				idxCar = idxCar + 1;
			}
			progress = ct + arides.size();
			bar.show(System.out, progress);
			// logger.info("TURN {}, {} {}", ct, Main.CR, this.toSMintring());
			// logger.info("------------------------ NEW TURN ---------------------------");
		}
		logger.info("END {}, {}", ct, this.toSMintring());
		bar.end(System.out);
		logger.error("THIS IS FUCKING SCORE :" + this.totalScore);
		this.data.score = this.totalScore;
		this.data.selectedRides = srides;
		return this.data;
	}
	
	private Ride getPerfectForCar(Car c, List<Ride> arides, int ct) {
		curScore = 0;
		Integer max = Integer.MIN_VALUE;
		Ride selected = null;
		int score = 0;
		for (Ride r : arides) {
			score = c.getScoreForRide(r, ct, bonus, maxTurn, conf);
			
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
		return conf.toString();
	}
	
	@Override
	protected Solver<H2018BeanContainer, H2018Config> build(H2018Config conf, Long timeout) {
		return new H2018Solver(timeout, conf);
	}
	
}
