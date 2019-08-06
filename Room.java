package Java;

import java.util.*;

/**
 * Rooms are areas on the board that players have to enter to make a suggestion
 * Doorway is the location that room is entered by players. Weapons and tokens are stored/put inside Locations 
 */
public class Room {

	private String name;

	private List<Location> loc; //The locations making up the room
	private List<Location>doorwayLoc; //The doorways making up the room
	private List<Location> doorwayExit; //The doorway "exits" (the tile that you move from to enter the room, so the exits are technically outside the room)

	private List<CharacterToken> playersInRoom; //The character tokens (that the players controls) that are in the room
	private Set<WeaponToken> weapons; //The weapon tokens in the room

	/**
	 *Room constructor initializes room fields
	 *@param aName
	 */
	public Room(String aName) {
		name = aName;
		doorwayLoc = new ArrayList<Location>();
		doorwayExit = new ArrayList<Location>();
		loc = new ArrayList<Location>();
		weapons = new HashSet<WeaponToken>();
	}

	//Used when moving a weapon token from one room to another through suggestion and when weapons are added to rooms for the first time 
	public void addWeapon(WeaponToken w) {
		weapons.add(w);
	}

	//Used when moving a weapon token from one room to another through suggestion
	public void removeWeapon(WeaponToken w) {
		weapons.remove(w);
	}

	public Set<WeaponToken> getWeapon() {
		return weapons;
	}

	public List<Location> getExits() {
		return doorwayExit;
	}
	
	public List<Location> getEntrances() {
		return doorwayLoc;
	}

	public List<CharacterToken> getCharacterTokens() {
		return playersInRoom;
	}

	public List<Location> getDoorwayLocations() {
		return doorwayLoc;  
	}

	public String getName() {
		return name;
	}

	public List<Location> getLoc() {
		return loc;
	}

	//Adds a location to the room
	public void addLoc(Location aLoc) {
		loc.add(aLoc);
	}

	/** Based on the room this method assigns the doorway entrances/exits to the room */
	public void setHallway() {
		if (name.equalsIgnoreCase("Kitchen")) {
			//Entrances
			Location l = new Location(4, 6);
			l.setType(new Type("Doorway", name));
			doorwayLoc.add(l);

			//Exits (Exits type are free space, not doorway)
			doorwayExit.add(new Location(4, 7));

		}
		else if (name.equalsIgnoreCase("Ballroom")) {
			//Entrances
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

			//Exits
			doorwayExit.add(new Location(7, 5));
			doorwayExit.add(new Location(9, 8));
			doorwayExit.add(new Location(14, 8));
			doorwayExit.add(new Location(16, 5));

		}
		else if (name.equalsIgnoreCase("Conservatory")) {
			//Entrances
			Location l = new Location(18, 4);
			l.setType(new Type("Doorway", name));
			doorwayLoc.add(l);

			//Exits
			doorwayExit.add(new Location(18, 5));
		}
		else if (name.equalsIgnoreCase("Dining Room")) {
			//Entrances
			Location l = new Location(7, 12);
			l.setType(new Type("Doorway", name));
			doorwayLoc.add(l);

			Location l1 = new Location(6, 15);
			l1.setType(new Type("Doorway", name));
			doorwayLoc.add(l1);

			//Exits
			doorwayExit.add(new Location(6, 16));
			doorwayExit.add(new Location(8, 12));
		}
		else if (name.equalsIgnoreCase("Billard Room")) {
			//Entrances
			Location l = new Location(18, 9);
			l.setType(new Type("Doorway", name));
			doorwayLoc.add(l);

			Location l1 = new Location(22, 12);
			l1.setType(new Type("Doorway", name));
			doorwayLoc.add(l1);

			//Exits
			doorwayExit.add(new Location(17, 9));
			doorwayExit.add(new Location(22, 13));
		}
		else if (name.equalsIgnoreCase("Library")) {
			//Entrances
			Location l = new Location(20, 14);
			l.setType(new Type("Doorway", name));
			doorwayLoc.add(l);

			Location l1 = new Location(17, 16);
			l1.setType(new Type("Doorway", name));
			doorwayLoc.add(l1);

			//Exits
			doorwayExit.add(new Location(20, 13));
			doorwayExit.add(new Location(16, 16));
		}
		else if (name.equalsIgnoreCase("Lounge")) {
			//Entrances
			Location l = new Location(6, 19);
			l.setType(new Type("Doorway", name));
			doorwayLoc.add(l);

			//Exits
			doorwayExit.add(new Location(6, 18));
		}
		else if (name.equalsIgnoreCase("Study")) {
			//Entrances
			Location l = new Location(17, 21);
			l.setType(new Type("Doorway", name));
			doorwayLoc.add(l);

			//Exits
			doorwayExit.add(new Location(17, 20));
		}
		else {
			//Entrances
			Location l = new Location(11, 18);
			l.setType(new Type("Doorway", name));
			doorwayLoc.add(l);

			Location l1 = new Location(12, 18);
			l1.setType(new Type("Doorway", name));
			doorwayLoc.add(l1);

			Location l2 = new Location(14, 20);
			l2.setType(new Type("Doorway", name));
			doorwayLoc.add(l2);

			//Exits
			doorwayExit.add(new Location(15, 20));
			doorwayExit.add(new Location(12, 17));
			doorwayExit.add(new Location(11, 17));
		}
	}

}