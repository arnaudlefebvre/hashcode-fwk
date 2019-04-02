package fr.noobeclair.hashcode.in.hashcode2018;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.noobeclair.hashcode.bean.hashcode2018.H2018BeanContainer;
import fr.noobeclair.hashcode.bean.hashcode2018.Ride;
import fr.noobeclair.hashcode.in.InReader;

public class H2018Reader extends InReader<H2018BeanContainer> {
	
	private static final int LIGNE_IDX_START_RIDES = 1;
	private int nbRides;
	
	public H2018Reader() {
		super();
		nbRides = 0;
	}
	
	@Override
	protected H2018BeanContainer readFile(String in) {
		H2018BeanContainer result = null;
		List<Ride> rides = new ArrayList<>();
		try (Stream<String> stream = Files.lines(Paths.get(in))) {
			List<String> lines = stream.collect(Collectors.toList());
			int i = 0;
			for (String ln : lines) {
				// logger.info(ln);
				if (i < LIGNE_IDX_START_RIDES) {
					result = H2018BeanContainer.parseFromStr(in, ln, " ");
					// logger.info(result);
				} else {
					Ride r = Ride.parseFromStr(ln, " ");
					r.setId(i - LIGNE_IDX_START_RIDES);
					if (r.getPoints() < result.getMaxTurn()) {
						rides.add(r);
					}
				}
				i++;
			}
			result.setAvailableRides(rides);
			// logger.info(result.toString());
			return result;
			
		} catch (Exception e) {
			logger.error("Erreur lors de la lecture du fichier {}", in, e);
		}
		return result;
	}
	
}
