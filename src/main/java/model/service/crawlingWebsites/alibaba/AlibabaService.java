package model.service.crawlingWebsites.alibaba;

import model.common.Fields;
import model.common.Spider;
import model.da.aerospike.FlightDA;
import model.service.crawlingWebsites.alibaba.windows.Flight;
import model.service.crawlingWebsites.alibaba.windows.Result;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

//import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * The type Wego service.
 */
public class AlibabaService {


    private String url;
    private int delay;
    private Fields fields;
    private WebDriver driver;
    private String selectedDriver;

    public AlibabaService(String url, int delay, Fields fields) {

        this.url=url;
        this.delay=delay;
        this.fields=fields;

    }


    public String getSelectedDriver() {
        return selectedDriver;
    }

    public void setSelectedDriver(String selectedDriver) {
        this.selectedDriver = selectedDriver;
    }

    /**
     * Main.
     */

    public void main() throws ExecutionException {



        final Spider alibaba=new Spider(this.selectedDriver,this.url,this.delay,fields);

        final File file;

        final BufferedWriter out;

        final JSONObject alibabaJson =new JSONObject();

        try {
           // driver.manage().window().setPosition(new Point(-2000,0));
           // WebDriverWait wait = new WebDriverWait(driver,10);

            alibaba.doSyncProcess(()->{
                driver=alibaba.getDriver();

                JavascriptExecutor js=(JavascriptExecutor) driver;

                //twoWay-----------------------------------------------------

                WebElement twoWay=driver.findElement(By.xpath("//*[@id=\"search-panels\"]/div[2]/div/div[2]/div/div/div/form/div[1]/div[2]/label"));
                Thread.sleep(1000);
                twoWay.click();
                Thread.sleep(1000);



                //departureLocation--------------------------------------
                WebElement departureLocation=driver.findElement(By.xpath("//*[@id=\"origin-input-domestic\"]"));
                Thread.sleep(1000);
                departureLocation.sendKeys(fields.getDepartureLocation());
                Thread.sleep(1000);
                Thread.sleep(1000);
                departureLocation.sendKeys(Keys.ENTER);
                Thread.sleep(1000);

                //arrivalLocation----------------------------------------
                WebElement arrivalLocation=driver.findElement(By.xpath("//*[@id=\"destination-input-domestic\"]"));
                arrivalLocation.sendKeys(fields.getArrivalLocation());
                Thread.sleep(1000);
                arrivalLocation.sendKeys(Keys.ENTER);
                Thread.sleep(1000);

                //departure date-------------------------------------------
                WebElement departureDate=driver.findElement(By.xpath("//*[@id=\"domestic-departing\"]"));
                departureDate.click();
                Thread.sleep(1000);
                WebElement e=driver.switchTo().activeElement();
                Thread.sleep(1000);
                WebElement adpmain=e.findElement(By.cssSelector(".adpmain.persian.two-months"));
                System.out.println("ee="+adpmain.isDisplayed());
                WebElement adpmain1=adpmain.findElement(By.xpath("table/tbody/tr[2]/td/table[1]/tbody/tr["+fields.getRow()+"]/td["+fields.getColumn()+"]"));
                adpmain1.click();


                //returnDate-------------------------------------------------
                WebElement returnDate=driver.findElement(By.xpath("//*[@id=\"domestic-returning\"]"));
                returnDate.click();
                Thread.sleep(1000);
                WebElement e2=driver.switchTo().activeElement();
                Thread.sleep(1000);
                WebElement arrivaladpmain=e2.findElement(By.cssSelector(".adpmain.persian.two-months"));
                System.out.println("arrivaladpmain="+arrivaladpmain.isDisplayed());
                WebElement arrivaladpmain1=arrivaladpmain.findElement(By.xpath("table/tbody/tr[2]/td/table[2]/tbody/tr["+fields.getReturnRow()+"]/td["+fields.getReturnColumn()+"]"));
                arrivaladpmain1.click();


                //search-----------------------------------------------
                WebElement search=driver.findElement(By.xpath("//*[@id=\"submit-domestic-search\"]"));
                search.submit();
                Thread.sleep(5000);


                //-----redirect to new page for flights and hotels--------
                Set<String> allWindows=driver.getWindowHandles();
                System.out.println("opening "+allWindows.size()+" pages to crawl");
                System.out.println("----- form submitted---------");

                int windowCount=0;
                for(String child:allWindows)
                {
                    windowCount++;

                    if (windowCount==1){

                        driver.switchTo().window(child);
                        ExecutorService exec = Executors.newSingleThreadExecutor();
                        Future<List<Result>> ali = exec.submit( new Flight(driver));
                        List<Result> alibabaData;
                        alibabaData=ali.get();
                        //database layer starts here------------------
                        FlightDA flightDA=new FlightDA("irsa","flights");
                        for (int i=0;i<alibabaData.size();i++){
                            System.out.println("alibabaData.get("+i+").getPrice()="+alibabaData.get(i).getPrice()); //Waits until the task is done, then prints 1
                            flightDA.addFlight(alibabaData.get(i).getStartAirline(),alibabaData.get(i).getDepartureStartTime(),
                                    alibabaData.get(i).getBusinessType(),alibabaData.get(i).getPrice(),
                                    alibabaData.get(i).getStartFlightNumber(),alibabaData.get(i).getStartWeighLimit(),
                                    alibabaData.get(i).getStartPriceClass(),
                                    alibabaData.get(i).getTimeStamp(),"alibaba",
                                    null);
                        }
                        flightDA.closeAerospikeConnection();
                        Thread.sleep(1000);
                        driver.close();




                        //driver.close();

                    }
                    else{

                    }

                }



            });


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
