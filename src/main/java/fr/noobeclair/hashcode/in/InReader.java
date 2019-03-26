package fr.noobeclair.hashcode.in;

import java.util.Map;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.utils.Utils;

public abstract class InReader<T extends BeanContainer> {
	
	protected Map<String, T> cache = new TreeMap<>();
	protected static final Logger logger = LogManager.getLogger(InReader.class);
	
	public InReader() {
		// useless constructor
	}
	
	public T read(String in) {
		long start = System.currentTimeMillis();
		logger.debug("-- Read start : {}", in);
		try {
			if (cache.containsKey(in)) {
				return cache.get(in);
			} else {
				T data = readFile(in);
				cache.put(in, data);
				return data;
			}
		} finally {
			logger.debug("-- Read End ({}). Total Time : {}s --", in, Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
		}
	}
	
	protected abstract T readFile(String in);
	
}
