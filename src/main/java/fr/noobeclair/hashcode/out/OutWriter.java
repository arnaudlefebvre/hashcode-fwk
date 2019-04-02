package fr.noobeclair.hashcode.out;

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
public abstract class OutWriter<T extends BeanContainer> {
	
	protected static final Logger logger = LogManager.getLogger(OutWriter.class);
	
	public OutWriter() {
		// useless constructor
	}
	
	/**
	 * write errors and duration handler.
	 * 
	 * @see fr.noobeclair.hashcode.out.OutWriter.writeFile(T out,String)
	 * @param out
	 *            <T extends BeanContainer> container of data to write out
	 * @param path
	 *            file path to write to
	 * @return <T extends BeanContainer> data
	 */
	public T write(T out, String path) {
		long start = System.currentTimeMillis();
		logger.debug("-- Write start : {}", path);
		try {
			if (out != null) {
				writeFile(out, path);
			} else {
				logger.error("in null !");
			}
			return out;
		} finally {
			logger.debug("-- Write End({}). Total Time : {}s --", path, Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
		}
	}
	
	/**
	 * Write data from T extends BeanContainer into file path
	 * 
	 * @param out
	 *            T extends BeanContainer
	 * @param path
	 *            file path to target
	 */
	protected abstract void writeFile(T out, String path);
	
}
