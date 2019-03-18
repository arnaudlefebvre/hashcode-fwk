package fr.noobeclair.hashcode.out;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.utils.Utils;

public abstract class OutWriter<T extends BeanContainer> {
	
	protected static final Logger logger = LogManager.getLogger(OutWriter.class);
	
	public OutWriter() {
		// useless constructor
	}
	
	public T write(T out, String path) {
		long start = System.currentTimeMillis();
		logger.debug("-- Write start : {}", path);				
		try {
			if (out != null) {
				writeFile(out, path);
			} else {
				logger.error(" <###----- !!!!!! -----#> in null !");
			}
			return out;
		} finally {
			logger.info("-- Write End({}). Total Time : {}s --",path, Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
		}
	}
	
	protected abstract void writeFile(T out, String path);
	
}
