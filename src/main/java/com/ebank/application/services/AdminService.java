package com.ebank.application.services;

import com.ebank.application.interfaces.InterfaceCRUD;
import com.ebank.application.models.Cheque;
import com.ebank.application.models.Publication;
import com.ebank.application.utils.MaConnexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class AdminService implements InterfaceCRUD<Publication> {

    Connection cnx = MaConnexion.getInstance().getCnx();

    @Override
    public String add(Publication publication) {
        return "";
    }

    @Override
    public void delete(int t) {

    }

    @Override
    public void update(Publication publication, int id) {

    }

    @Override
    public List<Publication> getAll() {
        return List.of();
    }







    public List<Cheque> getAllCheques() {
        List<Cheque> cheques = new ArrayList<>();

        String sql = "SELECT * FROM cheque WHERE status = 'in progress'";
        try (PreparedStatement stmt = cnx.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                Date dateEmission = rs.getDate("dateEmission");
                String titulaire = rs.getString("titulaire");
                int numberOfPapers = rs.getInt("numberOfPapers");
                int userId = rs.getInt("user_id");
                String status = rs.getString("status");

                Cheque cheque = new Cheque( id,  dateEmission,  titulaire,  numberOfPapers,  userId);
                System.out.println("Fetched Publication: " + cheque);
                cheques.add(cheque);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving pending publications", e);
        }

        return cheques;
    }


    public void approveCheque(int chequeId) {
        String sql = "UPDATE cheque SET status = 'READY' WHERE ID = ?";
        try {
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setInt(1, chequeId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }





    public Map<String, Integer> getPublicationCountPerCampaign() {
        Map<String, Integer> publicationCounts = new HashMap<>();
        String sql = "SELECT campaignName, COUNT(*) AS count FROM publication GROUP BY campaignName";
        try (PreparedStatement stmt = cnx.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String campaignName = rs.getString("campaignName");
                int count = rs.getInt("count");
                publicationCounts.put(campaignName, count);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving publication counts per campaign", e);
        }

        return publicationCounts;
    }
    public Map<String, Integer> getChequeCountPerUser() {
        Map<String, Integer> chequeCounts = new HashMap<>();
        String sql = "SELECT titulaire, COUNT(*) AS count FROM cheque GROUP BY titulaire";
        try (PreparedStatement stmt = cnx.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String titulaire = rs.getString("titulaire");
                int count = rs.getInt("count");
                chequeCounts.put(titulaire, count);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving cheque counts per user", e);
        }

        return chequeCounts;
    }




}