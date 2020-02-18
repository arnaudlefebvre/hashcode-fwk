package fr.noobeclair.hashcode.solve.hashcode2020;

import fr.noobeclair.hashcode.bean.hashcode2020.H2020Config;
import fr.noobeclair.hashcode.bean.hashcode2020.HashCode2020BeanContainer;
import fr.noobeclair.hashcode.solve.ConfigSolver;
import fr.noobeclair.hashcode.solve.Solver;

public class HashCode2020Solver extends ConfigSolver<HashCode2020BeanContainer, H2020Config> {

	public HashCode2020Solver() {
		super();
	}

	@Override
	protected HashCode2020BeanContainer runWithStat(HashCode2020BeanContainer data) {
		HashCode2020BeanContainer datas = data;
		long start = System.currentTimeMillis();
		
		// TODO 2020 : faire l'algo
		
		return datas;
	}

	@Override
	protected void addConfigStats() {
		// TODO Auto-generated method stub
		
	}


}
