import java.time.temporal.Temporal;
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

        Scanner[] readArray = {readCarRentalRecord, readCarMaintenanceRecord};

        //WRITE
        PrintWriter writeCarRentalRecord = null;
        PrintWriter writeCarMaintenanceRecord = null;

        PrintWriter[] writeArray = {writeCarRentalRecord, writeCarMaintenanceRecord};

        //Reading Car rental and Car Maintenance
        for (int i = 0; i < readArray.length; i++) {
            try {
                readArray[i] = new Scanner(fileArray[i]);
            } catch (FileNotFoundException e) {
                System.out.println("Could not open input file " + fileArray[i] + " for reading. Please check if file exists! Program will terminate after closing any opened files");
                //closing all previews opened files
                for (int j = 0; j < i; j++) {
                    readArray[j].close();
                }
                System.exit(0);
            }
        }

        //Writing to Car Rental and Car Maintenance
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

        processFilesForValidation(readCarRentalRecord,writeCarRentalRecord,CarRentalRecord, writeLogFile, logFile);
        processFilesForValidation(readCarMaintenanceRecord,writeCarMaintenanceRecord,CarMaintenanceRecord, writeLogFile, logFile);



    }


    public static Tokenizer[] CreateTokenizerArray(Scanner read) {
        //I dont' wanna count the first one.
        int count=-1;
        while (read.hasNextLine()){
           String str = read.nextLine();
            count++;
        }

        Tokenizer[] temp = new Tokenizer[count];
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
    public static void processFilesForValidation(Scanner read, PrintWriter write,File file, PrintWriter writeLogFile, File logFile) {

        //Create tokenizer Array for Car Rental/Car Maintenance
        Tokenizer[] TokenizerArray;
        TokenizerArray = CreateTokenizerArray(read);


        //Checking if we miss any attributes:
        try{
            if (missingAttribute(read) == true){
                writeLogFile.println(logFile + "is invalid: Field is missing. \nFile will not be converted to JSON");
                throw new InvalidException();
            }
            else   //VALID......................

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


//    public static int[] cleanArray (int[] longIndexOfWordsWithDoubleQuotes){
//        int count = 0;
//        for (int i = 0; i < longIndexOfWordsWithDoubleQuotes.length; i++) {
//            if (longIndexOfWordsWithDoubleQuotes[i] !=0 )
//                count++;
//        }
//
//        int[] tempArray = new int[count];
//
//        for (int i = 0; i < tempArray.length; i++) {
//            for (int j = 0; j < longIndexOfWordsWithDoubleQuotes.length; j++) {
//                if (longIndexOfWordsWithDoubleQuotes[j] != 0){
//                    tempArray[i] = longIndexOfWordsWithDoubleQuotes[j];
//                }
//            }
//        }
//        return tempArray;
//    }
//
//
//    public static String[] generateFinalDataLine(int[] shortIndexOfWordsWithDoubleQuotes, String[] wordDataLine){
//        int oldLength = wordDataLine.length;
//        int newLength = oldLength;
//
//
//        for (int i = 0; i < (shortIndexOfWordsWithDoubleQuotes.length/2); i+=2) {
//            newLength = newLength - ( shortIndexOfWordsWithDoubleQuotes[i+1] - shortIndexOfWordsWithDoubleQuotes[i]);
//        }
//        String[] finalDataLine = new String[newLength];
//
//
//
//    }


}
