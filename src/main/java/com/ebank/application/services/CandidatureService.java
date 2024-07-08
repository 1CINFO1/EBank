package com.ebank.application.services;

import com.ebank.application.interfaces.InterfaceCRUD;
import com.ebank.application.models.Candidature;
import com.ebank.application.models.CharityCampaignModel;
import com.ebank.application.models.Publication;
import com.ebank.application.utils.MaConnexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CandidatureService implements InterfaceCRUD<Candidature> {
    // Connection instance
    Connection cnx = MaConnexion.getInstance().getCnx();

    @Override
    public String add(Candidature c) {
        String req = "INSERT INTO `Candidature`(`job_id`,`nom`, `prenom`, `cvPath`) VALUES (?,?, ?, ?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, c.getJob_id());
            ps.setString(2, c.getNom());
            ps.setString(3, c.getPrenom());
            ps.setString(4, c.getCvPath());
            ps.executeUpdate();
            System.out.println("Candidature ajoutée avec succès!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return req;
    }

    @Override
    public void delete(int id) {
        String req = "DELETE FROM `Candidature` WHERE `id` = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Candidature supprimée avec succès!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Candidature c, int id) {
        String req = "UPDATE `Candidature` SET `job_id` = ?, `nom` = ?, `prenom` = ?, `cvPath` = ? WHERE `id` = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, c.getJob_id());
            ps.setString(2, c.getNom());
            ps.setString(3, c.getPrenom());
            ps.setString(4, c.getCvPath());
            ps.setInt(5, id);
            ps.executeUpdate();
            System.out.println("Candidature mise à jour avec succès!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Candidature> getAll() {
        List<Candidature> candidatures = new ArrayList<>();
        String req = "SELECT `id`, `job_id`, `nom`, `prenom`, `cvPath` FROM `Candidature`";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int job_id = rs.getInt("job_id");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String cvPath = rs.getString("cvPath");
                Candidature c = new Candidature(id, job_id, nom, prenom, cvPath);
                candidatures.add(c);
            }
            System.out.println("Liste des candidatures récupérée avec succès!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return candidatures;
    }

    public List<Candidature> getCandidatesForJobId(int jobId) {
        List<Candidature> candidates = new ArrayList<>();
        String req = "SELECT `id`, `job_id`, `nom`, `prenom`, `cvPath` FROM `Candidature` WHERE `job_id` = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, jobId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int job_id = rs.getInt("job_id");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String cvPath = rs.getString("cvPath");
                Candidature candidature = new Candidature(id, job_id, nom, prenom, cvPath);
                candidates.add(candidature);
            }
            System.out.println("Liste des candidats pour l'offre d'emploi " + jobId + " récupérée avec succès!");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des candidats pour l'offre d'emploi " + jobId,
                    e);
        }
        return candidates;
    }

    // Method to upload CV (PDF) to the server
    public void uploadCv(Candidature candidature, String filePath) {
        // Implement the logic to upload the CV to the server and set the cvPath
        candidature.setCvPath(filePath);
        update(candidature, candidature.getId());
    }

    // Method to download CV (PDF) from the server
    public void downloadCv(String cvPath, String destinationPath) {
        // Implement the logic to download the CV from the server to the specified
        // destination path
    }

}
