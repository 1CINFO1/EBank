package com.ebank.application.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import com.ebank.application.models.CharityCampaignModel;
import com.ebank.application.models.Publication;
import com.ebank.application.models.User;
import com.ebank.application.services.ICharityService;
import com.ebank.application.services.IpublicationImple;
import com.ebank.application.services.ReclamationService;
// import com.ebank.application.services.ConverterService;
import com.ebank.application.services.TransferService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DashboardController implements Initializable {

    @FXML
    private Button charityButton;
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
    private TextField transferAmountTextField2;
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
    private Pane transferCharityPane;

    @FXML
    private TextField withdrawAmountTextField;

    @FXML
    private Button withdrawButton;

    @FXML
    private Label withdrawConfirmationText;

    @FXML
    private Pane withdrawPane;

    @FXML
    private Button messagesButton;

    @FXML
    private Button backButton2;
    @FXML
    private Pane publicationPane;
    @FXML
    private Pane charityPane;

    @FXML
    private VBox publicationListVBox;
    @FXML
    private LineChart<String, Number> transferStatisticsChart;

    @FXML
    private NumberAxis yAxis;
    @FXML
    private Pane reclamationPane;
    @FXML
    private TextArea reclamationContent;

    @FXML
    private Label reclamationConfirmationText;

    @FXML
    private TextField recieverTextField1;

    @FXML
    private TextField recieverTextField2;

    private final TransferService transferService = new TransferService();
    private final IpublicationImple ipublicationImple = new IpublicationImple();
    private final ReclamationService reclamationService = new ReclamationService();
    // ReclamationController reclamationController= new ReclamationController();

    @FXML
    private Label title;
    @FXML
    private Label campaignName;
    @FXML
    private Label pubDescription;
    @FXML
    private Label pubPicture;

    private int selectedPublicationId;

    private IpublicationImple publicationService;

    public DashboardController() {
        // Initialize the service
        this.publicationService = new IpublicationImple();

    }

    private ICharityService charityService = new ICharityService();
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

    public void publicationById(int id) {
        Publication publication = publicationService.getById(id);

        title.setText(publication.getTitle());
        campaignName.setText(publication.getCampaignName());
        pubDescription.setText(publication.getDescription());
        pubPicture.setText(publication.getPicture());

    }

    public void getAllPublication() {
        try {
            List<Publication> publications = publicationService.getAll();
            if (publications != null && !publications.isEmpty()) {
                publicationListVBox.getChildren().clear(); // Clear previous items
                for (Publication pub : publications) {
                    Label publicationLabel = new Label(
                            "Title: " + pub.getTitle() + "\n" +
                                    "Campaign Name: " + pub.getCampaignName() + "\n" +
                                    "Description: " + pub.getDescription() + "\n" +
                                    "Publication Date: " + pub.getPublicationDate() + "\n" +
                                    "Picture: " + pub.getPicture() + "\n");

                    // Create a button for donation
                    Button donateButton = new Button("Donate");
                    donateButton.setStyle("-fx-background-color: #4CAF50; " +
                            "-fx-text-fill: white; " +
                            "-fx-font-size: 14px; " +
                            "-fx-font-weight: bold; " +
                            "-fx-padding: 8px 16px; " +
                            "-fx-border-radius: 5px; " +
                            "-fx-background-radius: 5px;");

                    donateButton.setOnAction(event -> {
                        selectedPublicationId = pub.getId();
                        showcharityByIdPane(selectedPublicationId);
                        System.out.println("publication ID: " + selectedPublicationId);
                    });

                    // Create a VBox to hold the label and donate button
                    VBox publicationBox = new VBox();
                    publicationBox.getChildren().addAll(publicationLabel, donateButton);
                    publicationBox.setStyle(
                            "-fx-padding: 19; -fx-border-style: solid inside; -fx-border-width: 2; -fx-border-insets: 5; -fx-border-radius: 5; -fx-border-color: gray;");

                    // Add the VBox to the publicationListVBox
                    publicationListVBox.getChildren().add(publicationBox);
                }
                // Clear any previous error messages
            } else {
                System.out.println("No publications found.");
            }
        } catch (RuntimeException e) {
            e.printStackTrace(); // Handle or log the exception properly
        }
    }

    @FXML
    void showCharityPane() {
        homePane.setVisible(false);
        depositPane.setVisible(false);
        withdrawPane.setVisible(false);
        transferPane.setVisible(false);
        converterPane.setVisible(false);
        charityPane.setVisible(true);
        reclamationPane.setVisible(false);

        publicationPane.setVisible(false);
        transferCharityPane.setVisible(false);
        getAllPublication();
    }

    @FXML
    void goBackToPublicationDetails() {
        showcharityByIdPane(selectedPublicationId);
    }

    @FXML
    void showcharityByIdPane(int id) {
        homePane.setVisible(false);
        depositPane.setVisible(false);
        withdrawPane.setVisible(false);
        transferPane.setVisible(false);
        converterPane.setVisible(false);
        charityPane.setVisible(false);
        publicationPane.setVisible(true);
        reclamationPane.setVisible(false);

        transferCharityPane.setVisible(false);
        publicationById(id);

    }

    @FXML
    void showDepositPane() {
        homePane.setVisible(false);
        depositPane.setVisible(true);
        withdrawPane.setVisible(false);
        transferPane.setVisible(false);
        converterPane.setVisible(false);
        charityPane.setVisible(false);
        reclamationPane.setVisible(false);

        publicationPane.setVisible(false);
        transferCharityPane.setVisible(false);
    }

    @FXML
    public void showHomePane() {
        homePane.setVisible(true);
        depositPane.setVisible(false);
        withdrawPane.setVisible(false);
        transferPane.setVisible(false);
        converterPane.setVisible(false);
        charityPane.setVisible(false);
        reclamationPane.setVisible(false);

        publicationPane.setVisible(false);
        transferCharityPane.setVisible(false);
        setLabels();
    }

    @FXML
    void showCharityTransferPane() {
        homePane.setVisible(false);
        depositPane.setVisible(false);
        withdrawPane.setVisible(false);
        transferPane.setVisible(true);
        converterPane.setVisible(false);
        charityPane.setVisible(false);
        publicationPane.setVisible(false);
        transferCharityPane.setVisible(true);
        reclamationPane.setVisible(false);

    }

    @FXML
    void showTransferPane() {
        homePane.setVisible(false);
        depositPane.setVisible(false);
        withdrawPane.setVisible(false);
        transferPane.setVisible(true);
        converterPane.setVisible(false);
        charityPane.setVisible(false);
        reclamationPane.setVisible(false);

        publicationPane.setVisible(false);
        transferCharityPane.setVisible(false);
    }

    @FXML
    void showWithdrawPane() {
        homePane.setVisible(false);
        depositPane.setVisible(false);
        withdrawPane.setVisible(true);
        transferPane.setVisible(false);
        converterPane.setVisible(false);
        charityPane.setVisible(false);
        reclamationPane.setVisible(false);

        publicationPane.setVisible(false);
        transferCharityPane.setVisible(false);
    }

    @FXML
    void showConverterPane() {
        homePane.setVisible(false);
        depositPane.setVisible(false);
        withdrawPane.setVisible(false);
        transferPane.setVisible(false);
        converterPane.setVisible(true);
        charityPane.setVisible(false);
        reclamationPane.setVisible(false);

        publicationPane.setVisible(false);
        transferCharityPane.setVisible(false);
    }

    @FXML
    public void confirmDeposit() {
        try {
            double amount = Double.parseDouble(depositAmountTextField.getText());
            transferService.deposit(amount, currentUser);
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
            transferService.withdraw(amount, currentUser);
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
    public void confirmTransfer2() {
        try {
            double amount = Double.parseDouble(transferAmountTextField2.getText());

            Publication publication = publicationService.getById(selectedPublicationId);
            int charityId = publication.getCompagnieDeDon_Patente();
            CharityCampaignModel campaignModel = charityService.getCharityBy(charityId);

            String receiverAccNumber = Integer.toString(campaignModel.getAcc_num());

            transferService.transfer2(amount, receiverAccNumber, currentUser);
            transferConfirmationText.setText("Transfer Succeeded");
            transferConfirmationText.setStyle(successStyle);
            recieverTextField.setText("");
            transferAmountTextField2.setText("");
        } catch (NumberFormatException e) {
            transferConfirmationText.setText("Please Enter a Numeric Value");
            transferConfirmationText.setStyle(errorStyle);
            transferAmountTextField2.setText("");
        } catch (SQLException e) {
            transferConfirmationText.setText("Transfer Failed");
            transferConfirmationText.setStyle(errorStyle);
            recieverTextField.setText("");
            transferAmountTextField2.setText("");
        } catch (IllegalArgumentException e) {
            transferConfirmationText.setText(e.getMessage());
            transferConfirmationText.setStyle(errorStyle);
            recieverTextField.setText("");
            transferAmountTextField2.setText("");
        }
    }

    @FXML
    public void confirmTransfer() {
        try {
            double amount = Double.parseDouble(transferAmountTextField.getText());
            String receiverAccNumber = recieverTextField.getText();
            handleFailure();
            transferService.transfer(amount, receiverAccNumber, currentUser);

            transferConfirmationText.setText("Transfer Succeeded");
            transferConfirmationText.setStyle(successStyle);
            recieverTextField.setText("");
            transferAmountTextField.setText("");
        } catch (NumberFormatException e) {

            transferConfirmationText.setText("Please Enter a Numeric Value");
            transferConfirmationText.setStyle(errorStyle);
            transferAmountTextField.setText("");
        } catch (SQLException e) {

            handleFailure();
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

    private void handleFailure() {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ebank/application/reclamation.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("New Reclamation");
                stage.initModality(Modality.APPLICATION_MODAL); // Block events to other windows
                stage.setScene(new Scene(root));
                stage.showAndWait();

                // Optional: handle actions after the form is closed, if needed
                // ReclamationController controller = loader.getController();
                // Use controller if needed
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void logout() throws IOException {
        URL location = getClass().getResource("/com/ebank/application/login.fxml");
        if (location == null) {
            throw new IOException("Cannot find login.fxml");
        }
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root1 = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("ABC Bank");
        stage.getIcons().add(new Image(
                Objects.requireNonNull(getClass().getResourceAsStream("/com/ebank/application/icons/icon.png"))));
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

    @SuppressWarnings("deprecation")
    public double convert(String from, String to, double amount) throws IOException {

        double result;
        String url_str = "https://v6.exchangerate-api.com/v6/e46d7d25fb4e41bae9710eef/latest/" + from;
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

            if (req_result.equals("success")) {
                JsonObject conversionRates = jsonobj.getAsJsonObject("conversion_rates");
                double rate = conversionRates.get(to).getAsDouble();
                result = amount * rate;
                return result;
            } else {
                throw new IOException("API request was not successful");
            }
        } else {
            throw new IOException("HTTP connection failed with response code: " + responseCode);
        }
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
                "RUB", "SGD", "SEK", "BRL", "IQD", "MAD", "CNY", "MXN", "KWD", "TRY", "TND", "ARS", "LYD", "AUD" };
        firstCurrency.getItems().addAll(currencies);
        secondCurrency.getItems().addAll(currencies);

    }

    public void updateTransferStatisticsChart() {
        try {
            LocalDate startDate = LocalDate.now().minusMonths(1);
            LocalDate endDate = LocalDate.now();
            List<Double> statistics = TransferController.getTransferStatistics(currentUser.getAcc_num(), startDate,
                    endDate);

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Transfer Statistics");

            LocalDate currentDate = startDate;
            for (Double total : statistics) {
                series.getData().add(new XYChart.Data<>(currentDate.toString(), total));
                currentDate = currentDate.plusDays(1);
            }

            transferStatisticsChart.getData().clear();
            transferStatisticsChart.getData().add(series);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private ComboBox<String> stateComboBox;

    @FXML
    private void initialize() {
        stateComboBox.getItems().addAll("New", "In Progress", "Resolved");
    }

    @FXML
    private void showReclamationPane() {
        // Hide all other panes
        homePane.setVisible(false);
        depositPane.setVisible(false);
        withdrawPane.setVisible(false);
        transferPane.setVisible(false);
        converterPane.setVisible(false);
        charityPane.setVisible(false);
        reclamationPane.setVisible(true);

    }

    @FXML
    private void sendReclamation() {
        String title = recieverTextField1.getText();
        String description = recieverTextField2.getText();
        if (title.trim().isEmpty()) {
            recieverTextField2.setText("Please enter your reclamation before submitting.");
            return;
        }

        try {
            String result = reclamationService.submitReclamation(title, description, currentUser);
            System.out.println(result);
            recieverTextField2.setText(result);
            recieverTextField1.clear();
        } catch (RuntimeException e) {
            recieverTextField2.setText("Failed to submit reclamation. Please try again.");
        }
    }

    @FXML
    private void cancelReclamation() {
        recieverTextField1.clear();
        recieverTextField2.clear();
    }

}
