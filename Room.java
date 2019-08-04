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

  //Room Associations
  private List<Location> loc;
  private List<Location>doorwayLoc;
  private List<Location> doorwayExit;
  
  private Location centreLocation;
  private List<CharacterToken> playersInRoom;
  private Set<WeaponToken> weapons;
  
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
    doorwayLoc = new ArrayList<Location>();
    doorwayExit = new ArrayList<Location>();
    loc = new ArrayList<Location>();
    weapons = new HashSet<WeaponToken>();
  }
  
  public void addWeapon(WeaponToken w) {
	  weapons.add(w);
  }
  
  public void removeWeapon(WeaponToken w) {
	  weapons.remove(w);
  }
  
  public Set<WeaponToken> getWeapon() {
	  return weapons;
  }
  
  public List<Location> getExits() {
	  return doorwayExit;
  }
  
  //Set hallway entrances and exits
  public void setHallway() {
	  if (name.equals("kitchen")) {
		  Location l = new Location(4, 6);
		  l.setType(new Type("Doorway", name));
		  doorwayLoc.add(l);
		  
		  doorwayExit.add(new Location(4, 7));
		  
	  }
	  else if (name.equals("ballroom")) {
		  Location l = new Location(8, 5);
		  l.setType(new Type("Doorway", name));
		  doorwayLoc.add(l);
		  
		  Location l1 = new Location(15, 5);
		  l1.setType(new Type("Doorway", name));
		  doorwayLoc.add(l1);
		  
		  Location l2 = new Location(9, 7);
		  l2.setType(new Type("Doorway", name));
		  doorwayLoc.add(l2);
		  
		  Location l3 = new Location(14, 7);
		  l3.setType(new Type("Doorway", name));
		  doorwayLoc.add(l3);
		  
		  
		  doorwayExit.add(new Location(7, 5));
		  doorwayExit.add(new Location(9, 8));
		  doorwayExit.add(new Location(14, 8));
		  doorwayExit.add(new Location(16, 5));
		  
	  }
	  else if (name.equals("conservatory")) {
		  Location l = new Location(18, 4);
		  l.setType(new Type("Doorway", name));
		  doorwayLoc.add(l);
		  
		  doorwayExit.add(new Location(18, 5));
	  }
	  else if (name.equals("dining room")) {
		  Location l = new Location(7, 12);
		  l.setType(new Type("Doorway", name));
		  doorwayLoc.add(l);
		  
		  Location l1 = new Location(6, 15);
		  l1.setType(new Type("Doorway", name));
		  doorwayLoc.add(l1);
		  
		  doorwayExit.add(new Location(6, 16));
		  doorwayExit.add(new Location(8, 12));
	  }
	  else if (name.equals("billard room")) {
		  Location l = new Location(18, 9);
		  l.setType(new Type("Doorway", name));
		  doorwayLoc.add(l);
		  
		  Location l1 = new Location(22, 12);
		  l1.setType(new Type("Doorway", name));
		  doorwayLoc.add(l1);
		  
		  doorwayExit.add(new Location(17, 9));
		  doorwayExit.add(new Location(22, 13));
	  }
	  else if (name.equals("library")) {
		  Location l = new Location(20, 14);
		  l.setType(new Type("Doorway", name));
		  doorwayLoc.add(l);
		  
		  Location l1 = new Location(17, 16);
		  l1.setType(new Type("Doorway", name));
		  doorwayLoc.add(l1);
		  
		  doorwayExit.add(new Location(20, 13));
		  doorwayExit.add(new Location(16, 16));
	  }
	  else if (name.equals("lounge")) {
		  Location l = new Location(6, 19);
		  l.setType(new Type("Doorway", name));
		  doorwayLoc.add(l);
		  
		  doorwayExit.add(new Location(6, 18));
	  }
	  else if (name.equals("study")) {
		  Location l = new Location(17, 21);
		  l.setType(new Type("Doorway", name));
		  doorwayLoc.add(l);
		  
		  doorwayExit.add(new Location(17, 20));
	  }
	  else {
		  Location l = new Location(11, 18);
		  l.setType(new Type("Doorway", name));
		  doorwayLoc.add(l);

		  Location l1 = new Location(12, 18);
		  l1.setType(new Type("Doorway", name));
		  doorwayLoc.add(l1);
		  
		  Location l2 = new Location(14, 20);
		  l2.setType(new Type("Doorway", name));
		  doorwayLoc.add(l2);
		  
		  doorwayExit.add(new Location(15, 20));
		  doorwayExit.add(new Location(12, 17));
		  doorwayExit.add(new Location(11, 17));
	  }
  }
  
  public List<Location> getEntrances() {
	  return doorwayLoc;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean inRoomCheck(CharacterToken c) {
	  
	  if(this.playersInRoom.contains(c)) {
		  return true;
	  }
	  return false;
  }
  
  public List<CharacterToken> getCharacterTokens() {
	  return playersInRoom;
  }
  
  public List<Location> getDoorwayLocations() {
	
	   return doorwayLoc;  
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

  public void addLoc(Location aLoc)
  {
	  loc.add(aLoc);
  }
  
  public void addCharacterToken(CharacterToken c) {
	  playersInRoom.add(c);
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

  public String toString()
  {
    return super.toString() + "["+
            "name" + ":" + getName()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "bo = "+(getBo()!=null?Integer.toHexString(System.identityHashCode(getBo())):"null");
  }
}