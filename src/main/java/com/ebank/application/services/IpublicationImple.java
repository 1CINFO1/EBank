package com.ebank.application.services;



import com.ebank.application.interfaces.InterfaceCRUD;
import com.ebank.application.models.Publication;
import com.ebank.application.utils.MaConnexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class IpublicationImple implements InterfaceCRUD<Publication> {
    Connection cnx = MaConnexion.getInstance().getCnx();


    @Override
    public String add(Publication publication) {
        String query = "INSERT INTO publication (CompagnieDeDon_Patente,title, CampaignName, description, picture, publicationDate) VALUES (?,?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, publication.getCompagnieDeDon_Patente());
            stmt.setString(2, publication.getTitle());
            stmt.setString(3, publication.getCampaignName());
            stmt.setString(4, publication.getDescription());
            stmt.setString(5, publication.getPicture());
            stmt.setDate(6, new java.sql.Date(publication.getPublicationDate().getTime()));

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                return "Publication added successfully";
            } else {
                return "Failed to add publication";
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately in your application
            return "Error adding publication: " + e.getMessage();
        }
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public void update(Publication publication, int id) {

    }

    @Override
    public List<Publication> getAll() {
        List<Publication> publications = new ArrayList<Publication>();

        String sql = "select * from publication";
        try {
            Statement st = cnx.createStatement();
            ResultSet rs=st.executeQuery(sql);
            while(rs.next()){
                Publication publication = new Publication();

                publication.setId(rs.getInt("id"));
                publication.setTitle(rs.getString("title"));
                publication.setCampaignName(rs.getString("campaignName"));
                publication.setDescription(rs.getString("description"));
                publication.setPublicationDate(rs.getDate("publicationDate"));
                publication.setPicture(rs.getString("picture"));
                publications.add(publication);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return publications;
    }


    public Publication getById(int id) {
        Publication publication = null;
        String sql = "SELECT * FROM publication WHERE id = ?";

        try {
            PreparedStatement pst = cnx.prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                publication = new Publication();
                publication.setId(rs.getInt("id"));
                publication.setTitle(rs.getString("title"));
                publication.setCampaignName(rs.getString("campaignName"));
                publication.setDescription(rs.getString("description"));
                publication.setPublicationDate(rs.getDate("publicationDate"));
                publication.setPicture(rs.getString("picture"));
                publication.setCompagnieDeDon_Patente(rs.getInt("compagnieDeDon_Patente"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return publication;
    }




}
