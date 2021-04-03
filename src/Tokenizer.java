import java.util.ArrayList;

public class Tokenizer {
    String[] record;

    //  THIS SPLITS REMOVES THE , SO REPLACE THEM BACK IN IN THOSE WITH THE QUOTES
    public Tokenizer(String record) {
        this.record = record.split(",");
    }


    public String[] splitRecord() {
        ArrayList<String> temp = new ArrayList<>();

        for (int i = 0; i < record.length; i++) {
            if (!record[i].startsWith("\"")){
                temp.add(record[i]);
            }else if (record[i].startsWith("\"")){
                String firstField = record[i];
                i++;
                while (!record[i].endsWith("\"")){
                    firstField = firstField + record[i];
//                    firstField.concat(record[i]);
                    i++;
                }
//                DOES NOT WORK
                firstField.replace("\"", "");
                temp.add(firstField);
            }
        }
        String[] temp2 = new String[temp.toArray().length];
        for (int i = 0; i < temp2.length; i++) {
            temp2[i] = temp.get(i);

        }
        return temp2;
    }

}
