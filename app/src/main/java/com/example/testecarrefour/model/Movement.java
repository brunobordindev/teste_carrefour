package com.example.testecarrefour.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Movement {

    private String date;
    private String category;
    private String description;
    private String type;
    private double value;
    private String key;

    public Movement() {
    }

    public void salve(String dataEscolhida) {

        FirebaseAuth auth = Settings.getFirebaseAuth();
        String idUser = Base64Custom.codeBase64(auth.getCurrentUser().getEmail());

        String monthYear = DateUtil.yearMonthDateSelect(dataEscolhida);

        DatabaseReference reference = Settings.getFirebaseReference();
        reference.child("movement")
                .child(idUser)
                .child(monthYear)
                .push()
                .setValue(this);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
