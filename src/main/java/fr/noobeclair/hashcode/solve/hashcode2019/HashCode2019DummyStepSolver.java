package fr.noobeclair.hashcode.solve.hashcode2019;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fr.noobeclair.hashcode.bean.hashcode2019.HashCode2019BeanContainer;
import fr.noobeclair.hashcode.bean.hashcode2019.Slide;
import fr.noobeclair.hashcode.bean.hashcode2019.SlideShow;
import fr.noobeclair.hashcode.solve.Solver;
import fr.noobeclair.hashcode.solve.step.AbstractStep;

public class HashCode2019DummyStepSolver extends Solver<HashCode2019BeanContainer> {
	
	private static final Long WAIT = 0L;
	//private static final Long WAIT = 1L;
	 //private static final Long WAIT = 10L;
	// private static final Long WAIT = 100L;
	// private static final Long WAIT = 1000L;
	
	public HashCode2019DummyStepSolver() {
		super();
	}
	
	public HashCode2019DummyStepSolver(Long timeout) {
		super(timeout);
	}

	@Override
	protected HashCode2019BeanContainer run(HashCode2019BeanContainer data) {
		List<AbstractStep<HashCode2019BeanContainer>> steps = new ArrayList<>();
		this.data = data;
		steps.add(step1());
		steps.add(step2());		
		for (AbstractStep<HashCode2019BeanContainer> s : steps) {
			this.data = s.run(this.data);
		}
		return this.data;	
	}
	
	private AbstractStep<HashCode2019BeanContainer> step1() {
		return new Step1("1", this.data);
	}
	
	private AbstractStep<HashCode2019BeanContainer> step2() {
		return new Step2("2", this.data);
	}		
	
	private class Step1 extends AbstractStep<HashCode2019BeanContainer> {
		
		public Step1(String id, HashCode2019BeanContainer solver) {
			super(id, solver);
		}
		
		@Override
		protected HashCode2019BeanContainer runStep(HashCode2019BeanContainer datas) {
			List<Slide> listSideH = datas.getPhotos().stream().filter(photo -> photo.getSens().equalsIgnoreCase("H")).map(p -> new Slide(p,null))
					.collect(Collectors.toList());
			datas.setSlides(listSideH);
			return datas;
		}
	}
	
	private class Step2 extends AbstractStep<HashCode2019BeanContainer> {
		
		public Step2(String id, HashCode2019BeanContainer solver) {
			super(id, solver);
		}
		
		@Override
		protected HashCode2019BeanContainer runStep(HashCode2019BeanContainer datas) {
			datas.setSlideshow(new SlideShow(datas.getSlides().stream().sorted().collect(Collectors.toList())));
			return datas;
		}
	}
	
	
}
