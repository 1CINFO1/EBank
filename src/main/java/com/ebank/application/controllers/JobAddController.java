package com.ebank.application.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ebank.application.models.OffreEmploi;

public class JobAddController {

    @FXML
    private TextField posteField;

    @FXML
    private TextField sujetField;

    @FXML
    private TextField typeField;

    @FXML
    private TextField emplacementField;

    private Stage dialogStage;
    private JobListControllerAdmin jobListController;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setJobListController(JobListControllerAdmin jobListController) {
        this.jobListController = jobListController;
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
