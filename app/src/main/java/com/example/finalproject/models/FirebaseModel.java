package com.example.finalproject.models;

abstract public class FirebaseModel {
    private String id;

    public FirebaseModel(String id) {
        this.id = id;
    }

    public FirebaseModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}