package fr.noobeclair.hashcode.bean.config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Preconditions;

import fr.noobeclair.hashcode.annotation.ConfGenerable;
import fr.noobeclair.hashcode.bean.config.param.BigDecConfigParam;
import fr.noobeclair.hashcode.bean.config.param.ConfigParam;
import fr.noobeclair.hashcode.bean.config.param.DoubleConfigParam;
import fr.noobeclair.hashcode.bean.config.param.EnumConfigParam;
import fr.noobeclair.hashcode.bean.config.param.IntegerConfigParam;
import fr.noobeclair.hashcode.bean.config.param.LongConfigParam;

public class ConfigFactory<C extends Config> extends AbstractFactory<C> {
	
	Class<C> c;
	List<ConfigParam<C>> cfgparams = new ArrayList<>();
	List<C> result = new ArrayList<>();
	C emptyConf = null;
	
	public ConfigFactory(Class<C> c) {
		super(c);
	}
	
	@Override
	public List<C> generate(C config) throws InstantiationException, IllegalAccessException {
		result = new ArrayList<>();
		cfgparams = new ArrayList<>();
		C res = c.newInstance();
		for (Field f : c.getDeclaredFields()) {
			if (f.isAnnotationPresent(ConfGenerable.class)) {
				ConfGenerable cf = f.getAnnotation(ConfGenerable.class);
				validateAddConfParam(cf, f);
			}
		}
		sortConfigParams();
		result = initEmptyResultList();
		System.out.println("Nb conf " + calcAllnumber());
		processConfigParam();
		//System.out.println("Result " + result);
		return result;
	}
	
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
	
	public List<C> oldgenerate(C config) throws InstantiationException, IllegalAccessException {
		result = new ArrayList<>();
		cfgparams = new ArrayList<>();
		C res = c.newInstance();
		
		//if (c.isAnnotationPresent(ConfGenerable.class)) {
		for (Field f : c.getDeclaredFields()) {
			if (f.isAnnotationPresent(ConfGenerable.class)) {
				ConfGenerable cf = f.getAnnotation(ConfGenerable.class);
				validateAddConfParam(cf, f);
			}
		}
		genConf(res);
		//}
		return result;
	}
	
	private C newConf(C toAdd) throws InstantiationException, IllegalAccessException {
		C res = (C) toAdd.getClass().newInstance();
		toAdd.cloneTo(res);
		return res;
	}
	
	private List<C> genConf(C config) throws InstantiationException, IllegalAccessException {
		C toadd = newConf(config);
		
		addConf(0, toadd);
		return result;
	}
	
	private C addConfR(int c, C toAdd) {
		if (c >= cfgparams.size()) {
			result.add(toAdd);
			System.out.println("Add : " + toAdd);
			return null;
		}
		for (int i = c; i < cfgparams.size(); i++) {
			
		}
		return toAdd;
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
		//if (emptyConf == null) {
		C res = c.newInstance();
		for (int i = 0; i < cfgparams.size(); i++) {
			res = cfgparams.get(i).go(res);
			cfgparams.get(i).reset();
		}
		//			emptyConf = res;
		//		} else {
		//			res = new C(emptyConf);
		//		}
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
	
	private void validateAddConfParam(ConfGenerable cf, Field f) {
		
		Preconditions.checkNotNull("ConFGenerable.TYPE is required ", cf.type());
		TYPE type = cf.type();
		if (AbstractFactory.TYPE.ENUM == type || AbstractFactory.TYPE.BOOLEAN == type) {
			if (AbstractFactory.TYPE.ENUM == type) {
				Preconditions.checkNotNull("ConFGenerable.eClass is required ", cf.eClass());
				cfgparams.add(new EnumConfigParam<C>(f.getName(), type, cf, cf.eClass(), cf.excludes(), null));
			}
		} else {
			String min = cf.min();
			String max = cf.max();
			String step = cf.step();
			Preconditions.checkNotNull("ConFGenerable.min is required ", min);
			Preconditions.checkNotNull("ConFGenerable.max is required ", max);
			Preconditions.checkNotNull("ConFGenerable.step is required ", step);
			switch (type) {
			case INT:
				cfgparams.add(new IntegerConfigParam<C>(f.getName(), type, min, max, step, cf));
				break;
			case BIGDEC:
				cfgparams.add(new BigDecConfigParam<C>(f.getName(), type, min, max, step, cf));
				break;
			case DOUBLE:
				cfgparams.add(new DoubleConfigParam<C>(f.getName(), type, min, max, step, cf));
				break;
			case LONG:
				cfgparams.add(new LongConfigParam<C>(f.getName(), type, min, max, step, cf));
				break;
			default:
				break;
			}
		}
	}
}
