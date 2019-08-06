package Java;

/** Contains a set of cards, one weapon card, one character card and one room card in that order. */
public class CardSet {

	private WeaponCard weaponC;
	private CharacterCard characterC;
	private RoomCard roomC;

	public CardSet(WeaponCard aWeaponC, CharacterCard aCharacterC, RoomCard aRoomC) {
		weaponC = aWeaponC;
		characterC = aCharacterC;
		roomC = aRoomC;
	}
	
	//gets the Weapon card in the CardSet
	public WeaponCard getWeaponCard() {
		return weaponC;
	}
	 //gets the CharacterCard in the CardSet
	public CharacterCard getCharacterCard() {
		return characterC;
	}
	
	//gets the RoomCard in the CardSet
	public RoomCard getRoomCard() {
		return roomC;
	}
}