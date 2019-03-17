package fr.noobeclair.hashcode.bean;

import java.math.BigDecimal;

public interface DistanceCalculable<T extends Bean> {
	public BigDecimal distance(T in);
}
