package code;

/** This class represents the type a location could be */
public class Type{
	private loctype r; //The type of location is represented using an enum
	private String roomName; //If the type of location is a room than the room name will also be stored

	//The four different types
	public enum loctype {
		FREESPACE, //Hallways, starting locations, locations that players can always access
		WALL, //Walls that are scattered around the board
		DOORWAY, //Entrance to the room, on the outer edge
		ROOM //Inside the room and not the doorway
	}

	//Since Free space and Wall aren't related to rooms they need a constructor without a room name parameter
	public Type(String s) {

		if(s.equalsIgnoreCase("Free space")) {
			this.r = loctype.FREESPACE;
		}
		else if (s.equalsIgnoreCase("Wall")) {
			this.r = loctype.WALL;
		}

	}

	/** Gets the type of location */
	public loctype getLocType() {
		return r;
	}

	public Type(String s, String s1) {
		if (s.equalsIgnoreCase("Room")) {
			r = loctype.ROOM;
		}
		else if (s.equalsIgnoreCase("Doorway")) {
			r = loctype.DOORWAY;
		}
		roomName = s1;
	}

	/** Gets the name of type. Doorways have a different name value than normal room tiles */
	public String getName() {
		if (r == loctype.FREESPACE) {
			return "Free space";
		}
		else if (r == loctype.WALL) {
			return "Wall";
		}
		else if (r == loctype.DOORWAY) {
			return roomName + " Doorway";
		}
		return roomName;
	}
	
	/** Returns the room name assuming the type is a doorway */
	public String getRoomName() {
		return roomName;
	}

}
