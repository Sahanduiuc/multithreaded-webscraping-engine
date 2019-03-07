package model.service.crawlingWebsites.simorgh24.windows;

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
        WebElement searchResults=driver.findElement(By.xpath("//*[@id=\"search-results\"]"));
        Thread.sleep(1000);
        System.out.println("searchResults.isDisplayed()="+searchResults.isDisplayed());

        List<WebElement> all=searchResults.findElements(By.cssSelector(".panel.panel-default.tkpnl.element-item"));
        Thread.sleep(6000);
        while (all.size()<1) {
            all=searchResults.findElements(By.cssSelector(".panel.panel-default.tkpnl.element-item"));
        }
        System.out.println("all.size()=" + all.size());

        System.out.println("number of flights=" + all.size());

        int count=0;
        for (WebElement e:all
             ) {
                count++;
                System.out.println("count=" + count);
                Result result = new Result();
                // String[] splited = startFlightNumber.getText().split("\\s+");
                //departureStartTime
                List<WebElement> departureStartTime = e.findElements(By.xpath("div/div/div[3]/div[4]/div[2]"));
                Thread.sleep(1000);
                if (departureStartTime.size() > 0) {
                    System.out.println("departureStartTime=" + departureStartTime.get(0).getText());
                    result.setDepartureStartTime(departureStartTime.get(0).getText());
                }
                //arrivalStartTime

                //startFlightNumber
                WebElement startfFlightNumber=e.findElement(By.xpath("div/div/div[3]/div[2]"));
                System.out.println("startfFlightNumber.getText()="+startfFlightNumber.getText().split(":")[1]);
                result.setStartFlightNumber(startfFlightNumber.getText().split(":")[1]);


                //startAirline
               WebElement startAirline=e.findElement(By.xpath("div/div/div[2]/p"));
               System.out.println("startAirline.getText()="+startAirline.getText());
               result.setStartAirline(startAirline.getText());

                //startAirplaneType


                //startSellingType
                WebElement startSellingType=e.findElement(By.xpath("div/div/div[1]"));
                System.out.println("startSellingType.getText()="+startSellingType.getText());
                result.setStartSellingType(startSellingType.getText());

                //startPriceClass
                WebElement startPriceClass=e.findElement(By.xpath("div/div/div[3]/div[3]"));
                System.out.println("startPriceClass.getText()="+startPriceClass.getText());
                result.setStartPriceClass(startPriceClass.getText());


                //capacity
                WebElement capacity=e.findElement(By.xpath("div/div/div[4]/p[2]"));
                System.out.println("capacity.getText()="+capacity.getText());
                result.setStartCapacity(capacity.getText());

                //price
                //*[@id="list"]/div[2]/div[1]/div/div[1]/div[1]/table/tr/td[1]
                WebElement price = e.findElement(By.xpath("div/div/div[4]/p[1]"));
                System.out.println("price.getText()=" + price.getText());
                result.setPrice(price.getText());

                listOfResults.add(result);

        }
        Thread.sleep(2000);
        return listOfResults;
    }




    //driver.close();
}
