package fr.noobeclair.hashcode.solve.hashcode2020;

import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

import fr.noobeclair.hashcode.bean.hashcode2020.HashCode2020BeanContainer;
import fr.noobeclair.hashcode.bean.hashcode2020.Library;

public class HashCode2020AmandineDumpSolver extends AbstractHashCode2020Solver {

    public HashCode2020AmandineDumpSolver() {
        super();
    }

    @Override
    protected HashCode2020BeanContainer runWithStat(HashCode2020BeanContainer data) {
        long start = System.currentTimeMillis();
        
        int limitDay = data.getIn().getDays();
        int currentDay = 0;
        int librairiesNumber = data.getIn().getLibraries().size();
        int lastLibraryId = -1;
        
        while (currentDay <= limitDay) {
            for (Library library : data.getIn().getLibraries().stream().filter(Library::isFlag).collect(Collectors.toList())) {
                int shippingNumber = library.getShippingNumber();
//                List<>
                
            }
            
            currentDay ++;
        }
        
//        while (currentDay <= limitDay && lastLibraryId < librairiesNumber) {
//            
//            Library library = data.getIn().getLibraries().get(lastLibraryId  + 1);
//            
//            if (currentDay + library.getSignupDay() < limitDay) {
//                library.setFlag(true);
//                
//                for (int i = 0; i < library.getSignupDay(); i++) {
//                    
//                }
//                
//                currentDay += library.getSignupDay();
//            }
//            
//            
//            
//        }
        
        return data;
    }

    @Override
    protected void addConfigStats() {
        // TODO Auto-generated method stub
        
    }

}
