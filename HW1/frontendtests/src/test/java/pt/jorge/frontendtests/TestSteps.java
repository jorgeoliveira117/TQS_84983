package pt.jorge.frontendtests;


import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class TestSteps {

    private WebDriver driver;


    private WebElement findByElem(String arg){
        try {
            return driver.findElement(
                    By.xpath("//*[ contains (text(), '" + arg + "' ) ]"));
        } catch (NoSuchElementException e) {
            throw new AssertionError(
                    "\"" + arg + "\" not available in results");
        }
    }

    @When("I navigate to {string}")
    public void iNavigateTo(String url) {
        driver = WebDriverManager.chromedriver().create();
        driver.get(url);
    }

    @Then("I should be shown a page with {string}")
    public void iShouldBeShownAPageWith(String arg) {
        findByElem(arg);
    }

    @And("I should see some continents")
    public void iShouldSeeSomeContinents() {
        List<WebElement> elems = driver.findElements(By.tagName("h3"));

        String [] continents = {"World", "Europe", "North America", "South America", "Asia", "Africa", "Oceania"};
        List<String> continentList = Arrays.asList(continents);
        Set<String> continentsFound = new HashSet<>();

        // loop through elems to check if they contain the required continents
        for(WebElement elem: elems){
            if(continentList.contains(elem.getText()))
                continentsFound.add(elem.getText());
            System.out.println(elem.getText());
        }

        assert continentList.size() == continentsFound.size() : "Elements don't match the required continents";
    }

    @And("There should be a list with most cases")
    public void thereShouldBeAListWithMostCases() {
        findByElem("Countries with most cases today");
        // check that there is a table
        try {
            driver.findElement(By.tagName("table"));
        } catch (NoSuchElementException e) {
            throw new AssertionError("Table is not present in the page");
        }
        // check that there are entries in the table
        List<WebElement> elems = driver.findElements(By.tagName("tr"));
        assert !elems.isEmpty() : "Table doesn't have any entry";
    }

    @And("I click the {string} button")
    public void iClickTheButton(String arg) {
        // check all buttons
        List<WebElement> buttons = driver.findElements(By.tagName("button"));
        for(WebElement button: buttons) {
            // click the button with the desired text
            if(button.getText().contains(arg)){
                button.click();
                return;
            }
        }
        throw new AssertionError("Button with text \"" + arg + "\" not found");
    }

    @And("I should see a cache named {string}")
    public void iShouldSeeACacheNamed(String arg) {
        findByElem(arg);
    }

    @And("I should see a date in {string}")
    public void iShouldSeeADateIn(String arg) {
        WebElement elem = findByElem(arg);
        Pattern pattern = Pattern.compile(".*\\d{4}-\\d{2}-\\d{2}$");
        if(!pattern.matcher(elem.getText()).matches())
            throw new AssertionError("Element doesn't contain a valid date: " + elem.getText());

    }

    @And("I should see a number in {string}")
    public void iShouldSeeANumberIn(String arg) {
        WebElement elem = findByElem(arg);
        Pattern pattern = Pattern.compile(".*[0-9]$");
        if(!pattern.matcher(elem.getText()).matches())
            throw new AssertionError("Element doesn't contain a number " + elem.getText());

    }

    @And("I select {string} in the dropdown")
    public void iSelectInTheDropdown(String arg) {
        try {
            driver.findElement(By.name("selectCountry")).click();
        } catch (NoSuchElementException e) {
            throw new AssertionError("There's no dropdown with countries");
        }
        try {
            driver.findElement(By.cssSelector("option[value=" + arg + "]")).click();
        } catch (NoSuchElementException e) {
            throw new AssertionError("Country " + arg + " not present in the dropdown");
        }
    }

    @And("I select {string} in the datepicker")
    public void iSelectInTheDatepicker(String arg) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar date = Calendar.getInstance();
        try{
            date.setTime(sdf.parse(arg));
        } catch (ParseException e) {
            throw new AssertionError("Test input is invalid, should follow a date of format yyyy-MM-dd " + arg);
        }

        try{
            driver.findElements(By.className("react-date-picker"));
        }catch (NoSuchElementException e){
            throw new AssertionError("There's no calendar picker");
        }
        try{
            WebElement elem = driver.findElement(By.className("react-date-picker__inputGroup__year"));
            elem.clear();
            elem.click();
            elem.sendKeys(""+date.get(Calendar.YEAR));
            elem = driver.findElement(By.className("react-date-picker__inputGroup__month"));
            elem.clear();
            elem.click();
            elem.sendKeys(""+(date.get(Calendar.MONTH)+1));
            elem = driver.findElement(By.className("react-date-picker__inputGroup__day"));
            elem.clear();
            elem.click();
            elem.sendKeys(""+date.get(Calendar.DAY_OF_MONTH));
        }catch (Exception e){
            throw new AssertionError("Couldn't change date");
        }
    }

    @And("I should see {string}")
    public void iShouldSee(String arg) {
        WebDriverWait wait = new WebDriverWait(driver,5);
        WebElement elem;
        try{
            elem = driver.findElement(By.name("statsDay"));
        } catch (Exception e) {
            throw new AssertionError("Page doesn't a statistic day element");
        }
        wait.until(ExpectedConditions.textToBePresentInElement(elem, arg));
        if(!elem.getText().contains(arg))
            throw new AssertionError("Page doesn't contain the correct day " + elem.getText());
    }

    @And("I should be shown a line chart")
    public void iShouldBeShownALineChart() {

        try{
            driver.findElement(By.name("statsHistory"));
        } catch (NoSuchElementException e) {
            throw new AssertionError("Chart section doesn't have a title");
        }
        try{
            driver.findElement(By.className("recharts-wrapper"));
        } catch (NoSuchElementException e) {
            throw new AssertionError("Page doesn't contain a chart");
        }
        try{
            driver.findElement(By.name("casesButton"));
            driver.findElement(By.name("criticalButton"));
            driver.findElement(By.name("deathsButton"));
        } catch (NoSuchElementException e) {
            throw new AssertionError("Chart doesn't have the essential buttons to change statistics");
        }

    }


}
