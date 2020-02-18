package fr.noobeclair.hashcode.bean.hashcode2019;

import java.util.List;

import fr.noobeclair.hashcode.bean.BeanContainer;
import lombok.Data;

@Data
public class HashCode2019BeanContainer extends BeanContainer {

	public HashCode2019BeanContainer(String inName) {
		super(inName);
	}

	@Override
	public Object getNew() {
		// Add custom properties copy
		// this.mycustom properties = o.custom;
		return this;
	}

	private int totalPhotos;
	private List<Photo> photos;
	private List<Photo> listVerticalPhoto;
	private List<Slide> slides;
	private SlideShow slideshow;

}
