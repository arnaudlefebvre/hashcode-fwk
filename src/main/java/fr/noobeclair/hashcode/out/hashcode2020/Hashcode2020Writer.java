package fr.noobeclair.hashcode.out.hashcode2020;

import java.io.FileWriter;
import java.io.IOException;

import fr.noobeclair.hashcode.bean.hashcode2020.HashCode2020BeanContainer;
import fr.noobeclair.hashcode.out.OutWriter;

public class Hashcode2020Writer extends OutWriter<HashCode2020BeanContainer> {
	
	public Hashcode2020Writer() {
		
	}
	
	@Override
	protected void writeFile(HashCode2020BeanContainer out, String path) {
		try (FileWriter writer = new FileWriter(path.replaceFirst("\\.txt", ".out"))) {
			// TODO 2020 : écrire le fichier
		} catch (IOException e) {
			logger.error("Erreur lors de l'écriture du fichier {}", path, e);
		}
		
	}

}
