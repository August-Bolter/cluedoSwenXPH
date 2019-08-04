package Java;

import java.util.List;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.1.4597.b7ac3a910 modeling language!*/



// line 41 "model.ump"
// line 206 "model.ump"
public class Suggestion
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Suggestion Attributes
  private CardSet suggSet;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Suggestion(CardSet aSuggSet)
  {
    suggSet = aSuggSet;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setSuggSet(CardSet aSuggSet)
  {
    boolean wasSet = false;
    suggSet = aSuggSet;
    wasSet = true;
    return wasSet;
  }

  public CardSet getSuggSet()
  {
    return suggSet;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "suggSet" + "=" + (getSuggSet() != null ? !getSuggSet().equals(this)  ? getSuggSet().toString().replaceAll("  ","    ") : "this" : "null");
  }

  public void moveTokens(List<Player> players, Board board, Game game) {
	  // TODO Auto-generated method stub
	  Room room = board.getRoom(suggSet.getRoomC().getName());
	  WeaponToken suggestWeapon = null;
	  for (WeaponToken w : room.getWeapon()) {
		  if (w.getName().equalsIgnoreCase(suggSet.getWeaponC().getName())) {
			  suggestWeapon = w;
		  }
	  }
	  if (!room.getWeapon().contains(suggestWeapon)) {
		  for (Room r : board.getRoom()) {
			  for (WeaponToken w : r.getWeapon()) {
				  if (w == suggestWeapon) {
					  r.removeWeapon(w);
				  }
			  }
		  }
		  room.addWeapon(suggestWeapon);
	  }
	  
	  boolean movedPlayer = false;
	  for (Player p : players) {
		  if (p.getToken().getName().equalsIgnoreCase(suggSet.getCharacterC().getName())) {
			  if (p.getToken().getRoom() != room) {
				  p.getToken().setRoom(room);
				  p.getToken().setMoved(true);
				  movedPlayer = true;
			  }
		  }
	  }
	  if (!movedPlayer) {
		  for (CharacterToken c : game.getCharacterTokens()) {
			  if (c.getName().equalsIgnoreCase(suggSet.getCharacterC().getName())) {
				  c.setRoom(room);
			  }
		  }
	  }
  }
}