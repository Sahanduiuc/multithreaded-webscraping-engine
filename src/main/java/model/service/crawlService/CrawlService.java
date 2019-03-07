package model.service.crawlService;

import model.common.Fields;
import model.service.crawlingWebsites.alibaba.TestAlibaba;
import model.service.crawlingWebsites.flightio.TestFlightio;
import model.service.crawlingWebsites.flytoday.TestFlytoday;
import model.service.crawlingWebsites.ghasedak24.TestGhasedak24;
import model.service.crawlingWebsites.irWego.TestWego;
import model.service.crawlingWebsites.mrbilit.*;
import model.service.crawlingWebsites.safarestan.TestSafarestan;
import model.service.crawlingWebsites.safarme.Testsafarme;
import model.service.crawlingWebsites.samtik.TestSamtik;
import model.service.crawlingWebsites.sepehr360.TestSepehr360;
import model.service.crawlingWebsites.simorgh24.TestSimorgh24;
import model.service.crawlingWebsites.tripir.TestTripir;
import model.service.crawlingWebsites.zoraq.TestZoraq;


import java.io.*;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class CrawlService {

    private Fields fields;

    public CrawlService( Fields fields) throws IOException {
        this.fields=fields;
    }

    public static void main(String[] args) throws IOException, ExecutionException, URISyntaxException {
        //مرداد

        //Fields fields=new Fields("تهران", "تبریز", 23 , 26,"مرداد", "شهريور");
        Fields fields=new Fields("THR", "MHD", 23 , 26,"مرداد", "شهريور");





       // System.out.println("*TestMrbilit**********************");
        //new TestMrbilit(fields).main();


        //System.out.println("*TestFlightio**********************");
        //new TestFlightio(fields).main();


        //System.out.println("*TestFlytoday**********************");
        //new TestFlytoday(fields).main();

        //System.out.println("*TestGhasedak24**********************");
        //new TestGhasedak24(fields).main();

        //System.out.println("*TestWego**********************");
        //new TestWego(fields).main();


        //System.out.println("*Testsafarme**********************");
        //new Testsafarme(fields).main();

        //System.out.println("*TestSamtik**********************");
        //new TestSamtik(fields).main();

        //System.out.println("*TestSepehr360**********************");
        //new TestSepehr360(fields).main();


        //System.out.println("*TestTripir**********************");
        //new TestTripir(fields).main();

        //System.out.println("*TestSimorgh24************************");
        //new TestSimorgh24(fields).main();

        //System.out.println("*TestZoraq**********************");
        //new TestZoraq(fields).main();

        //System.out.println("*TestAlibaba**********************");
        //new TestAlibaba(fields).main();

        System.out.println("*TestSafarestan**********************");
        new TestSafarestan(fields).main();

        /*



        */
    }

}
