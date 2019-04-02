package fr.noobeclair.hashcode.out.custom;

import java.io.FileWriter;
import java.io.IOException;

import fr.noobeclair.hashcode.bean.custom.CustomBeanContainer;
import fr.noobeclair.hashcode.out.OutWriter;

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
