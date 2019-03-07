package model.service.crawlingWebsites.alibaba.windows;

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

        WebElement container=driver.findElement(By.xpath("//*[@id=\"domestic-app\"]/div/div/div[3]"));
        System.out.println("container.isDisplayed()="+container.isDisplayed());
        Thread.sleep(1000);

        WebElement availablerowisCompleted=container.findElement(By.xpath("div/div[2]/div[3]"));
        System.out.println("availablerowisCompleted.isDisplayed()="+availablerowisCompleted.isDisplayed());

        List<WebElement> colxs12=availablerowisCompleted.findElements(By.cssSelector(".col-xs-12.available-columns.inner-available.trans-fade-in"));
        System.out.println("colxs12="+colxs12.size());
        //start ticket----------------------------------------------
        //*********************************************************
        int count=0;
        for (WebElement e:colxs12
             ) {
                Result result=new Result();
                count++;
                System.out.println("count="+count);

                //js.executeScript("window.scrollBy(0,150)", "");

                List<WebElement> startAirline=e.findElements(By.xpath("div[3]/div[1]/div[1]/div/span"));
                Thread.sleep(1000);
                if (startAirline.size()>0) {
                    System.out.println("startAirline.getText()=" + startAirline.get(0).getText());
                    result.setStartAirline(startAirline.get(0).getText());
                }

                List<WebElement> departureStartTime=e.findElements(By.xpath("div[2]/div[1]/div[2]/div[2]/div[1]/strong[2]"));
            //*[@id="domestic-app"]/div/div/div[3]/div/div[2]/div[3]/div[3]/div[2]/div[1]/div[2]/div[2]/div[1]/strong[2]
            if (departureStartTime.size()>0){
                System.out.println("departureStartTime.getText()="+departureStartTime.get(0).getText());
                result.setDepartureStartTime(departureStartTime.get(0).getText());

            }

            //*[@id="domestic-app"]/div/div/div[3]/div/div[2]/div[3]/div[3]/div[2]/div[1]/div[2]/div[1]/div[1]/span[2]
                List<WebElement> businessType=e.findElements(By.xpath("div[3]/div[1]/div[2]/div[1]/div"));
                if (businessType.size()>0) {
                    System.out.println("businessType.getText()=" + businessType.get(0).getText());
                    result.setBusinessType(businessType.get(0).getText());
                }

                List<WebElement> price=e.findElements(By.xpath("footer/div[1]/span/span"));
                if (price.size()>0) {
                    System.out.println("price.getText()=" + price.get(0).getText());
                    result.setPrice(price.get(0).getText());
                }
            //*[@id="domestic-app"]/div/div/div[3]/div/div[2]/div[3]/div[3]/div[2]/div[2]/div/div[2]/div/a/span
                List<WebElement> details=e.findElements(By.xpath("div[3]/div[2]/div/div[2]/div/a/span"));
                Thread.sleep(1000);
//*[@id="domestic-app"]/div/div/div[3]/div/div[2]/div[3]/div[3]/div[3]/div[2]/div/div[2]/div/a/span
                while(details.size()<1){
                    details=e.findElements(By.xpath("div[3]/div[2]/div/div[2]/div/a/span"));
                   Thread.sleep(3000);
                }

            js.executeScript("arguments[0].scrollIntoView()", details.get(0));
            wait.until(ExpectedConditions.elementToBeClickable(details.get(0)));
            js.executeScript("arguments[0].click();", details.get(0));
                List<WebElement> startFlightNumber=e.findElements(By.xpath("div[3]/div[2]/div[1]/div[1]/div/div/div[1]/div/div/div[1]/div[1]/span"));
                if (startFlightNumber.size()!=0){
                    System.out.println("startFlightNumber.getText()="+startFlightNumber.get(0).getText());
                    result.setStartFlightNumber(startFlightNumber.get(0).getText());
                }

                List<WebElement> startWeighLimit=e.findElements(By.xpath("div[3]/div[2]/div[1]/div[1]/div/div/div[1]/div/div/div[1]/div[2]/span"));
                if (startWeighLimit.size()!=0){
                    System.out.println("startWeighLimit.getText()="+startWeighLimit.get(0).getText());
                    result.setStartWeighLimit(startWeighLimit.get(0).getText());
                }

                List<WebElement> startPriceClass=e.findElements(By.xpath("div[3]/div[2]/div[1]/div[1]/div/div/div[1]/div/div/div[1]/div[3]/span"));
                if (startPriceClass.size()!=0){
                    System.out.println("startPriceClass.getText()="+startPriceClass.get(0).getText());
                    result.setStartPriceClass(startPriceClass.get(0).getText()); ;
                }

                /*
                //return ticket-----------------------------------------
                //*********************************************************

                if (!resolved) {
                    ExecutorService exec = Executors.newSingleThreadExecutor();

                    Future<List<Result2>> ali2 = exec.submit( new ReturnFlight(driver,e));
                    List<Result2> alibaba2=ali2.get();
                    System.out.println("alibaba2.size()="+alibaba2.size());
                    for (Result2 r:alibaba2
                         ) {
                        if (r.flag){
                            resolved=true;
                        }
                    }
                    result.setResult2(ali2.get());
                }
                */
                listOfResults.add(result);
        }
        Thread.sleep(2000);
        return listOfResults;
    }




    //driver.close();
}
