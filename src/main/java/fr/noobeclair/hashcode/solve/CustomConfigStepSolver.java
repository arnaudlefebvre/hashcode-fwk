package fr.noobeclair.hashcode.solve;

import fr.noobeclair.hashcode.bean.CustomBeanContainer;
import fr.noobeclair.hashcode.bean.CustomConfig;
import fr.noobeclair.hashcode.utils.ProgressBar;

public class CustomConfigStepSolver extends ConfigSolver<CustomBeanContainer, CustomConfig> {
	
	public CustomConfigStepSolver() {
		// TODO Auto-generated constructor stub
	}
	
	public CustomConfigStepSolver(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	public CustomConfigStepSolver(String name, Long timeout) {
		super(name, timeout);
		// TODO Auto-generated constructor stub
	}
	
	public CustomConfigStepSolver(String name, CustomConfig conf, Long timeout) {
		super(name, conf, timeout);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected CustomBeanContainer runWithStat(CustomBeanContainer data, ProgressBar bar) {
		throw new UnsupportedOperationException("Not supported yet");
	}
	
	@Override
	protected void addConfigStats() {
		throw new UnsupportedOperationException("Not supported yet");
	}
	
}
