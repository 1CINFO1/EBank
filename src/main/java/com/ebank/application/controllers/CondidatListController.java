package com.ebank.application.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import com.ebank.application.models.Candidature;
import com.ebank.application.services.CandidatureService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class CondidatListController {

    @FXML
    private ListView<Candidature> condidatListView;

    private CandidatureService candidatureService = new CandidatureService();
    private ObservableList<Candidature> candidats = FXCollections.observableArrayList();
    private int jobId; // To store the job ID for which candidates are being displayed

    public void setJobId(int jobId) {
        this.jobId = jobId;
        loadCandidatesForJob(jobId);
    }

    private void loadCandidatesForJob(int jobId) {
        List<Candidature> allCandidats = candidatureService.getCandidatesForJobId(jobId);
        candidats.setAll(allCandidats);
        condidatListView.setItems(candidats);
    }

    @FXML
    public void initialize() {
        // Optional: You can initialize anything else needed when the controller is
        // loaded
    }

    @FXML
    private void refreshCandidateList() {
        loadCandidatesForJob(jobId);
    }

    @FXML
    private void backToJobList() {
        Stage stage = (Stage) condidatListView.getScene().getWindow();
        stage.close(); // Close the candidate list window
    }

    @FXML
    private void downloadCandidateCV(ActionEvent event) {
        Candidature selectedCandidature = condidatListView.getSelectionModel().getSelectedItem();
        if (selectedCandidature != null) {
            String cvPath = selectedCandidature.getCvPath();
            if (cvPath != null) {
                File cvFile = new File(cvPath);
                if (cvFile.exists()) {
                    try {
                        // Open a directory chooser dialog
                        DirectoryChooser directoryChooser = new DirectoryChooser();
                        directoryChooser.setTitle("Select Download Location");
                        File selectedDirectory = directoryChooser.showDialog(condidatListView.getScene().getWindow());

                        if (selectedDirectory != null) {
                            File downloadFile = new File(selectedDirectory, cvFile.getName());
                            Files.copy(cvFile.toPath(), downloadFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            System.out.println("CV downloaded to: " + downloadFile.getAbsolutePath());
                        } else {
                            System.out.println("No directory selected for download.");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("CV file does not exist.");
                }
            } else {
                System.out.println("No CV path found for the selected candidate.");
            }
        }
    }
}
