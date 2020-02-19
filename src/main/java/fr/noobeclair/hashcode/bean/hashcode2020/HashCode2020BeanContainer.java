package fr.noobeclair.hashcode.bean.hashcode2020;

import java.util.List;

import fr.noobeclair.hashcode.bean.BeanContainer;
import lombok.Data;

@Data
public class HashCode2020BeanContainer extends BeanContainer {
	
	// TODO 2020 : ajouter les propriétés nécessaires au traitement des données

	

	private int intotalslice;
	
	private int intotaltypepizza;
	
	private List<Pizza> inPizzas;
	
	private int outTotalTypePizza;
	
	private List<Pizza> outPizzas;
	
	
	public HashCode2020BeanContainer(String inName) {
		super(inName);
	}

	@Override
	public Object getNew() {
		// Add custom properties copy
		// this.mycustom properties = o.custom;
		return this;
	}

	public int getIntotalslice() {
		return intotalslice;
	}

	public void setIntotalslice(int intotalslice) {
		this.intotalslice = intotalslice;
	}

	public int getIntotaltypepizza() {
		return intotaltypepizza;
	}

	public void setIntotaltypepizza(int intotaltypepizza) {
		this.intotaltypepizza = intotaltypepizza;
	}

	public List<Pizza> getInPizzas() {
		return inPizzas;
	}

	public void setInPizzas(List<Pizza> inPizzas) {
		this.inPizzas = inPizzas;
	}

	public int getOutTotalTypePizza() {
		return outTotalTypePizza;
	}

	public void setOutTotalTypePizza(int outTotalTypePizza) {
		this.outTotalTypePizza = outTotalTypePizza;
	}

	public List<Pizza> getOutPizzas() {
		return outPizzas;
	}

	public void setOutPizzas(List<Pizza> outPizzas) {
		this.outPizzas = outPizzas;
	}
	
}
