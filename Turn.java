package Java;

import java.util.ArrayList;
import java.util.List;

public class Turn {

	private Player player; //The player who is having this turn
	private Suggestion suggestion; //Potentially a suggestion that could be made by the player
	private Accusation accusation; //Potentially an accusation that could be made by the player

	public Player getPlayer() {
		return player;
	}

	public Turn(Player p) {
		player = p;
		suggestion = null;
		accusation = null;
	}

	public int rollDice(){
		int dieRoll1 = (int)(Math.random() * 6); //Random value between 0 and 5
		int dieRoll2 = (int)(Math.random() * 6); //Random value between 0 and 5
		return ((dieRoll1+1) + (dieRoll2+1)); //Add 1 since we want a dice roll between 1 and 6 not 0 and 5
	}

	/** Makes the suggestion using a given card set */
	public void makeSuggestion(CardSet c){
		suggestion = new Suggestion(c);
	}

	/** Returns said suggestion */
	public Suggestion getSuggestion() {
		return suggestion;
	}
	
	public void makeAccusation(CardSet c){
		accusation = new Accusation(c);
	}

	public Accusation getAccusation() {
		return accusation;
	}

	/** Moves the players character token 1 tile in the requested direction. Returns the coordinates of the token before moving so we can take note of this location
	 * and ensure the player can't move onto that location in the same turn. */
	public List<Integer> move(String direction, Board board){
		//Getting coordinates of player
		int x = player.getToken().getX();
		int y = player.getToken().getY();

		//Based on the direction provided it changes the x and/or y position of the players character token to move them to a different position
		if (direction.equalsIgnoreCase("W")) { //WEST
			player.getToken().setXPos(x-1);
		}
		else if (direction.equalsIgnoreCase("NW")) { //NORTHWEST
			player.getToken().setXPos(x-1);
			player.getToken().setYPos(y-1);
		}
		else if (direction.equalsIgnoreCase("N")) { //NORTH etc...
			player.getToken().setYPos(y-1);
		}
		else if (direction.equalsIgnoreCase("NE")) {
			player.getToken().setYPos(y-1);
			player.getToken().setXPos(x+1);
		}
		else if (direction.equalsIgnoreCase("E")) {
			player.getToken().setXPos(x+1);
		}
		else if (direction.equalsIgnoreCase("SE")) {
			player.getToken().setXPos(x+1);
			player.getToken().setYPos(y+1);
		}
		else if (direction.equalsIgnoreCase("S")) {
			player.getToken().setYPos(y+1);
		}
		else if (direction.equalsIgnoreCase("SW")) {
			player.getToken().setXPos(x-1);
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

}