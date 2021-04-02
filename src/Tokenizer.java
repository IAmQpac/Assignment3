import java.util.ArrayList;

public class Tokenizer {
    String[] record;

    //  THIS SPLITS REMOVES THE , SO REPLACE THEM BACK IN IN THOSE WITH THE QUOTES
    public Tokenizer(String record) {
        this.record = record.split(",");
    }


    public ArrayList<String> splitRecord() {
        ArrayList<String> temp = new ArrayList<>();

        for (int i = 0; i < record.length; i++) {
            if (record[i].charAt(0)!= '\"'){
                temp.add(record[i]);
            }else if (record[i].charAt(0) == '\"'){
                String firstField = record[i];
                i++;
                while (record[i].charAt(record[i].length()-1) != '\"'){
                    firstField.concat(record[i]);
                    if (i == record.length-1) break;
                    else i++;
                }
                temp.add(record[i]);
            }
        }
        return temp;
    }

}
