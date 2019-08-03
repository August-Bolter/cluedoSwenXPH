package Java;

import java.util.List;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.1.4597.b7ac3a910 modeling language!*/



// line 123 "model.ump"
// line 246 "model.ump"
public class CharacterToken extends Token
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //CharacterToken Attributes
  private int xPos;
  private int yPos;
  private String characterName;
  private Room room;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public CharacterToken(Location startingLocation, String aCharacterName)
  {
    super(aCharacterName);
    xPos = startingLocation.getX();
    yPos = startingLocation.getY();
    characterName = aCharacterName;
    room = null;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setXPos(int aXPos)
  {
    boolean wasSet = false;
    xPos = aXPos;
    wasSet = true;
    return wasSet;
  }

  public boolean setYPos(int aYPos)
  {
    boolean wasSet = false;
    yPos = aYPos;
    wasSet = true;
    return wasSet;
  }

  public boolean setCharacterName(String aCharacterName)
  {
    boolean wasSet = false;
    characterName = aCharacterName;
    wasSet = true;
    return wasSet;
  }
  
  public Room getRoom() {
	  return room;
  }
  
  public int getXPos()
  {
    return xPos;
  }

  public int getYPos()
  {
    return yPos;
  }

  public String getCharacterName()
  {
    return characterName;
  }

  public void delete()
  {
    super.delete();
  }

  // line 132 "model.ump"
   public String getName(){
	   return characterName;
  }

  // line 136 "model.ump"
   public int getX(){
	   return xPos;
  }

  // line 140 "model.ump"
   public int getY(){
	   return yPos;
  }


  public String toString()
  {
    return super.toString() + "["+
            "xPos" + ":" + getXPos()+ "," +
            "yPos" + ":" + getYPos()+ "," +
            "characterName" + ":" + getCharacterName()+ "]";
  }

public boolean inRoomCheck() {
	return (room != null);
}

public String setRoom(Board board) {
	List<Room> rooms = board.getRoom();
	boolean foundRoom = false;
	for(Room r : rooms) {
		for (Location l : r.getLoc()) {
			if (l.getX() == getX() && l.getY() == getY()) {
				room = r;
				foundRoom = true;
				//r.addCharacterToken(this);
			}
		}
	}
	if (!foundRoom) {
		room = null;
	}
	if (room != null) { 
		return "You are now in the " + room.getName(); 
	}
	return "";
}

}