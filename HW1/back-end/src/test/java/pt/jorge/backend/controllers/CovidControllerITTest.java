package pt.jorge.backend.controllers;


import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.servlet.MockMvc;
import pt.jorge.backend.entities.CovidDetails;
import pt.jorge.backend.exceptions.CountryAndDateNotFoundException;
import pt.jorge.backend.exceptions.CountryNotFoundException;
import pt.jorge.backend.exceptions.InvalidDateException;
import pt.jorge.backend.exceptions.InvalidIdException;
import pt.jorge.backend.fetcher.CovidFetcher;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CovidControllerITTest {

    // will need to use the server port for the invocation url
    @LocalServerPort
    int randomServerPort;

    // a REST client that is test-friendly
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MockMvc mockMvc;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private final String getCasesURL = "/cases";
    private final String getCasesCountryURL = "/cases/{country}";
    private final String getCasesCountryDateURL = "/cases/{country}/{date}";
    private final String getCasesContinentsURL = "/cases/continents";
    private final String getCasesTopURL = "/cases/top/{n}";
    private final String getEvolutionCountryURL = "/cases/evolution/{country}";
    private final String getCountriesURL = "/countries";
    private final String getCacheURL = "/cache";

    List<CovidDetails> detailsList;
    CovidDetails cdTemp;



    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);

        cdTemp = new CovidDetails();
        cdTemp.setCountry("Portugal");
        cdTemp.setContinent("Entry 1");
        cdTemp.setDay(Calendar.getInstance());
        cdTemp.setTime(Calendar.getInstance());

        detailsList = new ArrayList<>();
        detailsList.add(cdTemp);
    }
    // ==================================
    // /countries
    @Test
    @DisplayName("The result should be an OK with the results")
    public void whenGetCountries_GetOk(){
        RestAssuredMockMvc
                .when()
                    .get(getCountriesURL)
                .then()
                    .statusCode(200)
                    .body("", hasItem("Portugal"));
    }
    // ==================================
    // /cases/{country}
    @Test
    @DisplayName("The result should be an OK when there are valid parameters")
    public void whenGetCasesForValidCountry_GetResults(){
        String country = "Portugal";
        RestAssuredMockMvc
                .when()
                    .get(getCasesCountryURL, country)
                .then()
                    .statusCode(200)
                    .body("country", equalTo(country));
    }
    @Test
    @DisplayName("There should be an error when the requested country does not exist")
    public void whenGetCasesForInvalidCountry_GetError(){
        String country = "portugaaaal";
        RestAssuredMockMvc
                .when()
                    .get(getCasesCountryURL, country)
                .then()
                    .statusCode(404)
                .body(equalTo((new CountryNotFoundException(country)).getMessage().toString()));
    }
    // ==================================
    // /cases/{country}/{date}
    @Test
    @DisplayName("The result should be an OK when there are valid parameters")
    public void whenGetCasesForValidCountryAndValidDate_GetResults() throws ParseException {
        String country = "Portugal";
        // Set day to 2022-04-20
        String dayString = "2022-04-20";
        String expectedDay = dayString + "T00:00:00.000+00:00";
        RestAssuredMockMvc
                .when()
                    .get(getCasesCountryDateURL, country, dayString)
                .then()
                    .statusCode(200)
                    .body("day", equalTo(expectedDay));

    }
    @Test
    @DisplayName("There should be an error due to an invalid date")
    public void whenGetCasesForInvalidDate_GetError(){
        String country = "Portugal";
        String dayString = "not a date";
        RestAssuredMockMvc
                .when()
                .get(getCasesCountryDateURL, country, dayString)
                .then()
                .statusCode(400)
                .body(equalTo((new InvalidDateException(dayString)).getMessage().toString()));
    }
    @Test
    @DisplayName("There should be an error when the requested country does not exist")
    public void whenGetCasesForInvalidCountryAndValidDate_GetError(){
        String country = "Portugaaaaaal";
        String dayString = "2022-04-20";
        RestAssuredMockMvc
                .when()
                .get(getCasesCountryDateURL, country, dayString)
                .then()
                .statusCode(404)
                .body(equalTo((new CountryAndDateNotFoundException(country, dayString)).getMessage().toString()));
    }
    @Test
    @DisplayName("There should be an error due to a date without any result")
    public void whenGetCasesForValidCountryAndInvalidDate_GetError(){
        String country = "Portugal";
        String dayString = "2010-01-01";
        RestAssuredMockMvc
                .when()
                .get(getCasesCountryDateURL, country, dayString)
                .then()
                .statusCode(404)
                .body(equalTo((new CountryAndDateNotFoundException(country, dayString)).getMessage().toString()));
    }
    // ==================================
    // /cases/continents
    @Test
    @DisplayName("The result should be an OK and some continents should be present")
    public void whenGetCasesForContinents_GetResults(){
        RestAssuredMockMvc
                .when()
                .get(getCasesContinentsURL)
                .then()
                .statusCode(200)
                .body("continent", hasItems("Europe", "Asia", "Africa"))
                .body("country", hasItems("Europe", "Asia", "Africa"));
    }
    // ==================================
    // /cases
    @Test
    @DisplayName("The result should be an OK")
    public void whenGetAllCases_GetResults(){
        RestAssuredMockMvc
                .when()
                .get(getCasesURL)
                .then()
                .statusCode(200);
    }
    // ==================================
    // /cases/top/{n}
    @Test
    @DisplayName("The result should be OK and n results are given")
    public void whenGetTopCasesForValidN_GetResults(){
        int n = 3;
        RestAssuredMockMvc
                .when()
                .get(getCasesTopURL, n)
                .then()
                .statusCode(200)
                .body("size()", is(n));
    }
    @Test
    @DisplayName("The result should be an error")
    public void whenGetTopCasesForInvalidN_GetError(){
        int n = -1;
        RestAssuredMockMvc
                .when()
                .get(getCasesTopURL, n)
                .then()
                .statusCode(400)
                .body(equalTo((new InvalidIdException(n)).getMessage().toString()));
    }
    // ==================================
    // /cases/evolution/{country}
    @Test
    @DisplayName("The result should be a list where the specified country occurs in all entries")
    public void whenGetEvolutionForValidCountry_GetResults(){
        String country = "Portugal";
        RestAssuredMockMvc
                .when()
                .get(getEvolutionCountryURL, country)
                .then()
                .statusCode(200)
                .body("country", hasItem(country));
    }
    @Test
    @DisplayName("There should be an error")
    public void whenGetEvolutionForInvalidCountry_GetResults(){
        String country = "Portugaaaaal";
        RestAssuredMockMvc
                .when()
                .get(getEvolutionCountryURL, country)
                .then()
                .statusCode(404)
                .body(equalTo((new CountryNotFoundException(country)).getMessage().toString()));
    }
    // ==================================
    // /cases/world/evolution
    @Test
    public void whenGetWorldEvolution_GetResults(){
        // Test not implemented due the huge volume of data being processed
    }
    // ==================================
    // /cache
    @Test
    @DisplayName("At least a cache statistic should be shown")
    public void whenGetCcache_GetResults(){
        String cacheName = "Daily Cases";
        RestAssuredMockMvc
                .when()
                    .get(getCacheURL)
                .then()
                    .statusCode(200)
                    .body("name", hasItem(cacheName));
    }
}