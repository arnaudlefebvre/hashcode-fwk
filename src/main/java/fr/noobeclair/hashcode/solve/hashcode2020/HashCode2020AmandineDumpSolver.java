package fr.noobeclair.hashcode.solve.hashcode2020;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import fr.noobeclair.hashcode.bean.hashcode2020.Book;
import fr.noobeclair.hashcode.bean.hashcode2020.HashCode2020BeanContainer;
import fr.noobeclair.hashcode.bean.hashcode2020.Library;
import fr.noobeclair.hashcode.bean.hashcode2020.Out;

public class HashCode2020AmandineDumpSolver extends AbstractHashCode2020Solver {

    public HashCode2020AmandineDumpSolver() {
        super();
    }

    @Override
    protected HashCode2020BeanContainer runWithStat(HashCode2020BeanContainer data) {
        long start = System.currentTimeMillis();
        
        Out out = new Out();
        
        List<Library> libraries = data.getIn().getLibraries();

//        for (Library library : libraries) {
//            Set<Integer> booksIds = library.getIdAndFlagBooks().stream().map(Pair::getLeft).collect(Collectors.toSet());
//            int totalBookScore = booksIds.stream().map(bookId -> data.getIn().getBooks().get(bookId).getScore()).reduce(0, (a, b) -> a + b);
//            library.setMaxScoreLicorne(15 * totalBookScore / (52 * booksIds.size()));
//        }
//
//        Comparator<Library> compLib = new Comparator<Library>() {
//            @Override
//            public int compare(Library o1, Library o2) {
//                return o1.getMaxScoreLicorne() == o2.getMaxScoreLicorne() ? 0 : o1.getMaxScoreLicorne() > o2.getMaxScoreLicorne() ? -1 : 1;
//            }
//        };
//        Comparator<Library> compLib = new Comparator<Library>() {
//            @Override
//            public int compare(Library o1, Library o2) {
//                return o1.getIdAndFlagBooks().size() == o2.getIdAndFlagBooks().size() ? 0 : o1.getIdAndFlagBooks().size() > o2.getIdAndFlagBooks().size() ? -1 : 1;
//            }
//        };
//        Comparator<Library> compLib = new Comparator<Library>() {
//            @Override
//            public int compare(Library o1, Library o2) {
//                return o1.getSignupDay() == o2.getSignupDay() ? 0 : o1.getSignupDay() < o2.getSignupDay() ? -1 : 1;
//            }
//        };
//        Comparator<Library> compLib = new Comparator<Library>() {
//            @Override
//            public int compare(Library o1, Library o2) {
//                return o1.getSignupDay() == o2.getSignupDay() ? 0 : o1.getSignupDay() > o2.getSignupDay() ? -1 : 1;
//            }
//        };
//        Comparator<Library> compLib = new Comparator<Library>() {
//            @Override
//            public int compare(Library o1, Library o2) {
//                return o1.getShippingNumber() == o2.getShippingNumber() ? 0 : o1.getShippingNumber() < o2.getShippingNumber() ? -1 : 1;
//            }
//        };
        Comparator<Library> compLib = new Comparator<Library>() {
            @Override
            public int compare(Library o1, Library o2) {
                return 2*o1.getSignupDay() / 18*o1.getShippingNumber() == 2*o2.getSignupDay() / 18*o2.getShippingNumber() ? 0 : 2*o1.getSignupDay() / 18*o1.getShippingNumber() < 2*o2.getSignupDay() / 18*o2.getShippingNumber() ? -1 : 1;
            }
        };
        
     // Tri de books par score
        Comparator<Book> compBook = new Comparator<Book>() {
            @Override
            public int compare(Book o1, Book o2) {
                return o1.getScore() == o2.getScore() ? 0 : o1.getScore() > o2.getScore() ? -1 : 1;
            }};
        // Tri d'Id book
        Comparator<Integer> compIdBook = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                final Book book1 = data.getIn().getBooks().stream().filter(book -> book.getId() == o1).findFirst().get();
                final Book book2 = data.getIn().getBooks().stream().filter(book -> book.getId() == o2).findFirst().get();
                return compBook.compare(book1, book2);
            }};
            
        Collections.sort(libraries, compLib);
        
        Map<Integer, List<Integer>> orderedSignupLibrairiesWithScannedBooks = new TreeMap<>();
        
        int limitDay = data.getIn().getDays();
        int currentDay = 0;
        
        boolean signupLoading = false;
        AtomicInteger currentSignupLibraryId = new AtomicInteger(-1);
        Integer remainingSignupDay = -1;
        
        while (currentDay < limitDay) {
            if (!signupLoading) {
                Optional<Library> libraryOpt = libraries.stream().filter(library -> !library.isFlag()).findFirst();
                if (libraryOpt.isPresent()) {
                    signupLoading = true;
                    Library library = libraryOpt.get();
                    currentSignupLibraryId.set(library.getId());
                    remainingSignupDay = library.getSignupDay();
                }
            } else if (remainingSignupDay.equals(0)) {
                Library library = libraries.stream().filter(libraryTmp -> libraryTmp.getId().equals(currentSignupLibraryId.get())).findFirst().get();
                library.setFlag(true);
                
                signupLoading = false;
                currentSignupLibraryId.set(-1);
                remainingSignupDay = null;
                
                orderedSignupLibrairiesWithScannedBooks.put(library.getId(), new ArrayList<Integer>());
            }
            
            for (Library library : libraries.stream().filter(Library::isFlag).collect(Collectors.toList())) {
                int shippingNumber = library.getShippingNumber();
                
                List<Pair<Integer, Boolean>> booksNotScanned = library.getIdAndFlagBooks().stream().filter(idAndFlagBook -> !idAndFlagBook.getRight()).collect(Collectors.toList());
//                Collections.sort(booksNotScanned.stream().map(Pair::getLeft).collect(Collectors.toList()), compIdBook);
                for (int i = 0 ; i < shippingNumber ; i++) {
                    if (i < booksNotScanned.size()) {
                        Pair<Integer, Boolean> currentBook = booksNotScanned.get(i);
                        currentBook.setValue(Boolean.TRUE);
                        orderedSignupLibrairiesWithScannedBooks.get(library.getId()).add(currentBook.getLeft());
                    }
                }
            }
            
            if (remainingSignupDay != null) {
                remainingSignupDay--;
            }
            currentDay++;
//            System.out.println(currentDay);
//            if (System.currentTimeMillis() - start > 100000) {
//                currentDay = limitDay;
//            }
        }

        out.setOrderedSignupLibrairiesWithScannedBooks(orderedSignupLibrairiesWithScannedBooks);
        data.setOut(out);
        
        System.out.println(System.currentTimeMillis() - start);
        
        return data;
    }

    @Override
    protected void addConfigStats() {
        // TODO Auto-generated method stub
        
    }

}
