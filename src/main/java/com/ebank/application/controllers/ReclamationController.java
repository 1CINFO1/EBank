package com.ebank.application.controllers;

import com.ebank.application.models.Reclamation;
import com.ebank.application.services.ReclamationService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import java.time.LocalDateTime;

public class ReclamationController {

    @FXML private TextField contenuField;
    @FXML private TextField idDiscutionField;
    @FXML private TextField idEmetteurField;
    @FXML private TextField idRecepteurField;
    @FXML private TextField idTransactionField;
    @FXML private ComboBox<String> stateComboBox;
    @FXML private Button submitButton;
    @FXML private Button cancelButton;

    private ReclamationService reclamationService;

    public ReclamationController() {
        reclamationService = new ReclamationService();
    }

    @FXML
    private void initialize() {
        stateComboBox.getItems().addAll("New", "In Progress", "Resolved");
    }

    @FXML
    private void handleSubmit(ActionEvent event) {
        try {
            Reclamation reclamation = new Reclamation(
                0, contenuField.getText(),
                LocalDateTime.now(),
                Integer.parseInt(idEmetteurField.getText()), 0
            );
            reclamation.setIdTrans(Integer.parseInt(idTransactionField.getText()));

            String result = reclamationService.add(reclamation);
            showAlert(Alert.AlertType.INFORMATION, "Success", result);
            clearFields();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter valid numbers for IDs.");
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", e.getMessage());
        } catch (RuntimeException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while adding the reclamation.");
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        contenuField.clear();
        idDiscutionField.clear();
        idEmetteurField.clear();
        idRecepteurField.clear();
        idTransactionField.clear();
        stateComboBox.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}