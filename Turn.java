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
	private int steps;
	private Game game;
	private List<Location> previousLocations;
	Map<Player, Card> cardsFound;
	private Card refuteCard;
	private Player refutePlayer;

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
	 * adds two virtual dice rolling random values 
	 * @return integer is a positive number between with min value of 7 and max value of 12 //diceTest
	 */
	public ArrayList<Integer> rollDice(){
		ArrayList<Integer> diceValues = new ArrayList<Integer>(); //We return the number of each dice roll rather than total for the GUI
		int dieRoll1 = (int)(Math.random() * 6); //Random value between 0 and 5
		int dieRoll2 = (int)(Math.random() * 6); //Random value between 0 and 5
		diceValues.add(dieRoll1+1);
		diceValues.add(dieRoll2+1);
		return diceValues; //Add 1 since we want a dice roll between 1 and 6 not 0 and 5
	}
	
	public List<Location> getPrevLoc() {
		return previousLocations;
	}

	/** Makes the suggestion using a given card set 
	 * @param c    The CardSet to be stored as a suggestion
	 * */
	public void makeSuggestion(CardSet c){
		suggestion = new Suggestion(c);
	}

	/** Returns said suggestion */
	public Suggestion getSuggestion() {
		return suggestion;
	}
	
	/** Makes the accusation using a given card set */
	public void makeAccusation(CardSet c){
		accusation = new Accusation(c);
	}
	
	/** Returns said accusation*/
	public Accusation getAccusation() {
		return accusation;
	}
	
	public Card getRefuteCard() {
		return refuteCard;
	}
	
	public void setRefuteCard(Card c) {
		refuteCard = c;
	}
	
	public Map<Player, Card> getCardsFound() {
		return cardsFound;
	}

	/** Moves the players character token 1 tile in the requested direction. Returns the coordinates of the token before moving so we can take note of this location
	 * and ensure the player can't move onto that location in the same turn. 
	 * @param direction	 A compass coordinate. Direction of location player wants to move 
	 * @param board		 Will update location of player's Character Token on the board 
	 * @return coords	 list contains former x and y position of the player - is returned and stored in a visitedLocations list 
	 * @see Game line 758
	 * */
	public List<Integer> move(String direction, Board board){
		//Getting coordinates of player
		int x = player.getToken().getX();
		int y = player.getToken().getY();

		//Based on the direction provided it changes the x and/or y position of the players character token to move them to a different position
		if (direction.equalsIgnoreCase("W")) { //WEST
			player.getToken().setXPos(x-1);
		}
		else if (direction.equalsIgnoreCase("N")) { //NORTH etc...
			player.getToken().setYPos(y-1);
		}
		else if (direction.equalsIgnoreCase("E")) {
			player.getToken().setXPos(x+1);
		}
		else if (direction.equalsIgnoreCase("S")) {
			player.getToken().setYPos(y+1);
		}
		
		List<Integer> coords = new ArrayList<Integer>();
		//Adding pre-movement coordinates
		coords.add(x); 
		coords.add(y);
		
		board.getLocation(x, y).setPlayer(null); //Removing the player from the old location
		board.getLocation(player.getToken().getX(), player.getToken().getY()).setPlayer(player); //Adding the player to the new location

		return coords;
	}

	public void setSteps(int i) {
		// TODO Auto-generated method stub
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
				if (!previousLocations.contains(moveLocation)) { //Can't move onto locations that have been moved in the same turn
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
	
	/** Move token 
	 * @param moveLocation */
	public void move(Location moveLocation, Game game) {
		Location prevLoc = game.getBoard().getLocation(player.getToken().getX(), player.getToken().getY());
		previousLocations.add(prevLoc);
		player.getToken().setXPos(moveLocation.getX());
		player.getToken().setYPos(moveLocation.getY());
		steps--;
		//If we have entered a room
		Location playerLocation = game.getBoard().getLocation(player.getToken().getX(), player.getToken().getY());
		if (playerLocation.getType().getLocType() == loctype.DOORWAY) {
			steps = 0;
			for (Room r : game.getBoard().getRoom()) {
				for (Location l : r.getEntrances()) {
					if (l.getX() == playerLocation.getX() && l.getY() == playerLocation.getY()) {
						int indexX = 0;
						int indexY = 0;
						for (Player p : r.getPlayers()) {
							indexX++;
							if (indexX == 2) {
								indexX = 0;
								indexY = 1;
							}
						}
						Location topLeft = r.getLoc().get(0);
						player.getToken().setXPos(topLeft.getX()+3+indexX);
						player.getToken().setYPos(topLeft.getY()+1+indexY);
						player.getToken().setRoom(r);
						r.addPlayer(player);
					}
				}
			}
		}
	}

	public void makeSuggestion(Gui gui, String roomName, String weaponName, String characterName) {
		// TODO Auto-generated method stub
		cardsFound = new HashMap<Player, Card>();
		ArrayList<Card> multipleCards = new ArrayList<Card>();
		Player multipleCardsPlayer = null;
		for (Player p : game.getPlayers()) {
			if (p != game.getCurrentPlayer()) {
				ArrayList<Card> cardsFoundPerPlayer = new ArrayList<Card>();
				for (Card c : p.getHand()) {
					if (c.getName().equalsIgnoreCase(roomName) || c.getName().equalsIgnoreCase(weaponName) || c.getName().equalsIgnoreCase(characterName)) {
						cardsFoundPerPlayer.add(c);
					}
				}
				if (cardsFoundPerPlayer.size() < 2) {
					cardsFound.put(p, cardsFoundPerPlayer.get(0));
				}
				else {
					for (Card c : cardsFoundPerPlayer) {
						multipleCards.add(c);
					}
					multipleCardsPlayer = p;
				}
			}
		}
		
		if (multipleCardsPlayer != null) {
			refuteMultiple(gui, multipleCards, multipleCardsPlayer);
		}
		else {
			gui.displaySuggestionResults();
		}
	}

	private void refuteMultiple(Gui gui, ArrayList<Card> cardsFoundPerPlayer, Player p) {
		gui.refute(cardsFoundPerPlayer, p);
	}

	public void finishRefute(Gui gui) {
		cardsFound.put(refutePlayer, refuteCard);
		gui.displaySuggestionResults();
	}

	public void setRefutePlayer(Player p) {
		// TODO Auto-generated method stub
		refutePlayer = p;
	}
	
}