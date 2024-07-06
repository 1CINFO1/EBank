package  com.ebank.application.models;



import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Transfer {
    private int id;
    private int idReceiver;
    private int idSender;
    private double montant;
    private Date date;
    private LocalDateTime date2;
    private String type;


    public Transfer() {
    }

    public Transfer(int idReceiver, int idSender, double montant, Date date, String type) {
        this.idReceiver = idReceiver;
        this.idSender = idSender;
        this.montant = montant;
        this.date = date;
        this.type = type;
    }

    public Transfer(int id, int idReceiver, int idSender, double montant, Date date, String type) {
        this.id=id;
        this.idReceiver = idReceiver;
        this.idSender = idSender;
        this.montant = montant;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdReceiver() {
        return idReceiver;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setIdReceiver(int idReceiver) {
        this.idReceiver = idReceiver;
    }

    public int getIdSender() {
        return idSender;
    }

    public void setIdSender(int idSender) {
        this.idSender = idSender;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }


    @Override
    public String toString() {
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return "Transaction{" +
                "id=" + id +
                ", idReceiver=" + idReceiver +
                ", idSender=" + idSender +
                ", montant=" + montant +
                ", date=" + date +
                ", type='" + type + '\'' +
                '}';
    }

}

