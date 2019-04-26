package fr.noobeclair.hashcode.solve;

import java.util.TreeMap;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.bean.config.Config;

/**
 * Abstract Configurable Solver
 * 
 * Compute a solution of a in datasets (encapsulated in a BeanContainer)
 * 
 * This class provides a configurable solving. Which means that solver can use
 * constants
 * defined in Config to adapt solver algorithm
 * 
 * a timeout handler is provided. Default is 1 hour
 * 
 * @author arnaud
 *
 * @param <T>
 *        BeanContainer
 * @param <V>
 *        Config
 */
public abstract class ConfigSolver<T extends BeanContainer, V extends Config> extends Solver<T> {

	/** Config **/
	protected V config;

	/**
	 * Run with a stat handler
	 * 
	 * @param data
	 * @param bar
	 *             ProgressBar
	 * @return T extends BeanContainer
	 */
	protected abstract T runWithStat(T data);

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected T run(T data) {
		data = runWithStat(data);
		addConfigStats();
		return data;
	}

	/**
	 * Build a {@link ConfigSolver}
	 * 
	 * @param name
	 * @param config
	 * @param timeout
	 */
	public void build(String name, V config, Long timeout) {
		this.timeout = timeout;
		this.config = config;
		this.stats = new TreeMap<>();
		this.name = name;
	}

	/**
	 * Build a {@link ConfigSolver}
	 * 
	 * @param config
	 * @param timeout
	 */
	public void build(V config, Long timeout) {
		this.timeout = timeout;
		this.config = config;
		this.stats = new TreeMap<>();

	}

	/**
	 * New ConfigSolver which expires after DEFAULT_TIMEOUT seconds.
	 * name is randomly defined
	 * 
	 * @see #fr.noobeclair.hashcode.solve.ConfigSolver.Solver(Long) for tiemout
	 */
	public ConfigSolver() {
		this.timeout = DEFAULT_TIMEOUT;
		this.stats = new TreeMap<>();
		this.name = "ConfigSolver" + new Double(Math.random() * 100).intValue();
	}

	/**
	 * New ConfigSolver which expires after DEFAULT_TIMEOUT seconds.
	 * 
	 * @param name
	 *             String solver unique name
	 * @see #fr.noobeclair.hashcode.solve.ConfigSolver.Solver(Long) for tiemout
	 */
	public ConfigSolver(String name) {
		this.timeout = DEFAULT_TIMEOUT;
		this.stats = new TreeMap<>();
		this.name = name;
	}

	/**
	 * New solver wich will expire after a timeout in SECONDS
	 * 
	 * @param name
	 *                String solver unique name
	 * @param timeout
	 *                in seconds.
	 */
	public ConfigSolver(String name, final Long timeout) {
		this.timeout = timeout;
		this.stats = new TreeMap<>();
		this.name = name;
	}

	/**
	 * New solver wich will expire after a timeout in SECONDS
	 * 
	 * @param name
	 *                String solver unique name
	 * @param timeout
	 *                in seconds.
	 * @param V
	 *                conf config
	 */
	public ConfigSolver(String name, final V conf, final Long timeout) {
		this.timeout = timeout;
		this.config = conf;
		this.stats = new TreeMap<>();
		this.name = name;
	}

	@Override
	protected void close(Long total) {
		super.close(total);
		this.resultInfo.setConfig(this.config);
	}

	public V getConfig() {
		return config;
	}

	/**
	 * Add statistics in stats map.
	 */
	protected abstract void addConfigStats();

}
