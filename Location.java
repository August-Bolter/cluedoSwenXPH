package code;

/** Represents a location on the board. */

public class Location {

	private int x;
	private int y;
	private Type type; //The type of location it is
	private Player player; //The player that is on the location

	public Location(int i, int j) {
		x = i;
		y = j;
		type = null;
		player = null; //If player == null then there is no player on the location
	}


	/** Sets the type of location. Type is set after the board and all the locations have been created */
	public void setType(Type t) {
		type = t;
	}

	/** Sets a player to the location to show that it is occupied (when the location is moved on) */
	public void setPlayer(Player p) {
		player = p;
	}

	/** Gets the player occupying the location */
	public Player getPlayer() {
		return player;
	}

	public Type getType() {
		return type;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}