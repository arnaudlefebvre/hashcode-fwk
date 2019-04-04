package fr.noobeclair.hashcode.bean.config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;

import fr.noobeclair.hashcode.annotation.ConfGenerable;
import fr.noobeclair.hashcode.annotation.ConfStratEnabled;
import fr.noobeclair.hashcode.annotation.ConfStrategy;
import fr.noobeclair.hashcode.bean.config.param.BigDecConfigParam;
import fr.noobeclair.hashcode.bean.config.param.BooleanConfigParam;
import fr.noobeclair.hashcode.bean.config.param.ConfigParam;
import fr.noobeclair.hashcode.bean.config.param.DoubleConfigParam;
import fr.noobeclair.hashcode.bean.config.param.EnumConfigParam;
import fr.noobeclair.hashcode.bean.config.param.IntegerConfigParam;
import fr.noobeclair.hashcode.bean.config.param.LongConfigParam;

public class ConfigStratFactory<C extends Config> extends AbstractFactory<C> {
	
	List<ConfigParam<C>> cfgparams = new ArrayList<>();
	ConfStrategies<C, ConfigParam<C>> strategies = new ConfStrategies<>();
	
	public ConfigStratFactory(Class<C> c) {
		super(c);
	}
	
	@Override
	public List<C> generate(C config) throws InstantiationException, IllegalAccessException {
		result = new ArrayList<>();
		
		C res = c.newInstance();
		if (c.isAnnotationPresent(ConfStratEnabled.class)) {
			ConfStratEnabled allowed = c.getAnnotation(ConfStratEnabled.class);
			this.strategies = new ConfStrategies<C, ConfigParam<C>>(Arrays.asList(allowed.enabled()));
		}
		for (Field f : c.getDeclaredFields()) {
			if (f.isAnnotationPresent(ConfGenerable.class)) {
				ConfGenerable cf = f.getAnnotation(ConfGenerable.class);
				ConfStrategy[] strats = f.getAnnotationsByType(ConfStrategy.class);
				if (ArrayUtils.isNotEmpty(strats)) {
					for (ConfStrategy cs : strats) {
						validateAddConfStratParam(f, cf, cs);
					}
					//FIXME il faut peut etre quand mm faire une conf all avec tout ? mais désactivable ?
				} else {
					validateAddConfParam(f, cf);
				}
			}
		}
		System.out.println("Nb conf " + calcAllTuples());
		processStrategies();
		System.out.println("Result " + result);
		return result;
	}
	
	private void processStrategies() throws InstantiationException, IllegalAccessException {
		Iterator<Entry<String, List<ConfigParam<C>>>> it = strategies.getStrategies().entrySet().iterator();
		while (it.hasNext()) {
			List<ConfigParam<C>> params = it.next().getValue();
			sortConfigParams(params);
			List<C> tmp = processConfigParam(params);
			result.addAll(tmp);
		}
	}
	
	private List<C> processConfigParam(List<ConfigParam<C>> params) throws InstantiationException, IllegalAccessException {
		int resSize = calcTuples(params);
		List<C> result = initEmptyResultList(params, resSize);
		int size = params.size();
		//Need to start to an index and then return one
		for (int i = 0; i < size; i++) {
			
			int nbConf = params.get(i).getNbConf();
			int repeatEach = getPrevCfParamDim(i, params);
			int repeatBloc = result.size() / (repeatEach * nbConf);
			for (int bloc = 0; bloc < repeatBloc; bloc++) {
				for (int nb = 0; nb < nbConf; nb++) {
					params.get(i).reset("" + nb);
					for (int repeat = 0; repeat < repeatEach; repeat++) {
						int n = (nb == 0 ? 0 : nb * repeatEach);
						int b = (bloc == 0 ? 0 : bloc * nbConf);
						int r = (repeat == 0 ? 0 : repeat);
						int idx = b + n + r;
						if (nb == 0 && bloc == 00 && repeat == 0) {
							idx = 0;
						}
						//System.out.println("i : " + i + ",n : " + n + ", b : " + b + ", r : " + r + ",idx : " + idx + ", NB : " + nbConf + ",REPEAT : " + repeatEach + ",BLOC : " + repeatBloc);
						result.set(idx, params.get(i).go(result.get(idx)));
						params.get(i).reset("" + nb);
					}
				}
			}
		}
		return result;
	}
	
	private List<C> initEmptyResultList(List<ConfigParam<C>> params, int nb) throws InstantiationException, IllegalAccessException {
		List<C> result = new ArrayList<>(nb);
		for (int i = 0; i < nb; i++) {
			result.add(initEmptyConfig(params));
		}
		return result;
	}
	
	private List<C> initEmptyResultList(List<ConfigParam<C>> params) throws InstantiationException, IllegalAccessException {
		return initEmptyResultList(params, calcAllTuples());
	}
	
	private void sortConfigParams(List<ConfigParam<C>> params) {
		Collections.sort(params);
	}
	
	private C initEmptyConfig(List<ConfigParam<C>> params) throws InstantiationException, IllegalAccessException {
		//if (emptyConf == null) {
		C res = c.newInstance();
		for (int i = 0; i < params.size(); i++) {
			res = params.get(i).go(res);
			params.get(i).reset();
		}
		//			emptyConf = res;
		//		} else {
		//			res = new C(emptyConf);
		//		}
		return res;
	}
	
	private Integer calcAllTuples() {
		int res = 0;
		Iterator<Entry<String, List<ConfigParam<C>>>> it = strategies.getStrategies().entrySet().iterator();
		while (it.hasNext()) {
			res = res + calcTuples(it.next().getValue());
		}
		return res;
	}
	
	private Integer calcTuples(List<ConfigParam<C>> params) {
		int res = 1;
		for (int i = 0; i < params.size(); i++) {
			res = res * params.get(i).getNbConf();
		}
		return res;
	}
	
	private Integer getPrevCfParamDim(int i, List<ConfigParam<C>> params) {
		int res = 1;
		if (i > 0) {
			for (int j = i - 1; j >= 0; j--) {
				res = res * params.get(j).getNbConf();
			}
		}
		return res;
	}
	
	private Integer getNextCfParamDim(int i, List<ConfigParam<C>> params) {
		int res = 1;
		for (int j = i + 1; j < params.size(); j++) {
			res = res * params.get(j).getNbConf();
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
	
	private C addConf(int c, C toAdd, List<ConfigParam<C>> params) {
		if (c >= params.size()) {
			System.out.println("End");
			return null;
		}
		for (int i = c; i < params.size(); i++) {
			int next = i + 1;
			C tmp = params.get(i).go(toAdd);
			while (tmp != null) {
				System.out.println("curr : " + c + ", tmp : " + tmp);
				toAdd = tmp;
				tmp = addConf(next, toAdd, params);
				if (tmp != null) {
					toAdd = tmp;
					result.add(toAdd);
					System.out.println("Add : " + toAdd);
				}
				tmp = params.get(i).go(toAdd);
			}
			params.get(i).reset();
			
		}
		return toAdd;
	}
	
	private void validateAddConfParam(Field f, ConfGenerable cf) {
		
		Preconditions.checkNotNull("ConFGenerable.TYPE is required ", cf.type());
		AbstractFactory.TYPE type = cf.type();
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
	
	private void validateAddConfStratParam(Field f, ConfGenerable cf, ConfStrategy cfs) {
		String id = cfs.id();
		Preconditions.checkArgument(StringUtils.isNotBlank(id), "Strategy id is required");
		Preconditions.checkNotNull("ConFGenerable.TYPE is required ", cf.type());
		AbstractFactory.TYPE type = cf.type();
		if (AbstractFactory.TYPE.ENUM == type || AbstractFactory.TYPE.BOOLEAN == type) {
			if (AbstractFactory.TYPE.ENUM == type) {
				Preconditions.checkNotNull("ConFGenerable.eClass is required ", cf.eClass());
				String[] excl = (ArrayUtils.isNotEmpty(cfs.excludes()) ? cfs.excludes() : cf.excludes());
				strategies.addOne(id, new EnumConfigParam<C>(f.getName(), type, cf, cf.eClass(), excl, cfs.includes()));
			} else {
				strategies.addOne(id, new BooleanConfigParam<C>(f.getName(), type, cf));
			}
		} else {
			//			String min = cf.min();
			//			String max = cf.max();
			//			String step = cf.step();
			
			String min = (StringUtils.isNotBlank(cfs.min()) ? cfs.min() : cf.min());
			String max = (StringUtils.isNotBlank(cfs.max()) ? cfs.max() : cf.max());
			String step = (StringUtils.isNotBlank(cfs.step()) ? cfs.step() : cf.step());
			
			Preconditions.checkNotNull("ConFGenerable.min is required ", min);
			Preconditions.checkNotNull("ConFGenerable.max is required ", max);
			Preconditions.checkNotNull("ConFGenerable.step is required ", step);
			switch (type) {
			case INT:
				strategies.addOne(id, new IntegerConfigParam<C>(f.getName(), type, min, max, step, cf));
				break;
			case BIGDEC:
				strategies.addOne(id, new BigDecConfigParam<C>(f.getName(), type, min, max, step, cf));
				break;
			case DOUBLE:
				strategies.addOne(id, new DoubleConfigParam<C>(f.getName(), type, min, max, step, cf));
				break;
			case LONG:
				strategies.addOne(id, new LongConfigParam<C>(f.getName(), type, min, max, step, cf));
				break;
			default:
				break;
			}
		}
	}
	
	//	public void addStrat(String stratId, ConfigParam<C> strat) {
	//		List<ConfigParam<C>> strats = Arrays.asList(strat);
	//		if (strategies.containsKey(stratId)) {
	//			strats = strategies.get(stratId);
	//			strats.add(strat);
	//		}
	//		strategies.put(stratId, strats);
	//	}
}
