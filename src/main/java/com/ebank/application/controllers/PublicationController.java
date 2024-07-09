package com.ebank.application.controllers;

import com.ebank.application.models.Publication;
import com.ebank.application.services.ICharityService;
import com.ebank.application.services.IpublicationImple;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.sql.Date;

public class PublicationController {
    @FXML
    private Pane publicationListPane;

    @FXML
    private TableView<?> publicationTableView;

    @FXML
    private TableColumn<?, ?> column1;

    @FXML
    private TableColumn<?, ?> column2;

    @FXML
    private TableColumn<?, ?> descriptionColumn;

    @FXML
    private TableColumn<?, ?> pictureColumn;

    @FXML
    private TableColumn<?, ?> publicationDateColumn;

    @FXML
    private TableColumn<?, ?> column4;

    @FXML
    private Label headerLabel;

    @FXML
    private Button createPublicationButton;

    @FXML
    private TextField publicationTitleField;

    @FXML
    private TextField campaignNameField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private DatePicker publicationDatePicker;

    @FXML
    private Button confirmPublicationButton;



    private ICharityService iCharityService = new ICharityService();
    private final IpublicationImple ipublicationImple = new IpublicationImple();

    @FXML
    private void showPublicationForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ebank/application/views/addPublication.fxml"));
            Pane addPublicationPane = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Add Publication");
            stage.setScene(new Scene(addPublicationPane));
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
