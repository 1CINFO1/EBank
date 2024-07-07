package com.ebank.application.models;

public class Candidature {
    // Attributes
    private int id;
    private int job_id;
    private String nom;
    private String prenom;
    private String cvPath; // Path to the CV PDF file

    // Constructors
    public Candidature() {
    }

    public Candidature(int job_id, String nom, String prenom, String cvPath) {
        this.job_id = job_id;
        this.nom = nom;
        this.prenom = prenom;
        this.cvPath = cvPath;
    }

    public Candidature(int job_id, int id, String nom, String prenom, String cvPath) {
        this.id = id;
        this.job_id = job_id;
        this.nom = nom;
        this.prenom = prenom;
        this.cvPath = cvPath;
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

    public String getCvPath() {
        return this.cvPath;
    }

    public void setCvPath(String cvPath) {
        this.cvPath = cvPath;
    }

    // Display
    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", job_id='" + getJob_id() + "'" +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", cvPath='" + getCvPath() + "'" +
            "}";
    }
}
