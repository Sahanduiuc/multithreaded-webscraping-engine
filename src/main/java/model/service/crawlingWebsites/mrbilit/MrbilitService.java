package model.service.crawlingWebsites.mrbilit;

import model.common.Fields;
import model.common.Spider;
import model.da.aerospike.FlightDA;
import model.service.crawlingWebsites.mrbilit.windows.Flight;
import model.service.crawlingWebsites.mrbilit.windows.Result;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
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
public class MrbilitService {

    private String selectedDriver;
    private WebDriver driver;

    private String url;
    private int delay;
    private Fields fields;
    //private HtmlUnitDriver driver;
    //private ChromeDriver driver;

    public MrbilitService(String url, int delay, Fields fields) {
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



        final Spider mrbilit=new Spider(this.selectedDriver,this.url,this.delay,fields);

        final File file;

        final BufferedWriter out;

        final JSONObject mrbilitJson =new JSONObject();

        try {
            //WebDriverWait wait = new WebDriverWait(driver,10);

            mrbilit.doSyncProcess(()->{
                driver=mrbilit.getDriver();

                JavascriptExecutor js=(JavascriptExecutor) driver;

                //*[@id="onesignal-popover-cancel-button"]

                List<WebElement> closeDiscount=driver.findElements(By.xpath("//*[@id=\"onesignal-popover-cancel-button\"]"));
                if (closeDiscount.size()>0)
                {
                    closeDiscount.get(0).click();
                    Thread.sleep(1000);
                }




                //departureLocation--------------------------------------

                WebElement predepartureLocation=driver.findElement(By.xpath("//*[@id=\"form-plane\"]/div[2]/div[1]/div/div[1]/button/span[1]"));
                Thread.sleep(1000);
                predepartureLocation.click();
                Thread.sleep(2000);
                WebElement predepartureLocation1=driver.findElement(By.xpath("//*[@id=\"form-plane\"]/div[2]/div[1]/div/div[1]/div/div[1]/input"));
                predepartureLocation1.sendKeys(fields.getDepartureLocation());
                Thread.sleep(1000);
                predepartureLocation1.sendKeys(Keys.ARROW_DOWN);
                Thread.sleep(2000);
                predepartureLocation1.sendKeys(Keys.ENTER);
                Thread.sleep(2000);

                //arrivalLocation----------------------------------------
                WebElement prearrivalLocation=driver.findElement(By.xpath("//*[@id=\"form-plane\"]/div[2]/div[2]/div/div/button"));
                prearrivalLocation.click();
                Thread.sleep(2000);
                WebElement prearrivalLocation1=driver.findElement(By.xpath("//*[@id=\"form-plane\"]/div[2]/div[2]/div/div/div/div[1]/input"));
                prearrivalLocation1.sendKeys(fields.getArrivalLocation());
                Thread.sleep(1000);
                prearrivalLocation1.sendKeys(Keys.ARROW_DOWN);
                Thread.sleep(1000);
                prearrivalLocation1.sendKeys(Keys.ENTER);
                Thread.sleep(1000);

                //departure date-------------------------------------------
                WebElement departureDate=driver.findElement(By.xpath("//*[@id=\"date_from_fake\"]"));
                departureDate.click();
                Thread.sleep(1000);
                WebElement departureDateSelection=departureDate.findElement(By.xpath("//*[@id=\"ui-datepicker-div\"]/div[1]/table/tbody/tr["+fields.getRow()+"]/td["+fields.getColumn()+"]/a"));
                departureDateSelection.click();
                Thread.sleep(1000);

                //search-----------------------------------------------
                WebElement search=driver.findElement(By.xpath("//*[@id=\"submitbtn\"]"));
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
                        Future<List<Result>> bilit = exec.submit( new Flight(driver));
                        List<Result> mrbilitData;
                        mrbilitData=bilit.get();
                        //database layer starts here------------------

                        FlightDA flightDA=new FlightDA("irsa","flights");
                        for (int i=0;i<mrbilitData.size();i++){
                            System.out.println("mrbilitData.get("+i+").getPrice()="+mrbilitData.get(i).getPrice());
                            flightDA.addFlight(mrbilitData.get(i).getStartAirline(),mrbilitData.get(i).getDepartureStartTime(),
                                    mrbilitData.get(i).getBusinessType(),mrbilitData.get(i).getPrice(),
                                    mrbilitData.get(i).getStartFlightNumber(),mrbilitData.get(i).getStartWeighLimit(),
                                    mrbilitData.get(i).getStartPriceClass(),
                                    mrbilitData.get(i).getTimeStamp(),"mrbilit",
                                    null);
                        }
                        flightDA.closeAerospikeConnection();

                        Thread.sleep(1000);
                        driver.close();
                       // System.exit(0);
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
