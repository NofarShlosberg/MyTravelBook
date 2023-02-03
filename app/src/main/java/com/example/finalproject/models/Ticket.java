package com.example.finalproject.models;

import java.util.List;

public class Ticket extends TravelDoc {
    private String airportSource;
    private List<String> stopPoints;
    private String destinationAirport;
    private double flightTimeInHours;

    public Ticket(String id, String documentUrl, String airportSource, List<String> stopPoints, String destinationAirport, double flightTimeInHours) {
        super(id, documentUrl);
        this.airportSource = airportSource;
        this.stopPoints = stopPoints;
        this.destinationAirport = destinationAirport;
        this.flightTimeInHours = flightTimeInHours;
    }

    public Ticket() {
    }

    public String getAirportSource() {
        return airportSource;
    }

    public void setAirportSource(String airportSource) {
        this.airportSource = airportSource;
    }

    public List<String> getStopPoints() {
        return stopPoints;
    }

    public void setStopPoints(List<String> stopPoints) {
        this.stopPoints = stopPoints;
    }

    public String getDestinationAirport() {
        return destinationAirport;
    }

    public void setDestinationAirport(String destinationAirport) {
        this.destinationAirport = destinationAirport;
    }

    public double getFlightTimeInHours() {
        return flightTimeInHours;
    }

    public void setFlightTimeInHours(double flightTimeInHours) {
        this.flightTimeInHours = flightTimeInHours;
    }
}
