package fr.noobeclair.hashcode.solve;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.utils.ProgressBar;

public class NullSolver extends ConfigSolver {
	
	public NullSolver() {
		
	}
	
	@Override
	public BeanContainer runWithStat(BeanContainer data, ProgressBar bar) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected void addConfigStats() {
		return;
	}
	
}
