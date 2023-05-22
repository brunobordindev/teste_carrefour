package com.example.testecarrefour.model;

import com.google.firebase.database.DatabaseReference;

public class User {

    private String IdUser;
    private String name;
    private String email;
    private String password;
    private Double revenueTotal = 0.00;
    private Double expenseTotal = 0.00;

    public User() {
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void salve(){
        DatabaseReference reference = Settings.getFirebaseReference();
        reference.child("users")
                .child(this.IdUser)
                .setValue(this);
    }

    public String getIdUser() {
        return IdUser;
    }

    public void setIdUser(String idUser) {
        IdUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getRevenueTotal() {
        return revenueTotal;
    }

    public void setRevenueTotal(Double revenueTotal) {
        this.revenueTotal = revenueTotal;
    }

    public Double getExpenseTotal() {
        return expenseTotal;
    }

    public void setExpenseTotal(Double expenseTotal) {
        this.expenseTotal = expenseTotal;
    }
}
