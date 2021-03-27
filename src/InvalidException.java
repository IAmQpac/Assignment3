public class InvalidException extends Exception{
    String message = "Error: Input row cannot be parsed due to missing information";


    // default constructor
    public InvalidException(){
    }

    // constructor with desired message
    public InvalidException(String message){
        this.message=message;
    }

    @Override
    public String toString() {
        return "InvalidException{" +
                "message='" + message + '\'' +
                '}';
    }
}
