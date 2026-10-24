package ca.hccis.bookstore.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * File utilities for the application.
 *
 * @author BJM
 * @modifiedby KP 2026-10-24 #5 File name format changed to <report><yyyyMMddHHmm>.txt
 */
public class CisUtilityFile {

    public static final String FOLDER_NAME = "c:\\CIS2232\\";

    public static void writeReportToFile(String fileName, ArrayList theObjects) {

        //KP 2026-10-24 #5 File name matches the sprint requirement, e.g. IncomeReport202610241630.txt
        String fullName = FOLDER_NAME + fileName + CisUtility.getCurrentDate("yyyyMMddHHmm") + ".txt";
        Path path = Paths.get(FOLDER_NAME);
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            System.out.println("BJM-CisUtilityFile-Error creating directories");
        }

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fullName, true));

            for (Object current : theObjects) {
                writer.write(current.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("CisUtilityFile exception with file: " + fullName);
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                System.out.println("CisUtilityFile exception with file: " + fullName);
            }
        }

    }
}