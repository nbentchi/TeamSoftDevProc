package com.Kingdom.cards.Model;

public class Elf extends Card {

	public Elf() {
		super();
		this.race = "Elf";
	}

	public void Power() {
		// Need to have the class Kingdom
		// Select a card in the kingdom
		// Use the power
		System.out.println("I'm an Elf !!");
	}

	@Override
	public void Power(Board b) {
		// TODO Auto-generated method stub
		
	}

}
