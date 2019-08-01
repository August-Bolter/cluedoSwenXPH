package Java;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.1.4597.b7ac3a910 modeling language!*/


import java.util.*;

// line 16 "model.ump"
// line 183 "model.ump"
public class Board
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Board Associations
  private Game game;
  private List<Location> loc;
  private List<Room> room;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Board(Game aGame)
  {
    if (aGame == null || aGame.getBoard() != null)
    {
      throw new RuntimeException("Unable to create Board due to aGame. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    game = aGame;
    loc = new ArrayList<Location>();
    room = new ArrayList<Room>();
  }

  public Board(CardSet aSolutionForGame, Card aDeckForGame)
  {
    game = new Game(aSolutionForGame, aDeckForGame, this);
    loc = new ArrayList<Location>();
    room = new ArrayList<Room>();
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetOne */
  public Game getGame()
  {
    return game;
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
  /* Code from template association_GetMany */
  public Room getRoom(int index)
  {
    Room aRoom = room.get(index);
    return aRoom;
  }

  public List<Room> getRoom()
  {
    List<Room> newRoom = Collections.unmodifiableList(room);
    return newRoom;
  }

  public int numberOfRoom()
  {
    int number = room.size();
    return number;
  }

  public boolean hasRoom()
  {
    boolean has = room.size() > 0;
    return has;
  }

  public int indexOfRoom(Room aRoom)
  {
    int index = room.indexOf(aRoom);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfLoc()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Location addLoc(int aX, int aY, Room aRoom)
  {
    return new Location(aX, aY, this, aRoom);
  }

  public boolean addLoc(Location aLoc)
  {
    boolean wasAdded = false;
    if (loc.contains(aLoc)) { return false; }
    Board existingBo = aLoc.getBo();
    boolean isNewBo = existingBo != null && !this.equals(existingBo);
    if (isNewBo)
    {
      aLoc.setBo(this);
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
    //Unable to remove aLoc, as it must always have a bo
    if (!this.equals(aLoc.getBo()))
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
  /* Code from template association_IsNumberOfValidMethod */
  public boolean isNumberOfRoomValid()
  {
    boolean isValid = numberOfRoom() >= minimumNumberOfRoom() && numberOfRoom() <= maximumNumberOfRoom();
    return isValid;
  }
  /* Code from template association_RequiredNumberOfMethod */
  public static int requiredNumberOfRoom()
  {
    return 9;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfRoom()
  {
    return 9;
  }
  /* Code from template association_MaximumNumberOfMethod */
  public static int maximumNumberOfRoom()
  {
    return 9;
  }
  /* Code from template association_AddMNToOnlyOne */
  public Room addRoom(String aName)
  {
    if (numberOfRoom() >= maximumNumberOfRoom())
    {
      return null;
    }
    else
    {
      return new Room(aName, this);
    }
  }

  public boolean addRoom(Room aRoom)
  {
    boolean wasAdded = false;
    if (room.contains(aRoom)) { return false; }
    if (numberOfRoom() >= maximumNumberOfRoom())
    {
      return wasAdded;
    }

    Board existingBo = aRoom.getBo();
    boolean isNewBo = existingBo != null && !this.equals(existingBo);

    if (isNewBo && existingBo.numberOfRoom() <= minimumNumberOfRoom())
    {
      return wasAdded;
    }

    if (isNewBo)
    {
      aRoom.setBo(this);
    }
    else
    {
      room.add(aRoom);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeRoom(Room aRoom)
  {
    boolean wasRemoved = false;
    //Unable to remove aRoom, as it must always have a bo
    if (this.equals(aRoom.getBo()))
    {
      return wasRemoved;
    }

    //bo already at minimum (9)
    if (numberOfRoom() <= minimumNumberOfRoom())
    {
      return wasRemoved;
    }
    room.remove(aRoom);
    wasRemoved = true;
    return wasRemoved;
  }

  public void delete()
  {
    Game existingGame = game;
    game = null;
    if (existingGame != null)
    {
      existingGame.delete();
    }
    while (loc.size() > 0)
    {
      Location aLoc = loc.get(loc.size() - 1);
      aLoc.delete();
      loc.remove(aLoc);
    }

    while (room.size() > 0)
    {
      Room aRoom = room.get(room.size() - 1);
      aRoom.delete();
      room.remove(aRoom);
    }

  }

}