package code;

/** A character card with the name of the character associated with the card.*/
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