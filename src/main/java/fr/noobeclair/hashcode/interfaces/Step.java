package fr.noobeclair.hashcode.interfaces;

import fr.noobeclair.hashcode.bean.BeanContainer;

@FunctionalInterface
public interface Step {
	
	public BeanContainer run(BeanContainer data);

}
