package com.Kingdom.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class Player {
	
	public String pseudo;
	protected List<Card> hand = new ArrayList<Card>();
	protected List<Card> board = new ArrayList<Card>();
	
	public Player(){
		
	}
	
	public Player(String pseudo){
		this.pseudo = pseudo;
	}
	
	public void Draw(Deck deck){
		hand.add(deck.GetFirstCard());
	}
	
	public int SizeHand(){
		return hand.size();
	}
	public int SizeBoard(){
		return board.size();
	}
	
	protected boolean PlayCard(Card card){
		if(hand.contains(card)){
			hand.remove(card);
			board.add(card);
			return true;
		}
		else return false;
	}
	
	public int ScoreBoard(){
		int score = SizeBoard();
		if(board.contains(new Troll()) && board.contains(new Elf()) && board.contains(new Gnome()) && board.contains(new Goblin()) && board.contains(new Korrigan()) && board.contains(new Dryad())){
			score += 3;
		}
		return score;
	}
}
