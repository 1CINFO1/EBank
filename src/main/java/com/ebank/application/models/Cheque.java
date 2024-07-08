package com.ebank.application.models;

import java.util.Date;

public class Cheque {
    private int id;
    private Date dateEmission;
    private String titulaire;
    private int userId; // Reference to User ID

    public Cheque() {
    }

    public Cheque(int id, Date dateEmission, String titulaire, int userId) {
        this.id = id;
        this.dateEmission = dateEmission;
        this.titulaire = titulaire;
        this.userId = userId;
    }

    public Cheque(float montant, Date dateEmission, String titulaire, int userId) {
        this.dateEmission = dateEmission;
        this.titulaire = titulaire;
        this.userId = userId;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Cheque{" +
                "id=" + id +
                ", dateEmission=" + dateEmission +
                ", titulaire='" + titulaire + '\'' +
                ", userId=" + userId +
                '}';
    }
}

