package com.ebank.application.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import java.time.format.DateTimeFormatter;

import com.ebank.application.models.Discution;
import com.ebank.application.models.Message;
import com.ebank.application.services.DiscutionService;
import com.ebank.application.services.MessageService;

import java.time.LocalDateTime;

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
    }

    @FXML
    public void sendMessage() {
        String content = messageContent.getText();
        if (content != null && !content.isEmpty() && currentRoom != null) {
            Message message = new Message(content, LocalDateTime.now(), currentRoom.getId(), USER_ID_1, USER_ID_2);
            messageService.add(message);
            refreshMessages();
            messageContent.clear();
        }
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
