import java.util.Scanner;

import java.io.File;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.LinkedList;
import java.util.List;

public class BookClient {

    static Scanner scanner = new Scanner(System.in);

    public static File bookFile = new File("data/server.binary");

    public static List<Book> content = new LinkedList<Book>();

    public static void main(String[] args) {
	int input = -1;
	while (input != 0) {
	    System.out.println("Choose");
	    System.out.println(" (0) Quit program");
	    System.out.println(" (1) Load books from file");
	    System.out.println(" (2) Show Books");
	    System.out.println(" (3) Add Book");
	    System.out.println(" (4) Delete Book");
	    System.out.println(" (5) Save books in file");
	    input = scanner.nextInt();
	    switch (input) {
	    case 1:
		loadBooks(bookFile);
		break;
	    case 2:
		showBooks();
		break;
	    case 3:
		addBook();
		break;
	    case 4:
		deleteBook();
		break;
	    case 5:
		saveBooks(bookFile);
		break;
	    }
	}
    }

    public static void loadBooks(File server) {
	try(ObjectInputStream oos = new ObjectInputStream(new FileInputStream(server))){
		content = (LinkedList<Book>)oos.readObject();
	    } catch (IOException | ClassNotFoundException e) {
	    e.printStackTrace();
	}
    }

    public static void showBooks() {
	for (Book b: content) {
	    System.out.println(b);
	}
    }

    public static void addBook() {
	System.out.println("Please enter the ISBN: ");
	String isbn = scanner.nextLine();
	System.out.println("Please enter the forename of the author: ");
	String author_forename = scanner.nextLine();
	System.out.println("Please enter the surename of the author: ");
	String author_surename = scanner.nextLine();
	System.out.println("Please enter the title of the book: ");
	String title = scanner.nextLine();
	content.add(new Book(isbn, new Author(author_forename, author_surename),
			     title));
    }

    public static void deleteBook() {
	System.out.println("Please enter the ISBN: ");
	String isbn = scanner.nextLine();

	//search for the book
	for(int i=0; i<content.size(); i++) {
	    if(content.get(i).getIsbn().equals(isbn)) {
		content.remove(i);
	    }
	}
    }

    public static void saveBooks(File server) {
	try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(server))){
		oos.writeObject(content);
		oos.flush();
		oos.close();

	    } catch (IOException e) {

	}
    }

}
