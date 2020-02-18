package fr.noobeclair.hashcode.bean.hashcode2020;

import fr.noobeclair.hashcode.bean.BeanContainer;
import lombok.Data;

@Data
public class HashCode2020BeanContainer extends BeanContainer {
	
	// TODO 2020 : ajouter les propriétés nécessaires au traitement des données

	public HashCode2020BeanContainer(String inName) {
		super(inName);
	}

	@Override
	public Object getNew() {
		// Add custom properties copy
		// this.mycustom properties = o.custom;
		return this;
	}
	
}
