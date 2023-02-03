package com.example.finalproject.models;

public abstract class TravelDoc {
    private String id;
    private String documentUrl;

    public TravelDoc() {

    }

    public TravelDoc(String id, String documentUrl) {
        this.id = id;
        this.documentUrl = documentUrl;
    }

    public String getDocumentUrl() {
        return documentUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }
}
