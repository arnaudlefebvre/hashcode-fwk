package fr.noobeclair.hashcode.in.hashcode2020;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.noobeclair.hashcode.bean.hashcode2020.Book;
import fr.noobeclair.hashcode.bean.hashcode2020.HashCode2020BeanContainer;
import fr.noobeclair.hashcode.bean.hashcode2020.In;
import fr.noobeclair.hashcode.bean.hashcode2020.Library;
import fr.noobeclair.hashcode.in.InReader;
import fr.noobeclair.hashcode.utils.FileUtils;

public class Hashcode2020Reader extends InReader<HashCode2020BeanContainer> {

	public Hashcode2020Reader() {

	}

	@Override
	protected HashCode2020BeanContainer readFile(List<String> lines, String in) {
		HashCode2020BeanContainer result = new HashCode2020BeanContainer(in);
		
		In inObject = new In();		
		
		String[] conf = FileUtils.getTabFromLineSpace(lines, 0);
		
		inObject.setDays(Integer.parseInt(conf[2]));
		List<Book> books = new ArrayList<>(Integer.parseInt(conf[0]));

        String[] bookScores = FileUtils.getTabFromLineSpace(lines, 1);
        for (int i = 0; i < bookScores.length; i++) {
            Book book = new Book();
            book.setId(i);
            book.setScore(Integer.parseInt(bookScores[i]));
            books.add(book);
        }
		inObject.setBooks(books);
        
        int librariesNumber = Integer.parseInt(conf[1]);
		
		List<Library> libraries = new ArrayList<>(librariesNumber);
		for (int i = 0; i < librariesNumber; i++) {
		    Library library = new Library();
		    library.setId(i);
		    
		    String[] uglyLibrary = FileUtils.getTabFromLineSpace(lines, i * 2 + 2);
		    
		    library.setSignupDay(Integer.parseInt(uglyLibrary[1]));
		    library.setShippingNumber(Integer.parseInt(uglyLibrary[2]));
		    
		    String[] uglyBooksOfLibrary = FileUtils.getTabFromLineSpace(lines, i * 2 + 3);
		    library.setIdBooks(Stream.of(uglyBooksOfLibrary).map(Integer::parseInt).collect(Collectors.toList()));
		    
		    libraries.add(library);
		}
		
		inObject.setLibraries(libraries);
		
		result.setIn(inObject);
		
		return result;
	}

}
