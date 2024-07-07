package com.ebank.application.services;

import com.ebank.application.interfaces.InterfaceCRUD;
import com.ebank.application.models.OffreEmploi;
import com.ebank.application.utils.MaConnexion;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class OffreEmploiService implements InterfaceCRUD<OffreEmploi> {

    // Assuming MaConnexion manages a single connection throughout application lifecycle
    private Connection cnx = MaConnexion.getInstance().getCnx();

    @Override
    public String add(OffreEmploi o) {
        String req = "INSERT INTO `offreemploi`(`poste`, `sujet`, `type`, `emplacement`, `date_expiration`, `date_offre`) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, o.getPoste());
            ps.setString(2, o.getSujet());
            ps.setString(3, o.getType());
            ps.setString(4, o.getEmplacement());
            ps.setDate(5, Date.valueOf(o.getDate_expiration()));
            ps.setTimestamp(6, Timestamp.valueOf(o.getDate_offre()));

            ps.executeUpdate();

            // Retrieve the generated id
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);  // Assuming id is an INT
                o.setId(id);  // Set the generated id back to your object if needed
            }

            System.out.println("Offre d'emploi ajoutée avec succès!");
        } catch (SQLException e) {
            throw new RuntimeException("Error adding OffreEmploi", e);
        }
        return req;
    }

    @Override
    public void delete(int id) {
        String req = "DELETE FROM `offreemploi` WHERE `id` = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Failed to delete OffreEmploi with id: " + id);
            }
            System.out.println("OffreEmploi supprimée avec succès!");
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting OffreEmploi", e);
        }
    }

    @Override
    public void update(OffreEmploi o, int id) {
        String req = "UPDATE `offreemploi` SET `poste` = ?, `sujet` = ?, `type` = ?, `emplacement` = ?, `date_expiration` = ?, `date_offre` = ? WHERE `id` = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, o.getPoste());
            ps.setString(2, o.getSujet());
            ps.setString(3, o.getType());
            ps.setString(4, o.getEmplacement());
            ps.setDate(5, Date.valueOf(o.getDate_expiration()));
            ps.setTimestamp(6, Timestamp.valueOf(o.getDate_offre()));
            ps.setInt(7, id);

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Failed to update OffreEmploi with id: " + id);
            }
            System.out.println("Offre d'emploi mise à jour avec succès!");
        } catch (SQLException e) {
            throw new RuntimeException("Error updating OffreEmploi", e);
        }
    }

    @Override
    public List<OffreEmploi> getAll() {
        List<OffreEmploi> offres = new ArrayList<>();
        String req = "SELECT * FROM `offreemploi`";
        try (PreparedStatement ps = cnx.prepareStatement(req);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                OffreEmploi o = new OffreEmploi(
                        rs.getInt("id"),
                        rs.getString("poste"),
                        rs.getString("sujet"),
                        rs.getString("type"),
                        rs.getString("emplacement"),
                        rs.getDate("date_expiration").toLocalDate(),
                        rs.getTimestamp("date_offre").toLocalDateTime()
                );
                offres.add(o);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching OffreEmploi list", e);
        }
        return offres;
    }

    // Method to get the number of candidates for a specific job
    public int getCandidateCountByJobId(int jobId) {
        String req = "SELECT COUNT(*) FROM `candidature` WHERE `job_id` = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, jobId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching candidate count for job id: " + jobId, e);
        }
        return 0;
    }
}
