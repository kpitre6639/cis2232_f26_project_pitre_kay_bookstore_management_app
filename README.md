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

A web application for a small independent bookstore dealing in both new and vintage titles. It records what the store has sold and answers questions for two kinds of user.

**A customer** browsing the store searches by genre or author, sees which titles are selling well, and checks what a given number of copies would cost. Older titles carry a premium, so what a customer pays depends partly on when the book was first released.

**The store owner** looks at which titles sold best on a given day, month or year, and at what income those sales produced, to judge how the store is performing and what to reorder.

Both views read the same single table.

---

## What a record represents

**One record is the sales of one title on one date.**

The same title appears more than once if it sold on more than one date. A record holds the title's details, the copies that sold on that date, the price they sold at, and the inventory remaining afterwards.

This is what makes the reporting possible — because each record is tied to a single date, records can be grouped by day, month or year to answer which title sold best in a period and what income the store earned.

Two consequences:

- **The application records sales; it does not process them.** There is no till. Sales are entered as records after the fact, and working out what a customer owes is a *quote* — calculated and displayed, never stored.
- **A title's current inventory is the `inventoryAmount` on its most recent record.** Earlier records hold inventory as it stood at the time, which gives a stock history for free.

---

## Fields captured

One database table, `Book`, holding the fields specified by the BA.

| Field | Type | Description |
|---|---|---|
| `id` | int | Primary key. Auto-incremented row identifier |
| `createdDateTime` | varchar(20) | When the row was saved (`yyyy-MM-dd hh:mm:ss`) |
| `bookName` | varchar(100) | Name of the book. Repeats across records for the same title |
| `author` | varchar(50) | Author of the book |
| `genre` | varchar(30) | Comedy, horror, romance, etc. |
| `price` | decimal(7,2) | Price of one copy at the time of this sale |
| `dateReleased` | varchar(10) | Year the title was first released (`yyyy`). Drives the premium |
| `amountSold` | int | Copies of this title sold on this date |
| `inventoryAmount` | int | Copies remaining in inventory after this sale |
| `dateSold` | varchar(10) | Date these copies sold (`yyyy-MM-dd`) |

---

## Calculations

Specified by the BA.

### Cost

Implemented and unit tested in Assignment 2. Not part of Assignment 1.

```
cost = quantity × price × vintage multiplier
```

| `dateReleased` | Multiplier |
|---|---|
| Before 1950 | 1.50 — a 50% premium |
| 1970 through 1989 | 1.20 — a 20% premium |
| All other years | 1.00 — no premium |

Used two ways from the same logic:

- **Against a record**, where quantity is the record's `amountSold`, giving the income those sales produced
- **Against a customer's request**, where quantity is the number of copies they want, giving a price quote

Applying the premium to the unit price or to the line total gives the same result.

> **Open with the BA:** the original topic document gave two different answers for the 50% band — pre-1970 in the description, before the 1950s in the calculation section. **Pre-1950** is used here, pending confirmation. The boundary years are constants in `BookBO` so the change is a single line if the answer differs.

### Income reports

Built later in the project, as reports over many records.

| Report | Rule |
|---|---|
| Average income per day | Total sales for the year ÷ 365 |
| Average income per month | Total sales for the year ÷ 12 |
| Average income per year | Total of all years ÷ number of distinct years on record |

Total sales for a year is the sum of `amountSold × price × multiplier` across every record whose `dateSold` falls in that year.

---

## Repository structure

```
Assignments/
  cis2232_a1_pitre_kay/          Assignment 1 - file I/O console application
  cis2232_a2_pitre_kay/          Assignment 2 - unit testing, TDD
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

## Assignment 2 — unit testing, test driven development

The cost calculation, built test-first and unit tested. No user interface — the assignment exercises the method through tests only.

| Class | Purpose |
|---|---|
| `ca.hccis.bookstore.entity.Book` | The entity. Fields only, no I/O |
| `ca.hccis.bookstore.bo.BookBO` | `calculate(Book)` and `calculate(Book, int)`, plus `getVintageMultiplier(String)` |
| `ca.hccis.bookstore.bo.BookBOTest` | 11 tests |

The calculation sits in a business object rather than on the entity because entity classes are generated from the database later in the project, which would discard a custom method.

**Three tests were written by hand following a test driven development approach** — each written and run against the `NO_COST` sentinel before any logic existed, then just enough logic added to pass, then all tests re-run after refactoring. Each of those three carries a Javadoc note saying so.

**Eight further tests were AI generated** from this README and the entity class, then reviewed and run. They cover the band boundaries at 1950, 1970 and 1989, the quote overload, zero copies sold, an unreadable release year and a null record.

Run: `mvn test` from `Assignments/cis2232_a2_pitre_kay`, or run `BookBOTest` in the IDE.

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

Java 25 · Spring Boot · Thymeleaf · MySQL · Maven · JUnit Jupiter 6.1.3 · Gson (Assignment 1)
