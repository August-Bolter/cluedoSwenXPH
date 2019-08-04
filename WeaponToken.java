package Java;

//A weapon token, placed in a random room at the start and moved around different rooms as the game plays out because of suggestions.
public class WeaponToken extends Token {

	private String weaponName;

	public WeaponToken(String aWeaponName) {
		super(aWeaponName);
		weaponName = aWeaponName;
	}

	public String getWeaponName() {
		return weaponName;
	}

}