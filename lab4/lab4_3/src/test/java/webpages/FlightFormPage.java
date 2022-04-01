package webpages;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class FlightFormPage {
    private WebDriver driver;

    @FindBy(tagName = "h2")
    WebElement heading;

    @FindBy(id="inputName")
    WebElement inputName;

    @FindBy(id = "address")
    WebElement address;

    @FindBy(id = "city")
    WebElement city;

    @FindBy(id = "state")
    WebElement state;

    @FindBy(id = "zipCode")
    WebElement zipCode;

    @FindBy(id ="rememberMe")
    WebElement rememberMe;

    @FindBy(xpath = "/html/body/div[2]/form/div[11]/div/input")
    WebElement purchaseButton;


    //Constructor
    public FlightFormPage(WebDriver driver){
        this.driver=driver;

        //Initialise Elements
        PageFactory.initElements(driver, this);
    }

    public void setInputName(String name){
        inputName.clear();
        inputName.sendKeys(name);
    }

    public void setAddress(String addressStr){
        address.clear();
        address.sendKeys(addressStr);
    }

    public void setCity(String cityStr){
        city.clear();
        city.sendKeys(cityStr);
    }

    public void  setState(String stateStr){
        state.clear();
        state.sendKeys(stateStr);
    }

    public void setZipCode (String zip){
        zipCode.clear();
        zipCode.sendKeys(zip);
    }

    public void setRememberMe (){
        rememberMe.click();
    }

    public void clickOnJoin(){
        purchaseButton.click();
    }
    public boolean isPageOpened(){
        //Assertion
        return heading.getText().toString().contains("Your flight from");
    }
}