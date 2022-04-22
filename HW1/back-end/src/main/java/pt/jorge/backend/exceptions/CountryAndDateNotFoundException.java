package pt.jorge.backend.exceptions;

public class CountryAndDateNotFoundException extends RuntimeException{

    public CountryAndDateNotFoundException(String country, String date){
        super("Could not find history for country " + country + " with date " + date);
    }

}
