package Java;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.1.4597.b7ac3a910 modeling language!*/


import java.util.*;
import java.util.concurrent.CountDownLatch;

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
private boolean gameWon;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Game(CardSet aSolution, List<Card> aDeck, Board aBoard)
  {
    solution = aSolution;
    deck = aDeck;
    turns = new ArrayList<Turn>();
    players = new ArrayList<Player>();
    gameWon = false;
//    if (aBoard == null || aBoard.getGame() != null)
//    {
//      throw new RuntimeException("Unable to create Game due to aBoard. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
//    }
    board = aBoard;
    settingUp();
    playTurns();
  }

  //Creates and plays out turns until the game is finished
  private void playTurns() {
	  while(!gameWon()) {
		  for (Player p : players) {
			  //Printing out center point locations of the game, not included yet, will include if there is room
			  //System.out.println("Room Center-Points:\n");
			  //for (Room r: board.getRoom()) {
				//  System.out.println(r.getName() + " (" + r.getCenterLoc() + ", " + ")");
			  //}
			  
			  //Printing the players hand 
			  System.out.print("Your hand is:\n");
			  for (Card c : p.getHand()) {
				  System.out.print(c.getName() + "  ");
			  }
			  
			  //Printing out location of room entrances
			  System.out.println("Room hallway locations:\n");
			  for (Room r : board.getRoom()) {
				  int hallwayNumber = 0;
				  for (Location l : r.getEntrances()) {
					  hallwayNumber++;
					  System.out.println(r.getName() + " Entrance " + hallwayNumber + ": (" + l.getX() + ", " + l.getY() + ")\n");
				  }
			  }
			  
			  if (gameWon()) {
				  break;
			  }
			  if (!p.haveLost()) {
				  Turn playersTurn = new Turn(this, p); //Create a turn associated with the player
				  turns.add(playersTurn); //Maybe not needed
				  int steps = playersTurn.rollDice(); //Number of steps player can move				  
				  //Player must move all their steps
				  while (steps < 0) {
					  //Printing out players current location
					  int playerX = p.getToken().getX();
					  int playerY = p.getToken().getY();
					  System.out.println("Your current location is: (" + playerX + ", " + playerY + ")\n");
					  
					  //Printing out surrondings of player location 
					  List<Location> surrondingLocations = board.getSurrondingLocations(playerX, playerY);
					  String[] directionLabels = {"To the west of you is ", "To the northwest of you is ", "To the north of you is ", "To the northeast of you is ", "To the east of you is ", "To the southeast of you is ", "To the south of you is ", "To the southwest of you is "};
					  int i = 0;
					  for (Location l : surrondingLocations) {
						  System.out.println(directionLabels[i] + l.getType().toString() + ", ");
						  i++;
					  }
					  
					  //Determing what directions the player can move from their current location
					  i = 0;
					  List<Integer> locInts = new ArrayList<Integer>(); //Location indexes = index relates to direction
					  for (Location l : surrondingLocations) {
						  if (l.getType().getString() != "Wall") { //We can't move into a Wall block so don't add them
							  locInts.add(i);
						  }
						  i++;
					  }
					  //Getting direction of leftover locations
					  List<String> directions = new ArrayList<String>();
					  for (int index : locInts) {
						  if (index == 0) {directions.add("west (W), ");}
						  else if (index == 1) {directions.add("northwest (NW), ");}
						  else if (index == 2) {directions.add("north (N), ");}
						  else if (index == 3) {directions.add("northeast (NE), ");}
						  else if (index == 4) {directions.add("east (E), ");}
						  else if (index == 5) {directions.add("southeast (SE), ");}
						  else if (index == 6) {directions.add("south (S), ");}
						  else {directions.add("southwest (SW)");}
					  }
					  
					  String directionOutput = "Would you like to move ";
					  for (String s : directions) {
						  directionOutput = directionOutput + s;
					  }
					  directionOutput = directionOutput + "?\n";
					  try {
						  Scanner sc = new Scanner(System.in);
						  playersTurn.move(1, sc.next());
					  }
					  catch (Exception e) {}
					  
					  steps--;
				  }
				  try {
					  Scanner sc = new Scanner(System.in);
					  String answer;
					  if (p.getToken().inRoomCheck()) {
						  //Making a suggestion
						  System.out.println("Would you like to make a Suggestion (S), or an Accusation (A) or end your turn (E)?\n");
						  answer = sc.next();
						  if (answer.equals("S")) {
							  Suggestion suggestion = makeSuggestion(sc, p, playersTurn);
							  if (suggestion != null) {
								  refuting(suggestion, p);
							  }
						  }
					  }
					  
					  System.out.println("Would you like an Accusation (A) or end your turn (E)?\n");
					  //Making an accusation
					  if (answer.equals("A")) {
						  Accusation playersAccusation = makeAccusation(sc, p, playersTurn); 
						  CardSet accSet = playersAccusation.getAccSet();
						  if (accSet.getCharacterC() == solution.getCharacterC() && accSet.getRoomC() == solution.getRoomC() && accSet.getWeaponC() == accSet.getWeaponC()) {
							  gameWon = true;
							  System.out.println("You are right! You have won the game!");
							  break;
						  }
						  else {
							  p.setLost(true);
							  System.out.println("You are incorrect. You have lost the game but still have to refute suggestions if you can.\n");
						  }
					  }
		
				  }
				  catch (Exception e) {}
				  
			  }
		  }
	  }
	
  }

  private void refuting(Suggestion suggestion, Player p) {
	  //Go through each player excluding current turns player
	  for (Player player : players) {
		  if (player != p) {
			  for (Card c : player.getHand()) {
				  CardSet cs = suggestion.getSet();
				  if (c == cs.getWeaponC() || c == cs.getCharacterC() | c == cs.getRoomC()) {
					  System.out.println("Player controlling " + p.getToken().getName() + " " + " has " + c.getName());
				  }
			  }
		  }
	  }
  }

  private Suggestion makeSuggestion(Scanner sc, Player p, Turn playersTurn) {
	  //Checking suggestion isValid
	  System.out.println("Enter the name of the room you think the murder has taken place in.\n");
	  String roomName = sc.next();
	  boolean isValid = false;
	  Room r = board.getRoom(roomName);
	  for (CharacterToken t : r.getTokens()) {
		  if (t == p.getToken()) {
			  isValid = true;
		  }
	  }
	  if (!isValid) {
		  System.out.println("You have to be in " + roomName + " to make this suggestion. Enter Y if you would like to make another suggestion or if not press N.\n");
		  String answer = sc.next();
		  if (answer.equals("Y")) {
			  makeSuggestion(sc, p, playersTurn);
		  }
		  return null;
	  }
	  else {
		  //Gets details for the card set
		  System.out.println("Enter the name of the weapon you think was used in the murder.\n");
		  String weaponName = sc.next();
		  WeaponCard weaponCard;
		  CharacterCard characterCard;
		  RoomCard roomCard;
		  System.out.println("Enter the name of the person you think is the murderer");
		  String personName = sc.next();
		  //Finding cards (may want to move this to a different location)
		  for (Card c : deck) {
			  if (c.getName().equals(weaponName)) {
				  weaponCard = (WeaponCard) c;
			  }
			  else if (c.getName().equals(personName)) {
				  characterCard = (CharacterCard) c;
			  }
			  else if (c.getName().equals(roomName)) {
				  roomCard = (RoomCard) c;
			  }
		  }
		  
		  //Boundary testing needed
		  
		  playersTurn.makeSuggestion(new CardSet(weaponCard, characterCard, roomCard));
		  Suggestion playersSuggestion = playersTurn.getSuggestion();  
		  return playersSuggestion;
	  }
	}
  
  private Accusation makeAccusation(Scanner sc, Player p, Turn playersTurn) {
	  //Gets details for the card set
	  	  System.out.println("Enter the name of the room you think the murder has taken place in.\n");
	  	  String roomName = sc.next();
		  System.out.println("Enter the name of the weapon you think was used in the murder.\n");
		  String weaponName = sc.next();
		  WeaponCard weaponCard = null;
		  CharacterCard characterCard = null;
		  RoomCard roomCard = null;
		  System.out.println("Enter the name of the person you think is the murderer");
		  String personName = sc.next();
		  //Finding cards (may want to move this to a different location)
		  for (Card c : deck) {
			  if (c.getName().equals(weaponName)) {
				  weaponCard = (WeaponCard) c;
			  }
			  else if (c.getName().equals(personName)) {
				  characterCard = (CharacterCard) c;
			  }
			  else if (c.getName().equals(roomName)) {
				  roomCard = (RoomCard) c;
			  }
		  }
		  
		  //Boundary testing needed
		  
		  playersTurn.makeAccusation(new CardSet(weaponCard, characterCard, roomCard));
		  Accusation playersAccusation = playersTurn.getAccusation();  
		  return playersAccusation;
	  }

//A method which determines if the game has been won yet
private boolean gameWon() {
	
	return gameWon ;
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

  public void delete()
  {
    for(int i=turns.size(); i > 0; i--)
    {
      Turn aTurn = turns.get(i - 1);
      aTurn.delete();
    }
    for(int i=players.size(); i > 0; i--)
    {
      Player aPlayer = players.get(i - 1);
      aPlayer.delete();
    }
    Board existingBoard = board;
    board = null;
    if (existingBoard != null)
    {
      existingBoard.delete();
    }
  }

  /** begins game by getting player of players and creating player tokens
   * import java arraylist
   * @param
   *
  */
  // line 8 "model.ump"
   private void settingUp(){

	   int playerNumbers;
	   try (Scanner scanner = new Scanner(System.in)) {

	   System.out.println("How many players do you want? (Enter a number between 1 and 6)\n");
	   playerNumbers = scanner.nextInt();
	   //Testing for boundaries needs some improvement to repeatedly check, not just once
	   if (playerNumbers > 6 || playerNumbers < 1) {
		   //Throw an exception
	   }
	   
	   int count = playerNumbers;
	   ArrayList<String> characterNames  = new ArrayList<String>();
	   characterNames.add("Mrs. Peacock");
	   characterNames.add("Colonel Mustard");
	   characterNames.add("Miss Scarlett");
	   characterNames.add("Professor Plum");
	   characterNames.add("Mrs. White");
	   characterNames.add("Mr. Green");
	   ArrayList <Token> characterTokens = new ArrayList<Token>();

	   while( count > 0) { // ask user to choose from list of characters. Create character tokens and add into a token arraylist
		   System.out.printf("Player %d select character: \n", count);

		   for(int i = 0 ; i < characterNames.size() ; i++) {

			   System.out.println(i + " : " + characterNames.get(i) + "\n");
		   }

		   System.out.println("Please select a character using their respective number \n");
		   int playerSelection = scanner.nextInt(); //scan for player character. should throw an exception for an invalid input
		   String charName = characterNames.get(playerSelection);
		   
		   Location startLocation = getStartingLocation(playerSelection);

		   CharacterToken playerToken = new CharacterToken(startLocation, charName);
		   characterTokens.add(playerToken);

		   characterNames.remove(playerSelection); //remove character from possible choices
		   
		   Player newPlayer = new Player(null, playerToken, this);
		   players.add(newPlayer);
		   count--;
	   }
	   scanner.close();
	   } //end of try
	   
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
	   
	   //Initializing shuffled deck
	   deck = new ArrayList<Card>();
	   
	   Set<Integer> pickedIndexes = new HashSet<Integer>();
	   
	   //Shuffling deck by picking indexes randomly
	   while (deck.size() != unshuffledDeck.size()) { //Keep going until we have all the cards
		   int randomIndex = (int) (Math.random()*unshuffledDeck.size()); //Pick a card randomly out of unshuffled deck
		   if (!pickedIndexes.contains(randomIndex)) { //If random card selected is already in shuffled deck don't add it
			   deck.add(unshuffledDeck.get(randomIndex));
			   pickedIndexes.add(randomIndex); //So we get no duplicates
		   }
	   }
	   
	   Board gameBoard = new Board(this); //tokens have to be passed and stored into the board
	   solution = pickSolution();
	   deal(playerNumbers);

  }
   
   private void deal(int pNum) {
	   List<Card> tempDeck = deck;
	   while (tempDeck.size() < 0) { //Go until we have dealt out all the cards
		   for (Player p : players) { //Deal a card to each player
			   //To avoid index out of bounds error
			   if (tempDeck.size() == 0) {
				   break;
			   }
			   p.getHand().add(deck.get(0));
			   tempDeck.remove(0);
		   }
	   }
   }

private CardSet pickSolution() {
	   WeaponCard solutionWeapon;
	   CharacterCard solutionCharacter;
	   RoomCard solutionRoom;
	   solutionWeapon = (WeaponCard) deck.get((int)(Math.random()*5)); //Gets random weapon by getting random card between 0-5 (weapon indexes)
	   solutionCharacter = (CharacterCard) deck.get((int)(Math.random()*((11-6)+1))+6); //Getting random card between 6 and 11 (character indexes)
	   solutionRoom = (RoomCard) deck.get((int)(Math.random()*((20-12)+1))+12); //Getting random card between 12 and 20 (room indexes)
	   
	   return new CardSet(solutionWeapon, solutionCharacter, solutionRoom);
   }

   //Gets starting location depending on character picked
  private Location getStartingLocation(int playerSelection) {
	// TODO Auto-generated method stub
	if (playerSelection == 0) { //Mrs Peacock
		return new Location(23, 5);
	}
	else if (playerSelection == 1) { //Colonel Mustard
		return new Location(0, 17);
	}
	else if (playerSelection == 2) { //Miss  Scarlett
		return new Location(7, 24);
	}
	else if (playerSelection == 3) { //Professor Plum
		return new Location(23, 19);
	}
	else if (playerSelection == 4) { //Mrs White
		return new Location(9, 0);
	}
	else { //Mr. Green
		return new Location(14, 0);
	}
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