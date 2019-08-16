package code;

import java.util.Set;

public class Player {
	private Set<Card> hand; //The players hand
	private CharacterToken token; //The token they control
	private boolean haveLost; //Have they "lost" the game by making a wrong accusation?
	private String name;

	public Player(Set<Card> aHand, CharacterToken aToken, String playerName) {
		name = playerName;
		hand = aHand;
		token = aToken;
		haveLost = false;
	}
	
	public String getName() {
		return name;
	}

	//Returns if the player has lost
	public boolean haveLost() {
		return haveLost;
	}

	//If the player makes a wrong accusation this is always called with the parameter true
	public void setLost(boolean b) {
		haveLost = b;
	}

	//Gets the players hand
	public Set<Card> getHand() {
		return hand;
	}

	//Adds a card to the players hand
	public void addCard(Card c) {
		hand.add(c);
	}

	//Gets the character token the player is controlling
	public CharacterToken getToken() {
		return token;
	}

}