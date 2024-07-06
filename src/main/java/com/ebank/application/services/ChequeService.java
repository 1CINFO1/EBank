
        package com.mainbank.bank.services;

import com.mainbank.bank.interfaces.InterfaceCRUD;
import com.mainbank.bank.models.Cheque;
import com.mainbank.bank.utils.maConnexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChequeService implements InterfaceCRUD<Cheque> {
    Connection cnx = maConnexion.getInstance().getCnx();



    @Override
    public void add(Cheque cheque) {
        String req = "INSERT INTO `Cheque`(`montant`, `dateEmission`, `titulaire`, `banque`) VALUES (?,?,?,?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setFloat(1, cheque.getMontant());
            ps.setDate(2, new java.sql.Date(cheque.getDateEmission().getTime()));
            ps.setString(3, cheque.getTitulaire());
            ps.setString(4, cheque.getBanque());
            ps.executeUpdate();
            System.out.println("Cheque ajouté avec succès (prep)!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Cheque cheque) {
        String req = "DELETE FROM `Cheque` WHERE `id`=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, cheque.getId());
            ps.executeUpdate();
            System.out.println("Cheque supprimé avec succès!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Cheque cheque) {
        String req = "UPDATE `Cheque` SET `montant`=?, `dateEmission`=?, `titulaire`=?, `banque`=? WHERE `id`=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setFloat(1, cheque.getMontant());
            ps.setDate(2, new java.sql.Date(cheque.getDateEmission().getTime()));
            ps.setString(3, cheque.getTitulaire());
            ps.setString(4, cheque.getBanque());
            ps.setInt(5, cheque.getId());
            ps.executeUpdate();
            System.out.println("Cheque mis à jour avec succès!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
                Cheque c = new Cheque(
                        res.getInt("id"),
                        res.getFloat("montant"),
                        res.getDate("dateEmission"),
                        res.getString("titulaire"),
                        res.getString("banque")
                );
                cheques.add(c);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cheques;
    }
}