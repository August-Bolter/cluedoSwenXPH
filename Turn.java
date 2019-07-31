package Java;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.1.4597.b7ac3a910 modeling language!*/



// line 69 "model.ump"
// line 221 "model.ump"
public class Turn
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Turn Associations
  private Game game;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Turn(Game aGame)
  {
    boolean didAddGame = setGame(aGame);
    if (!didAddGame)
    {
      throw new RuntimeException("Unable to create turn due to game. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetOne */
  public Game getGame()
  {
    return game;
  }
  /* Code from template association_SetOneToMany */
  public boolean setGame(Game aGame)
  {
    boolean wasSet = false;
    if (aGame == null)
    {
      return wasSet;
    }

    Game existingGame = game;
    game = aGame;
    if (existingGame != null && !existingGame.equals(aGame))
    {
      existingGame.removeTurn(this);
    }
    game.addTurn(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Game placeholderGame = game;
    this.game = null;
    if(placeholderGame != null)
    {
      placeholderGame.removeTurn(this);
    }
  }

  // line 73 "model.ump"
   public int rollDice(){
	   return 1;
  }

  // line 76 "model.ump"
   public Suggestion makeSuggestion(){
	   return null;
  }

  // line 79 "model.ump"
   public Accusation makeAccusation(){
	   return null;
  }

  // line 83 "model.ump"
   public void move(int steps, String direction){

  }

}