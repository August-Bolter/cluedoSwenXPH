package Java;

public class Type extends Location{
	roomtype r;
	
	public enum roomtype {
		FREESPACE,
		WALL,
		DOORWAY
	}
	
	public Type(Integer i , Integer j, String s) {
		super(i, j);
		// TODO Auto-generated constructor stub
		if(s.equals("Free")) {
			this.r = roomtype.FREESPACE;
		}
		else if (s.equals("Wall")) {
			this.r = roomtype.WALL;
		}
		else if (s.equals("Doorway")) {
			this.r = roomtype.DOORWAY;
		}
		
	}
	
	String getType() {
		return this.r.toString();
	}

}
