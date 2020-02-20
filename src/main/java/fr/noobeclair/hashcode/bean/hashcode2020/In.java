package fr.noobeclair.hashcode.bean.hashcode2020;

import java.util.List;

public class In {
    
    private Integer days;
    
    private List<Library> libraries;
    
    private List<Book> books;

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public List<Library> getLibraries() {
        return libraries;
    }

    public void setLibraries(List<Library> libraries) {
        this.libraries = libraries;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
    
}
