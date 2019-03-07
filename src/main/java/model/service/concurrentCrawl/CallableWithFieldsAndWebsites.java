package model.service.concurrentCrawl;

import model.common.Fields;
import model.service.crawlingWebsites.alibaba.TestAlibaba;
import model.service.crawlingWebsites.flightio.TestFlightio;
import model.service.crawlingWebsites.flytoday.TestFlytoday;
import model.service.crawlingWebsites.ghasedak24.TestGhasedak24;
import model.service.crawlingWebsites.irWego.TestWego;
import model.service.crawlingWebsites.mrbilit.TestMrbilit;
import model.service.crawlingWebsites.safarestan.TestSafarestan;
import model.service.crawlingWebsites.safarme.Testsafarme;
import model.service.crawlingWebsites.samtik.TestSamtik;
import model.service.crawlingWebsites.sepehr360.TestSepehr360;
import model.service.crawlingWebsites.simorgh24.TestSimorgh24;
import model.service.crawlingWebsites.tripir.TestTripir;
import model.service.crawlingWebsites.zoraq.TestZoraq;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

/**
 * The type Callable with fields and websites.
 * @author farshad noravesh
 */
public class CallableWithFieldsAndWebsites  {


    private Order order;


    /**
     * Instantiates a new Callable with fields and websites.
     *
     * @param order the order
     */
    public CallableWithFieldsAndWebsites(Order order) {
        this.order=order;

    }


    /**
     * Main order.
     *
     * @return the order
     * @throws Exception the exception
     */
    public Order main() throws Exception {
        switch (order.getWebsite()){
            case "mrbilit":
                try {
                    System.out.println("*TestMrbilit"+" inside "+Thread.currentThread().getName()+"**********************");
                    new TestMrbilit(order.getFields()).main();
                    order.getFields().setStatus("done");


                }catch (Exception e){
                    System.out.println("e.getCause()="+e.getMessage()+" inside "+Thread.currentThread().getName());
                    order.getFields().setStatus("failed");
                }
                break;
             case "flightio":
                 try {
                     System.out.println("*TestFlightio"+" inside "+Thread.currentThread().getName()+"**********************");
                     new TestFlightio(order.getFields()).main();
                     order.getFields().setStatus("done");


                 }catch (Exception e){
                     System.out.println("e.getCause()="+e.getMessage()+" inside "+Thread.currentThread().getName());
                     order.getFields().setStatus("failed");

                 }
                 break;
             case "tripir":
                try {
                    System.out.println("*TestTripir"+" inside "+Thread.currentThread().getName()+"**********************");
                    new TestTripir(order.getFields()).main();
                    order.getFields().setStatus("done");


                }catch (Exception e){
                    System.out.println("e.getCause()="+e.getMessage()+" inside "+Thread.currentThread().getName());
                    order.getFields().setStatus("failed");


                }
                break;
            case "zoraq":
                try {
                    System.out.println("*TestZoraq"+" inside "+Thread.currentThread().getName()+"**********************");
                    new TestZoraq(order.getFields()).main();
                    order.getFields().setStatus("done");


                }catch (Exception e){
                    System.out.println("e.getCause()="+e.getMessage()+" inside "+Thread.currentThread().getName());
                    order.getFields().setStatus("failed");

                }
                break;
            case "alibaba":
                try {
                    System.out.println("*TestAlibaba"+" inside "+Thread.currentThread().getName()+"**********************");
                    new TestAlibaba(order.getFields()).main();
                    order.getFields().setStatus("done");


                }catch (Exception e){
                    System.out.println("e.getCause()="+e.getMessage()+" inside "+Thread.currentThread().getName());
                    order.getFields().setStatus("failed");

                }
                break;
            case "samtik":
                try {
                    System.out.println("*TestSamtik"+" inside "+Thread.currentThread().getName()+"**********************");
                    new TestSamtik(order.getFields()).main();
                    order.getFields().setStatus("done");

                }catch (Exception e){
                    System.out.println("e.getCause()="+e.getMessage()+" inside "+Thread.currentThread().getName());
                    order.getFields().setStatus("failed");

                }
                break;
            case "flytoday":
                try {
                    System.out.println("*TestFlytoday"+" inside "+Thread.currentThread().getName()+"**********************");
                    new TestFlytoday(order.getFields()).main();
                    order.getFields().setStatus("done");

                }catch (Exception e){
                    System.out.println("e.getCause()="+e.getMessage()+" inside "+Thread.currentThread().getName());
                    order.getFields().setStatus("failed");

                }
                break;
            case "ghasedak24":
                try {
                    System.out.println("*TestGhasedak24"+" inside "+Thread.currentThread().getName()+"**********************");
                    new TestGhasedak24(order.getFields()).main();
                    order.getFields().setStatus("done");


                }catch (Exception e){
                    System.out.println("e.getCause()="+e.getMessage()+" inside "+Thread.currentThread().getName());
                    order.getFields().setStatus("failed");

                }
                break;
            case "safarme":
                try {
                    System.out.println("*TestSafarme"+" inside "+Thread.currentThread().getName()+"**********************");
                    new Testsafarme(order.getFields()).main();
                    order.getFields().setStatus("done");
                }catch (Exception e){
                    System.out.println("e.getCause()="+e.getMessage()+" inside "+Thread.currentThread().getName());
                    order.getFields().setStatus("failed");
                }
                break;
            case "simorgh24":
                try {
                    System.out.println("*TestSimorgh24"+" inside "+Thread.currentThread().getName()+"**********************");
                    new TestSimorgh24(order.getFields()).main();
                    order.getFields().setStatus("done");
                }catch (Exception e){
                    System.out.println("e.getCause()="+e.getMessage()+" inside "+Thread.currentThread().getName());
                    order.getFields().setStatus("failed");
                }
                break;
            case "irwego":
                try {
                    System.out.println("*TestWego"+" inside "+Thread.currentThread().getName()+"**********************");
                    new TestWego(order.getFields()).main();
                    order.getFields().setStatus("done");
                }catch (Exception e){
                    System.out.println("e.getCause()="+e.getMessage()+" inside "+Thread.currentThread().getName());
                    order.getFields().setStatus("failed");

                }
                break;
            case "safarestan":
                try {
                    System.out.println("*TestSafarestan"+" inside "+Thread.currentThread().getName()+"**********************");
                    new TestSafarestan(order.getFields()).main();
                    order.getFields().setStatus("done");
                }catch (Exception e){
                    System.out.println("e.getCause()="+e.getMessage()+" inside "+Thread.currentThread().getName());
                    order.getFields().setStatus("failed");

                }
                break;
            case "sepehr360":
                try {
                    System.out.println("*TestSepehr360"+" inside "+Thread.currentThread().getName()+"**********************");
                    new TestSepehr360(order.getFields()).main();
                    order.getFields().setStatus("done");
                }catch (Exception e){
                    System.out.println("e.getCause()="+e.getMessage()+" inside "+Thread.currentThread().getName());
                    order.getFields().setStatus("failed");
                }
                break;
        }

        return  order;
    }



}
