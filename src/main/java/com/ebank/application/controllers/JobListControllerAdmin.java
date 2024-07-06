package com.ebank.application.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

import com.ebank.application.models.OffreEmploi;
import com.ebank.application.services.OffreEmploiService;

public class JobListControllerAdmin {

    @FXML
    private ListView<OffreEmploi> jobListView;

    private OffreEmploiService offreEmploiService = new OffreEmploiService();

    private ObservableList<OffreEmploi> offres = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadJobOffers();
        jobListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void loadJobOffers() {
        List<OffreEmploi> allOffres = offreEmploiService.getAll();
        offres.setAll(allOffres);
        jobListView.setItems(offres);
    }

    @FXML
    private void refreshJobList(ActionEvent event) {
        loadJobOffers();
    }

    @FXML
    private void addJob(ActionEvent event) {
        showAddJobDialog();
    }

    @FXML
    private void updateJob(ActionEvent event) {
        OffreEmploi selectedOffre = jobListView.getSelectionModel().getSelectedItem();
        if (selectedOffre != null) {
            showUpdateJobDialog(selectedOffre);
        }
    }

    @FXML
    private void deleteJob(ActionEvent event) {
        OffreEmploi selectedOffre = jobListView.getSelectionModel().getSelectedItem();
        if (selectedOffre != null) {
            offreEmploiService.delete(selectedOffre.getId());
            offres.remove(selectedOffre);
        }
    }

    @FXML
    private void viewCondidates(ActionEvent event) {
        OffreEmploi selectedOffre = jobListView.getSelectionModel().getSelectedItem();
        if (selectedOffre != null) {
            showCandidateList(selectedOffre.getId());
        }
    }

    private void showAddJobDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ebank/application/jobadd.fxml"));
            AnchorPane dialogPane = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Job");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(jobListView.getScene().getWindow());
            Scene scene = new Scene(dialogPane);
            dialogStage.setScene(scene);

            JobAddController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setJobListController(this);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showUpdateJobDialog(OffreEmploi offre) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ebank/application/updateJobDialog.fxml"));
            AnchorPane dialogPane = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Update Job");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(jobListView.getScene().getWindow());
            Scene scene = new Scene(dialogPane);
            dialogStage.setScene(scene);

            UpdateJobDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setOffreEmploi(offre);
            controller.setJobListController(this);

            dialogStage.showAndWait();
            refreshJobList(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateJobInList(OffreEmploi updatedOffre) {
        offreEmploiService.update(updatedOffre, updatedOffre.getId());
        loadJobOffers();
    }

    public void addJobToList(OffreEmploi newOffre) {
        offreEmploiService.add(newOffre);
        offres.add(newOffre);
    }

    private void showCandidateList(int jobId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ebank/application/condidatlist.fxml"));
            AnchorPane dialogPane = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("View Candidates");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(jobListView.getScene().getWindow());
            Scene scene = new Scene(dialogPane);
            dialogStage.setScene(scene);

            CondidatListController controller = loader.getController();
            controller.setJobId(jobId);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
