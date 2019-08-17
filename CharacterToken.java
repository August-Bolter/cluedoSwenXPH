package code;

import java.util.List;


/**
 * Character tokens are the player's game entity on the Board
 * Able to change location as player inputs moves
 * @extends Token			
 */
public class CharacterToken extends Token {

	//Coordinates
	private int xPos; 
	private int yPos;
	
	private String characterName;
	private Room room; //The room the token (player) is in, if it is null then the token/player isn't in a room
	private boolean movedBySuggestion; //If the token was moved into a room because of a suggestion involving them (not on their turn)

	public CharacterToken(Location startingLocation, String aCharacterName) {
		super(aCharacterName);
		xPos = startingLocation.getX();
		yPos = startingLocation.getY();
		characterName = aCharacterName;
		room = null; //Players are initialized at designated starting positions outside the 10 rooms.
		movedBySuggestion = false; // 
	}

	public void setMoved(boolean setMove) {
		movedBySuggestion = setMove;
	}

	public boolean getMoved() {
		return movedBySuggestion;
	}

	public void setXPos(int aXPos) {
		xPos = aXPos;
	}

	public void setYPos(int aYPos) {
		yPos = aYPos;
	}

	public Room getRoom() {
		return room;
	}
	
	/** gets character's name  */
	public String getName(){
		return characterName;
	}
	
	/** gets character's x position on the board  */
	public int getX(){
		return xPos;
	}
  
	/** gets character's y position on the board */
	public int getY(){
		return yPos;
	}

	/** Checks if the token is in a room */
	public boolean inRoomCheck() {
		return (room != null);
	}

	/** Sets the room of the token, used when a token is moved from one room to another directly (due to suggestions) 
	 * @param Room  room to be moved to
	 * */
	public void setRoom(Room r) {
		room = r;
	}

}