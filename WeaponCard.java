package Java;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.1.4597.b7ac3a910 modeling language!*/



// line 93 "model.ump"
// line 231 "model.ump"
public class WeaponCard extends Card
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //WeaponCard Attributes
  private String weaponName;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public WeaponCard(String aName, Set aSet, String aWeaponName)
  {
    super(aName, aSet);
    weaponName = aWeaponName;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setWeaponName(String aWeaponName)
  {
    boolean wasSet = false;
    weaponName = aWeaponName;
    wasSet = true;
    return wasSet;
  }

  public String getWeaponName()
  {
    return weaponName;
  }

  public void delete()
  {
    super.delete();
  }

  // line 100 "model.ump"
   public String getName(){
	   return null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "weaponName" + ":" + getWeaponName()+ "]";
  }
}