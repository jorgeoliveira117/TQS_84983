package org.example.restassured;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class ApiTest {

    private final String apiURL = "https://jsonplaceholder.typicode.com/todos";

    @Test
    public void whenGet_thenOK(){
        when().
                get(apiURL).
        then().
                statusCode(200);
    }

    @Test
    public void whenGetId4_thenReturnCorrect(){
        String expectedTitle = "et porro tempora";
        given().
                param("id", 4).
        when().
                get(apiURL).
        then().
                statusCode(200).
                log().body().
                and().body("title", contains(expectedTitle));
    }

    @Test
    public void whenGetAll_thenReturnId198And199(){
        String expectedTitle = "et porro tempora";
        when().
                get(apiURL).
        then().
                statusCode(200).
                and().body("id", hasItems(198, 199));
    }

}
