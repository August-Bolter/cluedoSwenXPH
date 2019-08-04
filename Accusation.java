package Java;

public class Accusation {

	private CardSet accSet; //Contains a card set which is the accusation the player has made
	
	public Accusation(CardSet aAccSet) {
		accSet = aAccSet;
	}

	public CardSet getAccSet() {
		return accSet;
	}
}