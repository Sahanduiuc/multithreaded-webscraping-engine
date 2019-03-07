package model.service.crawlingWebsites.ghasedak24;

import model.common.Fields;
import model.common.Spider;
import model.da.aerospike.FlightDA;
import model.service.crawlingWebsites.ghasedak24.windows.Flight;
import model.service.crawlingWebsites.ghasedak24.windows.Result;
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
public class Ghasedak24Service {


    private String url;
    private int delay;
    private Fields fields;
    private String selectedDriver;
    private WebDriver driver;


    public Ghasedak24Service(String url, int delay, Fields fields) {

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



        final Spider ghasedak24=new Spider(this.selectedDriver,this.url,this.delay,fields);

        final File file;

        final BufferedWriter out;

        final JSONObject ghasedak24Json =new JSONObject();

        try {

           // driver.manage().window().setPosition(new Point(-2000,0));

            ghasedak24.doSyncProcess(()->{
                driver=ghasedak24.getDriver();
                WebDriverWait wait = new WebDriverWait(driver,10);

                JavascriptExecutor js=(JavascriptExecutor) driver;


                //departureLocation--------------------------------------

                WebElement predepartureLocation=driver.findElement(By.xpath("//*[@id=\"select2-search-from-container\"]/span"));
                Thread.sleep(1000);
                predepartureLocation.click();
                Thread.sleep(1000);
                Thread.sleep(1000);
                ///html/body/span/span/span[1]/input
                WebElement departureLocation=driver.findElement(By.xpath("/html/body/span/span/span[1]/input"));
                departureLocation.sendKeys(fields.getDepartureLocation());
                Thread.sleep(1000);
                departureLocation.sendKeys(Keys.ARROW_DOWN);
                Thread.sleep(1000);
                departureLocation.sendKeys(Keys.ENTER);
                Thread.sleep(1000);

                //arrivalLocation----------------------------------------
                WebElement prearrivalLocation=driver.findElement(By.xpath("//*[@id=\"select2-search-to-container\"]/span"));
                Thread.sleep(1000);
                prearrivalLocation.click();
                Thread.sleep(1000);
                WebElement arrivalLocation=driver.findElement(By.xpath("/html/body/span/span/span[1]/input"));
                arrivalLocation.sendKeys(fields.getArrivalLocation());
                Thread.sleep(1000);
                arrivalLocation.sendKeys(Keys.ARROW_DOWN);
                Thread.sleep(1000);
                arrivalLocation.sendKeys(Keys.ENTER);
                Thread.sleep(1000);

                //departure date-------------------------------------------
                WebElement departureDate=driver.findElement(By.xpath("//*[@id=\"search-went-date\"]"));
                departureDate.click();
                WebElement departureDateSelection=driver.findElement(By.xpath("//*[@id=\"uibs-datepicker-div\"]/div[1]/table/tbody/tr["+fields.getRow()+"]/td["+fields.getColumn()+"]/a"));
                departureDateSelection.click();



                //returnDate-------------------------------------------------



                //search-----------------------------------------------
                WebElement search=driver.findElement(By.xpath("//*[@id=\"internalFlights\"]/div/form/fieldset/div[6]/button"));
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
                        ExecutorService exec = Executors.newSingleThreadExecutor();
                        Future<List<Result>> ghasedak = exec.submit( new Flight(driver));
                        List<Result> ghasedak24Data;
                        ghasedak24Data=ghasedak.get();
                        //database layer starts here------------------
                        FlightDA flightDA=new FlightDA("irsa","flights");
                        for (int i=0;i<ghasedak24Data.size();i++){
                            System.out.println("ghasedak24Data.get("+i+").getPrice()="+ghasedak24Data.get(i).getPrice());
                            flightDA.addFlight(ghasedak24Data.get(i).getStartAirline(),ghasedak24Data.get(i).getDepartureStartTime(),
                                    ghasedak24Data.get(i).getBusinessType(),ghasedak24Data.get(i).getPrice(),
                                    ghasedak24Data.get(i).getStartFlightNumber(),ghasedak24Data.get(i).getStartWeighLimit(),
                                    ghasedak24Data.get(i).getStartPriceClass(),
                                    ghasedak24Data.get(i).getTimeStamp(),"ghasedak24",
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
