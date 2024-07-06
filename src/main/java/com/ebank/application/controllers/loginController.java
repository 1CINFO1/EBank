package com.ebank.application.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Date;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

import com.ebank.application.models.CharityCampaignModel;
import com.ebank.application.models.AdminUser;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

import com.ebank.application.models.User;
import com.ebank.application.utils.MaConnexion;

public class loginController implements Initializable {

    // private final Image closedEye = new
    // Image(Objects.requireNonNull(getClass().getResourceAsStream("icons/eyes_closed.png")));
    // private final Image openEye = new
    // Image(Objects.requireNonNull(getClass().getResourceAsStream("icons/eyes_open.png")));

    @FXML
    ImageView eyesImageView;

    @FXML
    private Pane loginPane;

    @FXML
    private Pane signUpPane;

    @FXML
    private TextField email;
    @FXML
    private Button Jobs; // Assuming this is the "Job offre" button from main.fxml

    @FXML
    private PasswordField signupPassword;
    @FXML
    private Label signupConfirmationText;
    @FXML
    private Label loginLabel;
    @FXML
    private TextField signupName;

    @FXML
    private TextField signupEmail;

    @FXML
    private TextField signupAccountNumber;

    @FXML
    private ToggleButton loginToggleButton;
    @FXML
    private DatePicker signupDOB;

    @FXML
    private PasswordField loginPassword;

    @FXML
    private TextField shownPassword;
    @FXML
    private TextField shownLoginPassword;

    @FXML
    private ToggleButton toggleButton;

    @FXML
    void showPassword() {
        // eyesImageView.setImage(closedEye);
        // eyesImageView.setImage(openEye);
        shownPassword.setVisible(toggleButton.isSelected());
    }

    @FXML
    void showLoginPassword() {
        shownLoginPassword.setVisible(loginToggleButton.isSelected());
    }

    ResultSet rs = null;
    PreparedStatement pst = null;

    // Strings which hold css elements to easily re-use in the application
    protected String errorStyle = "-fx-border-color: RED;";
    String successStyle = "-fx-border-color: #A9A9A9;";
    String textFillError = "-fx-text-fill: RED";
    Connection conn = MaConnexion.getInstance().getCnx();

    private boolean emailAlreadyExists() throws SQLException {
        String sql = "Select * From users Where email = ?";
        assert conn != null;
        pst = conn.prepareStatement(sql);
        pst.setString(1, signupEmail.getText());
        ResultSet rs = pst.executeQuery();
        return rs.next();
    }

    private boolean accountNumberAlreadyExists() throws SQLException {
        String sql = "Select * From users Where account_number = ?";
        assert conn != null;
        pst = conn.prepareStatement(sql);
        pst.setString(1, signupAccountNumber.getText());
        ResultSet rs = pst.executeQuery();
        return rs.next();
    }

    private boolean isValid() throws SQLException {
        boolean isValid = true;
        if (signupName.getText().isBlank() || (signupName.getText().length() < 10)) {
            signupName.setStyle(errorStyle);
            isValid = false;
        } else {
            signupName.setStyle(successStyle);
        }

        if (signupEmail.getText().isBlank() || (emailAlreadyExists())) {
            signupEmail.setStyle(errorStyle);
            isValid = false;
        } else {
            signupEmail.setStyle(successStyle);
        }

        if (signupAccountNumber.getText().isBlank() || (signupAccountNumber.getText().length() < 8)
                || accountNumberAlreadyExists()) {
            signupAccountNumber.setStyle(errorStyle);
            isValid = false;
        } else {
            signupAccountNumber.setStyle(successStyle);
        }

        if (signupPassword.getText().isBlank() || (signupPassword.getText().length() < 8)) {
            signupPassword.setStyle(errorStyle);
            isValid = false;
        } else {
            signupPassword.setStyle(successStyle);
        }

        if (signupDOB.getValue().toString().isBlank()) {
            signupDOB.setStyle(errorStyle);
            isValid = false;
        } else {
            signupDOB.setStyle(successStyle);
        }
        return isValid;
    }

    private void closeWindow() {
        Stage stage = (Stage) email.getScene().getWindow();
        stage.close();
    }


    private void launchDashboard1(CharityCampaignModel c) throws IOException {

        URL location = getClass().getResource("/com/ebank/application/CharityCampDashboard.fxml");
        if (location == null) {
            throw new IOException("Cannot find dashboard.fxml");
        }

        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root1 = fxmlLoader.load();
        CharityController dController = fxmlLoader.getController();
        dController.currentUser = c;
        dController.setLabels();
        dController.showHomePane();
        Stage stage = new Stage();
        stage.setTitle("E-Bank");
        stage.getIcons().add(new Image(
                Objects.requireNonNull(getClass().getResourceAsStream("/com/ebank/application/icons/icon.png"))));
        stage.setScene(new Scene(root1));
        stage.show();
        // Additional customization for charity dashboard
    }

    private void launchDashboard(User c) throws IOException {
         {
             URL location = getClass().getResource("/com/ebank/application/dashboard.fxml");
             if (location == null) {
                 throw new IOException("Cannot find dashboard.fxml");
             }

             FXMLLoader fxmlLoader = new FXMLLoader(location);
             Parent root1 = fxmlLoader.load();
             DashboardController dController = fxmlLoader.getController();
             dController.currentUser = c;
             dController.setLabels();
             dController.showHomePane();
             Stage stage = new Stage();
             stage.setTitle("E-Bank");
             stage.getIcons().add(new Image(
                     Objects.requireNonNull(getClass().getResourceAsStream("/com/ebank/application/icons/icon.png"))));
             stage.setScene(new Scene(root1));
             stage.show();
            // Additional customization for regular user dashboard
        }

    }

    private void launchDashboard2(AdminUser c) throws IOException {
        {
            URL location = getClass().getResource("/com/ebank/application/adminDashboard.fxml");
            if (location == null) {
                throw new IOException("Cannot find dashboard.fxml");
            }

            FXMLLoader fxmlLoader = new FXMLLoader(location);
            Parent root1 = fxmlLoader.load();
            adminController dController = fxmlLoader.getController();
            dController.currentUser = c;
            dController.setLabels();
            dController.showHomePane();
            Stage stage = new Stage();
            stage.setTitle("E-Bank");
            stage.getIcons().add(new Image(
                    Objects.requireNonNull(getClass().getResourceAsStream("/com/ebank/application/icons/icon.png"))));
            stage.setScene(new Scene(root1));
            stage.show();
            // Additional customization for regular user dashboard
        }

    }


    @FXML
    private void Login() {
        if (email.getText().isBlank() || loginPassword.getText().isBlank()) {
            email.setStyle(errorStyle);
            loginPassword.setStyle(errorStyle);
            loginLabel.setText("Missing Email or Password!");
            return;
        }

        String charityQuery = "SELECT * FROM charitycampaignmodel WHERE email = ? AND password = ?";
        String userQuery = "SELECT * FROM users WHERE email = ? AND password = ?";
        String adminQuery = "SELECT * FROM adminuser WHERE email = ? AND password = ?";

        try {
            assert conn != null;

            // Check in charitycampaignmodel table first
            pst = conn.prepareStatement(charityQuery);
            pst.setString(1, email.getText());
            pst.setString(2, loginPassword.getText());
            rs = pst.executeQuery();

            if (rs.next()) {
                closeWindow();
                // User is a charity campaign member
                String name = rs.getString("name");
                String userEmail = rs.getString("email");
                LocalDate dob = rs.getDate("dob").toLocalDate();
                int accNum = rs.getInt("acc_num");
                double balance = rs.getDouble("balance");
                String compagnieDeDonPatente = rs.getString("compagnieDeDon_Patente");

                CharityCampaignModel loggedInUser = new CharityCampaignModel(name, userEmail, dob, accNum, balance, loginPassword.getText(), compagnieDeDonPatente);
                launchDashboard1(loggedInUser); // Launch charity campaign dashboard
                return;
            }

            // Check in users table
            pst = conn.prepareStatement(userQuery);
            pst.setString(1, email.getText());
            pst.setString(2, loginPassword.getText());
            rs = pst.executeQuery();

            if (rs.next()) {
                closeWindow();
                // User is a regular user
                String name = rs.getString("name");
                String userEmail = rs.getString("email");
                LocalDate dob = rs.getDate("dob").toLocalDate();
                int accNum = rs.getInt("acc_num");
                double balance = rs.getDouble("balance");

                User loggedInUser = new User(name, userEmail, dob, accNum, balance, loginPassword.getText());
                launchDashboard(loggedInUser); // Launch regular user dashboard
                return;
            }

            // Check in admin table
            pst = conn.prepareStatement(adminQuery);
            pst.setString(1, email.getText());
            pst.setString(2, loginPassword.getText());
            rs = pst.executeQuery();

            if (rs.next()) {
                closeWindow();
                // User is an admin
                String name = rs.getString("name");
                String userEmail = rs.getString("email");
                LocalDate dob = rs.getDate("dob").toLocalDate();
                int accNum = rs.getInt("acc_num");
                double balance = rs.getDouble("balance");

                AdminUser loggedInUser = new AdminUser(name, userEmail, dob, accNum, balance, loginPassword.getText());
                launchDashboard2(loggedInUser); // Launch admin dashboard
                return;
            }

            // No user found in any table
            email.setStyle(errorStyle);
            loginPassword.setStyle(errorStyle);
            loginLabel.setText("Email or password is invalid!");

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            loginLabel.setText("Connection failed, please check your internet.");
            loginLabel.setStyle(textFillError);
        }
    }

    public void addUser() {

        try {
            if (isValid()) {
                String sql = "insert into users (name, email, account_number,balance ,dob, password) values (?,?,?,?,?,?)";
                User newUser = new User(signupName.getText(), signupEmail.getText(), signupDOB.getValue(),
                        Integer.parseInt(signupAccountNumber.getText()));
                assert conn != null;
                pst = conn.prepareStatement(sql);
                pst.setString(1, newUser.getName());
                pst.setString(2, newUser.getEmail());
                pst.setInt(3, newUser.getAcc_num());
                pst.setDouble(4, newUser.getBalance());
                pst.setDate(5, Date.valueOf(newUser.getDob()));
                pst.setString(6, signupPassword.getText());
                pst.execute();
                JOptionPane.showMessageDialog(null, "Successful SignUp!");
                showLoginPane();
            } else {
                signupConfirmationText.setText("Please Fill In The Data Correctly!");
            }
        } catch (Exception e) {
            signupConfirmationText.setText("Please Check Your Internet Connection!");
        }
    }

    public void showLoginPane() {
        loginPane.setVisible(true);
        signUpPane.setVisible(false);
    }

    public void showSignUPPane() {
        loginPane.setVisible(false);
        signUpPane.setVisible(true);
    }

    public static void limitTextField(TextField tf) {
        final int max = 8;
        tf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tf.setText(newValue.replaceAll("\\D", ""));
            }
            if (tf.getText().length() > max) {
                String s = tf.getText().substring(0, max);
                tf.setText(s);
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        limitTextField(signupAccountNumber);
        signupDOB.setValue(LocalDate.of(2000, 1, 1));
        Bindings.bindBidirectional(signupPassword.textProperty(), shownPassword.textProperty());
        Bindings.bindBidirectional(loginPassword.textProperty(), shownLoginPassword.textProperty());
        showLoginPane();
    }
    @FXML
     void Jobs(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ebank/application/jobList.fxml"));
            Parent root = loader.load();
            JobListController jobListController = loader.getController();
            // Optionally, pass any necessary data to the controller
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}