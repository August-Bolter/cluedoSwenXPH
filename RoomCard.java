package code;

/** A room card associated with the name of the room */
public class RoomCard extends Card {

	private String roomName;

	public RoomCard(String aRoomName) {
		super(aRoomName);
		roomName = aRoomName;
	}

	public String getName(){
		return roomName;
	}

}