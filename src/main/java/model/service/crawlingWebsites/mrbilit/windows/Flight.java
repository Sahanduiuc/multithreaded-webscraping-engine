package model.service.crawlingWebsites.mrbilit.windows;

import model.common.Util;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
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



        WebElement searchResults=driver.findElement(By.xpath("//*[@id=\"mainTable\"]/div"));
        Thread.sleep(1000);
        System.out.println("searchResults.isDisplayed()="+searchResults.isDisplayed());

        List<WebElement> all=searchResults.findElements(By.cssSelector(".wrapper.mainRow"));
        Thread.sleep(6000);
        while (all.size()<1){
            System.out.println("all.size() in mrbilit="+all.size());
            all=searchResults.findElements(By.cssSelector(".wrapper.mainRow"));
            Thread.sleep(1000);
        }
        System.out.println("number of flights="+all.size());
        int count=0;
        for (WebElement e:all
             ) {
            count++;
            System.out.println("count="+count);
            Result result=new Result();
            // String[] splited = startFlightNumber.getText().split("\\s+");


            //departureStartTime

            List<WebElement> departureStartTime=e.findElements(By.xpath("div[1]/div[1]/div[2]/div[2]/div/span[1]"));
            Thread.sleep(1000);
            if (departureStartTime.size()>0){
                System.out.println("departureStartTime="+departureStartTime.get(0).getText().split("\\s+")[0]);
                result.setDepartureStartTime(departureStartTime.get(0).getText());
            }


            //startAirplaneType
            List<WebElement> startAirplaneType=e.findElements(By.xpath("div[1]/div[1]/div[4]"));
            Thread.sleep(2000);
            if (startAirplaneType.size()>0) {
                System.out.println("startAirplaneType=" + startAirplaneType.get(0).getText().split("\\|")[0]);
                result.setStartAirplaneType(startAirplaneType.get(0).getText().split("\\|")[0]);
            }
            //startFlightNumber
            List<WebElement> startFlightNumber=e.findElements(By.xpath("div[1]/div[1]/div[2]/div[1]/span[2]"));
            Thread.sleep(1000);
            if (startFlightNumber.size()>0) {
                System.out.println("startFlightNumber=" + startFlightNumber.get(0).getText());
                result.setStartFlightNumber(startFlightNumber.get(0).getText());
            }
            //startSellingType
            List<WebElement> startSellingType=e.findElements(By.xpath("div[1]/div[1]/div[4]"));
            Thread.sleep(1000);
            if (startSellingType.size()>0) {

                System.out.println("startSellingType=" + startSellingType.get(0).getText().split("\\|")[1]);
                result.setStartSellingType(startSellingType.get(0).getText().split("\\|")[1]);
            }
            //startPriceClass


            //capacity
            List<WebElement> startCapacity=e.findElements(By.xpath("div[1]/div[1]/div[4]"));
            Thread.sleep(1000);
            if (startCapacity.size()>0) {
                System.out.println("startCapacity=" + startCapacity.get(0).getText().split("\\|")[2]);
                result.setStartCapacity(startCapacity.get(0).getText().split("\\|")[2]);
            }

            //price
            List<WebElement> priceMenu=e.findElements(By.xpath("div[2]/div"));
            Thread.sleep(2000);
            if (priceMenu.size()>3) {
                System.out.println("price="+priceMenu.get(1).getText());
                result.setPrice(priceMenu.get(1).getText());
               // System.out.println("price=" + price.getText());
            }else{
                System.out.println("price="+priceMenu.get(0).getText());
                result.setPrice(priceMenu.get(0).getText());
            }

            listOfResults.add(result);

        }
        Thread.sleep(2000);
        return listOfResults;
    }




    //driver.close();
}
