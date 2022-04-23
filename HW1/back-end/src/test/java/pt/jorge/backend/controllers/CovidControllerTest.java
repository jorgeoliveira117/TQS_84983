package pt.jorge.backend.controllers;


import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import pt.jorge.backend.entities.CovidDetails;
import pt.jorge.backend.exceptions.CountryNotFoundException;
import pt.jorge.backend.fetcher.CovidFetcher;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CovidController.class)
class CovidControllerTest {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private final String getCasesCountryURL = "/cases/{country}";
    private final String getCasesCountryDateURL = "/cases/{country}/{date}";
    private final String getCasesContinentsURL = "/cases/continents";
    private final String getCasesTopURL = "/cases/top/{n}";
    private final String getEvolutionCountryURL = "/cases/evolution/{country}";
    private final String getCountriesURL = "/countries";

    List<CovidDetails> detailsList;
    CovidDetails cdTemp;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CovidFetcher fetcher;

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
    // /cases/{country}
    @Test
    public void whenGetCasesForValidCountry_GetResults(){
        /*
        String country = cdTemp.getCountry();

        Mockito.when(fetcher.getToday()).thenReturn(detailsList);

        RestAssuredMockMvc
                .when()
                    .get(getCasesCountryURL, country)
                .then()
                    .statusCode(200)
                    .body("country", equalTo(country));

         */
    }
    @Test
    public void whenGetCasesForInvalidCountry_GetError(){
        String country = "portugaaaal";

        Mockito.when(fetcher.getToday()).thenReturn(detailsList);

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
    public void whenGetCasesForValidCountryAndValidDate_GetResults() throws ParseException {
        /*
        String country = cdTemp.getCountry();

        String dayString = sdf.format(cdTemp.getDay().getTime());
        Calendar day = Calendar.getInstance();
        day.setTime(sdf.parse(dayString));

        CovidDetails cd = new CovidDetails();
        cd.setCountry(country);
        cd.setContinent("Entry 2");
        detailsList.add(cd);

        cd = new CovidDetails();
        cd.setCountry(country);
        cd.setContinent("Entry 3");
        detailsList.add(cd);

        Mockito.when(fetcher.getHistory(Mockito.any(), Mockito.any())).thenReturn(detailsList);

        RestAssuredMockMvc
                .when()
                    .get(getCasesCountryDateURL, country, dayString)
                .then()
                    .statusCode(200)
                    .body("continent", hasItems("Entry 1", "Entry 2", "Entry 3"));

         */
    }
    @Test
    public void whenGetCasesForInvalidDate_GetError(){

    }
    @Test
    public void whenGetCasesForInvalidCountryAndValidDate_GetError(){

    }
    @Test
    public void whenGetCasesForValidCountryAndInvalidDate_GetError(){

    }
    // ==================================
    // /cases/continents
    @Test
    public void whenGetCasesForContinents_GetResults(){

    }
    // ==================================
    // /cases
    @Test
    public void whenGetAllCases_GetResults(){

    }
    // ==================================
    // /cases/top/{n}
    @Test
    public void whenGetTopCasesForValidN_GetResults(){

    }
    @Test
    public void whenGetTopCasesForInvalidN_GetError(){

    }
    // ==================================
    // /cases/evolution/{country}
    @Test
    public void whenGetEvolutionForValidCountry_GetResults(){

    }
    @Test
    public void whenGetEvolutionForInvalidCountry_GetResults(){

    }
    // ==================================
    // /cases/world/evolution
    @Test
    public void whenGetWorldEvolution_GetResults(){

    }
}