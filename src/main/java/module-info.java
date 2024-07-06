module com.ebank.application {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires mysql.connector.j;
    requires com.google.gson;
    requires java.dotenv;
    requires java.mail;
    requires javafx.graphics;
    requires Java.WebSocket;
    requires org.json;
    requires javafx.base;

    opens com.ebank.application to javafx.fxml;

    opens com.ebank.application.controllers to javafx.fxml;

    exports com.ebank.application;
    exports com.ebank.application.controllers;
    exports com.ebank.application.models;

    opens com.ebank.application.models to javafx.fxml;
}