package com.ebank.application.services;

import com.ebank.application.interfaces.InterfaceCRUD;
import com.ebank.application.models.CharityCampaignModel;
import com.ebank.application.models.Publication;
import com.ebank.application.utils.MaConnexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    @Override
    public Publication getById(int id) {
        return null;
    }

    @Override
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
