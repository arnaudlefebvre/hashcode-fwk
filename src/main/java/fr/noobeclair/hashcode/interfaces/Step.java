package fr.noobeclair.hashcode.interfaces;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.solve.Solver;

@FunctionalInterface
public interface Step<T extends BeanContainer> {
	
	public T run(T solver);

}
