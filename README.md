# Java Library Management System (CLI)

A simple, file-based **Library Management System** built using **Java**. This console application allows users to **add**, **view**, **issue**, and **return books**. All data is persisted using a CSV file (`books.csv`), enabling data storage between sessions.

---

## 📌 Features

- 🔸 Add new books with unique ID, name, and author
- 🔸 View all books with real-time issue/return status
- 🔸 Issue books (records date and time)
- 🔸 Return books (records return date and time)
- 🔸 Save and load data using a CSV file
- 🔸 Modular code using OOP principles

---
## 🧱 Project Structure

projects/
└── library/
├── Book.java # Handles book properties and CSV conversion
├── Library.java # Manages book collection and file operations
└── LibraryManagement.java # Main class for CLI interface

## Sample CLI Session:
===== Library Management System =====
1. Add Book
2. View All Books
3. Issue a Book
4. Return a Book
5. Exit
Choose an option: 1
Enter Book ID: 101
Enter Book Name: Clean Code
Enter Author Name: Robert C. Martin
Book added.

===== Library Management System =====
Choose an option: 2
101 - Clean Code by Robert C. Martin [Available] | Issued: N/A | Returned: N/A


## Key Concepts Used:

.Java OOP (Object-Oriented Programming)
.Java Collections (ArrayList)
.File I/O using Scanner, FileWriter, and PrintWriter
.Date & Time handling with LocalDateTime and DateTimeFormatter
.CSV Parsing with support for escaping commas

## CSV Data Format:

Each book is stored in books.csv with the following format:
id,bookName,author,isIssued,issueDateTime,returnDateTime
Example:
101,Clean Code,Robert C. Martin,false,2025-09-07 14:35:21,



