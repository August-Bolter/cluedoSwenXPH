package Java;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.1.4597.b7ac3a910 modeling language!*/



// line 23 "model.ump"
// line 191 "model.ump"
public class Location
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Location Attributes
  private int x;
  private int y;

  //Location Associations
  private Board bo;
  private Room room;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Location(int aX, int aY, Board aBo, Room aRoom)
  {
    x = aX;
    y = aY;
    boolean didAddBo = setBo(aBo);
    if (!didAddBo)
    {
      throw new RuntimeException("Unable to create loc due to bo. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddRoom = setRoom(aRoom);
    if (!didAddRoom)
    {
      throw new RuntimeException("Unable to create loc due to room. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public Location(int i, int j) {
	// TODO Auto-generated constructor stub
	  x = i;
	  y = j;
  }

public boolean setX(int aX)
  {
    boolean wasSet = false;
    x = aX;
    wasSet = true;
    return wasSet;
  }

  public boolean setY(int aY)
  {
    boolean wasSet = false;
    y = aY;
    wasSet = true;
    return wasSet;
  }

  public int getX()
  {
    return x;
  }

  public int getY()
  {
    return y;
  }
  /* Code from template association_GetOne */
  public Board getBo()
  {
    return bo;
  }
  /* Code from template association_GetOne */
  public Room getRoom()
  {
    return room;
  }
  /* Code from template association_SetOneToMany */
  public boolean setBo(Board aBo)
  {
    boolean wasSet = false;
    if (aBo == null)
    {
      return wasSet;
    }

    Board existingBo = bo;
    bo = aBo;
    if (existingBo != null && !existingBo.equals(aBo))
    {
      existingBo.removeLoc(this);
    }
    bo.addLoc(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setRoom(Room aRoom)
  {
    boolean wasSet = false;
    if (aRoom == null)
    {
      return wasSet;
    }

    Room existingRoom = room;
    room = aRoom;
    if (existingRoom != null && !existingRoom.equals(aRoom))
    {
      existingRoom.removeLoc(this);
    }
    room.addLoc(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Board placeholderBo = bo;
    this.bo = null;
    if(placeholderBo != null)
    {
      placeholderBo.removeLoc(this);
    }
    Room placeholderRoom = room;
    this.room = null;
    if(placeholderRoom != null)
    {
      placeholderRoom.removeLoc(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "x" + ":" + getX()+ "," +
            "y" + ":" + getY()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "bo = "+(getBo()!=null?Integer.toHexString(System.identityHashCode(getBo())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "room = "+(getRoom()!=null?Integer.toHexString(System.identityHashCode(getRoom())):"null");
  }
}