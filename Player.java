package Java;

import java.util.Set;
/**
 * Player represents a user, their hand and state throughout the game - created in Game setup
 * Player has a gameCharacter token able to be placed on locations on the board
 * player stores current state of the player - when a player "loses" they cannot make a Turn (roll dice,move,accuse,suggest) for the rest of the game
 **/
public class Player {
	private Set<Card> hand; //The players hand is a set of Cards that have been dealt to the player
	private CharacterToken token; //The token they control
	private boolean haveLost; //Have they "lost" the game by making a wrong accusation? 

	/**
	 * Constructor for a player. Created at Game setup
	 * @param aHand	
	 * @param aToken
	 */
	public Player(Set<Card> aHand, CharacterToken aToken) {
		hand = aHand;
		token = aToken;
		haveLost = false; //because a Player loses after making an incorrect accusation
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

	/**Adds a card to the players hand 
	 * Always called at startup by Game
	 * @param c     Card to be added to hand
	 */
	public void addCard(Card c) {
		hand.add(c);
	}

	//Gets the character token the player is controlling
	public CharacterToken getToken() {
		return token;
	}

}