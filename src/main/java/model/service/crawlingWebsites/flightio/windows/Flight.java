package model.service.crawlingWebsites.flightio.windows;

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
        List<WebElement> pageLoaded=driver.findElements(By.xpath("//*[@id=\"departior-container\"]/div/div/div[1]/p"));

        while((pageLoaded.size()<1)){
             pageLoaded=driver.findElements(By.xpath("//*[@id=\"departior-container\"]/div/div/div[1]/p"));
            Thread.sleep(1000);
             System.out.println("---------waiting to load page in flightio------------");
        }


        WebElement searchResults=driver.findElement(By.xpath("//*[@id=\"Main_Container\"]/div[1]/div[2]/div/div[2]/div[3]"));
        Thread.sleep(1000);
        System.out.println("searchResults.isDisplayed()="+searchResults.isDisplayed());

        List<WebElement> all=searchResults.findElements(By.cssSelector(".search-result.flights-boxs.depart.flat-card"));
        Thread.sleep(2000);
        System.out.println("all.size()="+all.size());

        for (WebElement e:all
             ) {
            Result result=new Result();
            // String[] splited = startFlightNumber.getText().split("\\s+");

            //startFlighNumber
            WebElement startFlighNumber=e.findElement(By.xpath("div[2]/div[1]/div/div[1]/span[2]"));
            Thread.sleep(1000);
            System.out.println("startFlighNumber="+startFlighNumber.getText());
            result.setStartFlightNumber(startFlighNumber.getText());

            //departureStartTime
            WebElement departureStartTime=e.findElement(By.xpath("div[2]/div[1]/div/div[2]/div[1]/span[1]"));
            Thread.sleep(1000);
            System.out.println("departureStartTime="+departureStartTime.getText());
            result.setDepartureStartTime(departureStartTime.getText());


            //departureStartTime
            List<WebElement> arrivalStartTime=e.findElements(By.xpath("div[2]/div[1]/div/div[2]/div[3]/span[1]"));
            Thread.sleep(1000);
            if (arrivalStartTime.size()<1)
            {
                System.out.println("arrivalStartTime="+arrivalStartTime.get(0).getText());
                result.setArrivalStartTime(arrivalStartTime.get(0).getText());
            }


            //startSellingType
            List<WebElement> startSellingType=e.findElements(By.xpath("div[2]/div[1]/div/div[4]/span[1]"));
            Thread.sleep(1000);
            if (startSellingType.size()>0)
            {
                System.out.println("startSellingType="+startSellingType.get(0).getText());
                result.setStartSellingType(startSellingType.get(0).getText());
            }

            //startCancelType
            List<WebElement> startCancelType=e.findElements(By.xpath("div[2]/div[1]/div/div[4]/span[2]"));
            Thread.sleep(1000);
            if (startCancelType.size()>0){
                System.out.println("startCancelType="+startCancelType.get(0).getText());
                result.setStartCancelType(startCancelType.get(0).getText());
            }

            //startPriceClass
            List<WebElement> startPriceClass=e.findElements(By.xpath("div[2]/div[1]/div/div[4]/span[3]"));
            Thread.sleep(1000);
            if (startPriceClass.size()>0){
                System.out.println("startPriceClass="+startPriceClass.get(0).getText());
                result.setStartPriceClass(startPriceClass.get(0).getText());
            }


            //capacity
            List<WebElement> startCapacity=e.findElements(By.xpath("div[2]/div[1]/div/div[4]/span[4]"));
            Thread.sleep(1000);
            if (startCapacity.size()>0)
            {
                System.out.println("startCapacity="+startCapacity.get(0).getText());
                result.setStartCapacity(startCapacity.get(0).getText());
            }

            //price
            List<WebElement> price=e.findElements(By.xpath("div[2]/div[2]/div/span[2]"));
            Thread.sleep(1000);
            if (price.size()>0) {
                System.out.println("price.getText()=" + price.get(0).getText());
                result.setPrice(price.get(0).getText());
            }

            listOfResults.add(result);

        }
        Thread.sleep(2000);
        return listOfResults;
    }




    //driver.close();
}
