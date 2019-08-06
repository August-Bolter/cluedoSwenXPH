package Java;

/**
 * Weapon tokens 
 * Weapons are placed in a random room during game setup
 * Able to be moved into rooms after a suggestion
 */
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