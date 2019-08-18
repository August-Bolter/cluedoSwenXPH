package code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import code.Type.loctype;

/**
 * A single Turn covers all the actions an active player can do in one turn
 * Roll dice, move, accuse and suggest
 */
public class Turn {

	private Player player; //The player who is having this turn
	private Suggestion suggestion; //Potentially a suggestion that could be made by the player
	private Accusation accusation; //Potentially an accusation that could be made by the player
	private int steps; //The number of steps the player can move, determined by the dice roll they get
	private Game game; //The game associated with this turn
	private List<Location> previousLocations; //All the locations the player has moved onto during the turn.
	Map<Player, Card> cardsFound; //All the refuting cards which were found in other players hands
	private Card refuteCard; //The card the player used to refute if they have multiple refuting cards
	private Player refutePlayer; //The player which has multiple refuting cards

	/** Gets the turns player */
	public Player getPlayer() {
		return player;
	}

	public Turn(Player p, Game g) {
		player = p;
		game = g;
		refuteCard = null;
		suggestion = null;
		accusation = null;
		previousLocations = new ArrayList<Location>();
	}

	/**
	 * Adds two virtual dice rolling pseudo-random values
	 * @return the dice rolls (integers between 1 and 6)
	 */
	public ArrayList<Integer> rollDice(){
		ArrayList<Integer> diceValues = new ArrayList<Integer>(); //We return the number of each dice roll rather than total for the GUI
		int dieRoll1 = (int)(Math.random() * 6); //Random value between 0 and 5
		int dieRoll2 = (int)(Math.random() * 6); //Random value between 0 and 5
		
		diceValues.add(dieRoll1+1); //Add 1 since we want a dice roll between 1 and 6 not 0 and 5
		diceValues.add(dieRoll2+1);
		return diceValues; 
	}
	
	/** Gets the previous locations the player has traversed across in the turn */
	public List<Location> getPrevLoc() {
		return previousLocations;
	}

	/** Returns suggestion made in the turn */
	public Suggestion getSuggestion() {
		return suggestion;
	}
	
	/** Returns accusation made in the turn */
	public Accusation getAccusation() {
		return accusation;
	}
	
	/** Returns card used for refuting */
	public Card getRefuteCard() {
		return refuteCard;
	}
	
	/** Sets the refuting card. This method is called after the player chooses what card to refute with */
	public void setRefuteCard(Card c) {
		refuteCard = c;
	}
	
	/** Gets all the cards used in the refuting process */
	public Map<Player, Card> getCardsFound() {
		return cardsFound;
	}

	/** Sets the steps the player has for the turn */
	public void setSteps(int i) {
		steps = i;
	}
	
	public int getSteps() {
		return steps;
	}
	
	/** Checks if a move is valid. */
	public boolean validMove(Location moveLocation) {
		if (steps > 0) { //They need to have moves left
			//Can only move into a free space or doorway
			if (moveLocation.getType().getLocType() == loctype.FREESPACE || moveLocation.getType().getLocType() == loctype.DOORWAY) {
				//Can't move onto locations that have been moved in the same turn or locations which are occupied by another player
				if (!previousLocations.contains(moveLocation) && moveLocation.getPlayer() == null) { 
					//Checking that the player isn't trying to reenter a room they have been in this turn
					if (moveLocation.getType().getLocType() == loctype.DOORWAY) { //Only concerns trying to move into doorways
						for (Room r : game.getBoard().getRoom()) {
							for (Location l : r.getLoc()) {
								if (l.getX() == moveLocation.getX() && l.getY() == moveLocation.getY()) {
									if (r == game.getCurrentPlayer().getPastRoom()) {
										return false;
									}
								}
							}
						}
					}
					int newX = moveLocation.getX();
					int newY = moveLocation.getY();			
					//Check if they are only moving one step
					if ((Math.abs(newX - player.getToken().getX()) + Math.abs(newY - player.getToken().getY())) == 1) {
						return true;
					}	
				}
			}
		}

		return false;
	}
	
	/** Move token to the desired location, if they enter a room moves them to a specific spot in the room (partially determined by the number of players)
	 * @param moveLocation The desired new location 
	 * @param game The current game */
	public void move(Location moveLocation, Game game) {
		//Storing the location before we move because we need to add it to previousLocations and state that its not occupied anymore by the player
		Location prevLoc = game.getBoard().getLocation(player.getToken().getX(), player.getToken().getY());
		previousLocations.add(prevLoc);
		prevLoc.setPlayer(null);
		//Clearing the players name from the location since they are no longer on it
		game.getGui().clearEntranceLabel(new Location(player.getToken().getX(), player.getToken().getY()));
		//Moving the location
		player.getToken().setXPos(moveLocation.getX());
		player.getToken().setYPos(moveLocation.getY());
		moveLocation.setPlayer(player); //Player now occupies this location
		steps--;
		Location playerLocation = game.getBoard().getLocation(player.getToken().getX(), player.getToken().getY());
		//If we have entered a room
		if (playerLocation.getType().getLocType() == loctype.DOORWAY) {
			steps = 0;
			for (Room r : game.getBoard().getRoom()) {
				/* This is determining where to place the player in the room based on the number of players in the room currently. If there are no player it will 
				 * place the player 3 tiles to the right and one tile down from the top left corner of the room */
				for (Location l : r.getEntrances()) {
					if (l.getX() == playerLocation.getX() && l.getY() == playerLocation.getY()) {
						int indexX = 0;
						int indexY = 0;
						for (Player p : r.getPlayers()) {
							//Fills out row by row, 2x3 grid (since 6 players max can be in a room)
							indexX++;
							if (indexX == 2) {
								indexX = 0;
								indexY++;
							}
						}
						//We are no longer in the doorway so remove the player from the doorway
						game.getBoard().getLocation(player.getToken().getX(), player.getToken().getY()).setPlayer(null);
						//Moving the player to the new location and occupying the location
						Location topLeft = r.getLoc().get(0);
						player.getToken().setXPos(topLeft.getX()+3+indexX);
						player.getToken().setYPos(topLeft.getY()+1+indexY);
						game.getBoard().getLocation(topLeft.getX()+3+indexX, topLeft.getY()+1+indexY).setPlayer(player);
						//Now we are in the room. There are two ways of identifying if a player is in a room for convenience.
						player.getToken().setRoom(r);
						r.addPlayer(player);
					}
				}
			}
		}
	}
	

	/** This method moves the character token and weapon token that were named in the suggestion into the room named in the suggestion unless they are already
	 * in the room 
	 * @param players		players in the game
	 * @param board			board containing rooms and locations
	 * @param game			game contains the characterTokens
	 * */
	public void moveTokens(List<Player> players, Board board, Game game) {
		Room room = board.getRoom(suggestion.getSuggSet().getRoomC().getName()); //Gets the room based on the room card used in the suggestion
		WeaponToken suggestWeapon = null;
		
		//Finds the weapon token based on the weapon card used in the suggestion
		for (Room r : board.getRoom()) {
			for (WeaponToken w : r.getWeapon()) {
				if (w.getName().equalsIgnoreCase(suggestion.getSuggSet().getWeaponC().getName())) { //Do the names match
					suggestWeapon = w;
					//If the weapon isn't already in the room then we need to move it by removing it from its room and adding it to the suggestion room
					if (!r.getName().equalsIgnoreCase(suggestion.getSuggSet().getRoomC().getName())) {
						r.removeWeapon(w);
						room.addWeapon(suggestWeapon);
					}
					break;
				}
			}
		}

		//Finds player to move into suggested room unless they are already in the room
		for (Player p : players) {
			if (p.getToken().getName().equalsIgnoreCase(suggestion.getSuggSet().getCharacterC().getName())) { //If the names match
				if (!room.getPlayers().contains(p)) { //and the player isn't in the room (which means it can never move the current player)
					movePlayerToken(room, p);
				}
			}
		}
	}

	/** Moves the suggestion player token to the room 
	 * @param room The room that the token is being moved into
	 * @param player The player controlling the token */
	public void movePlayerToken(Room room, Player p) {
		//Again finding where to place them in the room
		int indexX = 0;
		int indexY = 0;
		for (Player player : room.getPlayers()) {
			indexX++;
			if (indexX == 2) {
				indexX = 0;
				indexY++;
			}
		}
		//If they were previously in a room remove them from that room
		if (p.getToken().getRoom() != null) {
			p.getToken().getRoom().removePlayer(p);
		}
		
		Location topLeft = room.getLoc().get(0);
		game.getBoard().getLocation(player.getToken().getX(), player.getToken().getY()).setPlayer(null); //Since they are moving location
		//Moving player
		p.getToken().setXPos(topLeft.getX()+3+indexX);
		p.getToken().setYPos(topLeft.getY()+1+indexY);
		game.getBoard().getLocation(topLeft.getX()+3+indexX, topLeft.getY()+1+indexY).setPlayer(p);
		
		//Then change the room of the player
		p.getToken().setRoom(room); 
		room.addPlayer(p);
		p.getToken().setMoved(true); //Since the player was moved by a suggestion, they will have different options on their next turn
	}

	/** Make a suggestion in the room the player is in 
	 * @param gui The game gui 
	 * @param roomName The name of the suggestion room
	 * @param weaponName The name of the weapon in the suggestion
	 * @param characterName The name of the character in the suggestion */
	public void makeSuggestion(Gui gui, String roomName, String weaponName, String characterName) {

		cardsFound = new HashMap<Player, Card>();
		//Creating the suggestion
		suggestion = new Suggestion(new CardSet(new WeaponCard(weaponName), new CharacterCard(characterName), new RoomCard(roomName)));
		ArrayList<Card> multipleCards = new ArrayList<Card>(); //Used when the player has multiple cards to refute
		Player multipleCardsPlayer = null; //The player which has multiple cards which they can use to refute
		
		for (Player p : game.getPlayers()) {
			//Check other players hand
			if (p != game.getCurrentPlayer()) { 
				ArrayList<Card> cardsFoundPerPlayer = new ArrayList<Card>(); //To determine if they have multiple cards to refute
				for (Card c : p.getHand()) {
					if (c.getName().equalsIgnoreCase(roomName) || c.getName().equalsIgnoreCase(weaponName) || c.getName().equalsIgnoreCase(characterName)) {
						//We have found a refuting card
						cardsFoundPerPlayer.add(c);
					}
				}
				//Assuming there are cards which can be used to refute
				if (cardsFoundPerPlayer.size() != 0) {
					if (cardsFoundPerPlayer.size() < 2) {
						//If they only have one card they can use to refute then that is automatically refuted
						cardsFound.put(p, cardsFoundPerPlayer.get(0)); 
					}
					else {
						//Otherwise the player has multiple cards which they can use to refute
						for (Card c : cardsFoundPerPlayer) {
							multipleCards.add(c);
						}
						multipleCardsPlayer = p;
					}
				}
			}
		}
		
		if (multipleCardsPlayer != null) {
			//So we need to ask what card they want to refute with
			refuteMultiple(gui, multipleCards, multipleCardsPlayer);
		}
		else {
			//If no one has multiple refuting cards then show the refuting results immediately
			gui.displaySuggestionResults();
		}
	}

	/** Calls refute in the GUI which shows the multiple refute window. */
	private void refuteMultiple(Gui gui, ArrayList<Card> cardsFoundPerPlayer, Player p) {
		gui.refute(cardsFoundPerPlayer, p);
	}

	/** Finishes off the refute. Because 'makeSuggetion()' doesn't wait for the user to choose their refuting card, the refute method is split up if this is the 
	 * case */
	public void finishRefute(Gui gui) {
		cardsFound.put(refutePlayer, refuteCard); //Add all the refuting cards
		gui.displaySuggestionResults(); //Then show these cards to the current player
	}

	/** Sets the refuting player */
	public void setRefutePlayer(Player p) {
		// TODO Auto-generated method stub
		refutePlayer = p;
	}

	/** Makes an accusation. Doesn't need to make an accusation object as it can just compare them directly. Returns if the accusation is correct
	 * @param roomName The room mentioned in the accusation
	 * @param weaponName The weapon mentioned in the accusation
	 * @param characterName The character mentioned in the accusation */
	public boolean makeAccusation(Gui gui, String roomName, String weaponName, String characterName) {

		if (game.getSolution().getRoomC().getName().equalsIgnoreCase(roomName)) {
			if (game.getSolution().getWeaponC().getName().equalsIgnoreCase(weaponName)) {
				if (game.getSolution().getCharacterC().getName().equalsIgnoreCase(characterName)) {
					//If they got the accusation correct return true
					return true;
				}
			}
		}
		//Otherwise the player has made an incorrect accusation and has lost the game (but still has to refute suggestions if they can)
		game.getCurrentPlayer().setLost(true);
		return false;
	}

	/** Moves the player from the room to an exit.
	 * @param moveLocation The exit they are moved to */
	public void moveToExit(Location moveLocation) {
		game.getBoard().getLocation(player.getToken().getX(), player.getToken().getY()).setPlayer(null);
		//Move the player
		game.getCurrentPlayer().getToken().setXPos(moveLocation.getX());
		game.getCurrentPlayer().getToken().setYPos(moveLocation.getY());
		moveLocation.setPlayer(player);
		game.getCurrentPlayer().getToken().setRoom(null); //They just left the room
		
		for (Room r : game.getBoard().getRoom()) {
			//Make a copy to avoid concurrent modification error
			ArrayList<Player> playersCopy = new ArrayList<Player>();
			for (Player p : r.getPlayers()) {
				playersCopy.add(p);
			}
			//Remove the player from the room again
			for (Player p : playersCopy) {
				if (p == game.getCurrentPlayer()) {
					r.removePlayer(p);
				}
			}
		}
	}
	
}