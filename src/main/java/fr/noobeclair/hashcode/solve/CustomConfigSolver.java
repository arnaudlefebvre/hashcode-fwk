package fr.noobeclair.hashcode.solve;

import java.util.ArrayList;
import java.util.List;

import fr.noobeclair.hashcode.bean.CustomBeanContainer;
import fr.noobeclair.hashcode.bean.CustomConfig;
import fr.noobeclair.hashcode.solve.step.AbstractStep;
import fr.noobeclair.hashcode.utils.ProgressBar;

public class CustomConfigSolver extends ConfigSolver<CustomBeanContainer, CustomConfig> {
	
	public CustomConfigSolver() {
		// TODO Auto-generated constructor stub
	}
	
	public CustomConfigSolver(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	public CustomConfigSolver(String name, Long timeout) {
		super(name, timeout);
		// TODO Auto-generated constructor stub
	}
	
	public CustomConfigSolver(String name, CustomConfig conf, Long timeout) {
		super(name, conf, timeout);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected CustomBeanContainer runWithStat(CustomBeanContainer data, ProgressBar bar) {
		List<AbstractStep<CustomBeanContainer>> steps = new ArrayList<>();
		this.data = data;
		steps.add(step1());
		steps.add(step2());
		steps.add(step3());
		steps.add(step4());
		for (AbstractStep<CustomBeanContainer> s : steps) {
			this.data = s.run(this.data);
		}
		return this.data;
	}
	
	@Override
	protected void addConfigStats() {
		throw new UnsupportedOperationException("Not supported yet");
	}
	
	private class CustomStep1 extends AbstractStep<CustomBeanContainer> {
		
		public CustomStep1(String id, CustomBeanContainer solver) {
			super(id, solver);
		}
		
		@Override
		protected CustomBeanContainer runStep(CustomBeanContainer datas) {
			throw new UnsupportedOperationException("Not supported yet");
		}
	}
	
	private class CustomStep2 extends AbstractStep<CustomBeanContainer> {
		
		public CustomStep2(String id, CustomBeanContainer solver) {
			super(id, solver);
		}
		
		@Override
		protected CustomBeanContainer runStep(CustomBeanContainer datas) {
			throw new UnsupportedOperationException("Not supported yet");
		}
	}
	
	private class CustomStep3 extends AbstractStep<CustomBeanContainer> {
		
		public CustomStep3(String id, CustomBeanContainer solver) {
			super(id, solver);
		}
		
		@Override
		protected CustomBeanContainer runStep(CustomBeanContainer datas) {
			throw new UnsupportedOperationException("Not supported yet");
		}
	}
	
	private class CustomStep4 extends AbstractStep<CustomBeanContainer> {
		
		public CustomStep4(String id, CustomBeanContainer solver) {
			super(id, solver);
		}
		
		@Override
		protected CustomBeanContainer runStep(CustomBeanContainer datas) {
			throw new UnsupportedOperationException("Not supported yet");
		}
	}
	
	private AbstractStep<CustomBeanContainer> step1() {
		return new CustomStep1("1", this.data);
	}
	
	private AbstractStep<CustomBeanContainer> step2() {
		return new CustomStep2("2", this.data);
	}
	
	private AbstractStep<CustomBeanContainer> step3() {
		return new CustomStep3("3", this.data);
	}
	
	private AbstractStep<CustomBeanContainer> step4() {
		return new CustomStep4("4", this.data);
	}
	
}
