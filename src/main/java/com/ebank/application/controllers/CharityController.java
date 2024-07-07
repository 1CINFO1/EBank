package com.ebank.application.controllers;

import com.ebank.application.models.CharityCampaignModel;
import com.ebank.application.models.Publication;
import com.ebank.application.models.User;
import com.ebank.application.services.ICharityService;
import com.ebank.application.services.IpublicationImple;
import com.ebank.application.services.TransferService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class CharityController implements Initializable {

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

    @FXML
    private Pane charityPane;

    @FXML
    private VBox publicationListVBox;

    // private final ConverterService transfertService = new ConverterService();
    private final TransferService transferService = new TransferService();

    @FXML
    private Pane createPublicationPane;

    @FXML
    private Pane publicationListPane;

    @FXML
    private TableView<Publication> publicationTableView;
    @FXML
    private TableColumn<Publication, Integer> idColumn;
    @FXML
    private TableColumn<Publication, String> patenteColumn;
    @FXML
    private TableColumn<Publication, String> titleColumnid;
    @FXML
    private TableColumn<Publication, String> campaignNameColumn;
    @FXML
    private TableColumn<Publication, String> descriptionColumn;
    @FXML
    private TableColumn<Publication, String> pictureColumn;
    @FXML
    private TableColumn<Publication, Date> publicationDateColumn;

    @FXML
    private ListView<VBox> publicationListView;

    private final TransfertService transfertService = new TransfertService();
    private final IpublicationImple ipublicationImple = new IpublicationImple();
    private final ICharityService iCharityService = new ICharityService();

    protected String errorStyle = "-fx-text-fill: RED;";
    String successStyle = "-fx-text-fill: GREEN;";

    public CharityCampaignModel currentUser = new CharityCampaignModel();

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
        createPublicationPane.setVisible(false);
        publicationListPane.setVisible(false);
    }

    @FXML
    public void showHomePane() {
        homePane.setVisible(true);
        depositPane.setVisible(false);
        withdrawPane.setVisible(false);
        transferPane.setVisible(false);
        converterPane.setVisible(false);
        createPublicationPane.setVisible(false);
        publicationListPane.setVisible(false);
        setLabels();
    }

    @FXML
    void showTransferPane() {
        homePane.setVisible(false);
        depositPane.setVisible(false);
        withdrawPane.setVisible(false);
        transferPane.setVisible(true);
        converterPane.setVisible(false);
        createPublicationPane.setVisible(false);
        publicationListPane.setVisible(false);

    }

    @FXML
    void showWithdrawPane() {
        homePane.setVisible(false);
        depositPane.setVisible(false);
        withdrawPane.setVisible(true);
        transferPane.setVisible(false);
        converterPane.setVisible(false);
        createPublicationPane.setVisible(false);
        publicationListPane.setVisible(false);
    }

    @FXML
    void showConverterPane() {
        homePane.setVisible(false);
        depositPane.setVisible(false);
        withdrawPane.setVisible(false);
        transferPane.setVisible(false);
        converterPane.setVisible(true);
        createPublicationPane.setVisible(false);
        publicationListPane.setVisible(false);

    }

    @FXML
    public void showPublicationForm() {
        homePane.setVisible(false);
        depositPane.setVisible(false);
        withdrawPane.setVisible(false);
        transferPane.setVisible(false);
        converterPane.setVisible(false);
        createPublicationPane.setVisible(true);
        publicationListPane.setVisible(false);
    }

    @FXML
    public void showListPublication() {
        homePane.setVisible(false);
        depositPane.setVisible(false);
        withdrawPane.setVisible(false);
        transferPane.setVisible(false);
        converterPane.setVisible(false);
        createPublicationPane.setVisible(false);
        publicationListPane.setVisible(true);

        showUserPublications();

    }

    public void getAllPublication() {
        try {
            List<Publication> publications = ipublicationImple.getAll();
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
                        // Handle donation action, e.g., open donation form or process donation
                        System.out.println("Clicked Donate for publication ID: " + pub.getId());
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
    public void confirmTransfer() {
        try {
            double amount = Double.parseDouble(transferAmountTextField.getText());
            String receiverAccNumber = recieverTextField.getText();
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

    public void showUserPublications() {
        try {
            // Parse the compagnieDeDonPatente from string to int
            int compagnieDeDonPatente = Integer.parseInt(currentUser.getCompagnieDeDon_Patente());

            // Get the publications for the user
            List<Publication> publications = iCharityService.getByCharityId(compagnieDeDonPatente);

            // Initialize the TableView columns

            titleColumnid.setCellValueFactory(new PropertyValueFactory<>("title"));
            campaignNameColumn.setCellValueFactory(new PropertyValueFactory<>("campaignName"));
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            pictureColumn.setCellValueFactory(new PropertyValueFactory<>("picture"));
            publicationDateColumn.setCellValueFactory(new PropertyValueFactory<>("publicationDate"));

            // Create the actions column
            TableColumn<Publication, Void> actionsColumn = new TableColumn<>("Actions");
            actionsColumn.setPrefWidth(200); // Adjust width as needed
            actionsColumn.setStyle("-fx-alignment: CENTER;");

            actionsColumn.setCellFactory(param -> new TableCell<>() {
                private final Button editButton = new Button("Edit");
                private final Button deleteButton = new Button("Delete");

                {
                    editButton.setOnAction(event -> {
                        Publication publication = getTableView().getItems().get(getIndex());
                        // Handle edit action here
                        System.out.println("Editing publication: " + publication.getId());
                    });
                    editButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");

                    deleteButton.setOnAction(event -> {
                        Publication publication = getTableView().getItems().get(getIndex());
                        // Handle delete action here
                        System.out.println("Deleting publication: " + publication.getId());
                    });
                    deleteButton
                            .setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;");

                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        HBox buttons = new HBox(10); // Adjust spacing between buttons if needed
                        buttons.getChildren().addAll(editButton, deleteButton);
                        setGraphic(buttons);
                    }

                }
            });

            // Add the actions column to the TableView if it's not already added
            if (!publicationTableView.getColumns().contains(actionsColumn)) {
                publicationTableView.getColumns().add(actionsColumn);
            }

            // Clear any existing items in the TableView
            publicationTableView.getItems().clear();

            if (publications != null && !publications.isEmpty()) {
                // Add the publications to the TableView
                ObservableList<Publication> publicationObservableList = FXCollections.observableArrayList(publications);
                publicationTableView.setItems(publicationObservableList);
            } else {
                System.out.println("No publications found.");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace(); // Handle the exception properly
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
                "RUB", "SGD", "SEK", "BRL", "IQD", "MAD", "CNY", "MXN", "KWD", "TRY", "TND", "ARS", "LYD", "AUD" };
        firstCurrency.getItems().addAll(currencies);
        secondCurrency.getItems().addAll(currencies);
    }

    public void updatePublication(ActionEvent actionEvent) {
    }

    public void addPublication(ActionEvent actionEvent) {
    }

    public void deletePublication(ActionEvent actionEvent) {
    }
}
