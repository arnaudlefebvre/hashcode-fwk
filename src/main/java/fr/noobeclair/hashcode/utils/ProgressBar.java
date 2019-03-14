package fr.noobeclair.hashcode.utils;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.ButtonGroup;

public class ProgressBar {
	
	private Integer end;
	private Integer maxwidth;
	private String progressStart;
	private String progressEnd;
	private String progressFill;
	private String progressHead;
	private String progressDone;
	private BigDecimal progressMeanTime;
	private Long currentStepTime;
	private Integer step;
	private Integer stepsize;
	private Integer lpad;
	private Integer rpad;
	private Integer progressWidth;
	private Integer progressOption;
	private Long startTime;
	private Long lastRefresh;
	
	public static final Integer ONLY_BAR = 0;
	public static final Integer BAR_PERCENT = 10;
	public static final Integer BAR_COUNT = 20;
	public static final Integer BAR_ALL = 30;
	public static final Integer BAR_ETA = 20;
	
	public Long REFRESH_MS = 10L;
	public static final BigDecimal MIN  = new BigDecimal("5");
	public static final BigDecimal MAX  = new BigDecimal("5000");
	public static final BigDecimal MIN_DISPLAY = new BigDecimal("10");
	
	public static final String PAD_STR = " ";
	public static final String REMAIN_FILL_STR = " ";
	public static final String PAD_NB_STR = " ";
	
	public ProgressBar(Integer count, Integer maxwidth,
			String progressStart, String progressEnd, String progressFill,
			String progressHead, String progressDone) {
		super();
		this.startTime = 0L;
		this.lastRefresh = 0L;
		this.end = count;
		this.maxwidth = maxwidth;
		this.progressStart = progressStart;
		this.progressEnd = progressEnd;
		this.progressFill = progressFill;
		this.progressHead = progressHead;
		this.progressDone = progressDone;
		this.progressMeanTime = new BigDecimal("0");
		this.step = 0;
		this.stepsize = 0;
		this.lpad = 0;
		this.rpad = 0;
		this.progressOption = BAR_ALL;
		this.currentStepTime = System.currentTimeMillis();
		prepare();		
	}
	
	public ProgressBar(Integer count, Integer maxwidth,
			String progressStart, String progressEnd, String progressFill,
			String progressHead, String progressDone, Integer progressOption) {
		super();
		this.startTime = 0L;
		this.lastRefresh = 0L;
		this.end = count;
		this.maxwidth = maxwidth;
		this.progressStart = progressStart;
		this.progressEnd = progressEnd;
		this.progressFill = progressFill;
		this.progressHead = progressHead;
		this.progressDone = progressDone;
		this.progressMeanTime = new BigDecimal("0");
		this.step = 0;
		this.stepsize = 0;
		this.lpad = 0;
		this.rpad = 0;
		this.progressOption = progressOption;
		this.currentStepTime = System.currentTimeMillis();
		prepare();		
	}
	
	public ProgressBar(Integer count, Integer maxwidth,
			String progressStart, String progressEnd, String progressFill,
			String progressHead, String progressDone, Integer lpad, Integer rpad) {
		super();
		this.startTime = 0L;
		this.lastRefresh = 0L;
		this.end = count;
		this.maxwidth = maxwidth;
		this.progressStart = progressStart;
		this.progressEnd = progressEnd;
		this.progressFill = progressFill;
		this.progressHead = progressHead;
		this.progressDone = progressDone;
		this.progressMeanTime = new BigDecimal("0");
		this.step = 0;
		this.stepsize = 0;
		this.lpad = lpad;
		this.rpad = rpad;
		this.progressOption = BAR_ALL;
		this.currentStepTime = System.currentTimeMillis();
		prepare();		
	}
	
	public ProgressBar(Integer count, Integer maxwidth,
			String progressStart, String progressEnd, String progressFill,
			String progressHead, String progressDone, Integer lpad, Integer rpad, Integer progressOption) {
		super();
		this.startTime = 0L;
		this.lastRefresh = 0L;
		this.end = count;
		this.maxwidth = maxwidth;
		this.progressStart = progressStart;
		this.progressEnd = progressEnd;
		this.progressFill = progressFill;
		this.progressHead = progressHead;
		this.progressDone = progressDone;
		this.progressMeanTime = new BigDecimal("0");
		this.step = 0;
		this.stepsize = 0;
		this.lpad = lpad;
		this.rpad = rpad;
		this.progressOption = progressOption;
		this.currentStepTime = System.currentTimeMillis();
		prepare();		
	}
	
	private void reset() {
		this.startTime = 0L;
		this.lastRefresh = 0L;
		this.progressMeanTime = new BigDecimal("0");
		this.step = 0;
		this.currentStepTime = System.currentTimeMillis();
	}
	
	public void reset(Integer count) {
		this.startTime = 0L;
		this.lastRefresh = 0L;
		this.step = 0;
		this.progressMeanTime = new BigDecimal("0");
		this.end = count;
		prepare();
		this.currentStepTime = System.currentTimeMillis();
	}
	
	private void prepare() {
		int percentsize = 0;
		int countsize = 0;
		int etasize = 0;
		int queuesize = 0;		
		if (BAR_PERCENT.equals(this.progressOption) || BAR_ALL.equals(this.progressOption)) {
			//- xxx% 
			percentsize = "xxx%".length();
		}
		if (BAR_COUNT.equals(this.progressOption) || BAR_ALL.equals(this.progressOption)) {
			//- end/end 
			countsize = "/".length()+(end.toString().length()*2);
		}
		if (BAR_ETA.equals(this.progressOption) || BAR_ALL.equals(this.progressOption)) {
			//- 00h00m00s
			etasize = "00h00m00s".length();
		}
		if (BAR_COUNT.equals(this.progressOption) || BAR_ALL.equals(this.progressOption) || BAR_PERCENT.equals(this.progressOption) || BAR_ETA.equals(this.progressOption)) {
			queuesize = " [  ] ".length();
			if (countsize > 0 && percentsize > 0 && etasize > 0) {
				queuesize = queuesize + (" - ".length()*2);
			} else if ( (countsize > 0 && percentsize > 0) || (countsize > 0 && etasize > 0) || (etasize > 0 && percentsize > 0)) {
				queuesize = queuesize + " - ".length();
			}
			queuesize = queuesize + countsize + percentsize + etasize;
		}		
		//Taille de la zone de chargement, si maxwidth = 100, 96
		this.progressWidth = this.maxwidth - (this.lpad+this.rpad+this.progressStart.length()+this.progressEnd.length()+queuesize);
		//pas de chargement = nombre d'item par unité de la zone de chargement. si end : 480 = 5
		this.stepsize = ((Long)Math.round((Math.ceil((double)(end)/ progressWidth )))).intValue() * this.progressFill.length();
		//lissage de la taille de la zone de chargement
		this.progressWidth = ((Long)Math.round(Math.ceil((double)this.end/stepsize))).intValue() + this.progressHead.length();
	}
	
	private void setStart() {
		if (this.startTime.equals(0L)) {
			this.startTime = System.currentTimeMillis();
		}		
	}
	
	private boolean isRefresh() {
		boolean res = false;
		if (this.lastRefresh.equals(0L)) {
			res = true;
			this.lastRefresh = startTime;
		} else if (System.currentTimeMillis() - this.lastRefresh >= REFRESH_MS) {
			res = true;
			this.lastRefresh = System.currentTimeMillis();
		}
		return res;
		
	}
	
	private String getQueue(int step) {
		StringBuffer buf = new StringBuffer();
		if (BAR_COUNT.equals(this.progressOption) || BAR_ALL.equals(this.progressOption) || BAR_PERCENT.equals(this.progressOption)|| BAR_ETA.equals(this.progressOption)) {
			buf.append(" [ ");
			if (BAR_COUNT.equals(this.progressOption) || BAR_ALL.equals(this.progressOption)) {
				buf.append(""+pad(""+step,""+end)+"/"+end);
			}
			if ((BAR_COUNT.equals(this.progressOption) && BAR_PERCENT.equals(this.progressOption))  || (BAR_COUNT.equals(this.progressOption) && BAR_ETA.equals(this.progressOption)) || BAR_ALL.equals(this.progressOption)) {
				buf.append(" - ");
			}
			if (BAR_PERCENT.equals(this.progressOption) || BAR_ALL.equals(this.progressOption)) {
				buf.append(""+(pad(""+step*100/this.end,"100"))+"%");
			}
			//|| (BAR_PERCENT.equals(this.progressOption) && BAR_ETA.equals(this.progressOption))
			if ((BAR_PERCENT.equals(this.progressOption) && BAR_ETA.equals(this.progressOption))  || BAR_ALL.equals(this.progressOption)) {
				buf.append(" - ");
			}
			if (BAR_ETA.equals(this.progressOption) || BAR_ALL.equals(this.progressOption)) {							
				buf.append("ETA "+Utils.formatToHHMMSS(this.progressMeanTime.multiply(new BigDecimal(end - step)).longValue()));
			}
			buf.append(" ] ");
			
		}
		return buf.toString();
	}
	
	private String getRPadS() {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < this.rpad; i ++ ) {
			buf.append(PAD_STR);
		}
		return buf.toString();
	}
	
	private String getLPadS() {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < this.lpad; i ++ ) {
			buf.append(PAD_STR);
		}
		return buf.toString();
	}

	public String getBar(int step) {
		StringBuffer buf = new StringBuffer();
		Integer endbar = ((Long)Math.round(Math.ceil(((double)step/this.stepsize)))).intValue() - (this.progressHead.length()/this.progressHead.length());
		for (int i = 0; i < endbar; i ++ ) {
			buf.append(this.progressFill);
		}
		buf.append(this.progressFill+this.progressHead);
		return buf.toString();
	}
	
	public String getRest(Integer step) {
		StringBuffer buf = new StringBuffer();
		Integer stepbar = ((Long)Math.round(Math.ceil(((double)step/this.stepsize)))).intValue();
		Integer endbar =  0;
		endbar = stepbar + this.progressHead.length();
		for (int i = 0; i < this.progressWidth - endbar ; i ++ ) {
			for (int j = 0 ; j < this.progressFill.length(); j ++) {
				buf.append(REMAIN_FILL_STR);
			}
		}
		buf.append(this.progressEnd);
		return buf.toString();
	}
	
	public String getProgress(Integer step) {
		StringBuffer res = new StringBuffer();
		res.append(getLPadS());
		res.append(this.progressStart);
		res.append(getBar(step));
		res.append(getRest(step));
		res.append(getQueue(step));
		res.append("\r");
		return res.toString();
	}
	
	public void show(PrintStream out,Integer step) {
		this.step = step;		
		setStart();
		if (step.equals(end)) {
			out.println(getProgress(step));
			out.println(this.progressDone);
			reset();
		} else //if (step % stepsize == 0) { 
		{
			calcMeanTime(step);
			if (isRefresh()) {
				out.print(getProgress(step));
			}
			updateRefreshTime(step);
		}
	}
	
	public String pad(String pad, String max) {
		String res = pad;
		while (res.length() < max.length()) {
		 res = PAD_NB_STR + res;
		}
		return res;
	}
	
	private void calcMeanTime(int step) {
		if (BAR_ETA.equals(this.progressOption) || BAR_ALL.equals(this.progressOption)) {
			BigDecimal currSec = new BigDecimal(""+(System.currentTimeMillis() - this.currentStepTime )).divide(new BigDecimal("1000"));
			if (step != 0 ) {
				// (moy * stepdone + currtime) / stepdone + 1
				//this.progressMeanTime = ((this.progressMeanTime * step +currSec) / (step+1));	
				this.progressMeanTime = this.progressMeanTime.multiply(new BigDecimal(step)).add(currSec).divide(new BigDecimal(step+1),3, RoundingMode.HALF_UP);
			} else {
				this.progressMeanTime =  currSec;
			}
			this.currentStepTime = System.currentTimeMillis();
		}
	}
	
	private void updateRefreshTime(int step) {
		long per = step*100/this.end;
		if (per % 3 == 0) {
			BigDecimal nbRefresh = new BigDecimal(this.REFRESH_MS);
			BigDecimal meanMs = this.progressMeanTime.multiply(new BigDecimal("1000"));
			BigDecimal totalEstimatedTime  = meanMs.multiply(new BigDecimal(end - step)).add(new BigDecimal(System.currentTimeMillis() - this.startTime));
			nbRefresh = totalEstimatedTime.divide(totalEstimatedTime, 2, RoundingMode.HALF_UP);
			if (nbRefresh.compareTo(MIN) > 0) {
				this.REFRESH_MS = totalEstimatedTime.divide(MIN_DISPLAY).longValue();
			} else if (meanMs.compareTo(new BigDecimal(REFRESH_MS))> 0) {
				this.REFRESH_MS = meanMs.max(MAX).longValue();
			}
		}
	}
	
	public Long getStartTime() {
		return startTime;
	}
	public Integer getEnd() {
		return end;
	}
	public Integer getMaxwidth() {
		return maxwidth;
	}
	public String getProgressStart() {
		return progressStart;
	}
	public String getProgressEnd() {
		return progressEnd;
	}
	public String getProgressFill() {
		return progressFill;
	}
	public String getProgressHead() {
		return progressHead;
	}
	public String getProgressDone() {
		return progressDone;
	}
	
	

}
