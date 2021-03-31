import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class CSV2JSON {
    public static void main(String[] args) {

        File CarRentalRecord = new File("Car Rental Record.txt");
        File CarMaintenanceRecord = new File("Car Maintenance Record.txt");
        File logFile = new File("log.txt");


        Scanner readLogFile = null;
        PrintWriter writeLogFile = null;
        try {
            writeLogFile = new PrintWriter(logFile);
            readLogFile = new Scanner(logFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not find the log file");
        }

        File[] fileArray = {CarRentalRecord, CarMaintenanceRecord};


        //READ
        Scanner readCarRentalRecord = null;
        Scanner readCarMaintenanceRecord = null;

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

//        Scanner[] readArray = {readCarRentalRecord, readCarMaintenanceRecord};


        processFilesForValidation(readCarRentalRecord,CarRentalRecord, writeLogFile, logFile);
        processFilesForValidation(readCarMaintenanceRecord,CarMaintenanceRecord, writeLogFile, logFile);



    }


    public static ArrayList<Tokenizer> CreateTokenizerArray(Scanner read) {

        ArrayList<Tokenizer> temp = new ArrayList<>();

        while (read.hasNextLine()){
            String str = read.nextLine();
            temp.add(new Tokenizer(str));
        }

        for (int i = 0; i < temp.toArray().length; i++) {
            String[] record = temp.get(i).record;
            temp.get(i).fixRecord(record);
        }
        return temp;
    }




    public static boolean missingAttribute(Scanner readFile) {
        String attributesLine = readFile.nextLine();
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

        //Create tokenizer Array for Car Rental/Car Maintenance
        Tokenizer[] tokenizerArray;
        tokenizerArray = CreateTokenizerArray(read).toArray(new Tokenizer[0]);

        PrintWriter writeJSON = null;
        try {
            writeJSON=new PrintWriter(file.getName().replace(".txt", ".json"));
        }catch (FileNotFoundException e){
            System.out.println("No json files were found");
        }


        //Checking if we miss any attributes:
        try{
            if (missingAttribute(read) == true){
                writeLogFile.println(logFile + "is invalid: Field is missing. \nFile will not be converted to JSON");
                throw new InvalidException();
            }
        }catch (InvalidException e){
            System.out.println(e);
        }

        //Checking if we miss any data in Car Rental/Car Maintenance:
        writeJSON.println("[");
        try{
            for (int i = 0; i < tokenizerArray.length; i++) {
                for (int j = 0; j < tokenizerArray[i].record.length; j++) {
                    if (tokenizerArray[i].record[j].equals("")){
                        //writing to log file missing record.
                        writeLogFile.println(">Missing record: in " + file);
                        throw new InvalidException("There is record missing in "+ file +", we will transfer the rest of the records to JSON.");
                    }else {
                        writeJSON.println("{");
                        writeJSON.println("\" "+ tokenizerArray[0].record[j] + ": " + tokenizerArray[i].record[j] +" \"");
                        writeJSON.println("},");
                    }
                }
            }
        } catch (InvalidException e) {
            e.printStackTrace();
        }
    }


}
