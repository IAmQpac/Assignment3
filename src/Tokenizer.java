import java.util.ArrayList;

public class Tokenizer {
    String[] record;

    boolean missingField;
    ArrayList<Integer> missingIndex = new ArrayList<>();



    //  THIS SPLITS REMOVES THE , SO REPLACE THEM BACK IN IN THOSE WITH THE QUOTES
    public Tokenizer(String record) {
        this.record = record.split(",");
    }


    public String[] splitRecord() {
        ArrayList<String> tempRecord = new ArrayList<>();

        for (int i = 0; i < record.length; i++) {
            if (!record[i].startsWith("\"")){
                tempRecord.add(record[i]);
            }else if (record[i].startsWith("\"")){
                String firstField = record[i] +",";
                i++;
                while (!record[i].endsWith("\"")){
                    firstField = firstField + record[i];
                    i++;
                }
                firstField = firstField + record[i];
                firstField =  firstField.replace("\"", "");
                tempRecord.add(firstField);
            }
        }
        String[] tempStringArray = new String[tempRecord.toArray().length];

        for (int i = 0; i < tempStringArray.length; i++) {
            tempStringArray[i] = tempRecord.get(i);
        }
        return tempStringArray;
    }
}
