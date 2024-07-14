package com.ebank.application.models;

import javafx.scene.layout.HBox;

import java.util.Date;

public class Cheque {
    private int id;
    private Date dateEmission;
    private String titulaire;
    private int numberOfPapers; // Number of pages in the cheque book
    private int userId; // Reference to User ID
    private String status;
    private HBox action;

    public Cheque() {
        this.status = "in progress"; // Set default status
    }

    public Cheque(int id, Date dateEmission, String titulaire, int numberOfPapers, int userId) {
        this.id = id;
        this.dateEmission = dateEmission;
        this.titulaire = titulaire;
        this.numberOfPapers = numberOfPapers;
        this.userId = userId;
        this.status = "in progress"; // Set default status
    }


    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateEmission() {
        return dateEmission;
    }

    public void setDateEmission(Date dateEmission) {
        this.dateEmission = dateEmission;
    }

    public String getTitulaire() {
        return titulaire;
    }

    public void setTitulaire(String titulaire) {
        this.titulaire = titulaire;
    }

    public int getNumberOfPapers() {
        return numberOfPapers;
    }

    public void setNumberOfPapers(int numberOfPapers) {
        this.numberOfPapers = numberOfPapers;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public HBox  getAction() {
        return action;
    }

    public void setAction(HBox action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "Cheque{" +
                "id=" + id +
                ", dateEmission=" + dateEmission +
                ", titulaire='" + titulaire + '\'' +
                ", numberOfPapers=" + numberOfPapers +
                ", userId=" + userId +
                ", status='" + status + '\'' +
                '}';
    }
}
