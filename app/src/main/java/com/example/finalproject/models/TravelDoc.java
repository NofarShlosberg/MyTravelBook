package com.example.finalproject.models;

public class TravelDoc extends FirebaseModel {
    private String documentUrl;

    public TravelDoc() {
    }

    public TravelDoc(String id, String documentUrl) {
        super(id);
        this.documentUrl = documentUrl;
    }

    public String getDocumentUrl() {
        return documentUrl;
    }


    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }
}
