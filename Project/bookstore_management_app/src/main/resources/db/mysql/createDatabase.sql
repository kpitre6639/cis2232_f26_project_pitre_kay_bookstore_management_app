# Book Store Management App
# CIS2232 F26 - Developer: Kay Pitre - BA: Joseph L
# Fields as specified by the BA in the project topic document.
# This script drops and rebuilds the database so it can be re-run at any time.

# For hccis.ca version of the database
# DROP DATABASE IF EXISTS kpitre_bookstore_w26;
# CREATE DATABASE kpitre_bookstore_w26;
# use kpitre_bookstore_w26;

#For localhost
DROP DATABASE IF EXISTS cis2232_bookstore;
CREATE DATABASE cis2232_bookstore;
use cis2232_bookstore;

-- ------------------------------------------------------------------------------
-- Holds the books in the store.  One row per book as specified by the BA.
-- ------------------------------------------------------------------------------

CREATE TABLE Book
(
    id              int(5),
    createdDateTime varchar(20)   NOT NULL COMMENT 'yyyy-MM-dd hh:mm:ss',
    bookName        varchar(100)  NOT NULL COMMENT 'Name of the book',
    author          varchar(50)   NOT NULL COMMENT 'Author of the book',
    genre           varchar(30)   NOT NULL COMMENT 'Comedy, horror, romance, etc',
    price           decimal(7, 2) NOT NULL COMMENT 'Price of the book',
    dateReleased    varchar(10)   NOT NULL COMMENT 'Year the book was released (yyyy)',
    amountSold      int(5) COMMENT 'Amount of books sold',
    inventoryAmount int(5) COMMENT 'Amount of books left in inventory',
    dateSold        varchar(10) COMMENT 'Date the book was sold (yyyy-MM-dd)'
) COMMENT 'This table holds the books carried by the book store';

ALTER TABLE Book
    ADD PRIMARY KEY (id);
ALTER TABLE Book
    MODIFY id int(5) NOT NULL AUTO_INCREMENT COMMENT 'This is the primary key',
    AUTO_INCREMENT = 1;

INSERT INTO Book (id, createdDateTime, bookName, author, genre, price, dateReleased, amountSold, inventoryAmount,
                  dateSold)
VALUES (1, '2026-09-23 19:05:11', 'Dune', 'Frank Herbert', 'Science Fiction', 12.99, '1965', 340, 12, '2026-09-23'),
       (2, '2026-09-23 19:06:02', 'The Shining', 'Stephen King', 'Horror', 9.50, '1977', 512, 7, '2026-09-22'),
       (3, '2026-09-23 19:07:44', 'Pride and Prejudice', 'Jane Austen', 'Romance', 7.25, '1813', 288, 22, '2026-09-20'),
       (4, '2026-09-23 19:09:13', 'Good Omens', 'Terry Pratchett', 'Comedy', 14.00, '1990', 176, 31, '2026-09-19'),
       (5, '2026-09-23 19:11:35', 'The Hound of the Baskervilles', 'Arthur Conan Doyle', 'Mystery', 6.75, '1902', 95,
        18, '2026-09-18');


-- ------------------------------------------------------------------------------
-- Code tables.  These drive the dynamic drop down lists used in the application.
-- ------------------------------------------------------------------------------

CREATE TABLE CodeType
(
    codeTypeId         int(3) COMMENT 'This is the primary key for code types',
    englishDescription varchar(100) NOT NULL COMMENT 'English description',
    frenchDescription  varchar(100) DEFAULT NULL COMMENT 'French description',
    createdDateTime    datetime     DEFAULT NULL,
    createdUserId      varchar(20)  DEFAULT NULL,
    updatedDateTime    datetime     DEFAULT NULL,
    updatedUserId      varchar(20)  DEFAULT NULL
) COMMENT 'This table holds the code types that are available for the application';

ALTER TABLE CodeType
    ADD PRIMARY KEY (codeTypeId);

INSERT INTO CodeType (codeTypeId, englishDescription, frenchDescription, createdDateTime, createdUserId,
                      updatedDateTime, updatedUserId)
VALUES (1, 'User Types', 'Types d''utilisateur', sysdate(), 'admin', CURRENT_TIMESTAMP, 'admin');
INSERT INTO CodeType (codeTypeId, englishDescription, frenchDescription, createdDateTime, createdUserId,
                      updatedDateTime, updatedUserId)
VALUES (2, 'Genres', 'Genres', sysdate(), 'admin', CURRENT_TIMESTAMP, 'admin');


CREATE TABLE CodeValue
(
    codeTypeId              int(3)       NOT NULL COMMENT 'see CodeType table',
    codeValueSequence       int(3)       NOT NULL,
    englishDescription      varchar(100) NOT NULL COMMENT 'English description',
    englishDescriptionShort varchar(20)  NOT NULL COMMENT 'English abbreviation for description',
    frenchDescription       varchar(100) DEFAULT NULL COMMENT 'French description',
    frenchDescriptionShort  varchar(20)  DEFAULT NULL COMMENT 'French abbreviation for description',
    sortOrder               int(3)       DEFAULT NULL COMMENT 'Sort order if applicable',
    createdDateTime         datetime     DEFAULT NULL,
    createdUserId           varchar(20)  DEFAULT NULL,
    updatedDateTime         datetime     DEFAULT NULL,
    updatedUserId           varchar(20)  DEFAULT NULL
) COMMENT ='This will hold code values for the application.';

ALTER TABLE CodeValue
    ADD PRIMARY KEY (codeTypeId, codeValueSequence);

INSERT INTO CodeValue (codeTypeId, codeValueSequence, englishDescription, englishDescriptionShort, frenchDescription,
                       frenchDescriptionShort, sortOrder, createdDateTime, createdUserId, updatedDateTime,
                       updatedUserId)
VALUES (1, 1, 'General', 'General', 'General', 'General', 1, sysdate(), 'admin', CURRENT_TIMESTAMP, 'admin'),
       (1, 2, 'Admin', 'Admin', 'Admin', 'Admin', 2, sysdate(), 'admin', CURRENT_TIMESTAMP, 'admin'),
       (2, 1, 'Comedy', 'Comedy', 'Comedie', 'Comedie', 1, sysdate(), 'admin', CURRENT_TIMESTAMP, 'admin'),
       (2, 2, 'Horror', 'Horror', 'Horreur', 'Horreur', 2, sysdate(), 'admin', CURRENT_TIMESTAMP, 'admin'),
       (2, 3, 'Romance', 'Romance', 'Romance', 'Romance', 3, sysdate(), 'admin', CURRENT_TIMESTAMP, 'admin'),
       (2, 4, 'Science Fiction', 'Sci-Fi', 'Science-fiction', 'Sci-Fi', 4, sysdate(), 'admin', CURRENT_TIMESTAMP,
        'admin'),
       (2, 5, 'Mystery', 'Mystery', 'Mystere', 'Mystere', 5, sysdate(), 'admin', CURRENT_TIMESTAMP, 'admin');
