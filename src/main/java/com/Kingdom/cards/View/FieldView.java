package com.Kingdom.cards.View;

import com.Kingdom.cards.Controllers.FieldController;
import com.Kingdom.cards.Model.Board;
import com.Kingdom.cards.Model.Card;
import com.Kingdom.cards.Model.Hand;
import com.Kingdom.cards.PlayerTurn;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class FieldView {

    private FieldController fieldController;
    // Labels showing the number of cards per person
    @FXML
    private Label nmbrOfCardsPlayer1;
    @FXML
    private Label nmbrOfCardsPlayerAI;


    // The horizontal box
    @FXML
    HBox player1Field;
    @FXML
    HBox playerAIField;

    private HBox GetField(PlayerTurn playerTurn) {
        switch (playerTurn) {
            case player1:
                return player1Field;
            case playerAI:
                return playerAIField;
        }
        return null;
    }

    @FXML
    HBox player1Board;
    @FXML
    HBox playerAIBoard;

    @FXML
    Label turnLbl;

    @FXML
    Button btnEndTurn;

    @FXML
    public void initialize() {
        fieldController = new FieldController(this, null);
        if (nmbrOfCardsPlayer1 != null) {
            nmbrOfCardsPlayer1.textProperty().bind(fieldController.getBoard().player1ScoreStr);
        }
        if (nmbrOfCardsPlayerAI != null) {
            nmbrOfCardsPlayerAI.textProperty().bind(fieldController.getBoard().playerAIScoreStr);
        }

        
        GrayButtons(fieldController.playerTurn);

    }


    @FXML
    private void SendCard(ActionEvent event) {
        Button button = (Button) event.getSource();
        int index = button.getParent().getChildrenUnmodifiable().indexOf(button);

        if (!fieldController.playerHasPlay) {
            if (fieldController.playerTurn == PlayerTurn.player1) {
                player1Field.getChildren().remove(button);
            }
        }
        fieldController.SendCard(index);
        GrayButtons(fieldController.playerTurn);
    }

    @FXML
    public void GrayButtons(PlayerTurn pt) {
        if (pt == PlayerTurn.player1) {
            for (Node b : player1Field.getChildren()) {
                b.setDisable(false);
                b.setOpacity(1);
            }
            for (Node b : playerAIField.getChildren()) {
                b.setDisable(true);
                b.setOpacity(0.5);
            }
        } else {
            for (Node b : playerAIField.getChildren()) {
                b.setDisable(true);
                b.setOpacity(0.5);
            }
            for (Node b : player1Field.getChildren()) {
                b.setDisable(true);
                b.setOpacity(0.5);
            }
        }
    }

    @FXML
    public void DrawCard() {
        fieldController.DrawCard();
    }

    @FXML
    public void AddCardToBoard(Card card, PlayerTurn playerTurn) {
        switch (playerTurn) {
            case player1:
                if (card != null) {
                    Button b = card.GetView();
                    b.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent event) {
                            SendCard(event);
                        }
                    });
                    player1Field.getChildren().add(b);
                }
                break;
            case playerAI:
                if (card != null) {
                    Button b = card.GetView();
                    b.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent event) {
                            SendCard(event);
                        }
                    });
                    playerAIField.getChildren().add(b);
                }
                break;
        }
    }


    @FXML
    public void UpdateHands(Hand player1Hand, Hand playerAIHand) {
        player1Field.getChildren().clear();
        playerAIField.getChildren().clear();
        Button b;
        for (int i = 0; i < player1Hand.getHand().size(); i++) {
            b = player1Hand.getHand().get(i).GetView();
            b.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    SendCard(event);
                }
            });
            b.setDisable(false);
            player1Field.getChildren().add(b);
        }
        for (int i = 0; i < playerAIHand.getHand().size(); i++) {
            b = playerAIHand.getHand().get(i).GetView();
            b.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    SendCard(event);
                }
            });
            b.setDisable(false);
            playerAIField.getChildren().add(b);
        }
    }

    @FXML
    public void UpdateBoard(Board board) {
        player1Board.getChildren().clear();
        playerAIBoard.getChildren().clear();
        Button b;
        for (int i = 0; i < board.getPlayer1Cards().size(); i++) {
            b = board.getPlayer1Cards().get(i).GetView();
            b.setDisable(true);
            player1Board.getChildren().add(b);
        }
        for (int i = 0; i < board.getPlayerAICards().size(); i++) {
            b = board.getPlayerAICards().get(i).GetView();
            b.setDisable(true);
            playerAIBoard.getChildren().add(b);
        }
    }

    @FXML
    public void RemoveCardFromHand(Card card, PlayerTurn playerTurn) {
        int cardIndex = -1;
        HBox field = GetField(playerTurn);
        for (int i = 0; i < field.getChildren().size(); i++) {
            Button buti = (Button) field.getChildren().get(i);

            if (card.GetRace().equals(buti.textProperty().getValue())) {
                cardIndex = i;
                break;
            }
        }
        if (cardIndex >= 0) {
            field.getChildren().remove(cardIndex);
        }
    }

    @FXML
    public void EndTurn(ActionEvent actionEvent) {
        fieldController.EndTurn();
    }

    @FXML
    public void ShowEndScreen(boolean playerWin) throws IOException {
        if (playerWin) {
            //Show win screen
            Stage stage = (Stage) player1Field.getScene().getWindow();
            Parent winScene = FXMLLoader.load(getClass().getResource("/fxml/WinView.fxml"));
            Scene scene = new Scene(winScene);
            if (stage != null) {
                stage.setScene(scene);
                stage.setFullScreen(true);
                stage.show();
            }
        } else {
            //Show lose screen
            Stage stage = (Stage) player1Field.getScene().getWindow();
            Parent winScene = FXMLLoader.load(getClass().getResource("/fxml/EndView.fxml"));
            Scene scene = new Scene(winScene);
            if (stage != null) {
                stage.setScene(scene);
                stage.setFullScreen(true);
                stage.show();
            }
        }
    }

    @FXML
    public void keyPressed(KeyEvent keyEvent) throws IOException {
        switch (keyEvent.getCode()) {
            case ESCAPE:
                int maxWidth = 128;
                int maxHeight = 128;
                Window window = ((AnchorPane) keyEvent.getSource()).getScene().getWindow();
                final Popup popup = new Popup();
                popup.setX(window.getWidth() / 2);
                popup.setY(window.getHeight() / 2);

                Button quit = new Button("Quit");
                quit.setPrefSize(maxWidth, 64);
                Button resume = new Button("Resume");
                resume.setPrefSize(maxWidth, 64);

                quit.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        Platform.exit();
                    }
                });

                resume.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        popup.hide();
                    }
                });

                VBox box = new VBox();
                box.setPrefSize(maxWidth, maxHeight);
                box.setStyle("-fx-background-image: url('fxml/ingameMenuBackground.png'); " +
                        "-fx-background-position: center center; " +
                        "-fx-background-repeat: stretch;");
                box.getChildren().addAll(resume, quit);

                popup.getContent().add(box);
                popup.show(window);
                break;
        }
    }
}

