package com.ebank.application.services;

import com.ebank.application.interfaces.InterfaceCRUD;
import com.ebank.application.models.CharityCampaignModel;
import com.ebank.application.models.Publication;
import com.ebank.application.utils.MaConnexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ICharityService implements InterfaceCRUD<CharityCampaignModel> {

    Connection cnx = MaConnexion.getInstance().getCnx();
    @Override
    public String add(CharityCampaignModel charityCampaignModel) {
        return "";
    }

    @Override
    public void delete(int t) {

    }

    @Override
    public void update(CharityCampaignModel charityCampaignModel, int id) {

    }

    @Override
    public List<CharityCampaignModel> getAll() {
        return List.of();
    }




    public List<Publication> getByCharityId(int id) {
        List<Publication> publications = new ArrayList<>();
        String sql = "SELECT * FROM publication WHERE CompagnieDeDon_Patente = ?";

        try {
            PreparedStatement pst = cnx.prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Publication publication = new Publication();
                publication.setId(rs.getInt("ID"));
                publication.setCompagnieDeDon_Patente(rs.getInt("CompagnieDeDon_Patente"));
                publication.setTitle(rs.getString("title"));
                publication.setCampaignName(rs.getString("CampaignName"));
                publication.setDescription(rs.getString("Description"));
                publication.setPicture(rs.getString("picture"));
                publication.setPublicationDate(rs.getDate("publicationDate"));

                publications.add(publication);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return publications;
    }



    public CharityCampaignModel getCharityBy(int id) {
        CharityCampaignModel charityCampaign = null;
        String sql = "SELECT * FROM charitycampaignmodel WHERE compagnieDeDon_Patente = ?";

        try {
            PreparedStatement pst = cnx.prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                charityCampaign = new CharityCampaignModel();
                charityCampaign.setAcc_num(rs.getInt("acc_num"));
                charityCampaign.setBalance(rs.getDouble("balance"));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return charityCampaign;
    }
}
