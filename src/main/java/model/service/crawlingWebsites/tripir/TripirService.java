package model.service.crawlingWebsites.tripir;

import model.common.Fields;
import model.common.Spider;
import model.da.aerospike.FlightDA;
import model.service.crawlingWebsites.tripir.windows.Flight;
import model.service.crawlingWebsites.tripir.windows.Result;
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
public class TripirService {


    private String url;
    private int delay;
    private Fields fields;
    //private HtmlUnitDriver driver;
    private WebDriver driver;
    private String selectedDriver;


    public TripirService(String url, int delay, Fields fields) {

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




        final Spider tripir=new Spider(this.selectedDriver,this.url,this.delay,fields);

        final File file;

        final BufferedWriter out;

        final JSONObject tripirJson =new JSONObject();

        try {
            tripir.doSyncProcess(()->{
                driver=tripir.getDriver();
                WebDriverWait wait = new WebDriverWait(driver,10);

                JavascriptExecutor js=(JavascriptExecutor) driver;
                //*[@id="onesignal-popover-cancel-button"]


                //departureLocation--------------------------------------
                WebElement predepartureLocation=driver.findElement(By.xpath("/html/body/header/div[3]/form/div[1]/div[1]/div/div[1]/input"));
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
                WebElement prearrivalLocation=driver.findElement(By.xpath("/html/body/header/div[3]/form/div[1]/div[2]/div[1]/input"));
                prearrivalLocation.click();
                Thread.sleep(2000);
                prearrivalLocation.sendKeys(fields.getArrivalLocation());
                Thread.sleep(1000);
                prearrivalLocation.sendKeys(Keys.ARROW_DOWN);
                Thread.sleep(1000);
                prearrivalLocation.sendKeys(Keys.ENTER);
                Thread.sleep(1000);

                //departure date-------------------------------------------
                WebElement departureDate=driver.findElement(By.xpath("/html/body/header/div[3]/form/div[2]/div[1]/div"));
                departureDate.click();
                Thread.sleep(1000);
                // /html/body/header/div[3]/form/div[2]/div[3]/div[2]/div[5]/div[4]
                WebElement departureDateSelection=driver.findElement(By.xpath("/html/body/header/div[3]/form/div[2]/div[3]/div[2]/div["+fields.getRow()+"]/div["+fields.getColumn()+"]"));
                departureDateSelection.click();
                Thread.sleep(1000);

                //departureReturnTime-------------------------
                WebElement departureReturnTime=driver.findElement(By.xpath("/html/body/header/div[3]/form/div[2]/div[2]/div[2]/div"));
                departureReturnTime.click();
                Thread.sleep(1000);
                // /html/body/header/div[3]/form/div[2]/div[3]/div[2]/div[13]/div[3]
                WebElement departureReturnTimeSelection=driver.findElement(By.xpath("/html/body/header/div[3]/form/div[2]/div[3]/div[2]/div["+fields.getReturnRow()+"]/div["+fields.getReturnColumn()+"]"));
                departureReturnTimeSelection.click();
                Thread.sleep(1000);


                //search-----------------------------------------------
                WebElement search=driver.findElement(By.xpath("/html/body/header/div[3]/form/div[4]/button"));
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
