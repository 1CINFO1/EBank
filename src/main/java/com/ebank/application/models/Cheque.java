package com.ebank.application.models;

import java.util.Date;

public class Cheque {
    private int id;
    private float montant;
    private Date dateEmission;
    private String titulaire;
    private String banque;

    public Cheque(int id, float montant, Date dateEmission, String titulaire, String banque) {
        this.id = id;
        this.montant = montant;
        this.dateEmission = dateEmission;
        this.titulaire = titulaire;
        this.banque = banque;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getMontant() {
        return montant;
    }

    public void setMontant(float montant) {
        this.montant = montant;
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

    public String getBanque() {
        return banque;
    }

    public void setBanque(String banque) {
        this.banque = banque;
    }

    // Méthodes
    public void ajouter() {
        // Code pour ajouter un chèque
    }

    public void modifier() {
        // Code pour modifier un chèque
    }

    public void supprimer() {
        // Code pour supprimer un chèque
    }

    @Override
    public String toString() {
        return "Cheque{" +
                "id=" + id +
                ", montant=" + montant +
                ", dateEmission=" + dateEmission +
                ", titulaire='" + titulaire + '\'' +
                ", banque='" + banque + '\'' +
                '}';
    }
}

