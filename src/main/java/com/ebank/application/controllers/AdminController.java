package com.ebank.application.controllers;

import com.ebank.application.models.AdminUser;
import com.ebank.application.models.Publication;
import com.ebank.application.services.AdminService;
import com.ebank.application.services.IpublicationImple;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

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

    @FXML
    private Button messagesButton;
    @FXML
    private Pane publicationPane;

    @FXML
    private TableView<Publication> publicationTableList;

    @FXML
    private TableColumn<Publication, String> column1;

    @FXML
    private TableColumn<Publication, String> column2;

    @FXML
    private TableColumn<Publication, String> column3;

    @FXML
    private TableColumn<Publication, HBox> column4;

    @FXML
    private Pane stasPaneId;

    @FXML
    private BarChart<String, Number> statisticsChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private Pane messagerPane;

    private final IpublicationImple ipublicationImple = new IpublicationImple();
    private final AdminService adminService = new AdminService();

    protected String errorStyle = "-fx-text-fill: RED;";
    String successStyle = "-fx-text-fill: GREEN;";

    public AdminUser currentUser = new AdminUser();

    ResultSet rs = null;

    @FXML
    void showMessagerPane() {
        homePane.setVisible(false);
        messagerPane.setVisible(true);
        publicationPane.setVisible(false);
        stasPaneId.setVisible(false);
    }

    @FXML
    public void showHomePane() {
        homePane.setVisible(true);
        messagerPane.setVisible(false);
        publicationPane.setVisible(false);
        stasPaneId.setVisible(false);
        setLabels();
    }

    public void showPublicationPane() {
        homePane.setVisible(false);
        messagerPane.setVisible(false);
        publicationPane.setVisible(true);
        stasPaneId.setVisible(false);
        handleLoadPublications();

    }

    @FXML
    void showStatisticsPane(ActionEvent event) {
        // Hide other panes if needed
        homePane.setVisible(false);
        messagerPane.setVisible(false);
        publicationPane.setVisible(false);

        // Show statistics pane
        stasPaneId.setVisible(true);

        // Update axes labels
        CategoryAxis xAxis = (CategoryAxis) statisticsChart.getXAxis();
        xAxis.setLabel("Campaign Names");

        NumberAxis yAxis = (NumberAxis) statisticsChart.getYAxis();
        yAxis.setLabel("Number of Publications");

        // Populate statistics chart with data
        showStatistics();
    }

    private void showStatistics() {
        // Clear any previous data in the chart
        statisticsChart.getData().clear();

        // Example: Get the publication counts per campaign from your adminService
        Map<String, Integer> publicationCounts = adminService.getPublicationCountPerCampaign();

        // Create a series to hold the data
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        // Populate the series with data
        publicationCounts.forEach((campaignName, count) -> {
            series.getData().add(new XYChart.Data<>(campaignName, count));
        });

        // Add series to chart
        statisticsChart.getData().add(series);
    }

    @FXML
    private void handleLoadPublications() {
        column1.setCellValueFactory(new PropertyValueFactory<>("title"));
        column2.setCellValueFactory(new PropertyValueFactory<>("description"));
        column3.setCellValueFactory(new PropertyValueFactory<>("campaignName"));
        column4.setCellValueFactory(new PropertyValueFactory<>("action"));

        loadPublications();
    }

    private void loadPublications() {
        ObservableList<Publication> publications = FXCollections.observableArrayList();
        for (Publication publication : adminService.getAll()) {
            HBox actionButtons = createActionButtons(publication);
            publication.setAction(actionButtons);
            publications.add(publication);
        }
        publicationTableList.setItems(publications);
    }

    private HBox createActionButtons(Publication publication) {
        Button approveButton = new Button("Approve");
        approveButton.getStyleClass().add("action-button");
        approveButton.setOnAction(event -> {
            int id = publication.getId();
            System.out.println(id);
            adminService.approvePublication(id);
            loadPublications();
        });

        Button declineButton = new Button("Decline");
        declineButton.getStyleClass().addAll("action-button", "decline");
        declineButton.setOnAction(event -> {
            int id = publication.getId();
            System.out.println(id);
            adminService.declinePublication(id);
            loadPublications();
        });

        HBox hbox = new HBox(approveButton, declineButton);
        hbox.getStyleClass().add("hbox-actions");
        return hbox;
    }

    public void setLabels() {
        name.setText(currentUser.getName());
        dob.setText(currentUser.getDob().toString());
        accNumber.setText(Integer.toString(currentUser.getAcc_num()));
        balance.setText(String.format("%.2f", currentUser.getBalance()) + "$");
        emailLabel.setText(currentUser.getEmail());

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

    public void updatePublication(@SuppressWarnings("exports") ActionEvent actionEvent) {
    }

    public void addPublication(@SuppressWarnings("exports") ActionEvent actionEvent) {
    }

    public void deletePublication(@SuppressWarnings("exports") ActionEvent actionEvent) {
    }

    @FXML
    private Button jobsButton;

    @FXML
    private void handleJobsButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ebank/application/jobListViewAdmin.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Job List");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMessagesView(AdminUser u) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ebank/application/messages_view.fxml"));
            Pane messagesView = loader.load();
            MessagesController mController = loader.getController();

            mController.setCurrentUser(u);

            messagerPane.getChildren().add(messagesView);

            // Bind the size of the loaded view to the messagerPane
            messagesView.prefWidthProperty().bind(messagerPane.widthProperty());
            messagesView.prefHeightProperty().bind(messagerPane.heightProperty());
        } catch (IOException e) {
            e.printStackTrace();
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}