package fr.noobeclair.hashcode.bean.hashcode2019;

import java.math.BigDecimal;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

import fr.noobeclair.hashcode.bean.Bean;
import fr.noobeclair.hashcode.bean.Scorable;

public class Slide extends Bean implements Scorable<Slide> {
	
	private Photo photo1;
	private Photo photo2;
	private Set<String> tags;
	
	public Slide() {
		
	}
	
	public Set<String> getTags() {
		if (tags == null) {
			tags = new TreeSet<>();
			tags.addAll(this.photo1.getTags());
			
			if (this.photo2 != null) {
				tags.addAll(this.photo2.getTags());
			}
			
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
		BigDecimal s = score((Slide)b);
		if (BigDecimal.ZERO.equals(s)) {
			return 1;
		}
		return BigDecimal.ONE.divide(s).doubleValue();
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
			throw new RuntimeException("this(" + this + ") is not same class as b" + b);
		} else {
			Slide s = (Slide) b;
			return this.getTags().size() < s.getTags().size() ? 0 : 1;
		}
	}
	
	@Override
	public String toString() {
		//return new StringBuilder(photo1.getId()).append(photo2 != null ? photo2.getId() : StringUtils.EMPTY).append(StringUtils.CR).toString();
		return "S["+photo1.toString()+"-"+photo2+"]";
	}

	@Override
	public BigDecimal score(Slide in) {
		int onlyInThis = 0;
		int onlyInIn = 0;
		int common = 0;
		
		for (String tag : this.getTags()) {
			if (in.getTags().contains(tag)) {
				common = common +1;
			} else {
				onlyInThis = onlyInThis +1;
			}
		}
		
		for (String tag : in.getTags()) {
			if (!this.getTags().contains(tag)) {
				onlyInIn = onlyInIn + 1;
			}
		}
		
		
		return new BigDecimal(Math.min(onlyInThis,  Math.min(onlyInIn, common)));
	}
	
}
