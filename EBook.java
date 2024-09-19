package bookshop;

//EBook class inherits attributes and methods from abstract class, Book
public class EBook extends Book{
	
	//Unique attributes for eBook class
	private int pageNumber;
	private String format;
	
	//Constructor for eBook inheriting from Book and two new attributes in addition
	public EBook (int barcode, String title, String language, String genre, String releaseDate, int quantity, double price, int pageNumber, String format) {
		super(barcode, title, language, genre, releaseDate, quantity, price);
		this.pageNumber = pageNumber;
		this.format = format;
	}
	
	//Get methods for two new attributes
	public int getPageNumber() {
		return (this.pageNumber);
	}
	
	public String getFormat() {
		return (this.format);
	}
	
	//Unique string conversion method for eBook objects
	public String toString() {
		return(this.getBarcode() + ", ebook, " + this.getTitle() + ", " + this.getLanguage() + ", " + this.getGenre() + ", " + this.getReleaseDate() + ", " + this.getQuantity() + ", " + this.getPrice() + ", " + this.getPageNumber() + ", " + this.getFormat());
	}
}