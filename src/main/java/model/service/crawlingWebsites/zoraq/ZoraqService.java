package model.service.crawlingWebsites.zoraq;

import model.common.Fields;
import model.common.Spider;
import model.da.aerospike.FlightDA;
import model.service.crawlingWebsites.zoraq.windows.Flight;
import model.service.crawlingWebsites.zoraq.windows.Result;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

//import org.openqa.selenium.htmlunit.HtmlUnitDriver;


import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * The type Wego service.
 */
public class ZoraqService {


    private String url;
    private int delay;
    private Fields fields;
    //private HtmlUnitDriver driver;
    private WebDriver driver;
    private String selectedDriver;


    public ZoraqService(String url, int delay, Fields fields) {

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

    public void main() {




        final Spider zoraq=new Spider(this.selectedDriver,this.url,this.delay,fields);

        final File file;

        final BufferedWriter out;

        final JSONObject zoraqJson =new JSONObject();

        try {
           // driver.manage().window().setPosition(new Point(-2000,0));

            zoraq.doSyncProcess(()->{
                driver=zoraq.getDriver();
                WebDriverWait wait = new WebDriverWait(driver,10);

                JavascriptExecutor js=(JavascriptExecutor) driver;

                WebElement internal=driver.findElement(By.xpath("//*[@id=\"domestic-panel-search\"]/a/h2"));
                Thread.sleep(1000);
                internal.click();

                //departureLocation--------------------------------------
                WebElement predepartureLocation=driver.findElement(By.xpath("//*[@id=\"origin-input-domestic\"]"));
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
                WebElement prearrivalLocation=driver.findElement(By.xpath("//*[@id=\"destination-input-domestic\"]"));
                prearrivalLocation.click();
                Thread.sleep(2000);
                prearrivalLocation.sendKeys(fields.getArrivalLocation());
                Thread.sleep(1000);
                prearrivalLocation.sendKeys(Keys.ARROW_DOWN);
                Thread.sleep(1000);
                prearrivalLocation.sendKeys(Keys.ENTER);
                Thread.sleep(1000);

                //departure date-------------------------------------------
                WebElement departureDate=driver.findElement(By.xpath("//*[@id=\"domestic-departing\"]"));
                departureDate.click();
                Thread.sleep(1000);
                WebElement departureDateSelection=driver.findElement(By.xpath("/html/body/div[2]/table/tbody/tr[2]/td/table[1]/tbody/tr["+fields.getRow()+"]/td["+fields.getColumn()+"]/a"));
                departureDateSelection.click();
                Thread.sleep(1000);

                //departureReturnTime-------------------------
               /*
                WebElement departureReturnTime=driver.findElement(By.xpath("//*[@id=\"domestic-returning\"]"));
                departureReturnTime.click();
                Thread.sleep(1000);
                WebElement departureReturnTimeSelection=driver.findElement(By.xpath("/html/body/div[2]/table/tbody/tr[2]/td/table[2]/tbody/tr["+fields.getReturnRow()+"]/td["+fields.getReturnColumn()+"]/a"));
                departureReturnTimeSelection.click();
                Thread.sleep(1000);
*/

                //search-----------------------------------------------
                WebElement search=driver.findElement(By.xpath("//*[@id=\"submit-domestic-search\"]"));
                search.click();

                System.out.println("****form submitted******");
                //driver.close();
                //-----redirect to new page for flights and hotels--------
                Set<String> allWindows=driver.getWindowHandles();
                System.out.println("opening "+allWindows.size()+" pages to crawl");
                System.out.println("-----step5 complete---------");

                int windowCount=0;
                for(String child:allWindows)
                {
                    windowCount++;

                    if (windowCount==1){
                        driver.switchTo().window(child);
                        Flight flight=new Flight(driver);
                        //flight.index(Integer.parseInt("10000"));
                        ExecutorService exec = Executors.newSingleThreadExecutor();
                        Future<List<Result>> zoraq1 = exec.submit( new Flight(driver));
                        List<Result> zoraqData;
                        zoraqData=zoraq1.get();
                        //database layer starts here------------------

                        FlightDA flightDA=new FlightDA("irsa","flights");
                        for (int i=0;i<zoraqData.size();i++){
                            System.out.println("zoraqData.get("+i+").getPrice()="+zoraqData.get(i).getPrice());
                            flightDA.addFlight(zoraqData.get(i).getStartAirline(),zoraqData.get(i).getDepartureStartTime(),
                                    zoraqData.get(i).getBusinessType(),zoraqData.get(i).getPrice(),
                                    zoraqData.get(i).getStartFlightNumber(),zoraqData.get(i).getStartWeighLimit(),
                                    zoraqData.get(i).getStartPriceClass(),
                                    zoraqData.get(i).getTimeStamp(),"zoraq",
                                    null);
                        }
                        flightDA.closeAerospikeConnection();



                        //Thread.sleep(2000);
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
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }




}
