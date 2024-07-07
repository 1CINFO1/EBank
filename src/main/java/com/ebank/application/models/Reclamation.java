package  com.ebank.application.models;

import java.time.LocalDateTime;

public class Reclamation {
    private int id;
    private String title;
    private String description;
    private LocalDateTime dateEnvoi;
    private int idSender;

    // Constructor with parameters
    public Reclamation(int id, String title, String description, LocalDateTime dateEnvoi, int idSender) {
        this.id = id;
        this.title=title;
        this.description = description;
        this.dateEnvoi = dateEnvoi;
        this.idSender = idSender;

    }
    // Constructor without id

    public Reclamation(String titlte,String description, LocalDateTime dateEnvoi,  int idSender, int id_transaction) {
        this.title=title;
        this.description = description;
        this.dateEnvoi = dateEnvoi;
        this.idSender = idSender;

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


    public String getContenu() {
        return description;
    }

    public void setTitle(String title) {
        this.description = title;
    }
    public String getTitle() {
        return title;
    }

    public void setContenu(String contenu) {
        this.description = contenu;
    }

    public LocalDateTime getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(LocalDateTime dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }



    public int getIdSender() {
        return idSender;
    }

    public void setIdSender(int idSender) {
        this.idSender = idSender;
    }


    // toString method for displaying object information
    @Override
    public String toString() {
        return "Reclamation{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contenu='" + description + '\'' +
                ", dateEnvoi=" + dateEnvoi +
                
                ", idSender=" + idSender +
                
                '}';
    }
}
