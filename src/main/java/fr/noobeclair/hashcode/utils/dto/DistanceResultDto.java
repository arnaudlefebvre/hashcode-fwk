package fr.noobeclair.hashcode.utils.dto;

import fr.noobeclair.hashcode.bean.Bean;
import fr.noobeclair.hashcode.interfaces.Distanceable;

public class DistanceResultDto {
	
	private int index;
	private Bean object;
	private double distance;
	
	public DistanceResultDto() {
		// TODO Auto-generated constructor stub
	}

	public DistanceResultDto(int index, Bean object, double distance) {
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

	public Bean getObject() {
		return object;
	}

	public void setObject(Bean object) {
		this.object = object;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	

}
