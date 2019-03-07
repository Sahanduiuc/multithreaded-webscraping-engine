package model.service.crawlingWebsites.flytoday;

import model.common.Fields;
import model.common.Spider;
import model.da.aerospike.FlightDA;
import model.service.crawlingWebsites.flytoday.windows.Flight;
import model.service.crawlingWebsites.flytoday.windows.Result;
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
public class FlytodayService {


    private String url;
    private int delay;
    private Fields fields;
    //private HtmlUnitDriver driver;
    private String selectedDriver;
    private WebDriver driver;

    public FlytodayService(String url, int delay, Fields fields) {

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



        final Spider flytoday=new Spider(this.selectedDriver,this.url,this.delay,fields);

        final File file;

        final BufferedWriter out;

        final JSONObject flytodayJson =new JSONObject();

        try {
            flytoday.doSyncProcess(()->{
                driver=flytoday.getDriver();
                WebDriverWait wait = new WebDriverWait(driver,10);

                JavascriptExecutor js=(JavascriptExecutor) driver;


                //*[@id="Domestic"]
                WebElement internal=driver.findElement(By.xpath("//*[@id=\"Domestic\"]"));
                internal.click();

                //*[@id="Domestic_Oneway"]

                //departureLocation--------------------------------------
                WebElement predepartureLocation=driver.findElement(By.xpath("//*[@id=\"select2-Domestic_OriginLocationCodes_0_-container\"]/span/div"));
                Thread.sleep(1000);
                predepartureLocation.click();
                Thread.sleep(2000);
                WebElement departureLocation=driver.findElement(By.xpath("/html/body/span/span/span[1]/input"));
                departureLocation.sendKeys(fields.getDepartureLocation());
                Thread.sleep(1000);
                departureLocation.sendKeys(Keys.ARROW_DOWN);
                Thread.sleep(2000);
                departureLocation.sendKeys(Keys.ENTER);
                Thread.sleep(2000);

                //arrivalLocation----------------------------------------
                WebElement prearrivalLocation=driver.findElement(By.xpath("//*[@id=\"select2-Domestic_DestinationLocationCodes_0_-container\"]/span/div"));
                prearrivalLocation.click();
                Thread.sleep(2000);
                WebElement arrivalLocation=driver.findElement(By.xpath("/html/body/span/span/span[1]/input"));
                arrivalLocation.sendKeys(fields.getArrivalLocation());
                Thread.sleep(1000);
                arrivalLocation.sendKeys(Keys.ARROW_DOWN);
                Thread.sleep(2000);
                arrivalLocation.sendKeys(Keys.ENTER);
                Thread.sleep(2000);

                //departure date-------------------------------------------
                WebElement departureDate=driver.findElement(By.xpath("//*[@id=\"domesticSearchForm\"]/div[3]/div[1]/div/div/span"));
                System.out.println("departureDate.isDisplayed()="+departureDate.isDisplayed());
                departureDate.click();
                Thread.sleep(2000);
               // /html/body/div[5]/div/div[3]/div[1]/table/tbody/tr[4]/td[4]/a
                WebElement departureDateSelection=driver.findElement(By.xpath("/html/body/div[5]/div/div[3]/div[1]/table/tbody/tr["+fields.getRow()+"]/td["+fields.getColumn()+"]/a"));
                System.out.println("departureDateSelection.isDisplayed()="+departureDateSelection.isDisplayed());
                departureDateSelection.click();
                Thread.sleep(2000);

                //departureReturnTime-------------------------

                WebElement departureReturnDateSelection=driver.findElement(By.xpath("/html/body/div[5]/div/div[3]/div[2]/table/tbody/tr["+fields.getReturnRow()+"]/td["+fields.getReturnColumn()+"]/a"));
                departureReturnDateSelection.click();
                Thread.sleep(2000);

                //search-----------------------------------------------
                WebElement search=driver.findElement(By.xpath("//*[@id=\"domesticSearchForm\"]/div[5]/div[1]/button"));
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
                        Future<List<Result>> trip = exec.submit( new Flight(driver));
                        List<Result> tripData;
                        tripData=trip.get();
                        //database layer starts here------------------

                        FlightDA flightDA=new FlightDA("irsa","flights");
                        for (int i=0;i<tripData.size();i++){
                            System.out.println("tripData.get("+i+").getPrice()="+tripData.get(i).getPrice());
                            flightDA.addFlight(tripData.get(i).getStartAirline(),tripData.get(i).getDepartureStartTime(),
                                    tripData.get(i).getBusinessType(),tripData.get(i).getPrice(),
                                    tripData.get(i).getStartFlightNumber(),tripData.get(i).getStartWeighLimit(),
                                    tripData.get(i).getStartPriceClass(),
                                    tripData.get(i).getTimeStamp(),"tripir",
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
