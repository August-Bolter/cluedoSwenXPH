package code;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;

/**
 * Gui for cleudo game
 *@ extends JFrame   
 */
public class Gui extends JFrame {
	
	private JLabel info;
	private JLabel characterQ;
	private ButtonGroup numPlayers;
	private JButton confirmNumPlayers;
	private Game game;
	private int playerCount;
	private ArrayList<String> playerNames = new ArrayList<String>();
	private ArrayList<String> pCharacterNames = new ArrayList<String>();
	private String[] characterNames  = {"Mrs. Peacock", "Colonel Mustard", "Miss Scarlett", "Professor Plum", "Mrs. White", "Mr. Green"}; //All the possible character names the character tokens can have
	private GuiState state;
	
	enum GuiState{
		Setup, SetupDone
	}
		
	public Gui(Game g) {
		super("Cleudo");
		game = g;
		playerCount = 0;
		this.state = GuiState.Setup;
		setLayout(new FlowLayout());
		numPlayersSetup();
	}
	
	private void numPlayersSetup() {
		numPlayers = new ButtonGroup();
		info = new JLabel();
		this.add(info);
		addButtons(6);
		confirmNumPlayers = new JButton("Confirm");
		confirmNumPlayers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				 if(ev.getSource() == confirmNumPlayers) {
					 game.getGui().remove(confirmNumPlayers);
					 removeButtonGroup(numPlayers);
					 info.setText("");
					 revalidate();
					 repaint();
					 playersSetup(Integer.parseInt(numPlayers.getSelection().getActionCommand()));
				 }	
			}});
		this.add(confirmNumPlayers);
		
	}
	
	protected void playersSetup(int parseInt) {
		playerCount++;
		if (playerCount <= parseInt) {
			JPanel playerPanel = new JPanel();
			playerPanel.setLayout(new GridLayout(2, 2));
			Box formattingBox = Box.createVerticalBox();
			
			setLabel("Player " + playerCount + " what is your name?");
			JTextField playerName = new JTextField();
			characterQ = new JLabel("Choose your character");
			JComboBox<String> characters = new JComboBox<String>(characterNames);
			characters.setSelectedIndex(0);
			
			playerPanel.add(info);
			playerPanel.add(playerName);
			playerPanel.add(characterQ);
			playerPanel.add(characters);
			
			formattingBox.add(playerPanel);
			confirmNumPlayers = new JButton("Confirm");
			formattingBox.add(confirmNumPlayers);

			this.add(formattingBox);
			revalidate();
			repaint();
			confirmNumPlayers.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ev) {
					if(ev.getSource() == confirmNumPlayers) {
						removeCharacter((String) characters.getSelectedItem());
						pCharacterNames.add((String) characters.getSelectedItem());
						playerNames.add(playerName.getText());
						 
						game.getGui().remove(formattingBox);
						revalidate();
						repaint();
						playersSetup(parseInt);
					 }	
				}});
		}
		else {
			game.addPlayers(playerNames, pCharacterNames);
			game.deal(); //Deal the cards
		}
		
	}

	protected void removeCharacter(String charName) {
		// TODO Auto-generated method stub
		String[] newCharN = new String[characterNames.length-1];
		int index = 0;
		for (int i = 0; i < characterNames.length; i++) {
			if (characterNames[i] != charName) {
				newCharN[index] = characterNames[i];
				index++;
			}
		}
		characterNames = newCharN;
	}

	protected void removeButtonGroup(ButtonGroup buttonGroup) {
		Enumeration<AbstractButton> s = buttonGroup.getElements();
		while(s.hasMoreElements()) {
			this.remove(s.nextElement());
		}
	}

	public void deleteComponents(ArrayList<Component> components) {
		
	}

	public void addButtons(int i) {
		for(int j = 0; j < i ; j++) {
			JRadioButton b = new JRadioButton(Integer.toString(j+1), true);
			b.setActionCommand(Integer.toString(j+1));
			add(b);
			numPlayers.add(b);
		}
		
	}
	
	public void setLabel(String string) {
		// TODO Auto-generated method stub
		info.setText(string);
	}
	
}