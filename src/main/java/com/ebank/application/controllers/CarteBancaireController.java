// CarteBancaireController.java
package controller;

import database.DatabaseConnection;
import com.mainbank.bank.models.CarteBancaire;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CarteBancaireController {

    public void ajouterCarteBancaire(CarteBancaire carteBancaire) throws SQLException {
        String sql = "INSERT INTO cartes_bancaires (numero, dateExpiration, titulaire, type) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, carteBancaire.getNumero());
            pstmt.setDate(2, new java.sql.Date(carteBancaire.getDateExpiration().getTime()));
            pstmt.setString(3, carteBancaire.getTitulaire());
            pstmt.setString(4, carteBancaire.getType());
            pstmt.executeUpdate();
        }
    }

    // Méthodes pour update, delete, et get peuvent être ajoutées ici.
}
