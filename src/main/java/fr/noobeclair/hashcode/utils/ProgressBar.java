package fr.noobeclair.hashcode.utils;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Progress bar for console app. Used to display progress of a calculation.
 * Minimal input is a number of items that will be processed and method show
 * must be called each time an item was processed.
 * 
 * ProgressBar must be created using its builder. For example : ProgressBar bar
 * = ProgressBar.builder(100) //For 100 items to process. .... //Process an item
 * in your code bar.show(n) // n is the number of items already processed, and
 * show() display the progress bar.
 * 
 * 
 * @see ProgressBar.Builder for availables features.
 * @author arnaud
 *
 */
public class ProgressBar {

	private Long end;
	private final Integer maxwidth;
	private final String progressStart;
	private final String progressEnd;
	private final String progressFill;
	private final String progressHead;
	private final String progressDone;
	private BigDecimal progressMeanTime;
	private BigDecimal progressTotalTime;
	private Long currentStepTime;
	private Long step;
	private Integer stepsize;
	private final Integer lpad;
	private final Integer rpad;
	private Integer progressWidth;
	private final List<ProgressBarOption> progressOption;
	private Long startTime;
	private Long lastRefresh;
	private final Integer msgSize;
	private long refreshMs;
	private boolean autoRefresh;
	private String msg;

	public enum ProgressBarOption {
		BAR, PERCENT, COUNT, ETA, MSG, ALL
	}

	public static Long REFRESH_MS_DEFAULT = 1000L;
	public static final BigDecimal MIN = new BigDecimal("20");
	public static final BigDecimal MAX = new BigDecimal("2000");
	public static final BigDecimal MIN_DISPLAY = new BigDecimal("30");
	public static final int UPDATE_REFRESH_STEP = 1;
	public static final BigDecimal MAX_DISPLAY_SINCE_START = new BigDecimal("2");

	public static final String PAD_STR = " ";
	public static final String REMAIN_FILL_STR = " ";
	public static final String PAD_NB_STR = " ";
	public static final BigDecimal DEFAULT_BAR_MSG_SIZE_PER = new BigDecimal("0.27");

	public ProgressBar(Builder builder) {
		super();
		/** Internal non customizable var **/
		this.startTime = 0L;
		this.lastRefresh = 0L;
		this.progressMeanTime = new BigDecimal("0");
		this.step = 0L;
		this.stepsize = 0;
		this.lpad = 0;
		this.rpad = 0;
		this.currentStepTime = System.currentTimeMillis();
		/** Builder var **/
		this.progressStart = builder.barStartChar;
		this.progressEnd = builder.barStartEnd;
		this.progressFill = builder.barFill;
		this.progressHead = builder.barHead;
		this.maxwidth = builder.maxwidth;
		this.end = builder.end;
		this.msgSize = builder.barMsgSize;
		this.progressDone = builder.barDone;
		this.autoRefresh = builder.autoRefreshTimeCalculation;
		this.refreshMs = builder.refreshTime;
		this.progressOption = builder.progressOption;
		prepare();
	}

	private void reset() {
		this.startTime = 0L;
		this.lastRefresh = 0L;
		this.progressMeanTime = new BigDecimal("0");
		this.currentStepTime = System.currentTimeMillis();
	}

	public void reset(final Long count) {
		this.startTime = 0L;
		this.lastRefresh = 0L;
		this.step = 0L;
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
		int msgSize = 0;

		if (CollectionUtils.containsAny(this.progressOption,
				Arrays.asList(ProgressBarOption.ALL, ProgressBarOption.MSG))) {
			msgSize = this.msgSize;
		}
		if (CollectionUtils.containsAny(this.progressOption,
				Arrays.asList(ProgressBarOption.ALL, ProgressBarOption.PERCENT))) {
			// - xxx%
			percentsize = "xxx%".length();
		}
		if (CollectionUtils.containsAny(this.progressOption,
				Arrays.asList(ProgressBarOption.ALL, ProgressBarOption.COUNT))) {
			// - end/end
			countsize = "/".length() + (end.toString().length() * 2);
		}
		if (CollectionUtils.containsAny(this.progressOption,
				Arrays.asList(ProgressBarOption.ALL, ProgressBarOption.ETA))) {
			// - 00h00m00s
			etasize = "00h00m00s".length();
		}
		if (CollectionUtils.containsAny(this.progressOption, Arrays.asList(ProgressBarOption.ALL, ProgressBarOption.MSG,
				ProgressBarOption.ETA, ProgressBarOption.COUNT, ProgressBarOption.PERCENT))) {
			queuesize = " [  ] ".length();
			if (countsize > 0 && percentsize > 0 && etasize > 0) {
				queuesize = queuesize + (" - ".length() * 2);
			} else if ((countsize > 0 && percentsize > 0) || (countsize > 0 && etasize > 0)
					|| (etasize > 0 && percentsize > 0)) {
				queuesize = queuesize + " - ".length();
			}
			queuesize = queuesize + countsize + percentsize + etasize;
		}
		// Taille de la zone de chargement, si maxwidth = 100, 96
		this.progressWidth = this.maxwidth - (msgSize + this.lpad + this.rpad + this.progressStart.length()
				+ this.progressEnd.length() + queuesize);
		// pas de chargement = nombre d'item par unité de la zone de chargement. si end
		// : 480 = 5
		this.stepsize = ((Long) Math.round((Math.ceil((double) (end) / progressWidth)))).intValue()
				* this.progressFill.length();
		// lissage de la taille de la zone de chargement
		this.progressWidth = ((Long) Math.round(Math.ceil((double) this.end / stepsize))).intValue()
				+ this.progressHead.length();
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
		} else if (System.currentTimeMillis() - this.lastRefresh >= refreshMs) {
			res = true;
			this.lastRefresh = System.currentTimeMillis();
		}
		return res;

	}

	private String getQueue(final Long step) {
		final StringBuffer buf = new StringBuffer();
		if (CollectionUtils.containsAny(this.progressOption, Arrays.asList(ProgressBarOption.ALL, ProgressBarOption.MSG,
				ProgressBarOption.ETA, ProgressBarOption.COUNT, ProgressBarOption.PERCENT))) {
			buf.append(" [ ");
			if (CollectionUtils.containsAny(this.progressOption,
					Arrays.asList(ProgressBarOption.ALL, ProgressBarOption.COUNT))) {
				buf.append("" + pad("" + step, "" + end) + "/" + end);
			}
			if (CollectionUtils.containsAll(this.progressOption,
					Arrays.asList(ProgressBarOption.COUNT, ProgressBarOption.PERCENT))
					|| CollectionUtils.containsAll(this.progressOption,
							Arrays.asList(ProgressBarOption.COUNT, ProgressBarOption.ETA))
					|| this.progressOption.contains(ProgressBarOption.ALL)) {
				buf.append(" - ");
			}
			if (CollectionUtils.containsAny(this.progressOption,
					Arrays.asList(ProgressBarOption.ALL, ProgressBarOption.PERCENT))) {
				buf.append("" + (pad("" + step * 100 / this.end, "100")) + "%");
			}
			if (CollectionUtils.containsAll(this.progressOption,
					Arrays.asList(ProgressBarOption.PERCENT, ProgressBarOption.ETA))
					|| this.progressOption.contains(ProgressBarOption.ALL)) {
				buf.append(" - ");
			}
			if (CollectionUtils.containsAny(this.progressOption,
					Arrays.asList(ProgressBarOption.ALL, ProgressBarOption.ETA))) {
				buf.append("ETA "
						+ Utils.formatToHHMMSS(this.progressMeanTime.multiply(new BigDecimal(end - step)).longValue()));
			}
			buf.append(" ] ");

		}
		return buf.toString();
	}

	private String getRPadS() {
		final StringBuffer buf = new StringBuffer();
		for (int i = 0; i < this.rpad; i++) {
			buf.append(PAD_STR);
		}
		return buf.toString();
	}

	private String getLPadS() {
		final StringBuffer buf = new StringBuffer();
		for (int i = 0; i < this.lpad; i++) {
			buf.append(PAD_STR);
		}
		return buf.toString();
	}

	public String getBar(final Long step) {
		final StringBuffer buf = new StringBuffer();
		final Integer endbar = ((Long) Math.round(Math.ceil(((double) step / this.stepsize)))).intValue()
				- (this.progressHead.length() / this.progressHead.length());
		for (int i = 0; i < endbar; i++) {
			buf.append(this.progressFill);
		}
		buf.append(this.progressFill + this.progressHead);
		return buf.toString();
	}

	public String getRest(final Long step) {
		final StringBuffer buf = new StringBuffer();
		final Integer stepbar = ((Long) Math.round(Math.ceil(((double) step / this.stepsize)))).intValue();
		Integer endbar = 0;
		endbar = stepbar + this.progressHead.length();
		for (int i = 0; i < this.progressWidth - endbar; i++) {
			for (int j = 0; j < this.progressFill.length(); j++) {
				buf.append(REMAIN_FILL_STR);
			}
		}
		buf.append(this.progressEnd);
		return buf.toString();
	}

	private String getMsg(final String s) {
		if (CollectionUtils.containsAny(this.progressOption,
				Arrays.asList(ProgressBarOption.ALL, ProgressBarOption.MSG))) {
			String result = null;
			final String DOT = "...";
			final int max = this.msgSize - (DOT.length() + 1);
			if (StringUtils.isNotEmpty(s)) {
				if (s.length() > max) {
					result = s.substring(0, this.msgSize - (DOT.length() + 1)) + DOT;
				} else if (s.length() == max) {
					result = s + DOT;
				} else {
					result = String.format("%1$-" + (max + DOT.length()) + "s", s);
				}
			} else {
				result = String.format("%1$-" + (max + DOT.length()) + "s", " ");
			}
			this.msg = result;
			return result;

		}
		this.msg = StringUtils.EMPTY;
		return StringUtils.EMPTY;
	}

	public String getProgress(final Long step, final String s) {
		final StringBuffer res = new StringBuffer();
		res.append(getMsg(s));
		res.append(getLPadS());
		res.append(this.progressStart);
		res.append(getBar(step));
		res.append(getRest(step));
		res.append(getQueue(step));
		res.append("\r");
		return res.toString();
	}

	public String getProgress(final Long step) {
		final StringBuffer res = new StringBuffer();
		res.append(getMsg(null));
		res.append(getLPadS());
		res.append(this.progressStart);
		res.append(getBar(step));
		res.append(getRest(step));
		res.append(getQueue(step));
		res.append("\r");
		return res.toString();
	}

	public void show(final PrintStream out, final Integer step) {
		show(out, step.longValue());
	}

	public void show(final PrintStream out, final Integer step, Boolean force) {
		show(out, step.longValue(), StringUtils.EMPTY, force);
	}

	public void show(final PrintStream out, final Integer step, final String msg) {
		show(out, step.longValue(), msg, false);
	}

	public void show(final PrintStream out, final Long step) {
		show(out, step, StringUtils.EMPTY, false);
	}

	public void show(final PrintStream out, final Long step, Boolean force) {
		show(out, step, StringUtils.EMPTY, force);
	}

	public void show(final PrintStream out, final Long step, final String msg) {
		show(out, step, msg, false);
	}

	public void show(final PrintStream out, final Long step, final String msg, Boolean force) {
		this.step = step;
		setStart();
		if (step >= end) {
			out.println(getProgress(step, msg));
			if (StringUtils.isNotEmpty(this.progressDone)) {
				out.println(this.progressDone);
			}
			reset();
		} else // if (step % stepsize == 0) {
		{
			calcMeanTime(step);
			if (isRefresh() || force) {
				out.print(getProgress(step, msg));
			}
			updateRefreshTime(step);
		}
	}

	public void end(final PrintStream out) {
		if (this.step < end) {
			this.step = end;
			show(out, this.step);
			this.step = 0L;
		}
	}

	public String pad(final String pad, final String max) {
		String res = pad;
		while (res.length() < max.length()) {
			res = PAD_NB_STR + res;
		}
		return res;
	}

	private void calcMeanTime(final Long step) {
		if (CollectionUtils.containsAny(this.progressOption,
				Arrays.asList(ProgressBarOption.ALL, ProgressBarOption.ETA))) {
			this.progressTotalTime = new BigDecimal("" + (System.currentTimeMillis() - this.startTime));
			final BigDecimal currSec = new BigDecimal("" + (System.currentTimeMillis() - this.currentStepTime))
					.divide(new BigDecimal("1000"));
			if (step != 0) {
				// (moy * stepdone + currtime) / stepdone + 1
				// this.progressMeanTime = ((this.progressMeanTime * step +currSec) / (step+1));
				this.progressMeanTime = this.progressTotalTime.divide(new BigDecimal(step + 1), 4, RoundingMode.HALF_UP)
						.divide(new BigDecimal("1000"));
			} else {
				this.progressMeanTime = currSec;
			}
			this.currentStepTime = System.currentTimeMillis();
		}
	}

	private void updateRefreshTime(final Long step) {
		if (this.autoRefresh) {
			final double per = step * 100D / this.end;
			final BigDecimal start = new BigDecimal(System.currentTimeMillis() - this.startTime);
			BigDecimal nbRefresh = this.refreshMs > 0L ? new BigDecimal(this.refreshMs) : BigDecimal.ONE;
			final BigDecimal nbDisplaySinceStart = start.divide(nbRefresh, 4, RoundingMode.HALF_DOWN);
			if ((per > 0 && per % UPDATE_REFRESH_STEP == 0)
					|| nbDisplaySinceStart.compareTo(MAX_DISPLAY_SINCE_START) > 0) {

				final BigDecimal meanMs = this.progressTotalTime.multiply(new BigDecimal(step + 1));
				BigDecimal totalEstimatedTime = BigDecimal.ZERO;

				totalEstimatedTime = meanMs.multiply(new BigDecimal(end - step)).add(start);

				if (totalEstimatedTime.compareTo(BigDecimal.ZERO) > 0) {
					nbRefresh = totalEstimatedTime.divide(nbRefresh, 4, RoundingMode.HALF_DOWN);
				}
				if (nbRefresh.compareTo(MIN) < 0) {
					this.refreshMs = totalEstimatedTime.divide(MIN_DISPLAY, 4, RoundingMode.HALF_DOWN).longValue();
				} else if (meanMs.compareTo(new BigDecimal(refreshMs)) > 0) {
					this.refreshMs = meanMs.min(MAX).longValue();
				} else {
					this.refreshMs = MAX.longValue();
				}
				// System.out.println(totalEstimatedTime+" - "+this.REFRESH_MS+" - "+meanMs+ " -
				// "+nbRefresh+ " - "+totalEstimatedTime.compareTo(BigDecimal.ZERO)+" -
				// "+totalEstimatedTime.divide(MIN_DISPLAY)+ " - "+nbDisplaySinceStart);
			}
		}
	}

	public Long getStartTime() {
		return startTime;
	}

	public Long getEnd() {
		return end;
	}

	public void updateEnd(final PrintStream s, final Long step, final Long end) {
		this.end = end;
		prepare();
		this.show(s, step, "UPDT END");
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

	public static Builder builder(final Long end) {
		return new Builder(end);
	}

	public Long getStep() {
		return step;
	}

	public String getMsg() {
		return msg;
	}

	public static final class Builder {
		private Long end;
		private Integer maxwidth = 100;
		private String barStartChar = "|";
		private String barStartEnd = "|";
		private String barFill = "=";
		private String barHead = "=>";
		private String barDone = StringUtils.EMPTY;
		private List<ProgressBarOption> progressOption = Arrays.asList(ProgressBarOption.BAR, ProgressBarOption.ETA);
		private Integer barMsgSize = null;
		private Long refreshTime = ProgressBar.REFRESH_MS_DEFAULT;
		private boolean autoRefreshTimeCalculation = true;

		/**
		 * Init new Builder with end count of items to process
		 * 
		 * @param end
		 */
		public Builder(Long end) {
			this.end = end;
		}

		/**
		 * Set maxWidth in characters for display. Default 100
		 * 
		 * @param maxWidth
		 * @return Builder
		 */
		public Builder withMaxWidth(Integer maxWidth) {
			this.maxwidth = maxWidth;
			return this;
		}

		/**
		 * Set Bar Characters for display advancement. Default is "=" for already done
		 * filling, and "=>" for head of done filling. the progress bar will be
		 * surrounded by "|" by default. Ex : "|===> |"
		 * 
		 * @param doneFiller
		 *                   character(s) for already done filling
		 * @param doneHead
		 *                   character(s) for head of already done filling
		 * @see fr.noobeclair.hashcode.utils.ProgressBar.Builder#withBarOpt(String,
		 *      String, String, String)
		 * @return Builder
		 */
		public Builder withBarOpt(String doneFiller, String doneHead) {
			this.barFill = doneFiller;
			this.barHead = doneHead;
			return this;
		}

		/**
		 * Set Bar Characters for display advancement. Default is "=" for already done
		 * filling, and "=>" for head of done filling. Default is "|" for start and end
		 * separator . Ex : "|===> |"
		 * 
		 * @param doneFiller
		 *                   character(s) for already done filling
		 * @param doneHead
		 *                   character(s) for head of already done filling
		 * @see fr.noobeclair.hashcode.utils.ProgressBar.Builder#withBarOpt(String,
		 *      String, String, String)
		 * @return Builder
		 */
		public Builder withBarOpt(String doneFiller, String doneHead, String startSep, String endSep) {
			this.barFill = doneFiller;
			this.barHead = doneHead;
			this.barStartChar = startSep;
			this.barStartEnd = endSep;
			return this;
		}

		/**
		 * Set end message. If not empty, will be displayed once progress reach 100%
		 * Default is empty String (ie no message)
		 * 
		 * @param endMsg
		 * @return Builder
		 */
		public Builder withEndMessage(String endMsg) {
			this.barDone = endMsg;
			return this;
		}

		/**
		 * Set Bar display option, and choose how the bar will display progress
		 * Available option : - ProgressBarOption.BAR : Display a progress bar (ex :
		 * "|===> |") - ProgressBarOption.PERCENT : Display a percentage indicator -
		 * ProgressBarOption.COUNT : Display a current/all indicator (ex: 10/100) -
		 * ProgressBarOption.ETA : Display a Estimated Time to finish (ex : 01h35m20s) -
		 * ProgressBarOption.MSG : Display a msg before bar that can be provided during
		 * process (Ex : loading file)
		 * 
		 * DEFAULT is BAR and ETA. Ex : "|===> | ETA 01h35m20s"
		 * 
		 * @param opt
		 *            {@link ProgressBarOption}
		 * @see fr.noobeclair.hashcode.utils.ProgressBar.Builder#withOptions(List<ProgressBarOption>)
		 * @return Buidler
		 */
		public Builder withOption(ProgressBarOption opt) {
			if (opt != null) {
				this.progressOption = Arrays.asList(opt);
			} else {
				throw new IllegalArgumentException("Option cannot be null");
			}
			return this;
		}

		/**
		 * Set Bar display option, and choose how the bar will display progress
		 * Available option : - ProgressBarOption.BAR : Display a progress bar (ex :
		 * "|===> |") - ProgressBarOption.PERCENT : Display a percentage indicator -
		 * ProgressBarOption.COUNT : Display a current/all indicator (ex: 10/100) -
		 * ProgressBarOption.ETA : Display a Estimated Time to finish (ex : 01h35m20s) -
		 * ProgressBarOption.MSG : Display a msg before bar that can be provided during
		 * process (Ex : loading file)
		 * 
		 * DEFAULT is BAR and ETA. Ex : "|===> | ETA 01h35m20s"
		 * 
		 * @param opt
		 *            {@link ProgressBarOption}
		 * @see fr.noobeclair.hashcode.utils.ProgressBar.Builder#withOptions(List<ProgressBarOption>)
		 * @return Buidler
		 */
		public Builder withOptions(List<ProgressBarOption> opts) {
			if (CollectionUtils.isNotEmpty(opts)) {
				this.progressOption = opts;
			} else {
				throw new IllegalArgumentException("Option cannot be null");
			}
			return this;
		}

		/**
		 * Set msg area size if the ProgressBarOption.MSG is set. Default is 1/3 of
		 * maxwidth
		 * 
		 * TODO : Add method to set size in percentage of maxwidth
		 * 
		 * @param size
		 * @return Builder
		 */
		public Builder withBarMsgSize(Integer size) {
			if (CollectionUtils.isNotEmpty(this.progressOption) && CollectionUtils.containsAny(this.progressOption,
					Arrays.asList(ProgressBarOption.MSG, ProgressBarOption.ALL))) {
				this.barMsgSize = size;
			} else {
				throw new IllegalArgumentException(
						"Bar message size cannot be set if ProgressBarOption.MSG is not set");
			}
			return this;
		}

		/**
		 * Set the time between each display refresh. Default 10 milliseconds.
		 * 
		 * @param ms
		 *           time between each display refresh (in ms)
		 * @return Builder
		 */
		public Builder withRefreshTime(Long ms) {
			if (ms != null) {
				this.refreshTime = ms;
			} else {
				throw new IllegalArgumentException("Refresh Time cannot be null");
			}
			return this;
		}

		/**
		 * Enable or disable automatic refreshTime calculation. if activated, refresh
		 * time is adjusted according to each item computation time. Default is true
		 * (activated)
		 * 
		 * @param auto
		 *             boolean
		 * @return Builder
		 */
		public Builder withAutoRefreshTime(boolean auto) {
			this.autoRefreshTimeCalculation = auto;
			return this;
		}

		public void updateMsgSize() {
			if (this.barMsgSize == null) {
				this.barMsgSize = new BigDecimal(this.maxwidth).multiply(DEFAULT_BAR_MSG_SIZE_PER).intValue();
			}
		}

		/**
		 * Build that fuckin ProgresBar yo !
		 * 
		 * @return ProgressBar
		 */
		public ProgressBar build() {
			updateMsgSize();
			return new ProgressBar(this);
		}

	}

}
