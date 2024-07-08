package com.ebank.application.controllers;

import com.ebank.application.models.Candidature;
import com.ebank.application.models.OffreEmploi;
import com.ebank.application.services.CandidatureService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;

public class ApplyForJobController {

    @FXML
    private TextField jobIdField;

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField cvField;

    private CandidatureService candidatureService;

    private OffreEmploi selectedOffre;

    public ApplyForJobController() {
        this.candidatureService = new CandidatureService();
    }

    public void initData(OffreEmploi selectedOffre) {
        this.selectedOffre = selectedOffre;
        jobIdField.setText(String.valueOf(selectedOffre.getId()));
        // Disable the jobIdField if you want to prevent editing
        jobIdField.setDisable(true);
    }

    @FXML
    private void submitApplication() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String cv = cvField.getText();

        Candidature candidature = new Candidature(selectedOffre.getId(), nom, prenom, cv);
        candidatureService.add(candidature);
    }

    @FXML
    private void importCV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            cvField.setText(selectedFile.getAbsolutePath());
        }
    }
    @FXML
private void browseCV() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
    );
    File selectedFile = fileChooser.showOpenDialog(null);
    if (selectedFile != null) {
        cvField.setText(selectedFile.getAbsolutePath());
    }
}

}
