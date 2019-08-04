package Java;

import java.io.PrintStream;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.1.4597.b7ac3a910 modeling language!*/


import java.util.*;
import java.util.concurrent.CountDownLatch;

import Java.Type.loctype;

// line 2 "model.ump"
// line 176 "model.ump"
public class Game
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Game Attributes
  private CardSet solution;
  private List<Card> deck;

  //Game Associations
  private List<Turn> turns;
  private List<Player> players;
  private Board board;
private boolean gameOver;
private List<Location> startingLocations;
private List<Card> allCards;
private List<CharacterToken> characterTokens;
private boolean endingEarly;
private Room cantEnter;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Game(CardSet aSolution, List<Card> aDeck, Board aBoard)
  {
    solution = aSolution;
    deck = aDeck;
    turns = new ArrayList<Turn>();
    players = new ArrayList<Player>();
    gameOver = false;
    allCards = new ArrayList<Card>();
    endingEarly = false;
    cantEnter = null;
    characterTokens = new ArrayList<CharacterToken>();
//    if (aBoard == null || aBoard.getGame() != null)
//    {
//      throw new RuntimeException("Unable to create Game due to aBoard. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
//    }
    board = aBoard;
    try {
    	Scanner s = new Scanner(System.in);
    	settingUp(s);
    	playTurns(s);
    }
    catch(Exception e) {
    	e.printStackTrace(System.out);
    }
  }
  
  public List<Location> getStartingLocations() {
	  return startingLocations;
  }
  
  public List<CharacterToken> getCharacterTokens() {
	  return characterTokens;
  }
  
  /** begins game by getting player of players and creating player tokens
   * import java arraylist
 * @param s 
   * @param
   *
  */
  // line 8 "model.ump"
   private void settingUp(Scanner scanner){
	   
	   //Storing startingLocations
	   startingLocations = new ArrayList<Location>();
	   startingLocations.add(new Location(23, 6));
	   startingLocations.add(new Location(0, 17));
	   startingLocations.add(new Location(7, 24));
	   startingLocations.add(new Location(23, 19));
	   startingLocations.add(new Location(9, 0));
	   startingLocations.add(new Location(14, 0));
	   
	   int playerNumbers = 0;
	   System.out.println("How many players do you want? (Enter a number between 1 and 6)\n");
	   boolean isValid = false;
	   while (!isValid) {
		   String playerNumbersAsString = scanner.next();
		   if (playerNumbersAsString.matches("\\d+")) {
			   playerNumbers = Integer.parseInt(playerNumbersAsString);
		   }
		   if (playerNumbers <= 6 && playerNumbers >= 1) {
			   isValid = true;
		   }
		   else {
			   System.out.println("Please enter a number between 1 and 6");
		   }
	   }
		   
		   int count = playerNumbers;
		   ArrayList<String> characterNames  = new ArrayList<String>();
		   characterNames.add("Mrs. Peacock");
		   characterNames.add("Colonel Mustard");
		   characterNames.add("Miss Scarlett");
		   characterNames.add("Professor Plum");
		   characterNames.add("Mrs. White");
		   characterNames.add("Mr. Green");
	
		   while( count > 0) { // ask user to choose from list of characters. Create character tokens and add into a token arraylist
			   System.out.printf("Player %d please select a character using their respective number: \n", (1+playerNumbers-count));
	
			   for(int i = 0 ; i < characterNames.size() ; i++) {
	
				   System.out.println(i + " : " + characterNames.get(i) + "\n");
			   }
			   
			   isValid = false;
			   
			   int playerSelection = -1;
			   while (!isValid) {
				   String playerSelectionString = scanner.next();
				   if (playerSelectionString.matches("\\d+")) {
					   playerSelection = Integer.parseInt(playerSelectionString);
				   }
				   if (playerSelection < characterNames.size() && playerSelection >= 0) {
					   isValid = true;
				   }
				   else {
					   System.out.println("Please enter a valid number.");
				   }
			   }
 
			   String charName = characterNames.get(playerSelection);
			   
			   Location startLocation = getStartingLocation(playerSelection);
	
			   CharacterToken playerToken = new CharacterToken(startLocation, charName);
			   characterTokens.add(playerToken);
	
			   characterNames.remove(playerSelection); //remove character from possible choices
			   startingLocations.remove(startLocation);
			   
			   Player newPlayer = new Player(new HashSet<Card>(), playerToken, this);
			   players.add(newPlayer);
			   count--;
		   }
		   
		   //Creating other character tokens so they can be moved into rooms when suggestions are made (even if they aren't controlled by a player)
		   for (String s : characterNames) {
			   characterTokens.add(new CharacterToken(new Location(-1, -1), s)); //Location set to -1, -1 because these tokens aren't on the board
		   }
	   
	   //Creating unshuffled deck
	   List<Card> unshuffledDeck = new ArrayList<Card>();
	  
	   //Creating and adding weapon cards to deck (pre-determined not random, this could be changed later)
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
	   
	   //Adding character cards to deck
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
	   
	   //Adding room cards to deck
	   RoomCard kitchen = new RoomCard("Kitchen");
	   RoomCard ballRoom = new RoomCard("Ball Room");
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
	   
	   solution = pickSolution(unshuffledDeck); //Picking solution
	   
	   for (Card c : unshuffledDeck) {
		   allCards.add(c);
	   }
	   
	   //Removing solution from deck
	   unshuffledDeck.remove(solution.getCharacterC());
	   unshuffledDeck.remove(solution.getWeaponC());
	   unshuffledDeck.remove(solution.getRoomC());
	   
	   //Initializing shuffled deck
	   deck = new ArrayList<Card>();
	   
	   Set<Integer> pickedIndexes = new HashSet<Integer>();
	   
	   //Shuffling deck by picking indexes randomly
	   while (deck.size() != unshuffledDeck.size()) { //Keep going until we have all the cards
		   int randomIndex = (int) ((Math.random()*unshuffledDeck.size())); //Pick a card randomly out of unshuffled deck
		   if (!pickedIndexes.contains(randomIndex)) { //If random card selected is already in shuffled deck don't add it
			   deck.add(unshuffledDeck.get(randomIndex));
			   pickedIndexes.add(randomIndex); //So we get no duplicates
		   }
	   }
	   
	   board = new Board(this); //tokens have to be passed and stored into the board
	   for (Player p : players) {
		   board.getLocation(p.getToken().getX(), p.getToken().getY()).setPlayer(p);
	   }
	   deal();

  }
   
   private void deal() {
	   List<Card> tempDeck = new ArrayList<Card>();
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

private CardSet pickSolution(List<Card> unshuffledDeck) {
	   WeaponCard solutionWeapon;
	   CharacterCard solutionCharacter;
	   RoomCard solutionRoom;
	   solutionWeapon = (WeaponCard) unshuffledDeck.get((int)(Math.random()*6)); //Gets random weapon by getting random card between 0-5 (weapon indexes)
	   solutionCharacter = (CharacterCard) unshuffledDeck.get((int)(Math.random()*((11-6)+1))+6); //Getting random card between 6 and 11 (character indexes)
	   solutionRoom = (RoomCard) unshuffledDeck.get((int)(Math.random()*((20-12)+1))+12); //Getting random card between 12 and 20 (room indexes)
	   
	   return new CardSet(solutionWeapon, solutionCharacter, solutionRoom);
}

   //Gets starting location depending on character picked
  private Location getStartingLocation(int playerSelection) {
	// TODO Auto-generated method stub
	return startingLocations.get(playerSelection);
  }

  //Creates and plays out turns until the game is finished
  private void playTurns(Scanner sc) {
	PrintStream out = System.out;
	  while(!gameOver()) {
		  for (Player p : players) {
			  if (gameOver()) {
				  break;
			  }
			  if (!p.haveLost()) {
				  endingEarly = false;
				  cantEnter = null;
				  Turn playersTurn = new Turn(this, p); //Create a turn associated with the player
					List<Location> visitedLocations = new ArrayList<Location>();
				  
				  //Printing out location of room entrances
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
				  for (Card c : p.getHand()) {
					  out.print(c.getName() + ",  ");
				  }
				  
				  out.println();
				  out.println();
					  
					  if (p.getToken().inRoomCheck()) {
						  if (p.getToken().getMoved()) {
							  p.getToken().setMoved(false);
							  out.println("You have been moved to " + p.getToken().getRoom().getName() + ". Would you like to make a Suggestion (S) or move out of the room (any other key)?");
							  String answer = sc.nextLine();
							  if (answer.equalsIgnoreCase("S")) {
								  Suggestion suggestion = makeSuggestion(sc, p, playersTurn, out);
								  suggestion.moveTokens(players, board, this);
								  if (suggestion != null) {
									  refuting(suggestion, p, out, sc);
								  }
								  endingEarly = true;
							  }
						  }
						  
						  if (!endingEarly) {
						  
							  List<Location> doorways = p.getToken().getRoom().getExits();
							  out.println("You are in the " + p.getToken().getRoom().getName());
							  for (Location l : p.getToken().getRoom().getDoorwayLocations()) {
								  if (l.getPlayer() != null) {
									  doorways.remove(l);
								  }
							  }
							  sc.nextLine();
							  if (doorways.size() == 0) {
								  out.println("There are no possible doorways to exit out of. Press any key to finish your turn");
								  String answer = sc.nextLine();
								  endingEarly = true;;
							  }
							  
							  else {
								  String output = "Which exit would you like to go to?:\n";
								  boolean validAnswer = false;
								  if (doorways.size() == 1) {
									  output = output + "Entrance 1 (A): (" + doorways.get(0).getX() + ", " + doorways.get(0).getY() + ")\n";
									  out.println(output);
									  while (!validAnswer) {
										  String answer = sc.nextLine();
										  if (answer.equalsIgnoreCase("A")) {
											  validAnswer = true;
											  p.getToken().setXPos(doorways.get(0).getX());
											  p.getToken().setYPos(doorways.get(0).getY());
										  } 
										  else {
											  System.out.println("Invalid answer. Please enter in A");
										  }
									  }
								  }
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
											  p.getToken().setXPos(doorways.get(1).getX());
											  p.getToken().setYPos(doorways.get(1).getY());
										  }
										  else {
											  System.out.println("Invalid answer. Please enter in A or B");
										  }
									  }
								  }
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
											  System.out.println("Invalid answer. Please enter in A or B or C");
										  }
									  }
								  }
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
											  System.out.println("Invalid answer. Please enter in A or B or C or D");
										  }
									  }
								  }
								  cantEnter = p.getToken().getRoom();
								  p.getToken().setRoom(board);
							  }
						  }
					  }
					  
					  if (!endingEarly) {
						  int steps = playersTurn.rollDice(); //Number of steps player can move		
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
							  int i = 0;
							  boolean facingCorrectDirection = false;
							  for (Location l : surrondingLocations) {
								  if (l == null) {
									  out.print(directionLabels[i] + "Nothing, ");
								  }
								  else if (l.getPlayer() != null && !l.getType().getName().contains("Doorway")) {
									  out.print(directionLabels[i] + l.getPlayer().getToken().getName() + ", ");
								  }
								  else {
									  if (l.getType().getLocType() == loctype.DOORWAY) {
										  Room doorwayRoom = null;
										  for (Room r : board.getRoom()) {
											  if (l.getType().getName().contains(r.getName())) {
												  doorwayRoom = r;
											  }
										  }
										  for (Location loc : doorwayRoom.getExits()) {
											  if (loc.getX() == p.getToken().getX() && loc.getY() == p.getToken().getY()) {
												  out.print(directionLabels[i] + l.getType().getName() + ", ");
												  facingCorrectDirection = true;
												  break;
											  }
										  }
										  if (!facingCorrectDirection) {
											  out.print(directionLabels[i] + l.getType().getName() + " (enter through " + board.getDirection(l, board.getClosestExit(l, doorwayRoom)) + "), ");
										  }
									  }
									  else {
										  out.print(directionLabels[i] + l.getType().getName() + ", ");
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
											  if (l.getPlayer() == null) {
												  locInts.add(i);
											  }
										  }
										  else if (l.getType().getName().contains("Doorway")) {
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
							  //Getting direction of leftover locations
							  List<String> directions = new ArrayList<String>();
							  for (int index : locInts) {
								  if (index == 0) {directions.add("west (W), ");}
								  else if (index == 1) {directions.add("northwest (NW) ");}
								  else if (index == 2) {directions.add("north (N) ");}
								  else if (index == 3) {directions.add("northeast (NE) ");}
								  else if (index == 4) {directions.add("east (E) ");}
								  else if (index == 5) {directions.add("southeast (SE) ");}
								  else if (index == 6) {directions.add("south (S) ");}
								  else {directions.add("southwest (SW)");}
							  }
							  out.println();
							  String directionOutput = "Would you like to move ";
							  for (String s : directions) {
								  directionOutput = directionOutput + s;
							  }
							  directionOutput = directionOutput + "?\n";
							  move(sc, out, playersTurn, visitedLocations, locInts, directionOutput);
							  String inRoom = p.getToken().setRoom(board);
							  if (p.getToken().inRoomCheck()) {
								  out.println(inRoom);
								  steps = 0;
							  }
							  steps--;
						  }
							  String answer = "";
							  boolean gonnaSuggest = true;
							  boolean gonnaAccuse = false;
							  if (p.getToken().inRoomCheck()) {
								  //Making a suggestion
								  out.println("Would you like to make a Suggestion (S), or an Accusation (A) or end your turn (enter any other key)?\n");
								  answer = sc.next();
								  if (answer.equals("S")) {
									  Suggestion suggestion = makeSuggestion(sc, p, playersTurn, out);
									  suggestion.moveTokens(players, board, this);
									  if (suggestion != null) {
										  refuting(suggestion, p, out, sc);
									  }
								  }
								  else if (answer.equals("A")) {
									  gonnaAccuse = true;
								  }
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
								  if (answer.equals("A") || gonnaAccuse) {
									  Accusation playersAccusation = makeAccusation(sc, p, playersTurn, out); 
									  CardSet accSet = playersAccusation.getAccSet();
									  if (accSet.getCharacterC() == solution.getCharacterC() && accSet.getRoomC() == solution.getRoomC() && accSet.getWeaponC() == accSet.getWeaponC()) {
										  gameOver = true;
										  out.println("You are right! You have won the game!");
										  break;
									  }
									  else {
										  p.setLost(true);
										  boolean haveAllPlayersLost = true;
										  
										  //Moving dead player if they are blocking doorways
										  int xPos = p.getToken().getX();
										  int yPos = p.getToken().getY();
										  
										  for (Room r : board.getRoom()) {
											  for (Location l : r.getExits()) {
												  if (xPos == l.getX() && yPos == l.getY()) {
													  p.getToken().setXPos(r.getEntrances().get(0).getX());
													  p.getToken().setYPos(r.getEntrances().get(0).getY());
													  break;
												  }
											  }
										  }
										  
										  for (Player player : players) {
											  if (!player.haveLost()) {
												  haveAllPlayersLost = false;
											  }
										  }
										  if (!haveAllPlayersLost) {
											  out.println("You are incorrect. You have lost the game but still have to refute suggestions if you can.\n");
										  }
										  else {
											  gameOver = true;
											  out.println("All players have the lost the game, the game is over");
										  }
									  }
									  try {
										Thread.sleep(3000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								  }
							  }
					  }
			  }
		  }
	  }
	
  }

  private void move(Scanner sc, PrintStream out, Turn playersTurn, List<Location> visitedLocations, List<Integer> locInts, String directionOutput) {
	// TODO Auto-generated method stub
	  out.println(directionOutput);
	  String direction = sc.next();
	  int temp = -2;
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
	  if (locInts.contains(temp)) {
		  List<Integer> coords = playersTurn.move(1, direction, board);
		  visitedLocations.add(board.getLocation(coords.get(0), coords.get(1)));
	  }
	  
	  else {
		  out.println("You cannot move there.");
		  move(sc, out, playersTurn, visitedLocations, locInts, directionOutput);
	  }
	  
  }

private void refuting(Suggestion suggestion, Player p, PrintStream out, Scanner sc) {
	  //Go through each player excluding current turns player
	  for (Player player : players) {
		  if (player != p) {
			  List<Card> matchingCards = new ArrayList<Card>();
			  for (Card c : player.getHand()) {
				  CardSet cs = suggestion.getSuggSet();
				  if (c == cs.getWeaponC() || c == cs.getCharacterC() || c == cs.getRoomC()) {
					  matchingCards.add(c);
					  out.println("Player controlling " + player.getToken().getName() + " has " + c.getName());
				  }
			  }
			  if (matchingCards.size() == 1) {
				  out.println("Player controlling " + player.getToken().getName() + " has " + matchingCards.get(0).getName());
			  }
			  else {
				  out.println("Player controlling " + player.getToken().getName() + ", you have multiple cards that you can use to refute");
				  String output = "Which will card will you show?";
				  boolean validAnswer = false;
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
				  else {
					  output = output + matchingCards.get(0) + "(A)" + matchingCards.get(1) + "(B)" + matchingCards.get(2) + "(C)?";
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
						  else if (answer.equalsIgnoreCase("C")) {
							  validAnswer = true;
							  out.println("Player controlling " + player.getToken().getName() + " has " + matchingCards.get(2).getName());
						  }
						  else {
							  System.out.println("Invalid answer. Please enter in A or B or C");
						  }
					  }
				  }
			  }
		  }
	  }
  }

  private Suggestion makeSuggestion(Scanner sc, Player p, Turn playersTurn, PrintStream out) {
	  //Checking suggestion isValid
	  Room r = null;
	  String roomName = "";
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
	  if (r != p.getToken().getRoom()) {
		  out.println("You have to be in " + roomName + " to make this suggestion. Enter Y if you would like to make another suggestion or if not press N.\n");
		  String answer = sc.next();
		  if (answer.equalsIgnoreCase("Y")) {
			  makeSuggestion(sc, p, playersTurn, out);
		  }
		  return null;
	  }
	  else {
		  WeaponCard weaponCard = null;
		  CharacterCard characterCard = null;
		  RoomCard roomCard = null;
		  //Gets details for the card set
		  out.println("Enter the name of the weapon you think was used in the murder.\n");
		  
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
		  
		  //Boundary testing needed
		  playersTurn.makeSuggestion(new CardSet(weaponCard, characterCard, roomCard));
		  Suggestion playersSuggestion = playersTurn.getSuggestion();  
		  return playersSuggestion;
	  }
	}
  
  private Card findCard(String cardName) {
	  for (Card c : allCards) {
		  if (c.getName().equalsIgnoreCase(cardName)) {
			  return c;
		  }
	  }
	  return null;
  }
  
  private Accusation makeAccusation(Scanner sc, Player p, Turn playersTurn, PrintStream out) {
	  
	  WeaponCard weaponCard = null;
	  CharacterCard characterCard = null;
	  RoomCard roomCard = null;
	  //Gets details for the card set
	  
	  	  out.println("Enter the name of the room you think the murder has taken place in.\n");
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

		  out.println("Enter the name of the weapon you think was used in the murder.\n");

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
		  
		  //Boundary testing needed
		  playersTurn.makeAccusation(new CardSet(weaponCard, characterCard, roomCard));
		  Accusation playersAccusation = playersTurn.getAccusation();  
		  return playersAccusation;
	  }

//A method which determines if the game has been won yet
private boolean gameOver() {
	
	return gameOver;
}

public Game(CardSet aSolution, List<Card> aDeck)
  {
    solution = aSolution;
    deck = aDeck;
    turns = new ArrayList<Turn>();
    players = new ArrayList<Player>();
    board = new Board(this);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setSolution(CardSet aSolution)
  {
    boolean wasSet = false;
    solution = aSolution;
    wasSet = true;
    return wasSet;
  }

  public boolean setDeck(List<Card> aDeck)
  {
    boolean wasSet = false;
    deck = aDeck;
    wasSet = true;
    return wasSet;
  }

  public CardSet getSolution()
  {
    return solution;
  }

  public List<Card> getDeck()
  {
    return deck;
  }
  /* Code from template association_GetMany */
  public Turn getTurn(int index)
  {
    Turn aTurn = turns.get(index);
    return aTurn;
  }

  public List<Turn> getTurns()
  {
    List<Turn> newTurns = Collections.unmodifiableList(turns);
    return newTurns;
  }

  public int numberOfTurns()
  {
    int number = turns.size();
    return number;
  }

  public boolean hasTurns()
  {
    boolean has = turns.size() > 0;
    return has;
  }

  public int indexOfTurn(Turn aTurn)
  {
    int index = turns.indexOf(aTurn);
    return index;
  }
  /* Code from template association_GetMany */
  public Player getPlayer(int index)
  {
    Player aPlayer = players.get(index);
    return aPlayer;
  }

  public List<Player> getPlayers()
  {
    List<Player> newPlayers = Collections.unmodifiableList(players);
    return newPlayers;
  }

  public int numberOfPlayers()
  {
    int number = players.size();
    return number;
  }

  public boolean hasPlayers()
  {
    boolean has = players.size() > 0;
    return has;
  }

  public int indexOfPlayer(Player aPlayer)
  {
    int index = players.indexOf(aPlayer);
    return index;
  }
  /* Code from template association_GetOne */
  public Board getBoard()
  {
    return board;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfTurns()
  {
    return 0;
  }

  public boolean addTurn(Turn aTurn)
  {
    boolean wasAdded = false;
    if (turns.contains(aTurn)) { return false; }
    Game existingGame = aTurn.getGame();
    boolean isNewGame = existingGame != null && !this.equals(existingGame);
    if (isNewGame)
    {
      aTurn.setGame(this);
    }
    else
    {
      turns.add(aTurn);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeTurn(Turn aTurn)
  {
    boolean wasRemoved = false;
    //Unable to remove aTurn, as it must always have a game
    if (!this.equals(aTurn.getGame()))
    {
      turns.remove(aTurn);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addTurnAt(Turn aTurn, int index)
  {
    boolean wasAdded = false;
    if(addTurn(aTurn))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTurns()) { index = numberOfTurns() - 1; }
      turns.remove(aTurn);
      turns.add(index, aTurn);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveTurnAt(Turn aTurn, int index)
  {
    boolean wasAdded = false;
    if(turns.contains(aTurn))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTurns()) { index = numberOfTurns() - 1; }
      turns.remove(aTurn);
      turns.add(index, aTurn);
      wasAdded = true;
    }
    else
    {
      wasAdded = addTurnAt(aTurn, index);
    }
    return wasAdded;
  }
  /* Code from template association_IsNumberOfValidMethod */
  public boolean isNumberOfPlayersValid()
  {
    boolean isValid = numberOfPlayers() >= minimumNumberOfPlayers() && numberOfPlayers() <= maximumNumberOfPlayers();
    return isValid;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfPlayers()
  {
    return 1;
  }
  /* Code from template association_MaximumNumberOfMethod */
  public static int maximumNumberOfPlayers()
  {
    return 6;
  }
  /* Code from template association_AddMNToOnlyOne */
  public Player addPlayer(Card aHand, CharacterToken aToken)
  {
    if (numberOfPlayers() >= maximumNumberOfPlayers())
    {
      return null;
    }
    else
    {
      return new Player(null, aToken, this);
    }
  }

  public boolean addPlayer(Player aPlayer)
  {
    boolean wasAdded = false;
    if (players.contains(aPlayer)) { return false; }
    if (numberOfPlayers() >= maximumNumberOfPlayers())
    {
      return wasAdded;
    }

    Game existingGame = aPlayer.getGame();
    boolean isNewGame = existingGame != null && !this.equals(existingGame);

    if (isNewGame && existingGame.numberOfPlayers() <= minimumNumberOfPlayers())
    {
      return wasAdded;
    }

    if (isNewGame)
    {
      aPlayer.setGame(this);
    }
    else
    {
      players.add(aPlayer);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removePlayer(Player aPlayer)
  {
    boolean wasRemoved = false;
    //Unable to remove aPlayer, as it must always have a game
    if (this.equals(aPlayer.getGame()))
    {
      return wasRemoved;
    }

    //game already at minimum (1)
    if (numberOfPlayers() <= minimumNumberOfPlayers())
    {
      return wasRemoved;
    }
    players.remove(aPlayer);
    wasRemoved = true;
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addPlayerAt(Player aPlayer, int index)
  {
    boolean wasAdded = false;
    if(addPlayer(aPlayer))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPlayers()) { index = numberOfPlayers() - 1; }
      players.remove(aPlayer);
      players.add(index, aPlayer);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMovePlayerAt(Player aPlayer, int index)
  {
    boolean wasAdded = false;
    if(players.contains(aPlayer))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPlayers()) { index = numberOfPlayers() - 1; }
      players.remove(aPlayer);
      players.add(index, aPlayer);
      wasAdded = true;
    }
    else
    {
      wasAdded = addPlayerAt(aPlayer, index);
    }
    return wasAdded;
  }

// line 11 "model.ump"
   private Player whosTurn(){
	   return null;
  }


  public String toString()
  {
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "solution" + "=" + (getSolution() != null ? !getSolution().equals(this)  ? getSolution().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "deck" + "=" + (getDeck() != null ? !getDeck().equals(this)  ? getDeck().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "board = "+(getBoard()!=null?Integer.toHexString(System.identityHashCode(getBoard())):"null");
  }
  
  public static void main(String arg[]) {
	  Game cluedo = new Game(null, null, null); //Change these parameters
  }
}