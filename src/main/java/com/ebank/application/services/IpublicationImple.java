package com.ebank.application.services;

import com.ebank.application.interfaces.InterfaceCRUD;
import com.ebank.application.models.CharityCampaignModel;
import com.ebank.application.models.Publication;
import com.ebank.application.utils.MaConnexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IpublicationImple implements InterfaceCRUD<Publication> {
    Connection cnx = MaConnexion.getInstance().getCnx();

    @Override
    public String add(Publication publication) {
        return "";
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
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
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
