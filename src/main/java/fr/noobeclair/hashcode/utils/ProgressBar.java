package fr.noobeclair.hashcode.utils;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.commons.lang3.StringUtils;

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
    private final Integer progressOption;
    private Long startTime;
    private Long lastRefresh;
    private final Integer msgSize;

    public static final Integer ONLY_BAR = 0;
    public static final Integer BAR_PERCENT = 10;
    public static final Integer BAR_COUNT = 20;
    public static final Integer BAR_ALL = 30;
    public static final Integer BAR_ETA = 20;
    public static final Integer BAR_MSG = 40;

    public Long REFRESH_MS = 10L;
    public static final BigDecimal MIN = new BigDecimal("20");
    public static final BigDecimal MAX = new BigDecimal("2000");
    public static final BigDecimal MIN_DISPLAY = new BigDecimal("30");
    public static final int UPDATE_REFRESH_STEP = 1;
    public static final BigDecimal MAX_DISPLAY_SINCE_START = new BigDecimal("2");

    public static final String PAD_STR = " ";
    public static final String REMAIN_FILL_STR = " ";
    public static final String PAD_NB_STR = " ";
    public static final BigDecimal DEFAULT_BAR_MSG_SIZE_PER = new BigDecimal("0.33");

    public ProgressBar(final Long count, final Integer maxwidth, final String progressStart, final String progressEnd, final String progressFill, final String progressHead,
                       final String progressDone) {
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
        this.step = 0L;
        this.stepsize = 0;
        this.lpad = 0;
        this.rpad = 0;
        this.progressOption = BAR_ALL;
        this.currentStepTime = System.currentTimeMillis();
        this.msgSize = new BigDecimal(this.maxwidth).multiply(DEFAULT_BAR_MSG_SIZE_PER).intValue();
        prepare();
    }

    public ProgressBar(final Long count, final Integer maxwidth, final String progressStart, final String progressEnd, final String progressFill, final String progressHead,
                       final String progressDone, final Integer progressOption) {
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
        this.step = 0L;
        this.stepsize = 0;
        this.lpad = 0;
        this.rpad = 0;
        this.progressOption = progressOption;
        this.currentStepTime = System.currentTimeMillis();
        this.msgSize = new BigDecimal(this.maxwidth).multiply(DEFAULT_BAR_MSG_SIZE_PER).intValue();
        prepare();
    }

    public ProgressBar(final Long count, final Integer maxwidth, final String progressStart, final String progressEnd, final String progressFill, final String progressHead,
                       final String progressDone, final Integer lpad, final Integer rpad) {
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
        this.step = 0L;
        this.stepsize = 0;
        this.lpad = lpad;
        this.rpad = rpad;
        this.progressOption = BAR_ALL;
        this.currentStepTime = System.currentTimeMillis();
        this.msgSize = new BigDecimal(this.maxwidth).multiply(DEFAULT_BAR_MSG_SIZE_PER).intValue();
        prepare();
    }

    public ProgressBar(final Long count, final Integer maxwidth, final String progressStart, final String progressEnd, final String progressFill, final String progressHead,
                       final String progressDone, final Integer lpad, final Integer rpad,
                       final Integer progressOption) {
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
        this.step = 0L;
        this.stepsize = 0;
        this.lpad = lpad;
        this.rpad = rpad;
        this.progressOption = progressOption;
        this.currentStepTime = System.currentTimeMillis();
        this.msgSize = new BigDecimal(this.maxwidth).multiply(DEFAULT_BAR_MSG_SIZE_PER).intValue();
        prepare();
    }

    public ProgressBar(final Integer count, final Integer maxwidth, final String progressStart, final String progressEnd, final String progressFill, final String progressHead,
                       final String progressDone) {
        super();
        this.startTime = 0L;
        this.lastRefresh = 0L;
        this.end = count.longValue();
        this.maxwidth = maxwidth;
        this.progressStart = progressStart;
        this.progressEnd = progressEnd;
        this.progressFill = progressFill;
        this.progressHead = progressHead;
        this.progressDone = progressDone;
        this.progressMeanTime = new BigDecimal("0");
        this.step = 0L;
        this.stepsize = 0;
        this.lpad = 0;
        this.rpad = 0;
        this.progressOption = BAR_ALL;
        this.currentStepTime = System.currentTimeMillis();
        this.msgSize = new BigDecimal(this.maxwidth).multiply(DEFAULT_BAR_MSG_SIZE_PER).intValue();
        prepare();
    }

    public ProgressBar(final Integer count, final Integer maxwidth, final String progressStart, final String progressEnd, final String progressFill, final String progressHead,
                       final String progressDone, final Integer progressOption) {
        super();
        this.startTime = 0L;
        this.lastRefresh = 0L;
        this.end = count.longValue();
        this.maxwidth = maxwidth;
        this.progressStart = progressStart;
        this.progressEnd = progressEnd;
        this.progressFill = progressFill;
        this.progressHead = progressHead;
        this.progressDone = progressDone;
        this.progressMeanTime = new BigDecimal("0");
        this.step = 0L;
        this.stepsize = 0;
        this.lpad = 0;
        this.rpad = 0;
        this.progressOption = progressOption;
        this.currentStepTime = System.currentTimeMillis();
        this.msgSize = new BigDecimal(this.maxwidth).multiply(DEFAULT_BAR_MSG_SIZE_PER).intValue();
        prepare();
    }

    public ProgressBar(final Integer count, final Integer maxwidth, final String progressStart, final String progressEnd, final String progressFill, final String progressHead,
                       final String progressDone, final Integer lpad, final Integer rpad) {
        super();
        this.startTime = 0L;
        this.lastRefresh = 0L;
        this.end = count.longValue();
        this.maxwidth = maxwidth;
        this.progressStart = progressStart;
        this.progressEnd = progressEnd;
        this.progressFill = progressFill;
        this.progressHead = progressHead;
        this.progressDone = progressDone;
        this.progressMeanTime = new BigDecimal("0");
        this.step = 0L;
        this.stepsize = 0;
        this.lpad = lpad;
        this.rpad = rpad;
        this.progressOption = BAR_ALL;
        this.currentStepTime = System.currentTimeMillis();
        this.msgSize = new BigDecimal(this.maxwidth).multiply(DEFAULT_BAR_MSG_SIZE_PER).intValue();
        prepare();
    }

    public ProgressBar(final Integer count, final Integer maxwidth, final String progressStart, final String progressEnd, final String progressFill, final String progressHead,
                       final String progressDone, final Integer lpad, final Integer rpad,
                       final Integer progressOption) {
        super();
        this.startTime = 0L;
        this.lastRefresh = 0L;
        this.end = count.longValue();
        this.maxwidth = maxwidth;
        this.progressStart = progressStart;
        this.progressEnd = progressEnd;
        this.progressFill = progressFill;
        this.progressHead = progressHead;
        this.progressDone = progressDone;
        this.progressMeanTime = new BigDecimal("0");
        this.step = 0L;
        this.stepsize = 0;
        this.lpad = lpad;
        this.rpad = rpad;
        this.progressOption = progressOption;
        this.currentStepTime = System.currentTimeMillis();
        this.msgSize = new BigDecimal(this.maxwidth).multiply(DEFAULT_BAR_MSG_SIZE_PER).intValue();
        prepare();
    }

    private void reset() {
        this.startTime = 0L;
        this.lastRefresh = 0L;
        this.progressMeanTime = new BigDecimal("0");
        // this.step = 0L;
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
        if (BAR_MSG.equals(this.progressOption) || BAR_ALL.equals(this.progressOption)) {
            msgSize = this.msgSize;
        }
        if (BAR_PERCENT.equals(this.progressOption) || BAR_ALL.equals(this.progressOption)) {
            // - xxx%
            percentsize = "xxx%".length();
        }
        if (BAR_COUNT.equals(this.progressOption) || BAR_ALL.equals(this.progressOption)) {
            // - end/end
            countsize = "/".length() + (end.toString().length() * 2);
        }
        if (BAR_ETA.equals(this.progressOption) || BAR_ALL.equals(this.progressOption)) {
            // - 00h00m00s
            etasize = "00h00m00s".length();
        }
        if (BAR_COUNT.equals(this.progressOption) || BAR_ALL.equals(this.progressOption) || BAR_PERCENT.equals(this.progressOption) || BAR_ETA.equals(this.progressOption)) {
            queuesize = " [  ] ".length();
            if (countsize > 0 && percentsize > 0 && etasize > 0) {
                queuesize = queuesize + (" - ".length() * 2);
            } else if ((countsize > 0 && percentsize > 0) || (countsize > 0 && etasize > 0) || (etasize > 0 && percentsize > 0)) {
                queuesize = queuesize + " - ".length();
            }
            queuesize = queuesize + countsize + percentsize + etasize;
        }
        // Taille de la zone de chargement, si maxwidth = 100, 96
        this.progressWidth = this.maxwidth - (msgSize + this.lpad + this.rpad + this.progressStart.length() + this.progressEnd.length() + queuesize);
        // pas de chargement = nombre d'item par unité de la zone de chargement. si end
        // : 480 = 5
        this.stepsize = ((Long) Math.round((Math.ceil((double) (end) / progressWidth)))).intValue() * this.progressFill.length();
        // lissage de la taille de la zone de chargement
        this.progressWidth = ((Long) Math.round(Math.ceil((double) this.end / stepsize))).intValue() + this.progressHead.length();
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

    private String getQueue(final Long step) {
        final StringBuffer buf = new StringBuffer();
        if (BAR_COUNT.equals(this.progressOption) || BAR_ALL.equals(this.progressOption) || BAR_PERCENT.equals(this.progressOption) || BAR_ETA.equals(this.progressOption)) {
            buf.append(" [ ");
            if (BAR_COUNT.equals(this.progressOption) || BAR_ALL.equals(this.progressOption)) {
                buf.append("" + pad("" + step, "" + end) + "/" + end);
            }
            if ((BAR_COUNT.equals(this.progressOption) && BAR_PERCENT.equals(this.progressOption)) || (BAR_COUNT.equals(this.progressOption) && BAR_ETA.equals(this.progressOption))
                            || BAR_ALL.equals(this.progressOption)) {
                buf.append(" - ");
            }
            if (BAR_PERCENT.equals(this.progressOption) || BAR_ALL.equals(this.progressOption)) {
                buf.append("" + (pad("" + step * 100 / this.end, "100")) + "%");
            }
            // || (BAR_PERCENT.equals(this.progressOption) &&
            // BAR_ETA.equals(this.progressOption))
            if ((BAR_PERCENT.equals(this.progressOption) && BAR_ETA.equals(this.progressOption)) || BAR_ALL.equals(this.progressOption)) {
                buf.append(" - ");
            }
            if (BAR_ETA.equals(this.progressOption) || BAR_ALL.equals(this.progressOption)) {
                buf.append("ETA " + Utils.formatToHHMMSS(this.progressMeanTime.multiply(new BigDecimal(end - step)).longValue()));
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
        final Integer endbar = ((Long) Math.round(Math.ceil(((double) step / this.stepsize)))).intValue() - (this.progressHead.length() / this.progressHead.length());
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
        final String DOT = "...";
        final int max = this.msgSize - (DOT.length() + 1);
        if (StringUtils.isNotEmpty(s)) {
            if (s.length() > max) {
                return s.substring(0, this.msgSize - (DOT.length() + 1)) + DOT;
            } else if (s.length() == max) {
                return s + DOT;
            } else {
                return String.format("%1$-" + (max + DOT.length()) + "s", s);
            }
        }
        return String.format("%1$-" + (max + DOT.length()) + "s", " ");
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

    public void show(final PrintStream out, final Long step) {
        show(out, step, null);
    }

    public void show(final PrintStream out, final Long step, final String msg) {
        this.step = step;
        setStart();
        if (step.equals(end)) {
            out.println(getProgress(step, msg));
            // out.println(this.progressDone);
            reset();
        } else // if (step % stepsize == 0) {
        {
            calcMeanTime(step);
            if (isRefresh()) {
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
        if (BAR_ETA.equals(this.progressOption) || BAR_ALL.equals(this.progressOption)) {
            this.progressTotalTime = new BigDecimal("" + (System.currentTimeMillis() - this.startTime));
            final BigDecimal currSec = new BigDecimal("" + (System.currentTimeMillis() - this.currentStepTime)).divide(new BigDecimal("1000"));
            if (step != 0) {
                // (moy * stepdone + currtime) / stepdone + 1
                // this.progressMeanTime = ((this.progressMeanTime * step +currSec) / (step+1));
                this.progressMeanTime = this.progressTotalTime.divide(new BigDecimal(step + 1), 3, RoundingMode.HALF_UP).divide(new BigDecimal("1000"));
            } else {
                this.progressMeanTime = currSec;
            }
            this.currentStepTime = System.currentTimeMillis();
        }
    }

    private void updateRefreshTime(final Long step) {

        final double per = step * 100D / this.end;
        final BigDecimal start = new BigDecimal(System.currentTimeMillis() - this.startTime);
        BigDecimal nbRefresh = this.REFRESH_MS > 0L ? new BigDecimal(this.REFRESH_MS) : BigDecimal.ONE;
        final BigDecimal nbDisplaySinceStart = start.divide(nbRefresh, 2, RoundingMode.HALF_UP);
        if ((per > 0 && per % UPDATE_REFRESH_STEP == 0) || nbDisplaySinceStart.compareTo(MAX_DISPLAY_SINCE_START) > 0) {

            final BigDecimal meanMs = this.progressTotalTime.multiply(new BigDecimal(step + 1));
            BigDecimal totalEstimatedTime = BigDecimal.ZERO;

            totalEstimatedTime = meanMs.multiply(new BigDecimal(end - step)).add(start);

            if (totalEstimatedTime.compareTo(BigDecimal.ZERO) > 0) {
                nbRefresh = totalEstimatedTime.divide(nbRefresh, 2, RoundingMode.HALF_UP);
            }
            if (nbRefresh.compareTo(MIN) < 0) {
                this.REFRESH_MS = totalEstimatedTime.divide(MIN_DISPLAY, 3, RoundingMode.HALF_UP).longValue();
            } else if (meanMs.compareTo(new BigDecimal(REFRESH_MS)) > 0) {
                this.REFRESH_MS = meanMs.min(MAX).longValue();
            } else {
                this.REFRESH_MS = MAX.longValue();
            }
            // System.out.println(totalEstimatedTime+" - "+this.REFRESH_MS+" - "+meanMs+ " -
            // "+nbRefresh+ " - "+totalEstimatedTime.compareTo(BigDecimal.ZERO)+" -
            // "+totalEstimatedTime.divide(MIN_DISPLAY)+ " - "+nbDisplaySinceStart);
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

}
