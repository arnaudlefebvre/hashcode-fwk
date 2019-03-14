package fr.noobeclair.hashcode.bean.hashcode2019;

import java.util.List;

import fr.noobeclair.hashcode.bean.Bean;

public class Photo extends Bean {
	
	private int id;
	private String sens;
	private List<String> tags;
	
	public Photo() {
		// TODO Auto-generated constructor stub
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
		// TODO Auto-generated method stub
		return 0;
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
	
}
