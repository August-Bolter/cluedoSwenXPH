package code;

import java.util.List;

/**
 * Suggestion is a set of cards which can prompt refutes from other players
 * 
 */
public class Suggestion {

	private CardSet suggSet; //Contains a card set which is the cards that are in the suggestion the player has made

	public Suggestion(CardSet aSuggSet) {
		suggSet = aSuggSet;
	}

	/** Gets the suggestion set */
	public CardSet getSuggSet() {
		return suggSet;
	}
}