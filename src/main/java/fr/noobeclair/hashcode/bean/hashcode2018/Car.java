package fr.noobeclair.hashcode.bean.hashcode2018;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import fr.noobeclair.hashcode.MainRunner;
import fr.noobeclair.hashcode.bean.Bean;

public class Car extends Bean {
	
	public static final Integer STRAT_DUMMY = 4;
	public static final Integer STRAT_LONGDIST = 1;
	public static final Integer STRAT_SHORTDIST = 3;
	public static final Integer STRAT_RUSH_END = 2;
	public static final Integer STRAT_ALL = 1000;
	
	public long id;
	public Point pos;
	public Ride ride;
	public boolean available = true;
	public Integer finishTurn;
	public Integer curScore = 0;
	public Integer strategy = 0;
	public List<Ride> rides = new ArrayList<>();
	
	public Car() {
		super();
	}
	
	public Car(long id, Point pos, boolean available) {
		super();
		this.id = id;
		this.pos = pos;
		this.available = available;
	}
	
	public Car(Car c) {
		super();
		this.id = c.id;
		this.pos = c.pos;
		this.ride = c.ride;
		this.finishTurn = c.finishTurn;
		this.available = c.available;
		this.curScore = c.curScore;
	}
	
	public Car(long id, Point pos) {
		super();
		this.id = id;
		this.pos = pos;
	}
	
	@Override
	public double realdistance(Bean b) {
		throw new UnsupportedOperationException("Not supported yet");
	}
	
	public int finish(Integer turn) {
		Integer score = -1;
		if (this.ride != null && this.finishTurn <= turn) {
			this.rides.add(this.ride);
			score = this.curScore;
			this.pos = this.ride.getEnd();
			this.available = true;
			this.finishTurn = 0;
			this.ride = null;
			this.curScore = 0;
			
		}
		return score;
	}
	
	public boolean isFinish(Integer turn) {
		boolean result = this.available || this.finishTurn <= turn;
		if (result && this.ride != null) {
			finish(turn);
		}
		return result;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public Point getPos() {
		return pos;
	}
	
	public void setPos(Point pos) {
		this.pos = pos;
	}
	
	public Ride getRide() {
		return ride;
	}
	
	public void setRide(Ride ride, Integer turn, Integer score) {
		this.ride = ride;
		int d = ride.getPoints();
		int t = this.pos.distance(ride.getStart());
		this.finishTurn = turn + d + t;
		this.curScore = d;
	}
	
	public boolean isAvailable() {
		return available;
	}
	
	public void setAvailable(boolean available) {
		this.available = available;
	}
	
	@Override
	public int realhashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}
	
	@Override
	public boolean realequals(Object obj) {
		return ((Car) obj).id == this.id;
	}
	
	@Override
	public int realcompareTo(Bean b) {
		if (((Car) b).id == this.id) {
			return 0;
		}
		return 1;
	}
	
	public Integer getStrategy() {
		return strategy;
	}
	
	public void setStrategy(Integer strategy) {
		this.strategy = strategy;
	}
	
	// public int getScoreForRide(Ride r, Integer turn, Integer bonus, Integer
	// maxTurn, H2018Config conf) {
	// Double d = new Double(r.getPoints());
	// Double t = new Double(this.pos.distance(r.getStart()));
	// if (Car.STRAT_SHORTDIST == this.strategy || Car.STRAT_ALL == this.strategy) {
	// // On favorise les trajets proches du point de départ
	// t = Math.exp(this.pos.distance(r.getStart()));
	// d = d * COEF_SHORT;
	// }
	// Double ratio = (double) 1;
	// // if (Car.STRAT_RUSH_END == this.strategy) {
	// // On favorise les trajets qui se finissent le plus avant la fin
	// Double per = 100 * (d + t + turn) / maxTurn;
	// ratio = 1000 / per;
	// // }
	// if (Car.STRAT_LONGDIST == this.strategy || Car.STRAT_ALL == this.strategy) {
	// d = d * COEF_LONG;
	// t = t / COEF_LONG;
	// }
	// Double goStart = t + turn;
	// int totalTrip = ((d + t) > (r.getTripEnd() - r.getTripSt()) ? 0 : 1);
	// int availabililty = ((d + t + turn) > (maxTurn) ? 0 : 1);
	// Double abonus = (goStart.intValue() == r.getTripSt() ? 1 : 0) * bonus *
	// COEF_BONUS;
	// Double score = (d - t + abonus) * (totalTrip) * (availabililty) *
	// ratio.intValue();
	// // logger.debug("score {}, turn {}, max turn {}, d {}, t {}, goSt {},
	// totalTrip
	// // {}, availability {}, bonus {}", score, turn, maxTurn, d, t, goStart,
	// // totalTrip, availabililty, abonus);
	// return score.intValue();
	//
	// }
	public int getSimpleScore(Ride r, Integer turn, Integer bonus, Integer maxTurn, H2018Config conf) {
		Double d = new Double(r.getPoints());
		Double t = new Double(this.pos.distance(r.getStart()));
		Double score = d + t;
		return score.intValue();
	}
	
	public int getScoreForRide(Ride r, Integer turn, Integer bonus, Integer maxTurn, H2018Config conf, Point thisPos) {
		Double d = new Double(r.getPoints());
		Double t = new Double(thisPos.distance(r.getStart()));
		Double rideTimeEnd = new Double(d + t + turn);
		Double ratio = conf.adjustTimeWhenFinishRatio(t, d, turn, maxTurn, r.getTripSt());
		
		t = conf.adjustNearTravel(t);
		d = conf.adjustNearDist(d);
		
		if (Car.STRAT_LONGDIST == this.strategy || Car.STRAT_ALL == this.strategy) {
			// Improve score for long trip. we add a bonus to distance of trip and decrease
			// time to travel to start
			t = conf.adjustLongTravel(t);
			d = conf.adjustLongDistance(d);
		}
		Double goStart = t + turn;
		int totalTrip = ((d + t) > (r.getTripEnd() - r.getTripSt()) ? 0 : 1);
		int availabililty = ((d + t + turn) > (maxTurn) ? 0 : 1);
		int abonus = (goStart.intValue() == r.getTripSt() ? 1 : 0) * (bonus);
		Double score = (d - t + abonus) * (totalTrip) * (availabililty) * ratio.intValue();
		
		if (rideTimeEnd > r.getTripEnd()) {
			score = 0.0;
		}
		//		if (goStart.intValue() < r.getTripSt()) {
		//			int res = r.getTripSt() - goStart.intValue();
		//			double coef = (1 - res / maxTurn);
		//			score = score * coef;
		//		}
		// logger.debug("score {}, turn {}, max turn {}, d {}, t {}, goSt {}, totalTrip
		// {}, availability {}, bonus {}", score, turn, maxTurn, d, t, goStart,
		// totalTrip, availabililty, abonus);
		
		return score.intValue();
		
	}
	
	@Override
	public String toString() {
		return "Car [id=" + id + ", pos=" + pos + ", ride=" + ride + ", available=" + available + ", finishTurn=" + finishTurn + ",  score=" + curScore + "]" + MainRunner.CR;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (available ? 1231 : 1237);
		result = prime * result + curScore;
		result = prime * result + finishTurn;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((pos == null) ? 0 : pos.hashCode());
		result = prime * result + ((ride == null) ? 0 : ride.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Car other = (Car) obj;
		if (available != other.available)
			return false;
		if (curScore != other.curScore)
			return false;
		if (finishTurn != other.finishTurn)
			return false;
		if (id != other.id)
			return false;
		if (pos == null) {
			if (other.pos != null)
				return false;
		} else if (!pos.equals(other.pos))
			return false;
		if (ride == null) {
			if (other.ride != null)
				return false;
		} else if (!ride.equals(other.ride))
			return false;
		return true;
	}
	
	public String write() {
		StringBuilder res = new StringBuilder();
		if (CollectionUtils.isNotEmpty(this.rides)) {
			res.append(this.rides.size()).append(" ");
			for (Ride r : this.rides) {
				res.append(r.getId()).append(" ");
			}
		}
		return res.toString();
	}
}
