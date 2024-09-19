package bookshop;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import java.io.*;
import java.util.*;

//Customer class inherits attributes and methods from abstract class, User
public class Customer extends User{
	private String address;
	
	//Unique attributes of the Customer class
	private ArrayList<Book> shoppingBasket;
	private double balance;
	
	//Unique constructor for the Customer class
	public Customer(int id, String username, String surname, int houseNumber, String postcode, String city, double balance) {
		super(id, username, surname, houseNumber, postcode, city);
		this.address = houseNumber + ", " + postcode + ", " + city;
		this.shoppingBasket = new ArrayList<Book>();
		this.balance = balance;
	}
	
	//Get methods for the two unique attributes
	public double getBalance() {
		return(this.balance);
	}
	
	//Get method for address
	public String getAddress() {
		return address;
	}

	public ArrayList<Book> getShoppingBasket() {
		return(this.shoppingBasket);
	}
	
	//Method to take an amount away from a customer's balance
	public void decrementBalance(double amount) {
		this.balance -= amount;
	}
	
	//Method to empty a customer's shopping basket
	public void emptyShoppingBasket() {
		this.shoppingBasket.clear();
	}
	
	public String toString() {
		return (this.getId() + ", " + this.getUsername() + ", " + this.getSurname()  + ", " + this.getAddress() + ", " + this.getBalance() + ", customer");
	}
	
	//Private, internal use method to add a book into the customer's shopping basket 
	//Method takes rowIndex as a parameter
	private void addToBasket(FileManipulator fileManipulator, ArrayList<Book> rawArray, int rowIndex, JLabel reminderLabel) {
		
		//rowIndex of the table the book was selected from will be used to reference the actual Book object
		Book selectedBook = rawArray.get(rowIndex);
		
		//Catching scenarios where the addition to shopping basket would be rejected
		//If the selected book is already in the basket
		if (shoppingBasket.contains(selectedBook)){
			reminderLabel.setText("Selected book is already in the basket.");
			
		//If the selected book is out of stock
		} else if (selectedBook.getQuantity() < 1) {
			reminderLabel.setText("Selected book is out of stock.");
		} else {
			this.shoppingBasket.add(selectedBook);
		}
	}
	
	//Method to generate the page displaying the receipt after a customer has paid for the books in their shopping basket
	public void payAndReceiptPage(FileManipulator fileManipulator, JLabel reminderLabel, JPanel tertiaryPanel) throws FileNotFoundException {
		Customer currentUser = this;
		
		//Calculating total price
		double totalPrice = 0;
		for (Book item: this.getShoppingBasket()) {
			totalPrice += item.getPrice();
		}
		
		//Calculating if the customer's balance can cover the price of the books in the shopping basket 
		if (this.getBalance() > totalPrice) {
			tertiaryPanel.setVisible(false);
			
			//Update the stock records to represent the stock for the selected books to go down
			try {
				fileManipulator.paymentStockUpdate(fileManipulator, currentUser);
			} catch (IOException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
			
			//Update the user records to represent the customer's new balance after the payment
			fileManipulator.paymentUsersUpdate(fileManipulator, currentUser, totalPrice);
			
			//Empty the customer's shopping basket
			currentUser.emptyShoppingBasket();
			
			JPanel quarternaryPanel = new JPanel();
			
			//Putting components of the receipt in an array to be displayed in a easy to read fashion without repeating code
			String[] stringArray = {"Receipt:","Thank you for the purchase!","£" + totalPrice,"paid and your remaining credit balance is","£" + currentUser.getBalance(),
					"Your delivery address is",currentUser.getAddress()};
			
			JLabel[] labelArray = new JLabel[7];
			
			//Iterating through the array to put the receipt in labels
			for (int x = 0; x < 7; x ++) {
				labelArray[x] = new JLabel(stringArray[x]);
			}
			
			JButton backButton = new JButton("Back");
			backButton.addActionListener(new ActionListener(){
		    	public void actionPerformed(ActionEvent e){  
		    		try {
						currentUser.Interface(fileManipulator);
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		    		quarternaryPanel.setVisible(false);
		    	}
		    });
			
			JButton exitButton = new JButton("Exit");
			exitButton.addActionListener(new ActionListener(){
		    	public void actionPerformed(ActionEvent e){  
		    		UserInterface.mainFrame.setVisible(false);  
		    	}
		    });
			
			quarternaryPanel.setBackground(Color.lightGray);
			quarternaryPanel.setBounds(0,0,1400,800);
			quarternaryPanel.setLayout(new GridBagLayout());
			
			GridBagConstraints gbcPointer = new GridBagConstraints();
			gbcPointer.insets = new Insets(2,2,2,2);
			
			gbcPointer.gridx = 0;
			gbcPointer.gridy = 0;
			
			for (JLabel label: labelArray) {
				quarternaryPanel.add(label, gbcPointer);
				gbcPointer.gridy += 1;
			}
			
			gbcPointer.gridy = 7;
			quarternaryPanel.add(backButton, gbcPointer);
			
			gbcPointer.gridy = 8;
			quarternaryPanel.add(exitButton, gbcPointer);
			
		    UserInterface.mainFrame.add(quarternaryPanel);
		} else {
			reminderLabel.setText("Insufficient balance.");
		}
	}
	
	//Method to generate the page displaying the customer's shopping basket and buttons to check out 
	public void checkOutPage(FileManipulator fileManipulator) throws FileNotFoundException {
		Customer currentUser = this;
		
		//Read the customer's shopping basket into an array
		ArrayList<Book> rawArray = this.getShoppingBasket();
	
		//Converting the ArrayList into an Object[][] for the JTable widget
		ArrayList<Object[]> bookArray = fileManipulator.readAllPaperbackToString(rawArray);
		bookArray.addAll(fileManipulator.readAllEBookToString(rawArray));
		bookArray.addAll(fileManipulator.readAllAudioBookToString(rawArray));
		Object[][] finalbookArray = new Object[bookArray.size()][];
		for (int x = 0; x < bookArray.size(); x++) {
			finalbookArray[x] = bookArray.get(x);
		}
		
		Object[] bookHeadings = {"Barcode", "Book Type", "Title", "Language", "Genre", "Release Date", "Quantity", "Retail Price", "Page No./ Page No./ Length", "Condition/ Format/ Format"};
		
		JPanel tertiaryPanel = new JPanel();
		
		JLabel reminderLabel = new JLabel();
		
		//Display the contents of the customer's shopping basket in a table
		JTable bookDisplay = new JTable(finalbookArray, bookHeadings);
		
		//Check out button to allow the customer to pay for the books after confirming the contents of the shopping basket
		JButton checkOutbutton = new JButton("Check out");
		checkOutbutton.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){  
	    		reminderLabel.setText("");
				try {
					currentUser.payAndReceiptPage(fileManipulator, reminderLabel, tertiaryPanel);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    	}
	    });
		
		//Button to empty shopping basket to start order over again
		JButton emptyBasketButton = new JButton("Empty basket");
		emptyBasketButton.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){  
	    		currentUser.emptyShoppingBasket();
	    		tertiaryPanel.setVisible(false);
	    		try {
					currentUser.checkOutPage(fileManipulator);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    	}
	    });
		
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
		
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){  
	    		UserInterface.mainFrame.setVisible(false);  
	    	}
	    });
		
		JScrollPane bookScroll = new JScrollPane(bookDisplay);  
		bookScroll.setPreferredSize(new Dimension(1200,300));
		
		tertiaryPanel.setBackground(Color.lightGray);
		tertiaryPanel.setBounds(0,0,1400,800);
		tertiaryPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints gbcPointer = new GridBagConstraints();
		gbcPointer.insets = new Insets(2,2,2,2);
		
		gbcPointer.gridx = 0;
		gbcPointer.gridy = 0;
		gbcPointer.gridwidth = 2;
		tertiaryPanel.add(reminderLabel, gbcPointer);
		
		gbcPointer.gridy = 1;
		tertiaryPanel.add(bookScroll, gbcPointer);
		
		gbcPointer.gridy = 2;
		tertiaryPanel.add(checkOutbutton, gbcPointer);
		gbcPointer.gridy = 3;
		tertiaryPanel.add(emptyBasketButton, gbcPointer);
		
		gbcPointer.gridy = 4;
		tertiaryPanel.add(backButton, gbcPointer);
		
		gbcPointer.gridy = 5;
		tertiaryPanel.add(exitButton, gbcPointer);
		
	    UserInterface.mainFrame.add(tertiaryPanel); 
	}
	
	//Private, internal use method to search books by their bar-code
	private ArrayList<Book> searchByBarcode(FileManipulator fileManipulator, JLabel reminderLabel, int barcode) throws FileNotFoundException {
		
		//Read all books into an array
		ArrayList<Book> rawArray = fileManipulator.readAllBooks();
		
		//Iterate through all books in the record
		ArrayList<Book> finalReturnArray = new ArrayList<Book>();
		boolean found = false;
		for (Book tempBook: rawArray) {
			//If the bar-code entered by the customer matches one of the books in the record, return the book
			if (tempBook.getBarcode() == barcode) {
				finalReturnArray.add(tempBook);
				found = true;
			}
		}
		//If no books are found, set reminder label to prompt the customer that no book has that bar-code in stock
		if (found == false) {
			reminderLabel.setText("Barcode does not exist.");
		}
		return (finalReturnArray);
	}
	
	//Private, internal use method to search audio-books by their lengths
	private ArrayList<Book> searchByLength(FileManipulator fileManipulator, JLabel reminderLabel, double length) throws FileNotFoundException {
		ArrayList<Book> rawArray = fileManipulator.readAllBooks();
		ArrayList<AudioBook> audiobookArray = new ArrayList<AudioBook>();
		
		//Read all audio-books into an array
		for (Book tempBook: rawArray) {
			if (tempBook instanceof AudioBook) {
				audiobookArray.add((AudioBook) tempBook);
			}
		}
		
		//Iterate through all audio-books
		ArrayList<Book> finalReturnArray = new ArrayList<Book>();
		boolean found = false;
		for (AudioBook tempBook: audiobookArray) {
			//If the current audio-book is longer than the length entered by the customer, add it to the return array of audio-books
			if (tempBook.getLength() > length) {
				found = true;
				finalReturnArray.add(tempBook);
			}
		}
		//If no books are found, set reminder label to prompt the customer that no audio-book longer than that length in stock
		if (found == false) {
			reminderLabel.setText("There are no books fitting the search criteria.");
		}
		return (finalReturnArray);
	}
	
	
	//Method to generate the page displaying the results to the customer's bar-code or length query
	public void queryResultPage(FileManipulator fileManipulator, ArrayList<Book> finalBookArray) {
		Customer currentUser = this;
		JPanel quarternaryPanel = new JPanel();
		
		JLabel reminderLabel = new JLabel();
		
		//Converting the results to be displayed from an ArrayList to an Object[][] for the JTable widget
		Object[][] finalReturnArray = new Object[finalBookArray.size()][];
		for (int x = 0; x < finalBookArray.size(); x++) {
			Object[] bookArray = new Object[10];
			String bookString = finalBookArray.get(x).toString();
			String[] tempBookArray = bookString.split(", ");
			for (int y = 0;y < 10; y++) {
				bookArray[y] = tempBookArray[y];
			finalReturnArray[x] = bookArray;
			}
		}
		
		Object[] bookHeading = {"Barcode", "Book Type", "Title", "Language", "Genre", "Release Date", "Quantity", "Retail Price", "Page No./ Page No./ Length", "Condition/ Format/ Format"};
		JTable bookDisplay = new JTable(finalReturnArray,bookHeading);
		
		//Any selected books from the JTable widget would be added to the customer's shopping basket 
		JButton addSelectedButton = new JButton("Add selected book to basket");
		addSelectedButton.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){  
	    		reminderLabel.setText("");
	    		currentUser.addToBasket(fileManipulator, finalBookArray, bookDisplay.getSelectedRow(), reminderLabel);  
	    	}
	    });
		
		JButton backButton = new JButton("Back");
		backButton.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){  
	    		try {
					currentUser.Interface(fileManipulator);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    		quarternaryPanel.setVisible(false);
	    	}
	    });
		
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){  
	    		UserInterface.mainFrame.setVisible(false);  
	    	}
	    });
		
		JScrollPane bookScroll = new JScrollPane(bookDisplay);  
		bookScroll.setPreferredSize(new Dimension(1200,300));
		
		quarternaryPanel.setBackground(Color.lightGray);
		quarternaryPanel.setBounds(0,0,1400,800);
		quarternaryPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints gbcPointer = new GridBagConstraints();
		gbcPointer.insets = new Insets(2,2,2,2);
		
		gbcPointer.gridx = 0;
		gbcPointer.gridy = 0;
		quarternaryPanel.add(reminderLabel, gbcPointer);
		
		gbcPointer.gridy = 1;
		quarternaryPanel.add(bookScroll, gbcPointer);
		
		gbcPointer.gridy = 2;
		quarternaryPanel.add(addSelectedButton, gbcPointer);
		
		gbcPointer.gridy = 3;
		quarternaryPanel.add(backButton, gbcPointer);
		
		gbcPointer.gridy = 4;
		quarternaryPanel.add(exitButton, gbcPointer);
		
	    UserInterface.mainFrame.add(quarternaryPanel); 
	}
	
	//Method to generate the page to ask for query data and type of query
	public void searchBookPage(FileManipulator fileManipulator) throws FileNotFoundException {
		Customer currentUser = this;
		
		JPanel tertiaryPanel = new JPanel();
		
		JLabel reminderLabel = new JLabel();
		
		JTextField barcodeText = new JTextField(10);
		JTextField lengthText = new JTextField(10);
		
		//Search option one is to search by bar-code
		JButton searchBarcodeButton = new JButton("Search by barcode");
		searchBarcodeButton.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){  
	    		reminderLabel.setText("");
	    		try {
	    			//Passes bar-code into the search method and collects the return query array to be displayed in a different page
	    			ArrayList<Book> finalBookArray = currentUser.searchByBarcode(fileManipulator, reminderLabel, Integer.valueOf(barcodeText.getText()));
	    			currentUser.queryResultPage(fileManipulator, finalBookArray);
				} catch (NumberFormatException | FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    		tertiaryPanel.setVisible(false);
	    	}
	    });
		
		//Search option two is to search by length, but this query is only application for audio-books
		JButton searchLengthButton = new JButton("Search by length");
		searchLengthButton.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){  
	    		reminderLabel.setText("");
	    		try {
	    			ArrayList<Book> finalBookArray = currentUser.searchByLength(fileManipulator, reminderLabel, Double.valueOf(lengthText.getText()));
	    			currentUser.queryResultPage(fileManipulator, finalBookArray);
				} catch (NumberFormatException | FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}  
	    		tertiaryPanel.setVisible(false);
	    	}
	    });
		
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
		
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){  
	    		UserInterface.mainFrame.setVisible(false);  
	    	}
	    });
		
		tertiaryPanel.setBackground(Color.lightGray);
		tertiaryPanel.setBounds(0,0,1400,800);
		tertiaryPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints gbcPointer = new GridBagConstraints();
		gbcPointer.insets = new Insets(2,2,2,2);
		
		gbcPointer.gridx = 0;
		gbcPointer.gridy = 0;
		gbcPointer.gridwidth = 2;
		tertiaryPanel.add(reminderLabel, gbcPointer);
		
		gbcPointer.gridwidth = 1;
		gbcPointer.gridy = 1;
		tertiaryPanel.add(barcodeText, gbcPointer);
		gbcPointer.gridx = 1;
		tertiaryPanel.add(lengthText, gbcPointer);
		
		gbcPointer.gridx = 0;
		gbcPointer.gridy = 2;
		tertiaryPanel.add(searchBarcodeButton, gbcPointer);
		gbcPointer.gridx = 1;
		tertiaryPanel.add(searchLengthButton, gbcPointer);
		
		gbcPointer.gridwidth = 2;
		gbcPointer.gridx = 0;
		gbcPointer.gridy = 3;
		tertiaryPanel.add(backButton, gbcPointer);
		gbcPointer.gridy = 4;
		tertiaryPanel.add(exitButton, gbcPointer);
		
	    UserInterface.mainFrame.add(tertiaryPanel); 
	}
	
	//Method to generate the main user interface page for all customers
	public void Interface(FileManipulator fileManipulator) throws FileNotFoundException {
		Customer currentUser = this;
		
		//Reads all books in stock into an ArrayList
		ArrayList<Book> rawArray = fileManipulator.readAllBooks();
		//This ArrayList of Book objects is to be passed into the addToBasket() method, therefore it has to be sorted here
		Collections.sort(rawArray, new Comparator<Book>() {
		    public int compare(Book a, Book b) {
		    	double x = Double.valueOf(a.getPrice());
		    	double y = Double.valueOf(b.getPrice());
		        return Double.compare(x, y);
		    }
		});
		
		//Reads all books in stock into an Object[][]
		ArrayList<Object[]> bookArray = fileManipulator.readAllPaperbackToString(rawArray);
		bookArray.addAll(fileManipulator.readAllEBookToString(rawArray));
		bookArray.addAll(fileManipulator.readAllAudioBookToString(rawArray));
		Object[][] finalBookArray = new Object[bookArray.size()][];
		for (int x = 0; x < bookArray.size(); x++) {
			finalBookArray[x] = bookArray.get(x);
		}
		
		//Object[][] has to be sorted again as the previous sort is undone by the readAllBookToString() methods
		Arrays.sort(finalBookArray, new Comparator<Object[]>() {
		    public int compare(Object[] a, Object[] b) {
		    	double x = Double.valueOf(a[7].toString());
		    	double y = Double.valueOf(b[7].toString());
		        return Double.compare(x, y);
		    }
		});
		
		Object[] bookHeadings = {"Barcode", "Book Type", "Title", "Language", "Genre", "Release Date", "Quantity", "Retail Price", "Page No./ Page No./ Length", "Condition/ Format/ Format"};
		
		JPanel secondaryPanel = new JPanel();
		
		JLabel reminderLabel = new JLabel();
		
		JTable bookDisplay = new JTable(finalBookArray, bookHeadings);
		
		JButton checkOutbutton = new JButton("See basket and check out");
		checkOutbutton.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){  
	    		reminderLabel.setText("");
	    		try {
					currentUser.checkOutPage(fileManipulator);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}  
	    		secondaryPanel.setVisible(false);
	    	}
	    });
		
		//Adds any selected book from the bookDisplay table widget into the customer's shopping basket
		JButton addSelectedButton = new JButton("Add selected book to basket");
		addSelectedButton.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){  
	    		reminderLabel.setText("");
	    		currentUser.addToBasket(fileManipulator, rawArray, bookDisplay.getSelectedRow(), reminderLabel);  
	    	}
	    });
		
		//Triggers a page where the customer can search for books by their bar-codes or audio-books by their lengths
		JButton searchButton = new JButton("Search by length or barcode");
		searchButton.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){  
	    		reminderLabel.setText("");
	    		try {
					currentUser.searchBookPage(fileManipulator);
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
		
		JScrollPane bookScroll = new JScrollPane(bookDisplay);  
		bookScroll.setPreferredSize(new Dimension(1200,300));
		
		secondaryPanel.setBackground(Color.lightGray);
		secondaryPanel.setBounds(0,0,1400,800);
		secondaryPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints gbcPointer = new GridBagConstraints();
		gbcPointer.insets = new Insets(2,2,2,2);
		
		gbcPointer.gridx = 0;
		gbcPointer.gridy = 0;
		gbcPointer.gridwidth = 2;
		secondaryPanel.add(reminderLabel, gbcPointer);
		
		gbcPointer.gridy = 1;
		secondaryPanel.add(bookScroll, gbcPointer);
		
		gbcPointer.gridy = 2;
		secondaryPanel.add(checkOutbutton, gbcPointer);
		gbcPointer.gridy = 3;
		secondaryPanel.add(addSelectedButton, gbcPointer);
		gbcPointer.gridy = 4;
		secondaryPanel.add(searchButton, gbcPointer);
		
		gbcPointer.gridy = 5;
		secondaryPanel.add(exitButton, gbcPointer);
		
	    UserInterface.mainFrame.add(secondaryPanel); 
	}
}
