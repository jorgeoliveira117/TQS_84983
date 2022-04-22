package pt.jorge.backend.exceptions;

public class InvalidIdException extends RuntimeException{

    public InvalidIdException(int id){
        super("Provided id '" + id + "' is invalid. Must be an integer larger than 0");
    }
    
}
