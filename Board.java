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
  private Location[][] loc; //Locations that make up the board
  private List<Room> room;
  private List<WeaponToken> weapons;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Board(Game aGame)
  {
    game = aGame;
    loc = new Location[24][25];
    room = new ArrayList<Room>();
    weapons = new ArrayList<WeaponToken>();
    createRoom(); //This splits the board into rooms
    registerStartingLocations();
    registerWalls();
    findAndSetHallwayLocations();
    registerRemainingLocations();
    distributeWeaponTokens();
    
  }
  
  private void distributeWeaponTokens() {
	weapons.add(new WeaponToken("Candlestick"));
	weapons.add(new WeaponToken("Dagger"));
	weapons.add(new WeaponToken("Lead Pipe"));
	weapons.add(new WeaponToken("Revolver"));
	weapons.add(new WeaponToken("Rope"));
	weapons.add(new WeaponToken("Spanner"));
	
	Set<Integer> pickedIndexes = new HashSet<Integer>();
	List<Room> randomOrder = new ArrayList<Room>();
	
	while (room.size() != pickedIndexes.size()) {
		int randomIndex = (int) ((Math.random()*room.size()));
		if (!pickedIndexes.contains(randomIndex)) {
			pickedIndexes.add(randomIndex);
			randomOrder.add(room.get(randomIndex));
		}
	}
	for (int i = 0; i < weapons.size(); i++) {
		randomOrder.get(i).addWeapon(weapons.get(i));
	}
	
  }

private void registerRemainingLocations() {
	// TODO Auto-generated method stub
	for (int j = 0; j < 25; j++) {
		for (int i = 0; i < 24; i++) {
			if (loc[i][j].getType() == null) {
				loc[i][j].setType(new Type("Free space"));
			}
		}
	}
}

private void findAndSetHallwayLocations() {
	// TODO Auto-generated method stub
	for (Room r : room) {
		r.setHallway();
	}
	for (Room r : room) {
		for (Location l : r.getEntrances()) {
			loc[l.getX()][l.getY()].setType(l.getType());
		}
	}
}

public Location getLocation(int x, int y) {
	return loc[x][y];
}

//Sets the border of the board to walls, unless they have already been registered (type set) e.g. starting locations, rooms
private void registerWalls() {
	// TODO Auto-generated method stub
	for (int i = 0; i < 24; i++) {
		if (loc[i][0].getType() == null) {
			loc[i][0].setType(new Type("Wall"));
		}
		if (loc[i][24].getType() == null) {
			loc[i][24].setType(new Type("Wall"));
		}
	}
	for (int j = 0; j < 25; j++) {
		if (loc[0][j].getType() == null) {
			loc[0][j].setType(new Type("Wall"));
		}
		if (loc[23][j].getType() == null) {
			loc[23][j].setType(new Type("Wall"));
		}
	}
	//Registering odd walls
	loc[6][1].setType(new Type("Wall"));
	loc[17][1].setType(new Type("Wall"));
}

private void registerStartingLocations() {
	// TODO Auto-generated method stub
	for (Location l : game.getStartingLocations()) {
		loc[l.getX()][l.getY()].setType(new Type("Free space"));
	}
	
}

public void createRoom() {
  //generate rooms and generate locations
  Room dining = new Room("dining room", this);
  Room lounge = new Room("lounge", this);
  Room kitchen = new Room("kitchen", this);
  Room study = new Room("study", this);
  Room hall = new Room("hall", this);
  Room billiard = new Room("billiard room", this);
  Room conservatory = new Room("conservatory", this);
  Room ballroom = new Room("ball room", this);
  Room library = new Room("library", this);	
  
  this.room.add(dining);
  this.room.add(lounge);
  this.room.add(kitchen);
  this.room.add(study);
  this.room.add(hall);
  this.room.add(billiard);
  this.room.add(conservatory);
  this.room.add(ballroom);
  this.room.add(library);
  
  //add 600 locations into the locations field
  for(int j = 0 ; j < 25; j++) {
  	for(int i = 0; i < 24; i++) {
  		Location newLocation = new Location(i, j);
  	    loc[i][j] = newLocation;
  	}
  }
  
  //Create rooms made up of board locations
  
  for (Room room : this.room) {
	  if(room.getName().equals("kitchen")) {
	  	for(int j = 1 ; j < 7; j++) {
	  		for(int i = 0 ; i < 6 ; i++) {
	  			if (i == 0 && j == 6) {
	  				loc[i][j].setType(new Type("Wall"));
	  			}
	  			else {
	  				room.addLoc(loc[i][j]);
	  				loc[i][j].setType(new Type("Room", room.getName()));
	  			}
	  		}
	  	}
	  }
	  else if(room.getName().equals("ball room")) {
	  	for(int j = 1; j < 8; j++) {
	  		for(int i = 8; i < 16; i++) {
	  			if ((i == 8 && j == 1) || (i == 9 && j == 1) || (i == 14 && j == 1) || (i == 15 && j == 1)) {
	  				loc[i][j].setType(new Type("Free space"));
	  			}
	  			else {
	  				room.addLoc(loc[i][j]);
	  				loc[i][j].setType(new Type("Room", room.getName()));
	  			}
	  		}
	  	}
	  }
	  
	  else if(room.getName().equals("conservatory")) {
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
	  
	  else if(room.getName().equals("dining room")) {
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
	  
	  else if(room.getName().equals("billard room")) {
		  	for(int j = 8; j < 13; j++) {
		  		for(int i = 18; i < 24; i++) {
		  			room.addLoc(loc[i][j]);
		  			loc[i][j].setType(new Type("Room", room.getName()));
		  		}
		  	}
	  }
	  
	  else if(room.getName().equals("library")) {
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
	  
	  else if(room.getName().equals("study")) {
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
	  else if(room.getName().equals("hall")) {
		  	for(int j = 18; j < 25; j++) {
		  		for(int i = 9; i < 15; i++) {
		  			room.addLoc(loc[i][j]);
		  			loc[i][j].setType(new Type("Room", room.getName()));
		  		}	
		  	}
	  }
	  
	  else if(room.getName().equals("lounge")) {
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
  
  public List<Location> getSurroundingLocations(int x, int y){
	  List<Location> locations = new ArrayList<Location>();
	  if (x != 0) {
		  locations.add(loc[x-1][y]); //WEST
	  }
	  else {locations.add(null);}
	  
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
  
  public Room getRoom(String name) {
	  for (Room r : room) {
		  if (r.getName().equalsIgnoreCase(name)) {
			  return r;
		  }
	  }
	  return null;
  }

  public List<Room> getRoom()
  {
	  return room;
  }

public Location getClosestExit(Location l, Room room) {
	// TODO Auto-generated method stub
	Location closest = null;
	int min = Integer.MAX_VALUE;
	for (Location loc : room.getExits()) {
		int distance = (( (loc.getX() - l.getX()) * (loc.getX() - l.getX()) ) * ( (loc.getY() - l.getY()) * (loc.getY() - l.getY()) ));
		if (distance < min) {
			min = distance;
			closest = loc;
		}
	}
	return closest;
}

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