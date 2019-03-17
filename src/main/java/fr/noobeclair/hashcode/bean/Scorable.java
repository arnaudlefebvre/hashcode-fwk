package fr.noobeclair.hashcode.bean;

import java.math.BigDecimal;

public interface Scorable<T extends Bean> {
	
	public BigDecimal score(T in);

}
