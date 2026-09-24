# Book Store Management App

CIS-2232 Advanced Object Oriented Programming · Fall 2026 · Holland College

Base colour: **Burgundy** (`#800020`)

---

## Development team

| Role | Name | Responsibility |
|---|---|---|
| BA / Business Client | Joseph L | Defines the topic, the fields captured, and the report requirements. Approves and tests the finished application. |
| Developer | Kay Pitre | Builds the application. Owns this repository and all code commits. |
| Project Manager / QA | Jose Villanueva | Tracks milestones, verifies setup and basic functionality, reports when progress stalls. |

---

## Description

This application checks various data points around a book management system. The user is able to find the best-selling book by genre, author, day, month and year, find the average income in a day, month or year, and find the cost of a book based on a yearly premium.

For example, a book released in the 1970s or 1980s is worth 20% more, while a book released before 1970 is worth 50% more.

---

## Fields captured

One database table, `Book`, holding the fields specified by the BA.

| Field | Type | Description |
|---|---|---|
| `id` | int | Primary key. Auto-incremented row identifier |
| `createdDateTime` | varchar(20) | When the row was saved (`yyyy-MM-dd hh:mm:ss`) |
| `bookName` | varchar(100) | Name of the book |
| `author` | varchar(50) | Author of the book |
| `genre` | varchar(30) | Comedy, horror, romance, etc. |
| `price` | decimal(7,2) | Price of the book |
| `dateReleased` | varchar(10) | Year the book was released (`yyyy`) |
| `amountSold` | int | Amount of books sold |
| `inventoryAmount` | int | Amount of books left in inventory |
| `dateSold` | varchar(10) | Date the book was sold (`yyyy-MM-dd`) |

---

## Calculations

Specified by the BA. Implemented test-first in Assignment 2, not in Assignment 1.

| Calculation | Rule |
|---|---|
| Average income per day | Total sales for the year ÷ 365 |
| Average income per month | Total sales for the year ÷ 12 |
| Average income per year | Total of all years ÷ number of years |
| Cost of a book | `quantity × price`, plus 20% for books released in the 1970s–1980s, plus 50% for any book released before the 1950s |

---

## Repository structure

```
Assignments/
  cis2232_a1_pitre_kay/          Assignment 1 - file I/O console application
Project/
  Documentation/                 Project topic document from the BA
  bookstore_management_app/      Spring Boot MVC web application
```

---

## Assignment 1 — file I/O console application

Console application that adds and views books, storing them as JSON.

- Menu: `A) Add`, `V) View`, `X) eXit`
- Data file: `c:\cis2232\data_pitre_kay.json`
- The `c:\cis2232` folder is created by the program if it does not exist
- One JSON object per line, so entries survive a restart

Run `ca.hccis.bookstore.Controller`.

---

## Web application

Spring Boot MVC with Thymeleaf, running on MySQL.

| Setting | Value |
|---|---|
| Application name | `bookstore_management_app` |
| Port | `8080` |
| Database | `cis2232_bookstore` |

### Running it

1. Start **Apache** and **MySQL** in XAMPP
2. Run `Project/bookstore_management_app/src/main/resources/db/mysql/createDatabase.sql`
   — it drops and rebuilds the database, so it is safe to re-run at any time
3. Start the application and open **http://localhost:8080**

---

## Technology

Java 21 · Spring Boot · Thymeleaf · MySQL · Maven · Gson (Assignment 1)
