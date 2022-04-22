package pt.jorge.backend.exceptions;

public class InvalidDateException extends RuntimeException{

    public InvalidDateException(String date){
        super("Provided date '" + date + "' is invalid. Should be YYYY-MM-DD");
    }
    
}
