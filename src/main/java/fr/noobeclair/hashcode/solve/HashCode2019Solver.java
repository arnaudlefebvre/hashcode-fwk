package fr.noobeclair.hashcode.solve;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import fr.noobeclair.hashcode.bean.Bean;
import fr.noobeclair.hashcode.bean.hashcode2019.HashCode2019BeanContainer;
import fr.noobeclair.hashcode.bean.hashcode2019.Photo;
import fr.noobeclair.hashcode.bean.hashcode2019.Slide;
import fr.noobeclair.hashcode.bean.hashcode2019.SlideShow;
import fr.noobeclair.hashcode.utils.AlgoUtils;
import fr.noobeclair.hashcode.utils.ProgressBar;
import fr.noobeclair.hashcode.utils.Utils;
import fr.noobeclair.hashcode.utils.dto.DistanceResultDto;

public class HashCode2019Solver extends Solver<HashCode2019BeanContainer> {
	
	// private List<? extends AbstractStep<HashCode2019Solver>> steps;
	
	private static final Long WAIT = 0L;
	// private static final Long WAIT = 10L;
	// private static final Long WAIT = 100L;
	// private static final Long WAIT = 1000L;
	
	public HashCode2019Solver() {
		super();
	}
	
	@Override
	protected HashCode2019BeanContainer run(HashCode2019BeanContainer data) {
		HashCode2019BeanContainer datas = data;
		// 1 - Extraction des photos verticales pour les positionner ensembles
		long start = System.currentTimeMillis();
		logger.info("-- Solve sep 1 start");
		List<Bean> listPhotosVertical = datas.getPhotos().stream().filter(photo -> photo.getSens().equalsIgnoreCase("V"))
				.collect(Collectors.toList());
		HashMap<Integer, Bean> processed = new HashMap<Integer, Bean>();
		List<Bean> allSlides = new ArrayList<Bean>();
		logger.info("-- Solve sep 1 End. Total Time : {}s --", Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
		// AbstractStep 1 - Regroupement des photos verticales - On regroupe les photos
		// les plus
		// �loign�s
		start = System.currentTimeMillis();
		logger.info("-- Solve sep 2 start");
		for (Bean b : listPhotosVertical) {
			Photo p = (Photo) b;
			// We add current to processed has we do not want to compute distance against
			// itself.
			// We do not need the object as the id is unique in our case
			processed.put(p.hashCode(), null);
			DistanceResultDto res = AlgoUtils.farthestSibling(p, listPhotosVertical, processed);
			if (res.getObject() != null) {
				Slide s = new Slide(p, (Photo) res.getObject());
				processed.put(res.getObject().hashCode(), null);
				allSlides.add(s);
			}
		}
		logger.info("-- Solve sep 2 End. Total Time : {}s --", Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
		start = System.currentTimeMillis();
		logger.info("-- Solve sep 3 start");
		processed = new HashMap<Integer, Bean>();
		// AbstractStep 2 - cr�ation des slides horizontales -> A voir si on ne pourrait
		// pas
		// faire �a direct au chargement du fichier... �a �viterait des boucles
		allSlides.addAll(datas.getPhotos().stream().filter(photo -> photo.getSens().equalsIgnoreCase("H")).map(p -> new Slide(p, null))
				.collect(Collectors.toList()));
		logger.info("-- Solve sep 3 End. Total Time : {}s --", Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
		start = System.currentTimeMillis();
		logger.info("-- Solve sep 4 start");
		// AbstractStep 3 - tri
		Collections.sort(allSlides);
		List<Slide> slideshow = new ArrayList<>();
		// AbstractStep 4 - Construction du slideshow
		for (Bean b : allSlides) {
			Slide s = (Slide) b;
			processed.put(s.hashCode(), null);
			slideshow.add(s);
			DistanceResultDto res = AlgoUtils.farthestSibling(s, allSlides, processed);
			if (res.getObject() != null) {
				slideshow.add((Slide) res.getObject());
			}
		}
		logger.info("-- Solve sep 4 End. Total Time : {}s --", Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
		datas.setSlideshow(new SlideShow(slideshow));
		return datas;
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
			List<Bean> listPhotosVertical = datas.getPhotos().stream().filter(photo -> photo.getSens().equalsIgnoreCase("V"))
					.collect(Collectors.toList());
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
			HashMap<Integer, Bean> processed = new HashMap<Integer, Bean>();
			List<Bean> slides = new ArrayList<>();
			for (Bean b : datas.getListVerticalPhoto()) {
				Photo p = (Photo) b;
				// We add current to processed has we do not want to compute distance against
				// itself.
				// We do not need the object as the id is unique in our case
				processed.put(p.hashCode(), null);
				DistanceResultDto res = AlgoUtils.farthestSibling(p, datas.getListVerticalPhoto(), processed);
				if (res.getObject() != null) {
					Slide s = new Slide(p, (Photo) res.getObject());
					processed.put(res.getObject().hashCode(), null);
					slides.add(s);
				}
			}
			datas.setSlides(slides);
			return datas;
		}
	}
	
	private class Step3 extends AbstractStep<HashCode2019BeanContainer> {
		
		public Step3(String id, HashCode2019BeanContainer solver) {
			super(id, solver);
		}
		
		@Override
		protected HashCode2019BeanContainer runStep(HashCode2019BeanContainer datas) {
			List<Bean> slides = datas.getSlides();
			slides.addAll(datas.getPhotos().stream().filter(photo -> photo.getSens().equalsIgnoreCase("H")).map(p -> new Slide(p, null))
					.collect(Collectors.toList()));
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
			HashMap<Integer, Bean> processed = new HashMap<Integer, Bean>();
			Collections.sort(datas.getSlides());
			List<Slide> slideshow = new ArrayList<>();
			// AbstractStep 4 - Construction du slideshow
			int i = 1;
			int j = datas.getSlides().size();
			ProgressBar bar = new ProgressBar(j, 100, "|", "|", "=", "=>", "Done!");
			for (Bean b : datas.getSlides()) {
				Slide s = (Slide) b;
				processed.put(s.hashCode(), null);
				slideshow.add(s);
				DistanceResultDto res = AlgoUtils.farthestSibling(s, datas.getSlides(), processed);
				if (res.getObject() != null) {
					slideshow.add((Slide) res.getObject());
				}
				halt(WAIT);
				bar.show(System.out, i);
				i = i + 1;
			}
			datas.setSlideshow(new SlideShow(slideshow));
			return datas;
		}
	}
	
	@Override
	protected HashCode2019BeanContainer runSteps(HashCode2019BeanContainer data) {
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
	
}
