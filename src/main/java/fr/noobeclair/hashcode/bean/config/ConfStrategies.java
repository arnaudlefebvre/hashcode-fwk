package fr.noobeclair.hashcode.bean.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import fr.noobeclair.hashcode.bean.config.param.ConfigParam;

public class ConfStrategies<C extends Config, P extends ConfigParam<C>> {
	
	private HashMap<String, List<P>> strategies;
	private List<String> allowed = new ArrayList<>();
	
	public ConfStrategies() {
		strategies = new HashMap<>();
	}
	
	public ConfStrategies(List<String> allowed) {
		super();
		strategies = new HashMap<>();
		this.allowed = allowed;
	}
	
	public void addAll(String id, List<P> strats) {
		strategies.put(id, strats);
	}
	
	public void addOne(String stratId, P strat) {
		List<P> strats = new ArrayList<>();
		if (isAllowed(stratId)) {
			strats.add(strat);
			if (strategies.containsKey(stratId)) {
				List<P> stratsCont = strategies.get(stratId);
				strats.addAll(stratsCont);
			}
			strategies.put(stratId, strats);
		}
	}
	
	private boolean isAllowed(String id) {
		if (CollectionUtils.isNotEmpty(allowed) && allowed.contains(id)) {
			return true;
		}
		return false;
	}
	
	public void getNbConf() {
		//TODO
	}
	
	public HashMap<String, List<P>> getStrategies() {
		return strategies;
	}
	
}
