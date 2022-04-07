package io.cucumber.skeleton;

import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Format;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookSearchSteps {

    Library library = new Library();
    List<Book> result = new ArrayList<>();

    @ParameterType("([0-9]{4})-([0-9]{2})-([0-9]{2})")
    public LocalDate isoDate(String year, String month, String day){
        return LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
    }

    @Given("a book with the title '{string}', written by '{string}', published in {isoDate}")
    public void addNewBook(final String title, final String author, final LocalDate published) {
        Book book = new Book(title, author, java.sql.Date.valueOf(published));
        library.addBook(book);
    }

    @When("the customer searches for books published between {int} and {int}")
    public void setSearchParameters(final int from, final int to) {
        result = library.findBooks(Calendar.getInstance().set(Calendar.YEAR, from), new Date());
    }

    @Then("{int} books should have been found")
    public void verifyAmountOfBooksFound(final int booksFound) {
        assertEquals(booksFound, result.size());
    }

    @Then("Book {int} should have the title {string}")
    public void verifyBookAtPosition(final int position, final String title) {
        assertEquals(title, result.get(position - 1).getTitle());
    }


    /*
    @Given("I have {int} cukes in my belly")
    public void I_have_cukes_in_my_belly(int cukes) {
        Belly belly = new Belly();
        belly.eat(cukes);
    }
    @When("I wait {int} hour")
    public void i_wait_hour(int hours){
        Belly belly = new Belly();
        belly.wait(hours);
    }
    @Then("my belly should growl")
    public void my_belly_should_growl() {
        Belly belly = new Belly();
        belly.growl();
    }

    */
}
