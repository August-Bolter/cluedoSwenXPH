package Java;

public class Type{
	private loctype r;
	private String roomName;
	
	public enum loctype {
		FREESPACE,
		WALL,
		DOORWAY,
		ROOM
	}
	
	public Type(String s) {
		// TODO Auto-generated constructor stub
		if(s.equals("Free space")) {
			this.r = loctype.FREESPACE;
		}
		else if (s.equals("Wall")) {
			this.r = loctype.WALL;
		}
		else if (s.equals("Room")) {
			this.r = loctype.ROOM;
		}
		
	}
	
	public Type(String s, String s1) {
		if (s.equals("Room")) {
			r = loctype.ROOM;
			roomName = s1;
		}
		else if (s.equals("Doorway")) {
			r = loctype.DOORWAY;
			roomName = s1;
		}
	}
	
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

}
