/**
 * 
 */
package fr.noobeclair.hashcode.in.custom;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.noobeclair.hashcode.bean.custom.CustomBeanContainer;
import fr.noobeclair.hashcode.in.InReader;

public class CustomReader extends InReader<CustomBeanContainer> {
	
	public CustomReader() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected CustomBeanContainer readFile(String in) {
		CustomBeanContainer result = null;
		try (Stream<String> stream = Files.lines(Paths.get(in))) {
			List<String> lines = stream.collect(Collectors.toList());
			int i = 0;
			for (String ln : lines) {
				// TODO Do something
			}
			return result;
			
		} catch (Exception e) {
			logger.error("Erreur lors de la lecture du fichier {}", in, e);
		}
		return result;
	}
	
}
