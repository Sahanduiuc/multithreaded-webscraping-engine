package model.service.crawlingWebsites.zoraq.windows;

import model.common.Util;
import model.service.crawlingWebsites.zoraq.windows.Result;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

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
     *
     *
     * @throws InterruptedException the interrupted exception
     */
    public List<Result>  call() throws InterruptedException {
        List<Result> listOfResults=new ArrayList<Result>();

        JavascriptExecutor js=(JavascriptExecutor) driver;

        this.delay=delay;
        Util expandShadow=new Util(driver);

        Thread.sleep(this.delay);


        List<WebElement> ngscope=driver.findElements(By.xpath("//*[@id=\"domestic-app\"]/div/div/div[3]/div/div[2]/div[3]"));
        //Thread.sleep(2000);
        //wait.until(ExpectedConditions.visibilityOf(ngscope.get(0)));
        while (ngscope.size()<1){
            ngscope=driver.findElements(By.xpath("//*[@id=\"domestic-app\"]/div/div/div[3]/div/div[2]/div[3]"));
            Thread.sleep(1000);
        }
        System.out.println("ngscope="+ngscope.get(0).isDisplayed());

        List<WebElement> ngrepeats=ngscope.get(0).findElements(By.cssSelector(".col-xs-12.available-columns.inner-available.trans-fade-in"));
        while (ngrepeats.size()<1){
            ngrepeats=ngscope.get(0).findElements(By.cssSelector(".col-xs-12.available-columns.inner-available.trans-fade-in"));
        }

        Thread.sleep(3000);
        System.out.println("ngrepeats.size()="+ngrepeats.size());
        int count=0;
        for (WebElement e:ngrepeats
             ) {
            count++;


            System.out.println("count="+count);
           Result result=new Result();

            // String[] splited = startFlightNumber.getText().split("\\s+");


            //departureStartTime
            WebElement departureStartTime=e.findElement(By.xpath("div[3]/div[1]/div[2]/div[2]/div[1]/strong[2]"));
            Thread.sleep(1000);
            System.out.println("departureStartTime.getText()="+departureStartTime.getText());
            result.setDepartureStartTime(departureStartTime.getText());


            //startAirline
            WebElement startAirline=e.findElement(By.xpath("div[3]/div[1]/div[1]/div/span"));
            Thread.sleep(1000);
            System.out.println("startAirline.getText()="+startAirline.getText());
            result.setStartAirline(startAirline.getText());

            //arrivalStartTime


            //startAirplaneType
            WebElement startAirplaneType=e.findElement(By.xpath("div[3]/div[1]/div[2]/div[1]/div[1]/span[1]"));
            Thread.sleep(1000);
            System.out.println("startAirplaneType="+startAirplaneType.getText());
            result.setStartAirplaneType(startAirplaneType.getText());
            //startFlightNumber

            //startSellingType

            //startPriceClass
            List<WebElement> startPriceClass=e.findElements(By.xpath("div[3]/div[1]/div[2]/div[1]/div[1]/span[2]"));
            if (startPriceClass.size()>0){
                Thread.sleep(1000);
                System.out.println("startPriceClass.getText()="+startPriceClass.get(0).getText());
                result.setStartPriceClass(startPriceClass.get(0).getText());
            }



            //capacity


            //price
            WebElement price=e.findElement(By.xpath("footer/div[1]/span[1]/span"));
            System.out.println("price.getText()="+price.getText());
            result.setPrice(price.getText());

            listOfResults.add(result);






/*
            //click on details to see modal
            WebElement details=e.findElement(By.xpath("div[3]/div[1]/span[2]"));
            Thread.sleep(1000);
            if (details.isDisplayed()){
                details.click();
                System.out.println("clicked on details----------------");
                Thread.sleep(3000);
            }
*/





        }
        return listOfResults;





        //driver.close();
    }
}
