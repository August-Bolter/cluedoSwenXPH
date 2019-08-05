package Java;

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

	/** This method moves the character token and weapon token that were named in the suggestion into the room named in the suggestion unless they are already
	 * in the room 
	 * @param players		players in the game
	 * @param board			board containing rooms and locations
	 * @param game			game contains the characterTokens
	 * */
	public void moveTokens(List<Player> players, Board board, Game game) {
		Room room = board.getRoom(suggSet.getRoomCard().getName()); //Gets the actual room based on the room card used in the suggestion
		WeaponToken suggestWeapon = null;
		
		/** Finds the weapon token based on the weapon card used in the suggestion */
		for (WeaponToken w : room.getWeapon()) {
			if (w.getName().equalsIgnoreCase(suggSet.getWeaponCard().getName())) { //Do the names match
				suggestWeapon = w;
			}
		}
		//If the room doesn't contain the weapon token then
		if (!room.getWeapon().contains(suggestWeapon)) {
			for (Room r : board.getRoom()) {
				for (WeaponToken w : r.getWeapon()) {
					if (w == suggestWeapon) {
						r.removeWeapon(w); //Remove the weapon from the current room it is in
					}
				}
			}
			room.addWeapon(suggestWeapon); //And add it to the suggestion room
			System.out.println(suggestWeapon.getName() + " was moved to " + room.getName());
		}

		/** Finds player to move into suggested room unless already in the room*/
		boolean activeCharacter= false; //Is the suggestion character a character token controlled by a player or an inactive character token (no player is controlling it)
		for (Player p : players) {
			if (p.getToken().getName().equalsIgnoreCase(suggSet.getCharacterCard().getName())) { //If the names match
				if (p.getToken().getRoom() != room) { //and we aren't in the room
					p.getToken().setRoom(room); //Then change the room of the player
					p.getToken().setMoved(true); //Since the player was moved by a suggestion, they will have different options on their next turn
					System.out.println(p.getToken().getName() + " was moved to " + room.getName());
				}
				activeCharacter = true; 
			}
		}
		if (!activeCharacter) { //If character is inactive
			for (CharacterToken c : game.getCharacterTokens()) { //Look through inactive and active characters
				if (c.getName().equalsIgnoreCase(suggSet.getCharacterCard().getName())) {
					c.setRoom(room); //Set the room of the inactive character
				}
			}
		}
	}
}