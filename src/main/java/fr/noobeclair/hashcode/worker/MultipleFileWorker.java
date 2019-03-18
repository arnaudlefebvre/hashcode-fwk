package fr.noobeclair.hashcode.worker;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.out.OutWriter;
import fr.noobeclair.hashcode.score.ScoreCalculator;
import fr.noobeclair.hashcode.solve.Solver;

public class MultipleFileWorker<T extends BeanContainer> extends MultipleWorker<T> {

    protected Solver<T> solver;
    protected List<InOut> files;

    public MultipleFileWorker(final InReader<T> reader, final OutWriter<T> writer, final ScoreCalculator<T> scorer, final Solver<T> solver,
                              final List<InOut> files) {
        super();
        this.reader = reader;
        this.writer = writer;
        this.scorer = scorer;
        this.solver = solver;
        this.files = files;
    }

    public MultipleFileWorker(final InReader<T> reader, final OutWriter<T> writer, final ScoreCalculator<T> scorer, final Solver<T> solver) {
        super();
        this.reader = reader;
        this.writer = writer;
        this.scorer = scorer;
        this.solver = solver;
        this.files = new ArrayList<>();
    }

    public void addFiles(final List<InOut> files) {
        this.files = files;
    }

    public void addFile(final String in, final String out) {
        this.files.add(new InOut(in, out));
    }

    @Override
    public Map<String, BigDecimal> run() {
        final SimpleWorker<T> sw = new SimpleWorker<>(reader, solver, scorer, writer, StringUtils.EMPTY, StringUtils.EMPTY);
        final Map<String, BigDecimal> result = new TreeMap<>();
        for (final InOut io : files) {
            sw.setInOut(io);
            try {
                result.put(io.in, sw.run());
            } catch (final RuntimeException e) {
                logger.error("Something went wrong running this worker : {}", sw, e.getMessage());
                result.put(io.in, BigDecimal.ZERO);
            }
        }
        if (files.isEmpty()) {
            logger.error("No file - No run ... !");
        }
        return result;
    }

}
