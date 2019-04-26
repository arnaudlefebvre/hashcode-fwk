package fr.noobeclair.hashcode.solve.hashcode2019;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import fr.noobeclair.hashcode.bean.hashcode2019.H2019Config;
import fr.noobeclair.hashcode.bean.hashcode2019.HashCode2019BeanContainer;
import fr.noobeclair.hashcode.bean.hashcode2019.Photo;
import fr.noobeclair.hashcode.bean.hashcode2019.Slide;
import fr.noobeclair.hashcode.bean.hashcode2019.SlideShow;
import fr.noobeclair.hashcode.solve.ConfigSolver;
import fr.noobeclair.hashcode.solve.step.AbstractStep;
import fr.noobeclair.hashcode.utils.AlgoUtils;
import fr.noobeclair.hashcode.utils.ProgressBar;
import fr.noobeclair.hashcode.utils.RandomIterator;
import fr.noobeclair.hashcode.utils.dto.DistanceResultDto;

public class HashCode2019RandomStepSolver extends ConfigSolver<HashCode2019BeanContainer, H2019Config> {

	private static final Long WAIT = 0L;

	public HashCode2019RandomStepSolver() {
		super();
	}

	public HashCode2019RandomStepSolver(String name, final Long timeout) {
		super(name, timeout);
	}

	@Override
	protected HashCode2019BeanContainer runWithStat(final HashCode2019BeanContainer data) {
		final List<AbstractStep<HashCode2019BeanContainer>> steps = new ArrayList<>();
		this.data = data;
		steps.add(step1());
		steps.add(step2());
		steps.add(step3());
		steps.add(step4());
		for (final AbstractStep<HashCode2019BeanContainer> s : steps) {
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

		public Step1(final String id, final HashCode2019BeanContainer solver) {
			super(id, solver);
		}

		@Override
		protected HashCode2019BeanContainer runStep(final HashCode2019BeanContainer datas) {
			final List<Photo> listPhotosVertical = datas.getPhotos().stream()
					.filter(photo -> photo.getSens().equalsIgnoreCase("V")).collect(Collectors.toList());
			datas.setListVerticalPhoto(listPhotosVertical);
			return datas;
		}
	}

	private class Step2 extends AbstractStep<HashCode2019BeanContainer> {

		public Step2(final String id, final HashCode2019BeanContainer solver) {
			super(id, solver);
		}

		@Override
		protected HashCode2019BeanContainer runStep(final HashCode2019BeanContainer datas) {
			final HashMap<Integer, Photo> processed = new HashMap<>();
			final List<Slide> slides = new ArrayList<>();
			for (final Photo p : datas.getListVerticalPhoto()) {
				// We add current to processed has we do not want to compute distance against
				// itself.
				// We do not need the object as the id is unique in our case
				processed.put(p.hashCode(), null);
				final AlgoUtils<Photo> algo = new AlgoUtils<>();
				final DistanceResultDto<Photo> res = algo.farthestSibling(p, datas.getListVerticalPhoto(), processed);
				if (res.getObject() != null) {
					final Slide s = new Slide(p, res.getObject());
					processed.put(res.getObject().hashCode(), null);
					slides.add(s);
				}
			}
			datas.setSlides(slides);
			return datas;
		}
	}

	private class Step3 extends AbstractStep<HashCode2019BeanContainer> {

		public Step3(final String id, final HashCode2019BeanContainer solver) {
			super(id, solver);
		}

		@Override
		protected HashCode2019BeanContainer runStep(final HashCode2019BeanContainer datas) {
			// Nothing to do here
			return datas;
		}
	}

	private class Step4 extends AbstractStep<HashCode2019BeanContainer> {

		public Step4(final String id, final HashCode2019BeanContainer solver) {
			super(id, solver);
		}

		@Override
		protected HashCode2019BeanContainer runStep(final HashCode2019BeanContainer datas) {
			final HashMap<Integer, Slide> processed = new HashMap<>();
			datas.setSlides(datas.getSlides().stream().sorted().collect(Collectors.toList()));
			final List<Slide> slideshow = new ArrayList<>();
			// AbstractStep 4 - Construction du slideshow
			int i = 1;
			final Integer j = datas.getSlides().size();
			final ProgressBar bar = ProgressBar.builder(j.longValue()).withMaxWidth(200).withBarMsgSize(30).build();
			bar.show(System.out, 0L, getName());
			final RandomIterator<Slide> it = new RandomIterator<>(datas.getSlides(), new Random());
			while (it.hasNext()) {
				final Slide s = it.next();
				if (Thread.currentThread().isInterrupted() || !Thread.currentThread().isAlive()) {
					return null;
				}
				if (slideshow.isEmpty()) {
					slideshow.add(s);
					processed.put(s.hashCode(), null);
				}
				final AlgoUtils<Slide> algo = new AlgoUtils<>();
				final DistanceResultDto<Slide> res = algo.nearestSibling(s, datas.getSlides(), processed);
				if (res.getObject() != null) {
					slideshow.add(res.getObject());
					processed.put(res.getObject().hashCode(), null);
				}
				halt(WAIT);
				bar.show(System.out, i, getName());
				i = i + 1;
			}
			bar.end(System.out);
			datas.setSlideshow(new SlideShow(slideshow));
			return datas;
		}
	}

	@Override
	protected void addConfigStats() {
		return;
	}

}
