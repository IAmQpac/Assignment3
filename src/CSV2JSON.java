import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class CSV2JSON {
    public static void main(String[] args) {

        File CarRentalRecord = new File("Car Rental Record.txt");
        File CarMaintenanceRecord = new File("Car Maintenance Record.txt");
        File CarRentalNoDriv = new File("Car Rental Record no DrivLic.txt");
        File CarRentalNoPlate= new File("Car Rental Record no Plate.txt");
        File logFile = new File("log.txt");


        Scanner readLogFile = null;
        PrintWriter writeLogFile = null;
        try {
            writeLogFile = new PrintWriter(logFile);
            readLogFile = new Scanner(logFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not find the log file");
        }

        File[] fileArray = {CarRentalRecord, CarMaintenanceRecord, CarRentalNoDriv, CarRentalNoPlate};


        //READ
        Scanner readCarRentalRecord = null;
        Scanner readCarMaintenanceRecord = null;
        Scanner readCarRentalNoDriv = null;
        Scanner readCarRentalNoPlate = null;

        //Reading Car rental
        try {
            readCarRentalRecord = new Scanner(fileArray[0]);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open input file " + fileArray[0] + " for reading. Please check if file exists! Program will terminate after closing any opened files");
            //closing all previews opened files
            readCarRentalRecord.close();
            System.exit(0);
        }
        //Reading Car Maintenance
        try {
            readCarMaintenanceRecord = new Scanner(fileArray[1]);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open input file " + fileArray[1] + " for reading. Please check if file exists! Program will terminate after closing any opened files");
            //closing all previews opened files
            readCarMaintenanceRecord.close();
            readCarRentalRecord.close();
            System.exit(0);
        }

        //Reading Car Rental No Driv
        try {
            readCarRentalNoDriv = new Scanner(fileArray[2]);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open input file " + fileArray[2] + " for reading. Please check if file exists! Program will terminate after closing any opened files");
            //closing all previews opened files
            readCarRentalNoDriv.close();
            readCarMaintenanceRecord.close();
            readCarRentalRecord.close();
            System.exit(0);
        }

        //Reading Car Rental No Plate
        try {
            readCarRentalNoPlate = new Scanner(fileArray[3]);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open input file " + fileArray[3] + " for reading. Please check if file exists! Program will terminate after closing any opened files");
            //closing all previews opened files
            readCarMaintenanceRecord.close();
            readCarRentalNoDriv.close();
            readCarRentalRecord.close();
            readCarRentalNoPlate.close();
            System.exit(0);
        }

        processFilesForValidation(readCarRentalRecord,CarRentalRecord, writeLogFile, logFile);
        processFilesForValidation(readCarMaintenanceRecord,CarMaintenanceRecord, writeLogFile, logFile);
        processFilesForValidation(readCarRentalNoDriv, CarRentalNoDriv, writeLogFile, logFile );
        processFilesForValidation(readCarRentalNoPlate,CarRentalNoPlate, writeLogFile, logFile);
    }


    public static ArrayList<Tokenizer> CreateTokenizerArray(Scanner read, File file) {
        ArrayList<Tokenizer> temp = new ArrayList<>();

        while (read.hasNextLine()){
            String str = read.nextLine();
            Tokenizer token = new Tokenizer(str);
            temp.add(token);
        }



        //Checks for missing fields AND missing Attributes
        for (int i = 0; i < temp.toArray().length; i++) {
            for (int j = 0; j < temp.get(i).record.length; j++) {
                if (temp.get(i).record[j] == ""){
                    temp.get(i).missingField = true;
                    temp.get(i).missingIndex.add(j);
                }
            }
        }

        for (int i = 0; i < temp.toArray().length; i++) {
            temp.get(i).record = temp.get(i).splitRecord();
        }

        return temp;
    }





    //    Processing input files and creating output ones
    public static void processFilesForValidation(Scanner read,File file, PrintWriter writeLogFile, File logFile) {
        ArrayList<Tokenizer> tokenizer = CreateTokenizerArray(read, file);

        PrintWriter writeJSON = null;
        try {
            writeJSON = new PrintWriter(file.getName().replace(".txt", ".json"));
        }catch (FileNotFoundException e){
            System.out.println("No json files were found");
            return;
        }

        //missing attributes
        if (tokenizer.get(0).missingField){
            System.out.println("----");
            System.out.println(file + " is invalid: Field is missing. \nFile will not be converted to JSON");
            System.out.println("----");
            writeLogFile.println("----");
            writeLogFile.println("File " + file + " is invalid.");

            writeLogFile.print("Missing Field: " + ((tokenizer.get(0).record.length) - (tokenizer.get(0).missingIndex.toArray().length)) + " detected," );
            writeLogFile.println( ((tokenizer.get(0).missingIndex.toArray().length)  + " missing" ));


            for (int i = 0; i < tokenizer.get(0).record.length; i++) {
                if (tokenizer.get(0).record[i].equals("")){
                    writeLogFile.print("****,");
                }else writeLogFile.print(tokenizer.get(0).record[i] + ",");
            }
            writeLogFile.close();
        }

        //missing data
        for (int i = 1; i < tokenizer.toArray().length; i++) {
            if (tokenizer.get(i).missingField){
                System.out.println("----");
                System.out.println("In file " + file + " line " + (i+1) + " not converted to JSON: Missing data");
                System.out.println("We will transfer the rest of the records to JSON.");
                writeLogFile.println("In file " + file + " line " + (i+1));
                System.out.println("----");
                for (int j = 0; j < tokenizer.get(i).record.length; j++) {
                    if (tokenizer.get(i).record[j].equals("")){
                        writeLogFile.print("****,");
                    }else writeLogFile.print(tokenizer.get(i).record[j] + ",");
                }
                writeLogFile.println("");
            }
        }


        //printing to json
        if (!tokenizer.get(0).missingField){
            writeJSON.println("[");
            //for all records (row)
            for (int i = 1; i < tokenizer.toArray().length; i++) {
                writeJSON.println("\t{");
                //for all fields
                for (int j = 0; j < tokenizer.get(i).record.length; j++) {
                    if (!tokenizer.get(i).record[j].equals("")){
                        writeJSON.print("\t\t\""+ tokenizer.get(0).record[j] + "\"" + ": ");


                        if (j == tokenizer.get(0).record.length -1 ){
                            try {
                                Integer.parseInt(tokenizer.get(i).record[j]);
                                writeJSON.println(tokenizer.get(i).record[j]);
                            }catch (Exception e){
                                writeJSON.println("\""+ tokenizer.get(i).record[j] +"\"");
                            }
                        } else {
                            try {
                                Integer.parseInt(tokenizer.get(i).record[j]);
                                writeJSON.println(tokenizer.get(i).record[j] + ",");
                            }catch (Exception e){
                                writeJSON.println("\""+ tokenizer.get(i).record[j] +"\"" + ",");
                            }
                        }

                    }

                }
                if (i == tokenizer.toArray().length - 1){
                    writeJSON.println("\t}");
                }else writeJSON.println("\t},");
            }
            writeJSON.println("]");
            writeJSON.close();

        }


    }


}
