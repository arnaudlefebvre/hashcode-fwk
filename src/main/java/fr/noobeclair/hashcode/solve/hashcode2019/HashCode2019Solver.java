package fr.noobeclair.hashcode.solve.hashcode2019;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import fr.noobeclair.hashcode.bean.hashcode2019.H2019Config;
import fr.noobeclair.hashcode.bean.hashcode2019.HashCode2019BeanContainer;
import fr.noobeclair.hashcode.bean.hashcode2019.Photo;
import fr.noobeclair.hashcode.bean.hashcode2019.Slide;
import fr.noobeclair.hashcode.bean.hashcode2019.SlideShow;
import fr.noobeclair.hashcode.solve.Solver;
import fr.noobeclair.hashcode.utils.AlgoUtils;
import fr.noobeclair.hashcode.utils.ProgressBar;
import fr.noobeclair.hashcode.utils.Utils;
import fr.noobeclair.hashcode.utils.dto.DistanceResultDto;

public class HashCode2019Solver extends Solver<HashCode2019BeanContainer, H2019Config> {
	
	public HashCode2019Solver() {
		super();
	}
	
	@Override
	protected HashCode2019BeanContainer run(HashCode2019BeanContainer data, ProgressBar bar) {
		HashCode2019BeanContainer datas = data;
		// 1 - Extraction des photos verticales pour les positionner ensembles
		long start = System.currentTimeMillis();
		logger.info("-- Solve sep 1 start");
		List<Photo> listPhotosVertical = datas.getPhotos().stream().filter(photo -> photo.getSens().equalsIgnoreCase("V")).collect(Collectors.toList());
		HashMap<Integer, Photo> processed = new HashMap<Integer, Photo>();
		List<Slide> allSlides = new ArrayList<Slide>();
		logger.info("-- Solve sep 1 End. Total Time : {}s --", Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
		// AbstractStep 1 - Regroupement des photos verticales - On regroupe les photos
		// les plus
		// �loign�s
		start = System.currentTimeMillis();
		logger.info("-- Solve sep 2 start");
		for (Photo p : listPhotosVertical) {
			// We add current to processed has we do not want to compute distance against
			// itself.
			// We do not need the object as the id is unique in our case
			processed.put(p.hashCode(), null);
			AlgoUtils<Photo> algo = new AlgoUtils<Photo>();
			DistanceResultDto<Photo> res = algo.farthestSibling(p, listPhotosVertical, processed);
			if (res.getObject() != null) {
				Slide s = new Slide(p, res.getObject());
				processed.put(res.getObject().hashCode(), null);
				allSlides.add(s);
			}
		}
		logger.info("-- Solve sep 2 End. Total Time : {}s --", Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
		start = System.currentTimeMillis();
		logger.info("-- Solve sep 3 start");
		HashMap<Integer, Slide> processedslide = new HashMap<Integer, Slide>();
		// AbstractStep 2 - cr�ation des slides horizontales -> A voir si on ne pourrait
		// pas
		// faire �a direct au chargement du fichier... �a �viterait des boucles
		allSlides.addAll(datas.getPhotos().stream().filter(photo -> photo.getSens().equalsIgnoreCase("H")).map(p -> new Slide(p, null)).collect(Collectors.toList()));
		logger.info("-- Solve sep 3 End. Total Time : {}s --", Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
		start = System.currentTimeMillis();
		logger.info("-- Solve sep 4 start");
		// AbstractStep 3 - tri
		Collections.sort(allSlides);
		List<Slide> slideshow = new ArrayList<>();
		// AbstractStep 4 - Construction du slideshow
		for (Slide s : allSlides) {
			processed.put(s.hashCode(), null);
			slideshow.add(s);
			AlgoUtils<Slide> algo = new AlgoUtils<Slide>();
			DistanceResultDto<Slide> res = algo.farthestSibling(s, allSlides, processedslide);
			if (res.getObject() != null) {
				slideshow.add(res.getObject());
			}
		}
		logger.info("-- Solve sep 4 End. Total Time : {}s --", Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
		datas.setSlideshow(new SlideShow(slideshow));
		return datas;
	}
	
}
