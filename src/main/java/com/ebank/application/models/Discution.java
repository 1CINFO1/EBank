package com.ebank.application.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Discution {
    // Attributes
    private int id;
    private String nom;
    private LocalDateTime dateCreation;

    // Constructor with parameters
    public Discution(int id, String nom, LocalDateTime dateCreation) {
        this.id = id;
        this.nom = nom;
        this.dateCreation = dateCreation;
    }

    // Constructor without id
    public Discution(String nom, LocalDateTime dateCreation) {
        this.nom = nom;
        this.dateCreation = dateCreation;
    }

    // Default constructor
    public Discution() {
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    // toString method for displaying object information
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return "Discution{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", dateCreation=" + dateCreation.format(formatter) +
                '}';
    }
}