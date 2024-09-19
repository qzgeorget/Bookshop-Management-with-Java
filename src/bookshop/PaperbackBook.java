package bookshop;

//Paperback class inherits attributes and methods from abstract class, Book
public class PaperbackBook extends Book{
	
	//Unique attributes for paperback class
	private int pageNumber;
	private String condition;
	
	//Constructor for paperback inheriting from Book and two new attributes in addition
	public PaperbackBook (int barcode, String title, String language, String genre, String releaseDate, int quantity, double price, int pageNumber, String condition) {
		super(barcode, title, language, genre, releaseDate, quantity, price);
		this.pageNumber = pageNumber;
		this.condition = condition;
	}
	
	//Get methods for two new attributes
	public int getPageNumber() {
		return (this.pageNumber);
	}
	
	public String getCondition() {
		return (this.condition);
	}

	//Unique string conversion method for paperback objects
	public String toString() {
		return(this.getBarcode() + ", paperback, " + this.getTitle() + ", " + this.getLanguage() + ", " + this.getGenre() + ", " + this.getReleaseDate() + ", " + this.getQuantity() + ", " + this.getPrice() + ", " + this.getPageNumber() + ", " + this.getCondition());
	}
}
