package code;

import java.util.Set;

public class Player {
	private Set<Card> hand; //The players hand
	private CharacterToken token; //The token they control
	private boolean haveLost; //Have they "lost" the game by making a wrong accusation?
	private String name; //The name which the player chose to have
	private Room pastRoom; //The room the player was in last turn, null if they weren't in a room last turn

	public Player(Set<Card> aHand, CharacterToken aToken, String playerName) {
		name = playerName;
		hand = aHand;
		token = aToken;
		haveLost = false;
	}
	
	/** Gets the players name */
	public String getName() {
		return name;
	}

	/** Returns if the player has lost */
	public boolean haveLost() {
		return haveLost;
	}

	/** If the player makes a wrong accusation this is always called with the parameter true */
	public void setLost(boolean b) {
		haveLost = b;
	}

	/** Gets the players hand */
	public Set<Card> getHand() {
		return hand;
	}

	/** Adds a card to the players hand */
	public void addCard(Card c) {
		hand.add(c);
	}

	/** Gets the character token the player is controlling */
	public CharacterToken getToken() {
		return token;
	}

	/** Sets the room the player was in last turn */
	public void setPastRoom(Room playerRoom) {
		pastRoom = playerRoom;
	}
	
	/** Gets the room the player was in last turn if they were in one. This method is used for ensuring a player can't re-enter a room on the same turn */
	public Room getPastRoom() {
		return pastRoom;
	}

}