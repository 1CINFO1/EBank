package com.ebank.application.services;

import com.ebank.application.interfaces.transfertInterface;
import com.ebank.application.models.Reclamation;
import com.ebank.application.models.Transfer;
import com.ebank.application.models.User;
import com.ebank.application.utils.MaConnexion;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;


public class ConverterService implements transfertInterface<Transfer> {
    Connection conn = MaConnexion.getInstance().getCnx();



    @Override
    public void deposit(double amount, User currentUser) throws SQLException {
        if(amount <= 0) {
            throw new IllegalArgumentException("Please enter a positive value");
        }

        double total = amount + currentUser.getBalance();
        currentUser.setBalance(total);

        String sql = "UPDATE users SET balance = ? WHERE email = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setDouble(1, total);
        pst.setString(2, currentUser.getEmail());
        pst.executeUpdate();
    }

    @Override
    public void withdraw(double amount, User currentUser) throws SQLException {
        if((currentUser.getBalance() - amount) < 0 || amount <= 0) {
            throw new IllegalArgumentException("Insufficient balance or invalid amount");
        }

        double total = currentUser.getBalance() - amount;
        currentUser.setBalance(total);

        String sql = "UPDATE users SET balance = ? WHERE email = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setDouble(1, total);
        pst.setString(2, currentUser.getEmail());
        pst.executeUpdate();
    }

    @Override
    public void transfer(double amount, String receiverAccNumber, User currentUser) throws SQLException {
        if(receiverAccNumber.isBlank() || receiverAccNumber.equals(String.valueOf(currentUser.getAcc_num()))) {
            showReclamationOption(currentUser, null, amount, "Invalid receiver account number");
            return;
        }

        int receiverAccNum = Integer.parseInt(receiverAccNumber);
        String sql = "SELECT * FROM users WHERE acc_num = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, receiverAccNum);
        ResultSet rs = pst.executeQuery();

        if(rs.next()) {
            User receiver = new User(
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getDate("dob").toLocalDate(),
                    rs.getInt("acc_num"),
                    rs.getDouble("balance")
            );

            if(amount <= 0 || amount > currentUser.getBalance()) {
                showReclamationOption(currentUser, receiver, amount, "Invalid amount or insufficient balance");
                return;
            }

            try {
                // Withdraw amount from the sender's account
                withdraw(amount, currentUser);

                // Deposit amount to the receiver's account
                receiver.setBalance(receiver.getBalance() + amount);
                String updateReceiverSql = "UPDATE users SET balance = ? WHERE acc_num = ?";
                PreparedStatement updateReceiverPst = conn.prepareStatement(updateReceiverSql);
                updateReceiverPst.setDouble(1, receiver.getBalance());
                updateReceiverPst.setInt(2, receiver.getAcc_num());
                updateReceiverPst.executeUpdate();

                // Show success message
                showAlert("Success", "Transfer completed successfully", Alert.AlertType.INFORMATION);
            } catch (SQLException e) {
                showReclamationOption(currentUser, receiver, amount, "Transfer failed: " + e.getMessage());
            }
        } else {
            showReclamationOption(currentUser, null, amount, "User not found");
        }
    }

    @Override
    public void transfer2(double amount, String receiverAccNumber, User currentUser) throws SQLException {

    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Platform.runLater(() -> {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }

    private void showReclamationOption(User sender, User receiver, double amount, String errorMessage) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Transfer Error");
            alert.setHeaderText("An error occurred: " + errorMessage);
            alert.setContentText("Do you want to file a reclamation?");

            ButtonType yesButton = new ButtonType("Yes");
            ButtonType noButton = new ButtonType("No");
            alert.getButtonTypes().setAll(yesButton, noButton);

            alert.showAndWait().ifPresent(response -> {
                if (response == yesButton) {
                    showReclamationDialog(sender, receiver, amount, errorMessage);
                }
            });
        });
    }

    private void showReclamationDialog(User sender, User receiver, double amount, String errorMessage) {
        Stage dialog = new Stage();
        dialog.setTitle("File Reclamation");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        Label errorLabel = new Label("Error: " + errorMessage);
        GridPane.setConstraints(errorLabel, 0, 0, 2, 1);

        TextArea textArea = new TextArea();
        textArea.setPromptText("Enter your reclamation here");
        textArea.setPrefRowCount(5);
        textArea.setPrefColumnCount(30);
        textArea.setWrapText(true);
        GridPane.setConstraints(textArea, 0, 1, 2, 1);

        Button sendButton = new Button("Send");
        GridPane.setConstraints(sendButton, 0, 2);

        Button cancelButton = new Button("Cancel");
        GridPane.setConstraints(cancelButton, 1, 2);

        grid.getChildren().addAll(errorLabel, textArea, sendButton, cancelButton);

        sendButton.setOnAction(e -> {
            String reclamationContent = textArea.getText();
            User currentUser = new User();

            if (!reclamationContent.trim().isEmpty()) {
                Reclamation reclamation = new Reclamation(
                    reclamationContent,
                    LocalDateTime.now(),
                    currentUser.getId(),
                    generateTransactionId(new Transfer(6,7,100.00,new Date(2002,3,3),"debiteur"))
                );
                // reclamation.setIdTrans(generateTransactionId(new Transfer(6,7,100.00,new Date(2002,3,3),"debiteur")));
                saveReclamation(reclamation);
                
                showAlert("Success", "Reclamation filed successfully!", Alert.AlertType.INFORMATION);
                dialog.close();
            } else {
                showAlert("Error", "Please enter a reclamation message.", Alert.AlertType.ERROR);
            }
        });

        cancelButton.setOnAction(e -> dialog.close());

        Scene scene = new Scene(grid);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private int generateTransactionId(Transfer transfer) {
        int transactionId = 0;
        String sql = "INSERT INTO Transactions (idReceiver, idSender, montant, date, type) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = MaConnexion.getInstance().getCnx();
            PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            preparedStatement.setInt(1, transfer.getIdReceiver());
            preparedStatement.setInt(2, transfer.getIdSender());
            preparedStatement.setDouble(3, transfer.getMontant());
            preparedStatement.setDate(4, transfer.getDate());
            preparedStatement.setString(5, transfer.getType());
            
            preparedStatement.executeUpdate();
            
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    transactionId = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        
        return transactionId;
    }
    private void saveReclamation(Reclamation reclamation) {
        // Implement logic to save the reclamation to your database
        try {
            String sql = "INSERT INTO reclamation (contenu, date_envoi, id_emetteur,  id_transaction) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, reclamation.getContenu());
            pst.setObject(2, reclamation.getDateEnvoi());
            pst.setInt(3, reclamation.getIdEmetteur());
            pst.setInt(4, reclamation.getIdTrans());
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save reclamation: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @Override
    public String add(Transfer transfer) {
        return "";
    }

    @Override
    public void delete(int t) {

    }

    @Override
    public void update(Transfer transfer, int id) {

    }

    @Override
    public List<Transfer> getAll() {
        return List.of();
    }
}