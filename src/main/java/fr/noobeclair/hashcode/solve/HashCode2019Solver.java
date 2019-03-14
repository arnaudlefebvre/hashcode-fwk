package fr.noobeclair.hashcode.solve;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import fr.noobeclair.hashcode.bean.Bean;
import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.bean.hashcode2019.HashCode2019BeanContainer;
import fr.noobeclair.hashcode.bean.hashcode2019.Photo;
import fr.noobeclair.hashcode.bean.hashcode2019.Slide;
import fr.noobeclair.hashcode.bean.hashcode2019.SlideShow;
import fr.noobeclair.hashcode.interfaces.Step;
import fr.noobeclair.hashcode.utils.AlgoUtils;
import fr.noobeclair.hashcode.utils.ProgressBar;
import fr.noobeclair.hashcode.utils.Utils;
import fr.noobeclair.hashcode.utils.dto.DistanceResultDto;

public class HashCode2019Solver extends Solver {
		
	public HashCode2019Solver() {
		super();
	}

	@Override
	protected BeanContainer run(BeanContainer data) {
		HashCode2019BeanContainer datas = (HashCode2019BeanContainer) data;
		// 1 - Extraction des photos verticales pour les positionner ensembles
		long start = System.currentTimeMillis();
		logger.info("-- Solve sep 1 start");
		List<Bean> listPhotosVertical = datas.getPhotos().stream()
				.filter(photo -> photo.getSens().equalsIgnoreCase("V")).collect(Collectors.toList());
		HashMap<Integer, Bean> processed = new HashMap<Integer, Bean>();
		List<Bean> allSlides = new ArrayList<Bean>();
		logger.info("-- Solve sep 1 End. Total Time : {}s --",
				Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
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
		logger.info("-- Solve sep 2 End. Total Time : {}s --",
				Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
		start = System.currentTimeMillis();
		logger.info("-- Solve sep 3 start");
		processed = new HashMap<Integer, Bean>();
		// AbstractStep 2 - cr�ation des slides horizontales -> A voir si on ne pourrait
		// pas
		// faire �a direct au chargement du fichier... �a �viterait des boucles
		allSlides.addAll(datas.getPhotos().stream().filter(photo -> photo.getSens().equalsIgnoreCase("H"))
				.map(p -> new Slide(p, null)).collect(Collectors.toList()));
		logger.info("-- Solve sep 3 End. Total Time : {}s --",
				Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
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
		logger.info("-- Solve sep 4 End. Total Time : {}s --",
				Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
		datas.setSlideshow(new SlideShow(slideshow));
		return datas;
	}

	@Override
	protected BeanContainer runSteps(BeanContainer data) {
		this.data = data;
		Step step1 = new AbstractStep("1") {

			@Override
			protected BeanContainer runStep(BeanContainer data) {
				HashCode2019BeanContainer datas = (HashCode2019BeanContainer) data;

				List<Bean> listPhotosVertical = datas.getPhotos().stream()
						.filter(photo -> photo.getSens().equalsIgnoreCase("V")).collect(Collectors.toList());
				datas.setListVerticalPhoto(listPhotosVertical);
				return datas;
			}
		};
		this.steps.add(step1);
		Step step2 = new AbstractStep("2") {

			@Override
			protected BeanContainer runStep(BeanContainer data) {
				HashCode2019BeanContainer datas = (HashCode2019BeanContainer) data;
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
		};
		this.steps.add(step2);

		Step step3 = new AbstractStep("3") {

			@Override
			protected BeanContainer runStep(BeanContainer data) {
				HashCode2019BeanContainer datas = (HashCode2019BeanContainer) data;
				List<Bean> slides = datas.getSlides();
				slides.addAll(datas.getPhotos().stream().filter(photo -> photo.getSens().equalsIgnoreCase("H"))
								.map(p -> new Slide(p, null)).collect(Collectors.toList()));
				datas.setSlides(slides);
				return datas;
			}
		};
		
		this.steps.add(step3);
		Step step4 = new AbstractStep("4") {

			@Override
			protected BeanContainer runStep(BeanContainer data) {
				HashCode2019BeanContainer datas = (HashCode2019BeanContainer) data;
				HashMap<Integer, Bean> processed = new HashMap<Integer, Bean>();
				Collections.sort(datas.getSlides());
				List<Slide> slideshow = new ArrayList<>();
				// AbstractStep 4 - Construction du slideshow
				int i = 0;
				int j = datas.getSlides().size();
				ProgressBar bar = new ProgressBar(j, 100, "|", "|", "=", "=>", "Done!");
				for (Bean b : datas.getSlides()) {
					bar.show(System.out, i);
					Slide s = (Slide) b;
					processed.put(s.hashCode(), null);
					slideshow.add(s);
					DistanceResultDto res = AlgoUtils.farthestSibling(s, datas.getSlides(), processed);
					if (res.getObject() != null) {
						slideshow.add((Slide) res.getObject());
					}
					i = i+1;
				}
				datas.setSlideshow(new SlideShow(slideshow));
				return datas;
			}
		};
		this.steps.add(step4);

		for (Step s : this.steps) {
			this.data = s.run(this.data);
		}
		return data;

	}

}
