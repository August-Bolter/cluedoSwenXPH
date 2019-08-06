package Java;

/**
 * a single Accusation can be made by a player when it is their Turn
 * Contains the Card Set made by a Player making an accusation	
 * Cards inside the accSet can be returned to be compared to the solution in Game	
 *  @see Game solution
 */

public class Accusation {

	private CardSet accSet; //Contains a card set which is the accusation the player has made
	
	public Accusation(CardSet aAccSet) {
		accSet = aAccSet;
	}

	public CardSet getAccSet() {
		return accSet;
	}
}
