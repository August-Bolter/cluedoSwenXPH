package code;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URI;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;
import java.awt.Color.*;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.Border;

import code.Type.loctype;

/**
 * Gui for cleudo game
 *@ extends JFrame   
 */
@SuppressWarnings("serial")
public class Gui extends JFrame implements MouseListener {
	
	private JLabel info; //This label is used throughout the program in different positions
	private JLabel characterQ; //Asking to pick the character
	private ButtonGroup numPlayers; //Group of radio buttons to pick number of players
	private JButton confirmNumPlayers; //Button to lock in number of players
	private JPanel diceOne; //The panel showing the first dice
	private JButton confirmSuggestion;
	private JPanel diceTwo; //The panel showing the second dice
	private JMenuBar menuBar; //Menu Bar of GUI
	private JPanel board; //Board display is a JPanel
	private JPanel bottomPanel; //Bottom Panel containing players hand, dice, buttons etc.
	private JButton rollDice; //For rolling the dice
	private JButton makeSuggestion;
	private JButton makeAccusation;
	private JButton endTurn;
	private Dimension screenSize; //Screens resolution
	private Box mainComponents; //Vertical box containing all the components of the main gui
	private Game game;
	private boolean haveMoved;
	private int playerCount; //To check if we have added all the players
	private ArrayList<String> playerNames = new ArrayList<String>(); //Names of players
	private ArrayList<String> pCharacterNames = new ArrayList<String>(); //Names of characters players have chosen
	private String[] characterNames  = {"Mrs. Peacock", "Colonel Mustard", "Miss Scarlett", "Professor Plum", "Mrs. White", "Mr. Green"}; //All the possible character names the character tokens can have
	private GuiState state; //Current state of GUI
	ArrayList<Location> roomExits;
	
	/** An enumeration representing the state the GUI is in. The GUI can either be setting up (determing number of players and their names/characters) or have
	 * finished the setup and the game is in progress. */
	enum GuiState{
		Setup, SetupDone
	}

	public static void main(String arg[]) {
		Game cluedo = new Game(null, null, null); //All set to null since the game is what defines these parameters, so they are set later on.
	}
	
	public Gui(Game g) {
		super("Cluedo");
		game = g;
		haveMoved = false;
		playerCount = 0;
		addKListener(); //Adding the key listener for the key shortcuts
		this.state = GuiState.Setup; //We are currently setting up
		/*Get the screen size as when we are manually setting the dimensions of some components it is better to base this change off the screenSize rather than a
		fixed value so the game can work with different screen resolutions. */
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		setLayout(new FlowLayout());
		mainComponents = Box.createVerticalBox(); //To align the main GUI components vertically
		numPlayersSetup(); //Before showing the main GUI we need to do the very initial setup (picking numbers and names)
	}
	
	/** Gets the screen size (resolution) */
	public Dimension getScreenSize() {
		return screenSize;
	}
	
	private void addKListener() {
		//The window (game) needs to be in focus to ensure the key listener is responsive.
		this.requestFocusInWindow();
		this.setFocusable(true);
		this.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				//Because we haven't made the main game GUI in the initial state we don't want keyboard shortcuts to work in that period of time.
				if (state == GuiState.SetupDone) {
					int keyPressed = e.getKeyCode();
					//Move keyboard shortcuts. The various if(index...) is for ensuring we aren't moving to a location outside the board (avoiding exceptions)
					if (game.canMove()) { 
						//Move up if possible.
						if (keyPressed == KeyEvent.VK_UP) {
							int index = game.getCurrentPlayer().getToken().getX() + game.getCurrentPlayer().getToken().getY()*24;
							if (index >= 24) {
								index = index - 24;
								movePlease(index);
							}
						}
						//Move down
						else if (keyPressed == KeyEvent.VK_DOWN) {
							int index = game.getCurrentPlayer().getToken().getX() + game.getCurrentPlayer().getToken().getY()*24;
							if (index <= 575) {
								index = index + 24;
								movePlease(index);
							}
						}
						//Move left
						else if (keyPressed == KeyEvent.VK_LEFT) {
							int index = game.getCurrentPlayer().getToken().getX() + game.getCurrentPlayer().getToken().getY()*24;
							if (index % 24 != 0) {
								index = index - 1;
								movePlease(index);
							}
						}
						//Move right
						else if (keyPressed == KeyEvent.VK_RIGHT) {
							int index = game.getCurrentPlayer().getToken().getX() + game.getCurrentPlayer().getToken().getY()*24;
							if ((index+1)%24 != 0) {
								index = index + 1;
								movePlease(index);
							}
						}
					}
					
					//Roll dice shortcut
					if (keyPressed == KeyEvent.VK_D) {
						rollDice.doClick();
					}
					
					//Make suggestion shortcut
					else if (keyPressed == KeyEvent.VK_S) {
						makeSuggestion.doClick();
					}
					
					//Make accusation shortcut
					else if (keyPressed == KeyEvent.VK_A) {
						makeAccusation.doClick();
					}
					
					//End turn shortcut
					else if (keyPressed == KeyEvent.VK_E) {
						endTurn.doClick();
					}
					
					//Quit shortcut
					else if (keyPressed == KeyEvent.VK_Q) {
						JMenu menu = menuBar.getMenu(0);
						JMenuItem quit = menu.getItem(0);
						quit.doClick();
					}
					
					//Color reference shortcut
					else if (keyPressed == KeyEvent.VK_C) {
						JMenu menu = menuBar.getMenu(2);
						JMenuItem references = menu.getItem(0);
						references.doClick();
					}
					
					//Rules shortcut
					else if (keyPressed == KeyEvent.VK_R) {
						JMenu menu = menuBar.getMenu(2);
						JMenuItem rules = menu.getItem(1);
						rules.doClick();
					}
					
					//Keyboard shortcuts info shortcut
					else if (keyPressed == KeyEvent.VK_K) {
						JMenu menu = menuBar.getMenu(1);
						JMenuItem keyBindings = menu.getItem(0);
						keyBindings.doClick();
					}
				}
				
			}

			@Override
			public void keyReleased(KeyEvent e) {}

			@Override
			public void keyTyped(KeyEvent e) {}
			
		});
		
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
	
	/** Gets the name of each player and their chosen character
	 * @param The number of players */
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
			//this.add(confirmNumPlayers);
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
			setPlayerToolTips();
			setRoomToolTips();
			setWeaponToolTips();
			this.add(mainComponents); //Add the components to the main GUI
			state = GuiState.SetupDone;
			//Updating
			revalidate();
			repaint();
		}
		
	}

	/** Draws and shows the menu bar with its menus */
	private void drawMenuBar() {
		//The File MenuBar option
		menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		
		JMenuItem quit = new JMenuItem("Quit");
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if(ev.getSource() == quit) {
					//Confirming that they do really want to exit
					int answer = JOptionPane.showConfirmDialog(game.getGui(), "Are you sure you want to quit?", "Quit", JOptionPane.YES_NO_OPTION);
					if (answer == JOptionPane.YES_OPTION) {
						System.exit(0);
					}
				}	
			}});
		
		file.add(quit); //Quit is under file
		
		//The Game MenuBar option
		JMenu game = new JMenu("Game");
		JMenuItem keyShortcuts = new JMenuItem("Keyboard Shortcuts");
		keyShortcuts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if(ev.getSource() == keyShortcuts) {
					//Show the keyboard shortcut keys
					drawKeyShortcuts();
				}	
			}});
		game.add(keyShortcuts);
		
		//The Help MenuBar option
		JMenu help = new JMenu("Help");
		JMenuItem colorReference = new JMenuItem("Board color reference");
		colorReference.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if(ev.getSource() == colorReference) {
					drawColorReference();
				}	
			}});
		
		help.add(colorReference);
		
		JMenuItem rules = new JMenuItem("Rules");
		rules.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if(ev.getSource() == rules) {
					drawRules();
				}	
			}});
		
		help.add(rules);
		
		menuBar.add(file);
		menuBar.add(game);
		menuBar.add(help);
		menuBar.setLayout(new FlowLayout(FlowLayout.LEFT)); //We want the menu bar positioned to the left
		mainComponents.add(menuBar); //Now the menu bar will display
		revalidate();
		repaint();
	}

	/** Draws all the keyboard shortcuts */
	protected void drawKeyShortcuts() {
		ArrayList<JLabel> labels = new ArrayList<JLabel>();
		
		JDialog keySWindow = new JDialog();
		keySWindow.setTitle("Keyboard shortcuts");
		Box keySBox = Box.createVerticalBox(); //So each keyboard shortcut label is placed on a new line
		JPanel keySPanel = new JPanel();

		JLabel keySUp = new JLabel("Move up: UP_ARROW");
		JLabel keySDown = new JLabel("Move down: DOWN_ARROW");
		JLabel keySLeft = new JLabel("Move left: LEFT_ARROW");
		JLabel keySRight = new JLabel("Move right: RIGHT_ARROW");
		JLabel keySRollDice = new JLabel("Roll dice: R");
		JLabel keySSuggestion = new JLabel("Make suggestion: S");
		JLabel keySAccusation = new JLabel("Make accusation: A");
		JLabel keySEndTurn = new JLabel("End turn: E");
		JLabel keySColorReferences = new JLabel("Color references: C");
		JLabel keySRules = new JLabel("Show rules: R");
		JLabel keySKeyBindings = new JLabel("Show key bindings: K");
		JLabel keySQuitGame = new JLabel("Quit game: Q");
		labels.add(keySUp);
		labels.add(keySDown);
		labels.add(keySLeft);
		labels.add(keySRight);
		labels.add(keySRollDice);
		labels.add(keySSuggestion);
		labels.add(keySAccusation);
		labels.add(keySEndTurn);
		labels.add(keySColorReferences);
		labels.add(keySRules);
		labels.add(keySKeyBindings);
		labels.add(keySQuitGame);
		
		for (JLabel l : labels) {
			l.setFont(new Font(l.getFont().getName(), Font.PLAIN, 26)); //So the text is easier to see
			keySBox.add(l);
		}
		
		keySPanel.add(keySBox);
		keySWindow.add(keySPanel);
		
		keySWindow.setVisible(true);
		keySWindow.pack(); //Resizing the window
		
	}

	/** Shows a link to the official cluedo game manual */
	protected void drawRules() {
		JDialog ruleWindow = new JDialog();
		ruleWindow.setTitle("Rules");
		JPanel rulePanel = new JPanel();
		String link = "Official Cluedo rules: https://www.hasbro.com/common/instruct/Clue_(2002).pdf";
		JTextPane ruleLink = new JTextPane();
		/*Masking JTextPane to look like a JLabel since we want the look of a JLabel, but one where the text can be copy-pasted so the user can copy and paste
		the URL in. */
		ruleLink.setContentType(link);
		ruleLink.setText(link);
		ruleLink.setCursor(new Cursor(Cursor.TEXT_CURSOR)); //To indicate the text is selectable
		ruleLink.setFont(new Font(ruleLink.getFont().getName(), Font.PLAIN, 18));
		ruleLink.setEditable(false);
		ruleLink.setBackground(null);
		ruleLink.setBorder(null);
        
		rulePanel.add(ruleLink);
		ruleWindow.add(rulePanel);
		
		ruleWindow.setVisible(true);
		ruleWindow.pack();
	}

	/** Draws each color that is on the board and what each color means */
	protected void drawColorReference() {
		//Drawing the color reference guide for the board
		JDialog colorReferenceWindow = new JDialog();
		colorReferenceWindow.setTitle("Color references");
		Box box = Box.createVerticalBox();
		JPanel referencePanelFormatted = new JPanel(new FlowLayout(FlowLayout.LEFT));

		drawReferences(box); //Draws the references
		referencePanelFormatted.add(box);
		
		colorReferenceWindow.add(referencePanelFormatted);
		colorReferenceWindow.setVisible(true);
		colorReferenceWindow.pack();
		
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
		drawWeapons(); //Draws the weapon tokens in their respective room
		
		mainComponents.add(board); //Now we can see the board
		revalidate();
		repaint();
	}
	
	/** Sets the weapon tool tip to the the name of the weapon */
	private void setWeaponToolTips() {
		for (Component c : board.getComponents()) {
			//If we have found a weapon token
			if (c.getBackground().equals(new Color(255, 98, 98))) {
				JPanel panel = (JPanel) c;
				JLabel label = (JLabel) panel.getComponent(0); //Get the weapon name
				panel.setToolTipText(label.getText()); //Set tool tip
			}
		}
	}
	
	/** Set the tool tip of each room so when any occupied tile in the room is hovered over for a while it will show the name of the room */
	private void setRoomToolTips() {
		for (Room r : game.getBoard().getRoom()) {
			ArrayList<Location> dontSet = new ArrayList<Location>();
			//Ensuring that we aren't setting the tooltip of locations that are occupied by a player
			int numPlayers = r.getPlayers().size();
			int indexX = 0;
			int indexY = 0;
			while (numPlayers > 0) {
				//Player locations found and added
				dontSet.add(game.getBoard().getLocation(r.getLoc().get(0).getX()+3+indexX, r.getLoc().get(0).getY()+1+indexY));
				//Using the same index structure as when the player is added in
				indexX++;
				if (indexX == 2) {
					indexX = 0;
					indexY++;
				}
				numPlayers--;
			}
			//Sets the tool tip of the locations
			for (Location l : r.getLoc()) {
				boolean foundPlayer = false;
				for (Location dontSetLoc : dontSet) {
					if (l.getX() == dontSetLoc.getX() && l.getY() == dontSetLoc.getY()) {
						foundPlayer = true;
					}
				}
				if (!foundPlayer) {
					JPanel j = (JPanel) board.getComponent(l.getX() + l.getY()*24); //Getting the GUI Board index
					j.setToolTipText(r.getName());
				}
			}
		}
	}
	
	/** Setting the tool tips of the players */
	private void setPlayerToolTips() {
		for (Component comp : board.getComponents()) {
			JPanel p = (JPanel) comp;
			if (comp.getName() == null || comp.getName().equalsIgnoreCase("Doorway Entrance")) {
				p.setToolTipText(null);
			}
		}
		
		for (Component c : board.getComponents()) {
			//Look for the player
			if (c.getBackground().equals(new Color(244, 139, 255))) {
				JPanel panel = (JPanel) c;
				JLabel label = (JLabel) panel.getComponent(0);
				for (Player p : game.getPlayers()) {
					if (p.getToken().getName().equalsIgnoreCase(label.getText())) {
						//Player has been found so set its tool tip to the player name
						panel.setToolTipText(p.getName());
					}
				}
			}
		}
	}

	/** Draws the weapon tokens in their respective rooms */
	private void drawWeapons() {
		for (Room r : game.getBoard().getRoom()) { //Looking in rooms for weapons
			int indexX = 0;
			int indexY = 0;
			for (WeaponToken w : r.getWeapon()) {
				Location topLeft = r.getLoc().get(0); //We get the top left corner of the room
				Location closeToTL = game.getBoard().getLocation(topLeft.getX()+1+indexX, topLeft.getY()+1+indexY); //We go one down and one to the right
				//Now we are going to set this board cell to a different color and a name to indicate the weapon token
				JPanel weaponLocation = (JPanel) board.getComponent(closeToTL.getX() + closeToTL.getY()*24);
				weaponLocation.setBackground(new Color(255, 98, 98));
				JLabel weaponName = new JLabel(w.getName());
				if (weaponLocation.getComponents().length != 0) {
					weaponLocation.remove(0);
				}
				weaponLocation.add(weaponName);
				indexX++;
				if (indexX == 2) {
					indexX = 0;
					indexY++;
				}
			}
		}
		
	}

	private void drawNames() {
		//Pink indicates players locations (in this method since we will need to update players location (and therefore color) everytime they move)
		for (Player p : game.getPlayers()) {
			board.getComponent(p.getToken().getX() + p.getToken().getY()*24).setBackground(new Color(244, 139, 255));
		}
		
		for (Player p : game.getPlayers()) {
			JPanel location = (JPanel) board.getComponent(p.getToken().getX() + p.getToken().getY()*24); //Getting the cell
			//Setting the label
			JLabel label = new JLabel(p.getToken().getName());
			label.setFont(new Font(label.getFont().getName(), Font.BOLD, 9));
			 //Removing excess labels
			if (location.getComponents().length != 0) {
				location.remove(0);
			}
			location.add(label); //Adding playerName as a label
		}
	}
	
	/** Clears the players location and possible move spots after they have moved */
	private void clearColors() {
		for (Component c : board.getComponents()) {
			if (c.getBackground().equals(new Color(246, 250, 111)) || c.getBackground().equals(new Color(244, 139, 255))) { //Only looking at player colors and highlightColors
				//Sets the color based on what the color was previously
				
				if (c.getName() != null && c.getName().equals("Doorway Entrance")) {
					c.setBackground(new Color(71, 228, 92)); //Set color back to green
				}
				else if (c.getName() != null && c.getName().equals("Doorway")) {
					c.setBackground(new Color(60, 99, 255)); //Set color back to blue
				}
				
				//Clearing color and label from a player in a room
				else if (c.getBackground().equals(new Color(244, 139, 255))) { 
					if (c.getName() == null) {
						c.setBackground(Color.white);
						JPanel j = (JPanel) c;
						if (j.getComponents().length != 0) {
							JLabel label = (JLabel) j.getComponent(0);
							label.setText("");
						}
					}
				}
				else {
					c.setBackground(Color.white);
				}
			}
		}
	}
	
	/** Clearing more labels/colors after player/weapons have been moved by a suggestion */
	private void clearAfterSuggestion() {
		int index = 0;
		for (Component c : board.getComponents()) {
			boolean clearedPlayer = false;
			JPanel panel = (JPanel) c;
			if (panel.getComponents().length != 0) {
				JLabel label = (JLabel) panel.getComponent(0);
				if (label.getText().equalsIgnoreCase(game.getCurrentPlayersTurn().getSuggestion().getSuggSet().getWeaponC().getName()) || label.getText().equalsIgnoreCase(game.getCurrentPlayersTurn().getSuggestion().getSuggSet().getCharacterC().getName())){
					Location l = null;
					if (index > 23) {
						int col = index%24;
						int row = index/24;
						l = game.getBoard().getLocation(col, row);
					}
					else {
						l = game.getBoard().getLocation(index, 0);
					}
					//Removing label and color if the player is being moved from one room to another
					for (Room r : game.getBoard().getRoom()) {
						for (Location loc : r.getLoc()) {
							if (loc.getX() == l.getX() && loc.getY() == l.getY()) {
								if (!r.getName().equalsIgnoreCase((game.getCurrentPlayersTurn().getSuggestion().getSuggSet().getRoomC().getName()))){
									c.setBackground(new Color(70, 218, 235)); //Setting to room color
									panel.remove(0); //Removing label
									if (label.getText().equalsIgnoreCase(game.getCurrentPlayersTurn().getSuggestion().getSuggSet().getCharacterC().getName())) {
										clearedPlayer = true;
									}
								}
								else {
									clearedPlayer = true;
								}
							}
						}
					}
					//Clear their label and background color if they were moved from outside a room to the room
					if (!clearedPlayer && label.getText().equalsIgnoreCase(game.getCurrentPlayersTurn().getSuggestion().getSuggSet().getCharacterC().getName())) {
						c.setBackground(Color.white);
						panel.remove(0);
					}
				}
			}
			index++;
		}
	}

	/** Set the colors of the board based on the type of location */
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
				board.getComponent(i).setName("Room"); //We need to be able to identify room locations 
			}
			//Blue indicates doorways
			else if (l.getType().getLocType() == loctype.DOORWAY) {
				board.getComponent(i).setBackground(new Color(60, 99, 255));
				board.getComponent(i).setName("Doorway"); 
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
						board.getComponent(i).setName("Doorway Entrance");
					}
				}
			}
		}
	}

	/** Draws the board by creating blank JPanels */
	private void drawingBlankLocations() {
		//Draw 600 cells 
		for (int i = 0; i < 600; i++) {
			JPanel newLocation = new JPanel();
			Border border = BorderFactory.createLineBorder(Color.black, 1); //With a thin black border and with no color
			newLocation.setBorder(border);
			newLocation.addMouseListener(this); //So they can register when they are hovered over/clicked on
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
		c.fill = GridBagConstraints.CENTER; //To center it
		c.gridx = 2; //Starts in grid 3 (gridx = 0 is the leftmost grid)
		c.gridwidth = 1; //Spans over one grid
		c.gridy = 0; //Is on the top row of the panel
		bottomPanel.add(info, c); //Add to panel with styling
		
		rollDice = new JButton("Roll dice");
		//If they press the roll dice button
		rollDice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if(ev.getSource() == rollDice) {
					rollDice.setEnabled(false); //Disable the button since they can only roll once per turn
					drawDiceNums(game.rollDice()); //Draw the dice numbers on the dice panels
					highlightMoveSpots(); //Highlight where the user can move
					revalidate(); 
					repaint();
				 }	
			}});
		
		//Formatting the roll dice button
		rollDice.setFont(new Font(rollDice.getFont().getName(), Font.PLAIN, 24)); //Making button text larger
		c.fill = GridBagConstraints.CENTER;
		c.weightx = 10; //For positioning
		c.weighty = 0;
		//Indicating where on the grid they will be and how many grid spaces will they take up
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
		cardPanel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		cardPanel.setLayout(new GridLayout(2, 9)); //21 cards in the game, 3 in the solution, 1 player means they can have a max of 18 cards, hence 9, 2
		drawCards(cardPanel);
		c.fill = GridBagConstraints.CENTER; //Center fill so all components are centered within their grid
		c.gridheight = 2;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 1;
		bottomPanel.add(cardPanel, c); //Add to panel with styling
		
		//Creating the suggestion button
		makeSuggestion = new JButton("Make Suggestion");
		
		makeSuggestion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if(ev.getSource() == makeSuggestion) {
					/*Players can only make one suggestion per turn. If they exit the suggestion pane without picking a suggestion then they can't make another 
					one that turn */
					makeSuggestion.setEnabled(false);
					drawSuggestion(); //Open the suggestion creator
					revalidate();
					repaint();
				 }	
			}});
		
		//Drawing and formatting it
		makeSuggestion.setEnabled(false); //Player can't make suggestions before they move
		makeSuggestion.setFont(new Font(makeSuggestion.getFont().getName(), Font.PLAIN, 24)); //Font size determines button size
		c.fill = GridBagConstraints.CENTER;
		c.gridx = 3;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridy = 1;
		bottomPanel.add(makeSuggestion, c);
		
		makeAccusation = new JButton("Make Accusation");
		makeAccusation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if(ev.getSource() == makeAccusation) {
					/*This is so that if the player has left open the suggestion window it closes so they can't have both panes open at the same time. Any other 
					 * windows will also be closed */
					for (Frame f : Frame.getFrames()) {
						if (!f.getTitle().equals("Cluedo")) {
							f.dispose();
						}
					}
					//Player can only make one accusation per turn
					makeAccusation.setEnabled(false);
					drawAccusation(); //Showing the accusation creator
					revalidate();
					repaint();
				 }	
			}});
		
		//Drawing and formatting the accusation button
		makeAccusation.setEnabled(false); //Player can't make accusations before they move (unless they can't move)
		makeAccusation.setFont(new Font(makeAccusation.getFont().getName(), Font.PLAIN, 24));
		c.fill = GridBagConstraints.CENTER;
		c.gridx = 3;
		c.gridwidth = 1;
		c.gridy = 2;
		bottomPanel.add(makeAccusation, c);
		
		//Drawing the end turn button
		endTurn = new JButton("End Turn");
		endTurn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if(ev.getSource() == endTurn) {
					haveMoved = false;
					game.nextTurn();
					//Nothing that could be used in the last players turn (suggestion creator/accusation creator) should be open for the next players turn
					for (Frame f : Frame.getFrames()) {
						if (!f.getTitle().equals("Cluedo")) {
							f.dispose();
						}
					}
					//Removes and re-adds the bottom layer to update it and show the next players cards
					mainComponents.remove(2);
					drawBottomLayer();
					
					//If the player was moved to a room by a suggestion 
					if (game.getCurrentPlayer().getToken().getMoved()) {
						game.getCurrentPlayer().getToken().setMoved(false); //Resetting the variable
						drawHaveMovedPanel(); //Drawing the window for when this situation occurs
					}
					
					revalidate();
					repaint();
				 }	
			}});
		
		endTurn.setEnabled(false); //Player can't make end turn before they move (unless they can't move)
		endTurn.setFont(new Font(endTurn.getFont().getName(), Font.PLAIN, 24));
		c.fill = GridBagConstraints.CENTER;
		c.gridx = 4;
		c.gridwidth = 1;
		c.gridy = 1;
		bottomPanel.add(endTurn, c);
		
		mainComponents.add(bottomPanel); //Now we can see the bottom panel
		revalidate();
		repaint();
		
		/* If the player is in a room then they can't roll the dice until they have chosen an exit (moving to an exit doesn't count as a 'step') */
		if (game.getCurrentPlayer().getToken().getRoom() != null) {
			rollDice.setEnabled(false);
			highlightRoomExits(); //Highlight unblocked room exits
		}
		
	}

	/** Draws the window that pops up if the player has moved to a new room through a suggestion */
	protected void drawHaveMovedPanel() {
		String[] choices = new String[] {"Move", "Suggestion"};
		int answer = JOptionPane.showOptionDialog(game.getGui(), "Would you like to move out of this room or make a suggestion?", "Suggestion or move", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
		if (answer == 1) { //User chose suggestion, if they chose move nothing needs to be changed
			rollDice.setEnabled(false); //They can only move or make a suggestion not both
			makeSuggestion.setEnabled(true);
			//It is unclear whether the user can make an accusation after making a suggestion in this case, so I set it so they can
			makeAccusation.setEnabled(true); 
			endTurn.setEnabled(true);
			makeSuggestion.doClick(); //Simulates a click on the suggestion button
		}
	}

	/** Highlights all the room exits of the room the current player is in */
	private void highlightRoomExits() {
		roomExits = new ArrayList<Location>();
		boolean canExit = false; //Determines whether all exits are blocked
		for (Location l : game.getCurrentPlayer().getToken().getRoom().getExits()) {
			if (game.getBoard().getLocation(l.getX(), l.getY()).getPlayer() == null) {
				canExit = true; //Not all exits are blocked
				roomExits.add(l);
				int index = l.getX() + l.getY()*24;
				board.getComponent(index).setBackground(new Color(246, 250, 111));
			}
		}
		//If we can't exit then show the dialogue informing the user this
		if (!canExit) {
			showAllHallwaysBlocked();
			makeAccusation.setEnabled(true);
			endTurn.setEnabled(true);
		}
	}

	/** Draws and shows the window that appears if the player has lost. Content of window depends on if they are the last player left in the game or not. */
	protected void drawLosingPanel() {
		JDialog losingDialog = new JDialog();
		losingDialog.setTitle("Wrong Accusation");
		JPanel losingPanel = new JPanel(new GridLayout(2, 1));
		losingPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); //For padding
		String str = game.getCurrentPlayer().getName() + " your accusation was wrong. ";
		
		//Show a different message if the game is over because all players have lost
		if(game.allLost()) {
			JLabel playerLost = new JLabel(str);
			playerLost.setFont(new Font(playerLost.getFont().getName(), Font.PLAIN, 36));
			losingPanel.add(playerLost);
			JLabel allPlayersLost = new JLabel("All players have made an incorrect accusation therefore the game is over");
			allPlayersLost.setFont(new Font(allPlayersLost.getFont().getName(), Font.PLAIN, 36));
			losingPanel.add(allPlayersLost);
			losingDialog.add(losingPanel);
			losingDialog.setVisible(true);
			losingDialog.pack();
			//Adds a 4 second timer to allow enough time for the user to read the message before the game closes
            Timer t = new Timer(4000, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                	System.exit(0);
                }
            });

            t.start();
		}
		else {
			//Otherwise let them know they still must refute
			str = str + "You have lost the game but still must refute suggestions when you can";
			JLabel playerLost = new JLabel(str);
			playerLost.setFont(new Font(playerLost.getFont().getName(), Font.PLAIN, 36));
			losingPanel.add(playerLost);
			game.moveLostPlayerIfRequired(); //In cases where the player loses on a exit they have to be moved to the room to prevent them from blocking it.
		}
		losingDialog.add(losingPanel);
		losingDialog.setVisible(true);
		losingDialog.pack();
	}

	/** Draws and shows the window when the player wins the game by making a correct accusation */
	protected void drawWinningPanel() {
		JDialog winningDialog = new JDialog();
		winningDialog.setTitle("Correct Accusation");
		JPanel winningPanel = new JPanel();
		winningPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		JLabel playerWon = new JLabel(game.getCurrentPlayer().getName() + " your accusation was correct. You have won the game!");
		playerWon.setFont(new Font(playerWon.getFont().getName(), Font.PLAIN, 36));
		
		winningPanel.add(playerWon);
		winningDialog.add(winningPanel);
		winningDialog.setVisible(true);
		winningDialog.pack();
      
		//3.45 second timer for the user to read the message before the game exits. Slightly less than the 4s timer above since the message is shorter.
		Timer t = new Timer(3450, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            	System.exit(0);
            }
        });

        t.start(); //Start the timer
       
	}

	/** Draws the 'suggestion creator' (i.e. the window which shows and allows you to make a suggestion through selecting character, room and weapon) */
	protected void drawSuggestion() {
		JDialog suggestionPane = new JDialog();
		suggestionPane.setTitle("Suggestion Creator");
		JPanel suggestionBox = new JPanel(new GridBagLayout());
		GridBagConstraints constraint = new GridBagConstraints(); //Layout constraint
		
		suggestionBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		JLabel makeSuggestionInfo = new JLabel("Please make a suggestion");
		makeSuggestionInfo.setFont(new Font(makeSuggestionInfo.getFont().getName(), Font.PLAIN, 20));
		constraint.gridx = 0;
		constraint.gridy = 0;
		constraint.insets = new Insets(5, 0, 5, 0); //So the components are spaced out from each vertically.
		suggestionBox.add(makeSuggestionInfo, constraint);
		
		String[] room = {game.getCurrentPlayer().getToken().getRoom().getName()}; //Players can only make suggestions in the room they are in
		String[] weapon = new String[6];
		String[] character = new String[6];
		int index = 0;
		//Getting the weapon names
		for (WeaponCard c : game.getWeaponCards()) {
			weapon[index] = c.getName();
			index++;
		}
		index = 0;
		//And the character names
		for (CharacterCard c : game.getCharacterCards()) {
			character[index] = c.getName();
			index++;
		}
		//Creating the combo boxes which show the possible options for the suggestion
		JComboBox<String> rooms = new JComboBox<String>(room);
		JComboBox<String> weapons = new JComboBox<String>(weapon);
		JComboBox<String> characters = new JComboBox<String>(character);
		rooms.setSelectedIndex(0); //Show by default show the first option
		//Using screen resolution as it makes GUI more flexible
		rooms.setPreferredSize(new Dimension((int) (screenSize.getWidth()/20), (int) (screenSize.getHeight()/40)));
		weapons.setSelectedIndex(0);
		weapons.setPreferredSize(new Dimension((int) (screenSize.getWidth()/20), (int) (screenSize.getHeight()/40)));
		characters.setSelectedIndex(0);
		rooms.setPreferredSize(new Dimension((int) (screenSize.getWidth()/20), (int) (screenSize.getHeight()/40)));
		
		JPanel comboboxes = new JPanel(new GridLayout(3, 2));
		comboboxes.add(new JLabel("Room: "));
		comboboxes.add(rooms);
		comboboxes.add(new JLabel("Weapon: "));
		comboboxes.add(weapons);
		comboboxes.add(new JLabel("Character: "));
		comboboxes.add(characters);
		
		confirmSuggestion = new JButton("Confirm suggestion");
		confirmSuggestion.setFont(new Font(confirmSuggestion.getFont().getName(), Font.PLAIN, 14));
		confirmSuggestion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if(ev.getSource() == confirmSuggestion) {
					//If they confirm there suggestion then it should make the suggestion in the Game class
					game.getCurrentPlayersTurn().makeSuggestion(game.getGui(), (String) rooms.getSelectedItem(), (String) weapons.getSelectedItem(), (String) characters.getSelectedItem());
					suggestionPane.dispose(); //And close the suggestion creator
					revalidate();
					repaint();
				 }	
			}});
		constraint.gridy = 1;
		suggestionBox.add(comboboxes, constraint);
		constraint.gridy = 2;
		suggestionBox.add(confirmSuggestion, constraint);
		suggestionPane.add(suggestionBox);
		suggestionPane.setVisible(true);
		suggestionPane.pack();
	}
	
	/** Draws the accusation window which shows the possible options for the accusation and a confirm button. */
	protected void drawAccusation() {
		JDialog accusationPane = new JDialog();
		accusationPane.setTitle("Accusation Creator");
		JPanel accusationBox = new JPanel(new GridBagLayout());
		accusationBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		GridBagConstraints constraint = new GridBagConstraints(); //Layout constraint
		
		JLabel makeAccusationInfo = new JLabel("Please make an accusation");
		makeAccusationInfo.setFont(new Font(makeAccusationInfo.getFont().getName(), Font.PLAIN, 20));
		constraint.gridx = 0;
		constraint.gridy = 0;
		constraint.insets = new Insets(5, 0, 5, 0);
		
		String[] room = new String[9]; //Accusation can be made anywhere in the game so rooms are not limited like they are with suggestions
		String[] weapon = new String[6];
		String[] character = new String[6];
		int index = 0;
		for (WeaponCard c : game.getWeaponCards()) {
			weapon[index] = c.getName();
			index++;
		}
		index = 0;
		for (CharacterCard c : game.getCharacterCards()) {
			character[index] = c.getName();
			index++;
		}
		index = 0;
		for (RoomCard c : game.getRoomCards()) {
			room[index] = c.getName();
			index++;
		}
		JComboBox<String> rooms = new JComboBox<String>(room);
		JComboBox<String> weapons = new JComboBox<String>(weapon);
		JComboBox<String> characters = new JComboBox<String>(character);
		rooms.setSelectedIndex(0);
		weapons.setSelectedIndex(0);
		characters.setSelectedIndex(0);
		JPanel comboboxes = new JPanel(new GridLayout(3, 2));
		
		comboboxes.add(new JLabel("Room: "));
		comboboxes.add(rooms);
		comboboxes.add(new JLabel("Weapon: "));
		comboboxes.add(weapons);
		comboboxes.add(new JLabel("Character: "));
		comboboxes.add(characters);
		
		JButton confirmAccusation = new JButton("Confirm accusation");
		confirmAccusation.setFont(new Font(confirmAccusation.getFont().getName(), Font.PLAIN, 12));
		confirmAccusation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if(ev.getSource() == confirmAccusation) {
					/* Adding an extra confirmation window to ensure that the player didn't accidentally click the confirm button and that they do really want to 
					 * make the accusation. */ 
					int answer = JOptionPane.showConfirmDialog(game.getGui(), "Are you sure you want to make this accusation?", "Confirm accusation", JOptionPane.YES_NO_OPTION);
					if (answer == JOptionPane.YES_OPTION) {
						//If the accusation was correct
						if (game.getCurrentPlayersTurn().makeAccusation(game.getGui(), (String) rooms.getSelectedItem(), (String) weapons.getSelectedItem(), (String) characters.getSelectedItem())) {
							drawWinningPanel(); //Then they won
						}
						//Otherwise the player lost
						else {
							drawLosingPanel();
						}
						accusationPane.dispose(); //Removing the accusation creator
						revalidate();
						repaint();
					}
				 }	
				
			}});
		accusationBox.add(makeAccusationInfo, constraint);
		constraint.gridy = 1;
		accusationBox.add(comboboxes, constraint);
		constraint.gridy = 2;
		accusationBox.add(confirmAccusation, constraint);
		accusationPane.add(accusationBox);
		accusationPane.setVisible(true);
		accusationPane.pack();
		
	}

	/** Highlights the locations on the board which the player can move to (player can only move one step per movement) */
	protected void highlightMoveSpots() {
		boolean canMove = false; //Can the player move in North, South, East, West?
		boolean cantEnter = false; //If the location being highlighted is a doorwway, it should only be highlighted if they are on the entrance to that doorway
		//Players coordinates
		int playerX = game.getCurrentPlayer().getToken().getX();
		int playerY = game.getCurrentPlayer().getToken().getY();
		int playerIndex = playerX + playerY*24;
		int northIndex, southIndex, eastIndex, westIndex;
		
		//For the east direction 
		if (playerX != 23) {
			//Only consider doorways or free spaces
			if (game.getBoard().getLocation(playerX+1, playerY).getType().getLocType() == loctype.DOORWAY || game.getBoard().getLocation(playerX+1, playerY).getType().getLocType() == loctype.FREESPACE) {
				//Only consider locations we haven't been on this turn
				if (!game.getCurrentPlayersTurn().getPrevLoc().contains(game.getBoard().getLocation(playerX+1, playerY))) {
					//Only consider unoccupied locations
					if (game.getBoard().getLocation(playerX+1, playerY).getPlayer() == null) {
						//If its a doorway then we have to find out whether its possible to enter it
						if (game.getBoard().getLocation(playerX+1, playerY).getType().getLocType() == loctype.DOORWAY) {
							//If we have entered the room in this turn then we can't re enter it
							if (game.getInaccesibleLocations().contains(game.getBoard().getLocation(playerX+1, playerY))) {
								cantEnter = true;
							}
							else {
								/* Also checking that the player is on the room exit (doorway entrance) for the room they are trying to enter, making sure they are
								entering from the right direction */
								Room r = game.getBoard().getRoom(game.getBoard().getLocation(playerX+1, playerY).getType().getRoomName());
								boolean onExit = false;
								for (Location l : r.getExits()) {
									if (l.getX() == playerX && l.getY() == playerY) {
										onExit = true;
									}
								}
								if (!onExit) {
									cantEnter = true;
								}
							}
						}
						if (!cantEnter) {
							//Highlight the doorway since it is possible for us to enter the room using that doorway
							eastIndex = playerIndex + 1;
							JPanel eastPanel = (JPanel) board.getComponent(eastIndex);
							eastPanel.setBackground(new Color(246, 250, 111));
							canMove = true;
						}
					}
				}
			}
		}
		
		//Same as with east but for west
		cantEnter = false;
		if (playerX != 0) {
			if (game.getBoard().getLocation(playerX-1, playerY).getType().getLocType() == loctype.DOORWAY || game.getBoard().getLocation(playerX-1, playerY).getType().getLocType() == loctype.FREESPACE) {
				if (!game.getCurrentPlayersTurn().getPrevLoc().contains(game.getBoard().getLocation(playerX-1, playerY))) {
					if (game.getBoard().getLocation(playerX-1, playerY).getPlayer() == null) {
						if (game.getBoard().getLocation(playerX-1, playerY).getType().getLocType() == loctype.DOORWAY) {
							if (game.getInaccesibleLocations().contains(game.getBoard().getLocation(playerX-1, playerY))) {
								cantEnter = true;
							}
						}
						
						if (!cantEnter) {
							westIndex = playerIndex - 1;
							JPanel westPanel = (JPanel) board.getComponent(westIndex);
							westPanel.setBackground(new Color(246, 250, 111));
							canMove = true;
						}
					}
				}
			}
		}
		
		//For north
		cantEnter = false;
		if (playerY != 0) {
			if (game.getBoard().getLocation(playerX, playerY-1).getType().getLocType() == loctype.DOORWAY || game.getBoard().getLocation(playerX, playerY-1).getType().getLocType() == loctype.FREESPACE) {
				if (!game.getCurrentPlayersTurn().getPrevLoc().contains(game.getBoard().getLocation(playerX, playerY-1))) {
					if (game.getBoard().getLocation(playerX, playerY-1).getPlayer() == null) {
						if (game.getBoard().getLocation(playerX, playerY-1).getType().getLocType() == loctype.DOORWAY) {
							if (game.getInaccesibleLocations().contains(game.getBoard().getLocation(playerX, playerY-1))) {
								cantEnter = true;
							}
						}
						
						if (!cantEnter) {
							northIndex = playerIndex - 24;
							JPanel northPanel = (JPanel) board.getComponent(northIndex);
							northPanel.setBackground(new Color(246, 250, 111));
							canMove = true;
						}
					}
				}
			}
		}
		
		//For south
		cantEnter = false;
		if (playerY != 24) {
			if (game.getBoard().getLocation(playerX, playerY+1).getType().getLocType() == loctype.DOORWAY || game.getBoard().getLocation(playerX, playerY+1).getType().getLocType() == loctype.FREESPACE) {
				if (!game.getCurrentPlayersTurn().getPrevLoc().contains(game.getBoard().getLocation(playerX, playerY+1))) {
					if (game.getBoard().getLocation(playerX, playerY+1).getPlayer() == null) {
						if (game.getBoard().getLocation(playerX, playerY+1).getType().getLocType() == loctype.DOORWAY) {
							if (game.getInaccesibleLocations().contains(game.getBoard().getLocation(playerX, playerY+1))) {
								cantEnter = true;
							}
						}
						
						if (!cantEnter) {
							southIndex = playerIndex + 24;
							JPanel southPanel = (JPanel) board.getComponent(southIndex);
							southPanel.setBackground(new Color(246, 250, 111));
							canMove = true;
						}
					}
				}
			}
		}
		
		//If we can't move at all 
		if (!canMove) {
			showCantMove(); //Then tell this to the player
			//Now they can make accusations or end their turn
			makeAccusation.setEnabled(true);
			endTurn.setEnabled(true);
			//I don't set suggestion to enabled because there is never a situation where the player can't move and still have steps left and is in a room
		}
	}

	/** Shows a window informing the player that there is no direction that they can move in */
	private void showCantMove() {
	    Object[] options = {"OK"};
	    int cantMove = JOptionPane.showOptionDialog(this,
	                   "Unfortuantely there is nowhere for you to move. You can still make an accusation. ","Nowhere to move",
	                   JOptionPane.PLAIN_MESSAGE,
	                   JOptionPane.PLAIN_MESSAGE,
	                   null,
	                   options,
	                   options[0]);
	    
	}
	
	/** Shows a window informing the player that all hallways are blocked by players and therefore inaccessible. */
	private void showAllHallwaysBlocked() {
	    Object[] options = {"OK"};
	    int cantMove = JOptionPane.showOptionDialog(this,
	                   "Unfortuantely all room exits are blocked by players. You can't make a suggestion this turn but can still make an accusation. ","Room exits blocked",
	                   JOptionPane.PLAIN_MESSAGE,
	                   JOptionPane.PLAIN_MESSAGE,
	                   null,
	                   options,
	                   options[0]);
	}

	/** Draws the dice numbers on the dice based on the integers generated from the simulated dice rolls in game.
	 * @param rolls The results of the simulated dice roll in game */
	protected void drawDiceNums(ArrayList<Integer> rolls) {
		//Draw 1st die roll
		JLabel diceOneText = (JLabel) diceOne.getComponent(0);
		diceOneText.setText(Integer.toString(rolls.get(0)));
		diceOneText.setFont(new Font(diceOneText.getFont().getName(), Font.PLAIN, 24));
		
		//Draw 2nd die roll
		JLabel diceTwoText = (JLabel) diceTwo.getComponent(0);
		diceTwoText.setText(Integer.toString(rolls.get(1)));
		diceTwoText.setFont(new Font(diceTwoText.getFont().getName(), Font.PLAIN, 24));
	}

	/** Draws all the color references */
	private void drawReferences(Box box) {
		drawReference(box, Color.white, "This is free space:   "); //Free space reference
		drawReference(box, Color.gray, "This is a wall:   "); //Wall reference
		drawReference(box, new Color(70, 218, 235), "This is a room:   "); //Room reference 
		drawReference(box, new Color(60, 99, 255), "This is a rooms doorway (inside the room):"); //Doorway reference
		drawReference(box, new Color(71, 228, 92), "This is a rooms doorway entrance (outside the room):"); //Doorway entrance
		drawReference(box, new Color(244, 139, 255), "This is a players character token location:"); //Player location reference
		drawReference(box, new Color(246, 250, 111), "This is a location which can be moved onto:"); //Move location reference
	
	}

	/** Draws a singular reference when given the color and name for it
	 * @param box The pane the reference is being added to
	 * @param color The color of the reference
	 * @param info The description of the reference */
	private void drawReference(Box box, Color color, String info) {
		JPanel iconPanel = new JPanel(); //Panel for holding the color cell icon
		
		JPanel icon = new JPanel(); //The color cell icon
		icon.setBackground(color); //The color of the cell
		Border border = BorderFactory.createLineBorder(Color.black, 1);
		icon.setBorder(border);
		//Make the icon a bit bigger so its easier to see
		icon.setPreferredSize(new Dimension((int) (icon.getPreferredSize().getWidth()*2.5), (int) (icon.getPreferredSize().getHeight()*2.5)));
		
		//Adding the cell description
		JLabel iconInfo = new JLabel(info);
		iconInfo.setFont(new Font(iconInfo.getFont().getName(), Font.PLAIN, 24));
		
		iconPanel.add(iconInfo);
		iconPanel.add(icon);
	
		box.add(iconPanel); //Adds it to the main pane
	}

	/** Draws the two blank dice under the roll dice button */
	private void drawDice(JPanel dicePanel) {
		// TODO Auto-generated method stub
		diceOne = new JPanel();
		diceTwo = new JPanel();
		//Set the color to light red
		diceOne.setBackground(new Color(246, 250, 111));
		diceTwo.setBackground(new Color(246, 250, 111));
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
			/* Setting color of card based on type so its easier to know what type of card each card is and the tool tip so they know what kind of card it is 
			 * when they hover over it
			 */
			if (c instanceof WeaponCard) {
				newCard.setBackground(new Color(255, 98, 98));
				newCard.setToolTipText("Weapon Card");
			}
			else if (c instanceof CharacterCard) {
				newCard.setBackground(new Color(244, 139, 255));
				newCard.setToolTipText("Character Card");
			}
			else {
				newCard.setBackground(new Color(70, 218, 235));
				newCard.setToolTipText("Room Card");
			}
			newCard.addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent e) {}
				@Override
				public void mousePressed(MouseEvent e) {}
				@Override
				public void mouseReleased(MouseEvent e) {}
				@Override
				public void mouseEntered(MouseEvent e) {
					//If the card is hovered over set the border to white to highlight it
					newCard.setBorder(BorderFactory.createLineBorder(Color.white, 1));
				}

				@Override
				public void mouseExited(MouseEvent e) {
					//Set it back to normal when it is no longer being hovered over
					newCard.setBorder(BorderFactory.createLineBorder(Color.black, 1));
					
				}
				
			});
			
			newCard.add(cardLabel);
			cardPanel.add(newCard);
		}
	}

	/** Removing an option (character) from the combo box, by deleting a character from the string array it uses
	 * @param charName The name of the character being deleted */
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

	/** Remove the radio button group in the initial GUI 
	 * @param buttonGroup The button group that is being removed*/
	protected void removeButtonGroup(ButtonGroup buttonGroup) {
		Enumeration<AbstractButton> s = buttonGroup.getElements();
		while(s.hasMoreElements()) { //Keep going until there is no more elements
			this.remove(s.nextElement());
		}
	}

	/** Create and add the player number radio buttons to the group, with their appropriate label
	 * @param i The number of buttons being added */
	public void addButtons(int i) {
		for(int j = 0; j < i ; j++) {
			JRadioButton b = new JRadioButton(Integer.toString(j+1), true);
			b.setActionCommand(Integer.toString(j+1));
			add(b);
			numPlayers.add(b);
		}
		
	}
	
	/* Sets the label of one JLabel called 'info'. This label is used in different positions throughout the program */
	public void setLabel(String string) {
		info.setText(string);
	}

	/** Moves the player by one step if possible otherwise shows a message letting the user know that it is not a possible move
	 * @param index The index of the panel in the GUI board that the user wants to move to */
	private void movePlease(int index) {
		//Attempt to move
		if(game.move(index)) {
			//Show the players movement
			clearColors();
			drawNames();
			//Update the tool tips of the players, rooms and weapons
			setPlayerToolTips();
			setRoomToolTips();
			setWeaponToolTips();
			//Only highlight move spots if they have moves left
			if (game.getCurrentPlayersTurn().getSteps() != 0) {
				highlightMoveSpots();
			}
			else {
				//If they are out of moves then they can end their turn/make an accusation
				endTurn.setEnabled(true);
				makeAccusation.setEnabled(true);
				if (game.getCurrentPlayer().getToken().getRoom() != null) {
					//They can also make a suggestion if they are in a room
					makeSuggestion.setEnabled(true);
				}
			}
			revalidate();
			repaint();
		}
		//If the move is invalid
		else {
			//Then show them the appropriate message
			if (game.getCurrentPlayersTurn().getSteps() != 0) {
				String[] options = {"OK"};
				int answer = JOptionPane.showOptionDialog(game.getGui(), "That move is invalid. Please move onto one of the highlighted (yellow) locations", "Invalid move", JOptionPane.OK_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
			}
		}
		
	}

	/** Updates the player name when they exit the room
	 * @param index The index of the room exit in the GUI Board
	 * @param xOG The original x coordinate the player had before moving
	 * @param yOG The original y coordinate the player had before moving
	 *  */
	private void drawNamesWhenExiting(int index, int xOG, int yOG) {
		//Getting the old and new position
		JPanel newP = (JPanel) board.getComponent(index);
		JPanel oldP = (JPanel) board.getComponent((xOG + yOG*24));
		JLabel label = (JLabel) oldP.getComponent(0); //Getting the player name
		newP.add(label); //Adding it to the exit
		if (oldP.getComponents().length != 0) { //Removing the name from the previous location
			oldP.remove(0);	
		}
	}

	/** Updates the cell colors when the player exits the room
	 * @param index The index of the room exit in the GUI Board
	 * @param xOG The original x coordinate the player had before moving
	 * @param yOG The original y coordinate the player had before moving
	 *  */
	private void clearColorsWhenExiting(int index, int xOG, int yOG) {
		JPanel newP = (JPanel) board.getComponent(index);
		JPanel oldP = (JPanel) board.getComponent((xOG + yOG*24));
		//Setting the background color (cell color) of the old and new location
		newP.setBackground(oldP.getBackground());
		oldP.setBackground(new Color(70, 218, 235));
		
		for (Location l : roomExits) {
			if (board.getComponent(l.getX() + l.getY()*24).getBackground().equals(new Color(246, 250, 111))) {
				//Removing the highlight from all the highlighted exits
				board.getComponent(l.getX() + l.getY()*24).setBackground(new Color(71, 228, 92));
			}
		}
		//Highlight the locations which the user can move onto from the exit
		highlightMoveSpots();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		/** Whenever the user hover overs a cell in the board a thick red border is created for that cell to highlight it. */
		Component c = (Component) e.getSource();
		JPanel panel = (JPanel) c;
		panel.setBorder(BorderFactory.createLineBorder(new Color(127, 0, 0), 3));
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		//Reverting back to the normal border when the player stops hovering over the cell
		Component c = (Component) e.getSource();
		JPanel panel = (JPanel) c;
		panel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		
	}

	@Override
	public void mousePressed(MouseEvent e) {}
	
	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	/** Is used for detecting movements in the GUI Board */
	public void mouseReleased(MouseEvent e) {
		Object item = e.getSource();
		int xOriginal = game.getCurrentPlayer().getToken().getX();
		int yOriginal = game.getCurrentPlayer().getToken().getY();
		//If the user has clicked on a location on the board
		if (item instanceof JPanel) {
			JPanel locationClicked = (JPanel) item;
			if (locationClicked.getParent() == board && locationClicked != board) {
				//Then find its index in the JPanel board
				int index = 0;
				for (Component c : board.getComponents()) {
					JPanel j = (JPanel) c;
					if (j == locationClicked) {
						break;
					}
					index++;
				}
				//If we can move then call the move method
				if(game.canMove()) {
					movePlease(index);
				}
				//Or else we are moving to a room exit
				else if (!haveMoved) {
					if(game.moveToExit(index)) { //If its a possible move
						//Then update the GUI accordingly
						clearColorsWhenExiting(index, xOriginal, yOriginal);
						drawNamesWhenExiting(index, xOriginal, yOriginal);
						rollDice.setEnabled(true); //Allow them to move
						haveMoved = true;
					}
					else {
						//If the user doesn't click on the exit (and is in a room) then tell them this
						if (game.getCurrentPlayer().getToken().getRoom() != null) {
							String[] options = {"OK"};
							int answer = JOptionPane.showOptionDialog(game.getGui(), "That move is invalid. Please move onto one of the highlighted (yellow) locations", "Invalid move", JOptionPane.OK_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
						}
					}
				}
			}
		}
	}

	/** Shows the refuting window, a window where the player has multiple cards which they can use to refute a suggestion and must select one
	 * @param cardsFoundPerPlayer the cards the player can use to refute
	 * @param p The refuting player*/
	public void refute(ArrayList<Card> cardsFoundPerPlayer, Player p) {
		JDialog refutePane = new JDialog();
		refutePane.setTitle("Refute");
		refutePane.addWindowListener(new WindowAdapter() {
		    @Override
		    //If the user chooses to exit the window without selecting a card then by default the program will use the first card it finds from them to refute
		    public void windowClosing(WindowEvent e) {
		    	if (game.getCurrentPlayersTurn().getRefuteCard() == null) {
					game.getCurrentPlayersTurn().setRefuteCard(cardsFoundPerPlayer.get(0)); //Setting it to first card
					game.getCurrentPlayersTurn().setRefutePlayer(p);
					game.getCurrentPlayersTurn().finishRefute(game.getGui()); //Finish off the refute now that we have all the refuting cards
		    	}
		    }
		});
		JPanel refuteBox = new JPanel(new GridLayout(3, 1));
		refuteBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		//Making the text
		JLabel refuteHeading = new JLabel(p.getName() + " you have multiple cards you can use to refute, which one will you pick?");
		refuteBox.add(refuteHeading);
		
		//Showing the refute options in a combo box
		String[] cardNames = new String[cardsFoundPerPlayer.size()];
		int i = 0;
		for (Card c : cardsFoundPerPlayer) {
			cardNames[i] = c.getName();
			i++;
		}
		JComboBox<String> cardChoices = new JComboBox<String>(cardNames);
		refuteBox.add(cardChoices);
		
		//Confirming button
		JButton confirmCardChoice = new JButton("Confirm card choice");
		confirmCardChoice.setFont(new Font(confirmCardChoice.getFont().getName(), Font.PLAIN, 12));
		confirmCardChoice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if(ev.getSource() == confirmCardChoice) {
					//If the button is pressed
					for (Card c : cardsFoundPerPlayer) {
						//Find the card the player chose to refute from the list
						if (c.getName().equalsIgnoreCase((String) cardChoices.getSelectedItem())) {
							game.getCurrentPlayersTurn().setRefuteCard(c); //Set the card that was refuted to this card 
							game.getCurrentPlayersTurn().setRefutePlayer(p);
							game.getCurrentPlayersTurn().finishRefute(game.getGui());
						}
					}
					//Dispose of the refuting window since we no longer need it
					refutePane.dispose();
					revalidate();
					repaint();
				 }	
			}});
		refuteBox.add(confirmCardChoice);
		
		refutePane.add(refuteBox);
		refutePane.setVisible(true);
		refutePane.pack();
		//Makes the refute pane a bit larger to allow a bit more room/spacing
		refutePane.setPreferredSize(new Dimension((int) (screenSize.getWidth()/40), (int) (screenSize.getHeight()/50)));
	}
	
	/** Displays the results of the suggestion (i.e. the refuting results = who refuted and what card they used to refute the suggestion) */
	public void displaySuggestionResults() {
		JDialog refutePane = new JDialog();
		refutePane.setTitle("Suggestion findings");
		JPanel refuteBox = new JPanel(new GridLayout(3, 1));
		refuteBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		//If no one could refute the suggestion then let the user know this
		if (game.getCurrentPlayersTurn().cardsFound.values().size() == 0) {
			JLabel noCardsToRefute = new JLabel("No one was able to refute your suggestion");
			refuteBox.add(noCardsToRefute);
		}
		else {
			//Adding the refuting info
			for (Entry<Player, Card> e : game.getCurrentPlayersTurn().cardsFound.entrySet()) {
				JLabel refuteInfo = new JLabel(e.getKey().getName() + " used the card '" + e.getValue().getName() + "' to refute your suggestion");
				refuteBox.add(refuteInfo);
			}
		}
		refutePane.add(refuteBox);
		refutePane.setVisible(true);
		refutePane.pack();
		//Move the tokens to the suggested room if they need to be moved
		game.getCurrentPlayersTurn().moveTokens(game.getPlayers(), game.getBoard(), game);
		//Updating the GUI after the suggestion
		drawNames();
		drawWeapons();
		clearAfterSuggestion();
		setPlayerToolTips();
		setRoomToolTips();
		setWeaponToolTips();
		revalidate();
		repaint();
	}

	/** Clearing the label of a doorway entrance/room exit
	 * @param l The location which will have its label removed */
	public void clearEntranceLabel(Location l) {
		JPanel p = (JPanel) board.getComponent((l.getX() + l.getY()*24));
		p.remove(0);
	}

	/** Moving the label and color of the player when they are moved into a room after losing on a doorway entrance/room exit
	 * @param i The old x coordinate of the player
	 * @param j The old y coordinate of the player */
	public void clearAfterDeadMovement(int i, int j) {
		//Getting the old location
		Component c = board.getComponent((i + j*24));
		JPanel panel = (JPanel) c;
		JLabel label = (JLabel) panel.getComponent(0);
		//Removing the label from the old location and reverting its color
		panel.remove(0);
		panel.setBackground(new Color(71, 228, 92)); //Back to the green color the doorway entrances have
		
		//Adding the label and color of a player (pink) to the players current (and final) location.
		Component cNew = board.getComponent(game.getCurrentPlayer().getToken().getX() + game.getCurrentPlayer().getToken().getY()*24);
		JPanel newPanel = (JPanel) cNew;
		newPanel.setBackground(new Color(244, 139, 255));
		newPanel.add(label);
	}
	
}