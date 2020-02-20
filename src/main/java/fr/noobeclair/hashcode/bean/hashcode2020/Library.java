package fr.noobeclair.hashcode.bean.hashcode2020;

import java.util.List;

public class Library {
    
    private Integer id;
    
    private Integer signupDay;
    
    private Integer shippingNumber;
    
    private List<Book> books;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSignupDay() {
        return signupDay;
    }

    public void setSignupDay(Integer signupDay) {
        this.signupDay = signupDay;
    }

    public Integer getShippingNumber() {
        return shippingNumber;
    }

    public void setShippingNumber(Integer shippingNumber) {
        this.shippingNumber = shippingNumber;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
    
    
}
