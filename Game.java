package code;

//Importing needed libraries
import java.util.*;

import javax.swing.JFrame;

public class Game {
	
	private CardSet solution; //The solution of the game, whoever makes an accusation matching this wins the game
	private List<Card> deck; //A deck of all the cards
	private List<WeaponCard> weaponCards;
	private List<CharacterCard> characterCards;
	private List<RoomCard> roomCards;
 
	private List<Player> players; //The players in the game
	private ArrayList<Location> inaccessibleEntrances;
	private Board board; //The cluedo board
	private Player currentPlayer;
	private Turn currentPlayersTurn;
	private boolean canMove;
	private Map<String, Location> startingLocations; //The starting locations of all the character tokens
	private Gui gui;
	
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
		gui = new Gui(this);
		gui.setExtendedState(JFrame.MAXIMIZED_BOTH);
		gui.setSize(800, 450);
		gui.setVisible(true);
		settingUp(); //Setting up the game
	}

	//Various getters
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
	
	public void addPlayers(ArrayList<String> playerNames, ArrayList<String> characterNames) {
		for (int i = 0; i < characterNames.size(); i++) {
			Player newPlayer = new Player(new HashSet<Card>(), new CharacterToken(startingLocations.get(characterNames.get(i)), characterNames.get(i)), playerNames.get(i));
			players.add(newPlayer);
		}
		currentPlayersTurn = new Turn(players.get(0), this);
		canMove = false;
		
		//Set the locations player for the starting locations
		for (Player p : players) {
			board.getLocation(p.getToken().getX(), p.getToken().getY()).setPlayer(p);
		}
	}

	/** Sets up everything so the game can start, like shuffling cards, dealing cards, setting character tokens, number of players etc. */
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
		
		/* You will see this boolean variable, while loop setup very frequently in this class. Pretty much every time a user could put in an invalid input 
		 * especially if it would throw an exception. This code setup means that instead of exceptions being thrown the user is asked the question again
		 * as I feel this is more user friendly and will lead to a better experience. */

		//Creating unshuffled deck
		List<Card> unshuffledDeck = new ArrayList<Card>();

		//Creating and adding weapon cards to deck
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
		for (WeaponCard c : weaponCards) {
			unshuffledDeck.add(new WeaponCard(c.getName()));
		}

		//Creating and adding character cards to deck
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

		//Creating and adding room cards to deck
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

	public ArrayList<Integer> rollDice() {
		canMove = true;
		ArrayList<Integer> diceRolls = new ArrayList<Integer>();
		diceRolls = currentPlayersTurn.rollDice();
		currentPlayersTurn.setSteps(diceRolls.get(0) + diceRolls.get(1));
		return diceRolls;
	}

	public boolean move(int index) {
		//Going from 1D to 2D
		Location moveLocation = null;
		if (index >= 24) {
			int col = index%24;
			int row = index/24; 
			moveLocation = board.getLocation(col, row);
		}
		else {
			moveLocation = board.getLocation(index, 0);
		}
		//Move has to be valid
		if (currentPlayersTurn.validMove(moveLocation)) {
			currentPlayersTurn.move(moveLocation, this);
			return true;
		}
		return false;
	}
	

	public boolean moveToExit(int index) {
		// TODO Auto-generated method stub
		Location moveLocation = null;
		if (index >= 24) {
			int col = index%24;
			int row = index/24; 
			moveLocation = board.getLocation(col, row);
		}
		else {
			moveLocation = board.getLocation(index, 0);
		}
		//Move has to be valid
		Room playerRoom = currentPlayer.getToken().getRoom();
		if (playerRoom == null) {
			return false;
		}
		for (Location l : playerRoom.getExits()) {
			if (l.getX() == moveLocation.getX() && l.getY() == moveLocation.getY()) {
				if(board.getLocation(l.getX(), l.getY()).getPlayer() == null) {
					currentPlayersTurn.moveToExit(moveLocation);
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

	public boolean allLost() {
		for (Player p : players) {
			if (!p.haveLost()) {
				return false;
			}
		}
		return true;
	}

	public void nextTurn() {
		currentPlayer.setPastRoom(null);
		inaccessibleEntrances = new ArrayList<Location>();
		int playerIndex = 0;
		for (Player p : players) {
			if (p == currentPlayer) {
				break;
			}
			playerIndex++;
		}
		Player startingPlayer;
		int count = 0;
		boolean setPlayer = true;
		if (playerIndex+1 == players.size()) {
			playerIndex = 0;
			startingPlayer = players.get(playerIndex);
		}
		else {
			playerIndex++;
			startingPlayer = players.get(playerIndex);
		}
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
			if (count > 8) {
				setPlayer = false;
				break;
			}
		}
		if (setPlayer) {
			currentPlayer = startingPlayer;
			currentPlayersTurn = new Turn(currentPlayer, this); //Switching turns
			canMove = false;
		}
		
	}

	public void moveLostPlayerIfRequired() {
		// TODO Auto-generated method stub
		for (Room r : board.getRoom()) {
			for (Location l : r.getExits()) {
				if (l.getX() == currentPlayer.getToken().getX() && l.getY() == currentPlayer.getToken().getY()) {
					currentPlayersTurn.movePlayerToken(r, currentPlayer);
					gui.clearAfterDeadMovement(l.getX(), l.getY());
				}
			}
		}
	}

}