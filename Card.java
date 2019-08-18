package code;

/** A class representing an individual card in Cluedo */
public class Card {

	private String name;

	public Card(String aName) {
		name = aName;
	}

	/** Sets the name of the card */
	public void setName(String aName) {
		name = aName;
	}

	/** Returns the card name */
	public String getName() {
		return name;
	}
}