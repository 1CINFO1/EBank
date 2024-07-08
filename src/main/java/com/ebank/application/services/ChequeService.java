
package com.ebank.application.services;

import com.ebank.application.utils.MaConnexion;
import com.ebank.application.interfaces.InterfaceCRUD;
import  com.ebank.application.models.Cheque;
import  com.ebank.application.utils.MaConnexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChequeService implements InterfaceCRUD<Cheque> {
    private Connection cnx;

    public ChequeService() {
        this.cnx = MaConnexion.getInstance().getCnx();
    }

    @Override
    public String add (Cheque c) {
        String req = "INSERT INTO `Cheque`( `dateEmission`, `titulaire`) VALUES (?,?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setDate(2, new java.sql.Date(c.getDateEmission().getTime()));
            ps.setString(3, c.getTitulaire());
            ps.executeUpdate();
            System.out.println("Cheque ajouté avec succès (prep)!");
            return("saif rabii yehdik");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) {
        String req = "DELETE FROM `Cheque` WHERE `id`=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            int rowsDeleted = ps.executeUpdate();
            System.out.println("Cheque supprimé avec succès! Rows affected: " + rowsDeleted);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Cheque c , int id) {
        String req = "UPDATE `Cheque` SET  `dateEmission`=?, `titulaire`=? WHERE `id`=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setDate(1, new java.sql.Date(c.getDateEmission().getTime()));
            ps.setString(2, c.getTitulaire());
            ps.setInt(3, c.getId());
            ps.executeUpdate();
            System.out.println("Cheque mis à jour avec succès!");
        } catch (SQLException e) {
            throw new RuntimeException("Error while updating the cheque",e);
        }
    }

    @Override
    public List<Cheque> getAll() {
        List<Cheque> cheques = new ArrayList<>();
        String req = "SELECT * FROM `Cheque`";
        try {
            Statement st = cnx.createStatement();
            ResultSet res = st.executeQuery(req);
            while (res.next()) {
                Cheque c = new Cheque();
                c.setId(res.getInt("id"));
                c.setDateEmission(res.getDate("Date"));
                c.setTitulaire(res.getString("titulaire"));
                cheques.add(c);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cheques;
    }
}