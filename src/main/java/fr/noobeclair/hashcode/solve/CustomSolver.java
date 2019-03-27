package fr.noobeclair.hashcode.solve;

import fr.noobeclair.hashcode.bean.CustomBeanContainer;
import fr.noobeclair.hashcode.utils.ProgressBar;

public class CustomSolver extends Solver<CustomBeanContainer> {
	
	public CustomSolver() {
		// TODO Auto-generated constructor stub
	}
	
	public CustomSolver(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	public CustomSolver(String name, Long timeout) {
		super(name, timeout);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected CustomBeanContainer run(CustomBeanContainer data, ProgressBar bar) {
		throw new UnsupportedOperationException("Not supported yet");
	}
	
}
