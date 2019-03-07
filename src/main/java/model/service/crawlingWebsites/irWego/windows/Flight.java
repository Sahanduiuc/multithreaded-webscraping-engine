package model.service.crawlingWebsites.irWego.windows;

import model.common.Util;
import model.service.crawlingWebsites.irWego.Result;
import model.service.crawlingWebsites.irWego.WegoService;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.swing.text.html.parser.TagElement;
import javax.xml.ws.WebEndpoint;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * The type Flight.
 */
public class Flight implements Callable<List<Result>> {

    private WebDriver driver;
    private WebDriverWait wait ;
    private int delay;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {

    }

    /**
     * Instantiates a new Flight.
     *
     * @param driver the driver
     */
    public Flight(WebDriver driver){
        this.driver=driver;
        wait = new WebDriverWait(driver,10);


    }

    /**
     * Index.
     *
     * @throws InterruptedException the interrupted exception
     */
    public  List<Result> call() throws InterruptedException, ExecutionException {

        List<Result> listOfResults=new ArrayList<Result>();
        JavascriptExecutor js=(JavascriptExecutor) driver;

        this.delay=delay;
        Util expandShadow=new Util(driver);
        Thread.sleep(this.delay);
        WebElement pages= (WebElement) js.executeScript("return document.getElementById('app').shadowRoot.getElementById('pages');");
        //wait.until(ExpectedConditions.visibilityOf(pages));
        while (!pages.isDisplayed()){
            Thread.sleep(1000);
            pages= (WebElement) js.executeScript("return document.getElementById('app').shadowRoot.getElementById('pages');");
        }


        WebElement flightSearch= (WebElement) js.executeScript("return arguments[0].querySelector('#flights-search');",pages);
        System.out.println("f="+flightSearch.isDisplayed());
        WebElement contentLayoutHorizontal= (WebElement) js.executeScript("return arguments[0].shadowRoot.querySelector('.content.layout.horizontal');",flightSearch);
        WebElement resultsContainerFlex=contentLayoutHorizontal.findElement(By.cssSelector(".results-container.flex"));
        WebElement flightResultList=resultsContainerFlex.findElement(By.id("flightResultList"));
        WebElement ironList= (WebElement) js.executeScript("return arguments[0].shadowRoot.querySelector('#listview');",flightResultList);
        Thread.sleep(1000);
        List<WebElement> flights=ironList.findElements(By.cssSelector(".group"));
        Thread.sleep(2000);
        while (flights.size()<1){
            flights=ironList.findElements(By.cssSelector(".group"));
            Thread.sleep(2000);
            System.out.println("in while---------");
        }
        System.out.println("number of flights="+flights.size());
        for (WebElement f:flights
             ) {
            Result result=new Result();

            System.out.println("----------------------");
            WebElement flightCard=f.findElement(By.tagName("flight-card"));
            WebElement shadow1= expandShadow.expandRootElement(flightCard);
            WebElement card=shadow1.findElement(By.cssSelector(".card"));
            //card trip
            List<WebElement> startAirlineCompanyName=card.findElements(By.cssSelector(".card-trip >div:nth-child(1) >.airline > span "));
            System.out.println("startAirlineCompanyName="+startAirlineCompanyName.get(0).getText());
            result.setStartAirline(startAirlineCompanyName.get(0).getText());

            List<WebElement> startTime=card.findElements(By.cssSelector(".card-trip >div:nth-child(1) >.time >  div:nth-child(1) "));
            System.out.println("startTime="+startTime.get(0).getText());
            result.setDepartureStartTime(startTime.get(0).getText());

            List<WebElement> returnAirlineCompanyName=card.findElements(By.cssSelector(".card-trip >div:nth-child(2) >.airline > span "));
            if (returnAirlineCompanyName.size()>0)
            {
                System.out.println("returnAirlineCompanyName="+returnAirlineCompanyName.get(0).getText());
            }

            List<WebElement> returnTime=card.findElements(By.cssSelector(".card-trip >div:nth-child(2) >.time >  div:nth-child(1) "));
            if (returnTime.size()>0) {
                System.out.println("returnTime=" + returnTime.get(0).getText());
            }
            WebElement cardPrice=card.findElement(By.cssSelector(".card-price"));
            WebElement wegoPrice=cardPrice.findElement(By.tagName("wego-price"));
            WebElement shadow2=expandShadow.expandRootElement(wegoPrice);
            WebElement price=shadow2.findElement(By.id("price"));
            System.out.println("price="+price.isDisplayed());
            result.setPrice(price.getText());
            WebElement priceText=price.findElement(By.cssSelector(".price-text"));
            String  amount=priceText.findElement(By.cssSelector("span.amount")).getText();
            System.out.println("amount="+amount);
            listOfResults.add(result);
        }
        //driver.close();
        return listOfResults;
    }
}
