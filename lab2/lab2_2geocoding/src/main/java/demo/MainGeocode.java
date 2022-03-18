/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demo;

import connection.TqsBasicHttpClient;
import geocode.Address;
import geocode.AddressResolver;
import org.apache.http.ParseException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ico
 */
public class MainGeocode {

    /**
     * demo for address resolver
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            AddressResolver resolver =new AddressResolver( new TqsBasicHttpClient());

            Optional<Address> result = resolver.findAddressForLocation( 40.6406609,-8.6566883);
            System.out.println( "Result: ".concat( result.get().toString() ));

            result = resolver.findAddressForLocation( 120,-600);
            System.out.println( "Result: ".concat( String.valueOf(result.isPresent())));

        } catch (URISyntaxException | IOException | ParseException | org.json.simple.parser.ParseException ex) {
            Logger.getLogger(MainGeocode.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
