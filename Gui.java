package code;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;
import javax.swing.border.Border;

import code.Type.loctype;

/**
 * Gui for cleudo game
 *@ extends JFrame   
 */
public class Gui extends JFrame {
	
	private JLabel info; //This label is used throughout the program in different positions
	private JLabel characterQ; //Asking to pick the character
	private ButtonGroup numPlayers; //Group of radio buttons to pick number of players
	private JButton confirmNumPlayers; //Button to lock in number of players
	private JMenuBar menuBar; //Menu Bar of GUI
	private JPanel board; //Board display is a JPanel
	private JPanel bottomPanel; //Bottom Panel containing players hand, dice, buttons etc.
	private JButton rollDice; //For rolling the dice
	private JButton makeSuggestion;
	private JButton makeAccusation;
	private Dimension screenSize; //Screens resolution
	private Box mainComponents; //Vertical box containing all the components of the main gui
	private Game game;
	private int playerCount; //To check if we have added all the players
	private ArrayList<String> playerNames = new ArrayList<String>(); //Names of players
	private ArrayList<String> pCharacterNames = new ArrayList<String>(); //Names of characters players have chosen
	private String[] characterNames  = {"Mrs. Peacock", "Colonel Mustard", "Miss Scarlett", "Professor Plum", "Mrs. White", "Mr. Green"}; //All the possible character names the character tokens can have
	private GuiState state; //Current state of GUI
	
	enum GuiState{
		Setup, SetupDone
	}
		
	
	public Gui(Game g) {
		super("Cleudo");
		game = g;
		playerCount = 0;
		this.state = GuiState.Setup;
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLayout(new FlowLayout());
		mainComponents = Box.createVerticalBox(); //To align the main gui components vertically
		numPlayersSetup(); //Before showing the main GUI we need to do the very initial setup (picking numbers and names)
	}
	
	/** Obtains the number of players that the game will have */
	private void numPlayersSetup() {
		numPlayers = new ButtonGroup();
		info = new JLabel();
		this.add(info);
		addButtons(6);
		confirmNumPlayers = new JButton("Confirm");
		confirmNumPlayers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				//If button is clicked on 
				 if(ev.getSource() == confirmNumPlayers) {
					 //Remove/clear these components
					 game.getGui().remove(confirmNumPlayers);
					 removeButtonGroup(numPlayers);
					 info.setText("");
					 //Updating GUI
					 revalidate();
					 repaint();
					 //Now get the names of the players and their chosen characters
					 playersSetup(Integer.parseInt(numPlayers.getSelection().getActionCommand()));
				 }	
			}});
		this.add(confirmNumPlayers);
		
	}
	
	/** Gets the name of each player and their chosen character */
	protected void playersSetup(int parseInt) {
		playerCount++;
		//Keep going until we have processed all the players
		if (playerCount <= parseInt) {
			JPanel playerPanel = new JPanel();
			//Making the layout for the initial GUI
			playerPanel.setLayout(new GridLayout(2, 2));
			Box formattingBox = Box.createVerticalBox();
			
			setLabel("Player " + playerCount + " what is your name?"); //Sets the text of the flexible label 'info'
			JTextField playerName = new JTextField();
			characterQ = new JLabel("Choose your character");
			
			 //Combo box as it is more visually appealing than radio buttons and is better for removing options from it later on.
			JComboBox<String> characters = new JComboBox<String>(characterNames);
			characters.setSelectedIndex(0); //Default option should be first character
			
			//Adding components to panel
			playerPanel.add(info);
			playerPanel.add(playerName);
			playerPanel.add(characterQ);
			playerPanel.add(characters);
			
			//So confirm button is underneath player name and their character choice
			formattingBox.add(playerPanel);
			confirmNumPlayers = new JButton("Confirm");
			formattingBox.add(confirmNumPlayers);

			this.add(formattingBox); //Adding it to the initial GUI
			revalidate();
			repaint();
			confirmNumPlayers.addActionListener(new ActionListener() {
				//If they confirm that they want the name and character they chose
				public void actionPerformed(ActionEvent ev) {
					if(ev.getSource() == confirmNumPlayers) {
						//Remove option so two players can't have the same character
						removeCharacter((String) characters.getSelectedItem());
						pCharacterNames.add((String) characters.getSelectedItem()); //Storing the character name
						playerNames.add(playerName.getText()); //and the player name
						 
						game.getGui().remove(formattingBox); //Removing components
						revalidate();
						repaint();
						playersSetup(parseInt); //Calling recursively (so we can process the next player (if there is one))
					 }	
				}});
		}
		else {
			//If we have processed all the players then add them to the actual game
			game.addPlayers(playerNames, pCharacterNames);
			game.deal(); //Deal the cards
			game.setCurrentPlayer(game.getPlayers().get(0)); //Current player is player 1
			setLayout(new FlowLayout(FlowLayout.LEFT));
			drawMenuBar(); //Initializing and drawing menu bar
			drawBoard(); 
			drawBottomLayer();
			this.add(mainComponents); //Add the components to the main GUI
			//Updating
			revalidate();
			repaint();
		}
		
	}

	private void drawMenuBar() {
		//Need to make (the menu item) functional, may change these menuBar options/items later on, we'll see.
		
		//The File MenuBar option
		menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenuItem quit = new JMenuItem("Quit");
		file.add(quit);
		
		//The Game MenuBar option
		JMenu game = new JMenu("Game");
		
		//The Help MenuBar option
		JMenu help = new JMenu("Help");
		JMenuItem colorReference = new JMenuItem("Board color reference");
		help.add(colorReference);
		JMenuItem rules = new JMenuItem("Rules");
		help.add(rules);
		
		menuBar.add(file);
		menuBar.add(game);
		menuBar.add(help);
		menuBar.setLayout(new FlowLayout(FlowLayout.LEFT)); //We want the menu bar positioned to the left
		mainComponents.add(menuBar); //Now the menu bar will display
		revalidate();
		repaint();
	}

	/** Draws the board in its current state, identifies rooms, walls etc. by color */
	private void drawBoard() {
		// TODO Auto-generated method stub
		board = new JPanel();
		board.setLayout(new GridLayout(25, 24)); //Cause board is 24 x 25 for cluedo (row is first parameter in GridLayout)
		//The size of the board depends on the screens resolution, every components which has its size manually modified has this property
		board.setPreferredSize(new Dimension((int) (screenSize.getWidth()/1.005), (int) (screenSize.getHeight()/1.4)));
		
		drawingBlankLocations(); //Draws the board cells with no colors
		setLocationsColors(); //Fills the board cells in with colors
		drawNames(); //Draws the player names on their character token cell
		
		mainComponents.add(board); //Now we can see the board
		revalidate();
		repaint();
		
	}

	private void drawNames() {
		//Pink indicates players locations (in this method since we will need to update players location (and therefore color) everytime they move)
		for (Player p : game.getPlayers()) {
			board.getComponent(p.getToken().getX() + p.getToken().getY()*24).setBackground(new Color(244, 139, 255));
		}
		
		for (Player p : game.getPlayers()) {
			JPanel location = (JPanel) board.getComponent(p.getToken().getX() + p.getToken().getY()*24); //Getting the cell
			//Setting the label
			JLabel label = new JLabel(p.getName());
			location.add(label);
		}
	}

	private void setLocationsColors() {
		//Iterate through every cell in board
		for (int i = 0; i < 600; i++) {
			Location l;
			//If we are in the first row then we don't have to convert from our 1D JPanel array in 'board' to our 2d board in 'Board.java'
			if (i < 24) {
				l = game.getBoard().getLocation(i, 0);
			}
			else {
				int col = i%24; //Getting the column. E.g. index = 40, 40/24 = 16
				int row = i/24; //E.g. index = 40, 40/24=1.6 = 1 (rounded), so index 40 = board[16][1] which is true
				l = game.getBoard().getLocation(col, row); 	//Getting the cell from board
			}
			
			//Grey indicates walls
			if (l.getType().getName().equals("Wall")) {
				board.getComponent(i).setBackground(Color.gray);
			}
			//Light blue indicates rooms
			else if (l.getType().getLocType() == loctype.ROOM) {
				board.getComponent(i).setBackground(new Color(70, 218, 235));
			}
			//Blue indicates doorways
			else if (l.getType().getLocType() == loctype.DOORWAY) {
				board.getComponent(i).setBackground(new Color(60, 99, 255));
			}
			//White indicates free space
			else {
				board.getComponent(i).setBackground(Color.white);
			}
			
			//Green indicates entraces to doorways
			for (Room r : game.getBoard().getRoom()) {
				for (Location loc : r.getExits()) {
					if (loc.getX() == l.getX() && loc.getY() == l.getY()) {
						board.getComponent(i).setBackground(new Color(71, 228, 92));
					}
				}
			}
		}
	}

	private void drawingBlankLocations() {
		//Draw 600 cells 
		for (int i = 0; i < 600; i++) {
			JPanel newLocation = new JPanel();
			Border border = BorderFactory.createLineBorder(Color.black, 1); //With a thin black border and with no color
			newLocation.setBorder(border);
			board.add(newLocation); //Add them to the board
		}
	}

	/** Draws the bottom layer e.g. cards, dice, action buttons, color code */
	private void drawBottomLayer() {
		bottomPanel = new JPanel();
		//Because the layout to this panel is quite complex we just a GridBagLayout as that is a very flexible layout
		bottomPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints(); //Constraining/Morphing the layout
		
		//Drawing the label which shows whos turn it is
		setLabel(game.getCurrentPlayer().getName() + " Turn");
		info.setFont(new Font(info.getFont().getName(), Font.BOLD, 30)); //Heading so we make it bold
		c.fill = GridBagConstraints.VERTICAL; //To center it
		c.gridx = 2; //Starts in grid 3 (gridx = 0 is the leftmost grid)
		c.gridwidth = 1; //Spans over one grid
		c.gridy = 0; //Is on the top row of the panel
		bottomPanel.add(info, c); //Add to panel with styling
		
		//Drawing the roll dice button
		rollDice = new JButton("Roll dice");
		c.ipady = (int) screenSize.getHeight()/80;
		c.ipadx = (int) screenSize.getWidth()/20;
		rollDice.setFont(new Font(rollDice.getFont().getName(), Font.PLAIN, 24)); //Making button text larger
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1000; //To position the other items, does mean they are quite cramped up next to each other though
		c.weighty = 0;
		c.gridx = 0;
		c.gridwidth = 1;
		c.gridy = 1;
		bottomPanel.add(rollDice, c); //Add to panel with styling
		
		//Drawing the dice underneath the button
		JPanel dicePanel = new JPanel();
		drawDice(dicePanel);
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 2;
		bottomPanel.add(dicePanel, c);
		
		//Drawing the cards next to the roll dice section
		JPanel cardPanel = new JPanel();
		cardPanel.setLayout(new GridLayout(2, 9)); //21 cards in the game, 3 in the solution, 1 player means they can have a max of 18 cards, hence 9, 2
		drawCards(cardPanel);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridheight = 2;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 1;
		bottomPanel.add(cardPanel, c); //Add to panel with styling
		
		//Drawing the suggestion button
		makeSuggestion = new JButton("Make Suggestion");
		makeSuggestion.setEnabled(false); //Player can't make suggestions before they move
		makeSuggestion.setFont(new Font(makeSuggestion.getFont().getName(), Font.PLAIN, 24));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = (int) screenSize.getHeight()/80;
		c.ipadx = (int) screenSize.getWidth()/20;
		c.gridx = 3;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridy = 1;
		bottomPanel.add(makeSuggestion, c);
		
		//Drawing the accusation button
		makeAccusation = new JButton("Make Accusation");
		makeAccusation.setEnabled(false); //Player can't make accusations before they move
		makeAccusation.setFont(new Font(makeAccusation.getFont().getName(), Font.PLAIN, 24));
		c.fill = GridBagConstraints.HORIZONTAL;
		//Making button bigger using padding
		c.ipady = (int) screenSize.getHeight()/80;
		c.ipadx = (int) screenSize.getWidth()/20;
		c.gridx = 3;
		c.gridwidth = 1;
		c.gridy = 2;
		bottomPanel.add(makeAccusation, c);
		
		//REFERENCE GUIDE CODE. WAS GOING TO BE IN BOTTOM PANEL BUT WHEN I TRIED ADDING IT THE WHOLE FORMATTING FUCKED UP :(
		//WILL BE MOVED TO THE MENU BAR UNDER HELP -> BOARD COLOR GUIDE
		
		//Drawing the color reference guide for the board
//		JPanel referencePanelFormatted = new JPanel(new GridLayout(2, 1));
		//Heading
//		JLabel referenceHeading = new JLabel("What each board cell color means:");
//		JPanel referencePanel = new JPanel(new GridLayout(6, 2)); //6 different colors on the board
//		drawReferences(referencePanel); //Draws the references
		//So the references are underneath the heading
//		referencePanelFormatted.add(referenceHeading);
//		referencePanelFormatted.add(referencePanel);
//		
//		//Format and then add the reference guide to the panel
//		c.fill = GridBagConstraints.VERTICAL;
//		c.ipady = 0;
//		c.ipadx = 0;
//		c.gridx = 4;
//		c.gridy = 1;
//		bottomPanel.add(referencePanelFormatted, c);
		
		mainComponents.add(bottomPanel); //Now we can see the bottom panel
		revalidate();
		repaint();
		
	}

	/** Draws all the references */
	private void drawReferences(JPanel referencePanel) {
		drawReference(referencePanel, Color.white, "This is free space:   "); //Free space reference
		drawReference(referencePanel, Color.gray, "This is a wall:   "); //Wall reference
		drawReference(referencePanel, new Color(70, 218, 235), "This is a room:   "); //Room reference 
		drawReference(referencePanel, new Color(60, 99, 255), "This is a rooms doorway (inside the room):   "); //Doorway reference
		drawReference(referencePanel, new Color(71, 228, 92), "This is a rooms doorway entrance (outside the room):   "); //Doorway entrance
		drawReference(referencePanel, new Color(244, 139, 255), "This is a players character token location:   "); //Player location reference
	
	}

	/** Draws a singular reference when given the color and name for it */
	private void drawReference(JPanel referencePanel, Color color, String info) {
		JPanel iconPanel = new JPanel(); //Panel for holding the color cell icon
		
		JPanel icon = new JPanel(); //The color cell icon
		icon.setBackground(color); //The color of the cell
		Border border = BorderFactory.createLineBorder(Color.black, 1);
		icon.setBorder(border);
		
		iconPanel.add(icon);
		
		//Adding the cell description
		JLabel iconInfo = new JLabel(info);
		referencePanel.add(iconInfo);
		referencePanel.add(iconPanel);
	}

	/** Draws the two dice under the roll dice button */
	private void drawDice(JPanel dicePanel) {
		// TODO Auto-generated method stub
		JPanel diceOne = new JPanel();
		JPanel diceTwo = new JPanel();
		//Set the color to light red
		diceOne.setBackground(new Color(255, 78, 78));
		diceTwo.setBackground(new Color(255, 78, 78));
		//Give them a border
		Border border = BorderFactory.createLineBorder(Color.black, 1);
		diceOne.setBorder(border);
		diceTwo.setBorder(border);
		//Make them larger
		diceOne.setPreferredSize(new Dimension((int) (screenSize.getWidth()/30), (int) (screenSize.getHeight()/17)));
		diceTwo.setPreferredSize(new Dimension((int) (screenSize.getWidth()/30), (int) (screenSize.getHeight()/17)));
		
		//Give them a label (will be set to dice roll when the dice are rolled)
		JLabel diceRollOne = new JLabel();
		JLabel diceRollTwo = new JLabel();
		diceOne.add(diceRollOne);
		diceTwo.add(diceRollTwo);
		dicePanel.add(diceOne);
		dicePanel.add(diceTwo);
	}

	/** Draws the cards of the current players hand */
	private void drawCards(JPanel cardPanel) {
		//Get each card
		for (Card c : game.getCurrentPlayer().getHand()) {
			//Each card is a JPanel with their description (their name)
			JPanel newCard = new JPanel();
			JLabel cardLabel = new JLabel(c.getName());
			//Adding a border
			Border border = BorderFactory.createLineBorder(Color.black, 1);
			newCard.setBorder(border);
			//Setting color of card based on type so its easier to know what type of card each card is.
			if (c instanceof WeaponCard) {
				newCard.setBackground(new Color(104, 249, 249));
			}
			else if (c instanceof CharacterCard) {
				newCard.setBackground(new Color(255, 255, 88));
			}
			else {
				newCard.setBackground(new Color(253, 175, 73));
			}
			newCard.add(cardLabel);
			cardPanel.add(newCard);
		}
	}

	//Removing an option (character) from the combo box, by deleting a character from the string array it uses
	protected void removeCharacter(String charName) {

		//The modified string array
		String[] newCharN = new String[characterNames.length-1];
		int index = 0;
		for (int i = 0; i < characterNames.length; i++) {
			if (characterNames[i] != charName) { //Don't add the picked character
				newCharN[index] = characterNames[i];
				index++;
			}
		}
		characterNames = newCharN; //Now set the array to an array without the picked character
	}

	//Remove the radio button group in the initial GUI
	protected void removeButtonGroup(ButtonGroup buttonGroup) {
		Enumeration<AbstractButton> s = buttonGroup.getElements();
		while(s.hasMoreElements()) { //Keep going until there is no more elements
			this.remove(s.nextElement());
		}
	}

	//Create and add the player number radio buttons to the group, with their appropriate label
	public void addButtons(int i) {
		for(int j = 0; j < i ; j++) {
			JRadioButton b = new JRadioButton(Integer.toString(j+1), true);
			b.setActionCommand(Integer.toString(j+1));
			add(b);
			numPlayers.add(b);
		}
		
	}
	
	/* Sets the label of one JLabel */
	public void setLabel(String string) {
		// TODO Auto-generated method stub
		info.setText(string);
	}
	
}