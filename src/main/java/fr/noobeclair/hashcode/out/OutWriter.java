package fr.noobeclair.hashcode.out;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.utils.Utils;

public abstract class OutWriter {

	private static final Logger logger = LogManager.getLogger(OutWriter.class);
	
	public OutWriter() {
		// useless constructor
	}
	
	public void write(BeanContainer out, String path) {
		long start = System.currentTimeMillis();
		logger.debug("-- Write start : {}",path);
		try {
			writeFile(out, path);
		} finally {
			logger.debug("-- Write End. Total Time : {}s --",Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
		}
	}
	
	public abstract void writeFile(BeanContainer out, String path);

}
