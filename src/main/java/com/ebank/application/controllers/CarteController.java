package com.ebank.application.controllers;

import com.ebank.application.models.CarteBancaire;
import com.ebank.application.services.CarteService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class CarteController {
    private CarteService carteService;

    @FXML
    private TextField nomTitulaireTextField;

    @FXML
    private TextField identifiantTitulaireTextField;

    @FXML
    private DatePicker dateExpirationDatePicker;

    @FXML
    private Button confirmerButton;

    @FXML
    private ComboBox<String> typeCarteComboBox;

    @FXML
    private TableView<CarteBancaire> cartesTableView;

    @FXML
    private TableColumn<CarteBancaire, Integer> idColumn;

    @FXML
    private TableColumn<CarteBancaire, String> numeroColumn;

    @FXML
    private TableColumn<CarteBancaire, Date> dateExpirationColumn;

    @FXML
    private TableColumn<CarteBancaire, String> titulaireColumn;

    @FXML
    private TableColumn<CarteBancaire, String> typeColumn;

    @FXML
    private TableColumn<CarteBancaire, Void> modifierColumn;

    @FXML
    private TableColumn<CarteBancaire, Void> supprimerColumn;
    @FXML
    private TableColumn<CarteBancaire, String> statusColumn;

    public CarteController() {
        this.carteService = new CarteService();
    }

    @FXML
    public void initialize() {
        // Initialize the ComboBox with card types
        ObservableList<String> cardTypes = FXCollections.observableArrayList(
                "Visa", "MasterCard", "American Express", "Autre");
        typeCarteComboBox.setItems(cardTypes);
        typeCarteComboBox.getSelectionModel().selectFirst();

        // Initialize table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        numeroColumn.setCellValueFactory(new PropertyValueFactory<>("numero"));
        dateExpirationColumn.setCellValueFactory(new PropertyValueFactory<>("dateExpiration"));
        titulaireColumn.setCellValueFactory(new PropertyValueFactory<>("titulaire"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Add button to Modifier column
        addButtonToTable(modifierColumn, "Modifier", this::handleModifierAction);

        // Add button to Supprimer column
        addButtonToTable(supprimerColumn, "Supprimer", this::handleSupprimerAction);

        // Load existing cards into the TableView
        refreshCartesList();
    }

    private void addButtonToTable(TableColumn<CarteBancaire, Void> column, String text,
            java.util.function.Consumer<CarteBancaire> action) {
        Callback<TableColumn<CarteBancaire, Void>, TableCell<CarteBancaire, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<CarteBancaire, Void> call(final TableColumn<CarteBancaire, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button(text);

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            CarteBancaire data = getTableView().getItems().get(getIndex());
                            action.accept(data);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };

        column.setCellFactory(cellFactory);
    }

    private void handleModifierAction(CarteBancaire carte) {
        updateCarte(carte);
    }

    private void handleSupprimerAction(CarteBancaire carte) {
        deleteCarte(carte.getId());
        refreshCartesList();
    }

    private void refreshCartesList() {
        List<CarteBancaire> cartes = getAllCartes();
        cartesTableView.setItems(FXCollections.observableArrayList(cartes));
    }

    @FXML
    private void confirmerButtonAction(ActionEvent event) {
        String nomTitulaire = nomTitulaireTextField.getText();
        String identifiantTitulaire = identifiantTitulaireTextField.getText();
        Date dateExpiration = Date
                .from(dateExpirationDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        String typeCarte = typeCarteComboBox.getValue();

        CarteBancaire nouvelleCarte = new CarteBancaire();
        nouvelleCarte.setTitulaire(nomTitulaire);
        nouvelleCarte.setNumero(identifiantTitulaire);
        nouvelleCarte.setDateExpiration(dateExpiration);
        nouvelleCarte.setType(typeCarte);

        String result = addCarte(nouvelleCarte);
        showAlert(Alert.AlertType.INFORMATION, "Ajout de carte", result);
        clearFields();
        refreshCartesList();
    }

    private void updateCarte(CarteBancaire carte) {
        // Populate fields with current card info
        nomTitulaireTextField.setText(carte.getTitulaire());
        identifiantTitulaireTextField.setText(carte.getNumero());
        dateExpirationDatePicker
                .setValue(carte.getDateExpiration().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        typeCarteComboBox.setValue(carte.getType());

        // Change confirmer button to update
        confirmerButton.setText("Mettre à jour");
        confirmerButton.setOnAction(e -> {
            carte.setTitulaire(nomTitulaireTextField.getText());
            carte.setNumero(identifiantTitulaireTextField.getText());
            carte.setDateExpiration(
                    Date.from(dateExpirationDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            carte.setType(typeCarteComboBox.getValue());

            updateCarte(carte, carte.getId());
            showAlert(Alert.AlertType.INFORMATION, "Mise à jour", "Carte mise à jour avec succès");
            clearFields();
            refreshCartesList();
            resetConfirmerButton();
        });
    }

    private void resetConfirmerButton() {
        confirmerButton.setText("Confirmer");
        confirmerButton.setOnAction(this::confirmerButtonAction);
    }

    private void clearFields() {
        nomTitulaireTextField.clear();
        identifiantTitulaireTextField.clear();
        dateExpirationDatePicker.setValue(null);
        typeCarteComboBox.getSelectionModel().selectFirst();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public String addCarte(CarteBancaire carte) {
        return carteService.add(carte);
    }

    public void deleteCarte(int id) {
        carteService.delete(id);
    }

    public void updateCarte(CarteBancaire carte, int id) {
        carteService.update(carte, id);
    }

    public List<CarteBancaire> getAllCartes() {
        return carteService.getAll();
    }
}