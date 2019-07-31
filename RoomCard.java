package Java;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.1.4597.b7ac3a910 modeling language!*/



// line 113 "model.ump"
// line 241 "model.ump"
public class RoomCard extends Card
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //RoomCard Attributes
  private String roomName;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public RoomCard(String aName, Set aSet, String aRoomName)
  {
    super(aName, aSet);
    roomName = aRoomName;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setRoomName(String aRoomName)
  {
    boolean wasSet = false;
    roomName = aRoomName;
    wasSet = true;
    return wasSet;
  }

  public String getRoomName()
  {
    return roomName;
  }

  public void delete()
  {
    super.delete();
  }

  // line 120 "model.ump"
   public String getName(){
	   return roomName;
  }


  public String toString()
  {
    return super.toString() + "["+
            "roomName" + ":" + getRoomName()+ "]";
  }
}