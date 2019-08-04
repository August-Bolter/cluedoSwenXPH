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

	public WeaponCard getWeaponC() {
		return weaponC;
	}

	public CharacterCard getCharacterC() {
		return characterC;
	}

	public RoomCard getRoomC() {
		return roomC;
	}
}