package fr.noobeclair.hashcode.bean.hashcode2018;

import java.util.ArrayList;
import java.util.List;

import fr.noobeclair.hashcode.bean.Bean;

public class CityMap extends Bean {
	
	public long maxX;
	public long maxY;
	public List<Quarter> quarters;
	
	public CityMap() {
		super();
	}
	
	public CityMap(long maxX, long maxY) {
		super();
		this.maxX = maxX;
		this.maxY = maxY;
	}
	
	@Override
	public double realdistance(Bean b) {
		throw new UnsupportedOperationException("Not supported yet");
	}
	
	public long getMaxX() {
		return maxX;
	}
	
	public void setMaxX(long maxX) {
		this.maxX = maxX;
	}
	
	public long getMaxY() {
		return maxY;
	}
	
	public void setMaxY(long maxY) {
		this.maxY = maxY;
	}
	
	@Override
	public int realhashCode() {
		return 0;
	}
	
	@Override
	public boolean realequals(Object obj) {
		// return (((CityMap)obj).maxX == this.maxX) && ((CityMap)obj).maxY ==
		// this.maxY));
		return true;
	}
	
	@Override
	public int realcompareTo(Bean b) {
		throw new UnsupportedOperationException("Not supported yet");
	}
	
	public void getCalcQuarters(List<Ride> rides, List<Car> cars) {
		List<Ride> rds = new ArrayList<>();
		// double
		for (Ride r : rides) {
			
		}
	}
}
