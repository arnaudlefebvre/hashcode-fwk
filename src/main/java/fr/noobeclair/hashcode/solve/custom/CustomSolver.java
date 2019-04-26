package fr.noobeclair.hashcode.solve.custom;

import fr.noobeclair.hashcode.bean.custom.CustomBeanContainer;
import fr.noobeclair.hashcode.solve.Solver;

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
	protected CustomBeanContainer run(CustomBeanContainer data) {
		throw new UnsupportedOperationException("Not supported yet");
	}

}
