package com.ebank.application.SharedDataMModels;

public class SharedDataModel {
    private static SharedDataModel instance;
    private int publicationId;

    private SharedDataModel() {
    }

    public static synchronized SharedDataModel getInstance() {
        if (instance == null) {
            instance = new SharedDataModel();
        }
        return instance;
    }

    public int getPublicationId() {
        return publicationId;
    }

    public void setPublicationId(int publicationId) {
        this.publicationId = publicationId;
    }
}