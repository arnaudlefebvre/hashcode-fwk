package fr.noobeclair.hashcode.bean;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.utils.Utils;

public abstract class Bean implements Comparable<Bean> {
	
	protected static final Logger logger = LogManager.getLogger(InReader.class);
	
	public abstract double realdistance(Bean b);
	
	public abstract int realhashCode();
	
	public abstract boolean realequals(Object obj);
	
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
