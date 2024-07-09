package com.ebank.application.models;

import java.time.LocalDateTime;

public class Message {
    // Attributes
    private int id;
    private String contenu;
    private LocalDateTime dateEnvoi;
    private int idDiscution;
    private int idEmetteur;
    private int idRecepteur;

    // Constructor with parameters
    public Message(int id, String contenu, LocalDateTime dateEnvoi, int idDiscution, int idEmetteur, int idRecepteur) {
        this.id = id;
        this.contenu = contenu;
        this.dateEnvoi = dateEnvoi;
        this.idDiscution = idDiscution;
        this.idEmetteur = idEmetteur;
        this.idRecepteur = idRecepteur;
    }
    // Constructor without id

    public Message(String contenu, LocalDateTime dateEnvoi, int idDiscution, int idEmetteur, int idRecepteur) {
        this.contenu = contenu;
        this.dateEnvoi = dateEnvoi;
        this.idDiscution = idDiscution;
        this.idEmetteur = idEmetteur;
        this.idRecepteur = idRecepteur;
    }

    // Default constructor
    public Message() {
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public LocalDateTime getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(LocalDateTime dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public int getIdDiscution() {
        return idDiscution;
    }

    public void setIdDiscution(int idDiscution) {
        this.idDiscution = idDiscution;
    }

    public int getIdEmetteur() {
        return idEmetteur;
    }

    public void setIdEmetteur(int idEmetteur) {
        this.idEmetteur = idEmetteur;
    }

    public int getIdRecepteur() {
        return idRecepteur;
    }

    public void setIdRecepteur(int idRecepteur) {
        this.idRecepteur = idRecepteur;
    }

    // toString method for displaying object information
    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", contenu='" + contenu + '\'' +
                ", dateEnvoi=" + dateEnvoi +
                ", idDiscution=" + idDiscution +
                ", idEmetteur=" + idEmetteur +
                ", idRecepteur=" + idRecepteur +
                '}';
    }
}
