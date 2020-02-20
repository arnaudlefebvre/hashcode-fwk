package fr.noobeclair.hashcode.out.hashcode2020;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import fr.noobeclair.hashcode.bean.hashcode2020.HashCode2020BeanContainer;
import fr.noobeclair.hashcode.bean.hashcode2020.Out;
import fr.noobeclair.hashcode.out.OutWriter;

public class Hashcode2020Writer extends OutWriter<HashCode2020BeanContainer> {
	
	private static final String LINE = "\n";

    public Hashcode2020Writer() {
		
	}
	
	@Override
	protected void writeFile(HashCode2020BeanContainer out, String path) {
	    
		try (FileWriter writer = new FileWriter(path.replaceFirst("\\.txt", ".out"))) { 
		    Out outrealy = out.getOut();
		    writer.write(outrealy.getOrderedSignupLibrairiesWithScannedBooks().size() + LINE);
            for (Map.Entry<Integer, List<Integer>> entry : outrealy.getOrderedSignupLibrairiesWithScannedBooks().entrySet()) {
                System.out.println(entry.getKey() + ":" + entry.getValue());
                writer.write(entry.getKey() + " " + entry.getValue().size() + LINE);
                for(Integer idBook : entry.getValue()) {
                    writer.write(idBook + " ");
                }
                writer.write(LINE);
                System.out.println( writer.toString());
            }
		} catch (IOException e) {
			logger.error("Erreur lors de l'écriture du fichier {}", path, e);
		}
		
	}

}
