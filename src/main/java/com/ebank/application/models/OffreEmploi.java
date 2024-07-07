package com.ebank.application.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class OffreEmploi {
    // attributs
    private int id;
    private String poste, sujet, type, emplacement;
    private LocalDate date_expiration;
    private LocalDateTime date_offre;

    // Contructeurs

    public OffreEmploi() {
    }

    public OffreEmploi(String poste, String sujet, String type, String emplacement, LocalDate date_expiration,
            LocalDateTime date_offre) {
        this.poste = poste;
        this.sujet = sujet;
        this.type = type;
        this.emplacement = emplacement;
        this.date_expiration = date_expiration;
        this.date_offre = date_offre;
    }

    public OffreEmploi(int id, String poste, String sujet, String type, String emplacement, LocalDate date_expiration,
            LocalDateTime date_offre) {
        this.id = id;
        this.poste = poste;
        this.sujet = sujet;
        this.type = type;
        this.emplacement = emplacement;
        this.date_expiration = date_expiration;
        this.date_offre = date_offre;
    }

    // Getters and setters

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPoste() {
        return this.poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public String getSujet() {
        return this.sujet;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmplacement() {
        return this.emplacement;
    }

    public void setEmplacement(String emplacement) {
        this.emplacement = emplacement;
    }

    public LocalDate getDate_expiration() {
        return this.date_expiration;
    }

    public void setDate_expiration(LocalDate date_expiration) {
        this.date_expiration = date_expiration;
    }

    public LocalDateTime getDate_offre() {
        return this.date_offre;
    }

    public void setDate_offre(LocalDateTime date_offre) {
        this.date_offre = date_offre;
    }

    // Display

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", poste='" + getPoste() + "'" +
                ", sujet='" + getSujet() + "'" +
                ", type='" + getType() + "'" +
                ", emplacement='" + getEmplacement() + "'" +
                ", date_expiration='" + getDate_expiration() + "'" +
                ", date_offre='" + getDate_offre() + "'" +
                "}";
    }

}
