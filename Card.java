package Java;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.1.4597.b7ac3a910 modeling language!*/



// line 29 "model.ump"
// line 196 "model.ump"
public class Card
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Card Attributes
  private String name;

  //Card Associations
  private Set set;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Card(String aName, Set aSet)
  {
    name = aName;
    boolean didAddSet = setSet(aSet);
    if (!didAddSet)
    {
      throw new RuntimeException("Unable to create c due to set. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public String getName()
  {
    return name;
  }
  /* Code from template association_GetOne */
  public Set getSet()
  {
    return set;
  }
  /* Code from template association_SetOneToAtMostN */
  public boolean setSet(Set aSet)
  {
    boolean wasSet = false;
    //Must provide set to c
    if (aSet == null)
    {
      return wasSet;
    }

    //set already at maximum (3)
    if (aSet.numberOfC() >= Set.maximumNumberOfC())
    {
      return wasSet;
    }

    Set existingSet = set;
    set = aSet;
    if (existingSet != null && !existingSet.equals(aSet))
    {
      boolean didRemove = existingSet.removeC(this);
      if (!didRemove)
      {
        set = existingSet;
        return wasSet;
      }
    }
    set.addC(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Set placeholderSet = set;
    this.set = null;
    if(placeholderSet != null)
    {
      placeholderSet.removeC(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "name" + ":" + getName()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "set = "+(getSet()!=null?Integer.toHexString(System.identityHashCode(getSet())):"null");
  }
}