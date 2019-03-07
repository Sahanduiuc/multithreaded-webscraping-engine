package model.common;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * The type Spider.
 *
 * @author farshadnsh
 * @version 1
 * @since 2018 -06-10
 */
public class Spider  {


    private int delay;
    private String url;
   // private FirefoxDriver driver;
    private String selectedDriver;
    private Fields fields;
    private WebDriver driver;


    /**
     * Instantiates a new Spider.
     * @param url    the url
     * @param delay  the delay
     * @param fields this is the object to initialize parameters              for the crawler to search which is created                by Farshad Noravesh
     */
    public Spider(String  selectedDriver, String url, int delay, Fields fields)
    {
        this.selectedDriver=selectedDriver;
        this.delay = delay;
        this.url=url;
        this.fields=fields;





    }

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Gets delay.
     *
     * @return int this returns the delaye of the crawler
     */
    public int getDelay() {
        return delay;
    }

    /**
     * Gets url.
     *
     * @return String this returns the url of the crawler
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets delay.
     *
     * @param delay the delay
     */
    public void setDelay(int delay) {
        this.delay = delay;
    }

    /**
     * Sets url.
     *
     * @param url the url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Do sync process.
     *
     * @param template the template
     * @throws InterruptedException the interrupted exception
     * @throws IOException          the io exception
     */
    public void doSyncProcess(Template template) throws InterruptedException, IOException, ExecutionException {
        if (selectedDriver.equals("chrome")){
            System.out.println("chrome is chosen!");
            System.setProperty("webdriver.chrome.driver", "chromedriver");
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
             driver = new ChromeDriver();
             setDriver(driver);

        }else if (selectedDriver.equals("firefox")){
            System.out.println("firfox is chosen!");
            System.setProperty("webdriver.gecko.driver", "geckodriver");
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--headless");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
             driver=new FirefoxDriver(options);
             setDriver(driver);
        }

        driver.get(this.url);
        Thread.sleep(this.delay);
        System.out.println("---starting spider in "+Thread.currentThread().getName());
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        template.run();

    }
    /*
    public void printResults(){

    }
    */

}
