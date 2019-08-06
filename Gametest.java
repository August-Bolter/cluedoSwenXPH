package cleudo.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.xml.stream.events.Characters;

import org.junit.Rule;
import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.junit.rules.ExpectedException;


import Java.Accusation;
import Java.Board;
import Java.Game;
import Java.Location;
import Java.Player;
import Java.Room;
import Java.RoomCard;
import Java.Suggestion;
import Java.Token;
import Java.Card;
import Java.CardSet;
import Java.CharacterCard;
import Java.CharacterToken;
import Java.Turn;
import Java.WeaponCard;
/**
 *jUnit tests for cleudo 
 *tests run singly
 */
class Gametest {
	
	private String[] weapons = {"Candlestick", "Lead Pipe", "Dagger", "Revolver", "Rope", "Spanner"};
	private String[] characters = {"Mrs. Peacock","Miss Scarlett","Colonel Mustard","Mrs. White","Mr. Green","Professor Plum"};
	private String[] rooms = {"Kitchen","Ballroom","Conservatory","Dining Room","Billiard Room","Library","Lounge","Hall","Study"};
	@SuppressWarnings("serial")
	private HashMap<String, Location> shMap = new HashMap<String, Location>() {{
		put(characters[0], new Location(23, 6));
		put(characters[1], new Location(0, 17));
		put(characters[2], new Location(7, 24));
		put(characters[2], new Location(23, 19));
		put(characters[3], new Location(9, 0));
		put(characters[4], new Location(14, 0));
	}};
	
	@Test	//dice test line 31 of Turn class testing max and min bound of dice
	void turnDiceTest() {
		//Game game = new Game(null, null, null);
		//Scanner st = game.getStScanner();
		Turn turn = new Turn(new Player(null, null));
		int val = turn.rollDice();
		System.out.println(val);
		int i = 20;
		while (i> 0) {
			i--;
			if(val < 6 || val > 12) {
				fail();
			}
			else {
				assertTrue(true);
			}
		}
	}
	
	@Test
	void turnMoveTest() {
		Turn turn = new Turn(new Player(null, new CharacterToken(shMap.get(characters[0]), characters[0])));
		//Game game = new Game(null, null, null);
		//Board board = new Board(null);
		List<Integer>moveResultIntegers = turn.move("w", null);  //can't need a game and when game is made it does play turns which doesn't stop
		System.out.println(moveResultIntegers);
		moveResultIntegers = turn.move("W", null);
		System.out.println(moveResultIntegers);
		moveResultIntegers = turn.move("NW", null);
		System.out.println(moveResultIntegers);
		moveResultIntegers = turn.move("N", null);
		System.out.println(moveResultIntegers);
		moveResultIntegers = turn.move("NE", null);
		System.out.println(moveResultIntegers);
		//x and y coordinates work as expected with user inputs
	}
	
	@Test //nothing is moved when invalid case passed => test passes //only works if board is not updated in Turn.move()
	void turnInvalidMoveTest() {
		Turn turn = new Turn(new Player(null, new CharacterToken(shMap.get(characters[0]), characters[0])));
		//Game game = new Game(null, null, null);
		//Board board = new Board(null);
		List<Integer>moveResultIntegers = turn.move("invalid", null);  
		System.out.println(moveResultIntegers);
		moveResultIntegers = turn.move("invalid", null); 
		System.out.println(moveResultIntegers);
		
	}
	
	@Test //player shouldn't be able to move if they have lost
	public void inactivePlayerTest() {		
		Player player = new Player(null, new CharacterToken(shMap.get(characters[0]), characters[0]));
		player.setLost(true);
		Turn turn = new Turn(player);
		List<Integer>moveResultIntegers = turn.move("W", null);
		System.out.println(moveResultIntegers);
		moveResultIntegers = turn.move("W", null);
		System.out.println(moveResultIntegers);
		//can't test anything because p.havelost is used in a if loop in game before this method is called(handled by game)
	}
	
	@Test 
	/**when input (0,0) getSurroundingLocations will only store south, east and southeast as valid locations and not crash
	 * requires registerStartingLocations() to be commented out in constructor of Board
	 */
	public void edgeTestGetSurroundingLocationsTest() {
		Board board = new Board(null); 
		List<Location>locsList = board.getSurroundingLocations(0, 0);
		System.out.println(locsList);
		int count = 0;
		for(int i = 0 ; i < 8 ; i++) {
			Location location =locsList.get(i);
			if(location == null) count++;
		}
		assertEquals(5, count);
		
	}

	@Test //turn class make suggestion()
	void turnMakeSuggestion() {
		
		CardSet solution = new CardSet(new WeaponCard(weapons[0]), new CharacterCard(characters[0]), new RoomCard(rooms[0]));
			
		Turn turn = new Turn(new Player(null, null));	
		turn.makeSuggestion(solution);
		Suggestion suggestion = turn.getSuggestion();
		
		if(suggestion != null) {
		assertTrue(true);
		} else fail();
	}
	
	@Test //turn class testing suggestion requires token is in the same room
	void turnRoomSuggestionTest() {
		
		CharacterToken characterToken = new CharacterToken(shMap.get(characters[0]), characters[0]);
		characterToken.setRoom(new Room(rooms[0]));
		Set<Card> handCards = new HashSet<Card>();
		handCards.add( new Card(rooms[0]));
		Player p1 = new Player(handCards, characterToken);
		//Game tGame = new Game(null, null, null);	
		/*
		 * if(tGame.makeSuggestionTest(p1))return;
		 * 
		 * else fail();
		 */
	
	}
	@SuppressWarnings("serial")
    public static class InvalidMove extends Exception {
        public InvalidMove(String msg) {
            super(msg);
        }
    }
}


