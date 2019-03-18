package fr.noobeclair.hashcode.worker;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import fr.noobeclair.hashcode.bean.BeanContainer;
import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.out.OutWriter;
import fr.noobeclair.hashcode.score.ScoreCalculator;
import fr.noobeclair.hashcode.solve.Solver;

//This class is only a list of SimpleWorker that are run one after the other and return a map of solver-score
public class MultipleSolverWorker<T extends BeanContainer> extends MultipleWorker<T> {

    protected List<SimpleWorker<T>> solvers;
    protected InOut inOut;

    public MultipleSolverWorker(final InReader<T> reader, final OutWriter<T> writer, final ScoreCalculator<T> scorer, final String in, final String out) {
        super();
        this.solvers = new ArrayList<>();
        this.reader = reader;
        this.writer = writer;
        this.scorer = scorer;
        this.inOut = new InOut(in, out);
    }

    public MultipleSolverWorker(final InReader<T> reader, final OutWriter<T> writer, final ScoreCalculator<T> scorer, final InOut inOut) {
        super();
        this.solvers = new ArrayList<>();
        this.reader = reader;
        this.writer = writer;
        this.scorer = scorer;
        this.inOut = inOut;
    }

    public void addSolver(final Solver<T> solver) {
        this.solvers.add(new SimpleWorker<>(reader, solver, scorer, writer, inOut.in, inOut.out + "-" + solver.getClass()));
    }

    @Override
    public Map<String, BigDecimal> run() {
        final Map<String, BigDecimal> result = new TreeMap<>();
        for (final SimpleWorker<T> sw : solvers) {
            try {
                result.put(sw.getSolver().getClass().getSimpleName(), sw.run());
            } catch (final RuntimeException e) {
                logger.error(" <###----- !!!!!! -----#> Something went wrong running this worker : {}", sw, e.getMessage());
                result.put(sw.getSolver().getClass().getSimpleName(), BigDecimal.ZERO);
            }
        }
        if (solvers.isEmpty()) {
            logger.error(" <###----- !!!!!! -----#> No solver - No run ... !");
        }
        return result;
    }

    public List<SimpleWorker<T>> getSolvers() {
        return solvers;
    }

    public void setSolvers(final List<SimpleWorker<T>> solvers) {
        this.solvers = solvers;
    }

    public InOut getInOut() {
        return inOut;
    }

    public void setInOut(final InOut inOut) {
        this.inOut = inOut;
    }

}
