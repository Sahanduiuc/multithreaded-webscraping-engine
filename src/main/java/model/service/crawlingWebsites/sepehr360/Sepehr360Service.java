package model.service.crawlingWebsites.sepehr360;

import model.common.Fields;
import model.common.Spider;
import model.da.aerospike.FlightDA;
import model.service.crawlingWebsites.sepehr360.windows.Flight;
import model.service.crawlingWebsites.sepehr360.windows.Result;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
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
public class Sepehr360Service {


    private String url;
    private int delay;
    private Fields fields;
    //private HtmlUnitDriver driver;
    private WebDriver driver;
    private String selectedDriver;



    public Sepehr360Service(String url, int delay, Fields fields) {

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




        final Spider sepehr360=new Spider(this.selectedDriver,this.url,this.delay,fields);

        final File file;

        final BufferedWriter out;

        final JSONObject sepehr360Json =new JSONObject();

        try {
            sepehr360.doSyncProcess(()->{
                driver=sepehr360.getDriver();
                WebDriverWait wait = new WebDriverWait(driver,10);

                JavascriptExecutor js=(JavascriptExecutor) driver;
                //*[@id="onesignal-popover-cancel-button"]


                //departureLocation--------------------------------------
                WebElement predepartureLocation=driver.findElement(By.xpath("//*[@id=\"firstPageSource\"]"));
                Thread.sleep(1000);
                predepartureLocation.click();
                Thread.sleep(2000);
                predepartureLocation.sendKeys(fields.getDepartureLocation());
                Thread.sleep(1000);
                predepartureLocation.sendKeys(Keys.ARROW_DOWN);
                Thread.sleep(2000);
                predepartureLocation.sendKeys(Keys.ENTER);
                Thread.sleep(2000);

                //arrivalLocation----------------------------------------
                WebElement prearrivalLocation=driver.findElement(By.xpath("//*[@id=\"firstPageSearchlistDestination\"]/div/span/input"));
                prearrivalLocation.click();
                Thread.sleep(2000);
                prearrivalLocation.sendKeys(fields.getArrivalLocation());
                Thread.sleep(1000);
                prearrivalLocation.sendKeys(Keys.ARROW_DOWN);
                Thread.sleep(1000);
                prearrivalLocation.sendKeys(Keys.ENTER);
                Thread.sleep(1000);

                //departure date-------------------------------------------
               // WebElement departureDate=driver.findElement(By.xpath("/html/body/div[5]"));
                prearrivalLocation.sendKeys(Keys.TAB);
                Thread.sleep(1000);
                //System.out.println("departureDate.isDisplayed()="+departureDate.isDisplayed());
                //departureDate.click();
                Thread.sleep(2000);
                WebElement departureDateSelection=driver.findElement(By.xpath("//*[@id=\"ui-datepicker-div\"]/div[1]/table/tbody/tr["+fields.getRow()+"]/td["+fields.getColumn()+"]/a"));
                System.out.println("departureDateSelection.isDisplayed()="+departureDateSelection.isDisplayed());
                departureDateSelection.click();
                Thread.sleep(1000);

                //departureReturnTime-------------------------



                //search-----------------------------------------------
                WebElement search=driver.findElement(By.xpath("//*[@id=\"exTab1\"]/div[5]/div[2]/button"));
                search.click();

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
                        Thread.sleep(3000);
                        ExecutorService exec = Executors.newSingleThreadExecutor();
                        Future<List<Result>> sepehr = exec.submit( new Flight(driver));
                        List<Result> sepehr360Data;
                        sepehr360Data=sepehr.get();
                        //database layer starts here------------------

                        FlightDA flightDA=new FlightDA("irsa","flights");
                        for (int i=0;i<sepehr360Data.size();i++){
                            System.out.println("sepehr360Data.get("+i+").getPrice()="+sepehr360Data.get(i).getPrice());
                            flightDA.addFlight(sepehr360Data.get(i).getStartAirline(),sepehr360Data.get(i).getDepartureStartTime(),
                                    sepehr360Data.get(i).getBusinessType(),sepehr360Data.get(i).getPrice(),
                                    sepehr360Data.get(i).getStartFlightNumber(),sepehr360Data.get(i).getStartWeighLimit(),
                                    sepehr360Data.get(i).getStartPriceClass(),
                                    sepehr360Data.get(i).getTimeStamp(),"sepehr360",
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
