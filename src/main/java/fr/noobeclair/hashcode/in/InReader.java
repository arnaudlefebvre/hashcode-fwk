package fr.noobeclair.hashcode.in;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
 *        BeanContainer
 */
public abstract class InReader<T extends BeanContainer> {
	/**
	 * Cache of in resource - BeanContainer
	 */
	protected HashMap<Integer, List<String>> cache = new HashMap<>();
	protected static final Logger logger = LogManager.getLogger(InReader.class);

	public InReader() {
		// useless constructor
	}

	/**
	 * Read errors duration, and cache handler.
	 * 
	 * @see fr.noobeclair.hashcode.in.InReader.readFile(String)
	 * @param in
	 *           String in file path
	 * @return <T extends BeanContainer> data from in file
	 */
	public T read(String in) {
		long start = System.currentTimeMillis();
		logger.debug("-- Read start : {}", in);
		try {
			T data = readFile(getFileContent(in), in);
			return data;
		} finally {
			logger.debug("-- Read End ({}). Total Time : {}s --", in,
					Utils.roundMiliTime((System.currentTimeMillis() - start), 3));
		}
	}

	/**
	 * Read file with path in, and load into T BeanContainer
	 * 
	 * @param in
	 * @return T extends BeanContainer
	 */
	protected abstract T readFile(List<String> lines, String in);

	private List<String> getFileContent(String in) {
		List<String> lines = null;
		if (!cache.containsKey(in.hashCode())) {
			try (Stream<String> stream = Files.lines(Paths.get(in))) {
				lines = stream.collect(Collectors.toList());
				cache.put(in.hashCode(), lines);
			} catch (Exception e) {
				logger.error("Erreur lors de la lecture du fichier {}", in, e);
				throw new RuntimeException(e);
			}
		} else {
			lines = cache.get(in.hashCode());
		}
		return lines;
	}
}
