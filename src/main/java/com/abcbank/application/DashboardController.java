package com.abcbank.application;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

import com.abcbank.application.Models.User;
import com.abcbank.application.TransfertServices.TransfertService;
import com.abcbank.application.Utils.MaConnexion;
import com.abcbank.application.loginController;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class DashboardController implements Initializable {

    @FXML
    private Label accNumber;

    @FXML
    private Label balance;

    @FXML
    private Button confirmDepositButton;

    @FXML
    private Button confirmTransferButton;

    @FXML
    private Button confirmWithdrawButton;

    @FXML
    private TextField convertAmount;

    @FXML
    private Button convertButton;

    @FXML
    private Button converterButton;

    @FXML
    private Pane converterPane;

    @FXML
    private TextField depositAmountTextField;

    @FXML
    private Button depositButton;

    @FXML
    private Label depositConfirmationText;

    @FXML
    private Pane depositPane;

    @FXML
    private Label dob;

    @FXML
    private Label emailLabel;

    @FXML
    private ComboBox<String> firstCurrency;

    @FXML
    private Button homeButton;

    @FXML
    private Pane homePane;

    @FXML
    private Label name;

    @FXML
    private TextField recieverTextField;

    @FXML
    private Button resetButton;

    @FXML
    private TextField resultAmount;

    @FXML
    private ComboBox<String> secondCurrency;

    @FXML
    private TextField transferAmountTextField;

    @FXML
    private Button transferButton;

    @FXML
    private Button swapButton;

    @FXML
    private Label transferConfirmationText;

    @FXML
    private Label converterLabel;

    @FXML
    private Pane transferPane;

    @FXML
    private TextField withdrawAmountTextField;

    @FXML
    private Button withdrawButton;

    @FXML
    private Label withdrawConfirmationText;

    @FXML
    private Pane withdrawPane;

    private final TransfertService transfertService = new TransfertService();

    protected String errorStyle = "-fx-text-fill: RED;";
    String successStyle = "-fx-text-fill: GREEN;";

    public User currentUser = new User();

    ResultSet rs = null;

    public void setLabels() {
        name.setText(currentUser.getName());
        dob.setText(currentUser.getDob().toString());
        accNumber.setText(Integer.toString(currentUser.getAcc_num()));
        balance.setText(String.format("%.2f", currentUser.getBalance()) + "$");
        emailLabel.setText(currentUser.getEmail());
    }

    @FXML
    void showDepositPane() {
        homePane.setVisible(false);
        depositPane.setVisible(true);
        withdrawPane.setVisible(false);
        transferPane.setVisible(false);
        converterPane.setVisible(false);
    }

    @FXML
    void showHomePane() {
        homePane.setVisible(true);
        depositPane.setVisible(false);
        withdrawPane.setVisible(false);
        transferPane.setVisible(false);
        converterPane.setVisible(false);
        setLabels();
    }

    @FXML
    void showTransferPane() {
        homePane.setVisible(false);
        depositPane.setVisible(false);
        withdrawPane.setVisible(false);
        transferPane.setVisible(true);
        converterPane.setVisible(false);
    }

    @FXML
    void showWithdrawPane() {
        homePane.setVisible(false);
        depositPane.setVisible(false);
        withdrawPane.setVisible(true);
        transferPane.setVisible(false);
        converterPane.setVisible(false);
    }

    @FXML
    void showConverterPane() {
        homePane.setVisible(false);
        depositPane.setVisible(false);
        withdrawPane.setVisible(false);
        transferPane.setVisible(false);
        converterPane.setVisible(true);
    }

    @FXML
    public void confirmDeposit() {
        try {
            double amount = Double.parseDouble(depositAmountTextField.getText());
            transfertService.deposit(amount, currentUser);
            depositConfirmationText.setText("Deposit Succeeded");
            depositConfirmationText.setStyle(successStyle);
            depositAmountTextField.setText("");
        } catch (NumberFormatException e) {
            depositConfirmationText.setText("Please Enter a Numeric Value");
            depositConfirmationText.setStyle(errorStyle);
            depositAmountTextField.setText("");
        } catch (SQLException e) {
            depositConfirmationText.setText("Deposit Failed");
            depositConfirmationText.setStyle(errorStyle);
            depositAmountTextField.setText("");
        } catch (IllegalArgumentException e) {
            depositConfirmationText.setText(e.getMessage());
            depositConfirmationText.setStyle(errorStyle);
            depositAmountTextField.setText("");
        }
    }

    @FXML
    public void confirmWithdraw() {
        try {
            double amount = Double.parseDouble(withdrawAmountTextField.getText());
            transfertService.withdraw(amount, currentUser);
            withdrawConfirmationText.setText("Withdraw Succeeded");
            withdrawConfirmationText.setStyle(successStyle);
            withdrawAmountTextField.setText("");
        } catch (NumberFormatException e) {
            withdrawConfirmationText.setText("Please Enter a Numeric Value");
            withdrawConfirmationText.setStyle(errorStyle);
            withdrawAmountTextField.setText("");
        } catch (SQLException e) {
            withdrawConfirmationText.setText("Withdraw Failed");
            withdrawConfirmationText.setStyle(errorStyle);
            withdrawAmountTextField.setText("");
        } catch (IllegalArgumentException e) {
            withdrawConfirmationText.setText(e.getMessage());
            withdrawConfirmationText.setStyle(errorStyle);
            withdrawAmountTextField.setText("");
        }
    }

    @FXML
    public void confirmTransfer() {
        try {
            double amount = Double.parseDouble(transferAmountTextField.getText());
            String receiverAccNumber = recieverTextField.getText();
            transfertService.transfer(amount, receiverAccNumber, currentUser);
            transferConfirmationText.setText("Transfer Succeeded");
            transferConfirmationText.setStyle(successStyle);
            recieverTextField.setText("");
            transferAmountTextField.setText("");
        } catch (NumberFormatException e) {
            transferConfirmationText.setText("Please Enter a Numeric Value");
            transferConfirmationText.setStyle(errorStyle);
            transferAmountTextField.setText("");
        } catch (SQLException e) {
            transferConfirmationText.setText("Transfer Failed");
            transferConfirmationText.setStyle(errorStyle);
            recieverTextField.setText("");
            transferAmountTextField.setText("");
        } catch (IllegalArgumentException e) {
            transferConfirmationText.setText(e.getMessage());
            transferConfirmationText.setStyle(errorStyle);
            recieverTextField.setText("");
            transferAmountTextField.setText("");
        }
    }


    public void logout() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root1 = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("ABC Bank");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("icons/icon.png"))));
        stage.setScene(new Scene(root1));
        stage.setResizable(false);
        stage.show();
        Stage stage2 = (Stage) balance.getScene().getWindow();
        stage2.close();
    }

    @FXML
    public void resetConverter() {
        firstCurrency.setValue("");
        secondCurrency.setValue("");
        resultAmount.setText("");
        convertAmount.setText("");
    }

    public double convert(String from, String to, double amount) throws IOException {
        double result;
        String url_str = "https://api.exchangerate.host/convert?from=" + from + "&to=" + to;
        URL url = new URL(url_str);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.setRequestMethod("GET");
        request.connect();
        int responseCode = request.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            JsonObject jsonobj = root.getAsJsonObject();
            String req_result = jsonobj.get("result").getAsString();
            result = Double.parseDouble(req_result);
            return amount * result;
        } else {
            converterLabel.setText("Connection Failed!");
        }
        return 0;
    }

    @FXML
    public void convertButtonAction() {
        double amount;
        String from = firstCurrency.getValue();
        String to = secondCurrency.getValue();
        String value = "";
        if (Objects.equals(firstCurrency.getValue(), "") || Objects.equals(secondCurrency.getValue(), "")) {
            converterLabel.setText("Please Select a Currency!");
            return;
        } else
            converterLabel.setText("");
        try {
            amount = Double.parseDouble(convertAmount.getText());
            value = String.format("%.2f", convert(from, to, amount));
            resultAmount.setText(value + " " + secondCurrency.getValue());
        } catch (NumberFormatException e) {
            return;
        } catch (IOException ex) {
            converterLabel.setText("Connection Failed!");
        }
    }

    public void swap() {
        String val = firstCurrency.getValue();
        firstCurrency.setValue(secondCurrency.getValue());
        secondCurrency.setValue(val);
        convertButtonAction();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginController.limitTextField(recieverTextField);
        loginController.limitTextField(depositAmountTextField);
        loginController.limitTextField(withdrawAmountTextField);
        loginController.limitTextField(transferAmountTextField);
        String[] currencies = new String[] { "USD", "EUR", "GBP", "CAD", "AED", "EGP", "SAR", "INR", "JPY", "CHF",
                "RUB", "SGD", "SEK", "BRL", "IQD", "MAD", "CNY", "MXN", "KWD", "TRY", "ARS", "LYD", "AUD" };
        firstCurrency.getItems().addAll(currencies);
        secondCurrency.getItems().addAll(currencies);
    }
}