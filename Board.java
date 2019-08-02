package Java;

import java.awt.desktop.PrintFilesEvent;

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
	private List<Location> hallwayLocations;

	//------------------------
	// CONSTRUCTOR
	//------------------------

	public Board(Game aGame)
	{
		if (aGame == null || aGame.getBoard() != null)
			throw new RuntimeException("Unable to create Board due to aGame. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
		else {
		}
		game = aGame;
		loc = new ArrayList<Location>();
		room = new ArrayList<Room>();

		//generate rooms and generate locations
		Room dining = new Room("dining room", this);
		Room lounge = new Room("lounge", this);
		Room kitchen = new Room("kitchen", this);
		Room study = new Room("study", this);
		Room hall = new Room("hall", this);
		Room billiard = new Room("billiard room", this);
		Room conservatory = new Room("conservatory", this);
		Room ballroom = new Room("ballroom", this);
		Room library = new Room("library", this);
		Room cellar = new Room("cellar", this);		

		this.room.add(dining);
		this.room.add(lounge);
		this.room.add(kitchen);
		this.room.add(study);
		this.room.add(hall);
		this.room.add(billiard);
		this.room.add(conservatory);
		this.room.add(ballroom);
		this.room.add(library);
		this.room.add(cellar);

		//add 600 locations into the locations field
		for(int i = 0 ; i <= 24 ; i++) {
			for(int j = 0; j <= 25; j++) {
				Location newLocation = new Location(i, j);
				loc.add(newLocation);
			}
		}

		//set locations as part of a room
		for(Room room : this.room) {
			if(room.getName().equals("kitchen")) {
				for(int i = 1 ; i < 6 ; i++) {
					for(int j = 0 ; j <6 ; j++) {
						room.addLoc(new Location(i, j));
						//Location.setRoom?
					}
				}
			}
			else if(room.getName().equals("ballroom")) {
				for(int i = 2; i < 7 ; i++) {
					for(int j = 8 ; j < 16 ; j++) {
						room.addLoc(new Location(i, j));
					}
				}
			}

			else if(room.getName().equals("conservatory")) {
				for(int i = 1; i < 6 ; i++) {
					for(int j = 18 ; j < 24 ; j++) {

						if(i==5 && j==18) {
							break;
						}
						room.addLoc(new Location(i, j));
					}
				}
			}

			else if(room.getName().equals("dining room")) {
				for(int i = 9; i < 16 ; i++) {
					for(int j = 0 ; j < 8 ; j++) {			
						if(i==9 && (j == 5 || j == 6 || j==7)) { //rooms to not be added
							break;
						}
						else {
							room.addLoc(new Location(i, j));
						}
					}
				}
			}

			else if(room.getName().equals("library")) {
				for(int i = 14; i < 19 ; i++) {
					for(int j = 17 ; j < 24 ; j++) {
						if(i== 14 && j == 17) {
							break;
						}
						else if(i==18 && j == 17) {
							break;
						}
						else {
							room.addLoc(new Location(i, j));
						}
					}
				}
			}
			
			else if(room.getName().equals("lounge")) {
				for(int i = 19; i < 24 ; i++) {
					for(int j = 1 ; j < 6	 ; j++) {
						room.addLoc(new Location(i, j));
					}
				}
			}
			
			else if(room.getName().equals("hall")) {
				for(int i = 18; i < 24 ; i++) {
					for(int j = 9 ; j < 15	 ; j++) {
						room.addLoc(new Location(i, j));
					}
				}
			}
			
			else if(room.getName().equals("study")) {
				for(int i = 21; i < 24 ; i++) {
					for(int j = 17 ; j < 24	 ; j++) {
						room.addLoc(new Location(i, j));
					}
				}
			}
		}
				
	}

	public Board(CardSet aSolutionForGame, Card aDeckForGame)
	{
		game = new Game(aSolutionForGame, aDeckForGame, this);
		loc = new ArrayList<Location>();
		room = new ArrayList<Room>();

	}

	//interface
	
	public List<Location>getSurroundingLocations(Location l){
		
		List<Location> surroundingLocations = new ArrayList<>();
		
		//west
		int westLocationX = l.getX() - 1 ;
		int westLocationY = l.getY();
		Location westLocation = new Location(westLocationX, westLocationY);
		surroundingLocations.add(westLocation);
		
		
		//northwest
		int nwLocationX = l.getX() - 1 ;
		int nwLocationY = l.getY()+1;
		Location nwLocation = new Location(nwLocationX, nwLocationY);
		surroundingLocations.add(nwLocation);

		//north
		int nLocationX = l.getX() ;
		int nLocationY = l.getY() + 1;
		Location nLocation = new Location(nLocationX, nLocationY);
		surroundingLocations.add(nLocation);
		
		//northeast
		int neLocationX = l.getX() + 1 ;
		int neLocationY = l.getY() +1 ;
		Location neLocation = new Location(neLocationX, neLocationY);
		surroundingLocations.add(neLocation);
		
		
		//east
		int eLocationX = l.getX() + 1 ;
		int eLocationY = l.getY() ;
		Location eLocation = new Location(eLocationX, eLocationY);
		surroundingLocations.add(eLocation);
		
		
		//southeast
		int seLocationX = l.getX() + 1 ;
		int seLocationY = l.getY() - 1;
		Location seLocation = new Location(seLocationX, seLocationY);
		surroundingLocations.add(seLocation);
		
		//south
		int sLocationX = l.getX() ;
		int sLocationY = l.getY() -1;
		Location sLocation = new Location(sLocationX, sLocationY);
		surroundingLocations.add(sLocation);
		
		//southwest
		int swLocationX = l.getX() - 1 ;
		int swLocationY = l.getY();
		Location swLocation = new Location(swLocationX, swLocationY);
		surroundingLocations.add(swLocation);
		
		return surroundingLocations;
	}
	
	public void printSurroundingLocations(List<Location> l) {
		System.out.println("Surrounding locations: west - northwest - north - northeast - east - southeast - south - southwest");
		for(int i = 0 ; i < l.size() ; i++) {
			Location l1 = l.get(i);
			String locString  = l1.toString();
			System.out.println(locString + " " );
		}
	}

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