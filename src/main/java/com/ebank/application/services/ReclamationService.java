
package  com.ebank.application.services;
import com.ebank.application.EmailUtil;
import com.ebank.application.interfaces.InterfaceCRUD;
import com.ebank.application.models.Reclamation;
import com.ebank.application.models.User;
import com.ebank.application.utils.MaConnexion;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReclamationService implements InterfaceCRUD<Reclamation> {
    // Connection instance
    Connection cnx = MaConnexion.getInstance().getCnx();

    @Override
    public String add(Reclamation r) {
    validateReclamation(r); // Validate before adding
    String req = "INSERT INTO `reclamation`(`title`,`description`, `date_envoi`, `idSender`) VALUES (?,?,?, ? )";
    try {
        PreparedStatement ps = cnx.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, r.getTitle());
        ps.setString(2, r.getContenu());
        ps.setTimestamp(3, java.sql.Timestamp.valueOf(r.getDateEnvoi()));
        ps.setInt(4, r.getIdSender());
        ps.executeUpdate();

        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            r.setId(generatedKeys.getInt(1)); // Set the generated ID to the reclamation object
        }
        String subject = "New Reclamation Created";
        String body = "A new reclamation has been created with the following details:\n" + r.toString();
        EmailUtil.sendEmail("recipient@example.com", subject, body);

        System.out.println("Reclamation ajoutée avec succès!");
        return "Reclamation ajoutée avec succès!";
    } catch (SQLException e) {
        throw new RuntimeException("Erreur lors de l'ajout de la réclamation", e);
    }
}
    @Override
    public void delete(int id) {
        if (!canDeleteReclamation(id)) {
            throw new IllegalArgumentException("La réclamation ne peut pas être supprimée pour l'instant.");
        }
        String req = "DELETE FROM `reclamation` WHERE `id` = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Réclamation supprimée avec succès!");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de la réclamation", e);
        }
    }

    @Override
    public void update(Reclamation r, int id) {
        validateReclamation(r); // Validate before updating
        String req = "UPDATE `reclamation` SET `contenu` = ?, `date_envoi` = ?, `idSender` = ? WHERE `id` = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, r.getContenu());
            ps.setTimestamp(2, java.sql.Timestamp.valueOf(r.getDateEnvoi()));
            ps.setInt(3, r.getIdSender());
            ps.executeUpdate();
            System.out.println("Réclamation mise à jour avec succès!");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de la réclamation", e);
        }
    }

    @Override
    public List<Reclamation> getAll() {
        List<Reclamation> reclamations = new ArrayList<>();
        String req = "SELECT * FROM `reclamation`";
        try {
            Statement st = cnx.createStatement();
            ResultSet res = st.executeQuery(req);
            while (res.next()) {
                Reclamation r = new Reclamation();
                r.setId(res.getInt("id"));
                r.setContenu(res.getString("description"));
                r.setDateEnvoi(res.getTimestamp("date_envoi").toLocalDateTime()); // Convert SQL Timestamp to LocalDateTime
                r.setIdSender(res.getInt("idSender"));

                reclamations.add(r);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des réclamations", e);
        }
        return reclamations;
    }

    private void validateReclamation(Reclamation r) {
        if (r.getContenu() == null || r.getContenu().isEmpty()) {
            throw new IllegalArgumentException("Le contenu de la réclamation ne peut pas être vide");
        }
        if (r.getDateEnvoi() == null) {
            throw new IllegalArgumentException("La date de la réclamation ne peut pas être nulle");
        }
        
        if (r.getIdSender() <= 0) {
            throw new IllegalArgumentException("ID d'émetteur invalide");
        }
        
    }

    private boolean canDeleteReclamation(int id) {
        Reclamation reclamation = getById(id);
        if (reclamation == null) {
            throw new IllegalArgumentException("Réclamation non trouvée avec l'ID: " + id);
        }
        // Add your delete logic here
        return true; // Placeholder logic; implement as per your requirements
    }

    private Reclamation getById(int id) {
        String req = "SELECT * FROM `reclamation` WHERE `id` = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ResultSet res = ps.executeQuery();
            if (res.next()) {
                Reclamation r = new Reclamation();
                r.setId(res.getInt("id"));
                r.setTitle(res.getString("title"));
                r.setContenu(res.getString("description"));
                r.setDateEnvoi(res.getTimestamp("date_envoi").toLocalDateTime());
                r.setIdSender(res.getInt("id_emetteur"));
                return r;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de la réclamation par ID", e);
        }
    }
     public User currentUser = new User();
    public String submitReclamation(String tiltle,String description, User  currentUser) {
        String req = "INSERT INTO `reclamation`(`title`,`description`, `dateEnvoi`, `idSender`) VALUES (?, ?, ?, ?)";
        int userId= currentUser.getId();
        try {
            PreparedStatement ps = cnx.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, tiltle);
            ps.setString(2, description);
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(4, userId);
            ps.executeUpdate();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                return "Reclamation submitted successfully with ID: " + generatedKeys.getInt(1);
            }
            return "Reclamation submitted successfully!";
        } catch (SQLException e) {
            throw new RuntimeException("Error submitting reclamation", e);
        }
    }

    public Reclamation getReclamationById(int id) {
        String req = "SELECT * FROM `reclamation` WHERE `id` = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ResultSet res = ps.executeQuery();
            if (res.next()) {
                Reclamation r = new Reclamation();
                r.setId(res.getInt("id"));
                r.setTitle(res.getString("title"));
                r.setContenu(res.getString("description"));
                r.setDateEnvoi(res.getTimestamp("date_envoi").toLocalDateTime());
                r.setIdSender(res.getInt("idSender"));
                return r;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving reclamation by ID", e);
        }
    }
}
