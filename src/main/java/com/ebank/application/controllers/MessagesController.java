package com.ebank.application.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
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
import java.net.URI;
import java.time.LocalDateTime;

import com.ebank.application.models.Discution;
import com.ebank.application.models.Message;
import com.ebank.application.services.DiscutionService;
import com.ebank.application.services.MessageService;
import com.ebank.application.websocket.ChatWebSocketClient;
import org.json.JSONObject;

public class MessagesController {

    @FXML
    private TextField roomName;

    @FXML
    private ListView<Discution> roomList;

    @FXML
    private TextField messageContent;

    @FXML
    private ListView<Message> messageList;

    private DiscutionService discutionService = new DiscutionService();
    private MessageService messageService = new MessageService();

    private Discution currentRoom;

    private static final int USER_ID_1 = 2;
    private static final int USER_ID_2 = 1;

    private Image avatarUser1;
    private Image avatarUser2;

    private ChatWebSocketClient webSocketClient;

    @FXML
    public void initialize() {
        avatarUser1 = new Image(getClass().getResource("/com/ebank/application/icons/avatar1.jpeg").toString());
        avatarUser2 = new Image(getClass().getResource("/com/ebank/application/icons/avatar2.jpg").toString());

        refreshRooms();
        setupMessageListCellFactory();
        setupRoomListCellFactory();

        roomList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                currentRoom = newSelection;
                refreshMessages();
            }
        });

        initializeWebSocket();
        startPeriodicRefresh();
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

    private void handleIncomingMessage(String message) {
        System.out.println("Received raw message: " + message);
        Platform.runLater(() -> {
            Message newMessage = parseMessage(message);
            if (newMessage != null) {
                System.out.println("Parsed message: " + newMessage);
                updateMessageList(newMessage);
                messageService.add(newMessage);
            } else {
                System.out.println("Failed to parse message");
            }
        });
    }

    private void updateMessageList(Message newMessage) {
        if (currentRoom != null && newMessage.getIdDiscution() == currentRoom.getId()) {
            messageList.getItems().add(newMessage);
            messageList.scrollTo(messageList.getItems().size() - 1);
        }
    }

    @FXML
    public void sendMessage() {
        String content = messageContent.getText();
        if (content != null && !content.isEmpty() && currentRoom != null) {
            Message message = new Message(content, LocalDateTime.now(), currentRoom.getId(), USER_ID_1, USER_ID_2);
            sendMessageToServer(message);
            messageContent.clear();
        }
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
                        currentRoom != null ? currentRoom.getId() : 0,
                        USER_ID_2,
                        USER_ID_1);
            }
        } catch (Exception e) {
            System.out.println("Error parsing message: " + messageString);
            e.printStackTrace();
            return null;
        }
    }

    private void startPeriodicRefresh() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> refreshMessages()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void refreshRooms() {
        roomList.getItems().setAll(discutionService.getAll());
        if (!roomList.getItems().isEmpty()) {
            roomList.getSelectionModel().select(0);
            currentRoom = roomList.getItems().get(0);
            refreshMessages();
        }
    }

    private void refreshMessages() {
        if (currentRoom != null) {
            messageList.getItems().setAll(messageService.getAll().stream()
                    .filter(m -> m.getIdDiscution() == currentRoom.getId())
                    .toList());
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

                    if (item.getIdEmetteur() == USER_ID_1) {
                        avatarImageView.setImage(avatarUser1);
                        messageBox.getChildren().addAll(messageText, timeText);
                        hbox.getChildren().addAll(avatarImageView, messageBox, spacer);
                        messageBox.getStyleClass().addAll("message-bubble", "message-left");
                        messageBox.setAlignment(Pos.CENTER_LEFT);
                    } else if (item.getIdEmetteur() == USER_ID_2) {
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

    private void setupRoomListCellFactory() {
        roomList.setCellFactory(param -> new ListCell<Discution>() {
            @Override
            protected void updateItem(Discution item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNom());
                }
            }
        });
    }
}