package model.service.crawlingWebsites.mrbilit.windows;

import model.common.Util;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ReturnFlight implements Callable<List<Result2>> {

    private FirefoxDriver driver;
    private WebDriverWait wait ;
    private int delay;
    private WebElement e1;
    private Result2 result2;

    public ReturnFlight(FirefoxDriver driver, WebElement e1) {
        this.driver = driver;
        this.e1=e1;
        wait = new WebDriverWait(driver,10);

    }


    public List<Result2> call() throws InterruptedException {
        List<Result2> listOfResults2=new ArrayList<Result2>();

        boolean notAvailableFlag=false;
        JavascriptExecutor js = (JavascriptExecutor) driver;
        this.delay = delay;
        Util expandShadow = new Util(driver);
        System.out.println("return flight on--------------------------------------");
        WebElement startTicket=e1.findElement(By.xpath("footer/div[2]/button"));
        js.executeScript("arguments[0].scrollIntoView()", startTicket);
        wait.until(ExpectedConditions.elementToBeClickable(startTicket));
        js.executeScript("arguments[0].click();", startTicket);
        Thread.sleep(4000);

        WebElement container=driver.findElement(By.xpath("//*[@id=\"domestic-app\"]/div/div/div[4]"));
        Thread.sleep(1000);
        System.out.println("container in return flight="+container.isDisplayed());

        List<WebElement> availablerowisCompleted=container.findElements(By.xpath("div/div[2]/div[3]"));
        Thread.sleep(1000);

        if (availablerowisCompleted.size()>0) {

            System.out.println("availablerowisCompleted in return flight=" + availablerowisCompleted.get(0).isDisplayed());
            List<WebElement> colxs12 = availablerowisCompleted.get(0).findElements(By.cssSelector(".col-xs-12.available-columns.inner-available.trans-fade-in"));
            Thread.sleep(2000);
            System.out.println("colxs12 in return flight=" + colxs12.size());

            int count = 0;
            for (WebElement e : colxs12
                    ) {
                 result2=new Result2();
                 result2.flag=true;
                count++;
                System.out.println("return count="+count);
                //  js.executeScript("window.scrollBy(0,150)", "");

                List<WebElement> returnAirline = e.findElements(By.xpath("div[3]/div[1]/div[1]/div/span"));
                Thread.sleep(1000);
                System.out.println("returnAirline =" + returnAirline.get(0).getText());
                result2.setReturnAirline(returnAirline.get(0).getText());

                List<WebElement> departureReturnTime = e.findElements(By.xpath("div[3]/div[1]/div[2]/div[2]/div[1]/strong[2]"));
                System.out.println("departureReturnTime.getText()=" + departureReturnTime.get(0).getText());
                result2.setDepartureReturnTime(departureReturnTime.get(0).getText());

                List<WebElement> returnBusinessType = e.findElements(By.xpath("div[3]/div[1]/div[2]/div[1]/div"));
                System.out.println("returnBusinessType.getText()=" + returnBusinessType.get(0).getText());
                result2.setReturnBusinessType(returnBusinessType.get(0).getText());

                List<WebElement> returnPrice = e.findElements(By.xpath("footer/div[1]/span/span"));
                System.out.println("returnPrice.getText()=" + returnPrice.get(0).getText());
                result2.setReturnPrice(returnPrice.get(0).getText());

                WebElement returnDetails = e.findElement(By.xpath("div[3]/div[2]/div[1]/div[2]/div/a/span"));
                js.executeScript("arguments[0].scrollIntoView()", returnDetails);
                wait.until(ExpectedConditions.elementToBeClickable(returnDetails));
                js.executeScript("arguments[0].click();", returnDetails);
                Thread.sleep(4000);

                List<WebElement> returnFlightNumber = e.findElements(By.xpath("div[3]/div[2]/div[1]/div[1]/div/div/div[1]/div/div/div[1]/div[1]/span"));
                if (returnFlightNumber.size() != 0) {
                    System.out.println("returnFlightNumber.getText()=" + returnFlightNumber.get(0).getText());
                    result2.setReturnFlightNumber(returnFlightNumber.get(0).getText());
                }

                List<WebElement> returnWeighLimit = e.findElements(By.xpath("div[3]/div[2]/div[1]/div[1]/div/div/div[1]/div/div/div[1]/div[2]/span"));
                if (returnWeighLimit.size() != 0) {
                    System.out.println("returnWeighLimit.get(0).getText()=" + returnWeighLimit.get(0).getText());
                    result2.setReturnWeighLimit(returnWeighLimit.get(0).getText());
                }

                List<WebElement> returnPriceClass = e.findElements(By.xpath("div[3]/div[2]/div[1]/div[1]/div/div/div[1]/div/div/div[1]/div[3]/span"));
                if (returnPriceClass.size() != 0) {
                    System.out.println("returnPriceClass.getText()=" + returnPriceClass.get(0).getText());
                    result2.setReturnPriceClass(returnPriceClass.get(0).getText());
                }


                listOfResults2.add(result2);

            }
            try {
                WebElement changeToMain = driver.findElement(By.xpath("//*[@id=\"domestic-app\"]/div/div/div[3]/div/div[1]/div[3]/div[2]/button"));
                js.executeScript("arguments[0].scrollIntoView()", changeToMain);
                wait.until(ExpectedConditions.elementToBeClickable(changeToMain));
                js.executeScript("arguments[0].click();", changeToMain);
                Thread.sleep(1000);
                System.out.println("clicked changeToMain");
            }catch(StaleElementReferenceException ex)
            {
                WebElement changeToMain = driver.findElement(By.xpath("//*[@id=\"domestic-app\"]/div/div/div[3]/div/div[1]/div[3]/div[2]/button"));
                js.executeScript("arguments[0].scrollIntoView()", changeToMain);
                wait.until(ExpectedConditions.elementToBeClickable(changeToMain));
                js.executeScript("arguments[0].click();", changeToMain);
                Thread.sleep(1000);
                System.out.println("clicked changeToMain");
            }

            /*
            try {
                WebElement changeToMain=driver.findElement(By.xpath("//*[@id=\"domestic-app\"]/div/div/div[3]/div/div[1]/div[3]/div[2]/button"));
                js.executeScript("arguments[0].click();", changeToMain);
                Thread.sleep(1000);
                System.out.println("changeToMain.isDisplayed() in try="+changeToMain.isDisplayed());

            }
            catch(org.openqa.selenium.StaleElementReferenceException ex)
            {
                WebElement changeToMain=driver.findElement(By.xpath("//*[@id=\"domestic-app\"]/div/div/div[3]/div/div[1]/div[3]/div[2]/button"));
                js.executeScript("arguments[0].click();", changeToMain);
                System.out.println("changeToMain.isDisplayed() in catch="+changeToMain.isDisplayed());

            }
            */



        }else{
            System.out.println("not available");
        }

            //*[@id="domestic-app"]/div/div/div[3]/div/div[1]/div[3]/div[2]/button/span
            //*[@id="domestic-app"]/div/div/div[3]/div/div[1]/div[3]/div[2]/button
            /*
            js.executeScript("arguments[0].scrollIntoView()", changeToMain);
            wait.until(ExpectedConditions.elementToBeClickable(changeToMain));
            js.executeScript("arguments[0].click();", changeToMain);
            Thread.sleep(5000);
            */



            System.out.println("return flight off--------------------------------------");



        return listOfResults2;


    }
}
