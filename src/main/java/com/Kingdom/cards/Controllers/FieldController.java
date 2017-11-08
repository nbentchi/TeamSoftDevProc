package com.Kingdom.cards.Controllers;

import com.Kingdom.cards.AI;
import com.Kingdom.cards.Deck;
import com.Kingdom.cards.Model.Board;
import com.Kingdom.cards.Model.Card;
import com.Kingdom.cards.Model.Player;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.Random;

public class FieldController {

	/*
	 * Kingdom cards! A card game about a growing population. Generate a Deck,
	 * the deck contains 7 cards of each race. Each player draws 1 card till
	 * both players have 5 cards. Flip a coin to see who starts The first player
	 * draws a card. He plays a card. Both are obligatory The players turn then
	 * ends. The game ends when all the cards are played.
	 *
	 * We will be using a sort of State machine to control the turns of each
	 * player.
	 *
	 * There are 3 states in the game: 1) Init, shuffle and first 5 card draw 2)
	 * Gameplay 3) End game
	 */

	// The horizontal box
	@FXML
	HBox player1Field;
	@FXML
	HBox playerAIField;

	@FXML
	HBox player1Board;
	@FXML
	HBox playerAIBoard;

	@FXML
	Label turnLbl;

	@FXML
	Button btnEndTurn;

	// The deck of cards
	private Deck deck;

	// Board containing all the cards
	private Board board;

	// Player
	private Player player1 = new Player();
	private AI playerAI = new AI();

	// Number of cards per player
	private int nmbrOfCardsInit = 5;

	// Player turn state variable
	public enum PlayerTurn {
		player1, playerAI
	}

	private PlayerTurn playerTurn;
	private boolean playerHasDrawn = false;
	private boolean playerHasPlay = false;

	// The state of the game, where we are.
	public enum GameState {
		init, game, end
	}

	private GameState gamestate;

	// Labels showing the number of cards per person
	@FXML
	private Label nmbrOfCardsPlayer1;
	@FXML
	private Label nmbrOfCardsPlayerAI;

	@FXML
	public void initialize() {
		gamestate = GameState.init;
		board = new Board();
		// Generate the deck of cards
		deck = new Deck(7);
		// Shuffle the deck of cards
		deck.Shuffle();

		nmbrOfCardsPlayer1.textProperty().bind(board.player1Score_2);
		nmbrOfCardsPlayerAI.textProperty().bind(board.playerAIScore_2);

		Card c;
		Button b;
		for (int i = 0; i < nmbrOfCardsInit; i++) {
			c = player1.Draw(deck);
			b = new Button(c.GetRace());
			b.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					SendCard(event);
				}
			});
			player1Field.getChildren().add(b);
		}
		for (int i = 0; i < nmbrOfCardsInit; i++) {
			c = playerAI.Draw(deck);
			b = new Button(c.GetRace());
			b.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					SendCard(event);
				}
			});
			playerAIField.getChildren().add(b);
		}

		playerTurn = FlipACoin();
		// TODO Make this a function. Needs to make the other players buttons
		// disabled and opacied
		EndTurn();
		turnLbl.setText(playerTurn.toString());
		gamestate = GameState.game;
		/*
		 * //Start game loop new AnimationTimer() {
		 * 
		 * @Override public void handle(long now) { if(gamestate ==
		 * GameState.game) {
		 * 
		 * } } }.start();
		 */
	}

	private PlayerTurn FlipACoin() {
		Random rand = new Random();
		boolean randomVal = rand.nextBoolean();
		if (randomVal)
			return PlayerTurn.player1;
		else
			return PlayerTurn.playerAI;
	}

	public void DrawCard(ActionEvent event) {
		if (!playerHasDrawn && playerTurn == PlayerTurn.player1) {
			Card c = player1.Draw(deck);
			playerHasDrawn = true;

			Button b = new Button(c.GetRace());
			b.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					SendCard(event);
				}
			});
			player1Field.getChildren().add(b);
		}
	}

	private void SendCard(ActionEvent event) {
		Button button = (Button) event.getSource();

		if (playerTurn == PlayerTurn.player1) {
			Card playedCard = player1.hand.PlayCard(button.getParent().getChildrenUnmodifiable().indexOf(button));
			board.PlayCard(playedCard, playerTurn);
			player1Field.getChildren().remove(button);

		}
    playerHasPlay = true;
		UpdateBoard();
	}

	public void UpdateBoard() {
		player1Board.getChildren().clear();
		playerAIBoard.getChildren().clear();
		Button b;
		for (int i = 0; i < board.getPlayer1Cards().size(); i++) {
			b = new Button(board.getPlayer1Cards().get(i).GetRace());
			b.setDisable(true);
			player1Board.getChildren().add(b);
		}
		for (int i = 0; i < board.getPlayerAICards().size(); i++) {
			b = new Button(board.getPlayerAICards().get(i).GetRace());
			b.setDisable(true);
			playerAIBoard.getChildren().add(b);
		}
	}

  private void SwapButtons(ObservableList<Node> set1, ObservableList<Node> set2)
    {
        for (Node b : set1) {
            b.setDisable(true);
            b.setOpacity(0.5);
        }
        for (Node b : set2) {
            b.setDisable(false);
            b.setOpacity(1);
        }
    }

	public void turnOfAI() {
		playerHasDrawn = false;
		playerHasPlay = false;
		playerTurn = PlayerTurn.playerAI;

		// Draw Card
		Card c = playerAI.Draw(deck);
		playerHasDrawn = true;
		Button b = new Button(c.GetRace());
		b.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				SendCard(event);
			}
		});
		playerAIField.getChildren().add(b);
		// Play Card
		Card playedCard = playerAI.PlayCard();
		board.PlayCard(playedCard, playerTurn);

		// Remove the card from the hand of the AI
		int playedCardIndex = -1;
		int nbOfCardInHand = playerAIField.getChildren().size();
		for (int i = 0; i < nbOfCardInHand; i++) {
			Button buti = (Button) playerAIField.getChildren().get(i);

			if (buti.textProperty().getValue() == playedCard.GetRace()) {
				playedCardIndex = i;
				break;
			}
		}
		playerAIField.getChildren().remove(playedCardIndex);

		// Update Board and variables
		playerHasPlay = true;
		UpdateBoard();

		EndTurn();
	}

public void EndTurn() {
    	if(playerHasPlay){
            ObservableList<Node> buttonsP1 = player1Field.getChildren();
            ObservableList<Node> buttonsP2 = player2Field.getChildren();
        //AI play    
        if (playerTurn == PlayerTurn.player1) {
                turnOfAI();
        //Player play    
        } else {
                playerHasDrawn = false;
                playerHasPlay = false;
                playerTurn = PlayerTurn.player1;
                SwapButtons(buttonsP2, buttonsP1);
            }
            turnLbl.setText(playerTurn.toString());
            DrawCard(null);
    	}
    }
}
