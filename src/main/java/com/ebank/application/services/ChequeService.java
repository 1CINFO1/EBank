
package com.ebank.application.services;

import com.ebank.application.utils.MaConnexion;
import com.ebank.application.interfaces.InterfaceCRUD;
import com.ebank.application.models.Cheque;
import com.ebank.application.utils.MaConnexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChequeService implements InterfaceCRUD<Cheque> {
    private Connection cnx;

    public ChequeService() {
        this.cnx = MaConnexion.getInstance().getCnx();
    }

    public String add(String titulaire, int id, int numberOfPages) {

        System.out.println(id);
        System.out.println(titulaire);

        String req = "INSERT INTO `cheque`( `dateEmission`,`titulaire`,`user_id`,`numberOfPapers`) VALUES (?,?,?,?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            ps.setString(2, titulaire);
            ps.setInt(3, id);
            ps.setInt(4, numberOfPages);
            ps.executeUpdate();

            return ("all set");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String add(Cheque cheque) {
        return "";
    }

    @Override
    public void delete(int id) {
        String req = "DELETE FROM `cheque` WHERE `id`=?";
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
    public void update(Cheque c, int id) {
        String req = "UPDATE `cheque` SET  `dateEmission`=?, `titulaire`=? WHERE `id`=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setDate(1, new java.sql.Date(c.getDateEmission().getTime()));
            ps.setString(2, c.getTitulaire());
            ps.setInt(3, c.getId());
            ps.executeUpdate();
            System.out.println("Cheque mis à jour avec succès!");
        } catch (SQLException e) {
            throw new RuntimeException("Error while updating the cheque", e);
        }
    }

    @Override
    public List<Cheque> getAll() {
        return List.of();
    }

    public List<Cheque> getChequesByUserId(int user_Id) {
        List<Cheque> cheques = new ArrayList<>();

        String sql = "SELECT * FROM cheque WHERE `user_id` = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, user_Id); // Set the user_Id parameter
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    java.util.Date dateEmission = rs.getDate("dateEmission");
                    String titulaire = rs.getString("titulaire");
                    int numberOfPapers = rs.getInt("numberOfPapers"); // Check if this matches your database column name
                    int userId = rs.getInt("user_id");
                    String status = rs.getString("status");

                    // Assuming Cheque constructor parameters are in the correct order
                    Cheque cheque = new Cheque(id, dateEmission, titulaire, numberOfPapers, userId);
                    System.out.println("Fetched Cheque: " + cheque);
                    cheques.add(cheque);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving cheques by user ID", e);
        }

        return cheques;
    }

}