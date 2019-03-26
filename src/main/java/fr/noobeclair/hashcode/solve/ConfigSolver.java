package fr.noobeclair.hashcode.solve;

import java.util.TreeMap;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.bean.Config;

public abstract class ConfigSolver<T extends BeanContainer, V extends Config> extends Solver<T> {
	
	protected V config;
	
	public void build(String name, V config, Long timeout) {
		this.timeout = timeout;
		this.config = config;
		this.stats = new TreeMap<>();
		this.name = name;
	}
	
	public void build(V config, Long timeout) {
		this.timeout = timeout;
		this.config = config;
		this.stats = new TreeMap<>();
		
	}
	
	/**
	 * New ConfigSolver which never expires.
	 *
	 * @see #fr.noobeclair.hashcode.solve.ConfigSolver.Solver(Long) for tiemout
	 */
	public ConfigSolver() {
		this.timeout = DEFAULT_TIMEOUT;
		this.stats = new TreeMap<>();
		this.name = "ConfigSolver" + new Double(Math.random() * 100).intValue();
	}
	
	public ConfigSolver(String name) {
		this.timeout = DEFAULT_TIMEOUT;
		this.stats = new TreeMap<>();
		this.name = name;
	}
	
	/**
	 * New solver wich will expire after a timeout in SECONDS
	 *
	 * @param timeout
	 *            in seconds.
	 */
	public ConfigSolver(String name, final Long timeout) {
		this.timeout = timeout;
		this.stats = new TreeMap<>();
		this.name = name;
	}
	
	/**
	 * New solver wich will expire after a timeout in SECONDS
	 *
	 * @param timeout
	 *            in seconds.
	 * @param V
	 *            conf config
	 */
	public ConfigSolver(String name, final V conf, final Long timeout) {
		this.timeout = timeout;
		this.config = conf;
		this.stats = new TreeMap<>();
		this.name = name;
	}
	
	public V getConfig() {
		return config;
	}
	
}
