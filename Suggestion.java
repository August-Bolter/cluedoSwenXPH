package Java;

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

  // line 46 "model.ump"
   public CardSet getSet(){
	   return null;
  }


  public String toString()
  {
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "suggSet" + "=" + (getSuggSet() != null ? !getSuggSet().equals(this)  ? getSuggSet().toString().replaceAll("  ","    ") : "this" : "null");
  }
}