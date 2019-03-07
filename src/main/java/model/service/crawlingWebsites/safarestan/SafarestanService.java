package model.service.crawlingWebsites.safarestan;

import model.common.Fields;
import model.common.Spider;
import model.da.aerospike.FlightDA;
import model.service.crawlingWebsites.safarestan.windows.Flight;
import model.service.crawlingWebsites.safarestan.windows.Result;
import org.json.JSONObject;
import org.openqa.selenium.*;
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
public class SafarestanService {


    private String url;
    private int delay;
    private Fields fields;
    private WebDriver driver;
    private String selectedDriver;


    public SafarestanService(String url, int delay, Fields fields) {

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



        final Spider safarestan=new Spider(this.selectedDriver,this.url,4000,fields);

        final File file;

        final BufferedWriter out;

        final JSONObject safarestanJson =new JSONObject();

        try {

            safarestan.doSyncProcess(()->{
                driver=safarestan.getDriver();
                WebDriverWait wait = new WebDriverWait(driver,10);

                JavascriptExecutor js=(JavascriptExecutor) driver;


               //close the modal if present:
                //*[@id="popModal2"]/div/div/button
                List<WebElement> close=driver.findElements(By.xpath("//*[@id=\"popModal2\"]/div/div/button"));
                if (close.size()>0){
                    close.get(0).click();
                }


                //i forgot to select two way checkbox
                Thread.sleep(1000);
                List<WebElement> homeSearchdivs=driver.findElements(By.xpath("//*[@id=\"homeSearch\"]/div[2]/div"));
                Thread.sleep(1000);
                System.out.println("homeSearchdivs="+homeSearchdivs.size());
                WebElement tabsearchclearfix=homeSearchdivs.get(1);
                Thread.sleep(1000);
                WebElement Flightsearch=tabsearchclearfix.findElement(By.cssSelector("div>div:nth-child(1)>div"));
                Thread.sleep(1000);
                System.out.println("Flighsearch="+Flightsearch.isDisplayed());
                WebElement preinput1=Flightsearch.findElement(By.xpath("div[1]/div"));
                Thread.sleep(1000);
                System.out.println("preinput1="+preinput1.isDisplayed());
                WebElement input=preinput1.findElement(By.xpath("//*[@id=\"check_legs_selector_round_trip\"]"));
                Thread.sleep(2000);
                //System.out.println("roundtrip="+input.isDisplayed());

                //*[@id="check_legs_selector_round_trip"]
               // input.click();  //checkbox for return
                Thread.sleep(1000);
                System.out.println("****step0 complete********");
                WebElement formItemInputHeight=Flightsearch.findElement(By.cssSelector(".clearfix.flightCityName.rowSpc > div:nth-child(1) > .form-elements >" +
                        ".form-item.inputHeight.text-fa.rtl"));
                Thread.sleep(3000);
                System.out.println("formItemInputHeight="+formItemInputHeight.isDisplayed());
                WebElement DepartureCountryShower=formItemInputHeight.findElement(By.id("DepartureCountryShower"));
                Thread.sleep(2000);
                js.executeScript("arguments[0].setAttribute('selecteditem', arguments[1])",DepartureCountryShower,fields.getDepartureLocation());
                Thread.sleep(1000);
                  WebElement formelements=Flightsearch.findElement(By.cssSelector(".clearfix.flightCityName.rowSpc > div:nth-child(1)> .form-elements"));
                Thread.sleep(1000);
                String selected=DepartureCountryShower.getAttribute("selecteditem");
                DepartureCountryShower.sendKeys(Keys.ENTER);
                Thread.sleep(3000);
                System.out.println("dep="+DepartureCountryShower.isEnabled()+" "+selected);
                Thread.sleep(2000);
                WebElement searchautocomplatertl=formItemInputHeight.findElement(By.cssSelector("div"));
                //predepartureCityList.click();
                Thread.sleep(2000);
                System.out.println("searchautocomplatertl="+searchautocomplatertl.getAttribute("style"));
                Thread.sleep(1000);
                System.out.println("****step1 complete********");
                WebElement formItemInputHeight2=Flightsearch.findElement(By.cssSelector(".clearfix.flightCityName.rowSpc > div:nth-child(3) > .form-elements >" +
                        ".form-item.inputHeight.text-fa.rtl"));
                Thread.sleep(2000);
                WebElement arrival=formItemInputHeight2.findElement(By.id("ArrivalCountryShower"));
                Thread.sleep(2000);
                js.executeScript("arguments[0].setAttribute('selecteditem', arguments[1])",arrival,fields.getArrivalLocation());
                Thread.sleep(1000);
                String selected2=arrival.getAttribute("selecteditem");
                System.out.println("selected2="+selected2);
                System.out.println("****step2 complete********");
                WebElement clearfix2=Flightsearch.findElement(By.cssSelector(".clearfix.flightRange.rowSpc"));
                Thread.sleep(1000);
                System.out.println("clearfix2="+clearfix2.isDisplayed());
                WebElement row=clearfix2.findElement(By.cssSelector("div:nth-child(1)>div"));
                Thread.sleep(1000);
                System.out.println("row="+row.isDisplayed());
                WebElement Flyingstart=row.findElement(By.cssSelector("div:nth-child(1)>div>div>#Flying_Start"));
                Thread.sleep(1000);
                System.out.println("flyingstart="+Flyingstart.isDisplayed());
                Flyingstart.sendKeys(Keys.ENTER);
                WebElement uidatepickerdiv=driver.findElement(By.cssSelector("#ui-datepicker-div"));
                Thread.sleep(2000);
                System.out.println("ui-datepicker-div="+uidatepickerdiv.isDisplayed());
                Thread.sleep(2000);
                WebElement startTime=uidatepickerdiv.findElement(By.cssSelector(".ui-datepicker-calendar>tbody>tr:nth-child("+fields.getRow()+")>td:nth-child("+fields.getColumn()+")>a"));
                Thread.sleep(2000);
                System.out.println("startTime="+startTime.isDisplayed());
                startTime.sendKeys(Keys.ENTER);
                Thread.sleep(1000);
                System.out.println("****step3 complete********");
                /*
                WebElement flyingStart=driver.findElement(By.xpath("//*[@id=\"Flying_Start\"]"));
                Thread.sleep(1000);
                System.out.println("flyingStart="+flyingStart.isDisplayed());
                flyingStart.sendKeys(Keys.TAB);
                Thread.sleep(2000);
                WebElement flyingBack=driver.findElement(By.xpath("//*[@id=\"Flying_Back\"]"));
                Thread.sleep(1000);
                flyingBack.click();
                Thread.sleep(1000);
                WebElement returnTime=driver.findElement(By.xpath("//*[@id=\"ui-datepicker-div\"]/table/tbody/tr["+5+"]/td["+7+"]/a"));
                Thread.sleep(2000);
                System.out.println("returnTime="+returnTime.isDisplayed());
                returnTime.sendKeys(Keys.ENTER);
                Thread.sleep(1000);
                System.out.println("****step4 complete********");
*/
                //*[@id="flight_passenger_count_adult"]/option[2]
                WebElement numberOfAdults=driver.findElement(By.xpath("//*[@id=\"flight_passenger_count_adult\"]/option["+1+"]"));
                Thread.sleep(1000);
                numberOfAdults.click();
                Thread.sleep(1000);
                System.out.println("******step5 complete**********");


                /*
                WebElement numberOfChilds=driver.findElement(By.xpath("//*[@id=\"flight_passenger_count_child\"]/option["+1+"]")); //4 child
                Thread.sleep(1000);
                System.out.println("numberOfChilds="+numberOfChilds.isDisplayed());
                numberOfChilds.click();
                Thread.sleep(1000);
                System.out.println("**********step6 complete******");
                WebElement numberOfInfants=driver.findElement(By.xpath("//*[@id=\"flight_passenger_count_infant\"]/option["+1+"]")); //1 infant
                Thread.sleep(1000);
                System.out.println("numberOfInfants="+numberOfInfants.isDisplayed());
                numberOfInfants.click();
                Thread.sleep(2000);
                System.out.println("********step 7 complete*********");
                WebElement classType=driver.findElement(By.xpath("//*[@id=\"Flying_Class\"]/option["+1+"]"));  //economy,  businesss, promo
                Thread.sleep(1000);
                System.out.println("classtype="+classType.isDisplayed());
                classType.click();
                Thread.sleep(2000);
               */

                System.out.println("******step 6 complete*********");
                WebElement search=driver.findElement(By.xpath("//*[@id=\"a\"]/div/div[4]/div/div/a"));
                js.executeScript("arguments[0].click();", search);
                Thread.sleep(1000);
                System.out.println("****form submitted******");
                //driver.close();
                //-----redirect to new page for flights and hotels--------
                Set<String> allWindows=driver.getWindowHandles();
                System.out.println("opening "+allWindows.size()+" pages to crawl");
                System.out.println("-----step7 complete---------");

                int windowCount=0;
                for(String child:allWindows)
                {
                    windowCount++;

                    if (windowCount==1){
                        driver.switchTo().window(child);
                        ExecutorService exec = Executors.newSingleThreadExecutor();
                        Future<List<Result>> safarestan1 = exec.submit( new Flight(driver));
                        List<Result> safarestanData;
                        safarestanData=safarestan1.get();
                        //database layer starts here------------------

                        FlightDA flightDA=new FlightDA("irsa","flights");
                        for (int i=0;i<safarestanData.size();i++){
                            System.out.println("safarestanData.get("+i+").getPrice()="+safarestanData.get(i).getPrice());
                            flightDA.addFlight(safarestanData.get(i).getStartAirline(),safarestanData.get(i).getDepartureStartTime(),
                                    safarestanData.get(i).getBusinessType(),safarestanData.get(i).getPrice(),
                                    safarestanData.get(i).getStartFlightNumber(),safarestanData.get(i).getStartWeighLimit(),
                                    safarestanData.get(i).getStartPriceClass(),
                                    safarestanData.get(i).getTimeStamp(),"safarestan",
                                    null);
                        }
                        flightDA.closeAerospikeConnection();
                        Thread.sleep(1000);
                        driver.close();
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
