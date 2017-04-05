import java.io.Serializable;

public class Book implements Serializable{
    private String isbn;
    private Author author;
    private String title;

    public Book (String isbn, Author author, String title){
	this.isbn = isbn;
	this.author = author;
	this.title = title;
    }

    public String getIsbn() {
	return isbn;
    }

    public Author getAuthor() {
	return author;
    }

    public String getTitle() {
	return title;
    }

    public String toString() {
	return "ISBN: "+isbn+"\nAuthor: "+author.getForename() + " "
	    +author.getSurename() + "\nTitle: " + title;
    }
}
