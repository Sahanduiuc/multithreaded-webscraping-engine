package model.service.crawlingWebsites.flightio;

import model.common.Fields;
import model.common.Spider;
import model.da.aerospike.FlightDA;
import model.service.crawlingWebsites.flightio.windows.Flight;
import model.service.crawlingWebsites.flightio.windows.Result;
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
public class FlightioService {

    private String selectedDriver;
    private WebDriver driver;
    private String url;
    private int delay;
    private Fields fields;


    public String getSelectedDriver() {
        return selectedDriver;
    }

    public void setSelectedDriver(String selectedDriver) {
        this.selectedDriver = selectedDriver;
    }




    public FlightioService(String url, int delay, Fields fields) {

        this.url=url;
        this.delay=delay;
        this.fields=fields;

    }


    /**
     * Main.
     */

    public void main() throws ExecutionException {



        final Spider flightio=new Spider(this.selectedDriver,this.url,this.delay,fields);

        final File file;

        final BufferedWriter out;

        final JSONObject flightioJson =new JSONObject();

        try {
           // driver.manage().window().setPosition(new Point(-2000,0));

            flightio.doSyncProcess(()->{
                driver=flightio.getDriver();
                WebDriverWait wait = new WebDriverWait(driver,10);


                JavascriptExecutor js=(JavascriptExecutor) driver;


                //departureLocation--------------------------------------

                WebElement predepartureLocation=driver.findElement(By.xpath("//*[@id=\"DOM_SourceCityName\"]"));
                while(!predepartureLocation.isDisplayed()){
                    predepartureLocation=driver.findElement(By.xpath("//*[@id=\"DOM_SourceCityName\"]"));
                }
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
                WebElement prearrivalLocation=driver.findElement(By.xpath("//*[@id=\"DOM_DestinationCityName\"]"));
                prearrivalLocation.click();
                Thread.sleep(2000);
                prearrivalLocation.sendKeys(fields.getArrivalLocation());
                Thread.sleep(1000);
                prearrivalLocation.sendKeys(Keys.ARROW_DOWN);
                Thread.sleep(1000);
                prearrivalLocation.sendKeys(Keys.ENTER);
                Thread.sleep(1000);

                //departure date-------------------------------------------
                WebElement departureDate=driver.findElement(By.xpath("//*[@id=\"ui-datepicker-div\"]"));
                departureDate.click();
                Thread.sleep(1000);
                WebElement departureDateSelection=departureDate.findElement(By.xpath("div[1]/table/tbody/tr["+fields.getRow()+"]/td["+fields.getColumn()+"]/a"));
                departureDateSelection.click();
                Thread.sleep(1000);


                //returnDate-------------------------------------------------



                //search-----------------------------------------------
                WebElement search=driver.findElement(By.xpath("//*[@id=\"SubmitDomestic\"]"));
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
                        Future<List<Result>> flightio1 = exec.submit( new Flight(driver));
                        List<Result> flightioData;
                        flightioData=flightio1.get();
                        //database layer starts here------------------

                        FlightDA flightDA=new FlightDA("irsa","flights");
                        for (int i=0;i<flightioData.size();i++){
                            System.out.println("flightioData.get("+i+").getPrice()="+flightioData.get(i).getPrice());
                            flightDA.addFlight(flightioData.get(i).getStartAirline(),flightioData.get(i).getDepartureStartTime(),
                                    flightioData.get(i).getBusinessType(),flightioData.get(i).getPrice(),
                                    flightioData.get(i).getStartFlightNumber(),flightioData.get(i).getStartWeighLimit(),
                                    flightioData.get(i).getStartPriceClass(),
                                    flightioData.get(i).getTimeStamp(),"flightio",
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
