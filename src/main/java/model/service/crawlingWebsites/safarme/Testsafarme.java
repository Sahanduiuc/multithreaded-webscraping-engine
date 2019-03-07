package model.service.crawlingWebsites.safarme;


import model.common.Fields;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

 public class Testsafarme {

     private Fields fields;

     public Testsafarme(Fields fields) {
         this.fields=fields;
     }

     public  void main() throws ExecutionException, IOException, URISyntaxException {
         fields.setDepartureLocation(fields.convertToPersian(fields.getDepartureLocation()));
         fields.setArrivalLocation(fields.convertToPersian(fields.getArrivalLocation()));

            fields.adaptDate();
            fields.setStartDate(fields.getStartDate()+2);
            System.out.println("fields.getStartDate()="+fields.getStartDate());

         safarmeService safarmeService=new safarmeService("https://www.safarme.com",4000,fields);
         safarmeService.setSelectedDriver("firefox");
         safarmeService.main();


     }




}