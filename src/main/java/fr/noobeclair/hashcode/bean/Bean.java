package fr.noobeclair.hashcode.bean;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.noobeclair.hashcode.utils.Utils;

/**
 * This class aims to represents any bean/data you will need to load, store,
 * process, etc
 * from a resources.
 * It is encapuslated in a BeanContainer
 * 
 * @author arnaud
 *
 */
public abstract class Bean implements Comparable<Bean> {
	
	protected static final Logger logger = LogManager.getLogger(Bean.class);
	
	/**
	 * Calculate distance from this to another bean b of same type
	 * 
	 * @param b
	 *            Bean of which we want to know distance from this
	 * @return double distance from this to b
	 */
	public abstract double realdistance(Bean b);
	
	/**
	 * Calculate hascode of this. This is an implement of hashcode
	 * Making this abstract forces to implements an hashcode method at compile
	 * 
	 * @return int hashcode
	 */
	public abstract int realhashCode();
	
	/**
	 * Calculate hascode of this. This is an implement of equals
	 * Making this abstract forces to implements an equals method at compile
	 * 
	 * @return boolean true if equals
	 */
	public abstract boolean realequals(Object obj);
	
	/**
	 * Compare bean to this. This is an implement of compareTo
	 * Making this abstract forces to implements an compareTo method at compile
	 * 
	 * @return int compare
	 */
	public abstract int realcompareTo(Bean b);
	
	public double distance(Bean b) {
		long start = System.currentTimeMillis();
		logger.trace("-- distance start : {}", b);
		try {
			return realdistance(b);
		} finally {
			logger.trace("-- distance End. Total Time : {}s --", Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
		}
	}
	
	@Override
	public int hashCode() {
		long start = System.currentTimeMillis();
		logger.trace("-- hashCode start");
		try {
			return realhashCode();
		} finally {
			logger.trace("-- hashCode End. Total Time : {}s --", Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		long start = System.currentTimeMillis();
		logger.trace("-- equals start : {}", obj);
		try {
			return realequals(obj);
		} finally {
			logger.trace("-- equals End. Total Time : {}s --", Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
		}
	}
	
	@Override
	public int compareTo(Bean b) {
		long start = System.currentTimeMillis();
		logger.trace("-- compareTo start : {}", b);
		try {
			return realcompareTo(b);
		} finally {
			logger.trace("-- compareTo End. Total Time : {}s --", Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
		}
	}
	
}
