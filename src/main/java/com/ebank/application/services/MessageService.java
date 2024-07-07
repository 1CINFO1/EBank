package com.ebank.application.services;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.ebank.application.interfaces.InterfaceCRUD;
import com.ebank.application.models.CharityCampaignModel;
import com.ebank.application.models.Message;
import com.ebank.application.models.Publication;
import com.ebank.application.utils.MaConnexion;

public class MessageService implements InterfaceCRUD<Message> {
    // Connection instance
    Connection cnx = MaConnexion.getInstance().getCnx();

    @Override
    public String add(Message m) {
        System.out.println("-------------------msg service" + m.toString() + "--------------------");
        String req = "INSERT INTO `message`(`contenu`, `date_envoi`, `id_discution`, `id_emetteur`, `id_recepteur`) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, m.getContenu());
            ps.setTimestamp(2, Timestamp.valueOf(m.getDateEnvoi()));
            ps.setInt(3, m.getIdDiscution());
            ps.setInt(4, m.getIdEmetteur());
            ps.setInt(5, m.getIdRecepteur());
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Message ajouté avec succès!");

                return "added";
            } else {
                return "failed";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int m) {
        String req = "DELETE FROM `message` WHERE `id` = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, m);
            ps.executeUpdate();
            System.out.println("Message supprimé avec succès!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Message m, int id) {
        String req = "UPDATE `message` SET `contenu` = ?, `date_envoi` = ?, `id_discution` = ?, `id_emetteur` = ?, `id_recepteur` = ? WHERE `id` = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, m.getContenu());
            ps.setTimestamp(2, Timestamp.valueOf(m.getDateEnvoi()));
            ps.setInt(3, m.getIdDiscution());
            ps.setInt(4, m.getIdEmetteur());
            ps.setInt(5, m.getIdRecepteur());
            ps.setInt(6, id);
            ps.executeUpdate();
            System.out.println("Message mis à jour avec succès!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Message> getAll() {
        List<Message> messages = new ArrayList<>();
        String req = "SELECT * FROM `message`";
        try {
            Statement st = cnx.createStatement();
            ResultSet res = st.executeQuery(req);
            while (res.next()) {
                Message m = new Message();
                m.setId(res.getInt("id"));
                m.setContenu(res.getString("contenu"));
                m.setDateEnvoi(res.getTimestamp("date_envoi").toLocalDateTime());
                m.setIdDiscution(res.getInt("id_discution"));
                m.setIdEmetteur(res.getInt("id_emetteur"));
                m.setIdRecepteur(res.getInt("id_recepteur"));

                messages.add(m);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return messages;
    }







}
