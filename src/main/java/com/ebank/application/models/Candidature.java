package com.ebank.application.models;
public class Candidature {
    // attributs
    private int id, job_id;
    private String nom, prenom, CV;

    // Contructeurs
    public Candidature() {
    }

    public Candidature(int job_id, String nom, String prenom, String CV) {
        this.job_id = job_id;
        this.nom = nom;
        this.prenom = prenom;
        this.CV = CV;
    }

    public Candidature(int job_id, int id, String nom, String prenom, String CV) {
        this.id = id;
        this.job_id = job_id;
        this.nom = nom;
        this.prenom = prenom;
        this.CV = CV;
    }
// Getters and setters

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getJob_id() {
        return this.job_id;
    }

    public void setJob_id(int job_id) {
        this.job_id = job_id;
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getCV() {
        return this.CV;
    }

    public void setCV(String CV) {
        this.CV = CV;
    }




    // Display

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", job_id='" + getJob_id() + "'" +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", CV='" + getCV() + "'" +
            "}";
    }



}




