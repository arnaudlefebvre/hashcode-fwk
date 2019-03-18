package fr.noobeclair.hashcode.utils;

import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.noobeclair.hashcode.bean.Bean;
import fr.noobeclair.hashcode.utils.dto.DistanceResultDto;

public class AlgoUtils<T extends Bean> {
	
	private static final Logger logger = LogManager.getLogger(AlgoUtils.class);
	
	public AlgoUtils() {
		
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
	public DistanceResultDto<T> nearestSibling(T ref, List<T> list) {
		long start = System.currentTimeMillis();
		logger.trace("-- nearestSibling start");
		double max = Double.MAX_VALUE;
		int idx = 0, resIdx = 0;
		T tmp = null;
		for (T d : list) {
			double cur = ref.distance(d);
			if (max > cur) {
				tmp = d;
				max = cur;
				resIdx = idx;
			}
			idx = idx++;
		}
		logger.trace("-- nearestSibling End. Total Time : {}s --", Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
		return new DistanceResultDto<T>(resIdx, tmp, max);
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
	public DistanceResultDto<T> nearestSibling(T ref, List<T> list, HashMap<Integer, T> exclude) {
		long start = System.currentTimeMillis();
		logger.trace("-- nearestSibling start");
		double max = Double.MAX_VALUE;
		int idx = 0, resIdx = 0;
		T tmp = null;
		for (T d : list) {
			
				double cur = ref.distance(d);
				//logger.debug("nearestSibling ref {}, max dist {}, current {} (hash : {}) from distance {}, processed {}",ref,max,d,d.hashCode(),cur,exclude);
				if (!exclude.containsKey(d.hashCode())) {
					if (max > cur) {
						tmp = d;
						max = cur;
						resIdx = idx;
					}
			}
			idx = idx++;
		}
		//logger.debug("nearestSibling ref {}, selected {} from distance {}",ref,tmp,max);
		logger.trace("-- nearestSibling End. Total Time : {}s --", Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
		return new DistanceResultDto<T>(resIdx, tmp, max);
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
	public DistanceResultDto<T> farthestSibling(T ref, List<T> list) {
		long start = System.currentTimeMillis();
		logger.trace("-- farthestSibling start");
		double max = 0;
		int idx = 0, resIdx = 0;
		T tmp = null;
		for (T d : list) {
			double cur = ref.distance(d);
			if (max < cur) {
				tmp = d;
				max = cur;
				resIdx = idx;
			}
			idx = idx++;
		}
		logger.trace("-- farthestSibling End. Total Time : {}s --", Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
		return new DistanceResultDto<T>(resIdx, tmp, max);
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
	public DistanceResultDto<T> farthestSibling(T ref, List<T> list, HashMap<Integer, T> exclude) {
		long start = System.currentTimeMillis();
		logger.trace("-- farthestSibling start");
		double max = 0;
		int idx = 0, resIdx = 0;
		T tmp = null;
		for (T d : list) {
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
		logger.trace("-- farthestSibling End. Total Time : {}s --", Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
		return new DistanceResultDto<T>(resIdx, tmp, max);
	}
	
}
