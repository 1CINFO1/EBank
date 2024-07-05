package  com.ebank.application.models;

import java.time.LocalDateTime;

public class Reclamation {
    private int id;
    private String contenu;
    private LocalDateTime dateEnvoi;
    private int idDiscution;
    private int idEmetteur;
    private int idRecepteur;
    private int idTrans;
    private String state;

    // Constructor with parameters
    public Reclamation(int id, String contenu, LocalDateTime dateEnvoi, int idDiscution, int idEmetteur,
            int idRecepteur, int idTrans) {
        this.id = id;
        this.contenu = contenu;
        this.dateEnvoi = dateEnvoi;
        this.idDiscution = idDiscution;
        this.idEmetteur = idEmetteur;
        this.idRecepteur = idRecepteur;
        this.idTrans = idTrans;
    }
    // Constructor without id

    public Reclamation(String contenu, LocalDateTime dateEnvoi2, int idDiscution, int idEmetteur, int idRecepteur) {
        this.contenu = contenu;
        this.dateEnvoi = dateEnvoi2;
        this.idDiscution = idDiscution;
        this.idEmetteur = idEmetteur;
        this.idRecepteur = idRecepteur;
    }

    // Default constructor
    public Reclamation() {
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getIdTrans() {
        return idTrans;
    }

    public void setIdTrans(int idTrans) {
        this.idTrans = idTrans;
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
        return "Reclamation{" +
                "id=" + id +
                ", contenu='" + contenu + '\'' +
                ", dateEnvoi=" + dateEnvoi +
                ", IDTransaction=" + idTrans +
                ", idDiscution=" + idDiscution +
                ", idEmetteur=" + idEmetteur +
                ", idRecepteur=" + idRecepteur +
                '}';
    }
}
