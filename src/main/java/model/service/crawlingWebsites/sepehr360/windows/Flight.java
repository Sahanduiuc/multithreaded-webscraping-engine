package model.service.crawlingWebsites.sepehr360.windows;

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



        WebElement searchResults=driver.findElement(By.xpath("//*[@id=\"flightList\"]/div[2]"));
        Thread.sleep(1000);
        System.out.println("searchResults.isDisplayed()="+searchResults.isDisplayed());

        List<WebElement> all=searchResults.findElements(By.cssSelector(".col-lg-12.col-md-12.col-sm-12.col-xs-12.ng-scope"));
        Thread.sleep(6000);
        while (all.size()<1) {
            System.out.println("all.size()=" + all.size());
            all = searchResults.findElements(By.cssSelector(".col-lg-12.col-md-12.col-sm-12.col-xs-12.ng-scope"));
        }
        System.out.println("number of flights=" + all.size());

        int count=0;
        for (WebElement e:all
             ) {
            count++;
            if (count>1) {


                System.out.println("count=" + count);
                Result result = new Result();
                // String[] splited = startFlightNumber.getText().split("\\s+");


                //departureStartTime
                List<WebElement> departureStartTime = e.findElements(By.xpath("div/div/div[1]/div[3]/div[1]"));
                Thread.sleep(1000);
                if (departureStartTime.size() > 0) {
                    System.out.println("departureStartTime=" + departureStartTime.get(0).getText());
                    result.setDepartureStartTime(departureStartTime.get(0).getText());
                }

                //arrivalStartTime
                List<WebElement> arrivalStartTime = e.findElements(By.xpath("div/div/div[1]/div[5]/div[1]"));
               Thread.sleep(1000);
               if (arrivalStartTime.size()>0){
                   System.out.println("arrivalStartTime.getText()=" + arrivalStartTime.get(0).getText());
                   result.setArrivalStartTime(arrivalStartTime.get(0).getText());
               }


                //startAirplaneType
                List<WebElement> startAirplaneType=e.findElements(By.xpath("div/div/div[2]/div/div[1]/div[1]/div[2]/span[2]"));
               Thread.sleep(1000);
                if (startAirplaneType.size()>0) {
                    System.out.println("startAirplaneType.getText()=" + startAirplaneType.get(0).getText());
                    result.setStartAirplaneType(startAirplaneType.get(0).getText());
                }
                //startAirline
                List<WebElement> startAirline=e.findElements(By.xpath("div/div/div[1]/div[2]/div/div[2]/div[1]"));
                if (startAirline.size()>0) {
                    System.out.println("startAirline.getText()=" + startAirline.get(0).getText());
                    result.setStartAirline(startAirline.get(0).getText());
                }
                //startFlightNumber
                List<WebElement> startfFlightNumber=e.findElements(By.xpath("div/div/div[2]/div/div[1]/div[1]/div[1]/span[2]"));
                if (startfFlightNumber.size()>0) {
                    System.out.println("startfFlightNumber.getText()=" + startfFlightNumber.get(0).getText());
                    result.setStartFlightNumber(startfFlightNumber.get(0).getText());
                }
                //startSellingType

                //startPriceClass


                //capacity


                //price
                //*[@id="list"]/div[2]/div[1]/div/div[1]/div[1]/table/tr/td[1]
                List<WebElement> price = e.findElements(By.xpath("div/div/div[1]/div[1]/div/span"));
                if (price.size()>0) {
                    System.out.println("price.getText()=" + price.get(0).getText());
                    result.setPrice(price.get(0).getText());
                }
                listOfResults.add(result);
            }
        }
        Thread.sleep(2000);
        return listOfResults;
    }




    //driver.close();
}
