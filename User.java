package bookshop;

//Imported for throwback declaractions
import java.io.FileNotFoundException;

//Abstract class, User, will be a parent to classes for administrators and customers
public abstract class User {
	
	//Declaring common attributes of the above user classes set to private
	private int id;
	private String username;
	private String surname;
	private int houseNumber;
	private String postcode;
	private String city;
	private String address;
	
	//Constructor for User
	public User(int id, String username, String surname, int houseNumber, String postcode, String city) {
		this.id = id;
		this.username = username;
		this.surname = surname;
		this.houseNumber = houseNumber;
		this.postcode = postcode;
		this.city = city;
		this.address = houseNumber + ", " + postcode + ", " + city;
	}
	
	//Get methods declared for common private attributes
	public int getId() {
		return (this.id);
	}
	
	public String getUsername() {
		return (this.username);
	}
	
	public String getSurname() {
		return (this.surname);
	}
	
	public int getHouseNumber() {
		return (this.houseNumber);
	}
	
	public String getPostcode() {
		return (this.postcode);
	}
	
	public String getCity() {
		return (this.city);
	}
	
	public String getAddress() {
		return (this.address);
	}
	
	//Common string conversion method for all three book classes
	public abstract String toString();
	
	//Common user interface manipulation method for functionalities of the different user classes
	public abstract void Interface(FileManipulator fileManipulator) throws FileNotFoundException;
}
