package fr.noobeclair.hashcode.bean;

public abstract class Bean {
	public abstract double distance(Bean b);
	public abstract int hashCode();
	public abstract boolean equals(Object obj);
}
