package com.ebank.application.models;

import java.util.Date;

public class Cheque {
    private int id;
    private Date dateEmission;
    private String titulaire;

    public Cheque(){

    }
    public Cheque(int id,  Date dateEmission, String titulaire) {
        this.id = id;
        this.dateEmission = dateEmission;
        this.titulaire = titulaire;
    }
    public Cheque( float montant, Date dateEmission, String titulaire) {
        this.dateEmission = dateEmission;
        this.titulaire = titulaire;
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




    @Override
    public String toString() {
        return "Cheque{" +
                "id=" + id +
                ", dateEmission=" + dateEmission +
                ", titulaire='" + titulaire + '\'' +
                '}';
    }
}

