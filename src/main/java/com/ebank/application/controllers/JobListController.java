package com.ebank.application.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

import com.ebank.application.models.OffreEmploi;
import com.ebank.application.services.OffreEmploiService;

public class JobListController {

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
        offres.addAll(allOffres);
        jobListView.setItems(offres);
    }

    @FXML
    private void applyForJob(ActionEvent event) {
        OffreEmploi selectedOffre = jobListView.getSelectionModel().getSelectedItem();
        if (selectedOffre != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ebank/application/applyForJob.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(loader.load()));
                ApplyForJobController controller = loader.getController();
                controller.initData(selectedOffre);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
