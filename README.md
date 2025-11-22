# Attendance Management System

A simple desktop application for managing student attendance records built with Java Swing and SQLite.

---

## About

This is a desktop application that helps teachers and administrators track student attendance. It runs on your computer without needing internet or a server. All data is stored in a local SQLite database.

---

## Features

- Add new attendance records
- Update existing records
- Delete records
- Search by student ID
- View all attendance records in a table
- Automatic date recording
- Three status options: Present, Absent, Late

---

## What You Need

- Java JDK 8 or higher
- SQLite JDBC Driver (sqlite-jdbc-3.43.0.0.jar)
- Windows, macOS, or Linux

---

## How to Install

### Step 1: Download SQLite Driver

Download from: https://github.com/xerial/sqlite-jdbc/releases

Get the file: `sqlite-jdbc-3.43.0.0.jar`

### Step 2: Put Files Together

Create a folder and put these files in it:
- `AttendanceManagementSystem.java` (the code)
- `sqlite-jdbc-3.43.0.0.jar` (the driver)

### Step 3: Compile

**On Windows:**
```bash
javac -cp ".;sqlite-jdbc-3.43.0.0.jar" AttendanceManagementSystem.java
```

**On Mac/Linux:**
```bash
javac -cp ".:sqlite-jdbc-3.43.0.0.jar" AttendanceManagementSystem.java
```

### Step 4: Run

**On Windows:**
```bash
java -cp ".;sqlite-jdbc-3.43.0.0.jar" AttendanceManagementSystem
```

**On Mac/Linux:**
```bash
java -cp ".:sqlite-jdbc-3.43.0.0.jar" AttendanceManagementSystem
```

---

## How to Use

### Adding a Student

1. Type the Student ID (example: ST001)
2. Type the Student Name (example: John Doe)
3. Choose Present, Absent, or Late from dropdown
4. Click "Add Attendance" button
5. Done! The student appears in the table below

### Updating a Record

1. Click on any row in the table
2. The information fills the form automatically
3. Change what you want
4. Click "Update" button

### Deleting a Record

1. Click on the row you want to delete
2. Click "Delete" button
3. Confirm when asked

### Searching

1. Type Student ID in the search box
2. Click "Search" button
3. Click "Refresh All" to see everything again

---

## Database

The application creates a file called `attendance.db` in the same folder. This file contains all your attendance records.

**Table Structure:**

| Column | What it stores |
|--------|----------------|
| id | Unique number for each record |
| student_id | Student ID number |
| student_name | Student's full name |
| date | Date of attendance |
| status | Present, Absent, or Late |

---

## Common Problems

**Problem: "No suitable driver found"**
- Make sure the sqlite-jdbc JAR file is in the same folder
- Check your compile and run commands are correct

**Problem: Can't see the buttons**
- Make your window bigger
- Your screen might be too small (need at least 1024x768)

**Problem: Won't start**
- Make sure Java is installed: type `java -version` in command prompt
- Check all files are in the same folder

---

## Technologies Used

- **Java** - Programming language
- **Swing** - For the user interface
- **SQLite** - Database to store data
- **JDBC** - To connect Java with database

---

## What You'll Learn

By studying this project, you'll learn:

- How to create desktop applications with Java Swing
- How to connect Java programs to databases
- How to add, update, delete, and search data
- How to create buttons, text fields, and tables
- How to handle button clicks and user actions

---

## Future Ideas

Things that could be added later:

- Login system
- Export data to Excel
- Print reports
- Calculate attendance percentage
- Add photos of students
- Email notifications

---

## Files in Project

```
AttendanceSystem/
├── AttendanceManagementSystem.java    (main code)
├── sqlite-jdbc-3.43.0.0.jar          (database driver)
└── attendance.db                      (database - created automatically)
```

---

## License

This project is for educational purposes. Feel free to use it for learning.

---

## Author

Made by: [Your Name]  
Contact: [Your Email]

---

## Need Help?

If something doesn't work:

1. Read the "Common Problems" section above
2. Make sure Java is installed properly
3. Check that all files are in the right place
4. Make sure you're using the correct command for your operating system (Windows or Mac/Linux)

---

**Last Updated:** November 2024