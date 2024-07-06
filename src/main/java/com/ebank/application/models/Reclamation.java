package  com.ebank.application.models;

import java.time.LocalDateTime;

public class Reclamation {
    private int id;
    private String contenu;
    private LocalDateTime dateEnvoi;
    private int idEmetteur;
    private int id_transaction;

    // Constructor with parameters
    public Reclamation(int id, String contenu, LocalDateTime dateEnvoi, int idEmetteur, int id_transaction) {
        this.id = id;
        this.contenu = contenu;
        this.dateEnvoi = dateEnvoi;
        this.idEmetteur = idEmetteur;

        this.id_transaction = id_transaction;
    }
    // Constructor without id

    public Reclamation(String contenu, LocalDateTime dateEnvoi2,  int idEmetteur, int id_transaction) {
        this.contenu = contenu;
        this.dateEnvoi = dateEnvoi2;
        this.idEmetteur = idEmetteur;
        this.id_transaction = id_transaction;

    }

    // Default constructor
    public Reclamation() {
    }

    // Getters and Setters
    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getIdTrans() {
        return id_transaction;
    }

    public void setIdTrans(int id_transaction) {
        this.id_transaction = id_transaction;
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



    public int getIdEmetteur() {
        return idEmetteur;
    }

    public void setIdEmetteur(int idEmetteur) {
        this.idEmetteur = idEmetteur;
    }


    // toString method for displaying object information
    @Override
    public String toString() {
        return "Reclamation{" +
                "id=" + id +
                ", contenu='" + contenu + '\'' +
                ", dateEnvoi=" + dateEnvoi +
                ", IDTransaction=" + id_transaction +
                
                ", idEmetteur=" + idEmetteur +
                
                '}';
    }
}
