package fr.noobeclair.hashcode.bean.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.noobeclair.hashcode.bean.config.param.ConfigParam;

public abstract class AbstractFactory<C extends Config> {
	public enum TYPE {
		INT, LONG, DOUBLE, BIGDEC, BOOLEAN, ENUM
	}
	
	protected Class<C> c;
	protected List<ConfigParam<C>> cfgparams = new ArrayList<>();
	protected List<C> result = new ArrayList<>();
	protected C emptyConf = null;
	
	public AbstractFactory(Class<C> c) {
		this.c = c;
	}
	
	public abstract List<C> generate(C config) throws InstantiationException, IllegalAccessException;
	
	private void processConfigParam() {
		//System.out.println("SIZE : " + cfgparams.size());
		int size = cfgparams.size();
		for (int i = 0; i < size; i++) {
			int nbConf = cfgparams.get(i).getNbConf();
			int repeatEach = getPrevCfParamDim(i);
			int repeatBloc = result.size() / (repeatEach * nbConf);
			for (int bloc = 0; bloc < repeatBloc; bloc++) {
				for (int nb = 0; nb < nbConf; nb++) {
					cfgparams.get(i).reset("" + nb);
					for (int repeat = 0; repeat < repeatEach; repeat++) {
						int n = (nb == 0 ? 0 : nb * repeatEach);
						int b = (bloc == 0 ? 0 : bloc * nbConf);
						int r = (repeat == 0 ? 0 : repeat);
						int idx = b + n + r;
						if (nb == 0 && bloc == 00 && repeat == 0) {
							idx = 0;
						}
						//System.out.println("i : " + i + ",n : " + n + ", b : " + b + ", r : " + r + ",idx : " + idx + ", NB : " + nbConf + ",REPEAT : " + repeatEach + ",BLOC : " + repeatBloc);
						result.set(idx, cfgparams.get(i).go(result.get(idx)));
						cfgparams.get(i).reset("" + nb);
					}
				}
			}
		}
	}
	
	private List<C> initEmptyResultList() throws InstantiationException, IllegalAccessException {
		int nb = calcAllnumber();
		List<C> result = new ArrayList<>(nb);
		for (int i = 0; i < nb; i++) {
			result.add(initEmptyConfig());
		}
		return result;
	}
	
	private void sortConfigParams() {
		Collections.sort(cfgparams);
	}
	
	private C initEmptyConfig() throws InstantiationException, IllegalAccessException {
		C res = c.newInstance();
		for (int i = 0; i < cfgparams.size(); i++) {
			res = cfgparams.get(i).go(res);
			cfgparams.get(i).reset();
		}
		return res;
	}
	
	private Integer calcAllnumber() {
		int res = 1;
		for (int i = 0; i < cfgparams.size(); i++) {
			res = res * cfgparams.get(i).getNbConf();
		}
		return res;
	}
	
	private Integer getPrevCfParamDim(int i) {
		int res = 1;
		if (i > 0) {
			for (int j = i - 1; j >= 0; j--) {
				res = res * cfgparams.get(j).getNbConf();
			}
		}
		return res;
	}
	
	private Integer getNextCfParamDim(int i) {
		int res = 1;
		for (int j = i + 1; j < cfgparams.size(); j++) {
			res = res * cfgparams.get(j).getNbConf();
		}
		return res;
	}
	
	private List<C> goGenConf(ConfigParam<C> cp) throws InstantiationException, IllegalAccessException {
		List<C> result = new ArrayList<>();
		C res = c.newInstance();
		for (int i = 0; i < cp.getNbConf(); i++) {
			result.add(cp.go(res));
		}
		return result;
	}
	
	private C addConf(int c, C toAdd) {
		if (c >= cfgparams.size()) {
			System.out.println("End");
			return null;
		}
		for (int i = c; i < cfgparams.size(); i++) {
			int next = i + 1;
			C tmp = cfgparams.get(i).go(toAdd);
			while (tmp != null) {
				System.out.println("curr : " + c + ", tmp : " + tmp);
				toAdd = tmp;
				tmp = addConf(next, toAdd);
				if (tmp != null) {
					toAdd = tmp;
					result.add(toAdd);
					System.out.println("Add : " + toAdd);
				}
				tmp = cfgparams.get(i).go(toAdd);
			}
			cfgparams.get(i).reset();
			
		}
		return toAdd;
	}
	
}
