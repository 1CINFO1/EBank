module com.abcbank.application {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires mysql.connector.j;
    requires com.google.gson;

    opens com.abcbank.application to javafx.fxml;
    exports com.abcbank.application;
    exports com.abcbank.application.Utils;
    opens com.abcbank.application.Utils to javafx.fxml;
    exports com.abcbank.application.Models;
    opens com.abcbank.application.Models to javafx.fxml;
}