package model.service.crawlingWebsites.samtik;

import model.common.Fields;
import model.common.Spider;
import model.da.aerospike.FlightDA;
import model.service.crawlingWebsites.samtik.windows.Flight;
import model.service.crawlingWebsites.samtik.windows.Result;
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
public class SamtikService {


    private String url;
    private int delay;
    private Fields fields;
    //private HtmlUnitDriver driver;
    private WebDriver driver;
    private String selectedDriver;


    public SamtikService(String url, int delay, Fields fields) {

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




        final Spider samtik=new Spider(this.selectedDriver,this.url,this.delay,fields);

        final File file;

        final BufferedWriter out;

        final JSONObject samtikJson =new JSONObject();

        try {
            samtik.doSyncProcess(()->{
                driver=samtik.getDriver();
                WebDriverWait wait = new WebDriverWait(driver,10);

                JavascriptExecutor js=(JavascriptExecutor) driver;
              /*
               List<WebElement> twoway=driver.findElements(By.xpath("html/body/div[1]/section[1]/div/div/div/div/div/div/div[1]/form/div[1]/div/div/div/div/div[2]/label"));
                while (twoway.size()<1){
                    twoway=driver.findElements(By.xpath("html/body/div[1]/section[1]/div/div/div/div/div/div/div[1]/form/div[1]/div/div/div/div/div[2]/label"));
                    Thread.sleep(1000);
                }
                if (twoway.size()>0)
                {
                    twoway.get(0).click();
                    System.out.println("twoway clicked----");
                }
*/
                //departureLocation--------------------------------------
                WebElement predepartureLocation=driver.findElement(By.xpath("//*[@id=\"fromAirportfromAirport\"]"));
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
                WebElement prearrivalLocation=driver.findElement(By.xpath("//*[@id=\"toAirport\"]"));
                prearrivalLocation.click();
                Thread.sleep(2000);
                prearrivalLocation.sendKeys(fields.getArrivalLocation());
                Thread.sleep(1000);
                prearrivalLocation.sendKeys(Keys.ARROW_DOWN);
                Thread.sleep(2000);
                prearrivalLocation.sendKeys(Keys.ENTER);
                Thread.sleep(2000);

                //departure date-------------------------------------------

                //click on calendar to open
                WebElement departureDate=driver.findElement(By.xpath("//*[@id=\"outboundDate\"]"));
                System.out.println("departureDate.isDisplayed()="+departureDate.isDisplayed());
                departureDate.click();
                Thread.sleep(2000);

                //choose month
                List<WebElement> month=driver.findElements(By.xpath("html/body/div[4]/div[3]/div[1]/ul/li/a"));
                System.out.println("month.size()="+month.size());
                int monthboxCount=0;
                int finalmonth=0;
                for (WebElement m:month
                        ) {
                    monthboxCount++;
                    System.out.println("m.getText()="+m.getText());
                    if (m.getText().equals(fields.getStartMonth())){
                        System.out.println("got:"+m.getText());
                        finalmonth=monthboxCount;
                        Thread.sleep(1000);
                    }
                }

                WebElement departureDateSelection=driver.findElement(By.xpath("html/body/div[4]/div[3]/div[2]/div/div/div["+finalmonth+"]/div[2]/div/table/tbody/tr["+fields.getRow()+"]/td["+fields.getColumn()+"]"));
                Thread.sleep(1000);
                System.out.println("departureDateSelection.isDisplayed()="+departureDateSelection.isDisplayed());
                departureDateSelection.click();
                Thread.sleep(2000);

                //departureReturnTime-------------------------
             /*
                WebElement departureReturnDateSelection=driver.findElement(By.xpath("//*[@id=\"inboundDate\"]"));
                departureReturnDateSelection.click();
                Thread.sleep(2000);
                 monthboxCount=0;
                 finalmonth=0;
                for (WebElement m:month
                        ) {
                    monthboxCount++;
                    System.out.println("m2.getText()="+m.getText());
                    if (m.getText().equals(fields.getReturnMonth())){
                        System.out.println("fields.getReturnMonth()="+fields.getReturnMonth());
                        System.out.println("got:"+m.getText());
                        finalmonth=monthboxCount;
                        Thread.sleep(1000);
                    }
                }


                WebElement departureReturnDate=driver.findElement(By.xpath("html/body/div[4]/div[3]/div[2]/div/div/div["+finalmonth+"]/div[2]/div/table/tbody/tr["+fields.getReturnRow()+"]/td["+fields.getReturnColumn()+"]"));
                System.out.println("departureReturnDate.isDisplayed()="+departureReturnDate.isDisplayed());
                departureReturnDate.click();
                Thread.sleep(2000);
*/
                //search-----------------------------------------------
                WebElement search=driver.findElement(By.xpath("//*[@id=\"domesticFlights\"]/div[2]/div[6]/div/div/button"));
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
                        Future<List<Result>> samtik1 = exec.submit( new Flight(driver));
                        List<Result> samtikData;
                        samtikData=samtik1.get();
                        //database layer starts here------------------

                        FlightDA flightDA=new FlightDA("irsa","flights");
                        for (int i=0;i<samtikData.size();i++){
                            System.out.println("samtikData.get("+i+").getPrice()="+samtikData.get(i).getPrice());
                            flightDA.addFlight(samtikData.get(i).getStartAirline(),samtikData.get(i).getDepartureStartTime(),
                                    samtikData.get(i).getBusinessType(),samtikData.get(i).getPrice(),
                                    samtikData.get(i).getStartFlightNumber(),samtikData.get(i).getStartWeighLimit(),
                                    samtikData.get(i).getStartPriceClass(),
                                    samtikData.get(i).getTimeStamp(),"samtik",
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
