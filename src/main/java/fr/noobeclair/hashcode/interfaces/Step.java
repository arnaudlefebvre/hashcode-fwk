package fr.noobeclair.hashcode.interfaces;

import fr.noobeclair.hashcode.bean.BeanContainer;

@FunctionalInterface
public interface Step<T extends BeanContainer> {
	
	public T run(T solver);
	
}
