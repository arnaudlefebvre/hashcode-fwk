package fr.noobeclair.hashcode.bean.hashcode2020;

import java.util.List;

public class Library {
    
    private Integer id;
    
    private Integer signupDay;
    
    private Integer shippingNumber;
    
    private List<Integer> idBooks;
    
    private boolean flag = false;

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

    public List<Integer> getIdBooks() {
        return idBooks;
    }

    public void setIdBooks(List<Integer> idBooks) {
        this.idBooks = idBooks;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    
    
    
}
