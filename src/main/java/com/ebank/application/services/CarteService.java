
package com.ebank.application.services;

import com.ebank.application.interfaces.InterfaceCRUD;
import com.ebank.application.models.CarteBancaire;
import com.ebank.application.utils.MaConnexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarteService implements InterfaceCRUD<CarteBancaire> {
    private Connection cnx;

    public CarteService() {
        this.cnx = MaConnexion.getInstance().getCnx();
    }

    @Override
    public String add(CarteBancaire c) {
        String req = "INSERT INTO `CarteBancaire`(`numero`, `dateExpiration`, `titulaire`, `type`, `status`) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, c.getNumero());
            ps.setDate(2, new java.sql.Date(c.getDateExpiration().getTime()));
            ps.setString(3, c.getTitulaire());
            ps.setString(4, c.getType());
            ps.setString(5, c.getStatus());

            ps.executeUpdate();
            System.out.println("CarteBancaire ajoutée avec succès (prep)!");
            return ("la carte est bien ajoutee");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) {
        String req = "DELETE FROM `CarteBancaire` WHERE `id`=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            int rowsDeleted = ps.executeUpdate();
            System.out.println("carte supprimé avec succès! Rows affected: " + rowsDeleted);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(CarteBancaire c, int id) {
        String req = "UPDATE `CarteBancaire` SET `numero` = ?, `dateExpiration` = ?, `titulaire` = ?, `type` = ?, `status` = ? WHERE `id` = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, c.getNumero());
            ps.setDate(2, new java.sql.Date(c.getDateExpiration().getTime()));
            ps.setString(3, c.getTitulaire());
            ps.setString(4, c.getType());
            ps.setString(5, c.getStatus());
            ps.setInt(6, id);
            ps.executeUpdate();
            System.out.println("carte updated successfully!");
        } catch (SQLException e) {
            throw new RuntimeException("Error while updating the card", e);
        }
    }

    @Override
    public List<CarteBancaire> getAll() {
        List<CarteBancaire> cartes = new ArrayList<>();
        String req = "SELECT * FROM `CarteBancaire`";
        try (
                Statement st = cnx.createStatement();
                ResultSet res = st.executeQuery(req)) {
            while (res.next()) {
                CarteBancaire cb = new CarteBancaire();
                cb.setId(res.getInt("id"));
                cb.setNumero(res.getString("numero"));
                cb.setDateExpiration(res.getDate("dateExpiration"));
                cb.setTitulaire(res.getString("titulaire"));
                cb.setType(res.getString("type"));
                cb.setStatus(res.getString("status"));
                cartes.add(cb);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cartes;
    }

}