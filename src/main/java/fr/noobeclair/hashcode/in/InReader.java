package fr.noobeclair.hashcode.in;

import java.util.Map;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.utils.Utils;

/**
 * InReader, must be extended
 * Handle read of a file resources and caching of BeanContainer
 * 
 * @author arnaud
 *
 * @param <T>
 *            BeanContainer
 */
public abstract class InReader<T extends BeanContainer> {
	/**
	 * Cache of in resource - BeanContainer
	 */
	protected Map<String, T> cache = new TreeMap<>();
	protected static final Logger logger = LogManager.getLogger(InReader.class);
	
	public InReader() {
		// useless constructor
	}
	
	/**
	 * Read errors duration, and cache handler.
	 * 
	 * @see fr.noobeclair.hashcode.in.InReader.readFile(String)
	 * @param in
	 *            String in file path
	 * @return <T extends BeanContainer> data from in file
	 */
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
	
	/**
	 * Read file with path in, and load into T BeanContainer
	 * 
	 * @param in
	 * @return T extends BeanContainer
	 */
	protected abstract T readFile(String in);
	
}
