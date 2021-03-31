import java.util.Scanner;
import java.io.*;

public class CSV2JSON {
    public static void main(String[] args) {

        File CarRentalRecord = new File("Car Rental Record.csv");
        File CarMaintenanceRecord = new File("Car Maintenance Record.csv");
        File logFile = new File("log.txt");


        Scanner readLogFile = null;
        PrintWriter writeLogFile = null;
        try {
            readLogFile = new Scanner(logFile);
            writeLogFile = new PrintWriter(logFile);
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

        Scanner[] readArray = {readCarRentalRecord, readCarMaintenanceRecord};


        //WRITE
        PrintWriter writeCarRentalRecord = null;
        PrintWriter writeCarMaintenanceRecord = null;

//        //Writing to Car Rental
//        try {
//            writeCarRentalRecord = new PrintWriter(fileArray[0]);
//
//        }catch (FileNotFoundException e){
//            System.out.println("Could not open " + fileArray[0] + " for writing. We will delete all the created output files, close all opened input files and exit program.");
//            writeCarRentalRecord.close();
//            System.exit(0);
//        }
//
//        //Writing to Car Maintenance
//        try {
//            writeCarMaintenanceRecord = new PrintWriter(fileArray[1]);
//
//        }catch (FileNotFoundException e){
//            System.out.println("Could not open " + fileArray[1] + " for writing. We will delete all the created output files, close all opened input files and exit program.");
//            writeCarRentalRecord.close();
//            writeCarMaintenanceRecord.close();
//            System.exit(0);
//        }
//
//        PrintWriter[] writeArray = {writeCarRentalRecord, writeCarMaintenanceRecord};


        processFilesForValidation(readCarRentalRecord,CarRentalRecord, writeLogFile, logFile);
        processFilesForValidation(readCarMaintenanceRecord,CarMaintenanceRecord, writeLogFile, logFile);



    }


    public static Tokenizer[] CreateTokenizerArray(Scanner read) {

        int count=0;
        while (read.hasNextLine()){
            read.nextLine();
            count++;
        }
        System.out.println(count);

        Tokenizer[] temp = new Tokenizer[count-1];

        int i=0;
        while (read.hasNextLine()){
            String str = read.nextLine();
            temp[i] = new Tokenizer(str);
            i++;
        }

        for (int j = 0; j < temp.length; j++) {
            String[] record = temp[i].record;
            temp[i].fixRecord(record);
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
        Tokenizer[] TokenizerArray;
        TokenizerArray = CreateTokenizerArray(read);


        //Checking if we miss any attributes:
        try{
            if (missingAttribute(read) == true){
                writeLogFile.println(logFile + "is invalid: Field is missing. \nFile will not be converted to JSON");
                throw new InvalidException();
            }
            else {
                File jsonFile = new File(file.getName()+".json");
            }
        }catch (InvalidException e){
            System.out.println(e);
        }

        //Checking if we miss any data in Car Rental/Car Maintenance:
        try{
            for (int i = 0; i < TokenizerArray.length; i++) {
                for (int j = 0; j < TokenizerArray[i].record.length; j++) {
                    if (TokenizerArray[i].record[j].equals("")){
                        writeLogFile.println(">Missing record: in " + file);
                        throw new InvalidException("There is record missing in "+ file +", we will transfer the rest of the records to JSON.");
                    }
                }
            }
        } catch (InvalidException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
