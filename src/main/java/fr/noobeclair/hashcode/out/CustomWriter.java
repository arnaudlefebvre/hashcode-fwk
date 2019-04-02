package fr.noobeclair.hashcode.out;

import java.io.FileWriter;
import java.io.IOException;

import fr.noobeclair.hashcode.bean.CustomBeanContainer;

public class CustomWriter extends OutWriter<CustomBeanContainer> {
	
	public CustomWriter() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void writeFile(CustomBeanContainer out, String path) {
		try (FileWriter writer = new FileWriter(path)) {
			// Do something
		} catch (IOException e) {
			logger.error("Erreur lors de l'écriture du fichier {}", path, e);
		}
	}
	
}
