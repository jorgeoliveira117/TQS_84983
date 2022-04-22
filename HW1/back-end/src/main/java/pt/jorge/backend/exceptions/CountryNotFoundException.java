package pt.jorge.backend.exceptions;

public class CountryNotFoundException extends RuntimeException{

    public CountryNotFoundException(String country){
        super("Could not find country " + country);
    }

}
