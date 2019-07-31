package Java;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.1.4597.b7ac3a910 modeling language!*/



// line 103 "model.ump"
// line 236 "model.ump"
public class CharacterCard extends Card
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //CharacterCard Attributes
  private String characterName;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public CharacterCard(String aName, Set aSet, String aCharacterName)
  {
    super(aName, aSet);
    characterName = aCharacterName;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setCharacterName(String aCharacterName)
  {
    boolean wasSet = false;
    characterName = aCharacterName;
    wasSet = true;
    return wasSet;
  }

  public String getCharacterName()
  {
    return characterName;
  }

  public void delete()
  {
    super.delete();
  }

  // line 110 "model.ump"
   public String getName(){
	   return characterName;
  }


  public String toString()
  {
    return super.toString() + "["+
            "characterName" + ":" + getCharacterName()+ "]";
  }
}