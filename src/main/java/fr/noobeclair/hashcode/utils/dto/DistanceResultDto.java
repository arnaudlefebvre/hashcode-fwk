package fr.noobeclair.hashcode.utils.dto;

import fr.noobeclair.hashcode.bean.Bean;

public class DistanceResultDto<T extends Bean> {
	
	private int index;
	private T object;
	private double distance;
	
	public DistanceResultDto() {
		
	}
	
	public DistanceResultDto(int index, T object, double distance) {
		super();
		this.index = index;
		this.object = object;
		this.distance = distance;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public T getObject() {
		return object;
	}
	
	public void setObject(T object) {
		this.object = object;
	}
	
	public double getDistance() {
		return distance;
	}
	
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
}
