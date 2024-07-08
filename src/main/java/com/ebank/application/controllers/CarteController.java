package com.ebank.application.controllers;

import com.ebank.application.models.CarteBancaire;
import com.ebank.application.services.CarteService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    private Button annulerButton;

    @FXML
    private Button autreOptionButton;

    @FXML
    private ComboBox<String> typeCarteComboBox;

    @FXML
    private ListView<CarteBancaire> cartesListView;

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

        // Load existing cards into the ListView
        refreshCartesList();
    }

    private void refreshCartesList() {
        List<CarteBancaire> cartes = getAllCartes();
        cartesListView.setItems(FXCollections.observableArrayList(cartes));
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

    @FXML
    private void annulerButtonAction(ActionEvent event) {
        clearFields();
    }

    @FXML
    private void autreOptionButtonAction(ActionEvent event) {
        CarteBancaire selectedCarte = cartesListView.getSelectionModel().getSelectedItem();
        if (selectedCarte != null) {
            showOptionsDialog(selectedCarte);
        } else {
            showAlert(Alert.AlertType.WARNING, "Sélection", "Veuillez sélectionner une carte dans la liste.");
        }
    }

    private void showOptionsDialog(CarteBancaire carte) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Options de carte");
        dialog.setHeaderText("Choisissez une option pour la carte " + carte.getNumero());

        ButtonType updateButtonType = new ButtonType("Modifier", ButtonBar.ButtonData.OK_DONE);
        ButtonType deleteButtonType = new ButtonType("Supprimer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, deleteButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                return "update";
            } else if (dialogButton == deleteButtonType) {
                return "delete";
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(option -> {
            if ("update".equals(option)) {
                updateCarte(carte);
            } else if ("delete".equals(option)) {
                deleteCarte(carte.getId());
                refreshCartesList();
            }
        });
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