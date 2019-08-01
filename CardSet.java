package Java;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.1.4597.b7ac3a910 modeling language!*/


import java.util.*;

// line 150 "model.ump"
// line 256 "model.ump"
public class CardSet
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Set Attributes
  private WeaponCard weaponC;
  private CharacterCard characterC;
  private RoomCard roomC;

  //Set Associations
  private List<Card> c;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public CardSet(WeaponCard aWeaponC, CharacterCard aCharacterC, RoomCard aRoomC)
  {
    weaponC = aWeaponC;
    characterC = aCharacterC;
    roomC = aRoomC;
    c = new ArrayList<Card>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setWeaponC(WeaponCard aWeaponC)
  {
    boolean wasSet = false;
    weaponC = aWeaponC;
    wasSet = true;
    return wasSet;
  }

  public boolean setCharacterC(CharacterCard aCharacterC)
  {
    boolean wasSet = false;
    characterC = aCharacterC;
    wasSet = true;
    return wasSet;
  }

  public boolean setRoomC(RoomCard aRoomC)
  {
    boolean wasSet = false;
    roomC = aRoomC;
    wasSet = true;
    return wasSet;
  }

  public WeaponCard getWeaponC()
  {
    return weaponC;
  }

  public CharacterCard getCharacterC()
  {
    return characterC;
  }

  public RoomCard getRoomC()
  {
    return roomC;
  }
  /* Code from template association_GetMany */
  public Card getC(int index)
  {
    Card aC = c.get(index);
    return aC;
  }

  public List<Card> getCa()
  {
    List<Card> newC = Collections.unmodifiableList(c);
    return newC;
  }

  public int numberOfC()
  {
    int number = c.size();
    return number;
  }

  public boolean hasC()
  {
    boolean has = c.size() > 0;
    return has;
  }

  public int indexOfC(Card aC)
  {
    int index = c.indexOf(aC);
    return index;
  }
  /* Code from template association_IsNumberOfValidMethod */
  public boolean isNumberOfCValid()
  {
    boolean isValid = numberOfC() >= minimumNumberOfC() && numberOfC() <= maximumNumberOfC();
    return isValid;
  }
  /* Code from template association_RequiredNumberOfMethod */
  public static int requiredNumberOfC()
  {
    return 3;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfC()
  {
    return 3;
  }
  /* Code from template association_MaximumNumberOfMethod */
  public static int maximumNumberOfC()
  {
    return 3;
  }
  /* Code from template association_AddMNToOnlyOne */
  public Card addC(String aName)
  {
    if (numberOfC() >= maximumNumberOfC())
    {
      return null;
    }
    else
    {
      return new Card(aName, this);
    }
  }

  public boolean addC(Card aC)
  {
    boolean wasAdded = false;
    if (c.contains(aC)) { return false; }
    if (numberOfC() >= maximumNumberOfC())
    {
      return wasAdded;
    }

    CardSet existingSet = aC.getSet();
    boolean isNewSet = existingSet != null && !this.equals(existingSet);

    if (isNewSet && existingSet.numberOfC() <= minimumNumberOfC())
    {
      return wasAdded;
    }

    if (isNewSet)
    {
      aC.setSet(this);
    }
    else
    {
      c.add(aC);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeC(Card aC)
  {
    boolean wasRemoved = false;
    //Unable to remove aC, as it must always have a set
    if (this.equals(aC.getSet()))
    {
      return wasRemoved;
    }

    //set already at minimum (3)
    if (numberOfC() <= minimumNumberOfC())
    {
      return wasRemoved;
    }
    c.remove(aC);
    wasRemoved = true;
    return wasRemoved;
  }

  public void delete()
  {
    while (c.size() > 0)
    {
      Card aC = c.get(c.size() - 1);
      aC.delete();
      c.remove(aC);
    }

  }

  // line 160 "model.ump"
   public WeaponCard getW(){
	   return null;
  }

  // line 164 "model.ump"
   public CharacterCard getCaaa(){
	   return null;
  }

  // line 168 "model.ump"
   public RoomCard getR(){
	   return null;
  }


  public String toString()
  {
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "weaponC" + "=" + (getWeaponC() != null ? !getWeaponC().equals(this)  ? getWeaponC().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "characterC" + "=" + (getCharacterC() != null ? !getCharacterC().equals(this)  ? getCharacterC().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "roomC" + "=" + (getRoomC() != null ? !getRoomC().equals(this)  ? getRoomC().toString().replaceAll("  ","    ") : "this" : "null");
  }
}