package fr.noobeclair.hashcode.out;

import java.io.FileWriter;
import java.io.IOException;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.bean.hashcode2019.HashCode2019BeanContainer;
import fr.noobeclair.hashcode.bean.hashcode2019.Slide;

public class Hashcode2019Writer extends OutWriter {

	public Hashcode2019Writer() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void writeFile(BeanContainer out, String path) {
		HashCode2019BeanContainer result = (HashCode2019BeanContainer) out;
		try (FileWriter writer = new FileWriter(path.replaceFirst("\\.txt", ".out"))) {
			for (Slide s : result.getSlideshow().getSlides()) {
				writer.write(s.toString());
			}			
		} catch (IOException e) {
			logger.error("Erreur lors de l'écriture du fichier {}",path,e);
		}
		
	}
	
}
