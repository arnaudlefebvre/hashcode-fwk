package fr.noobeclair.hashcode.solve.hashcode2020;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

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
        
        Map<Integer, List<Integer>> orderedSignupLibrairiesWithScannedBooks = new TreeMap<>();
        

        int limitDay = data.getIn().getDays();
        int currentDay = 0;
        
        boolean signupLoading = false;
        AtomicInteger currentSignupLibraryId = new AtomicInteger(-1);
        Integer remainingSignupDay = -1;
        
        while (currentDay < limitDay) {
            if (!signupLoading) {
                Optional<Library> libraryOpt = data.getIn().getLibraries().stream().filter(library -> !library.isFlag()).findFirst();
                if (libraryOpt.isPresent()) {
                    signupLoading = true;
                    Library library = libraryOpt.get();
                    currentSignupLibraryId.set(library.getId());
                    remainingSignupDay = library.getSignupDay();
                }
            } else if (remainingSignupDay.equals(0)) {
                Library library = data.getIn().getLibraries().stream().filter(libraryTmp -> libraryTmp.getId().equals(currentSignupLibraryId.get())).findFirst().get();
                library.setFlag(true);
                
                signupLoading = false;
                currentSignupLibraryId.set(-1);
                remainingSignupDay = null;
                
                orderedSignupLibrairiesWithScannedBooks.put(library.getId(), new ArrayList<Integer>());
            }
            
            for (Library library : data.getIn().getLibraries().stream().filter(Library::isFlag).collect(Collectors.toList())) {
                int shippingNumber = library.getShippingNumber();
                
                List<Pair<Integer, Boolean>> booksNotScanned = library.getIdAndFlagBooks().stream().filter(idAndFlagBook -> !idAndFlagBook.getRight()).collect(Collectors.toList());
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
            
        }

        out.setOrderedSignupLibrairiesWithScannedBooks(orderedSignupLibrairiesWithScannedBooks);
        data.setOut(out);
        
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        
        return data;
    }

    @Override
    protected void addConfigStats() {
        // TODO Auto-generated method stub
        
    }

}
