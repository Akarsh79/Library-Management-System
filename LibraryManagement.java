package projects.library;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Book{

    private int id;
    private String bookName;
    private String author;
    private boolean isIssued;
    private LocalDateTime issueDateTime;
    private LocalDateTime returnDateTime;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Book(int id, String bookName, String author) {
        this.id = id;
        this.bookName = bookName;
        this.author = author;
        this.isIssued = false;
        this.issueDateTime = null;
        this.returnDateTime = null;
    }

    public int getId() {
        return id;
    }

    public boolean isIssued() {
        return isIssued;
    }

    public void issue() {
        this.isIssued = true;
        this.issueDateTime = LocalDateTime.now();
        this.returnDateTime = null;
    }

    public void returnBook() {
        this.isIssued = false;
        this.returnDateTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        String status = isIssued ? "Issued" : "Available";
        String issueTime = (issueDateTime != null) ? formatter.format(issueDateTime) : "N/A";
        String returnTime = (returnDateTime != null) ? formatter.format(returnDateTime) : "N/A";

        return id + " - " + bookName + " by " + author +
                " [" + status + "] | Issued: " + issueTime + " | Returned: " + returnTime;
    }

    public String toCSV() {
        return id + "," + escape(bookName) + "," + escape(author) + "," + isIssued + "," +
                (issueDateTime != null ? formatter.format(issueDateTime) : "") + "," +
                (returnDateTime != null ? formatter.format(returnDateTime) : "");
    }

    public static Book fromCSV(String csvLine) {
        try {
            String[] parts = csvLine.split(",", -1);
            int id = Integer.parseInt(parts[0]);
            String bookName = unescape(parts[1]);
            String author = unescape(parts[2]);
            boolean isIssued = Boolean.parseBoolean(parts[3]);

            LocalDateTime issueDate = parts[4].isEmpty() ? null : LocalDateTime.parse(parts[4], formatter);
            LocalDateTime returnDate = parts[5].isEmpty() ? null : LocalDateTime.parse(parts[5], formatter);

            Book book = new Book(id, bookName, author);
            if (isIssued) {
                book.issue();
                book.issueDateTime = issueDate;
            } else {
                book.issueDateTime = issueDate;
                book.returnDateTime = returnDate;
            }
            return book;
        } catch (Exception e) {
            return null;
        }
    }

    private static String escape(String input) {
        return input.replace(",", "\\,");
    }

    private static String unescape(String input) {
        return input.replace("\\,", ",");
    }
}

class Library {

    private List<Book> books = new ArrayList<>();
    private static final String FILE_NAME = "books.csv";

    public void addBook(Book book) {
        books.add(book);
        saveToFile();
    }

    public void displayBooks() {
        if (books.isEmpty()) {
            System.out.println("Library is Empty.");
        } else {
            for (Book b : books) {
                System.out.println(b);
            }
        }
    }

    public Book findBookById(int id) {
        for (Book b : books) {
            if (b.getId() == id) {
                return b;
            }
        }
        return null;
    }

    public void issueBook(int id) {
        Book book = findBookById(id);
        if (book != null && !book.isIssued()) {
            book.issue();
            System.out.println("Book Issued Successfully.");
            saveToFile();
        } else if (book == null) {
            System.out.println("Book Not Found.");
        } else {
            System.out.println("Book is Already Issued.");
        }
    }

    public void returnBook(int id) {
        Book book = findBookById(id);
        if (book != null && book.isIssued()) {
            book.returnBook();
            System.out.println("Book Returned Successfully.");
            saveToFile();
        } else if (book == null) {
            System.out.println("Book not Found.");
        } else {
            System.out.println("This Book wasn't Issued.");
        }
    }

    public void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Book b : books) {
                writer.println(b.toCSV());
            }
        } catch (IOException e) {
            System.out.println("Error Saving to File.");
        }
    }

    public void loadFromFile() {
        books.clear();
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Book book = Book.fromCSV(line);
                if (book != null) {
                    books.add(book);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error Loading from File.");
        }
    }
}

public class LibraryManagement {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library();
        library.loadFromFile();
        int choice;

        do {
            System.out.println("\n===== Library Management System =====");
            System.out.println("1. Add Book");
            System.out.println("2. View All Books");
            System.out.println("3. Issue a Book");
            System.out.println("4. Return a Book");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Book ID: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter Book Name: ");
                    String bookName = scanner.nextLine();
                    System.out.print("Enter Author Name: ");
                    String author = scanner.nextLine();
                    Book book = new Book(id, bookName, author);
                    library.addBook(book);
                    System.out.println("Book added.");
                }
                case 2 -> library.displayBooks();
                case 3 -> {
                    System.out.print("Enter Book ID to Issue: ");
                    int issueId = scanner.nextInt();
                    library.issueBook(issueId);
                }
                case 4 -> {
                    System.out.print("Enter Book ID to Return: ");
                    int returnId = scanner.nextInt();
                    library.returnBook(returnId);
                }
                case 5 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid option!");
            }
        } while (choice != 5);

        scanner.close();
    }
}

