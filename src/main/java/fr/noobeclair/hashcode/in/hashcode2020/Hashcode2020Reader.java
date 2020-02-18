package fr.noobeclair.hashcode.in.hashcode2020;

import java.util.ArrayList;
import java.util.List;

import fr.noobeclair.hashcode.bean.hashcode2019.HashCode2019BeanContainer;
import fr.noobeclair.hashcode.bean.hashcode2019.Photo;
import fr.noobeclair.hashcode.bean.hashcode2020.HashCode2020BeanContainer;
import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.utils.FileUtils;

public class Hashcode2020Reader extends InReader<HashCode2020BeanContainer> {

	public Hashcode2020Reader() {

	}

	@Override
	protected HashCode2020BeanContainer readFile(List<String> lines, String in) {
		HashCode2020BeanContainer result = new HashCode2020BeanContainer(in);

		String[] conf = FileUtils.getTabFromLineSpace(lines, 0);
		// TODO 2020 : récupérer les éléments d'une ligne et balancer le bouzin dans les propriétés de HashCode2020BeanContainer

		return result;
	}

}
