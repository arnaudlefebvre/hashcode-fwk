package fr.noobeclair.hashcode.solve;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.sun.org.apache.bcel.internal.generic.ALOAD;

import fr.noobeclair.hashcode.bean.Bean;
import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.bean.hashcode2019.HashCode2019BeanContainer;
import fr.noobeclair.hashcode.bean.hashcode2019.Photo;
import fr.noobeclair.hashcode.bean.hashcode2019.Slide;
import fr.noobeclair.hashcode.utils.AlgoUtils;
import fr.noobeclair.hashcode.utils.dto.DistanceResultDto;


public class HashCode2019Solver extends Solver{

	public HashCode2019Solver() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected BeanContainer run(BeanContainer data) {
		HashCode2019BeanContainer datas = (HashCode2019BeanContainer) data;
		//1 - Extraction des photos verticales pour les positionner ensembles
		List<Bean> listPhotosVertical = datas.getPhotos().stream().filter(photo -> photo.getSens().equalsIgnoreCase("V")).collect(Collectors.toList());
		HashMap<Integer,Bean> processed = new HashMap<Integer,Bean>();
		List<Slide> allSlides = new ArrayList<Slide>();
		
		//Step 1 - Regroupement des photos verticales - On regroupe les photos les plus éloignés
		for (Bean b : listPhotosVertical) {
			Photo p = (Photo) b;
			//We add current to processed has we do not want to compute distance against itself.
			//We do not need the object as the id is unique in our case
			processed.put(p.hashCode(), null);
			DistanceResultDto res = AlgoUtils.farthestSibling(p, listPhotosVertical, processed);
			if (res.getObject() != null) {
				Slide s = new Slide(p, (Photo)res.getObject());
				processed.put(res.getObject().hashCode(), null);
				allSlides.add(s);
			}
		}
		
		//Step 2 - création des slides horizontales -> A voir si on ne pourrait pas faire ça direct au chargement du fichier... ça éviterait des boucles
		allSlides.addAll(datas.getPhotos().stream().filter(photo -> photo.getSens().equalsIgnoreCase("H")).map(p -> new Slide(p,null)).collect(Collectors.toList()));
		//Step 3  - tri
		 Collections.sort(allSlides);
		 //Step 4 - Construction du slideshow
		 //TODO
		return null;
	}

}
