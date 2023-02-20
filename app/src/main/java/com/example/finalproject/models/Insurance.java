package com.example.finalproject.models;

public class Insurance extends TravelDoc {

    private String insuranceCompany;

    public Insurance(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }
    public Insurance() {
    }

    public Insurance(String id, String documentUrl, String insuranceCompany) {
        super(id, documentUrl);
        this.insuranceCompany = insuranceCompany;
    }

    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }
}

