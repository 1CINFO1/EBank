package com.ebank.application.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import com.ebank.application.models.Candidature;
import com.ebank.application.services.CandidatureService;

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
        try {
            List<Candidature> allCandidats = candidatureService.getCandidatesForJobId(jobId); // Assuming you have a method like this in CandidatureService
            candidats.setAll(allCandidats);
            condidatListView.setItems(candidats);
        } catch (Exception e) {
            e.printStackTrace(); // Handle exception appropriately
        }
    }

    @FXML
    public void initialize() {
        // Optional: You can initialize anything else needed when the controller is loaded
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
}
