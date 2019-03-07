package model.service.crawlingWebsites.safarme;

import model.common.Fields;
import model.common.Spider;
import model.da.aerospike.FlightDA;
import model.service.crawlingWebsites.safarme.windows.Flight;
import model.service.crawlingWebsites.safarme.windows.Result;
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
public class safarmeService {


    private String url;
    private int delay;
    private Fields fields;
    //private HtmlUnitDriver driver;
    private WebDriver driver;
    private String selectedDriver;



    public safarmeService(String url, int delay, Fields fields) {

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




        final Spider safarme=new Spider(this.selectedDriver,this.url,this.delay,fields);

        final File file;

        final BufferedWriter out;

        final JSONObject safarmeJson =new JSONObject();

        try {
           // driver.manage().window().setPosition(new Point(-2000,0));

            safarme.doSyncProcess(()->{
                driver=safarme.getDriver();
                WebDriverWait wait = new WebDriverWait(driver,10);

                JavascriptExecutor js=(JavascriptExecutor) driver;

                //departureLocation--------------------------------------

                WebElement predepartureLocation=driver.findElement(By.xpath("//*[@id=\"txtFromAirportID\"]"));
                while(!predepartureLocation.isDisplayed()){
                    predepartureLocation=driver.findElement(By.xpath("//*[@id=\"txtFromAirportID\"]"));
                }
                Thread.sleep(1000);
                predepartureLocation.click();
                Thread.sleep(2000);
                predepartureLocation.sendKeys(fields.getDepartureLocation());
                Thread.sleep(1000);
                predepartureLocation.sendKeys(Keys.ARROW_DOWN);
                predepartureLocation.sendKeys(Keys.ARROW_DOWN);
                Thread.sleep(2000);
                predepartureLocation.sendKeys(Keys.ENTER);
                Thread.sleep(2000);

                //arrivalLocation----------------------------------------
                WebElement prearrivalLocation=driver.findElement(By.xpath("//*[@id=\"txtToAirportID\"]"));
                prearrivalLocation.click();
                Thread.sleep(2000);
                prearrivalLocation.sendKeys(fields.getArrivalLocation());
                Thread.sleep(1000);
                prearrivalLocation.sendKeys(Keys.ARROW_DOWN);

                Thread.sleep(1000);
                prearrivalLocation.sendKeys(Keys.ENTER);
                Thread.sleep(1000);

                //departure date-------------------------------------------
                WebElement departureDate=driver.findElement(By.xpath("//*[@id=\"txtDate\"]"));
                departureDate.click();
                Thread.sleep(1000);
                WebElement departureDateSelection=departureDate.findElement(By.xpath("//*[@id=\"div_txtDate\"]/div[3]/div/div[1]/div[2]/div[2]/div["+fields.getStartDate()+"]"));
                departureDateSelection.click();
                Thread.sleep(1000);


                //returnDate-------------------------------------------------



                //search-----------------------------------------------
                WebElement search=driver.findElement(By.xpath("//*[@id=\"btnSearch\"]"));
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
                        Future<List<Result>> safar = exec.submit( new Flight(driver));
                        List<Result> safarmeData;
                        safarmeData=safar.get();
                        //database layer starts here------------------

                        FlightDA flightDA=new FlightDA("irsa","flights");
                        for (int i=0;i<safarmeData.size();i++){
                            System.out.println("safarmeData.get("+i+").getPrice()="+safarmeData.get(i).getPrice());
                            flightDA.addFlight(safarmeData.get(i).getStartAirline(),safarmeData.get(i).getDepartureStartTime(),
                                    safarmeData.get(i).getBusinessType(),safarmeData.get(i).getPrice(),
                                    safarmeData.get(i).getStartFlightNumber(),safarmeData.get(i).getStartWeighLimit(),
                                    safarmeData.get(i).getStartPriceClass(),
                                    safarmeData.get(i).getTimeStamp(),"safarme",
                                    null);
                        }
                        flightDA.closeAerospikeConnection();

                        Thread.sleep(1000);
                        driver.close();
                        //System.exit(0);


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
