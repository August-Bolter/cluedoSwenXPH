package Java;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.1.4597.b7ac3a910 modeling language!*/



// line 49 "model.ump"
// line 211 "model.ump"
public class Accusation
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Accusation Attributes
  private Set accSet;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Accusation(Set aAccSet)
  {
    accSet = aAccSet;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setAccSet(Set aAccSet)
  {
    boolean wasSet = false;
    accSet = aAccSet;
    wasSet = true;
    return wasSet;
  }

  public Set getAccSet()
  {
    return accSet;
  }

  public void delete()
  {}

  // line 54 "model.ump"
   public Set getSet(){
	   return accSet;
  }


  public String toString()
  {
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "accSet" + "=" + (getAccSet() != null ? !getAccSet().equals(this)  ? getAccSet().toString().replaceAll("  ","    ") : "this" : "null");
  }
}