package com.ebank.application.services;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.ebank.application.interfaces.IDiscution;
import com.ebank.application.models.Discution;
import com.ebank.application.models.Publication;
import com.ebank.application.utils.MaConnexion;

public class DiscutionService implements IDiscution<Discution> {
    // Connection instance
    Connection cnx = MaConnexion.getInstance().getCnx();

    @Override
    public int add(Discution d) {
        String req = "INSERT INTO `discution`(`nom`, `date_creation`) VALUES (?, ?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, d.getNom());
            ps.setTimestamp(2, Timestamp.valueOf(d.getDateCreation()));
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1; // Indicate failure to generate ID
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

    public Discution getDiscussionBetweenUsers(int userId1, int userId2) {
        String sql = "SELECT d.* FROM discution d " +
                "JOIN discution_users du1 ON d.id = du1.discution_id " +
                "JOIN discution_users du2 ON d.id = du2.discution_id " +
                "WHERE (du1.user_id = ? AND du2.user_id = ?) OR (du1.user_id = ? AND du2.user_id = ?) " +
                "LIMIT 1";

        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, userId1);
            ps.setInt(2, userId2);
            ps.setInt(3, userId2);
            ps.setInt(4, userId1);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Discution d = new Discution();
                    d.setId(rs.getInt("id"));
                    d.setNom(rs.getString("nom"));
                    d.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());
                    return d;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding discussion between users: " + e.getMessage(), e);
        }

        // If no discussion found, return null instead of creating a new one
        return null;
    }

    public void linkUsersToDiscussion(int discussionId, int userId1, int userId2) throws SQLException {
        System.err.println("discussionId=>> " + discussionId + "userId1=>> " + userId1 + "userId2=>> " + userId2);
        // Check if discussionId exists in discution table
        String checkQuery = "SELECT COUNT(*) FROM discution WHERE id = ?";
        try (PreparedStatement checkStmt = cnx.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, discussionId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                // If exists, proceed with linking users to the discussion
                String sql = "INSERT INTO discution_users (discution_id, user_id) VALUES (?, ?)";
                try (PreparedStatement ps = cnx.prepareStatement(sql)) {
                    ps.setInt(1, discussionId);
                    ps.setInt(2, userId1);
                    ps.executeUpdate();

                    ps.setInt(2, userId2);
                    ps.executeUpdate();
                }
            } else {
                throw new SQLException("Discussion ID does not exist in discution table");
            }
        }
    }

}
