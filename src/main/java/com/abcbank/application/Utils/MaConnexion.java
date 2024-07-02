package com.abcbank.application.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MaConnexion {
    // DB
    final String URL = "jdbc:mysql://localhost:3306/saif12";
    final String USR = "root";
    final String PWD = "admin";

    // var
    Connection cnx;
    static MaConnexion instance;

    // Constructeur
    private MaConnexion() {
        try {
            cnx = DriverManager.getConnection(URL, USR, PWD);
            System.out.println("Connexion etablie avec succes!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getCnx() {
        return cnx;
    }

    public static MaConnexion getInstance() {
        if (instance == null)
            instance = new MaConnexion();
        return instance;
    }
}
