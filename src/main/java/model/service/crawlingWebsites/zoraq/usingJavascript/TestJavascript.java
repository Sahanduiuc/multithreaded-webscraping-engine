package model.service.crawlingWebsites.zoraq.usingJavascript;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

public class TestJavascript {
    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "/home/farshadnsh/Documents/chromedriver");

        ChromeDriver driver=new ChromeDriver();
        driver.get("https://ir.wego.com");
        Thread.sleep(4000);
        JavascriptExecutor js=(JavascriptExecutor) driver;
        //String domain_name=(String) js.executeScript("return document.getElementById('app')");
        List<Object> domain_name= (List<Object>) js.executeScript("return document.getElementsByTagName('div')");
        Thread.sleep(1000);
        System.out.println(domain_name.get(0).toString());
        driver.close();
    }
}


//document.getElementById("app").shadowRoot.getElementById("pages").querySelector("#flights-search").shadowRoot.querySelector(".content.layout.horizontal")