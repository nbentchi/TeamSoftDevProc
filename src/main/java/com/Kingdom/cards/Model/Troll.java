package com.Kingdom.cards.Model;

import com.Kingdom.cards.Deck;
import com.Kingdom.cards.Model.Card;

import java.util.List;

public class Troll extends Card {

	public Troll(){
		super();
		this.race = "Troll";
	}

	public void power(Board b, Deck d, Player p1, Player p2) {
		//TODO This doesn't work for the AI, just gives me the cards
		List<Card> cardsP1 = b.getPlayerAICards();
		b.setPlayerAICards(b.getPlayer1Cards());
		b.setPlayer1Cards(cardsP1);
	}
}
