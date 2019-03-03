package fr.noobeclair.hashcode.in;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.utils.Utils;

public abstract class InReader {
	
	private static final Logger logger = LogManager.getLogger(InReader.class);
	
	private InReader() {
		//useless constructor
	}
	
	public BeanContainer read(String in) {
		long start = System.currentTimeMillis();
		logger.debug("-- Read start : {}",in);
		try {
			return readFile(in);
		} finally {
			logger.debug("-- End. Total Time : {}s --",Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
		}
	}
	
	public abstract BeanContainer readFile(String in);

}
