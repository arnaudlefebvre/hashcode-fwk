package fr.noobeclair.hashcode.in.hashcode2018;

import java.util.ArrayList;
import java.util.List;

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
	protected H2018BeanContainer readFile(List<String> lines, String in) {
		H2018BeanContainer result = null;
		List<Ride> rides = new ArrayList<>();
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
		result.setInputRides(rides);
		// logger.info(result.toString());
		return result;
	}

}
