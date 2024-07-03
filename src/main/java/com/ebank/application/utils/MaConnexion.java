package com.ebank.application.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MaConnexion {
    // DB
    final String URL = "jdbc:mysql://127.0.0.1:3306/ebank2";
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
            System.out.println("nooooop");

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
