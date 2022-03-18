package geocode;


import connection.ISimpleHttpClient;
import connection.TqsBasicHttpClient;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddressResolverTest {



    @Test
    void whenResolveJamorGps_returnEstadioNacionalAddress() throws ParseException, IOException, URISyntaxException {
        TqsBasicHttpClient mockClient = mock(TqsBasicHttpClient.class);

        String json = "{\"info\":{\"statuscode\":0,\"copyright\":{\"text\":\"\\u00A9 2022 MapQuest, Inc.\",\"imageUrl\":\"http://api.mqcdn.com/res/mqlogo.gif\",\"imageAltText\":\"\\u00A9 2022 MapQuest, Inc.\"},\"messages\":[]},\"options\":{\"maxResults\":1,\"thumbMaps\":true,\"ignoreLatLngInput\":false},\"results\":[{\"providedLocation\":{\"latLng\":{\"lat\":38.708908275981734,\"lng\":-9.261124272802487}},\"locations\":[{\"street\":\"Est\\u00E1dio Nacional\",\"adminArea6\":\"\",\"adminArea6Type\":\"Neighborhood\",\"adminArea5\":\"Murganhal\",\"adminArea5Type\":\"City\",\"adminArea4\":\"\",\"adminArea4Type\":\"County\",\"adminArea3\":\"Lisboa\",\"adminArea3Type\":\"State\",\"adminArea1\":\"PT\",\"adminArea1Type\":\"Country\",\"postalCode\":\"2795\",\"geocodeQualityCode\":\"P1AAA\",\"geocodeQuality\":\"POINT\",\"dragPoint\":false,\"sideOfStreet\":\"N\",\"linkId\":\"0\",\"unknownInput\":\"\",\"type\":\"s\",\"latLng\":{\"lat\":38.708791,\"lng\":-9.260568},\"displayLatLng\":{\"lat\":38.708791,\"lng\":-9.260568},\"mapUrl\":\"http://open.mapquestapi.com/staticmap/v5/map?key=uXSAVwYWbf9tJmsjEGHKKAo0gOjZfBLQ&type=map&size=225,160&locations=38.7087907,-9.26056810998582|marker-sm-50318A-1&scalebar=true&zoom=15&rand=-2048975361\",\"roadMetadata\":null}]}]}";

        AddressResolver res = new AddressResolver(mockClient);

        when(mockClient.doHttpGet(contains("location=38.708908%2C-9.261124"))).thenReturn(json);

        Optional<Address> result = res.findAddressForLocation(38.708908, -9.261124);

        assertEquals( result.get(), new Address( "Estádio Nacional", "Murganhal", "Lisboa", "2795", null) );
    }

    @Test
    public void whenBadCoordidates_thenReturnNoValidAddress() throws IOException, URISyntaxException, ParseException {

        TqsBasicHttpClient mockClient = mock(TqsBasicHttpClient.class);

        String json = "{\"info\":{\"statuscode\":0,\"copyright\":{\"text\":\"\\u00A9 2022 MapQuest, Inc.\",\"imageUrl\":\"http://api.mqcdn.com/res/mqlogo.gif\",\"imageAltText\":\"\\u00A9 2022 MapQuest, Inc.\"},\"messages\":[]},\"options\":{\"maxResults\":1,\"thumbMaps\":true,\"ignoreLatLngInput\":false},\"results\":[{\"providedLocation\":{\"latLng\":{\"lat\":38.708908275981734,\"lng\":-900.2611242728025}},\"locations\":[]}]}";

        AddressResolver res = new AddressResolver(mockClient);


        assertThrows(IndexOutOfBoundsException.class,
                () -> {res.findAddressForLocation(38.708908, -900.261124);});
    }
}
