import java.util.Scanner;
import java.io.*;

public class CSV2JSON {
    public static void main(String[] args) {

        File CarRentalRecord = new File("Car Rental Record.csv");
        File CarMaintenanceRecord = new File("Car Maintenance Record.csv");


        File[] fileArray = {CarRentalRecord, CarMaintenanceRecord};


        Scanner readCarRentalRecord = null;
        Scanner readCarMaintenanceRecord = null;

        Scanner[] readArray = {readCarRentalRecord, readCarMaintenanceRecord};

        PrintWriter writeCarRentalRecord = null;
        PrintWriter writeCarMaintenanceRecord = null;

        PrintWriter[] writeArray = {writeCarRentalRecord, writeCarMaintenanceRecord};


//        Reading Car rental and Car Maintenance
        for (int i = 0; i < readArray.length; i++) {
            try {
                readArray[i] = new Scanner(new FileReader(fileArray[i]));
            } catch (FileNotFoundException e) {
                System.out.println("Could not open input file " + fileArray[i] + " for reading. Please check if file exists! Program will terminate after closing any opened files");
                //closing all previews opened files
                for (int j = 0; j < i; j++) {
                    readArray[j].close();
                }
                System.exit(0);
            }
        }

//        Writing to Car Rental and Car Maintenance
        for (int i = 0; i < writeArray.length; i++) {
            try {
                writeArray[i] = new PrintWriter(fileArray[i]);
            } catch (FileNotFoundException e) {
                System.out.println("Could not open " + fileArray[i] + " for writing. We will delete all the created output files, close all opened input files and exit program.");
                for (int j = 0; j < i; j++) {
                    writeArray[j].close();
                }
                System.exit(0);
            }
        }
    }


    //    Processing input files and creating output ones
    public static void processFilesForValidation() {

    }
//
//    public static boolean validity() {
//
//    }
}
