package com.ebank.application.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;

public class MaConnexion {
    // DB
    private final String URL;
    private final String USR;
    private final String PWD;

    // var
    Connection cnx;
    static MaConnexion instance;

    // Constructeur
    private MaConnexion() {
        Dotenv dotenv = Dotenv.load();
        URL = dotenv.get("DB_URL");
        USR = dotenv.get("DB_USER");
        PWD = dotenv.get("DB_PASSWORD");

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
