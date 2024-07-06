package com.ebank.application.services;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.ebank.application.interfaces.InterfaceCRUD;
import com.ebank.application.models.Discution;
import com.ebank.application.utils.MaConnexion;

public class DiscutionService implements InterfaceCRUD<Discution> {
    // Connection instance
    Connection cnx = MaConnexion.getInstance().getCnx();

    @Override
    public String add(Discution d) {
        String req = "INSERT INTO `discution`(`nom`, `date_creation`) VALUES (?, ?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, d.getNom());
            ps.setTimestamp(2, Timestamp.valueOf(d.getDateCreation()));
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Message ajouté avec succès!");

                return "Publication added successfully  ";
            } else {
                return "Failed to add publication.";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int d) {
        String req = "DELETE FROM `discution` WHERE `id` = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, d);
            ps.executeUpdate();
            System.out.println("Discution supprimée avec succès!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Discution d, int id) {
        String req = "UPDATE `discution` SET `nom` = ?, `date_creation` = ? WHERE `id` = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, d.getNom());
            ps.setTimestamp(2, Timestamp.valueOf(d.getDateCreation()));
            ps.setInt(3, id);
            ps.executeUpdate();
            System.out.println("Discution mise à jour avec succès!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Discution> getAll() {
        List<Discution> discussions = new ArrayList<>();
        String req = "SELECT * FROM `discution`";
        try {
            Statement st = cnx.createStatement();
            ResultSet res = st.executeQuery(req);
            while (res.next()) {
                Discution d = new Discution();
                d.setId(res.getInt("id"));
                d.setNom(res.getString("nom"));
                d.setDateCreation(res.getTimestamp("date_creation").toLocalDateTime());

                discussions.add(d);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return discussions;
    }

    @Override
    public List<Discution> getByid(int i) {
        return List.of();
    }
}
