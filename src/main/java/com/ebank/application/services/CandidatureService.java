package com.ebank.application.services;

import com.ebank.application.interfaces.InterfaceCRUD;
import com.ebank.application.models.Candidature;
import com.ebank.application.models.Publication;
import com.ebank.application.utils.MaConnexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CandidatureService implements InterfaceCRUD<Candidature> {
    // Connection instance
    Connection cnx = MaConnexion.getInstance().getCnx();

    @Override
    public String add(Candidature c) {
        String req = "INSERT INTO `Candidature`(`job_id`,`nom`, `prenom`, `CV`) VALUES (?,?, ?, ?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, c.getJob_id());
            ps.setString(2, c.getNom());
            ps.setString(3, c.getPrenom());
            ps.setString(4, c.getCV());
            ps.executeUpdate();
            System.out.println("Candidature ajoutée avec succès!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return req;
    }

    @Override
    public void delete(int c) {
        String req = "DELETE FROM `Candidature` WHERE `id` = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, c);
            ps.executeUpdate();
            System.out.println("Candidature supprimée avec succès!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Candidature c, int i) {
        String req = "UPDATE `Candidature` SET `job_id` = ?, `nom` = ?, `prenom` = ?, `CV` = ? WHERE `id` = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, c.getJob_id());
            ps.setString(2, c.getNom());
            ps.setString(3, c.getPrenom());
            ps.setString(4, c.getCV());
            ps.setInt(5, i);
            ps.executeUpdate();
            System.out.println("Candidature mise à jour avec succès!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    

    @Override
    public List<Candidature> getAll() {
        List<Candidature> candidatures = new ArrayList<>();
        String req = "SELECT `job_id`, `nom`, `prenom`, `CV` FROM `Candidature`";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int job_id = rs.getInt("job_id");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String CV = rs.getString("CV");
                Candidature c = new Candidature(job_id, nom, prenom, CV);
                candidatures.add(c);
            }
            System.out.println("Liste des candidatures récupérée avec succès!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return candidatures;
    }
}