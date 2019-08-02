package Java;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.1.4597.b7ac3a910 modeling language!*/


import java.util.*;

// line 35 "model.ump"
// line 201 "model.ump"
public class Room
{
	
  //------------------------
  // MEMBER VARIABLES
  //------------------------
  public enum roomtype {
		DOORWAY,
		WALL,
  }
  
  //Room Attributes
  private String name;
  Location centreLocation;

  //Room Associations
  private List<Location> loc;
  private List<Location>doorwayLoc; //method to return this
  private List<Location>wallLoc;
  
  private List<CharacterToken> playersInRoom;
  
	//getter and setter methods for centreLocation  
  public Location getCentreLocation() {
			return centreLocation;
		}
		
  public void setCentreLocation(Location centreLocation) {
			this.centreLocation = centreLocation;
		}


   private Board bo;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Room(String aName, Board aBo)
  {
    name = aName;
    loc = new ArrayList<Location>();
    boolean didAddBo = setBo(aBo);

    
    if (!didAddBo)
    {
      throw new RuntimeException("Unable to create room due to bo. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public String inRoomCheck(CharacterToken c) {
	  
	  if(this.playersInRoom.contains(c)) {
		  return (this.toString());
	  }
	  return "no characther in this room";
  }
  
  public List getCharachterTokens() {
	  
	  return this.playersInRoom;
  }
  
  public List<Location> getEntrances() {
	
	  return doorwayLoc;  
  }
  
  public Location getCenLocation() {
	return centreLocation;
	  
  }
  
  public void setCenLocation(Location l) {
	  centreLocation  = l;
	  
  }
  
  
  
  public boolean addPlayerToken(CharacterToken c) {
	  
	  if (playersInRoom.add(c)) return true;  
	  return false;
  }
  
  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public String getName()
  {
    return name;
  }
  /* Code from template association_GetMany */
  public Location getLoc(int index)
  {
    Location aLoc = loc.get(index);
    return aLoc;
  }

  public List<Location> getLoc()
  {
    List<Location> newLoc = Collections.unmodifiableList(loc);
    return newLoc;
  }

  public int numberOfLoc()
  {
    int number = loc.size();
    return number;
  }

  public boolean hasLoc()
  {
    boolean has = loc.size() > 0;
    return has;
  }

  public int indexOfLoc(Location aLoc)
  {
    int index = loc.indexOf(aLoc);
    return index;
  }
  /* Code from template association_GetOne */
  public Board getBo()
  {
    return bo;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfLoc()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Location addLoc(int aX, int aY, Board aBo)
  {
    return new Location(aX, aY, aBo, this);
  }

  public boolean addLoc(Location aLoc)
  {
    boolean wasAdded = false;
    if (loc.contains(aLoc)) { return false; }
    Room existingRoom = aLoc.getRoom();
    boolean isNewRoom = existingRoom != null && !this.equals(existingRoom);
    if (isNewRoom)
    {
      aLoc.setRoom(this);
    }
    else
    {
      loc.add(aLoc);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeLoc(Location aLoc)
  {
    boolean wasRemoved = false;
    //Unable to remove aLoc, as it must always have a room
    if (!this.equals(aLoc.getRoom()))
    {
      loc.remove(aLoc);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addLocAt(Location aLoc, int index)
  {
    boolean wasAdded = false;
    if(addLoc(aLoc))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfLoc()) { index = numberOfLoc() - 1; }
      loc.remove(aLoc);
      loc.add(index, aLoc);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveLocAt(Location aLoc, int index)
  {
    boolean wasAdded = false;
    if(loc.contains(aLoc))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfLoc()) { index = numberOfLoc() - 1; }
      loc.remove(aLoc);
      loc.add(index, aLoc);
      wasAdded = true;
    }
    else
    {
      wasAdded = addLocAt(aLoc, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetOneToAtMostN */
  public boolean setBo(Board aBo)
  {
    boolean wasSet = false;
    //Must provide bo to room
    if (aBo == null)
    {
      return wasSet;
    }

    //bo already at maximum (9)
    if (aBo.numberOfRoom() >= Board.maximumNumberOfRoom())
    {
      return wasSet;
    }

    Board existingBo = bo;
    bo = aBo;
    if (existingBo != null && !existingBo.equals(aBo))
    {
      boolean didRemove = existingBo.removeRoom(this);
      if (!didRemove)
      {
        bo = existingBo;
        return wasSet;
      }
    }
    bo.addRoom(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    while (loc.size() > 0)
    {
      Location aLoc = loc.get(loc.size() - 1);
      aLoc.delete();
      loc.remove(aLoc);
    }

    Board placeholderBo = bo;
    this.bo = null;
    if(placeholderBo != null)
    {
      placeholderBo.removeRoom(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "name" + ":" + getName()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "bo = "+(getBo()!=null?Integer.toHexString(System.identityHashCode(getBo())):"null");
  }
}