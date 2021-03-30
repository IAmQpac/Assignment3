public class Tokenizer {
    String[] record;

    public Tokenizer(String dataLine) {
        this.record = dataLine.split(",");
    }

    public String[] fixRecord(String[] record) {
        String[] temp = new String[record.length];

        for (int i = 0; i < record.length; i++) {
            if (record[i].charAt(0) != '\"'){
                temp[i] = record[i];
            }else if (record[i].charAt(0) == '\"'){
                String tempString = record[i];
                int j = i+1;
                int wordLength = record[j].length();
                while (record[j].charAt(wordLength-1) != '\"'){
                    tempString.concat(record[j]);
                    j++;
                }
                temp[i] = tempString;
            }
        }
        return temp;

    }

    public String[] removeEmptyStrings(String[] temp){
        int count = 0;
        for (int i = 0; i < temp.length; i++) {

            if (temp[i] != null)
                count++;
        }
        String[] noEmptyStrings = new String[count];
        for (int i = 0; i < noEmptyStrings.length; i++) {
            noEmptyStrings[i] = temp[i];
        }
        return noEmptyStrings;

    }
}
