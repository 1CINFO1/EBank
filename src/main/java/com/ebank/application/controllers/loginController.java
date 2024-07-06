package com.ebank.application.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

import com.ebank.application.models.CharityCampaignModel;
import com.ebank.application.models.AdminUser;
import com.ebank.application.services.LoginService;
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
    private ComboBox<String> roleComboBox;

    private final LoginService loginService = new LoginService();

    public void showLoginPane() {
        loginPane.setVisible(true);
        signUpPane.setVisible(false);
    }

    public void showSignUPPane() {
        loginPane.setVisible(false);
        signUpPane.setVisible(true);
    }

    @FXML
    void switchToLoginPane() {
        loginPane.setVisible(true);
        signUpPane.setVisible(false);
    }

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

    private void closeWindow() {
        Stage stage = (Stage) email.getScene().getWindow();
        stage.close();
    }

    private void launchDashboard(CharityCampaignModel c) throws IOException {
        URL location = getClass().getResource("/com/ebank/application/CharityCampDashboard.fxml");
        if (location == null) {
            throw new IOException("Cannot find CharityCampDashboard.fxml");
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
    }

    private void launchDashboard(User c) throws IOException {
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
    }

    private void launchDashboard(AdminUser c) throws IOException {
        URL location = getClass().getResource("/com/ebank/application/adminDashboard.fxml");
        if (location == null) {
            throw new IOException("Cannot find adminDashboard.fxml");
        }

        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root1 = fxmlLoader.load();
        AdminController dController = fxmlLoader.getController();
        dController.currentUser = c;
        dController.loadMessagesView(c);

        dController.setLabels();
        dController.showHomePane();
        Stage stage = new Stage();
        stage.setTitle("E-Bank");
        stage.getIcons().add(new Image(
                Objects.requireNonNull(getClass().getResourceAsStream("/com/ebank/application/icons/icon.png"))));
        stage.setScene(new Scene(root1));
        stage.show();
    }

    @FXML
    void Login() throws SQLException, IOException {
        String emailText = email.getText();
        String passwordText = loginPassword.getText();
        System.out.println("==>> " + emailText + " pw==>>" + passwordText);

        if (emailText.isBlank() || passwordText.isBlank()) {
            JOptionPane.showMessageDialog(null, "Email or Password is empty");
            return;
        }

        User regularUser = loginService.getRegularUser(emailText, passwordText);
        if (regularUser != null && "USER".equals(regularUser.getRole())) {
            closeWindow();
            launchDashboard(regularUser);
            return;
        }

        CharityCampaignModel charityUser = loginService.getCharityUser(emailText, passwordText);
        if (charityUser != null && "CHARITY".equals(charityUser.getRole())) {
            closeWindow();
            launchDashboard(charityUser);
            return;
        }

        AdminUser adminUser = loginService.getAdminUser(emailText, passwordText);
        if (adminUser != null && "ADMIN".equals(adminUser.getRole())) {
            closeWindow();
            launchDashboard(adminUser);
            return;
        }

        JOptionPane.showMessageDialog(null, "Invalid email or password");
    }

    @FXML
    void signUp() throws SQLException {
        String name = signupName.getText();
        String email = signupEmail.getText();
        String accountNumber = signupAccountNumber.getText();
        LocalDate dob = signupDOB.getValue();
        String password = signupPassword.getText();
        String accountype= roleComboBox.getValue();

        System.out.println(accountype);

        if (!loginService.isValid(name, email, accountNumber, password, dob,accountype)) {
            JOptionPane.showMessageDialog(null, "Please fill in all fields correctly.");
            return;
        }
        if(accountype=="User"){loginService.addUser(name, email, accountNumber, dob, password);
            JOptionPane.showMessageDialog(null, "User added successfully!");
            switchToLoginPane();
        }else {
            loginService.addUser2(name, email, accountNumber, dob, password);
            JOptionPane.showMessageDialog(null, "User added successfully!");
            switchToLoginPane();
        }

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
    public void initialize(URL url, ResourceBundle resourceBundle) {
        shownPassword.visibleProperty()
                .bind(Bindings.createBooleanBinding(() -> toggleButton.isSelected(), toggleButton.selectedProperty()));
        shownLoginPassword.visibleProperty().bind(Bindings.createBooleanBinding(() -> loginToggleButton.isSelected(),
                loginToggleButton.selectedProperty()));
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