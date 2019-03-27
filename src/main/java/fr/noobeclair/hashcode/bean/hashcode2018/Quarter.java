package fr.noobeclair.hashcode.bean.hashcode2018;

import java.util.List;

import fr.noobeclair.hashcode.bean.Bean;

public class Quarter extends CityMap {
	
	private Point start;
	private Point end;
	private List<Ride> rides;
	
	public Quarter() {
		super();
	}
	
	public Quarter(Point start, Point end) {
		super();
		this.start = start;
		this.end = end;
	}
	
	public Point getStart() {
		return start;
	}
	
	public void setStart(Point start) {
		this.start = start;
	}
	
	public Point getEnd() {
		return end;
	}
	
	public void setEnd(Point end) {
		this.end = end;
	}
	
	@Override
	public int realhashCode() {
		throw new UnsupportedOperationException("Not supported yet");
	}
	
	@Override
	public int realcompareTo(Bean b) {
		throw new UnsupportedOperationException("Not supported yet");
	}
	
	@Override
	public double realdistance(Bean b) {
		throw new UnsupportedOperationException("Not supported yet");
	}
	
	@Override
	public boolean realequals(Object obj) {
		throw new UnsupportedOperationException("Not supported yet");
	}
	
	public List<Ride> getRides() {
		return rides;
	}
	
	public void setRides(List<Ride> rides) {
		this.rides = rides;
	}
	
}
