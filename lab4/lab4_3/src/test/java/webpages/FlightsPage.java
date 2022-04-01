package webpages;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class FlightsPage {
    private WebDriver driver;

    @FindBy(xpath = "/html/body/div[2]/h3")
    private WebElement heading;

    @FindBy(xpath = "/html/body/div[2]/table/tbody/tr[3]/td[1]/input")
    private WebElement chooseThirdButton;

    //Constructor
    public FlightsPage (WebDriver driver){
        this.driver=driver;

        //Initialise Elements
        PageFactory.initElements(driver, this);
    }

    //We will use this boolean for assertion. To check if page is opened
    public boolean isPageOpened(){
        return heading.getText().toString().contains("Flights from");
    }

    public void clickOnFlight(){
        chooseThirdButton.click();
    }
}