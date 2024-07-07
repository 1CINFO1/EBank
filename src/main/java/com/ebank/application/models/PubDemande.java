package com.ebank.application.models;

public class PubDemande {
    private int id;
    private String title;
    private String description;
    private String campaignName;
    private String status;


    public PubDemande(String title, String status, String campaignName, String description) {
        this.title = title;
        this.status = status;
        this.campaignName = campaignName;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
