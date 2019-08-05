package Java;

/** A character card consisting of the name of the character.*/
public class CharacterCard extends Card
{
	
	private String characterName;

	public CharacterCard(String aCharacterName) {
		super(aCharacterName);
		characterName = aCharacterName;
	}

	public String getName(){
		return characterName;
	}

}