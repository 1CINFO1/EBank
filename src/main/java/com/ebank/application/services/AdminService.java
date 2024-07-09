package com.ebank.application.services;

import com.ebank.application.interfaces.InterfaceCRUD;
import com.ebank.application.models.Publication;
import com.ebank.application.utils.MaConnexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<Publication> publications = new ArrayList<>();

        String sql = "SELECT * FROM publication WHERE status = 'pending'";
        try (PreparedStatement stmt = cnx.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String campaignName = rs.getString("campaignName");
                String status = rs.getString("status");

                Publication publication = new Publication(id, title, description, campaignName, status);
                System.out.println("Fetched Publication: " + publication);
                publications.add(publication);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving pending publications", e);
        }

        return publications;
    }

    public void approvePublication(int publicationId) {
        String sql = "UPDATE publication SET status = 'approved' WHERE ID = ?";
        try {
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setInt(1, publicationId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void declinePublication(int publicationId) {
        String sql = "UPDATE publication SET status = 'declined' WHERE ID = ?";
        try {
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setInt(1, publicationId);
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




}
