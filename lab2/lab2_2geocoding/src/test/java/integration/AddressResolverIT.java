package integration;

import connection.TqsBasicHttpClient;
import geocode.Address;
import geocode.AddressResolver;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AddressResolverIT {

    private AddressResolver res;
    @BeforeEach
    public void init(){
        TqsBasicHttpClient httpClient = new TqsBasicHttpClient();
        res = new AddressResolver(httpClient);
    }

    @Test
    public void whenGoodCoordidates_returnAddress() throws IOException, URISyntaxException, ParseException {
        Optional<Address> result = res.findAddressForLocation(38.708908, -9.261124);

        assertEquals( result.get(), new Address( "National Stadium", "Murganhal", "Lisboa", "2795", null) );
    }

    @Test
    public void whenBadCoordidates_thenReturnNoValidAddrress() throws IOException, URISyntaxException, ParseException {

        assertThrows(IndexOutOfBoundsException.class,
                () -> {res.findAddressForLocation(38.708908, -900.261124);});


    }

}
