package fr.noobeclair.hashcode.solve.hashcode2019;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import fr.noobeclair.hashcode.bean.hashcode2019.H2019Config;
import fr.noobeclair.hashcode.bean.hashcode2019.HashCode2019BeanContainer;
import fr.noobeclair.hashcode.bean.hashcode2019.Photo;
import fr.noobeclair.hashcode.bean.hashcode2019.Slide;
import fr.noobeclair.hashcode.bean.hashcode2019.SlideShow;
import fr.noobeclair.hashcode.solve.Solver;
import fr.noobeclair.hashcode.solve.step.AbstractStep;
import fr.noobeclair.hashcode.utils.AlgoUtils;
import fr.noobeclair.hashcode.utils.ProgressBar;
import fr.noobeclair.hashcode.utils.dto.DistanceResultDto;

public class HashCode2019StepSolver extends Solver<HashCode2019BeanContainer, H2019Config> {
	
	private static final Long WAIT = 0L;
	// private static final Long WAIT = 1L;
	// private static final Long WAIT = 10L;
	// private static final Long WAIT = 100L;
	// private static final Long WAIT = 1000L;
	
	public HashCode2019StepSolver() {
		super();
	}
	
	public HashCode2019StepSolver(Long timeout) {
		super(timeout);
	}
	
	@Override
	protected HashCode2019BeanContainer run(HashCode2019BeanContainer data) {
		List<AbstractStep<HashCode2019BeanContainer>> steps = new ArrayList<>();
		this.data = data;
		steps.add(step1());
		steps.add(step2());
		steps.add(step3());
		steps.add(step4());
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
	
	private AbstractStep<HashCode2019BeanContainer> step3() {
		return new Step3("3", this.data);
	}
	
	private AbstractStep<HashCode2019BeanContainer> step4() {
		return new Step4("4", this.data);
	}
	
	private class Step1 extends AbstractStep<HashCode2019BeanContainer> {
		
		public Step1(String id, HashCode2019BeanContainer solver) {
			super(id, solver);
		}
		
		@Override
		protected HashCode2019BeanContainer runStep(HashCode2019BeanContainer datas) {
			logger.info(" {} All photos", datas.getPhotos().size());
			List<Photo> listPhotosVertical = datas.getPhotos().stream().filter(photo -> photo.getSens().equalsIgnoreCase("V")).collect(Collectors.toList());
			logger.info(" {} Vertical photos", listPhotosVertical.size());
			datas.setListVerticalPhoto(listPhotosVertical);
			return datas;
		}
	}
	
	private class Step2 extends AbstractStep<HashCode2019BeanContainer> {
		
		public Step2(String id, HashCode2019BeanContainer solver) {
			super(id, solver);
		}
		
		@Override
		protected HashCode2019BeanContainer runStep(HashCode2019BeanContainer datas) {
			HashMap<Integer, Photo> processed = new HashMap<Integer, Photo>();
			List<Slide> slides = new ArrayList<>();
			for (Photo p : datas.getListVerticalPhoto()) {
				// We add current to processed has we do not want to compute distance against
				// itself.
				// We do not need the object as the id is unique in our case
				processed.put(p.hashCode(), null);
				AlgoUtils<Photo> algo = new AlgoUtils<Photo>();
				DistanceResultDto<Photo> res = algo.farthestSibling(p, datas.getListVerticalPhoto(), processed);
				if (res.getObject() != null) {
					Slide s = new Slide(p, res.getObject());
					processed.put(res.getObject().hashCode(), null);
					slides.add(s);
				}
			}
			datas.setSlides(slides);
			logger.info(" {} Vertical composed slides", slides.size());
			return datas;
		}
	}
	
	private class Step3 extends AbstractStep<HashCode2019BeanContainer> {
		
		public Step3(String id, HashCode2019BeanContainer solver) {
			super(id, solver);
		}
		
		@Override
		protected HashCode2019BeanContainer runStep(HashCode2019BeanContainer datas) {
			List<Slide> slides = datas.getSlides();
			slides.addAll(datas.getPhotos().stream().filter(photo -> photo.getSens().equalsIgnoreCase("H")).map(p -> new Slide(p, null)).collect(Collectors.toList()));
			datas.setSlides(slides);
			return datas;
		}
	}
	
	private class Step4 extends AbstractStep<HashCode2019BeanContainer> {
		
		public Step4(String id, HashCode2019BeanContainer solver) {
			super(id, solver);
		}
		
		@Override
		protected HashCode2019BeanContainer runStep(HashCode2019BeanContainer datas) {
			HashMap<Integer, Slide> processed = new HashMap<Integer, Slide>();
			datas.setSlides(datas.getSlides().stream().sorted().collect(Collectors.toList()));
			List<Slide> slideshow = new ArrayList<>();
			// AbstractStep 4 - Construction du slideshow
			int i = 1;
			final Integer j = datas.getSlides().size();
			logger.info(" {} All Slides", j);
			final ProgressBar bar = ProgressBar.builder(j.longValue()).withMaxWidth(100).build();
			for (Slide s : datas.getSlides()) {
				if (Thread.currentThread().isInterrupted()) {
					return null;
				}
				if (slideshow.isEmpty()) {
					slideshow.add(s);
					processed.put(s.hashCode(), null);
				}
				AlgoUtils<Slide> algo = new AlgoUtils<Slide>();
				DistanceResultDto<Slide> res = algo.nearestSibling(s, datas.getSlides(), processed);
				if (res.getObject() != null) {
					slideshow.add(res.getObject());
					processed.put(res.getObject().hashCode(), null);
				}
				halt(WAIT);
				bar.show(System.out, i);
				i = i + 1;
			}
			bar.end(System.out);
			logger.info(" {} All Slides in slideshow ", slideshow.size());
			datas.setSlideshow(new SlideShow(slideshow));
			return datas;
		}
	}
	
}
