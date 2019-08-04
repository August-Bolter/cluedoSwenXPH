package Java;

/** The structure of a token in Cluedo (WeaponToken, CharacterToken) */
public class Token {

	private String name;

	public Token(String aName) {
		name = aName;
	}

	public void setName(String aName) {
		name = aName;
	}

	public String getName() {
		return name;
	}

}