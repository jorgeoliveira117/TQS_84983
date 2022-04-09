/*
 * (C) Copyright 2020 Boni Garcia (https://bonigarcia.github.io/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package io.github.bonigarcia;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.NoSuchElementException;

public class BlazeSteps {

    private WebDriver driver;

    @When("I navigate to {string}")
    public void iNavigateTo(String url) {
        driver = WebDriverManager.chromedriver().create();
        driver.get(url);
    }

    @And("I choose {string} as the departure city and {string} as my destination")
    public void iChooseAsTheDepartureCityAndAsMyDestination(String dep, String dest) {
        // select Departure city
        driver.findElement(By.name("fromPort")).click();
        driver.findElement(By.cssSelector("option[value=" + dep + "]")).click();
        driver.findElement(By.name("toPort")).click();
        driver.findElement(By.cssSelector("option[value=" + dest + "]")).click();

    }

    @And("I click the Find Flights button")
    public void iClickTheButton() {
        driver.findElement(By.cssSelector(".btn-primary")).click();
    }

    @Then("I should be shown a page with {string}")
    public void iShouldBeShownAPageWith(String arg) {
        try {
            driver.findElement(
                    By.xpath("//*[contains(text(), '" + arg + "')]"));
        } catch (NoSuchElementException e) {
            throw new AssertionError(
                    "\"" + arg + "\" not available in results");
        } finally {
            driver.quit();
        }
    }

    @When("I click the {int} Choose This Flight button")
    public void iClickTheChooseThisFlightButton(int n) {
        driver.findElement(By.cssSelector("tr:nth-child(" + n + ") .btn")).click();
    }

}
