package Java;

import java.util.Set;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.1.4597.b7ac3a910 modeling language!*/



// line 57 "model.ump"
// line 216 "model.ump"
public class Player
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Player Attributes
  private Set<Card> hand;
  private CharacterToken token;
  private boolean haveLost;

  //Player Associations
  private Game game;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Player(Set<Card> aHand, CharacterToken aToken, Game aGame)
  {
    hand = aHand;
    token = aToken;
    haveLost = false;
  }
  
  public boolean haveLost() {
	  return haveLost;
  }
  
  public void setLost(boolean b) {
	  haveLost = b;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setHand(Set<Card> aHand)
  {
    boolean wasSet = false;
    hand = aHand;
    wasSet = true;
    return wasSet;
  }

  public boolean setToken(CharacterToken aToken)
  {
    boolean wasSet = false;
    token = aToken;
    wasSet = true;
    return wasSet;
  }

  public Set<Card> getHand()
  {
    return hand;
  }
  
  public void addCard(Card c) {
	  hand.add(c);
  }

  public CharacterToken getToken()
  {
    return token;
  }
  /* Code from template association_GetOne */
  public Game getGame()
  {
    return game;
  }
  /* Code from template association_SetOneToAtMostN */
  public boolean setGame(Game aGame)
  {
    boolean wasSet = false;
    //Must provide game to player
    if (aGame == null)
    {
      return wasSet;
    }

    //game already at maximum (6)
    if (aGame.numberOfPlayers() >= Game.maximumNumberOfPlayers())
    {
      return wasSet;
    }

    Game existingGame = game;
    game = aGame;
    if (existingGame != null && !existingGame.equals(aGame))
    {
      boolean didRemove = existingGame.removePlayer(this);
      if (!didRemove)
      {
        game = existingGame;
        return wasSet;
      }
    }
    game.addPlayer(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Game placeholderGame = game;
    this.game = null;
    if(placeholderGame != null)
    {
      placeholderGame.removePlayer(this);
    }
  }

  // line 63 "model.ump"
   private void playTurn(){

  }

  // line 66 "model.ump"
   public boolean canRefute(){
	   return false;
  }


  public String toString()
  {
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "hand" + "=" + (getHand() != null ? !getHand().equals(this)  ? getHand().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "token" + "=" + (getToken() != null ? !getToken().equals(this)  ? getToken().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "game = "+(getGame()!=null?Integer.toHexString(System.identityHashCode(getGame())):"null");
  }
}