package code;

//Importing needed libraries
import java.util.*;

import javax.swing.JFrame;

public class Game {
	
	private CardSet solution; //The solution of the game, whoever makes an accusation matching this wins the game
	private List<Card> deck; //A deck of all the cards
	private List<WeaponCard> weaponCards; //All weapons in the game
	private List<CharacterCard> characterCards; //All characters in the game
	private List<RoomCard> roomCards; //All rooms in the game
 
	private List<Player> players; //The players in the game
	private ArrayList<Location> inaccessibleEntrances; //Entrances to a room which the player has been in during their turn
	private Board board; //The cluedo board
	private Player currentPlayer; //The player whos turn it is
	private Turn currentPlayersTurn; //The turn itself
	private boolean canMove; //Can the player move on the board
	private Map<String, Location> startingLocations; //The starting locations of all the character tokens
	private Gui gui; //The graphical user interface of cluedo
	
	public Game(CardSet aSolution, List<Card> aDeck, Board aBoard) {
		//Initializing variables
		solution = aSolution;
		deck = aDeck;
		weaponCards = new ArrayList<WeaponCard>();
		characterCards = new ArrayList<CharacterCard>();
		roomCards = new ArrayList<RoomCard>();
		players = new ArrayList<Player>();
		inaccessibleEntrances = new ArrayList<Location>();
		board = aBoard;
		gui = new Gui(this); //Creating GUI
		gui.setExtendedState(JFrame.MAXIMIZED_BOTH); //Game starts in full screen since it is only functional in full screen.
		gui.setSize(800, 450); //If they chose to not full screen later on then this will be the size of the window
		gui.setVisible(true); //Can now see the GUI
		settingUp(); //Setting up the game
	}

	//Various getters and setters
	
	public Collection<Location> getStartingLocations() {
		return startingLocations.values();
	}
	
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	public Turn getCurrentPlayersTurn() {
		return currentPlayersTurn;
	}
	
	public void setCurrentPlayer(Player p) {
		currentPlayer = p;
	}
	
	public List<Player> getPlayers() {
		return players;
	}
	
	public boolean canMove() {
		return canMove;
	}
	
	public ArrayList<Location> getInaccesibleLocations() {
		return inaccessibleEntrances;
	}
	
	public Gui getGui() {
		return gui;
	}
	
	public Board getBoard() {
		return board;
	}
	
	public CardSet getSolution() {
		return solution;
	}
	
	public List<WeaponCard> getWeaponCards() {
		return weaponCards;
	}
	
	public List<CharacterCard> getCharacterCards() {
		return characterCards;
	}
	
	public List<RoomCard> getRoomCards() {
		return roomCards;
	}
	
	/** Adds all the players to the game and starts the game by creating the first turn
	 * @param The names the players have chosen for themselves
	 * @param The names of the characters the players have chosen */
	public void addPlayers(ArrayList<String> playerNames, ArrayList<String> characterNames) {
		for (int i = 0; i < characterNames.size(); i++) {
			//Creates a new player and add it to the list of player
			Player newPlayer = new Player(new HashSet<Card>(), new CharacterToken(startingLocations.get(characterNames.get(i)), characterNames.get(i)), playerNames.get(i));
			players.add(newPlayer);
		}
		//Start the game by creating the first turn
		currentPlayersTurn = new Turn(players.get(0), this);
		canMove = false; //They can't move since they haven't rolled the dice yet
		
		//Set the locations player for the starting locations
		for (Player p : players) {
			board.getLocation(p.getToken().getX(), p.getToken().getY()).setPlayer(p);
		}
	}

	/** Sets up everything so the game can start, like shuffling cards, dealing cards, setting solution etc. */
	private void settingUp(){

		//Storing startingLocations of the character tokens
		startingLocations = new HashMap<String, Location>();
		startingLocations.put("Mrs. Peacock", new Location(23, 6));
		startingLocations.put("Colonel Mustard", new Location(0, 17));
		startingLocations.put("Miss Scarlett", new Location(7, 24));
		startingLocations.put("Professor Plum", new Location(23, 19));
		startingLocations.put("Mrs. White", new Location(9, 0));
		startingLocations.put("Mr. Green", new Location(14, 0));

		gui.setLabel("How many players do you want?");

		//Creating unshuffled deck
		List<Card> unshuffledDeck = new ArrayList<Card>();

		//Creating and adding weapon cards to our weapon cards list
		WeaponCard candlestick = new WeaponCard("Candlestick");
		WeaponCard dagger = new WeaponCard("Dagger");
		WeaponCard leadPipe = new WeaponCard("Lead Pipe");
		WeaponCard revolver = new WeaponCard("Revolver");
		WeaponCard rope = new WeaponCard("Rope");
		WeaponCard spanner = new WeaponCard("Spanner");
		
		weaponCards.add(candlestick);
		weaponCards.add(dagger);
		weaponCards.add(leadPipe);
		weaponCards.add(revolver);
		weaponCards.add(rope);
		weaponCards.add(spanner);
		//Adding weapon cards to the deck using the list
		for (WeaponCard c : weaponCards) {
			unshuffledDeck.add(new WeaponCard(c.getName()));
		}

		//Creating and adding character cards to our character cards list
		CharacterCard mrsPeacock = new CharacterCard("Mrs. Peacock");
		CharacterCard missScarlett = new CharacterCard("Miss Scarlett");
		CharacterCard colonelMustard = new CharacterCard("Colonel Mustard");
		CharacterCard mrsWhite = new CharacterCard("Mrs. White");
		CharacterCard mrGreen = new CharacterCard("Mr. Green");
		CharacterCard professorPlum = new CharacterCard("Professor Plum");
		
		characterCards.add(mrsPeacock);
		characterCards.add(missScarlett);
		characterCards.add(colonelMustard);
		characterCards.add(mrsWhite);
		characterCards.add(mrGreen);
		characterCards.add(professorPlum);
		for (CharacterCard c : characterCards) {
			unshuffledDeck.add(new CharacterCard(c.getName()));
		}

		//Creating and adding room cards to our room cards list
		RoomCard kitchen = new RoomCard("Kitchen");
		RoomCard ballRoom = new RoomCard("Ballroom");
		RoomCard conservatory = new RoomCard("Conservatory");
		RoomCard diningRoom = new RoomCard("Dining Room");
		RoomCard billardRoom = new RoomCard("Billard Room");
		RoomCard library = new RoomCard("Library");
		RoomCard lounge = new RoomCard("Lounge");
		RoomCard hall = new RoomCard("Hall");
		RoomCard study = new RoomCard("Study");
		
		
		roomCards.add(kitchen);
		roomCards.add(ballRoom);
		roomCards.add(conservatory);
		roomCards.add(diningRoom);
		roomCards.add(billardRoom);
		roomCards.add(library);
		roomCards.add(lounge);
		roomCards.add(hall);
		roomCards.add(study);
		for (RoomCard c : roomCards) {
			unshuffledDeck.add(new RoomCard(c.getName()));
		}

		//Picking solution
		solution = pickSolution(unshuffledDeck); 

		//Removing solution from deck
		unshuffledDeck.remove(solution.getCharacterC());
		unshuffledDeck.remove(solution.getWeaponC());
		unshuffledDeck.remove(solution.getRoomC());

		//Initializing shuffled deck
		deck = new ArrayList<Card>();

		Set<Integer> pickedIndexes = new HashSet<Integer>(); //set of randomly picked indexes

		//Shuffling deck by picking indexes randomly
		while (deck.size() != unshuffledDeck.size()) { //Keep going until we have all the cards
			int randomIndex = (int) ((Math.random()*unshuffledDeck.size())); //Pick a card randomly out of unshuffled deck
			if (!pickedIndexes.contains(randomIndex)) { //If random card selected is already in shuffled deck don't add it
				deck.add(unshuffledDeck.get(randomIndex));
				pickedIndexes.add(randomIndex); //So we get no duplicates
			}
		}

		//Create the cluedo board
		board = new Board(this);

	}

	/** Deals out the shuffled deck to the players. */
	public void deal() {
		List<Card> tempDeck = new ArrayList<Card>(); //Because we want to keep the original deck intact
		
		//Copying the deck
		for (Card c : deck) {
			tempDeck.add(c);
		}
		while (tempDeck.size() > 0) { //Go until we have dealt out all the cards
			for (Player p : players) { //Deal a card to each player
				//To avoid index out of bounds error
				if (tempDeck.size() == 0) {
					break;
				}
				p.addCard(tempDeck.get(0));
				tempDeck.remove(0); 
			}
		}
	}

	/** Picks the solution from the unshuffled deck randomly */
	private CardSet pickSolution(List<Card> unshuffledDeck) {
		WeaponCard solutionWeapon;
		CharacterCard solutionCharacter;
		RoomCard solutionRoom;
		solutionWeapon = (WeaponCard) unshuffledDeck.get((int)(Math.random()*6)); //Gets random weapon by getting random card between 0-5 (weapon indexes)
		solutionCharacter = (CharacterCard) unshuffledDeck.get((int)(Math.random()*((11-6)+1))+6); //Getting random card between 6 and 11 (character indexes)
		solutionRoom = (RoomCard) unshuffledDeck.get((int)(Math.random()*((20-12)+1))+12); //Getting random card between 12 and 20 (room indexes)

		return new CardSet(solutionWeapon, solutionCharacter, solutionRoom);
	}

	/** Is used to roll the dice for the turn
	 * @return each die roll value */
	public ArrayList<Integer> rollDice() {
		canMove = true;
		ArrayList<Integer> diceRolls = new ArrayList<Integer>();
		diceRolls = currentPlayersTurn.rollDice();
		currentPlayersTurn.setSteps(diceRolls.get(0) + diceRolls.get(1));
		return diceRolls;
	}

	/** Moves the player to the location at 'index' if the move is valid
	 * @param The index of the move location in the GUI board
	 * @return Did the move occur */
	public boolean move(int index) {
		//Going from 1D array to 2D array. Going from GUI Board to Game Board
		Location moveLocation = null;
		if (index >= 24) {
			int col = index%24;
			int row = index/24; 
			moveLocation = board.getLocation(col, row);
		}
		else { //Because any positive number under 24 will have a remainder of 0 when divided by 24, so we have separate code for dealing with indexes less than 24
			moveLocation = board.getLocation(index, 0);
		}
		//Move has to be valid for the move to occur
		if (currentPlayersTurn.validMove(moveLocation)) {
			currentPlayersTurn.move(moveLocation, this);
			return true;
		}
		//If move isn't valid then move didn't occur
		return false;
	}
	
	/** Move the player to a rooms exit
	 * @param The index of the exit in the GUI Board
	 * @return whether the player moved to the exit */
	public boolean moveToExit(int index) {
		//Going from 1D array to 2D array. Going from GUI Board to Game Board
		Location moveLocation = null;
		if (index >= 24) {
			int col = index%24;
			int row = index/24; 
			moveLocation = board.getLocation(col, row);
		}
		else {
			moveLocation = board.getLocation(index, 0);
		}
		//If the player clicks on a room exit when they aren't in a room they obviously can't move to that exit
		Room playerRoom = currentPlayer.getToken().getRoom();
		if (playerRoom == null) {
			return false;
		}
		//Find the exit the player clicked on by iterating through the locations and finding matching coordinates
		for (Location l : playerRoom.getExits()) {
			if (l.getX() == moveLocation.getX() && l.getY() == moveLocation.getY()) {
				if(board.getLocation(l.getX(), l.getY()).getPlayer() == null) { //We can only move to unoccupied exits (exits that have no players on them)
					currentPlayersTurn.moveToExit(moveLocation); //Move the player to the exit
					
					//For ensuring they can't re-enter the room on the turn
					currentPlayer.setPastRoom(playerRoom); 
					for (Location loc : playerRoom.getLoc()) {
						inaccessibleEntrances.add(board.getLocation(loc.getX(), loc.getY()));
					}
					return true;
				}
			}
		}
		return false;
	}

	/** Determines if all players have lost (i.e. made a wrong accusation) */
	public boolean allLost() {
		for (Player p : players) {
			if (!p.haveLost()) {
				return false;
			}
		}
		return true;
	}

	/** Switches to the next players turn. */
	public void nextTurn() {
		//Resetting variables
		currentPlayer.setPastRoom(null);
		inaccessibleEntrances = new ArrayList<Location>();
		int playerIndex = 0;
		//Found out what index we are currently at (what player)
		for (Player p : players) {
			if (p == currentPlayer) {
				break;
			}
			playerIndex++;
		}
		Player startingPlayer;
		int count = 0;
		boolean setPlayer = true;
		//Preventing ArrayIndexOutOfBounds errors
		if (playerIndex+1 == players.size()) {
			playerIndex = 0;
			startingPlayer = players.get(playerIndex); //Goes from last player to the first player (the next player)
		}
		else {
			playerIndex++;
			startingPlayer = players.get(playerIndex); //Get the next player
		}
		//Keep going until we find a player who is still in the game
		while (startingPlayer.haveLost()) {
			if (playerIndex+1 == players.size()) {
				playerIndex = 0;
				startingPlayer = players.get(playerIndex);
			}
			else {
				playerIndex++;
				startingPlayer = players.get(playerIndex);
			}
			count++;
			//Just in case we get into a situation where no one can go (but this shouldn't happen since we can determine when all players have lost)
			if (count > 8) {
				setPlayer = false;
				break;
			}
		}
		//If we find the next player then create a turn associated to that player and make sure the player can't move at the very start of their turn
		if (setPlayer) {
			currentPlayer = startingPlayer;
			currentPlayersTurn = new Turn(currentPlayer, this); //Switching turns
			canMove = false;
		}
		
	}

	/** Used to deal with the issue that can arise if a player loses while on a room exit (potentially permanently blocking players in the room from leaving)
	 * It moves these players to the room to avoid this issue. */
	public void moveLostPlayerIfRequired() {
		// TODO Auto-generated method stub
		for (Room r : board.getRoom()) {
			for (Location l : r.getExits()) {
				//Check they are actually on a room exit
				if (l.getX() == currentPlayer.getToken().getX() && l.getY() == currentPlayer.getToken().getY()) {
					//Then move them to the closest room
					currentPlayersTurn.movePlayerToken(r, currentPlayer);
					//Clear the old labels/colors
					gui.clearAfterDeadMovement(l.getX(), l.getY());
				}
			}
		}
	}

}