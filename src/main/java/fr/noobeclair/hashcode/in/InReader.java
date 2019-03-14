package fr.noobeclair.hashcode.in;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.utils.Utils;

public abstract class InReader {
	
	protected static final Logger logger = LogManager.getLogger(InReader.class);
	
	public InReader() {
		// useless constructor
	}
	
	public BeanContainer read(String in) {
		long start = System.currentTimeMillis();
		logger.info("-- Read start : {}", in);
		try {
			return readFile(in);
		} finally {
			logger.info("-- Read End. Total Time : {}s --", Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
		}
	}
	
	protected abstract BeanContainer readFile(String in);
	
}
