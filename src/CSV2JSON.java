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



//        Scanner[] readArray = {readCarRentalRecord, readCarMaintenanceRecord};


        processFilesForValidation(readCarRentalRecord,CarRentalRecord, writeLogFile, logFile);
        processFilesForValidation(readCarMaintenanceRecord,CarMaintenanceRecord, writeLogFile, logFile);
        processFilesForValidation(readCarRentalNoDriv, CarRentalNoDriv, writeLogFile, logFile );
        processFilesForValidation(readCarRentalNoPlate,CarRentalNoPlate, writeLogFile, logFile);




    }


    public static ArrayList<Tokenizer> CreateTokenizerArray(Scanner read) {

        ArrayList<Tokenizer> temp = new ArrayList<>();

        while (read.hasNextLine()){
            String str = read.nextLine();
            temp.add(new Tokenizer(str));
        }

        // i=1 to skip attributes
        for (int i = 1; i < temp.toArray().length; i++) {
            temp.get(i).record = temp.get(i).splitRecord();
        }
        return temp;
    }


    public static boolean missingAttribute(File file) throws FileNotFoundException {
        Scanner temp = new Scanner(file);

        String attributesLine = temp.nextLine();
        String[] attributesWords = attributesLine.split(",");
        int count=0;

        for (int i = 0; i < attributesWords.length; i++) {
            if (attributesWords[i] == ""){
                count++;
            }
        }
        if (count > 0 ){
            return  true;
        }else return false;
    }


    //    Processing input files and creating output ones
    public static void processFilesForValidation(Scanner read,File file, PrintWriter writeLogFile, File logFile) {
        ArrayList<Tokenizer> tokenizer = CreateTokenizerArray(read);

        PrintWriter writeJSON = null;
        try {
            writeJSON = new PrintWriter(file.getName().replace(".txt", ".json"));
        }catch (FileNotFoundException e){
            System.out.println("No json files were found");
            return;
        }

        //Checking if we miss any attributes:
        try{
            if (missingAttribute(file) == true){
                writeLogFile.println(logFile + "is invalid: Field is missing. \nFile will not be converted to JSON");
                throw new InvalidException();
            }
        }catch (InvalidException | FileNotFoundException e){
            System.out.println(e);
        }

        //Checking if we miss any data in Car Rental/Car Maintenance:
        writeJSON.println("[");
            for (int i = 1; i < tokenizer.toArray().length; i++) {
                writeJSON.println("\t{");
                for (int j = 0; j < tokenizer.get(i).record.length; j++) {
                    if (tokenizer.get(i).record[j].equals("")){
                        //writing to log file missing record.
                        writeLogFile.println(">Missing record: in " + file);
                        System.out.println("There is record missing in "+ file +", we will transfer the rest of the records to JSON.");
                    }else {
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
                                Integer.parseInt(tokenizer.get(i).record[j] + ",");
                                writeJSON.println(tokenizer.get(i).record[j]);
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
