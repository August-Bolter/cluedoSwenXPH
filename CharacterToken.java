package Java;

import java.util.List;


//A class representing the character token the player controls (or the ones that aren't assigned to any players but are still placed in rooms if they are mentioned in suggestions)
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

	/** Sets the room of the token, this is called when the token walks into or out of a room
	 * Returns string outputs to inform player of the room change
	 * @param Board 
	 * @return String
	 *  */
	public String setRoom(Board board) {
		List<Room> rooms = board.getRoom(); //Grab the rooms of the board
		boolean foundRoom = false;
		//Going through each defined area of each room and check if the location of the token is inside the area, if it is the token must be in that room.
		for(Room r : rooms) {
			for (Location l : r.getLoc()) {
				if (l.getX() == getX() && l.getY() == getY()) {
					room = r;
					foundRoom = true;
				}
			}
		}
		/** This means the token has walked out (is out of) a room so set room to null */
		if (!foundRoom) {
			room = null;
		}

		/** room has been entered  */
		if (room != null) { 
			return "You are now in the " + room.getName(); 
		}
		return "";
	}

	/** Sets the room of the token, used when a token is moved from one room to another directly (due to suggestions) 
	 * @ param Room  room to be moved to
	 * */
	public void setRoom(Room r) {
		room = r;
	}

}