package code;

import java.util.List;

/**
 * Suggestion uses the set being played during a suggestion and handles moving suspected Character tokens and Weapon tokens into the room of the suggestion. 
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