package com.example.finalproject.repositories;

public enum TravelType {
    Created("createdTravels"),
    Connected("connectedTravels");
    private final String type;
    TravelType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
