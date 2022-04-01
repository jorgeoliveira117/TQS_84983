import io.github.bonigarcia.wdm.WebDriverManager;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import webpages.FlightFormPage;
import webpages.FlightsPage;
import webpages.HomePage;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SeleniumTest {
    WebDriver driver;


    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp(){
        driver = new ChromeDriver();
        // driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Test
    public void applyAsDeveloper() {
        //Create object of HomePage Class
        HomePage home = new HomePage(driver);
        home.clickOnFindFlightsButton();

        //Create object of FlightsPage
        FlightsPage flightspage = new FlightsPage(driver);

        //Check if page is opened
        assertTrue(flightspage.isPageOpened());

        //Click on Join Toptal
        flightspage.clickOnFlight();

        //Create object of FlightFormPage
        FlightFormPage formPage =new FlightFormPage(driver);

        //Check if page is opened
        assertTrue(formPage.isPageOpened());

        //Fill up data
        formPage.setInputName("First and Last");
        formPage.setAddress("A address that exists");
        formPage.setCity("Nowhere");
        formPage.setState("Galaxy");
        formPage.setZipCode("1337");
        formPage.setRememberMe();

        //Click on purchase
        //applyPage.clickOnJoin();
    }

    @After
    public void close(){
        driver.close();
    }
}
