package fr.noobeclair.hashcode.in.hashcode2019;

import java.util.ArrayList;
import java.util.List;

import fr.noobeclair.hashcode.bean.hashcode2019.HashCode2019BeanContainer;
import fr.noobeclair.hashcode.bean.hashcode2019.Photo;
import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.utils.FileUtils;

public class Hashcode2019Reader extends InReader<HashCode2019BeanContainer> {

	public Hashcode2019Reader() {

	}

	@Override
	protected HashCode2019BeanContainer readFile(List<String> lines, String in) {
		HashCode2019BeanContainer result = new HashCode2019BeanContainer(in);

		// lines.stream().forEach(System.out::println);
		String[] conf = FileUtils.getTabFromLineSpace(lines, 0);
		result.setTotalPhotos(Integer.valueOf(conf[0]));
		result.setPhotos(readLine(lines));

		return result;
	}

	private List<Photo> readLine(List<String> lines) {
		List<Photo> listPhoto = new ArrayList<>();
		int index = 0;
		for (String line : lines) {
			if (index != 0) {
				String[] linePhoto = FileUtils.getTabFromLineSpace(lines, index);
				String sens = linePhoto[0];
				long nbTag = Integer.valueOf(linePhoto[1]);
				List<String> tags = new ArrayList<>();

				for (int tagIndex = 2; tagIndex < nbTag + 2; tagIndex++) {
					tags.add(linePhoto[tagIndex]);
				}
				listPhoto.add(new Photo(index - 1, sens, tags));
			}
			index++;
		}
		return listPhoto;
	}

}
