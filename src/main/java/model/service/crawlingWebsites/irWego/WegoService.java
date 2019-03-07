package model.service.crawlingWebsites.irWego;

import model.common.Fields;
import model.common.Spider;
import model.da.aerospike.FlightDA;
import model.service.crawlingWebsites.irWego.windows.Flight;
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
import java.util.concurrent.*;

/**
 * The type Wego service.
 */
public class WegoService {


    private String url;
    private int delay;
    private Fields fields;
    private WebDriver driver;
    private String selectedDriver;




    public WegoService(String url,int delay,Fields fields) {

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



        final Spider wego=new Spider(this.selectedDriver,this.url,this.delay,fields);

        final File file;

        final BufferedWriter out;

        final JSONObject wegoJson =new JSONObject();

        try {

            wego.doSyncProcess(()->{
                driver=wego.getDriver();
                WebDriverWait wait = new WebDriverWait(driver,10);


               List<WebElement> makalu=driver.findElements(By.tagName("makalu-app"));
                //driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
                while(makalu.size()<1){
                    makalu=driver.findElements(By.tagName("makalu-app"));
                    Thread.sleep(1000);
                }
                System.out.println("makalu:"+makalu.get(0).isDisplayed());

                WebElement shadowRoot1 = expandRootElement(makalu.get(0));
                WebElement base=shadowRoot1.findElement(By.id("base"));
                WebElement wegoSearchForm=base.findElement(By.tagName("wego-search-form"));

                WebElement shadowRoot2=expandRootElement(wegoSearchForm);
                WebElement searchFormContainer=shadowRoot2.findElement(By.className("search-form-container"));

                WebElement wegoFlightSearchForm=searchFormContainer.findElement(By.tagName("wego-flight-search-form"));
                WebElement shadowRoot3=expandRootElement(wegoFlightSearchForm);

                WebElement main=shadowRoot3.findElement(By.className("main"));
                Thread.sleep(2000);

              //onewayselection
               //
                WebElement oneway=main.findElement(By.xpath("div[1]/div/div[2]"));
                oneway.click();
                Thread.sleep(1000);



               //--------------- start location picker for departure-------------------------------------
               WebElement locPicker=main.findElement(By.className("loc-picker"));
               WebElement departureAutoComplete=locPicker.findElement(By.tagName("auto-complete"));
               WebElement departureShadowRootLayer1=expandRootElement(departureAutoComplete);
               WebElement departureRootContainer=departureShadowRootLayer1.findElement(By.className("root-container"));
               WebElement departureContainer=departureRootContainer.findElement(By.className("container"));
               driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
               WebElement departureSearchKeywordInput=departureContainer.findElement(By.id("searchKeywordInput"));
               departureSearchKeywordInput.sendKeys(Keys.chord(Keys.CONTROL,"a"),fields.getDepartureLocation());
                System.out.println("----------step1 complete-------------");
               //--------------- end location picker for departure-------------------------------------



                //--------------- start location picker for arrival-------------------------------------
                WebElement arr=main.findElement(By.id("arr"));
                WebElement arrivalShadowRootLayer1=expandRootElement(arr);
                WebElement arrivalRootContainer=arrivalShadowRootLayer1.findElement(By.className("root-container"));
                WebElement arrivalContainer=arrivalRootContainer.findElement(By.className("container"));
                //driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
                Thread.sleep(1000);
                WebElement arrivalSearchKeywordInput=arrivalContainer.findElement(By.id("searchKeywordInput"));
                Thread.sleep(1000);
                arrivalSearchKeywordInput.sendKeys(Keys.chord(Keys.CONTROL,"a"),fields.getArrivalLocation());
                Thread.sleep(2000);
                System.out.println("----------step2 complete-------------");
                //--------------- end location picker for arrival-------------------------------------



                //--------------- start date -------------------------------------
                WebElement dates=main.findElement(By.id("dates"));
                System.out.println("dates="+dates.isDisplayed());
                WebElement datesAllShadowRootLayer1=expandRootElement(dates);

                //click on calandar
                WebElement pickerlayouthorizontal=datesAllShadowRootLayer1.findElement(By.cssSelector(".picker.layout.horizontal"));
                Thread.sleep(1000);
                WebElement depart=pickerlayouthorizontal.findElement(By.id("depart"));
                Thread.sleep(1000);
                WebElement departButton=expandRootElement(depart).findElement(By.id("btn"));
                departButton.click();
                Thread.sleep(1000);

                //now make shamsi

                WebElement shamsi1calendarpopup=datesAllShadowRootLayer1.findElement(By.cssSelector("#popupBox"));
                Thread.sleep(1000);
                System.out.println("shamsi1calendarpopup="+shamsi1calendarpopup.isDisplayed());
                Thread.sleep(1000);
                WebElement calendarpopup1=shamsi1calendarpopup.findElement(By.tagName("calendar-popup"));
                WebElement popupswitch=expandRootElement(calendarpopup1).findElement(By.cssSelector(".popup>.layout.horizontal.center-center>.switchbg>.switch"));
                System.out.println("popupswitch="+popupswitch.isDisplayed());
                popupswitch.click();
                Thread.sleep(1000);

                WebElement calendarpopup=datesAllShadowRootLayer1.findElement(By.cssSelector("#popupBox>calendar-popup"));
                System.out.println("calendarpopup="+calendarpopup.isDisplayed());
                WebElement popup=expandRootElement(calendarpopup).findElement(By.cssSelector(".popup"));
                System.out.println("popup"+popup.isDisplayed());
                WebElement calendarslayouthorizontal=popup.findElement(By.cssSelector(".calendars.layout.horizontal"));
                List<WebElement> monthflex=calendarslayouthorizontal.findElements(By.cssSelector(".month.flex"));
                System.out.println("monthflex0="+monthflex.get(0).isDisplayed());
                WebElement layouthorizontalwrap=monthflex.get(0).findElement(By.cssSelector(".layout.horizontal.wrap"));
                System.out.println("layouthorizontalwrap="+layouthorizontalwrap.isDisplayed());
                int m=fields.getStartDate()+fields.getOffset();
                WebElement startDepartTime=layouthorizontalwrap.findElement(By.xpath("i["+m+"]"));
                System.out.println("startDepartTime="+startDepartTime.isDisplayed());
                startDepartTime.click();
                Thread.sleep(1000);
                //--------------- end start date -------------------------------------


                //--------------- return date -------------------------------------
/*
                //click on calandar
                WebElement returndate=pickerlayouthorizontal.findElement(By.id("return"));
                Thread.sleep(1000);
                WebElement returnButton=expandRootElement(returndate).findElement(By.id("btn"));
                Thread.sleep(1000);
                System.out.println("returnButton="+returnButton.isDisplayed());
                returnButton.click();
                Thread.sleep(2000);
                System.out.println("monthflex1="+monthflex.get(1).isDisplayed());
                WebElement layouthorizontalwrap2=monthflex.get(1).findElement(By.cssSelector(".layout.horizontal.wrap"));
                System.out.println("layouthorizontalwrap2="+layouthorizontalwrap2.isDisplayed());

                int m2=fields.getReturnDate()+fields.getReturnOffset()-1;

                WebElement startReturnTime=layouthorizontalwrap2.findElement(By.xpath("i["+m2+"]"));
                System.out.println("startReturnTime="+startReturnTime.isDisplayed());
                startReturnTime.click();
                Thread.sleep(1000);



*/
                //--------------- end return  date -------------------------------------


                //----start search----------------
                WebElement search=main.findElement(By.id("search"));
                // wait.until(ExpectedConditions.visibilityOf(search));
                Thread.sleep(1000);
                //driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);

                JavascriptExecutor js=(JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", search);
                // ----end search--------

                //-----redirect to new page for flights and hotels--------
                Set<String> allWindows=driver.getWindowHandles();
                System.out.println("opening "+allWindows.size()+" pages to crawl");
                System.out.println("-----step5 complete---------");

                int windowCount=0;
                for(String child:allWindows)
                {
                    windowCount++;
                    if (windowCount==2){
                        driver.switchTo().window(child);
                        ExecutorService exec = Executors.newSingleThreadExecutor();
                        Future<List<Result>> wego1 = exec.submit( new Flight(driver));
                        List<Result> wego1Data;
                        wego1Data=wego1.get();
                        //database layer starts here------------------
                        FlightDA flightDA=new FlightDA("irsa","flights");
                        for (int i=0;i<wego1Data.size();i++){
                            System.out.println("wego1Data.get("+i+").getPrice()="+wego1Data.get(i).getPrice());
                            flightDA.addFlight(wego1Data.get(i).getStartAirline(),wego1Data.get(i).getDepartureStartTime(),
                                    wego1Data.get(i).getBusinessType(),wego1Data.get(i).getPrice(),
                                    wego1Data.get(i).getStartFlightNumber(),wego1Data.get(i).getStartWeighLimit(),
                                    wego1Data.get(i).getStartPriceClass(),
                                    wego1Data.get(i).getTimeStamp(),"wego",
                                    null);
                        }
                        flightDA.closeAerospikeConnection();
                        Thread.sleep(1000);
                        driver.close();

                    }
                    else{
                       // driver.switchTo().window(child);
                       // Hotel hotel=new Hotel(driver);
                       // hotel.index();
                    }

                }








                //driver.close();
            });


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Expand root element web element.
     *
     * @param element the element
     * @return the web element
     */
    public WebElement expandRootElement(WebElement element) {
        WebElement ele = (WebElement) ((JavascriptExecutor)driver)
                .executeScript("return arguments[0].shadowRoot", element);
        return ele;
    }



}
