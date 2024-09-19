package bookshop;

//Imported user interface libraries
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

//Imported for file access
import java.io.*;
import java.util.*;

//Admin class inherits attributes and methods from abstract class, User
public class Admin extends User{
	
	//Attribute address requires further manipulation after reading from file, therefore cannot be directly inherited from User
	private String address;
	
	//Constructor for paperback inheriting from Book
	public Admin(int id, String username, String surname, int houseNumber, String postcode, String city) {
		super(id, username, surname, houseNumber, postcode, city);
		this.address = houseNumber + ", " + postcode + ", " + city;
	}
	
	//Get method for address
	public String getAddress() {
		return address;
	} 
	
	//Unique string conversion method for Admin objects
	public String toString() {
		return (this.getId() + ", " + this.getUsername() + ", " + this.getSurname()  + ", " + this.getAddress() + ", , admin");
	}
	
	//Private, internal use method to check if the book entered by admin is already inside of the stock records already
	private boolean bookCompare(FileManipulator fileManipulator, Book tempBook) throws FileNotFoundException {
		
		//Read the record into an ArrayList
		ArrayList<Book> rawArray = fileManipulator.readAllBooks();
		
		//Iterate through record
		for (int x = 0; x < rawArray.size(); x++) {
			Book rawBook = rawArray.get(x);
			
			 //Compare bar-code and title between the book entered by the admin and the books in the record
			if ((rawBook.getBarcode() == tempBook.getBarcode()) && (rawBook.getTitle().compareTo(tempBook.getTitle()) == 0)) {
				return(true);
			}
		}
		return (false);
	}
	
	//User interface page for the admin to enter information of a book to be added to the stock
	public void addToStockPage(FileManipulator fileManipulator) throws FileNotFoundException {
		
		//Panel to contain all widgets
		JPanel tertiaryPanel = new JPanel(); 
		
		//Label for in case information entered is invalid
		JLabel reminderLabel = new JLabel();
		
		//Radio button to choose which type of book the admin is entering
		JRadioButton paperbackRadio = new JRadioButton("Paperback Book");
		JRadioButton ebookRadio = new JRadioButton("EBook");
		JRadioButton audiobookRadio = new JRadioButton("Audiobook");
		
		//Grouping three radio buttons together
		ButtonGroup bookTypeRadio = new ButtonGroup();
		bookTypeRadio.add(paperbackRadio);
		bookTypeRadio.add(ebookRadio);
		bookTypeRadio.add(audiobookRadio);
		
		//Label and text field for the two unique attributes for each type of book declared now
		JLabel firstTempLabel = new JLabel("");
		JTextField firstTempField = new JTextField(10);
		JLabel secondTempLabel = new JLabel("");
		JTextField secondTempField  = new JTextField(10);
		
		//Radio buttons would decide the value of the unique labels for each type of book, prompting the admin to enter the correct information
		ActionListener bookTypeSelect = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (paperbackRadio.isSelected()) {
					firstTempLabel.setText("Page Count: ");
					secondTempLabel.setText("Condition: ");
				} else if (ebookRadio.isSelected()) {
					firstTempLabel.setText("Page Count: ");
					secondTempLabel.setText("Format: ");
				} else if (audiobookRadio.isSelected()) {
					firstTempLabel.setText("Length: ");
					secondTempLabel.setText("Format: ");
				}
			}
		};
		paperbackRadio.addActionListener(bookTypeSelect);
		ebookRadio.addActionListener(bookTypeSelect);
		audiobookRadio.addActionListener(bookTypeSelect);
		
		//Label to prompt admin to choose a type of book
		JLabel bookTypeLabel = new JLabel("Type of book: ");
		
		//Creation of an array to contain the labels for the common attributes of the book to be added regardless of type
		String[] inputLabels = {"Barcode:", "Title: ", "Language: ", "Genre: ", "Release Date: ", "Quantity:", "Retail Price: "};
		JLabel[] labelArray = new JLabel[7];
		for (int x = 0; x < 7; x++) {
			JLabel tempLabel = new JLabel(inputLabels[x]);
			labelArray[x] = tempLabel;
		}
		
		//Creation of an array to contain the text fields for the common attributes prompted in the labels above
		JTextField[] textFieldArray = new JTextField[7];
		for (int x = 0; x < 7; x++) {
			JTextField tempTextField = new JTextField(10);
			textFieldArray[x] = tempTextField;
		}
		
		//Button trigger to carry out the check for the book existence or absence in stock
		JButton submitButton = new JButton("Submit");
		
		//Passing the current admin object as a accessible reference in the action listener for the submit button 
		Admin currentUser = this;
		
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				
				//To check that all fields are filled
				boolean fieldsFull = true;
				for (int x = 0; x < 7; x++) {
					if (textFieldArray[x].getText().isBlank()) {
						fieldsFull = false;
					}
				}
				if ((firstTempField.getText().isBlank())||(secondTempField.getText().isBlank())) {
					fieldsFull = false;
				}
				
				if (fieldsFull == true) {
					
					//Separate scenarios are to be created for each type of book as their constructors are different
					if (paperbackRadio.isSelected()) {
						PaperbackBook tempBook = new PaperbackBook(Integer.valueOf(textFieldArray[0].getText()),textFieldArray[1].getText(),textFieldArray[2].getText(),
								textFieldArray[3].getText(),textFieldArray[4].getText(),Integer.valueOf(textFieldArray[5].getText()),
								Double.valueOf(textFieldArray[6].getText()),Integer.valueOf(firstTempField.getText()),secondTempField.getText());
						
						//Call to method bookCompare() to check if book is already in stock or not
						try {
							if (currentUser.bookCompare(fileManipulator, tempBook)) {
								reminderLabel.setText("Book is already in stock.");	
							} else {
								fileManipulator.addToStock(tempBook.toString());
							}
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else if (ebookRadio.isSelected()) {
						EBook tempBook = new EBook(Integer.valueOf(textFieldArray[0].getText()),textFieldArray[1].getText(),textFieldArray[2].getText(),
								textFieldArray[3].getText(),textFieldArray[4].getText(),Integer.valueOf(textFieldArray[5].getText()),
								Double.valueOf(textFieldArray[6].getText()),Integer.valueOf(firstTempField.getText()),secondTempField.getText());
						try {
							if (currentUser.bookCompare(fileManipulator, tempBook)) {
								reminderLabel.setText("Book is already in stock.");	
							} else {
								fileManipulator.addToStock(tempBook.toString());
							}
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else if (audiobookRadio.isSelected()) {
						AudioBook tempBook = new AudioBook(Integer.valueOf(textFieldArray[0].getText()),textFieldArray[1].getText(),textFieldArray[2].getText(),
								textFieldArray[3].getText(),textFieldArray[4].getText(),Integer.valueOf(textFieldArray[5].getText()),
								Double.valueOf(textFieldArray[6].getText()),Double.valueOf(firstTempField.getText()),secondTempField.getText());
						try {
							if (currentUser.bookCompare(fileManipulator, tempBook)) {
								reminderLabel.setText("Book is already in stock.");	
							} else {
								fileManipulator.addToStock(tempBook.toString());
							}
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
			} else {
				//If not all fields are filled, reminder label would be set to remind the admin
				reminderLabel.setText("Please enter all information for the book.");
				}
				
			}
		});
		
		//Back button to return to the main admin interface page
		JButton backButton = new JButton("Back");
		backButton.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){  
	    		try {
					currentUser.Interface(fileManipulator);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    		tertiaryPanel.setVisible(false);
	    	}
	    });
		
		//Exit button to close the window
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){  
	    		UserInterface.mainFrame.setVisible(false);  
	    	}
	    });
		
		//Page panel attributes are set
		tertiaryPanel.setBackground(Color.lightGray);
		tertiaryPanel.setBounds(0,0,1400,800);
		tertiaryPanel.setLayout(new GridBagLayout());
		
		//Layout Pointer declaration
		GridBagConstraints gbcPointer = new GridBagConstraints();
		gbcPointer.insets = new Insets(2,2,2,2);
		
		//Updating layout pointer every time it needs to be moved for a new widget's placement
		gbcPointer.gridx = 0; gbcPointer.gridy = 0; gbcPointer.gridwidth = 1; tertiaryPanel.add(bookTypeLabel, gbcPointer);
		gbcPointer.gridx = 1; tertiaryPanel.add(paperbackRadio, gbcPointer);
		gbcPointer.gridx = 2; tertiaryPanel.add(ebookRadio, gbcPointer);
		gbcPointer.gridx = 3; tertiaryPanel.add(audiobookRadio, gbcPointer);
		
		//Using a loop to iterate through all the labels and text fields to reduce repeating code
		gbcPointer.gridwidth = 2;
		for (int y = 0; y < 7; y++) {
			gbcPointer.gridx = 0;
			gbcPointer.gridy = y + 3;
			tertiaryPanel.add(labelArray[y], gbcPointer);
			gbcPointer.gridx = 2;
			tertiaryPanel.add(textFieldArray[y], gbcPointer);
		}
		
		gbcPointer.gridx = 0; gbcPointer.gridy = 10; tertiaryPanel.add(firstTempLabel, gbcPointer);
		gbcPointer.gridx = 2; tertiaryPanel.add(firstTempField, gbcPointer);
		gbcPointer.gridx = 0; gbcPointer.gridy = 11; tertiaryPanel.add(secondTempLabel, gbcPointer);
		gbcPointer.gridx = 2; tertiaryPanel.add(secondTempField, gbcPointer);
		
		gbcPointer.gridx = 0; gbcPointer.gridy = 12; gbcPointer.gridwidth = 4; tertiaryPanel.add(submitButton, gbcPointer);
		gbcPointer.gridy = 13; tertiaryPanel.add(backButton, gbcPointer);
		gbcPointer.gridy = 14; tertiaryPanel.add(exitButton, gbcPointer);
		
		gbcPointer.gridy = 15; tertiaryPanel.add(reminderLabel, gbcPointer);
		
		//Adding page to the window
		UserInterface.mainFrame.add(tertiaryPanel);
	}
	
	//Main interface page for admin users
	public void Interface(FileManipulator fileManipulator) throws FileNotFoundException {
		
		//Reading all books in stock into an ArrayList
		ArrayList<Book> rawArray = fileManipulator.readAllBooks();
		Admin currentUser = this;
		
		//Sorting all books into their own types
		ArrayList<Object[]> paperbackArray = fileManipulator.readAllPaperbackToString(rawArray);
		ArrayList<Object[]> ebookArray = fileManipulator.readAllEBookToString(rawArray);
		ArrayList<Object[]> audiobookArray = fileManipulator.readAllAudioBookToString(rawArray);
		
		//Putting the three book type arrays into an array
		ArrayList<ArrayList<Object[]>> rawBookArrays = new ArrayList<ArrayList<Object[]>>();
		rawBookArrays.add(paperbackArray);
		rawBookArrays.add(ebookArray);
		rawBookArrays.add(audiobookArray);
		
		//Iterating through the three arrays, and then iterating through the books in each book type to order by quantity
		ArrayList<Object[][]> finalBookArrayArrays = new ArrayList<Object[][]>();
		for (ArrayList<Object[]> bookArray: rawBookArrays) {
			Object[][] finalBookArray = new Object[bookArray.size()][];
			for (int x = 0; x < bookArray.size(); x++) {
				finalBookArray[x] = bookArray.get(x);
			}
			Arrays.sort(finalBookArray, new Comparator<Object[]>() {
			    public int compare(Object[] a, Object[] b) {
			    	int x = Integer.valueOf((String)a[6]);
			    	int y = Integer.valueOf((String)b[6]);
			        return Double.compare(x, y);
			    }
			});
			finalBookArrayArrays.add(finalBookArray);
		}
		
		//Putting the headings for each book type in an array
		ArrayList<Object[]> headingArray = new ArrayList<Object[]>();
		Object[] paperbackHeadings = {"Barcode", "Type", "Title", "Language", "Genre", "Release Date", "Quantity", "Retail Price", "Page Count", "Condition"};
		Object[] ebookHeadings = {"Barcode", "Type", "Title", "Language", "Genre", "Release Date", "Quantity", "Retail Price", "Page Count", "Format"};
		Object[] audiobookHeadings = {"Barcode", "Type", "Title", "Language", "Genre", "Release Date", "Quantity", "Retail Price", "Length", "Format"};
		headingArray.add(paperbackHeadings); headingArray.add(ebookHeadings);headingArray.add(audiobookHeadings);
		
		JPanel secondaryPanel = new JPanel();
		
		//Putting three tables into an array to be referenced later
		ArrayList<JTable> tableArray = new ArrayList<JTable>();
		for (int x = 0; x < finalBookArrayArrays.size(); x++) {
			JTable tempTable = new JTable(finalBookArrayArrays.get(x), headingArray.get(x));
			tableArray.add(tempTable);
		}
		
		//Button to trigger the addToStockPage to allow admins to add books to stock
		JButton addToStockButton = new JButton("Add new book");
		addToStockButton.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){  
	    		try {
					currentUser.addToStockPage(fileManipulator);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}  
	    		secondaryPanel.setVisible(false);
	    	}
	    });
		
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){  
	    		UserInterface.mainFrame.setVisible(false);  
	    	}
	    });
		
		//Putting three scroll panes connected to the three tables for book types in an array to be called later
		ArrayList<JScrollPane> scrollArray = new ArrayList<JScrollPane>();
		for (int x = 0; x < finalBookArrayArrays.size(); x++) {
			JScrollPane tempScroll = new JScrollPane(tableArray.get(x));
			tempScroll.setMinimumSize(new Dimension(900,100));
			scrollArray.add(tempScroll);
		}
		
		secondaryPanel.setBackground(Color.lightGray);
		secondaryPanel.setBounds(0,0,1400,800);
		secondaryPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints gbcPointer = new GridBagConstraints();
		gbcPointer.insets = new Insets(2,2,2,2);
		
		//Iterating through the three book types to place scroll panes with tables on the page to reduce repeating code
		gbcPointer.gridx = 0;
		gbcPointer.gridwidth = 2;
		for (int y = 0; y < finalBookArrayArrays.size();y++) {
			gbcPointer.gridy = y;
			secondaryPanel.add(scrollArray.get(y), gbcPointer);
		}
		
		gbcPointer.gridy = 3;
		secondaryPanel.add(addToStockButton, gbcPointer);
		
		gbcPointer.gridy = 4;
		secondaryPanel.add(exitButton, gbcPointer);
		
	    UserInterface.mainFrame.add(secondaryPanel); 
	}
}
