package com.ebank.application.controllers;

import com.ebank.application.models.Cheque;
import com.ebank.application.services.ChequeService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class ChequeController {
    private final ChequeService chequeService = new ChequeService();

    @FXML
    private TextField nameField;

    @FXML
    private DatePicker dateField;

    @FXML
    private Button addButton;

    @FXML
    private Button cancelButton;

    @FXML
    private void handleAddButtonAction() {
        String name = nameField.getText();
        LocalDate localDate = dateField.getValue();

        if (name != null && !name.isEmpty() && localDate != null) {
            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Cheque cheque = new Cheque();
            cheque.setDateEmission(date);
            cheque.setTitulaire(name);
            String result = addCheque(cheque);
            System.out.println(result);
            // Optionally, clear the fields or show a success message
            nameField.clear();
            dateField.setValue(null);
        } else {
            // Show an error message
            System.out.println("Please fill all fields.");
        }
    }

    @FXML
    private void handleCancelButtonAction() {
        // Optionally, clear the fields or close the window
        nameField.clear();
        dateField.setValue(null);
    }

    public String addCheque(Cheque c) {
        return chequeService.add(c);
    }

    public void deleteCheque(int id) {
        chequeService.delete(id);
    }

    public void updateCheque(Cheque c, int id) {
        chequeService.update(c, id);
    }

    public List<Cheque> getAllCheques() {
        return chequeService.getAll();
    }
}
