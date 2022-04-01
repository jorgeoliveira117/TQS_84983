import io.github.bonigarcia.wdm.WebDriverManager;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SeleniumTest {
    private WebDriver driver;
    JavascriptExecutor js;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        js = (JavascriptExecutor) driver;
    }
    @AfterEach
    public void tearDown() {
        driver.quit();
    }
    @Test
    public void blazedemoTest() {
        driver.get("https://blazedemo.com/confirmation.php");
        driver.manage().window().setSize(new Dimension(1200, 800));
        // click the title on the navbar to return to the main page
        driver.findElement(By.linkText("Travel The World")).click();
        // select Departure city
        driver.findElement(By.name("fromPort")).click();
        {
            WebElement dropdown = driver.findElement(By.name("fromPort"));
            dropdown.findElement(By.xpath("//option[. = 'Boston']")).click();
        }
        // select Destination city
        driver.findElement(By.name("toPort")).click();
        {
            WebElement dropdown = driver.findElement(By.name("toPort"));
            dropdown.findElement(By.xpath("//option[. = 'Berlin']")).click();
        }
        // click the button
        driver.findElement(By.cssSelector(".btn-primary")).click();
        // click the button corresponding to the 4th flight
        driver.findElement(By.cssSelector("tr:nth-child(4) .btn")).click();
        // Fill out the form
        driver.findElement(By.id("inputName")).click();
        driver.findElement(By.id("inputName")).sendKeys("First Last");
        driver.findElement(By.id("address")).click();
        driver.findElement(By.id("address")).sendKeys("Address");
        driver.findElement(By.id("city")).click();
        driver.findElement(By.id("city")).sendKeys("City");
        driver.findElement(By.xpath("xpath=//label[contains(.,'Remember me')]")).click();
        // this assert fails sometimes and doesn't other
        assertTrue(driver.findElement(By.xpath("xpath=//label[contains(.,'Remember me')]")).isSelected());
        driver.findElement(By.cssSelector(".btn-primary")).click();

    }
}
