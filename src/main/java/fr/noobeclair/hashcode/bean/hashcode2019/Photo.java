package fr.noobeclair.hashcode.bean.hashcode2019;

import java.util.List;

import fr.noobeclair.hashcode.bean.Bean;

public class Photo extends Bean {
	
	private int id;
	private String sens;
	private List<String> tags;
	
	public Photo() {
		
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getSens() {
		return sens;
	}
	
	public void setSens(String sens) {
		this.sens = sens;
	}
	
	public List<String> getTags() {
		return tags;
	}
	
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
	public Photo(int id, String sens, List<String> tags) {
		super();
		this.id = id;
		this.sens = sens;
		this.tags = tags;
	}
	
	@Override
	public double realdistance(Bean b) {
		Photo p = (Photo) b;
		int onlyInThis = 0;
		int onlyInIn = 0;
		int common = 0;
		
		for (String tag : this.getTags()) {
			if (p.getTags().contains(tag)) {
				common = common +1;
			} else {
				onlyInThis = onlyInThis +1;
			}
		}
		
		for (String tag : p.getTags()) {
			if (!this.getTags().contains(tag)) {
				onlyInIn = onlyInIn + 1;
			}
		}
		int n = Math.min(onlyInIn,Math.min(onlyInThis,common));
		if (n!= 0) {
			return 1/n;
		}
		return 0;
	}
	
	public int getTagEnCommun(Photo photo) {
		int res = 0;
		for (String tag : tags) {
			for (String otherTag : photo.tags) {
				if (tag.equals(otherTag)) {
					res = res + 1;
				}
			}
		}
		return res;
	}
	
	@Override
	public int realhashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((sens == null) ? 0 : sens.hashCode());
		result = prime * result + ((tags == null) ? 0 : tags.hashCode());
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
		Photo other = (Photo) obj;
		if (id != other.id)
			return false;
		if (sens == null) {
			if (other.sens != null)
				return false;
		} else if (!sens.equals(other.sens))
			return false;
		if (tags == null) {
			if (other.tags != null)
				return false;
		} else if (!tags.equals(other.tags))
			return false;
		return true;
	}
	
	@Override
	public int realcompareTo(Bean b) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		return "P[id=" + id + ",s=" + sens + ",tags=" + tags + "]";
	}
	
	
}
