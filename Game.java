package Java;

//Importing needed libraries
import java.io.PrintStream;
import java.util.*;
import Java.Type.loctype;

public class Game {

	private CardSet solution; //The solution of the game, whoever makes an accusation matching this wins the game
	private List<Card> deck; //A deck of all the cards
 
	private List<Player> players; //The players in the game
	private Board board; //The cluedo board
	private boolean gameOver; //true if the game is over, false otherwise
	private List<Location> startingLocations; //The starting locations of all the character tokens
	private List<Card> allCards; //All the cards including the cards in the solution
	private List<CharacterToken> characterTokens; //All the character tokens in cluedo
	private boolean endingEarly; //True if someones turn has ended earlier than it usually does, false otherwise
	private Room cantEnter; //True if a player can't enter a room because they have already been in it before

	public Game(CardSet aSolution, List<Card> aDeck, Board aBoard) {
		//Initializing variables
		solution = aSolution;
		deck = aDeck;
		players = new ArrayList<Player>();
		gameOver = false;
		allCards = new ArrayList<Card>();
		endingEarly = false;
		cantEnter = null;
		characterTokens = new ArrayList<CharacterToken>();
		board = aBoard;
		try {
			Scanner s = new Scanner(System.in); //We need this to read the users input
			settingUp(s); //Setting up the game
			playTurns(s); //Playing the game
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
		}
	}

	//Various getters
	public List<Location> getStartingLocations() {
		return startingLocations;
	}

	public List<CharacterToken> getCharacterTokens() {
		return characterTokens;
	}

	public static void main(String arg[]) {
		Game cluedo = new Game(null, null, null); //All set to null since the game is what defines these parameters, so they are set later on.
	}

	/** Sets up everything so the game can start, like shuffling cards, dealing cards, setting character tokens, number of players etc. */
	private void settingUp(Scanner scanner){

		//Storing startingLocations of the character tokens
		startingLocations = new ArrayList<Location>();
		startingLocations.add(new Location(23, 6));
		startingLocations.add(new Location(0, 17));
		startingLocations.add(new Location(7, 24));
		startingLocations.add(new Location(23, 19));
		startingLocations.add(new Location(9, 0));
		startingLocations.add(new Location(14, 0));

		System.out.println("How many players do you want? (Enter a number between 1 and 6)\n");
		
		/* You will see this boolean variable, while loop setup very frequently in this class. Pretty much every time a user could put in an invalid input 
		 * especially if it would throw an exception. This code setup means that instead of exceptions being thrown the user is asked the question again
		 * as I feel this is more user friendly and will lead to a better experience. */
		
		int playerNumbers = 0; //Set to 0 so that if the user puts in something other than a number it will ask them to try again
		boolean isValid = false; //Did the user give a valid answer
		while (!isValid) { //Keep going until the user gives a valid answer
			String playerNumbersAsString = scanner.next();
			if (playerNumbersAsString.matches("\\d+")) { //Check that they entered an actual number without parsing the text
				playerNumbers = Integer.parseInt(playerNumbersAsString); //Parse the text since we know its a number
			}
			//Since playerNumbers initial value is 0, this means if the input wasn't a number or wasn't a valid number then ask again
			if (playerNumbers <= 6 && playerNumbers >= 1) {
				isValid = true; //Otherwise break out of while loop
			}
			else {
				System.out.println("Please enter a number between 1 and 6");
			}
		}

		int count = playerNumbers; //How many players we are going to have to set up
		
		ArrayList<String> characterNames  = new ArrayList<String>(); //All the possible character names the character tokens can have
		characterNames.add("Mrs. Peacock");
		characterNames.add("Colonel Mustard");
		characterNames.add("Miss Scarlett");
		characterNames.add("Professor Plum");
		characterNames.add("Mrs. White");
		characterNames.add("Mr. Green");

		while( count > 0) { // ask user to choose from list of characters. Create character tokens and add into a token arraylist
			//playerNumbers+count since we want to start from 1 to the number of players
			System.out.printf("Player %d please select a character using their respective number: \n\n", (1+playerNumbers-count)); 

			for(int i = 0 ; i < characterNames.size() ; i++) {

				System.out.println(i + " : " + characterNames.get(i));
			}

			isValid = false;

			int playerSelection = -1; //Again to have default value as an invalid input
			while (!isValid) {
				String playerSelectionString = scanner.next();
				if (playerSelectionString.matches("\\d+")) {
					playerSelection = Integer.parseInt(playerSelectionString);
				}
				//Have they picked a valid number
				if (playerSelection < characterNames.size() && playerSelection >= 0) {
					isValid = true;
				}
				else {
					System.out.println("Please enter a valid number.");
				}
			}

			//Getting character name
			String charName = characterNames.get(playerSelection);

			//Getting starting location based on character name
			Location startLocation = getStartingLocation(playerSelection);

			//Creating and adding character token
			CharacterToken playerToken = new CharacterToken(startLocation, charName);
			characterTokens.add(playerToken);

			characterNames.remove(playerSelection); //Remove character from possible choices
			startingLocations.remove(startLocation); //Remove starting location from possible choices

			//Create and add a new player with the character token they chose
			Player newPlayer = new Player(new HashSet<Card>(), playerToken);
			players.add(newPlayer);
			count--;
		}

		//Creating other character tokens so they can be moved into rooms when suggestions are made (even if they aren't controlled by a player)
		for (String s : characterNames) {
			characterTokens.add(new CharacterToken(new Location(-1, -1), s)); //Location set to -1, -1 because these tokens aren't on the board
		}

		//Creating unshuffled deck
		List<Card> unshuffledDeck = new ArrayList<Card>();

		//Creating and adding weapon cards to deck
		WeaponCard candlestick = new WeaponCard("Candlestick");
		WeaponCard dagger = new WeaponCard("Dagger");
		WeaponCard leadPipe = new WeaponCard("Lead Pipe");
		WeaponCard revolver = new WeaponCard("Revolver");
		WeaponCard rope = new WeaponCard("Rope");
		WeaponCard spanner = new WeaponCard("Spanner");
		unshuffledDeck.add(candlestick);
		unshuffledDeck.add(dagger);
		unshuffledDeck.add(leadPipe);
		unshuffledDeck.add(revolver);
		unshuffledDeck.add(rope);
		unshuffledDeck.add(spanner);

		//Creating and adding character cards to deck
		CharacterCard mrsPeacock = new CharacterCard("Mrs. Peacock");
		CharacterCard missScarlett = new CharacterCard("Miss Scarlett");
		CharacterCard colonelMustard = new CharacterCard("Colonel Mustard");
		CharacterCard mrsWhite = new CharacterCard("Mrs. White");
		CharacterCard mrGreen = new CharacterCard("Mr. Green");
		CharacterCard professorPlum = new CharacterCard("Professor Plum");
		unshuffledDeck.add(mrsPeacock);
		unshuffledDeck.add(missScarlett);
		unshuffledDeck.add(colonelMustard);
		unshuffledDeck.add(mrsWhite);
		unshuffledDeck.add(mrGreen);
		unshuffledDeck.add(professorPlum);

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
		unshuffledDeck.add(kitchen);
		unshuffledDeck.add(ballRoom);
		unshuffledDeck.add(conservatory);
		unshuffledDeck.add(diningRoom);
		unshuffledDeck.add(billardRoom);
		unshuffledDeck.add(library);
		unshuffledDeck.add(lounge);
		unshuffledDeck.add(hall);
		unshuffledDeck.add(study);

		//Picking solution
		solution = pickSolution(unshuffledDeck); 
		System.out.println("The solution has been randomly picked");

		//Makes a copy of all cards include ones in the solution before unshuffledDeck has solution removed from it
		for (Card c : unshuffledDeck) {
			allCards.add(c);
		}

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
		
		//Set the locations player for the starting locations
		for (Player p : players) {
			board.getLocation(p.getToken().getX(), p.getToken().getY()).setPlayer(p);
		}
		
		deal(); //Deal the cards
		
		//Print out information to let the player know whats going on
		System.out.println("The cards have been shuffled");
		System.out.println("The cards have been dealt out");
		System.out.println("The weapon tokens have been randomly placed\n");
		
		//So the player has enough time to read the output before more text is displayed
		try {
			Thread.sleep(3000);
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	/** Deals out the shuffled deck to the players. */
	private void deal() {
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

	/** Gets starting location depending on character picked */
	private Location getStartingLocation(int playerSelection) {
		return startingLocations.get(playerSelection);
	}
	

	/** A method which returns if the game has been won yet */
	private boolean gameOver() {
		return gameOver;
	}

	/** A large method which performs all the possible actions a turn could have like moving in and out of rooms and around the board, making suggestions etc. */
	private void playTurns(Scanner sc) {
		PrintStream out = System.out; //Using a PrintStream now so everything is printed out in the correct order
		while(!gameOver()) { //Keep going until the game is over
			for (Player p : players) {
				if (gameOver()) { //Because the game can end while still iterating through the for loop when need this if statement to break out of it
					break;
				}
				if (!p.haveLost()) { //A player only can have a turn if they are still in the game
					endingEarly = false; //A players turn always starts off not ending early (obviously)
					cantEnter = null; //Also a players turn always starts off being able to enter any room (because they haven't left any yet)
					Turn playersTurn = new Turn(p); //Create a turn associated with the player
					
					/*A list of locations that have been visited by the player during their turn and therefore can't be moved onto in the same turn */
					List<Location> visitedLocations = new ArrayList<Location>(); 

					//Printing out location of room entrances so the player has some locations to aim towards heading and can strategise a plan
					out.println("Room hallway locations:");
					for (Room r : board.getRoom()) {
						int hallwayNumber = 0;
						for (Location l : r.getEntrances()) {
							hallwayNumber++;
							out.println(r.getName() + " Entrance " + hallwayNumber + ": (" + l.getX() + ", " + l.getY() + ")");
						}
					}
					out.println();
					out.println("It is " + p.getToken().getName() + " turn");

					//Printing the players hand 
					out.print("Your hand is:\n");
					int handIndex = 0;
					for (Card c : p.getHand()) { //Outputting the name of every card in the hand
						handIndex++;
						//No comma on the last card for punctuation
						if (handIndex == p.getHand().size()) {
							out.print(c.getName());
						}
						else {
							out.print(c.getName() + ",  ");
						}
					}

					out.println();
					out.println();

					//If the player is in a room
					if (p.getToken().inRoomCheck()) {
						//If the player is in that room because of a previous suggestion
						if (p.getToken().getMoved()) {
							p.getToken().setMoved(false); //Resetting variable
							/* According to the official rules of Cluedo a player when moved into a room because of a suggestion can make a suggestion in that room
							 * immediately on their next turn or can choose to move out of that room*/
							out.println("You have been moved to " + p.getToken().getRoom().getName() + ". Would you like to make a Suggestion (S) or move out of the room (any other key)?");
							sc.nextLine();
							String answer = sc.nextLine();
							//If they want to make a suggestion
							if (answer.equalsIgnoreCase("S")) {
								Suggestion suggestion = makeSuggestion(sc, p, playersTurn, out);
								suggestion.moveTokens(players, board, this);
								if (suggestion != null) {
									refuting(suggestion, p, out, sc);
								}
								endingEarly = true; //Their turn ends early, a players turn always ends after making a suggestion
								
								//NOTE: There is no clarification in the rules on whether a player can make an accusation after this so I assume they can't
							}
						}

						//If the players turn hasn't ended early
						if (!endingEarly) {

							List<Location> doorways = p.getToken().getRoom().getExits(); //The doorway exits of the room the player is in
							out.println("You are in the " + p.getToken().getRoom().getName()); 
							for (Location l : p.getToken().getRoom().getDoorwayLocations()) {
								if (l.getPlayer() != null) { //We can't move onto a doorway exit that is occupied by another player
									doorways.remove(l);
								}
							}
							sc.nextLine();
							//An unfortunate rare possibility for the player
							if (doorways.size() == 0) {
								out.println("There are no possible doorways to exit out of. Press any key to finish your turn");
								String answer = sc.nextLine();
								endingEarly = true;;
							}

							else {
								//Otherwise we ask what exit the player would like to go to, a room could have 1 to 4 exits so we have to account for this
								
								String output = "Which exit would you like to go to?:\n";
								boolean validAnswer = false;
								
								//1 exit
								if (doorways.size() == 1) {
									output = output + "Entrance 1 (A): (" + doorways.get(0).getX() + ", " + doorways.get(0).getY() + ")\n";
									out.println(output);
									while (!validAnswer) {
										String answer = sc.nextLine();
										if (answer.equalsIgnoreCase("A")) {
											validAnswer = true;
											//Setting the players tokens coordinates to the doorways exit coordinates
											p.getToken().setXPos(doorways.get(0).getX());
											p.getToken().setYPos(doorways.get(0).getY());
										} 
										else {
											System.out.println("Invalid answer. Please enter in A");
										}
									}
								}
								
								//2 exits
								else if (doorways.size() == 2) {
									output = output + "Entrance 1 (A): (" + doorways.get(0).getX() + ", " + doorways.get(0).getY() + ")\n";
									output = output + "Entrance 2 (B): (" + doorways.get(1).getX() + ", " + doorways.get(1).getY() + ")\n";
									out.println(output);
									while (!validAnswer) {
										String answer = sc.nextLine();
										if (answer.equalsIgnoreCase("A")) {
											validAnswer = true;
											p.getToken().setXPos(doorways.get(0).getX());
											p.getToken().setYPos(doorways.get(0).getY());
										} 
										else if (answer.equalsIgnoreCase("B")) {
											validAnswer = true;
											//B = 2nd entrance so we get the 2nd exit (at index 1)
											p.getToken().setXPos(doorways.get(1).getX());
											p.getToken().setYPos(doorways.get(1).getY());
										}
										else {
											System.out.println("Invalid answer. Please enter in A or B");
										}
									}
								}
								
								//3 exits
								else if (doorways.size() == 3) {
									output = output + "Entrance 1 (A): (" + doorways.get(0).getX() + ", " + doorways.get(0).getY() + ")\n";
									output = output + "Entrance 2 (B): (" + doorways.get(1).getX() + ", " + doorways.get(1).getY() + ")\n";
									output = output + "Entrance 3 (C): (" + doorways.get(2).getX() + ", " + doorways.get(2).getY() + ")\n";
									out.println(output);
									while (!validAnswer) {
										String answer = sc.nextLine();
										if (answer.equalsIgnoreCase("A")) {
											validAnswer = true;
											p.getToken().setXPos(doorways.get(0).getX());
											p.getToken().setYPos(doorways.get(0).getY());
										} 
										else if (answer.equalsIgnoreCase("B")) {
											validAnswer = true;
											p.getToken().setXPos(doorways.get(1).getX());
											p.getToken().setYPos(doorways.get(1).getY());
										}
										else if (answer.equalsIgnoreCase("C")) {
											validAnswer = true;
											p.getToken().setXPos(doorways.get(2).getX());
											p.getToken().setYPos(doorways.get(2).getY());
										}
										else {
											System.out.println("Invalid answer. Please enter in A, B or C");
										}
									}
								}
								
								//4 exits
								else {
									output = output + "Entrance 1 (A): (" + doorways.get(0).getX() + ", " + doorways.get(0).getY() + ")\n";
									output = output + "Entrance 2 (B): (" + doorways.get(1).getX() + ", " + doorways.get(1).getY() + ")\n";
									output = output + "Entrance 3 (C): (" + doorways.get(2).getX() + ", " + doorways.get(2).getY() + ")\n";
									output = output + "Entrance 4 (D): (" + doorways.get(3).getX() + ", " + doorways.get(3).getY() + ")\n";
									out.println(output);
									while (!validAnswer) {
										String answer = sc.nextLine();
										if (answer.equalsIgnoreCase("A")) {
											validAnswer = true;
											p.getToken().setXPos(doorways.get(0).getX());
											p.getToken().setYPos(doorways.get(0).getY());
										} 
										else if (answer.equalsIgnoreCase("B")) {
											validAnswer = true;
											p.getToken().setXPos(doorways.get(1).getX());
											p.getToken().setYPos(doorways.get(1).getY());
										}
										else if (answer.equalsIgnoreCase("C")) {
											validAnswer = true;
											p.getToken().setXPos(doorways.get(2).getX());
											p.getToken().setYPos(doorways.get(2).getY());
										}
										else if (answer.equalsIgnoreCase("D")) {
											validAnswer = true;
											p.getToken().setXPos(doorways.get(3).getX());
											p.getToken().setYPos(doorways.get(3).getY());
										}
										else {
											System.out.println("Invalid answer. Please enter in A, B, C or D");
										}
									}
								}
								cantEnter = p.getToken().getRoom(); //Since we just exited the room we can't go back in it on the same turn
								p.getToken().setRoom(board); //This sets the players room to null since they are no longer in a room
								board.getLocation(p.getToken().getX(), p.getToken().getY()).setPlayer(p); //Set the doorway exit location player to the current player.
							}
						}
					}

					if (!endingEarly) {
						int steps = playersTurn.rollDice(); //Number of steps player can move determined by dice roll
						out.println("You rolled a " + steps);
						//Player must move all their steps
						while (steps > 0) {
							//Printing out players current location
							int playerX = p.getToken().getX();
							int playerY = p.getToken().getY();
							out.println("Your current location is: (" + playerX + ", " + playerY + ")\n");
							
							//Printing out surroundings of player location 
							List<Location> surrondingLocations = board.getSurroundingLocations(playerX, playerY);
							String[] directionLabels = {"West: ", "NorthWest: ", "North: ", "NorthEast: ", "East: ", "SouthEast: ", "South: ", "SouthWest: "};
							int i = 0; //index of directionLabels
							boolean facingCorrectDirection = false; //If we are next to a room doorway are we facing the correct direction to enter the room
							String punctuationOG = ", ";
							int surrondingLocationsIndex = 0; //For punctuation
							for (Location l : surrondingLocations) {
								surrondingLocationsIndex++;
								if (surrondingLocationsIndex == surrondingLocations.size()) {
									punctuationOG = ""; //For punctuation
								}
								if (l == null) { //In the case that the "location" is outside the board we output "Nothing" for that direction
									out.print(directionLabels[i] + "Nothing" + punctuationOG);
								}
								//If type is room we print room name
								else if (l.getPlayer() != null && !l.getType().getName().contains("Doorway")) {
									out.print(directionLabels[i] + l.getPlayer().getToken().getName() + punctuationOG);
								}
								else {
									//If type is doorway we check if we are in the right direction
									if (l.getType().getLocType() == loctype.DOORWAY) {
										//Getting the actual room
										Room doorwayRoom = null;
										for (Room r : board.getRoom()) {
											if (l.getType().getName().contains(r.getName())) {
												doorwayRoom = r;
											}
										}
										//Checking if the players token is on any of the doorway exits
										for (Location loc : doorwayRoom.getExits()) {
											if (loc.getX() == p.getToken().getX() && loc.getY() == p.getToken().getY()) {
												out.print(directionLabels[i] + l.getType().getName() + punctuationOG);
												facingCorrectDirection = true;
												break;
											}
										}
										//If not we output the doorway and mention what direction they must be heading it, to enter the room
										if (!facingCorrectDirection) {
											out.print(directionLabels[i] + l.getType().getName() + " (enter through " + board.getDirection(l, board.getClosestExit(l, doorwayRoom)) + ")" + punctuationOG);
										}
									}
									//Otherwise we don't print this and this will indicate they are on the exit.
									else {
										out.print(directionLabels[i] + l.getType().getName() + punctuationOG);
									}
								}
								i++;
							}

							//Determining what directions the player can move from their current location
							i = 0;
							List<Integer> locInts = new ArrayList<Integer>(); //Location indexes = index relates to direction
							for (Location l : surrondingLocations) {
								if (l != null) {
									if ((i+2)%2 == 0 && !visitedLocations.contains(l)) { //Don't consider NE, SE etc.
										//We can only move into doorways or corridors (also known as free space)
										if (l.getType().getName() == "Free space") {
											if (l.getPlayer() == null) { //We can't move into a location that is occupied by another player
												locInts.add(i);
											}
										}
										else if (l.getType().getName().contains("Doorway")) {
											/* We check that we aren't re-entering a room and are entering it in the correct direction. Note that we don't check
											 * for players on locations. This is because the doorway is in the room and multiple tokens can be in the same 
											 * room so I think it's fine to have them stacked if its on a doorway since it won't affect gameplay at all. */
											if (cantEnter != null) {
												if (!l.getType().getName().contains(cantEnter.getName()) && facingCorrectDirection) {
													locInts.add(i);
												}
											}
											else if (facingCorrectDirection) {
												locInts.add(i);
											}
										}
									}
								}
								i++;
							}
							//Getting direction of locations that we can move to
							List<String> directions = new ArrayList<String>();
							String punctuation = ", ";
							int locIntsIndex = 0;
							for (int index : locInts) {
								locIntsIndex++;
								if (locIntsIndex == locInts.size()) {
									punctuation = "";
								}
								//Adding to the string the moveable directions
								if (index == 0) {directions.add("West (W)" + punctuation);}
								else if (index == 1) {directions.add("Northwest(NW)" + punctuation);}
								else if (index == 2) {directions.add("North(N)" + punctuation);}
								else if (index == 3) {directions.add("Northeast(NE)" + punctuation);}
								else if (index == 4) {directions.add("East(E)" + punctuation);}
								else if (index == 5) {directions.add("Southeast(SE)" + punctuation);}
								else if (index == 6) {directions.add("South(S)" + punctuation);}
								else {directions.add("Southwest(SW)" + punctuation);}
							}
							out.println();
							String directionOutput = "Would you like to move ";
							for (String s : directions) {
								directionOutput = directionOutput + s;
							}
							directionOutput = directionOutput + "?\n";
							move(sc, out, playersTurn, visitedLocations, locInts, directionOutput); //Moving the token
							String inRoom = p.getToken().setRoom(board); //Checking if we are now in a room and if so then setting the token to the room
							if (p.getToken().inRoomCheck()) {
								out.println(inRoom); //This prints that the player has entered the room
								steps = 0; //A player cannot move once they enter a room
							}
							steps--;
						}
						
						//Asks the player what they would like to do next
						String answer = "";
						boolean gonnaSuggest = true; //Are they going to make a suggestion (or if they aren't in a room)
						boolean gonnaAccuse = false; //Are they going to make an accusation
						if (p.getToken().inRoomCheck()) {
							out.println("Would you like to make a Suggestion (S), or an Accusation (A) or end your turn (enter any other key)?\n");
							answer = sc.next();
							//If they chose suggestion
							if (answer.equalsIgnoreCase("S")) {
								Suggestion suggestion = makeSuggestion(sc, p, playersTurn, out);
								suggestion.moveTokens(players, board, this);
								if (suggestion != null) { //If they decide to actually make the suggestion
 									refuting(suggestion, p, out, sc); //Then force other players to refute
								}
							}
							//If they choose to make an accusation instead
							else if (answer.equalsIgnoreCase("A")) {
								gonnaAccuse = true;
							}
							//Otherwise the players turn ends
							else {
								gonnaSuggest = false;
							}
						}
						if (gonnaSuggest) {
							if (!gonnaAccuse) {
								out.println("Would you like to make an Accusation (A) or end your turn (enter any other key)?\n");
								//Making an accusation
								answer = sc.next();
							}
							//Making an accusation
							if (answer.equalsIgnoreCase("A") || gonnaAccuse) {
								Accusation playersAccusation = makeAccusation(sc, p, playersTurn, out); 
								CardSet accSet = playersAccusation.getAccSet();
								//Checks if accusation matches solution
								if (accSet.getCharacterC() == solution.getCharacterC() && accSet.getRoomC() == solution.getRoomC() && accSet.getWeaponC() == accSet.getWeaponC()) {
									gameOver = true; //Game is now over
									out.println("You are right! You have won the game!");
								}
								else {
									p.setLost(true);
									boolean haveAllPlayersLost = true; //Have all the players lost and therefore the game is over

									//Moving dead player if they are blocking doorways
									int xPos = p.getToken().getX();
									int yPos = p.getToken().getY();

									//Checking if they are in any of the doorway exits
									for (Room r : board.getRoom()) {
										for (Location l : r.getExits()) {
											//If they are then just move them to the first doorway of the room so they aren't in the active players way
											if (xPos == l.getX() && yPos == l.getY()) {
												p.getToken().setXPos(r.getEntrances().get(0).getX());
												p.getToken().setYPos(r.getEntrances().get(0).getY());
												break; //No point looking further
											}
										}
									}

									//Checking if all players have lost
									for (Player player : players) {
										if (!player.haveLost()) {
											haveAllPlayersLost = false;
										}
									}
									
									//Printing output depending on if all players have lost
									if (!haveAllPlayersLost) {
										out.println("You are incorrect. You have lost the game but still have to refute suggestions if you can.\n");
									}
									else {
										gameOver = true;
										out.println("You are incorrect. You have lost the game.");
										out.println("Now all players have the lost the game so the game is over");
									}
								}
								
								//So players have enough time to read the output
								try {
									Thread.sleep(3000);
								} 
								catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}

	}

	/** A recursive method which checks if the input the user put in was valid i.e. matches the possible directions their token can take and if so moves them
	 * otherwise it is called recursively to re-ask the question (this was before I wrote the small while loop algorithm) */
	private void move(Scanner sc, PrintStream out, Turn playersTurn, List<Location> visitedLocations, List<Integer> locInts, String directionOutput) {
		// TODO Auto-generated method stub
		out.println(directionOutput);
		String direction = sc.next();
		int temp = -2;
		//Don't consider diagonal directions
		if (direction.equalsIgnoreCase("W")) {
			temp = 0;
		}
		else if (direction.equalsIgnoreCase("N")) {
			temp = 2;
		}
		else if (direction.equalsIgnoreCase("E")) {
			temp = 4;
		}
		else if (direction.equalsIgnoreCase("S")){
			temp = 6;
		}
		//If the possible directions include the direction the user chose
		if (locInts.contains(temp)) {
			//Then moving the token and store the location they used to be in so they can't revisit that location in the same turn.
			List<Integer> coords = playersTurn.move(direction, board);
			visitedLocations.add(board.getLocation(coords.get(0), coords.get(1)));
		}

		else {
			out.println("You cannot move there.");
			move(sc, out, playersTurn, visitedLocations, locInts, directionOutput);
		}

	}

	/** Forces the other players to refute the suggestion if they can. If they have multiple possible cards to refute with it gives them the choice of which one
	 * to show. */
	private void refuting(Suggestion suggestion, Player p, PrintStream out, Scanner sc) {
		//Go through each player excluding current turns player
		for (Player player : players) {
			if (player != p) {
				List<Card> matchingCards = new ArrayList<Card>(); //For storing cards which match with the suggestion
				for (Card c : player.getHand()) {
					CardSet cs = suggestion.getSuggSet();
					if (c.getName().equalsIgnoreCase(cs.getWeaponC().getName()) || c.getName().equalsIgnoreCase(cs.getCharacterC().getName()) || c.getName().equalsIgnoreCase(cs.getRoomC().getName())) { //Do they match if so add them
						matchingCards.add(c);
					}
				}
				//Auto shows the card if they only have one possible card
				if (matchingCards.size() == 1) {
					out.println("Player controlling " + player.getToken().getName() + " has " + matchingCards.get(0).getName());
				}
				else {
					out.println("Player controlling " + player.getToken().getName() + ", you have multiple cards that you can use to refute");
					String output = "Which will card will you show?";
					boolean validAnswer = false;
					//If they have two cards, then pick which one they will show (A or B)
					if (matchingCards.size() == 2) {
						output = output + matchingCards.get(0) + "(A)" + matchingCards.get(1) + "(B)?";
						out.println(output);
						while (!validAnswer) {
							String answer = sc.nextLine();
							if (answer.equalsIgnoreCase("A")) {
								validAnswer = true;
								out.println("Player controlling " + player.getToken().getName() + " has " + matchingCards.get(0).getName());
							}
							else if (answer.equalsIgnoreCase("B")) {
								validAnswer = true;
								out.println("Player controlling " + player.getToken().getName() + " has " + matchingCards.get(1).getName());
							}
							else {
								System.out.println("Invalid answer. Please enter in A or B");
							}
						}
					}
					else if (matchingCards.size() == 3){ //Then they must have three matching cards
						output = output + matchingCards.get(0) + "(A)" + matchingCards.get(1) + "(B)" + matchingCards.get(2) + "(C)?";
						out.println(output);
						while (!validAnswer) {
							String answer = sc.nextLine();
							if (answer.equalsIgnoreCase("A")) {
								validAnswer = true;
								//Index based off answer
								out.println("Player controlling " + player.getToken().getName() + " has " + matchingCards.get(0).getName());
							}
							else if (answer.equalsIgnoreCase("B")) {
								validAnswer = true;
								out.println("Player controlling " + player.getToken().getName() + " has " + matchingCards.get(1).getName());
							}
							else if (answer.equalsIgnoreCase("C")) {
								validAnswer = true;
								out.println("Player controlling " + player.getToken().getName() + " has " + matchingCards.get(2).getName());
							}
							else {
								System.out.println("Invalid answer. Please enter in A, B or C");
							}
						}
					}
				}
				out.println();
			}
		}
	}
	
	/** Finds any card in the gamethat matches the String passed and returns that card */
	private Card findCard(String cardName) {
		for (Card c : allCards) {
			if (c.getName().equalsIgnoreCase(cardName)) {
				return c;
			}
		}
		return null;
	}

	/** Make a suggestion, make sure its a valid suggestion (i.e. they are in the room they are using in the suggestion) and return it. */
	private Suggestion makeSuggestion(Scanner sc, Player p, Turn playersTurn, PrintStream out) {
		Room r = null;
		String roomName = "";
		//Asking about the room
		out.println("Enter the name of the room you think the murder has taken place in.\n");
		sc.nextLine();
		boolean validAnswer = false;
		while (!validAnswer) {
			roomName = sc.nextLine();
			r = board.getRoom(roomName);
			if (r != null) {
				validAnswer = true;
			}
			else {
				out.println("Please enter in a valid room name (Kitchen, Dining Room, Ball Room, Conservatory, Billard Room, Library, Study, Hall, Lounge).");
			}
		}
		//Checking suggestion is valid by checking player is actually in the room they mentioned
		if (r != p.getToken().getRoom()) {
			out.println("You have to be in " + roomName + " to make this suggestion. Enter Y if you would like to make another suggestion or if not press N.\n");
			String answer = sc.next();
			if (answer.equalsIgnoreCase("Y")) {
				return makeSuggestion(sc, p, playersTurn, out); //Reask the suggestion questions
			}
			return null; //They can choose to back out of the suggestion (in case they weren't aware of this rule), null is returned if this is the case.
		}
		else {
			WeaponCard weaponCard = null;
			CharacterCard characterCard = null;
			RoomCard roomCard = null;
			
			//Ask and find out what weapon was used for the suggestion (asks again if they miss spell it for example)
			out.println("Enter the name of the weapon you think was used in the murder.\n");
			validAnswer = false;
			while (!validAnswer) {
				String weaponName = sc.nextLine();
				Card card = findCard(weaponName); //Finds the card
				if (card != null) {
					validAnswer = true;
					weaponCard = (WeaponCard) card; //Sets the suggestion weapon card
				}
				else {
					out.println("Please enter in a valid weapon name (Candlestick, Dagger, Lead Pipe, Revolver, Rope, Spanner)");
				}
			}

			//Ask and find out what character was used in the suggestion (asks again if they miss spell it for example)
			out.println("Enter the name of the person you think is the murderer");

			validAnswer = false;
			while (!validAnswer) {
				String characterName = sc.nextLine();
				Card card = findCard(characterName);
				if (card != null) {
					validAnswer = true;
					characterCard = (CharacterCard) card;
				}
				else {
					out.println("Please enter in a valid character name (Miss Scarlett, Colonel Mustard, Mrs. White, Mr. Green, Mrs. Peacock, Professor Plum)");
				}
			}

			roomCard = (RoomCard) findCard(roomName);

			//Create the suggestion, obtain it and return it
			playersTurn.makeSuggestion(new CardSet(weaponCard, characterCard, roomCard));
			Suggestion playersSuggestion = playersTurn.getSuggestion();  
			return playersSuggestion;
		}
	}

	/** Makes an accusation, makes sure the cards are in the game and returns the accusation */
	private Accusation makeAccusation(Scanner sc, Player p, Turn playersTurn, PrintStream out) {

		WeaponCard weaponCard = null;
		CharacterCard characterCard = null;
		RoomCard roomCard = null;

		//Asks and find out the room the murder took place in
		out.println("Enter the name of the room the murder took place in.\n");
		sc.nextLine();
		boolean validAnswer = false;
		while (!validAnswer) {
			String roomName = sc.nextLine();
			Card card = findCard(roomName);
			if (card != null) {
				validAnswer = true;
				roomCard = (RoomCard) card;
			}
			else {
				out.println("Please enter in a valid room name (Kitchen, Dining Room, Ball Room, Conservatory, Billard Room, Library, Study, Hall, Lounge).");
			}
		}

		//Asks and find out the weapon used in the accusation
		out.println("Enter the name of the weapon used in the murder.\n");
		validAnswer = false;
		while (!validAnswer) {
			String weaponName = sc.nextLine();
			Card card = findCard(weaponName);
			if (card != null) {
				validAnswer = true;
				weaponCard = (WeaponCard) card;
			}
			else {
				out.println("Please enter in a valid weapon name (Candlestick, Dagger, Lead Pipe, Revolver, Rope, Spanner)");
			}
		}

		//Asks and find out the murderer in question 
		out.println("Enter the name of the murderer");
		validAnswer = false;
		while (!validAnswer) {
			String characterName = sc.nextLine();
			Card card = findCard(characterName);
			if (card != null) {
				validAnswer = true;
				characterCard = (CharacterCard) card;
			}
			else {
				out.println("Please enter in a valid character name (Miss Scarlett, Colonel Mustard, Mrs. White, Mr. Green, Mrs. Peacock, Professor Plum)");
			}
		}

		//Boundary testing needed
		playersTurn.makeAccusation(new CardSet(weaponCard, characterCard, roomCard));
		Accusation playersAccusation = playersTurn.getAccusation();  
		return playersAccusation;
	}

}