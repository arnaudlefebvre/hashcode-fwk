package fr.noobeclair.hashcode.solve;

import java.util.ArrayList;
import java.util.List;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.bean.Config;

public class SolverFactory<T Solver<>> {
	
	public List<Solver<T, V>> createFromConfs(List<V> configs) {
		return createFromConfs(configs, Solver.DISABLE_TIMEOUT);
	}
	
	public List<Solver<T, V>> createFromConfs(List<V> configs, long timeout) {
		List<Solver<T, V>> res = new ArrayList<>();
		for (V c : configs) {
			res.add(Solver<>);
		}
		return res;
	}
}
