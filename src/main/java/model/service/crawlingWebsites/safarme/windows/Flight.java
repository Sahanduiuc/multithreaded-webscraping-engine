package model.service.crawlingWebsites.safarme.windows;

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
        List<WebElement> pageLoaded=driver.findElements(By.xpath("//*[@id=\"main-form-0\"]/div[1]/div[2]/div[2]/div[1]/div[4]"));
        while((pageLoaded.size()<1)){
             pageLoaded=driver.findElements(By.xpath("//*[@id=\"main-form-0\"]/div[1]/div[2]/div[2]/div[1]/div[4]"));
            System.out.println("---------waiting to load page------------");
        }



        List<WebElement> all=pageLoaded.get(0).findElements(By.xpath("//*[@id=\"divOneway\"]"));
        Thread.sleep(6000);
        while (all.size()<1){
            System.out.println("all.size() in safarme="+all.size());
            all=pageLoaded.get(0).findElements(By.xpath("//*[@id=\"divOneway\"]"));
            Thread.sleep(2000);
        }
        int count=0;
        for (WebElement e:all
             ) {
            count++;
            System.out.println("count="+count);
            Result result=new Result();
            // String[] splited = startFlightNumber.getText().split("\\s+");


            //departureStartTime
            WebElement departureStartTime=e.findElement(By.xpath("div/div[1]/div[1]"));
            Thread.sleep(1000);
            System.out.println("departureStartTime="+departureStartTime.getText().split("\\s+")[0]);
            result.setDepartureStartTime(departureStartTime.getText());


            //startAirplaneType
            List<WebElement> startAirplaneType=e.findElements(By.xpath("div/div[3]/div[1]/a"));
            Thread.sleep(2000);
            System.out.println("startAirplaneType="+startAirplaneType.get(0).getText());
            result.setStartAirplaneType(startAirplaneType.get(0).getText());

            //startFlightNumber
            List<WebElement> startFlightNumber=e.findElements(By.xpath("div/div[3]/div[2]"));
            Thread.sleep(1000);
            System.out.println("startFlightNumber="+startFlightNumber.get(0).getText());
            result.setStartFlightNumber(startFlightNumber.get(0).getText());

            //startSellingType
            WebElement startSellingType=e.findElement(By.xpath("div/div[3]/div[3]/a"));
            Thread.sleep(1000);
            System.out.println("startSellingType="+startSellingType.getText());
            result.setStartSellingType(startSellingType.getText());

            //startPriceClass
            List<WebElement> startPriceClass=e.findElements(By.xpath("div/div[3]/div[4]"));
            Thread.sleep(1000);
            if (startPriceClass.size()>0){
                System.out.println("startPriceClass="+startPriceClass.get(0).getText());
                result.setStartPriceClass(startPriceClass.get(0).getText());
            }

            //capacity

            //price
            WebElement price=e.findElement(By.xpath("div/div[4]/div[1]"));
            Thread.sleep(2000);
            System.out.println("price="+price.getText());
            result.setPrice(price.getText());

            listOfResults.add(result);

        }
        Thread.sleep(2000);
        return listOfResults;
    }




    //driver.close();
}
