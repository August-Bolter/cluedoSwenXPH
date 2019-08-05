package Java;

import java.util.*;


/**
 * game board creating and storing rooms and locations
 * creates rooms assigns locations, location types to the rooms.
 * randomly distributes weapons to Rooms to be stored
 * handles marking locations as a wall, hallway, entrance and exit types. 
 * 
 *@param aGame	Board is passed information to create a new Board from a Game
 */
public class Board {
	
	private Game game; //Game related to the board
	private Location[][] loc; //Locations that make up the board
	private List<Room> room; //rooms stored by the board
	private List<WeaponToken> weapons; //weapons on the board

	public Board(Game aGame) {
		game = aGame;
		loc = new Location[24][25]; //Board is 24x25
		room = new ArrayList<Room>();
		weapons = new ArrayList<WeaponToken>();
		createRoom(); //This splits the board into rooms
		registerStartingLocations();
		registerWalls();
		findAndSetHallwayLocations();
		registerRemainingLocations();
		distributeWeaponTokens();

	}

	/** Distribute the weapon tokens randomly onto the rooms */
	private void distributeWeaponTokens() {
		//Creating all the weapon tokens
		weapons.add(new WeaponToken("Candlestick"));
		weapons.add(new WeaponToken("Dagger"));
		weapons.add(new WeaponToken("Lead Pipe"));
		weapons.add(new WeaponToken("Revolver"));
		weapons.add(new WeaponToken("Rope"));
		weapons.add(new WeaponToken("Spanner"));

		Set<Integer> pickedIndexes = new HashSet<Integer>(); //HashSet used for making sure a room doesn't get multiple weapons
		List<Room> randomOrder = new ArrayList<Room>(); //Randomly assorted rooms

		while (room.size() != pickedIndexes.size()) { //Keep going until all rooms picked
			//Pick a random index and get the room from this index
			int randomIndex = (int) ((Math.random()*room.size())); 
			if (!pickedIndexes.contains(randomIndex)) {
				pickedIndexes.add(randomIndex);
				randomOrder.add(room.get(randomIndex));
			}
		}
		//Distributing the weapons
		for (int i = 0; i < weapons.size(); i++) {
			randomOrder.get(i).addWeapon(weapons.get(i));
		}

	}

	/** Registers (sets the type) of tiles that are unregistered. Will set them all to free space, since all the other types have been accounted for at this point. */
	private void registerRemainingLocations() {

		for (int j = 0; j < 25; j++) {
			for (int i = 0; i < 24; i++) {
				if (loc[i][j].getType() == null) {
					loc[i][j].setType(new Type("Free space"));
				}
			}
		}
	}

	/** Sets doorway locations for each room then marks/registers them on the board */
	private void findAndSetHallwayLocations() {
		// TODO Auto-generated method stub
		for (Room r : room) {
			r.setHallway(); //Setting them
		}
		for (Room r : room) {
			for (Location l : r.getEntrances()) {
				loc[l.getX()][l.getY()].setType(l.getType()); //Marking them
			}
		}
	}

	/** Gets a location on the board based on the coordinate given */
	public Location getLocation(int x, int y) {
		return loc[x][y];
	}

	/** Sets the border of the board to walls, unless they have already been registered (type set) e.g. starting locations, rooms */
	private void registerWalls() {

		for (int i = 0; i < 24; i++) {
			//Top 
			if (loc[i][0].getType() == null) {
				loc[i][0].setType(new Type("Wall"));
			}
			//Bottom
			if (loc[i][24].getType() == null) {
				loc[i][24].setType(new Type("Wall"));
			}
		}
		for (int j = 0; j < 25; j++) {
			//Left
			if (loc[0][j].getType() == null) {
				loc[0][j].setType(new Type("Wall"));
			}
			//Right
			if (loc[23][j].getType() == null) {
				loc[23][j].setType(new Type("Wall"));
			}
		}
		//Registering odd walls (walls not on the edge of the board)
		loc[6][1].setType(new Type("Wall"));
		loc[17][1].setType(new Type("Wall"));
	}

	/** Registers the starting location as free space */
	private void registerStartingLocations() {
			for (Location l : game.getStartingLocations()) {
			loc[l.getX()][l.getY()].setType(new Type("Free space"));
		}

	}

	/** Creates and adds all the rooms and assigns locations to each room */
	public void createRoom() {
		//Creating the rooms
		Room dining = new Room("Dining Room");
		Room lounge = new Room("Lounge");
		Room kitchen = new Room("Kitchen");
		Room study = new Room("Study");
		Room hall = new Room("Hall");
		Room billiard = new Room("Billiard Room");
		Room conservatory = new Room("Conservatory");
		Room ballroom = new Room("Ballroom");
		Room library = new Room("Library");	
		
		//Adding the rooms
		this.room.add(dining);
		this.room.add(lounge);
		this.room.add(kitchen);
		this.room.add(study);
		this.room.add(hall);
		this.room.add(billiard);
		this.room.add(conservatory);
		this.room.add(ballroom);
		this.room.add(library);

		//Adding all the locations to board
		for(int j = 0 ; j < 25; j++) {
			for(int i = 0; i < 24; i++) {
				Location newLocation = new Location(i, j);
				loc[i][j] = newLocation;
			}
		}

		//Assigning rooms board locations, based on the room (since the rooms come in different sizes).
		for (Room room : this.room) {
			if(room.getName().equalsIgnoreCase("Kitchen")) {
				for(int j = 1 ; j < 7; j++) {
					for(int i = 0 ; i < 6 ; i++) {
						//Because the rooms aren't perfect grids, we have to account for locations that are inside the grid but not inside the room		
						if (i == 0 && j == 6) {
							loc[i][j].setType(new Type("Wall")); //These locations could be a wall
						}
						else {
							room.addLoc(loc[i][j]);
							loc[i][j].setType(new Type("Room", room.getName()));
						}
					}
				}
			}
			else if(room.getName().equalsIgnoreCase("Ballroom")) {
				for(int j = 1; j < 8; j++) {
					for(int i = 8; i < 16; i++) {
						if ((i == 8 && j == 1) || (i == 9 && j == 1) || (i == 14 && j == 1) || (i == 15 && j == 1)) {
							loc[i][j].setType(new Type("Free space")); //Or a free space
						}
						else {
							room.addLoc(loc[i][j]);
							loc[i][j].setType(new Type("Room", room.getName()));
						}
					}
				}
			}

			else if(room.getName().equalsIgnoreCase("Conservatory")) {
				for(int j = 1; j < 6 ; j++) {
					for(int i = 18 ; i < 24 ; i++) {
						if ((i == 23 && j == 5)) {
							loc[i][j].setType(new Type("Wall"));
						}
						else if (i == 18 && j == 5){
							loc[i][j].setType(new Type("Free space"));
						}
						else {
							room.addLoc(loc[i][j]);
							loc[i][j].setType(new Type("Room", room.getName()));
						}
					}
				}
			}

			else if(room.getName().equalsIgnoreCase("Dining room")) {
				for(int j = 9; j < 16; j++) {
					for(int i = 0 ; i < 8; i++) {
						if ((i == 5 && j == 9) || (i == 6 && j == 9) || (i == 7 && j == 9)) {
							loc[i][j].setType(new Type("Free space"));
						}
						else {
							room.addLoc(loc[i][j]);
							loc[i][j].setType(new Type("Room", room.getName()));
						}
					}
				}
			}

			else if(room.getName().equalsIgnoreCase("Billard room")) {
				for(int j = 8; j < 13; j++) {
					for(int i = 18; i < 24; i++) {
						room.addLoc(loc[i][j]);
						loc[i][j].setType(new Type("Room", room.getName()));
					}
				}
			}

			else if(room.getName().equalsIgnoreCase("Library")) {
				for(int j = 14; j < 19; j++) {
					for(int i = 17; i < 24; i++) {
						if ((i == 17 && j == 15) || (i == 17 && j == 19) || (i == 23 && j == 15) || (i == 23 && j == 19)) {
							loc[i][j].setType(new Type("Wall"));
						}
						else if ((i == 17 && j == 15) || (i == 17 && j ==19)){
							loc[i][j].setType(new Type("Free space"));
						}
						else {
							room.addLoc(loc[i][j]);
							loc[i][j].setType(new Type("Room", room.getName()));
						}
					}	
				}
			}

			else if(room.getName().equalsIgnoreCase("Study")) {
				for(int j = 21; j < 25; j++) {
					for(int i = 17; i < 24; i++) {
						if ((i == 17 && j == 24)) {
							loc[i][j].setType(new Type("Wall"));
						}	
						else {
							room.addLoc(loc[i][j]);
							loc[i][j].setType(new Type("Room", room.getName()));
						}
					}	
				}
			}
			else if(room.getName().equalsIgnoreCase("Hall")) {
				for(int j = 18; j < 25; j++) {
					for(int i = 9; i < 15; i++) {
						room.addLoc(loc[i][j]);
						loc[i][j].setType(new Type("Room", room.getName()));
					}	
				}
			}

			else if(room.getName().equalsIgnoreCase("Lounge")) {
				for(int j = 19; j < 25; j++) {
					for(int i = 0; i < 7; i++) {
						if ((i == 6 && j == 24)) {
							loc[i][j].setType(new Type("Wall"));
						}
						else {
							room.addLoc(loc[i][j]);
							loc[i][j].setType(new Type("Room", room.getName()));
						}
					}	
				}
			}
		}

	}

	/** Gets the surrounding locations of a location based on coordinates */
	public List<Location> getSurroundingLocations(int x, int y){
		List<Location> locations = new ArrayList<Location>();
		if (x != 0) { //So we don't get index out of bounds errors
			locations.add(loc[x-1][y]); //WEST
		}
		else {locations.add(null);} //null represents a "nothing" tile (tile outside the board area)

		if (x != 0 && y != 0) {
			locations.add(loc[x-1][y-1]); //NORTHWEST
		}
		else {locations.add(null);}

		if (y != 0) {
			locations.add(loc[x][y-1]); //NORTH
		}
		else {locations.add(null);}

		if (y != 0 && x != 23) {
			locations.add(loc[x+1][y-1]); //NORTHEAST
		}
		else {locations.add(null);}

		if (x != 23) {
			locations.add(loc[x+1][y]); //EAST
		}
		else {locations.add(null);}

		if (x != 23 && y != 24) {
			locations.add(loc[x+1][y+1]); //SOUTHEAST
		}
		else {locations.add(null);}

		if (y != 24) {
			locations.add(loc[x][y+1]); //SOUTH
		}
		else {locations.add(null);}

		if (y != 24 && x != 0) {
			locations.add(loc[x-1][y+1]); //SOUTHWEST
		}
		else {locations.add(null);}

		return locations;
	}

	/** Gets a room based on the room name */
	public Room getRoom(String name) {
		for (Room r : room) {
			if (r.getName().equalsIgnoreCase(name)) {
				return r;
			}
		}
		return null;
	}

	/** Gets all the rooms */
	public List<Room> getRoom()
	{
		return room;
	}

	/** Gets the closest exit of a room from a doorway
	 * @param doorway  	location of doorway
	 * @param room		room contains information on exit locations
	 *  */
	public Location getClosestExit(Location doorway, Room room) {
		// TODO Auto-generated method stub
		Location closest = null;
		int min = Integer.MAX_VALUE;
		//find closest exit
		for (Location loc : room.getExits()) { //Going through the room exits
			int distanceToExit = (( (loc.getX() - doorway.getX()) * (loc.getX() - doorway.getX()) ) * ( (loc.getY() - doorway.getY()) * (loc.getY() - doorway.getY()) ));
			if (distanceToExit < min) {
				min = distanceToExit;
				closest = loc;				
			}
		}
		return closest; //location that is closest 
	}

	/** Gets the direction you would have to move in to go from a room exit to a room entrance. 
	 * @param l				current location
	 * @param closestExit	the location of an exit
	 * */
	public String getDirection(Location l, Location closestExit) {
		// TODO Auto-generated method stub
		String direction = "";

		//NORTH
		if (l.getX()-closestExit.getX() == 0 && l.getY()-closestExit.getY() < 0) {
			direction = "North";
		}

		//SOUTH
		else if (l.getX()-closestExit.getX() == 0 && l.getY()-closestExit.getY() > 0) {
			direction = "South";
		}

		//EAST
		else if (l.getX()-closestExit.getX() > 0 && l.getY()-closestExit.getY() == 0) {
			direction = "East";
		}

		//WEST
		else if (l.getX()-closestExit.getX() < 0 && l.getY()-closestExit.getY() == 0) {
			direction = "West";
		}

		return direction;
	}

}