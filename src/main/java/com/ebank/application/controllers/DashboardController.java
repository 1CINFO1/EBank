package com.ebank.application.controllers;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.ResourceBundle;

import com.ebank.application.models.CharityCampaignModel;
import com.ebank.application.models.Publication;
import com.ebank.application.models.User;
import com.ebank.application.services.ICharityService;
import com.ebank.application.services.IpublicationImple;
import com.ebank.application.services.TransfertService;
import com.ebank.application.utils.EmailUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;

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
    private Button backButton2;
    @FXML
    private Pane publicationPane;
    @FXML
    private Pane charityPane;

    @FXML
    private VBox publicationListVBox;

    @FXML
    private Label title;
    @FXML
    private Label campaignName;
    @FXML
    private Label pubDescription;
    @FXML
    private Label pubPicture;

    private int selectedPublicationId;


    @FXML
    private VBox ListPublicationsCards;

    @FXML
    private Pane getAllPubList;

    private IpublicationImple publicationService;
    @FXML
    private TableView<Publication> publicationTable;

    @FXML
    private TableColumn<Publication, String> charityNameColumn;

    @FXML
    private TableColumn<Publication, String> publicationTitleColumn;

    @FXML
    private TableColumn<Publication, String> publicationDescriptionColumn;

    @FXML
    private TableColumn<Publication, Void> actionColumn;



@FXML
private JScrollPane ListPublications;
    public DashboardController() {
        // Initialize the service
        this.publicationService = new IpublicationImple();

    }

    private final TransfertService transfertService = new TransfertService();

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
                    ImageView publicationImageView = new ImageView(new Image(pub.getPicture()));
                    Label publicationLabel = new Label(
                            "Title: " + pub.getTitle() + "\n" +
                                    "Campaign Name: " + pub.getCampaignName() + "\n" +
                                    "Description: " + pub.getDescription() + "\n" +
                                    "Publication Date: " + pub.getPublicationDate() + "\n" +
                                    "Picture: " + publicationImageView + "\n");

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

    private void loadPublications() {
    List<Publication> publications = publicationService.getAll();

        // Clear existing content
        ListPublicationsCards.getChildren().clear();

        // Create publication cards
        for (Publication publication : publications) {
            HBox publicationCard = createPublicationCard(publication);
            ListPublicationsCards.getChildren().add(publicationCard);
        }
    }

    private HBox createPublicationCard(Publication publication) {
        HBox cardLayout = new HBox();
        cardLayout.setSpacing(10);
        cardLayout.setPadding(new Insets(10));
        cardLayout.setStyle("-fx-border-color: #ddd; -fx-border-radius: 5; -fx-background-color: #f9f9f9;");

        VBox textLayout = new VBox();
        textLayout.setSpacing(10);

        Label charityNameLabel = new Label(publication.getCampaignName());
        charityNameLabel.setFont(Font.font("System Bold", 23.0));

        Label publicationTitleLabel = new Label(publication.getTitle());
        publicationTitleLabel.setFont(Font.font("System Bold", 21.0));

        Label publicationDescriptionLabel = new Label(publication.getDescription());
        publicationDescriptionLabel.setFont(Font.font(14.0));

        textLayout.getChildren().addAll(charityNameLabel, publicationTitleLabel, publicationDescriptionLabel);

        ImageView publicationImageView = new ImageView(new Image(publication.getPicture()));
        publicationImageView.setFitWidth(200.0);
        publicationImageView.setFitHeight(183.0);
        publicationImageView.setPreserveRatio(true);
        HBox cardLayouts = new HBox();

        Button donateButton = new Button("Donate");
        donateButton.setOnMouseClicked(event -> {
            // Handle donate action
            System.out.println("Donating to publication ID: " + publication.getId());
            selectedPublicationId = publication.getId();
            showcharityByIdPane(selectedPublicationId);
        });

        cardLayout.getChildren().addAll(textLayout, publicationImageView, donateButton);
        HBox.setHgrow(textLayout, Priority.ALWAYS); // Ensure textLayout grows horizontally if needed

        return cardLayout;
    }

    @FXML
    void showCharityPane() {
        homePane.setVisible(false);
        depositPane.setVisible(false);
        withdrawPane.setVisible(false);
        transferPane.setVisible(false);
        converterPane.setVisible(false);
       // charityPane.setVisible(true);
        getAllPubList.setVisible(true);
        publicationPane.setVisible(false);
        transferCharityPane.setVisible(false);
        loadPublications();
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
        //charityPane.setVisible(false);
        publicationPane.setVisible(true);
        transferCharityPane.setVisible(false);
        getAllPubList.setVisible(false);
        publicationById(id);

    }

    @FXML
    void showDepositPane() {
        homePane.setVisible(false);
        depositPane.setVisible(true);
        withdrawPane.setVisible(false);
        transferPane.setVisible(false);
        converterPane.setVisible(false);
       // charityPane.setVisible(false);
        getAllPubList.setVisible(false);
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
        // charityPane.setVisible(false);
        getAllPubList.setVisible(false);
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
        // charityPane.setVisible(false);
        publicationPane.setVisible(false);
        transferCharityPane.setVisible(true);
        getAllPubList.setVisible(false);
    }

    @FXML
    void showTransferPane() {
        homePane.setVisible(false);
        depositPane.setVisible(false);
        withdrawPane.setVisible(false);
        transferPane.setVisible(true);
        converterPane.setVisible(false);
        //charityPane.setVisible(false);
        getAllPubList.setVisible(false);
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
        //charityPane.setVisible(false);
        getAllPubList.setVisible(false);
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
        //charityPane.setVisible(false);
        getAllPubList.setVisible(false);
        publicationPane.setVisible(false);
        transferCharityPane.setVisible(false);
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
    public void confirmTransfer2() {
        try {
            double amount = Double.parseDouble(transferAmountTextField2.getText());

            Publication publication = publicationService.getById(selectedPublicationId);
            int charityId = publication.getCompagnieDeDon_Patente();
            CharityCampaignModel campaignModel = charityService.getCharityBy(charityId);

            String receiverAccNumber = Integer.toString(campaignModel.getAcc_num());
            String receiverEmail = campaignModel.getEmail();

            transfertService.transfer2(amount, receiverAccNumber, currentUser);
            System.out.println(currentUser.getEmail());
            System.out.println(receiverEmail);

            // Send confirmation email to the user
            String userSubject = "Transfer Confirmation";
            String userBody = "Dear " + currentUser.getName() + ",\n\nYour transfer of $" + amount + " to " + campaignModel.getName() + " has been successful.\n\nThank you for your generosity!\n\nBest regards,\nYour Charity App Team";
            sendMimeEmail(currentUser.getEmail(), userSubject, userBody);

            // Send notification email to the charity
            String charitySubject = "You Received a Donation";
            String charityBody = "Dear " + campaignModel.getName() + ",\n\nYou have received a donation of $" + amount + " from " + currentUser.getName() + ".\n\nBest regards,\nYour Charity App Team";
            sendMimeEmail(campaignModel.getEmail(), charitySubject, charityBody);

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
        } catch (Exception e) {
            e.printStackTrace();
            transferConfirmationText.setText("An error occurred");
            transferConfirmationText.setStyle(errorStyle);
        }
    }

    private void sendMimeEmail(String toEmail, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("saif.juini6", "Option123");
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("saif.juini6@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

            System.out.println("MimeMessage Email sent successfully.");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
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
        String url_str = "https://v6.exchangerate-api.com/v6/102db8a095627d3b05f54c7a/convert?from=" + from + "&to="
                + to;
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
