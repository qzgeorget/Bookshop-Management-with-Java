package bookshop;

//Audio-book class inherits attributes and methods from abstract class, Book
public class AudioBook extends Book{
	
	//Unique attributes for audio-book class
	private double length;
	private String format;
	
	//Constructor for audio-book inheriting from Book and two new attributes in addition
	public AudioBook (int barcode, String title, String language, String genre, String releaseDate, int quantity, double price, double length, String format) {
		super(barcode, title, language, genre, releaseDate, quantity, price);
		this.length = length;
		this.format = format;
	}
	
	//Get methods for two new attributes
	public double getLength() {
		return (this.length);
	}
	
	public String getFormat() {
		return (this.format);
	}
	
	//Unique string conversion method for audio-book objects
	public String toString() {
		return(this.getBarcode() + ", audiobook, " + this.getTitle() + ", " + this.getLanguage() + ", " + this.getGenre() + ", " + this.getReleaseDate() + ", " + this.getQuantity() + ", " + this.getPrice() + ", " + this.getLength() + ", " + this.getFormat());
	}
}