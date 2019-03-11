package fr.noobeclair.hashcode.bean.hashcode2019;

import java.util.List;

import fr.noobeclair.hashcode.bean.BeanContainer;

public class HashCode2019BeanContainer extends BeanContainer {

	public HashCode2019BeanContainer() {
		// TODO Auto-generated constructor stub
	}
	
	private int totalPhotos;
	private List<Photo> photos;
	private List<Slide> slides;
	private SlideShow slideshow;
	public int getTotalPhotos() {
		return totalPhotos;
	}
	public void setTotalPhotos(int totalPhotos) {
		this.totalPhotos = totalPhotos;
	}
	public List<Photo> getPhotos() {
		return photos;
	}
	public void setPhotos(List<Photo> photos) {
		this.photos = photos;
	}
	public List<Slide> getSlides() {
		return slides;
	}
	public void setSlides(List<Slide> slides) {
		this.slides = slides;
	}
	public SlideShow getSlideshow() {
		return slideshow;
	}
	public void setSlideshow(SlideShow slideshow) {
		this.slideshow = slideshow;
	}

}
