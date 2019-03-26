package fr.noobeclair.hashcode.utils.dto;

import java.math.BigDecimal;
import java.util.Map;

public class SolverResultDto {
	
	private BigDecimal score;
	private String solverName;
	private String inResource;
	private Map<String, String> stats;
	private Long duration;
	private Long nbItemProcessed;
	
	public SolverResultDto(BigDecimal score, String solverName, String inResource, Long duration, Long nbItemProcessed, Map<String, String> stats) {
		super();
		this.score = score;
		this.solverName = solverName;
		this.inResource = inResource;
		this.stats = stats;
		this.duration = duration;
		this.nbItemProcessed = nbItemProcessed;
	}
	
	public SolverResultDto(BigDecimal score, String solverName, String inResource, Long duration, Long nbItemProcessed) {
		super();
		this.score = score;
		this.solverName = solverName;
		this.inResource = inResource;
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
	
}
