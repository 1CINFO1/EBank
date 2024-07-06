
package com.mainbank.bank.services;

import com.mainbank.bank.interfaces.InterfaceCRUD;
import com.mainbank.bank.models.CarteBancaire;
import com.mainbank.bank.utils.maConnexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarteBancaireService implements InterfaceCRUD<CarteBancaire> {


    Connection cnx = maConnexion.getInstance().getCnx();

    @Override
    public void add(CarteBancaire carte) {
        String req = "INSERT INTO `CarteBancaire`(`numero`, `dateExpiration`, `titulaire`, `type`) VALUES (?,?,?,?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, carte.getNumero());
            ps.setDate(2, new java.sql.Date(carte.getDateExpiration().getTime()));
            ps.setString(3, carte.getTitulaire());
            ps.setString(4, carte.getType());
            ps.executeUpdate();
            System.out.println("CarteBancaire ajoutée avec succès (prep)!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(CarteBancaire carte) {
        String req = "DELETE FROM `CarteBancaire` WHERE `id`=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, carte.getId());
            ps.executeUpdate();
            System.out.println("CarteBancaire supprimée avec succès!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(CarteBancaire carte) {
        String req = "UPDATE `CarteBancaire` SET `numero`=?, `dateExpiration`=?, `titulaire`=?, `type`=? WHERE `id`=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, carte.getNumero());
            ps.setDate(2, new java.sql.Date(carte.getDateExpiration().getTime()));
            ps.setString(3, carte.getTitulaire());
            ps.setString(4, carte.getType());
            ps.setInt(5, carte.getId());
            ps.executeUpdate();
            System.out.println("CarteBancaire mise à jour avec succès!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CarteBancaire> getAll() {
        List<CarteBancaire> cartes = new ArrayList<>();
        String req = "SELECT * FROM `CarteBancaire`";
        try {
            Statement st = cnx.createStatement();
            ResultSet res = st.executeQuery(req);
            while (res.next()) {
                CarteBancaire c = new CarteBancaire(
                        res.getInt("id"),
                        res.getString("numero"),
                        res.getDate("dateExpiration"),
                        res.getString("titulaire"),
                        res.getString("type")
                );
                cartes.add(c);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cartes;
    }
}