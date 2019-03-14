package fr.noobeclair.hashcode.bean.hashcode2019;

import java.util.List;

import fr.noobeclair.hashcode.bean.Bean;

public class SlideShow extends Bean {
	
	private List<Slide> slides;
	
	public SlideShow() {
		// TODO Auto-generated constructor stub
	}

	public SlideShow(List<Slide> slides) {
		super();
		this.slides = slides;
	}

	public List<Slide> getSlides() {
		return slides;
	}

	public void setSlides(List<Slide> slides) {
		this.slides = slides;
	}

	@Override
	public double realdistance(Bean b) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int realhashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((slides == null) ? 0 : slides.hashCode());
		return result;
	}

	@Override
	public boolean realequals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SlideShow other = (SlideShow) obj;
		if (slides == null) {
			if (other.slides != null)
				return false;
		} else if (!slides.equals(other.slides))
			return false;
		return true;
	}
	
	@Override
	public int realcompareTo(Bean b) {
		throw new UnsupportedOperationException();
	}
	

}
