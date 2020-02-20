package fr.noobeclair.hashcode.score.hashcode2020;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import fr.noobeclair.hashcode.bean.hashcode2020.Book;
import fr.noobeclair.hashcode.bean.hashcode2020.HashCode2020BeanContainer;
import fr.noobeclair.hashcode.bean.hashcode2020.Library;
import fr.noobeclair.hashcode.score.ScoreCalculator;
import fr.noobeclair.hashcode.utils.dto.SolverResultDto;

public class Hashcode2020ScoreCalculator extends ScoreCalculator<HashCode2020BeanContainer> {

    public Hashcode2020ScoreCalculator() {

    }

    @Override
    protected SolverResultDto run(HashCode2020BeanContainer in, SolverResultDto currentResult) {
        BigDecimal result = BigDecimal.ZERO;

        // On prends toutes les bibliothéques flagé
        in.getIn().getLibraries().stream().filter(Library::isFlag).forEach(bibli -> {
            List<Book> listBookThisBibli = new ArrayList<>();
            for (int i : bibli.getIdBooks()) {

                // On prends tous les livres de cette bibli
                Book bookGet = in.getIn().getBooks().stream().filter(book -> book.getId() == i).findFirst().get();
                listBookThisBibli.add(bookGet);
            }

            listBookThisBibli.stream().filter(Book::isFlag).forEach(book -> {
               result.add(BigDecimal.valueOf(book.getScore()));
            });

        });

        currentResult.setScore(result);

        return currentResult;
    }

}
