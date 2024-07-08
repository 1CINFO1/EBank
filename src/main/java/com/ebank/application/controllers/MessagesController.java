package com.ebank.application.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.LocalDateTime;

import com.ebank.application.models.*;
import com.ebank.application.services.*;

import com.ebank.application.websocket.ChatWebSocketClient;
import org.json.JSONObject;

public class MessagesController {

    @FXML
    private TextField roomName;

    @FXML
    private TextField messageContent;

    @FXML
    private ListView<Message> messageList;

    private DiscutionService discutionService = new DiscutionService();
    private MessageService messageService = new MessageService();

    private Discution currentRoom;

    private Image avatarUser1;
    private Image avatarUser2;

    private ChatWebSocketClient webSocketClient;
    @FXML
    private ListView<AdminUser> userList;

    private AdminUserService adminUserService = new AdminUserService();

    private Discution currentDiscussion;
    private AdminUser selectedUser;

    public AdminUser currentUser;

    public void setCurrentUser(AdminUser currentUser) {
        this.currentUser = currentUser;
        System.out.println("Setting current user in msg controller: " + currentUser.getId());
        refreshUserList();
    }

    private Timeline timeline;
    @FXML
    private HBox inputContainer;

    @FXML
    public void initialize() {
        avatarUser1 = new Image(getClass().getResource("/com/ebank/application/icons/avatar1.png").toString());
        avatarUser2 = new Image(getClass().getResource("/com/ebank/application/icons/avatar2.png").toString());
        inputContainer.setVisible(false);
        inputContainer.setManaged(false);
        setupMessageListCellFactory();
        setupUserListCellFactory();
        userList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedUser = newSelection;
                loadOrCreateDiscussion();
                showInputFields();
            } else {
                hideInputFields();
            }
        });
        initializeWebSocket();
        startPeriodicRefresh();
    }

    private String moderateContent(String message) {
        String apiKey = "01907ecbbfc312de522ffcfa1603ecd5"; // Replace with your actual API key
        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
        String url = "https://api.moderatecontent.com/text/?key=" + apiKey + "&msg=" + encodedMessage;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonResponse = new JSONObject(response.body());
            return jsonResponse.getString("clean");
        } catch (Exception e) {
            e.printStackTrace();
            return message; // Return original message if moderation fails
        }
    }

    private void showInputFields() {
        inputContainer.setVisible(true);
        inputContainer.setManaged(true);
    }

    private void hideInputFields() {
        inputContainer.setVisible(false);
        inputContainer.setManaged(false);
    }

    private void initializeWebSocket() {
        try {
            URI serverUri = new URI("ws://localhost:8887");
            webSocketClient = new ChatWebSocketClient(serverUri) {
                @Override
                public void onMessage(String message) {
                    handleIncomingMessage(message);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    super.onClose(code, reason, remote);
                    Platform.runLater(() -> reconnectWebSocket());
                }
            };
            webSocketClient.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reconnectWebSocket() {
        // Implement reconnection logic with exponential backoff
        System.out.println("WebSocket disconnected. Attempting to reconnect...");
        // Add your reconnection logic here
    }

    private void refreshUserList() {
        List<AdminUser> allAdminUsers = new ArrayList<>(); // Initialize with an empty list
        try {
            allAdminUsers = adminUserService.getAllExcept(currentUser.getId());
        } catch (SQLException e) {
            e.printStackTrace();
            // Log the error or show an alert to the user
            System.err.println("Error fetching admin users: " + e.getMessage());
            // Optionally, show an alert to the user
            // Platform.runLater(() -> showAlert("Error", "Failed to fetch user list. Please
            // try again later."));
        }
        userList.getItems().setAll(allAdminUsers);
    }

    private void loadOrCreateDiscussion() {
        if (currentUser == null || selectedUser == null) {
            System.err.println("Error: currentUser or selectedUser is null");
            return;
        }

        try {
            currentDiscussion = discutionService.getDiscussionBetweenUsers(currentUser.getId(), selectedUser.getId());
            if (currentDiscussion == null) {
                hideInputFields();

                String discussionName = "Chat between " + currentUser.getName() + " and " + selectedUser.getName();
                currentDiscussion = new Discution(discussionName, LocalDateTime.now());
                int discussionId = discutionService.add(currentDiscussion);
                if (discussionId == -1) {
                    throw new Exception("Failed to create new discussion.");
                }
                currentDiscussion.setId(discussionId); // Set the generated ID
                // Link the users to this discussion
                discutionService.linkUsersToDiscussion(discussionId, currentUser.getId(), selectedUser.getId());
            } else {
                showInputFields();

            }
            System.out.println("Current discussion loaded: " + currentDiscussion.getId());
            currentRoom = currentDiscussion; // Ensure currentRoom is set
            refreshMessages();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading or creating discussion: " + e.getMessage());
            Platform.runLater(() -> showAlert("Error", "Failed to load or create discussion. Please try again."));
        }
    }

    private void handleIncomingMessage(String message) {
        System.out.println("Received raw message: " + message);
        Platform.runLater(() -> {
            Message newMessage = parseMessage(message);
            if (newMessage != null) {
                // Moderate the incoming message
                String moderatedContent = moderateContent(newMessage.getContenu());
                newMessage.setContenu(moderatedContent);

                System.out.println("Parsed message: " + newMessage);
                try {
                    String saved = messageService.add(newMessage);
                    if (saved == "added") {
                        System.out.println("Message saved successfully");
                        refreshMessages(); // Refresh the message list
                    } else {
                        System.out.println("Failed to save message");
                    }
                } catch (Exception e) {
                    System.err.println("Error saving message: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("Failed to parse message");
            }
        });
    }

    @FXML
    public void sendMessage() {
        String content = messageContent.getText();
        if (content != null && !content.isEmpty() && currentDiscussion != null) {
            // Moderate the content
            String moderatedContent = moderateContent(content);

            Message message = new Message(moderatedContent, LocalDateTime.now(), currentDiscussion.getId(),
                    currentUser.getId(),
                    selectedUser.getId());
            System.out.println("Attempting to send message: " + message);

            try {
                String saved = messageService.add(message);
                if (saved == "added") {
                    System.out.println("Message saved locally");
                    sendMessageToServer(message);
                    messageContent.clear();
                    refreshMessages(); // Refresh the message list
                } else {
                    System.out.println("Failed to save message locally");
                }
            } catch (Exception e) {
                System.err.println("Error saving message: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Message content is empty or current discussion is null.");
        }
    }

    private void setupUserListCellFactory() {
        userList.setCellFactory(param -> new ListCell<AdminUser>() {
            @Override
            protected void updateItem(AdminUser item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
    }

    private void sendMessageToServer(Message message) {
        if (webSocketClient != null && webSocketClient.isOpen()) {
            try {
                JSONObject jsonMessage = new JSONObject();
                jsonMessage.put("type", "CHAT_MESSAGE");
                jsonMessage.put("contenu", message.getContenu());
                jsonMessage.put("dateEnvoi", message.getDateEnvoi().toString());
                jsonMessage.put("idDiscution", message.getIdDiscution());
                jsonMessage.put("idEmetteur", message.getIdEmetteur());
                jsonMessage.put("idRecepteur", message.getIdRecepteur());
                String jsonString = jsonMessage.toString();
                System.out.println("Sending message: " + jsonString);
                webSocketClient.send(jsonString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("WebSocket is not connected. Message not sent.");
        }
    }

    private Message parseMessage(String messageString) {
        try {
            if (messageString.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(messageString);
                return new Message(
                        jsonObject.getString("contenu"),
                        LocalDateTime.parse(jsonObject.getString("dateEnvoi")),
                        jsonObject.getInt("idDiscution"),
                        jsonObject.getInt("idEmetteur"),
                        jsonObject.getInt("idRecepteur"));
            } else {
                System.out.println("Received non-JSON message: " + messageString);
                return new Message(
                        messageString,
                        LocalDateTime.now(),
                        currentDiscussion != null ? currentDiscussion.getId() : 0,
                        currentUser.getId(),
                        selectedUser.getId());
            }
        } catch (Exception e) {
            System.out.println("Error parsing message: " + messageString);
            e.printStackTrace();
            return null;
        }
    }

    private void startPeriodicRefresh() {
        if (timeline != null) {
            timeline.stop();
        }
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> refreshMessages()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void refreshMessages() {
        if (currentRoom != null) {
            List<Message> messages = messageService.getAll().stream()
                    .filter(m -> m.getIdDiscution() == currentRoom.getId())
                    .toList();
            Platform.runLater(() -> {
                messageList.getItems().setAll(messages);
                if (!messages.isEmpty()) {
                    messageList.scrollTo(messages.size() - 1);
                }
            });
        }
    }

    private void setupMessageListCellFactory() {
        messageList.setCellFactory(param -> new ListCell<Message>() {
            @Override
            protected void updateItem(Message item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(10);
                    VBox messageBox = new VBox(5);
                    Text messageText = new Text(item.getContenu());
                    Text timeText = new Text(
                            item.getDateEnvoi().format(DateTimeFormatter.ofPattern("dd/MMM/yyyy HH:mm")));

                    messageText.wrappingWidthProperty().bind(messageList.widthProperty().subtract(100));
                    timeText.getStyleClass().add("time-text");

                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);

                    ImageView avatarImageView = new ImageView();
                    avatarImageView.setFitWidth(40);
                    avatarImageView.setFitHeight(40);

                    if (item.getIdEmetteur() == currentUser.getId()) {
                        avatarImageView.setImage(avatarUser1);
                        messageBox.getChildren().addAll(messageText, timeText);
                        hbox.getChildren().addAll(avatarImageView, messageBox, spacer);
                        messageBox.getStyleClass().addAll("message-bubble", "message-left");
                        messageBox.setAlignment(Pos.CENTER_LEFT);
                    } else {
                        avatarImageView.setImage(avatarUser2);
                        messageBox.getChildren().addAll(messageText, timeText);
                        hbox.getChildren().addAll(spacer, messageBox, avatarImageView);
                        messageBox.getStyleClass().addAll("message-bubble", "message-right");
                        messageBox.setAlignment(Pos.CENTER_RIGHT);
                    }

                    setGraphic(hbox);
                }
            }
        });
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}