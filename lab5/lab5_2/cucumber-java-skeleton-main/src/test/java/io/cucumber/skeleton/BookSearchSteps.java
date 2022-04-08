package io.cucumber.skeleton;

import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.hu.De;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Format;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookSearchSteps {

    Library library = new Library();
    List<Book> result = new ArrayList<>();

    @ParameterType("([0-9]{4})-([0-9]{2})-([0-9]{2})")
    public Date isodate(String year, String month, String day) {
        Calendar date = Calendar.getInstance();
        date.set(Integer.parseInt(year), (Integer.parseInt(month))-1, Integer.parseInt(day));
        return date.getTime();
    }

    @Given("another book with the title {string}, written by {string}, published in {isodate}")
    @Given("a book with the title {string}, written by {string}, published in {isodate}")
    public void addNewBook(String title, String author, Date date) {
        Book book = new Book(title, author,date);
        library.addBook(book);
    }

    @When("the customer searches for books published between {int} and {int}")
    public void setSearchParameters(final int from, final int to) {
        Calendar fromCal = Calendar.getInstance();
        fromCal.set(from, Calendar.JANUARY, 1);
        Calendar toCal = Calendar.getInstance();
        toCal.set(to, Calendar.DECEMBER, 31);
        result = library.findBooks(fromCal.getTime(), toCal.getTime());
    }

    @Then("{int} books should have been found")
    public void verifyAmountOfBooksFound(final int booksFound) {
        assertEquals(booksFound, result.size());
    }

    @Then("Book {int} should have the title {string}")
    public void verifyBookAtPosition(final int position, final String title) {
        assertEquals(title, result.get(position - 1).getTitle());
    }
}
