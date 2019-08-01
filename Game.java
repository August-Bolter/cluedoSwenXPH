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
  private Set solution;
  private Card deck;

  //Game Associations
  private List<Turn> turns;
  private List<Player> players;
  private Board board;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Game(Set aSolution, Card aDeck, Board aBoard)
  {
    solution = aSolution;
    deck = aDeck;
    turns = new ArrayList<Turn>();
    players = new ArrayList<Player>();
    if (aBoard == null || aBoard.getGame() != null)
    {
      throw new RuntimeException("Unable to create Game due to aBoard. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    board = aBoard;
  }

  public Game(Set aSolution, Card aDeck)
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

  public boolean setSolution(Set aSolution)
  {
    boolean wasSet = false;
    solution = aSolution;
    wasSet = true;
    return wasSet;
  }

  public boolean setDeck(Card aDeck)
  {
    boolean wasSet = false;
    deck = aDeck;
    wasSet = true;
    return wasSet;
  }

  public Set getSolution()
  {
    return solution;
  }

  public Card getDeck()
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
  /* Code from template association_AddManyToOne */
  public Turn addTurn()
  {
    return new Turn(this);
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
      return new Player(aHand, aToken, this);
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

	   try (Scanner scanner = new Scanner(System.in)) {

	   System.out.println("Select number of players from 1 to 6 \n");
	   int playerNumbers = scanner.nextInt();

	   //
	   int count = playerNumbers;
	   ArrayList<String> charachterNames  = new ArrayList<String>();
	   charachterNames.add("Mrs Peacock");
	   charachterNames.add("Colonel Mustard");
	   charachterNames.add(" Miss Scarlett");
	   charachterNames.add("Professor Plum");
	   charachterNames.add("Mrs White");
	   charachterNames.add("Rev.Green");
	   ArrayList <Token> characterTokens = new ArrayList<Token>();

	   while( count > 0) { // ask user to choose from list of characters. Create character tokens and add into a token arraylist
		   System.out.printf("Player %d select character: \n", count);

		   for(int i = 0 ; i < charachterNames.size() ; i++) {

			   System.out.println(i + " : " + charachterNames.get(i) + "\n");
		   }

		   System.out.println("select a letter \n");
		   int playerSelection = scanner.nextInt(); //scan for player character. should throw an exception for an invalid input

		   Token playerToken = new Token(charachterNames.get(playerSelection));
		   characterTokens.add(playerToken);

		   charachterNames.remove(playerSelection); //remove character from possible choices
		   count--;
	   }
	   scanner.close();
	   } //end of try

	   Board gameBoard = new Board(this); //tokens have to be passed and stored into the board

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
}