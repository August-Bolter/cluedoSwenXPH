package Java;
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
	
	private JTextField tField;
	private JRadioButton b1;
	private JRadioButton b2;
	private ButtonGroup group;
	private ArrayList<Player> players = new ArrayList<Player>();
	private ArrayList<String> characterNames  = new ArrayList<String>(); //All the possible character names the character tokens can have
	private GuiState state;
	
	enum GuiState{
		Setup, SetupDone
	}
		
	public Gui(){
		super("Cleudo");
		setLayout(new FlowLayout());
		fieldSetup();
		
		tField.setText("Cleudo");
		
		group = new ButtonGroup();
		addButtons(6);
		

	}
	
	public void addButtons(int i) {
		for(int j = 0; j <i ; j++) {
			JRadioButton b = new JRadioButton(Integer.toString(j+1),true);
			add(b);
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ev) {
					 if(ev.getSource()==b)	{
						 //add player to game
						 
					 }	
				}});
			group.add(b);
		}
		
	}
	
	/*public static void main(String[] args) {
		Gui gui = new Gui();
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setSize(250,500);
		gui.setVisible(true);
	}*/
	
	public void fieldSetup() {
		characterNames.add("Mrs. Peacock");
		characterNames.add("Colonel Mustard");
		characterNames.add("Miss Scarlett");
		characterNames.add("Professor Plum");
		characterNames.add("Mrs. White");
		characterNames.add("Mr. Green");
		
		tField = new JTextField();
		add(tField);
		
		this.state = GuiState.Setup;
	}
	
	public void setText(String s) {
		tField.setText(s);
	}
	
}
