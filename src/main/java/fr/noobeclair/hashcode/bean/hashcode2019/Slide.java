package fr.noobeclair.hashcode.bean.hashcode2019;

import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

import fr.noobeclair.hashcode.bean.Bean;

public class Slide extends Bean {
	
	private Photo photo1;
	private Photo photo2;
	
	public Slide() {
		// TODO Auto-generated constructor stub
	}
	
	public Set<String> getTags() {
		Set<String> tags = new TreeSet<>();
		tags.addAll(this.photo1.getTags());
		
		if (this.photo2 != null) {
			tags.addAll(this.photo2.getTags());
		}
		
		return tags;
	}

	public Photo getPhoto1() {
		return photo1;
	}

	public void setPhoto1(Photo photo1) {
		this.photo1 = photo1;
	}

	public Photo getPhoto2() {
		return photo2;
	}

	public void setPhoto2(Photo photo2) {
		this.photo2 = photo2;
	}

	public Slide(Photo photo1, Photo photo2) {
		super();
		this.photo1 = photo1;
		this.photo2 = photo2;
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
		result = prime * result + ((photo1 == null) ? 0 : photo1.hashCode());
		result = prime * result + ((photo2 == null) ? 0 : photo2.hashCode());
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
		Slide other = (Slide) obj;
		if (photo1 == null) {
			if (other.photo1 != null)
				return false;
		} else if (!photo1.equals(other.photo1))
			return false;
		if (photo2 == null) {
			if (other.photo2 != null)
				return false;
		} else if (!photo2.equals(other.photo2))
			return false;
		return true;
	}


	@Override
	public int realcompareTo(Bean b) {
		if (getClass() != b.getClass()) {
			throw new RuntimeException("this("+this+") is not same class as b"+b);
		} else {
			Slide s = (Slide) b;
			return this.getTags().size() < s.getTags().size() ? 0 : 1;
		}
	}

	@Override
	public String toString() {
		return new StringBuilder(photo1.getId()).append(photo2 != null ?photo2.getId() : StringUtils.EMPTY).append(StringUtils.CR).toString();
	}
	
	

	
	
	

}
