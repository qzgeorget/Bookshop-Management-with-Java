package bookshop;

import java.util.*;
import java.io.*;

//Class FileManipulator creates an object that has methods to interact with Stock.txt and UserAccounts.txt 
public class FileManipulator {
	
	//This class does not have an explicit constructor declared as an object wouldn't have any attributes, therefore it uses a default implicit constructor
	
	//Method to read all paperback books into an ArrayList of each book with their attributes in Object[] form
	//Takes parameter of an ArrayList of all book objects read from Stock.txt
	public ArrayList<Object[]> readAllPaperbackToString(ArrayList<Book> rawArray) throws FileNotFoundException {
		
		ArrayList<Object[]> paperbackArray = new ArrayList<Object[]>();
		//Iterate through ArrayList<Book>
		for (int x = 0; x < rawArray.size(); x++) {
			ArrayList<String> tempArray = new ArrayList<String>();
			
			//Discard all books except paperback books
			if (rawArray.get(x) instanceof PaperbackBook) {
				
				//Each book must be casted into their respective type in order to access their unique attributes, i.e. page number and condition for paperback
				PaperbackBook tempPaperback = (PaperbackBook) rawArray.get(x);
				
				//Append to the declared temporary ArrayList the attributes of each paperback book
				tempArray.add(Integer.toString(tempPaperback.getBarcode()));
				tempArray.add("Paperback");
				tempArray.add(tempPaperback.getTitle());
				tempArray.add(tempPaperback.getLanguage());
				tempArray.add(tempPaperback.getGenre());
				tempArray.add(tempPaperback.getReleaseDate());
				tempArray.add(Integer.toString(tempPaperback.getQuantity()));
				tempArray.add(Double.toString(tempPaperback.getPrice()));
				tempArray.add(Integer.toString(tempPaperback.getPageNumber()));
				tempArray.add(tempPaperback.getCondition());
				paperbackArray.add(tempArray.toArray());
			}
		}
		return (paperbackArray);
	}
	
	//Method to read all eBook into an ArrayList of each book with their attributes in Object[] form
	public ArrayList<Object[]> readAllEBookToString(ArrayList<Book> rawArray) throws FileNotFoundException {
		ArrayList<Object[]> ebookArray = new ArrayList<Object[]>();
		for (int x = 0; x < rawArray.size(); x++) {
			ArrayList<String> tempArray = new ArrayList<String>();
	
			if (rawArray.get(x) instanceof EBook) {
				EBook tempEBook = (EBook) rawArray.get(x);
				tempArray.add(Integer.toString(tempEBook.getBarcode()));
				tempArray.add("EBook");
				tempArray.add(tempEBook.getTitle());
				tempArray.add(tempEBook.getLanguage());
				tempArray.add(tempEBook.getGenre());
				tempArray.add(tempEBook.getReleaseDate());
				tempArray.add(Integer.toString(tempEBook.getQuantity()));
				tempArray.add(Double.toString(tempEBook.getPrice()));
				tempArray.add(Integer.toString(tempEBook.getPageNumber()));
				tempArray.add(tempEBook.getFormat());
				ebookArray.add(tempArray.toArray());
			}
		}
		return (ebookArray);
	}
	
	//Method to read all audio-book into an ArrayList of each book with their attributes in Object[] form
	public ArrayList<Object[]> readAllAudioBookToString(ArrayList<Book> rawArray) throws FileNotFoundException {
		ArrayList<Object[]> audiobookArray = new ArrayList<Object[]>();
		for (int x = 0; x < rawArray.size(); x++) {
			ArrayList<String> tempArray = new ArrayList<String>();
			
			if (rawArray.get(x) instanceof AudioBook) {
				AudioBook tempAudiobook = (AudioBook) rawArray.get(x);
				
				tempArray.add(Integer.toString(tempAudiobook.getBarcode()));
				tempArray.add("AudioBook");
				tempArray.add(tempAudiobook.getTitle());
				tempArray.add(tempAudiobook.getLanguage());
				tempArray.add(tempAudiobook.getGenre());
				tempArray.add(tempAudiobook.getReleaseDate());
				tempArray.add(Integer.toString(tempAudiobook.getQuantity()));
				tempArray.add(Double.toString(tempAudiobook.getPrice()));
				tempArray.add(Double.toString(tempAudiobook.getLength()));
				tempArray.add(tempAudiobook.getFormat());
				audiobookArray.add(tempArray.toArray());
			}
		}
		return(audiobookArray);
	}
	
	//Method used when a customer checks out their shopping basket and the payment is going through
	//Method updates the customer's balance attribute to the new value after they've paid for the books in their shopping basket 
	//Method takes currentUser from the payment page as a parameter
	public void paymentUsersUpdate(FileManipulator fileManipulator, Customer currentUser, double totalPrice) throws FileNotFoundException {
		ArrayList<User> rawArray = fileManipulator.readAllUsers();
		
		String usersToString = "";
		//Iterate through every user 
		for (User tempUser: rawArray) {
			
			//Until user ID matches with currentUser
			if (currentUser.getId() == tempUser.getId()) {
				
				//Declare tempUser to be Customer to access decrementBalance()
				Customer tempCustomer = (Customer) tempUser;
				tempCustomer.decrementBalance(totalPrice);
				currentUser.decrementBalance(totalPrice);
			}
			
			//Put User objects back into text form compatible with UserAccounts.txt
			usersToString += tempUser.toString();
			usersToString += "\n";
		}
		
		//Overwrite text in UserAccounts.txt with the new records of user accounts
		try {
			FileWriter userWriter = new FileWriter("UserAccounts.txt", false);
			userWriter.write(usersToString);
			userWriter.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	//Method used when a customer checks out their shopping basket and the payment is going through
	//Method decrements the book's quantity attribute by one after they've paid for the books in their shopping basket 
	//Method takes currentUser from the payment page as a parameter
	public void paymentStockUpdate(FileManipulator fileManipulator, Customer currentUser) throws FileNotFoundException {
		ArrayList<Book> rawArray = fileManipulator.readAllBooks();
		
		//Iterate through every book in the customer's shopping basket
		for (Book purchaseItem: currentUser.getShoppingBasket()) {
			
			//Iterate through every book in stock
			for (Book stockItem: rawArray) {
				
				//Until bar-codes of the two books match
				if (purchaseItem.getBarcode() == stockItem.getBarcode()) {
					
					//Decrement the book's quantity by one in the records 
					stockItem.decrementQuantity();
				}
			}
		}
		
		//Put Book objects back into text form compatible with Stock.txt
		String stockToString = "";
		for (Book item: rawArray) {
			stockToString += item.toString();
			stockToString += "\n";
		}
		
		//Overwrite text in Stock.txt with the new records of books
		try {
			FileWriter stockWriter = new FileWriter("Stock.txt", false);
			stockWriter.write(stockToString);
			stockWriter.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	//Method used to add a new book into Stock.txt by Admin users
	public void addToStock(String inputString) {
		
		//Append to Stock.txt a new record with all the information in string converted form
		try {
			FileWriter StockAppender = new FileWriter("Stock.txt",true);
			StockAppender.write(inputString);
			StockAppender.write("\n");
			StockAppender.close();
			
		} catch (IOException ex) {
			ex.printStackTrace(); 
		}
	}
	
	//Method to read and return an ArrayList of all User objects from UserAccounts.txt
	public ArrayList<User> readAllUsers() throws FileNotFoundException {
		
		//Read from UserAccounts.txt
		File userManifest = new File("UserAccounts.txt");
		Scanner userReader = new Scanner(userManifest);
		
		ArrayList<User> tempArray = new ArrayList<User>();
		
		//As long as UserAccounts.txt has a next line
		while (userReader.hasNextLine()) {
			
			//Reads the line
			String userString = userReader.nextLine();
			
			//Splits the line up into separate values by ", "
			String[] userArray = userString.split(", ");
			
			//Assign every attribute with a value
			int id = Integer.valueOf(userArray[0]);
			String username = userArray[1];
			String surname = userArray[2];
			int houseNumber = Integer.valueOf(userArray[3]);
			String postcode = userArray[4];
			String city = userArray[5];
			String role = userArray[7];
			
			//Use class constructors to construct either Admin objects or Customer objects
			if (role.compareTo("admin") == 0) {
				Admin tempUser = new Admin(id, username, surname, houseNumber, postcode, city);
				tempArray.add(tempUser);
				
			} else if (role.compareTo("customer") == 0){
				
				//Customer objects have an extra attribute that Admin objects do not, which is assigned here
				double balance = Double.valueOf(userArray[6]);
				
				Customer tempUser = new Customer(id, username, surname, houseNumber, postcode, city, balance);
				tempArray.add(tempUser);
				
			}
			
		}
		userReader.close();
		return tempArray;
	}
	
	//Method to read and return an ArrayList of all Book objects from Stock.txt
	public ArrayList<Book> readAllBooks() throws FileNotFoundException {
		
		//Read from Stock.txt
		File bookList = new File("Stock.txt");
		Scanner bookReader = new Scanner(bookList);
		
		ArrayList<Book> tempArray = new ArrayList<Book>();
		
		//As long as Stock.txt has a next line
		while (bookReader.hasNextLine()) {
			
			//Read the line
			String bookString = bookReader.nextLine();
			String[] bookArray = bookString.split(", ");
			
			int barcode = Integer.valueOf(bookArray[0]);
			String type = bookArray[1];
			String title = bookArray[2];
			String language = bookArray[3];
			String genre = bookArray[4];
			String releaseDate = bookArray[5];
			int quantity = Integer.valueOf(bookArray[6]);
			double price = Double.valueOf(bookArray[7]);
			
			//Sort the books into their types and use their respective constructors to construct PaperbackBook, EBook,  and AudioBook objects
			if (type.compareTo("paperback") == 0) {
				int pageNumber = Integer.valueOf(bookArray[8]);
				String condition = bookArray[9];
				
				PaperbackBook tempBook = new PaperbackBook(barcode, title, language, genre, releaseDate, quantity, price, pageNumber, condition);
				tempArray.add(tempBook);
				
			} else if (type.compareTo("audiobook") == 0){
				double length = Double.valueOf(bookArray[8]);
				String format = bookArray[9];
				
				AudioBook tempBook = new AudioBook(barcode, title, language, genre, releaseDate, quantity, price, length, format);
				tempArray.add(tempBook);
				
			} else if (type.compareTo("ebook") == 0){
				int pageNumber = Integer.valueOf(bookArray[8]);
				String format = bookArray[9];
				
				EBook tempBook = new EBook(barcode, title, language, genre, releaseDate, quantity, price, pageNumber, format);
				tempArray.add(tempBook);
			
			}
		}
		bookReader.close();
		return tempArray;
	}
}
