package model.service.crawlingWebsites.simorgh24;

import model.common.Fields;
import model.common.Spider;
import model.da.aerospike.FlightDA;
import model.service.crawlingWebsites.simorgh24.windows.Flight;
import model.service.crawlingWebsites.simorgh24.windows.Result;
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
public class Simorgh24Service {


    private String url;
    private int delay;
    private Fields fields;
    //private HtmlUnitDriver driver;
    private WebDriver driver;
    private String selectedDriver;



    public Simorgh24Service(String url, int delay, Fields fields) {

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


        final Spider simorgh24=new Spider(this.selectedDriver,this.url,this.delay,fields);

        final File file;

        final BufferedWriter out;

        final JSONObject samtikJson =new JSONObject();

        try {
            simorgh24.doSyncProcess(()->{
                driver=simorgh24.getDriver();
                WebDriverWait wait = new WebDriverWait(driver,10);

                JavascriptExecutor js=(JavascriptExecutor) driver;




                //arrivalLocation----------------------------------------
                WebElement prearrivalLocation=driver.findElement(By.xpath("//*[@id=\"select2-search-to-container\"]/span"));
                prearrivalLocation.click();
                WebElement arrivalLocation=driver.findElement(By.xpath("/html/body/span/span/span[1]/input"));
                Thread.sleep(2000);
                arrivalLocation.sendKeys(fields.getArrivalLocation());
                Thread.sleep(1000);
                arrivalLocation.sendKeys(Keys.ARROW_DOWN);
                Thread.sleep(2000);
                arrivalLocation.sendKeys(Keys.ENTER);
                Thread.sleep(2000);

                //departure date-------------------------------------------
                WebElement departureDate=driver.findElement(By.xpath("//*[@id=\"search-went-date\"]"));
                System.out.println("departureDate.isDisplayed()="+departureDate.isDisplayed());
                departureDate.click();
                Thread.sleep(2000);
                WebElement departureDateSelection=driver.findElement(By.xpath("//*[@id=\"uibs-datepicker-div\"]/div[1]/table/tbody/tr["+fields.getRow()+"]/td["+fields.getColumn()+"]/a"));
                Thread.sleep(1000);
                System.out.println("departureDateSelection.isDisplayed()="+departureDateSelection.isDisplayed());
                departureDateSelection.click();
                Thread.sleep(2000);

                //departureReturnTime-------------------------



                //search-----------------------------------------------
                WebElement search=driver.findElement(By.xpath("//*[@id=\"internalFlights\"]/div/form/fieldset/div[6]/button"));
                search.click();
                Thread.sleep(1000);

                //departureLocation--------------------------------------
                WebElement predepartureLocation=driver.findElement(By.xpath("/html/body/span/span/span[1]/input"));
                Thread.sleep(1000);
                predepartureLocation.click();
                Thread.sleep(2000);
                predepartureLocation.sendKeys(fields.getDepartureLocation());
                Thread.sleep(1000);
                predepartureLocation.sendKeys(Keys.ARROW_DOWN);
                Thread.sleep(2000);
                predepartureLocation.sendKeys(Keys.ENTER);
                Thread.sleep(2000);


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
                        Future<List<Result>> simorgh = exec.submit( new Flight(driver));
                        List<Result> simorghData;
                        simorghData=simorgh.get();
                        //database layer starts here------------------

                        FlightDA flightDA=new FlightDA("irsa","flights");
                        for (int i=0;i<simorghData.size();i++){
                            System.out.println("simorgh24.get("+i+").getPrice()="+simorghData.get(i).getPrice());
                            flightDA.addFlight(simorghData.get(i).getStartAirline(),simorghData.get(i).getDepartureStartTime(),
                                    simorghData.get(i).getBusinessType(),simorghData.get(i).getPrice(),
                                    simorghData.get(i).getStartFlightNumber(),simorghData.get(i).getStartWeighLimit(),
                                    simorghData.get(i).getStartPriceClass(),
                                    simorghData.get(i).getTimeStamp(),"simorgh24",
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
