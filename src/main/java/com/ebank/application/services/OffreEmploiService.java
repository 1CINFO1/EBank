package com.ebank.application.services;

import com.ebank.application.interfaces.InterfaceCRUD;
import com.ebank.application.models.OffreEmploi;
import com.ebank.application.utils.MaConnexion;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class OffreEmploiService implements InterfaceCRUD<OffreEmploi> {
    // var
    Connection cnx = MaConnexion.getInstance().getCnx();

    @Override
    public String add(OffreEmploi o) {
        String req = "INSERT INTO `offreemploi`(`poste`, `sujet`, `type`, `emplacement`, `date_expiration`, `date_offre`) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, o.getPoste());
            ps.setString(2, o.getSujet());
            ps.setString(3, o.getType());
            ps.setString(4, o.getEmplacement());
            ps.setDate(5, Date.valueOf(o.getDate_expiration()));
            ps.setTimestamp(6, Timestamp.valueOf(o.getDate_offre()));
    
            ps.executeUpdate();
    
            // Retrieve the generated id
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);  // Assuming id is an INT
                o.setId(id);  // Set the generated id back to your object if needed
            }
    
            System.out.println("Offre d'emploi ajoutée avec succès!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return req;
    }
    

    @Override
    public void delete(int o) {
        String req = "DELETE FROM `OffreEmploi` WHERE `id` = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, o);
            ps.executeUpdate();
            System.out.println("OffreEmploi supprimée avec succès!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(OffreEmploi o, int i) {
        String req = "UPDATE `offreemploi` SET `poste` = ?, `sujet` = ?, `type` = ?, `emplacement` = ?, `date_expiration` = ?, `date_offre` = ? WHERE `id` = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, o.getPoste());
            ps.setString(2, o.getSujet());
            ps.setString(3, o.getType());
            ps.setString(4, o.getEmplacement());
            ps.setDate(5, Date.valueOf(o.getDate_expiration()));
            ps.setTimestamp(6, Timestamp.valueOf(o.getDate_offre()));
            ps.setInt(7, i);

            ps.executeUpdate();
            System.out.println("Offre d'emploi mise à jour avec succès!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<OffreEmploi> getAll() {
        List<OffreEmploi> offres = new ArrayList<>();
        String req = "SELECT * FROM `offreemploi`";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                OffreEmploi o = new OffreEmploi(
                    rs.getInt("id"),
                    rs.getString("poste"),
                    rs.getString("sujet"),
                    rs.getString("type"),
                    rs.getString("emplacement"),
                    rs.getDate("date_expiration").toLocalDate(),
                    rs.getTimestamp("date_offre").toLocalDateTime()
                );
                offres.add(o);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return offres;
    }
}
