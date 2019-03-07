package model.service.crawlingWebsites.flytoday.windows;

import model.common.Util;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

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
        this.delay=6000;
        Util expandShadow=new Util(driver);
        Thread.sleep(this.delay);
        List<WebElement> searchResults=driver.findElements(By.xpath("/html/body/div[2]/div[3]/div[2]/div[2]"));
        Thread.sleep(1000);
        while (searchResults.size()<1){
            searchResults=driver.findElements(By.xpath("/html/body/div[2]/div[3]/div[2]/div[2]"));
            Thread.sleep(1000);
            System.out.println("searchResults.isDisplayed()="+searchResults.get(0).isDisplayed());
        }

        List<WebElement> all=searchResults.get(0).findElements(By.cssSelector(".row.c-flight-search-result"));
        Thread.sleep(6000);
        while (all.size()<1) {
            System.out.println("all.size()=" + all.size());
            all = searchResults.get(0).findElements(By.cssSelector(".row.c-flight-search-result"));
            Thread.sleep(1000);
        }
        System.out.println("number of flights=" + all.size());

        int count=0;
        for (WebElement e:all
             ) {
                count++;
                System.out.println("count=" + count);
                Result result = new Result();
                // String[] splited = startFlightNumber.getText().split("\\s+");
                //departureStartTime
                List<WebElement> departureStartTime = e.findElements(By.xpath("div/div[1]/div[1]/div[2]/div[3]/div/div[3]"));
                Thread.sleep(1000);
                if (departureStartTime.size() > 0) {
                    System.out.println("departureStartTime=" + departureStartTime.get(0).getText().split("-")[0]);
                    result.setDepartureStartTime(departureStartTime.get(0).getText().split("-")[0]);
                }
                //arrivalStartTime
                WebElement arrivalStartTime = e.findElement(By.xpath("div/div[1]/div[1]/div[2]/div[4]/div/div[3]"));
                System.out.println("arrivalStartTime.getText()=" + arrivalStartTime.getText().split("-")[0]);
                result.setArrivalStartTime(arrivalStartTime.getText().split("-")[0]);

                //startFlightNumber
                WebElement startfFlightNumber=e.findElement(By.xpath("div/div[1]/div[1]/div[2]/div[2]/div/span"));
                System.out.println("startfFlightNumber.getText()="+startfFlightNumber.getText());
                result.setStartFlightNumber(startfFlightNumber.getText());


                //startAirline
                WebElement startAirline=e.findElement(By.xpath("div/div[1]/div[1]/div[2]/div[2]/p"));
                System.out.println("startAirline.getText()="+startAirline.getText());
                result.setStartAirline(startAirline.getText());



                //startAirplaneType


                //startSellingType

                //startPriceClass


                //capacity


                //price
                //*[@id="list"]/div[2]/div[1]/div/div[1]/div[1]/table/tr/td[1]
                WebElement price = e.findElement(By.xpath("div/div[1]/div[2]/div/div/p[2]/span[1]"));
                System.out.println("price.getText()=" + price.getText());
                result.setPrice(price.getText());

                listOfResults.add(result);

        }
        Thread.sleep(2000);
        return listOfResults;
    }




    //driver.close();
}
