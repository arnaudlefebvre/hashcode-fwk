package fr.noobeclair.hashcode.utils.dto;

import java.math.BigDecimal;
import java.util.Map;

import fr.noobeclair.hashcode.bean.config.Config;

/**
 * Solver result dto that encapsulate a solver results and stats :
 * 
 * @author arnaud
 *
 */
public class SolverResultDto {

	private BigDecimal score;
	private String solverName;
	private String inResource;
	private Map<String, String> stats;
	private Config config;
	private Long duration;
	private Long nbItemProcessed;
	private Long nbInputItem;

	public SolverResultDto() {
		super();
	}

	public SolverResultDto(BigDecimal score, String solverName, String inResource, Long duration, Long nbItemProcessed,
			Map<String, String> stats) {
		super();
		this.score = score;
		this.solverName = solverName;
		this.inResource = inResource;
		this.stats = stats;
		this.duration = duration;
		this.nbItemProcessed = nbItemProcessed;
	}

	public SolverResultDto(BigDecimal score, String solverName, String inResource, Long duration, Long nbItemProcessed,
			Long totalItem) {
		super();
		this.score = score;
		this.solverName = solverName;
		this.inResource = inResource;
		this.duration = duration;
		this.nbItemProcessed = nbItemProcessed;
		this.nbInputItem = totalItem;
	}

	public SolverResultDto(BigDecimal score, String solverName, String inResource, Map<String, String> stats,
			Config config, Long duration, Long nbItemProcessed) {
		super();
		this.score = score;
		this.solverName = solverName;
		this.inResource = inResource;
		this.stats = stats;
		this.config = config;
		this.duration = duration;
		this.nbItemProcessed = nbItemProcessed;
	}

	public BigDecimal getScore() {
		return score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}

	public String getSolverName() {
		return solverName;
	}

	public void setSolverName(String solverName) {
		this.solverName = solverName;
	}

	public String getInResource() {
		return inResource;
	}

	public void setInResource(String inResource) {
		this.inResource = inResource;
	}

	public Map<String, String> getStats() {
		return stats;
	}

	public void setStats(Map<String, String> stats) {
		this.stats = stats;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public Long getNbItemProcessed() {
		return nbItemProcessed;
	}

	public void setNbItemProcessed(Long nbItemProcessed) {
		this.nbItemProcessed = nbItemProcessed;
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public String toDetailled() {
		StringBuilder sb = toStringSb();
		if (this.config != null) {
			sb.append("\r\n").append("Conf [").append(this.config.show(Config.SHOW_OPT.ALL)).append("]");
		}
		return sb.toString();
	}

	private StringBuilder toStringSb() {
		StringBuilder builder = new StringBuilder();
		builder.append("SolverResultDto [");
		if (inResource != null) {
			builder.append("inResource=");
			builder.append(inResource);
			builder.append(", ");
		}
		if (solverName != null) {
			builder.append("solverName=");
			builder.append(solverName);
			builder.append(", ");
		}
		if (score != null) {
			builder.append("score=");
			builder.append(score);
			builder.append(", ");
		}
		if (nbItemProcessed != null) {
			builder.append("nbItemProcessed=");
			builder.append(nbItemProcessed);
			builder.append(", ");
		}
		if (duration != null) {
			builder.append("duration=");
			builder.append(duration);
			builder.append(", ");
		}
		if (stats != null) {
			builder.append("stats=");
			builder.append(stats);
		}
		builder.append("]");
		return builder;
	}

	@Override
	public String toString() {
		return toStringSb().toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((duration == null) ? 0 : duration.hashCode());
		result = prime * result + ((inResource == null) ? 0 : inResource.hashCode());
		result = prime * result + ((nbItemProcessed == null) ? 0 : nbItemProcessed.hashCode());
		result = prime * result + ((score == null) ? 0 : score.hashCode());
		result = prime * result + ((solverName == null) ? 0 : solverName.hashCode());
		result = prime * result + ((stats == null) ? 0 : stats.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SolverResultDto other = (SolverResultDto) obj;
		if (duration == null) {
			if (other.duration != null)
				return false;
		} else if (!duration.equals(other.duration))
			return false;
		if (inResource == null) {
			if (other.inResource != null)
				return false;
		} else if (!inResource.equals(other.inResource))
			return false;
		if (nbItemProcessed == null) {
			if (other.nbItemProcessed != null)
				return false;
		} else if (!nbItemProcessed.equals(other.nbItemProcessed))
			return false;
		if (score == null) {
			if (other.score != null)
				return false;
		} else if (!score.equals(other.score))
			return false;
		if (solverName == null) {
			if (other.solverName != null)
				return false;
		} else if (!solverName.equals(other.solverName))
			return false;
		if (stats == null) {
			if (other.stats != null)
				return false;
		} else if (!stats.equals(other.stats))
			return false;
		return true;
	}

	public Long getNbInputItem() {
		return nbInputItem;
	}

	public void setNbInputItem(Long nbInputItem) {
		this.nbInputItem = nbInputItem;
	}

}
