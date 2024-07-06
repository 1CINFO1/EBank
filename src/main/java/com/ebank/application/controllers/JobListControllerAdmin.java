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

    @FXML void updateJob(OffreEmploi offreEmploi) {
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

    private void showAddJobDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ebank/application/jobadd.fxml"));
            AnchorPane dialogPane = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Job");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(jobListView.getScene().getWindow());
            Scene scene = new Scene(dialogPane);
            dialogStage.setScene(scene);

            // Get controller and set necessary data or handlers
            JobAddController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setJobListController(this); // Pass the reference to this controller

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to add a job offer to the list after successful addition
    public void addJobToList(OffreEmploi newOffre) {
        offreEmploiService.add(newOffre);
        offres.add(newOffre);
    }

    private void showUpdateJobDialog(OffreEmploi offre) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ebank/application/updateJobDialog.fxml"));
            AnchorPane dialogPane = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Update Job");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(jobListView.getScene().getWindow());
            Scene scene = new Scene(dialogPane);
            dialogStage.setScene(scene);

            // Get controller and set necessary data or handlers
            UpdateJobDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setOffreEmploi(offre);

            dialogStage.showAndWait();
            refreshJobList(null); // Refresh job list after updating
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void updateJobInList(OffreEmploi updatedOffre) {
        offreEmploiService.update(updatedOffre, 0);
        // Optionally, update the list in the UI
        loadJobOffers();
    }
    
}
