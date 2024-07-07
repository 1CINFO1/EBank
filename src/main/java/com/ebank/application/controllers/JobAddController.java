package com.ebank.application.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.ebank.application.models.OffreEmploi;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class JobAddController {

    @FXML
    private TextField posteField;

    @FXML
    private TextField sujetField;

    @FXML
    private TextField typeField;

    @FXML
    private TextField emplacementField;

    @FXML
    private TextField cvField;

    private Stage dialogStage;
    private JobListControllerAdmin jobListController;

    private File selectedCvFile;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setJobListController(JobListControllerAdmin jobListController) {
        this.jobListController = jobListController;
    }

    @FXML
    private void handleChooseCv() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        Stage stage = (Stage) cvField.getScene().getWindow();
        selectedCvFile = fileChooser.showOpenDialog(stage);
        if (selectedCvFile != null) {
            cvField.setText(selectedCvFile.getAbsolutePath());
        }
    }

    @FXML
    private void handleAddJob() {
        String poste = posteField.getText();
        String sujet = sujetField.getText();
        String type = typeField.getText();
        String emplacement = emplacementField.getText();

        OffreEmploi newOffre = new OffreEmploi(poste, sujet, type, emplacement, LocalDate.now(), LocalDateTime.now());
        jobListController.addJobToList(newOffre); // Add job offer to the list in JobListControllerAdmin
        dialogStage.close();
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
}
