package model.service.crawlingWebsites.safarestan.windows;

import model.common.Util;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * The type Flight.
 */
public class Flight implements Callable<List<Result>> {

    private WebDriver driver;
    private WebDriverWait wait ;
    private int delay;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {

    }

    /**
     * Instantiates a new Flight.
     *
     * @param driver the driver
     */
    public Flight(WebDriver driver){
        this.driver=driver;
        wait = new WebDriverWait(driver,10);


    }

    /**
     * Index.
     *
     * @throws InterruptedException the interrupted exception
     */
    public  List<Result> call() throws InterruptedException {
        JavascriptExecutor js=(JavascriptExecutor) driver;

        this.delay=delay;
        Util expandShadow=new Util(driver);
        List<Result> listOfResults=new ArrayList<Result>();


        Thread.sleep(this.delay);
        List<WebElement> listOfFlights=driver.findElements(By.xpath("//*[@id=\"full_temp_founded_result\"]"));
        Thread.sleep(2000);
        while(listOfFlights.size()<1){
            listOfFlights=driver.findElements(By.xpath("//*[@id=\"full_temp_founded_result\"]"));
            Thread.sleep(1000);
        }

        //*********************charter*****************************************8
        System.out.println("listOfFlights="+listOfFlights.get(0).isDisplayed());
        js.executeScript("window.scrollBy(0,150)", "");
        Thread.sleep(1000);
        List<WebElement> allList=listOfFlights.get(0).findElements(By.cssSelector(".list-flight-item.oneWay"));
        Thread.sleep(6000);
        System.out.println("allList="+allList.size());
        int count=0;
        for (WebElement f:allList
             ) {
                count++;
                Result result=new Result();

                    //--------price-------------------------------------------------------------
                    WebElement price=f.findElement(By.xpath("div[1]/div[1]/div[1]/div[1]"));
                    System.out.println("price="+price.getText());
                    result.setPrice(price.getText());
                    //----takeoffTime----------------------------------------------------------
                    WebElement takeoffTime=f.findElement(By.xpath("div[1]/div[2]/div[2]/div[1]/span/span[1]/span[2]"));
                    System.out.println("takeoffTime.getText()="+takeoffTime.getText());
                    result.setDepartureStartTime(takeoffTime.getText());
                     //----landingTime--------------------------------------------------------
                    WebElement landingTime=f.findElement(By.xpath("div[1]/div[2]/div[2]/div[3]/span/span[1]/span[2]"));
                    System.out.println("landingTime.getText()="+landingTime.getText());
                    result.setArrivalStartTime(landingTime.getText());
                    //----airlineName---------------------------------------------------------
                    WebElement airlineName=f.findElement(By.xpath("div[1]/div[2]/div[2]/div[2]/div[2]/span/span"));
                    System.out.println("airlineName="+airlineName.getText());
                    result.setStartAirline(airlineName.getText());
                    listOfResults.add(result);
                    /*
                    //--------opening details modal---------------------------------------------
                    //WebElement viewDetail = driver.findElement(By.xpath("//*[@id=\"full_temp_founded_result\"]/div["+count+"]/div[1]/div[1]/div[2]"));
                    WebElement viewDetail = f.findElement(By.xpath("div[1]/div[1]/div[2]"));

                    //wait.until(ExpectedConditions.visibilityOf(viewDetail));

                    viewDetail.click();
                    Thread.sleep(3000);
                    System.out.println("detail clicked---");
                    WebElement e=driver.switchTo().activeElement();
                    Thread.sleep(5000);
                    String s=e.getText();
                    System.out.println("active element="+s);


                    List<WebElement> modaldialogmodal_1way=driver.findElements(By.cssSelector(".modal-dialog.modal_1way"));
                    Thread.sleep(1000);
                    System.out.println("modal-dialog modal_1way="+modaldialogmodal_1way.get(0).isDisplayed());

                    List<WebElement> detailtdclearfix=driver.findElements(By.cssSelector(".detail-td.clearfix"));
                    Thread.sleep(1000);
                    System.out.println("detailtdclearfix="+detailtdclearfix.get(0).isDisplayed());

                    List<WebElement> detailtbl = detailtdclearfix.get(0).findElements(By.cssSelector(".detail-tbl"));
                    Thread.sleep(1000);
                    System.out.println("detail-tbl="+detailtbl.get(0).isDisplayed());

                    //flightNumber
                    List<WebElement> flightNumber=detailtbl.get(0).findElements(By.xpath("div[4]/div/div[2]/div/div[1]/div[2]"));
                    if (flightNumber.size()>0)
                    {
                        System.out.println("flightNumber="+flightNumber.get(0).getText());
                    }

                    //numberOfSeats
                    List<WebElement> numberOfSeats=detailtbl.get(0).findElements(By.xpath("div[4]/div/div[2]/div/div[2]/div[2]/span"));
                    if (numberOfSeats.size()>0)
                    {
                        System.out.println("numberOfSeats="+numberOfSeats.get(0).getText());
                    }


                    //weighLimits
                    List<WebElement> weighLimits=detailtbl.get(0).findElements(By.xpath("div[4]/div/div[2]/div/div[2]/div[3]/span"));
                    if (weighLimits.size()>0)
                    {
                        System.out.println("weighLimits="+weighLimits.get(0).getText());
                    }

                    //sellType
                    List<WebElement> sellType=detailtbl.get(0).findElements(By.xpath("div[3]/div[2]/div[1]/span"));
                    if (sellType.size()>0)
                    {
                        System.out.println("sellType="+sellType.get(0).getText());
                    }


                   //List<WebElement> modaldialogmodal_1way=driver.findElements(By.cssSelector(".modal-dialog.modal_1way"));
                    // Thread.sleep(4000);
                   // wait.until(ExpectedConditions.visibilityOf(modaldialogmodal_1way.get(0)));

                    //System.out.println("modaldialogmodal_1way="+modaldialogmodal_1way.get(0).isDisplayed());

                    //--------close details modal--------------------------------------------
                     //WebElement close=modaldialogmodal_1way.get(0).findElement(By.xpath("div[1]/button"));
                    WebElement close=driver.findElement(By.xpath("//*[@id=\"full_temp_founded_result\"]/div["+count+"]/div[2]/div/div[1]/button"));

                     close.click();
                     Thread.sleep(2000);
                     WebElement s2=driver.switchTo().activeElement();
                     Thread.sleep(4000);
*/
                     //System.out.println("s2="+s2.getText());
                     //now scroll down to see elements
                    js.executeScript("window.scrollBy(0,150)", "");
                    Thread.sleep(1000);



            //System.out.println("active element s2="+s2);
        }


        //**********************system****************************************8
        List<WebElement> systemList=listOfFlights.get(0).findElements(By.cssSelector(".list-flight-item.oneWay.systemFli"));
        Thread.sleep(4000);
        System.out.println("systemList="+systemList.size());
        //**************************************************************8

        //driver.close();
        return listOfResults;
    }
}
