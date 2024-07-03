module com.ebank.application {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires mysql.connector.j;
    requires com.google.gson;

    opens com.ebank.application to javafx.fxml;

    opens com.ebank.application.controllers to javafx.fxml;

    exports com.ebank.application;
    exports com.ebank.application.controllers ;
}