package fr.noobeclair.hashcode.utils.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * List of SolverResultDto with display functions
 * 
 * @author arnaud
 *
 */
public class WorkerResultDto {

	private List<SolverResultDto> solverResults;

	public WorkerResultDto() {
		this.solverResults = new ArrayList<>();
	}

	public void addResult(SolverResultDto res) {
		this.solverResults.add(res);
	}

	public List<SolverResultDto> getSolverResults() {
		return solverResults;
	}

	public void setSolverResults(List<SolverResultDto> solverResults) {
		this.solverResults = solverResults;
	}

	public void displayAll() {
		System.out.println(" ALL RESULTS : ");
		for (SolverResultDto s : solverResults) {
			System.out.println(s);
		}
		System.out.println(" -------------------------------------- ");
	}

	private SolverResultDto findInResult(String inName, BigDecimal score) {
		for (SolverResultDto s : solverResults) {
			if (s.getInResource().equals(inName) && s.getScore().compareTo(score) == 0) {
				return s;
			}
		}
		return null;
	}

	public void displayBest() {
		System.out.println(" BEST RESULTS : ");
		Map<String, BigDecimal> cache = new TreeMap<>();
		for (SolverResultDto s : solverResults) {
			if (!cache.containsKey(s.getInResource())) {
				cache.put(s.getInResource(), s.getScore());
			} else {
				BigDecimal max = cache.get(s.getInResource());
				if (max.compareTo(s.getScore()) < 0) {
					cache.put(s.getInResource(), s.getScore());
				}
			}
		}

		for (String s : cache.keySet()) {
			BigDecimal sc = cache.get(s);
			System.out.println(findInResult(s, sc).toDetailled());
		}
		System.out.println(" -------------------------------------- ");
	}

}
