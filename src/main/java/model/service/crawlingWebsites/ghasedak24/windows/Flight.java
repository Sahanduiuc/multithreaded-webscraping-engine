package model.service.crawlingWebsites.ghasedak24.windows;

import model.common.Util;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

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


    public  List<Result> call() throws InterruptedException, ExecutionException {

        List<Result> listOfResults=new ArrayList<Result>();
        boolean resolved=false;

        JavascriptExecutor js=(JavascriptExecutor) driver;
        this.delay=5000;
        Util expandShadow=new Util(driver);
        Thread.sleep(this.delay);

        WebElement searchResults=driver.findElement(By.xpath("//*[@id=\"search-results\"]/div[2]/div/div[2]/div"));
        System.out.println("searchResults.isDisplayed()="+searchResults.isDisplayed());

        List<WebElement> all=searchResults.findElements(By.cssSelector(".panel"));
        System.out.println("all.size()="+all.size());

        for (WebElement e:all
             ) {
            Result result=new Result();

            WebElement startFlightNumber=e.findElement(By.xpath("div/div/div[2]/p"));
            String[] splited = startFlightNumber.getText().split("\\s+");
            System.out.println("startAirline="+splited[0]);
            System.out.println("setStartFlightNumber="+splited[1]);
            System.out.println("setBusinessType="+splited[2]);

            result.setStartAirline(splited[0]);
            result.setStartFlightNumber(splited[1]);
            result.setBusinessType(splited[2]);
            Thread.sleep(1000);

            //departureStartTime
            WebElement departureStartTime=e.findElement(By.xpath("div/div/div[3]/p"));
            System.out.println("departureStartTime.getText()="+departureStartTime.getText().split("\\s+")[1]);
            result.setDepartureStartTime(departureStartTime.getText().split("\\s+")[1]);

            //capacity
            WebElement capacity=e.findElement(By.xpath("div/div/div[4]/p"));
            System.out.println("capacity="+capacity.getText().split("\\s+")[1]);
            result.setStartCapacity(capacity.getText().split("\\s+")[1]);

            //price
            WebElement price=e.findElement(By.xpath("div/div/div[5]/p"));
            System.out.println("priceclass="+price.getText().split("\\s+")[0]);
            System.out.println("price="+price.getText().split("\\s+")[1]);
            result.setPrice(price.getText().split("\\s+")[1]);
            result.setStartPriceClass(price.getText().split("\\s+")[0]);
            listOfResults.add(result);

        }
        Thread.sleep(2000);
        return listOfResults;
    }




    //driver.close();
}
