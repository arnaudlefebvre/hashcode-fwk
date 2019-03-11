package fr.noobeclair.hashcode.in;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.bean.hashcode2019.HashCode2019BeanContainer;
import fr.noobeclair.hashcode.bean.hashcode2019.Photo;
import fr.noobeclair.hashcode.utils.ReadFileUtil;

public class Hashcode2019Reader extends InReader {

	public Hashcode2019Reader() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected BeanContainer readFile(String in) {
		HashCode2019BeanContainer result = new HashCode2019BeanContainer();
		try (Stream<String> stream = Files.lines(Paths.get(in))) {

			List<String> lines = stream.collect(Collectors.toList());
			lines.stream().forEach(System.out::println);
			String[] conf = ReadFileUtil.getTabFromLineSpace(lines, 0);
			result.setTotalPhotos(Integer.valueOf(conf[0]));
			result.setPhotos(readLine(lines));
			
		} catch (Exception e) {
			logger.error("Erreur lors de la lecture du fichier {}",in);
		}
		return result;
	}
	
	private List<Photo> readLine(List<String> lines) {
		List<Photo> listPhoto = new ArrayList<>();
		int index = 0;
		for(String line : lines) {
			if (index != 0) {
				String[] linePhoto = ReadFileUtil.getTabFromLineSpace(lines, index);
				String sens = linePhoto[0];
				int nbTag = Integer.valueOf(linePhoto[1]);
				List<String> tags = new ArrayList<>();
				
				for(int tagIndex = 2; tagIndex < nbTag+2;tagIndex++) {
					tags.add(linePhoto[tagIndex]);
				}
				listPhoto.add(new Photo(index - 1, sens, tags));
			}
			index++;
		}
		return listPhoto;
	}
	
}
