package bookshop;

//Abstract class, Book, will be a parent to classes for paperback, eBooks and audio-books
public abstract class Book {
	
	//Declaring common attributes of the above three book classes set to private
	private int barcode;
	private String title;
	private String language;
	private String genre;
	private String releaseDate;
	private int quantity;
	private double price;
	
	//Constructor for Book
	public Book (int barcode, String title, String language, String genre, String releaseDate, int quantity, double price) {
		this.barcode = barcode;
		this.title = title;
		this.language = language;
		this.genre = genre;
		this.releaseDate = releaseDate;
		this.quantity = quantity;
		this.price = price;
	}
	
	//Get methods declared for common private attributes
	public int getBarcode() {
		return(this.barcode);
	}
	
	public String getTitle() {
		return(this.title);
	}
	
	public String getLanguage() {
		return(this.language);
	}
	
	public String getGenre() {
		return(this.genre);
	}
	
	public String getReleaseDate() {
		return(this.releaseDate);
	}
	
	public int getQuantity() {
		return(this.quantity);
	}
	
	public double getPrice() {
		return(this.price);
	}
	
	
	//Increment and decrement methods for quantity attributes for all three book classes
	public void incrementQuantity() {
		this.quantity += 1;
	}
	
	public void decrementQuantity() {
		this.quantity -= 1;
	}
	
	//Common string conversion method for all three book classes
	public abstract String toString();
}
