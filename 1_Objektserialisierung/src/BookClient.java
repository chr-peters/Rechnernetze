import java.util.Scanner;

import java.io.File;

public class BookClient {

    // TODO: declare book container
    static Scanner scanner = new Scanner(System.in);

    public static File bookFile = new File("../data/server.txt");

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
	// TODO
    }

    public static void showBooks() {
	// TODO
    }

    public static void addBook() {
	// TODO
    }

    public static void deleteBook() {
	// TODO
    }

    public static void saveBooks(File server) {
	// TODO
    }

}
