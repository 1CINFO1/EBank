package com.ebank.application.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.List;

public class JobStatisticsController {

    @FXML
    private ListView<String> statisticsListView;

    public void setStatistics(List<String> statistics) {
        statisticsListView.getItems().setAll(statistics);
    }
}
