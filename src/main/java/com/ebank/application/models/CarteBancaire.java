package com.ebank.application.models;

import java.util.Date;

public class CarteBancaire {
    private int id;
    private String numero;
    private Date dateExpiration;
    private String titulaire;
    private String type;

    public CarteBancaire() {
    }

    public CarteBancaire(int id, String numero, Date dateExpiration, String titulaire, String type) {
        this.id = id;
        this.numero = numero;
        this.dateExpiration = dateExpiration;
        this.titulaire = titulaire;
        this.type = type;
    }

    public CarteBancaire(String numero, Date dateExpiration, String titulaire, String type) {
        this.numero = numero;
        this.dateExpiration = dateExpiration;
        this.titulaire = titulaire;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(Date dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public String getTitulaire() {
        return titulaire;
    }

    public void setTitulaire(String titulaire) {
        this.titulaire = titulaire;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "CarteBancaire{" +
                "id=" + id +
                ", numero='" + numero + '\'' +
                ", dateExpiration=" + dateExpiration +
                ", titulaire='" + titulaire + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
