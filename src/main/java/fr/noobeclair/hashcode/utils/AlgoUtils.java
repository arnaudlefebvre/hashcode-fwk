package fr.noobeclair.hashcode.utils;

import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.noobeclair.hashcode.bean.Bean;
import fr.noobeclair.hashcode.utils.dto.DistanceResultDto;

public class AlgoUtils {
	
	private static final Logger logger = LogManager.getLogger(AlgoUtils.class);
	
	private AlgoUtils() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Finds the nearests obj of ref in list
	 * 
	 * @param ref
	 *            object on which we wants the nearest sibling
	 * @param list
	 *            list of object to compute against ref. ref must NOT be in this
	 *            list
	 * @return nearest object of ref in list
	 */
	public static DistanceResultDto nearestSibling(Bean ref, List<Bean> list) {
		long start = System.currentTimeMillis();
		logger.debug("-- nearestSibling start");
		double max = Double.MAX_VALUE;
		int idx = 0, resIdx = 0;
		Bean tmp = null;
		for (Bean d : list) {
			double cur = ref.distance(d);
			if (max > cur) {
				tmp = d;
				max = cur;
				resIdx = idx;
			}
			idx = idx++;
		}
		logger.debug("-- nearestSibling End. Total Time : {}s --", Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
		return new DistanceResultDto(resIdx, tmp, max);
	}
	
	/**
	 * Finds the nearests obj of ref in list
	 * 
	 * @param ref
	 *            object on which we wants the nearest sibling
	 * @param list
	 *            list of object to compute against ref.
	 * @param exclude
	 *            {@link HashMap<Integer, Distanceable>} exclude these items from
	 *            comparison
	 * @return nearest object of ref in list
	 */
	public static DistanceResultDto nearestSibling(Bean ref, List<Bean> list, HashMap<Integer, Bean> exclude) {
		long start = System.currentTimeMillis();
		logger.debug("-- nearestSibling start");
		double max = Double.MAX_VALUE;
		int idx = 0, resIdx = 0;
		Bean tmp = null;
		for (Bean d : list) {
			if (!exclude.containsKey(d.hashCode())) {
				double cur = ref.distance(d);
				if (max > cur) {
					tmp = d;
					max = cur;
					resIdx = idx;
				}
			}
			idx = idx++;
		}
		logger.debug("-- nearestSibling End. Total Time : {}s --", Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
		return new DistanceResultDto(resIdx, tmp, max);
	}
	
	/**
	 * Finds the farthest obj of ref in list
	 * 
	 * @param ref
	 *            object on which we wants the nearest sibling
	 * @param list
	 *            list of object to compute against ref. ref must NOT be in this
	 *            list
	 * @return farthest object of ref in list
	 */
	public static DistanceResultDto farthestSibling(Bean ref, List<Bean> list) {
		long start = System.currentTimeMillis();
		logger.debug("-- farthestSibling start");
		double max = 0;
		int idx = 0, resIdx = 0;
		Bean tmp = null;
		for (Bean d : list) {
			double cur = ref.distance(d);
			if (max < cur) {
				tmp = d;
				max = cur;
				resIdx = idx;
			}
			idx = idx++;
		}
		logger.debug("-- farthestSibling End. Total Time : {}s --", Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
		return new DistanceResultDto(resIdx, tmp, max);
	}
	
	/**
	 * Finds the farthest obj of ref in list
	 * 
	 * @param ref
	 *            object on which we wants the nearest sibling
	 * @param list
	 *            list of object to compute against ref. ref must NOT be in this
	 *            list
	 * @param exclude
	 *            {@link HashMap<Integer, Distanceable>} exclude these items from
	 *            comparison
	 * @return farthest object of ref in list
	 */
	public static DistanceResultDto farthestSibling(Bean ref, List<Bean> list, HashMap<Integer, Bean> exclude) {
		long start = System.currentTimeMillis();
		logger.debug("-- farthestSibling start");
		double max = 0;
		int idx = 0, resIdx = 0;
		Bean tmp = null;
		for (Bean d : list) {
			if (!exclude.containsKey(d.hashCode())) {
				double cur = ref.distance(d);
				if (max < cur) {
					tmp = d;
					max = cur;
					resIdx = idx;
				}
			}
			idx = idx++;
		}
		logger.debug("-- farthestSibling End. Total Time : {}s --", Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
		return new DistanceResultDto(resIdx, tmp, max);
	}
	
}
