package fr.noobeclair.hashcode.out;

import fr.noobeclair.hashcode.bean.BeanContainer;

public class NullWriter<T extends BeanContainer> extends OutWriter<T> {

	public NullWriter() {

	}

	@Override
	public void writeFile(BeanContainer out, String path) {
	}

}
