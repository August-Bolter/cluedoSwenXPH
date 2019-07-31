package Java;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.1.4597.b7ac3a910 modeling language!*/



// line 143 "model.ump"
// line 251 "model.ump"
public class WeaponToken extends Token
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //WeaponToken Attributes
  private Room residingRoom;
  private String weaponName;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public WeaponToken(String aName, Room aResidingRoom, String aWeaponName)
  {
    super(aName);
    residingRoom = aResidingRoom;
    weaponName = aWeaponName;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setResidingRoom(Room aResidingRoom)
  {
    boolean wasSet = false;
    residingRoom = aResidingRoom;
    wasSet = true;
    return wasSet;
  }

  public boolean setWeaponName(String aWeaponName)
  {
    boolean wasSet = false;
    weaponName = aWeaponName;
    wasSet = true;
    return wasSet;
  }

  public Room getResidingRoom()
  {
    return residingRoom;
  }

  public String getWeaponName()
  {
    return weaponName;
  }

  public void delete()
  {
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+
            "weaponName" + ":" + getWeaponName()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "residingRoom" + "=" + (getResidingRoom() != null ? !getResidingRoom().equals(this)  ? getResidingRoom().toString().replaceAll("  ","    ") : "this" : "null");
  }
}