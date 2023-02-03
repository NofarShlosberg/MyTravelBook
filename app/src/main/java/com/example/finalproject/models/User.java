package com.example.finalproject.models;

public class User extends FirebaseModel {
    private String name;
    private String email;
    private String image;

    private TravelDoc coronaCertificates;
    private TravelDoc passport;

    // all args constructor
    public User(String id, String name, String email, String image, TravelDoc coronaCertificates, TravelDoc passport) {
        super(id);
        this.name = name;
        this.email = email;
        this.image = image;
        this.coronaCertificates = coronaCertificates;
        this.passport = passport;
    }

    public TravelDoc getCoronaCertificates() {
        return coronaCertificates;
    }

    public void setCoronaCertificates(TravelDoc coronaCertificates) {
        this.coronaCertificates = coronaCertificates;
    }

    public TravelDoc getPassport() {
        return passport;
    }

    public void setPassport(TravelDoc passport) {
        this.passport = passport;
    }

    // no args constructor (firebase)
    public User() {}


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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
