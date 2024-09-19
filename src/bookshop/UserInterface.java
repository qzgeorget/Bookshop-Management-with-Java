package bookshop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.util.*;

//Class UserInterface creates a window for all the pages to be contained and exist in 
public class UserInterface {
	
	//Declares a window to be accessible anywhere in the package
	static JFrame mainFrame = new JFrame();
	
	//Method for the page displaying the opening page of the system, allows the user to choose their user-name and account
	public void mainPage() throws FileNotFoundException {
		
		//Declares a file manipulator to be passed into all succeeding pages to access Stock.txt and UserAccounts.txt
		FileManipulator fileManipulator = new FileManipulator();
		
		//Reads all users into an array
		ArrayList<User> userArray = fileManipulator.readAllUsers();
		
		//Reads all users' user-name into an array
		String[] usernameArray = new String[userArray.size()];
		for (int x = 0; x < userArray.size(); x++) {
			usernameArray[x] = userArray.get(x).getUsername();
		}
		
		JPanel primaryPanel = new JPanel();
		
		//Instruction label to prompt the user about their next course of action 
		JLabel instructionLabel = new JLabel("Select your username:");
		
		JLabel reminderLabel = new JLabel();
		
		//Combo box allows the user to choose their user-name using a drop down menu
	    JComboBox<String> idSelector = new JComboBox<String>(usernameArray);
	    
	    //Submit button passes the value chosen in the drop down menu into its action listener
	    JButton submitButton = new JButton("Submit"); 
	    submitButton.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){  
	    		//Iterate through all users
	    		for (int x = 0; x < userArray.size(); x++) {
	    			//Find corresponding user object with the same user-name
	    			if (userArray.get(x).getUsername().compareTo(idSelector.getItemAt(idSelector.getSelectedIndex()).toString()) == 0) {
	    				primaryPanel.setVisible(false);
						try {
							//Call each the selected user object's Interface() method for the next page
							userArray.get(x).Interface(fileManipulator);
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
	    			}
	    		}
	    	}
	    });
	    
	    JButton exitButton = new JButton("Exit"); 
	    exitButton.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){  
	    		mainFrame.setVisible(false);  
	    	}
	    });
		
	    primaryPanel.setBackground(Color.lightGray);
	    primaryPanel.setBounds(0,0,1400,800);
	    primaryPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints gbcPointer = new GridBagConstraints();
		gbcPointer.insets = new Insets(2,2,2,2);

		gbcPointer.gridx = 0;
		gbcPointer.gridy = 0;
		primaryPanel.add(instructionLabel, gbcPointer);
		
		gbcPointer.gridx = 1;
	    primaryPanel.add(idSelector, gbcPointer);
	    
	    gbcPointer.gridx = 0;
	    gbcPointer.gridy = 1;
	    primaryPanel.add(submitButton, gbcPointer);
	    
	    gbcPointer.gridx = 1;
	    primaryPanel.add(exitButton, gbcPointer);
	    
	    gbcPointer.gridx = 0;
	    gbcPointer.gridy = 2;
	    gbcPointer.gridwidth = 2;
	    primaryPanel.add(reminderLabel, gbcPointer);
		
	    //Declare window attributes
		mainFrame.setVisible(true);      
	    mainFrame.setSize(1400,800);  
	    mainFrame.setTitle("BOOK SHOP"); 
	    mainFrame.setLayout(null);
	    mainFrame.add(primaryPanel); 
	}
	
	//Main method for the system to start and boot up
	public static void main(String[] args) throws FileNotFoundException {
		UserInterface userInterface = new UserInterface();
		userInterface.mainPage();
	}
}
