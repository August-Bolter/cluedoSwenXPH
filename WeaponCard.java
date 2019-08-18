package code;

//A weapon card consisting of the name of the weapon
public class WeaponCard extends Card {

	private String weaponName;

	public WeaponCard(String aWeaponName) {
		super(aWeaponName);
		weaponName = aWeaponName;
	}

	public String getName(){
		return weaponName;
	}

}