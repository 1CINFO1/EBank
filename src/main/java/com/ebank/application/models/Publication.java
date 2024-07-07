package com.ebank.application.models;

import java.sql.Date;

public class Publication {
    private int id;
    private String title;
    private String campaignName;
    private String description;

    public Publication(String title, String campaignName, String description, String picture) {
        this.title = title;
        this.campaignName = campaignName;
        this.description = description;
        this.picture = picture;
    }

    private String picture;
    private Date publicationDate;
    private int CompagnieDeDon_Patente;
    public Publication(int id, int CompagnieDeDon_Patente, String campaignName, String title, String description, String picture, Date publicationDate) {
        this.id = id;
        this.campaignName = campaignName;
        this.title = title;
        this.description = description;
        this.picture = picture;
        this.publicationDate = publicationDate;
        this.CompagnieDeDon_Patente = CompagnieDeDon_Patente;
    }

    public int getCompagnieDeDon_Patente() {
        return CompagnieDeDon_Patente;
    }

    public void setCompagnieDeDon_Patente(int compagnieDeDon_Patente) {
        CompagnieDeDon_Patente = compagnieDeDon_Patente;
    }

    public Publication() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }
}
