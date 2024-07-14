package com.ebank.application.controllers;

import com.ebank.application.models.CarteBancaire;
import com.ebank.application.services.CarteService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class AdminCarteController {

    @FXML
    private TableView<CarteBancaire> cartesTableView;

    @FXML
    private TableColumn<CarteBancaire, Integer> idColumn;

    @FXML
    private TableColumn<CarteBancaire, String> numeroColumn;

    @FXML
    private TableColumn<CarteBancaire, String> dateExpirationColumn;

    @FXML
    private TableColumn<CarteBancaire, String> titulaireColumn;

    @FXML
    private TableColumn<CarteBancaire, String> typeColumn;

    @FXML
    private TableColumn<CarteBancaire, String> statusColumn;

    @FXML
    private TableColumn<CarteBancaire, Void> actionColumn;

    private CarteService carteService;

    public AdminCarteController() {
        this.carteService = new CarteService();
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        numeroColumn.setCellValueFactory(new PropertyValueFactory<>("numero"));
        dateExpirationColumn.setCellValueFactory(new PropertyValueFactory<>("dateExpiration"));
        titulaireColumn.setCellValueFactory(new PropertyValueFactory<>("titulaire"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        addButtonToTable();

        refreshCartesList();
    }

    private void addButtonToTable() {
        Callback<TableColumn<CarteBancaire, Void>, TableCell<CarteBancaire, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<CarteBancaire, Void> call(final TableColumn<CarteBancaire, Void> param) {
                return new TableCell<>() {
                    private final Button approveButton = new Button("Approuver");
                    private final Button rejectButton = new Button("Rejeter");

                    {
                        approveButton.setOnAction((event) -> {
                            CarteBancaire carte = getTableView().getItems().get(getIndex());
                            approveCard(carte);
                        });

                        rejectButton.setOnAction((event) -> {
                            CarteBancaire carte = getTableView().getItems().get(getIndex());
                            rejectCard(carte);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            CarteBancaire carte = getTableView().getItems().get(getIndex());
                            if ("In Progress".equals(carte.getStatus())) {
                                HBox hbox = new HBox(5);
                                hbox.getChildren().addAll(approveButton, rejectButton);
                                setGraphic(hbox);
                            } else {
                                setGraphic(null);
                            }
                        }
                    }
                };
            }
        };

        actionColumn.setCellFactory(cellFactory);
    }

    private void approveCard(CarteBancaire carte) {
        carte.setStatus("Confirmed");
        carteService.update(carte, carte.getId());
        refreshCartesList();
    }

    private void rejectCard(CarteBancaire carte) {
        carte.setStatus("Rejected");
        carteService.update(carte, carte.getId());
        refreshCartesList();
    }

    private void refreshCartesList() {
        cartesTableView.getItems().setAll(carteService.getAll());
    }
}