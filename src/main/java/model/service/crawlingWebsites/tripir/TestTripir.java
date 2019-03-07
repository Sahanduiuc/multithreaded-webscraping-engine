package model.service.crawlingWebsites.tripir;


import model.common.Fields;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TestTripir {
    private Fields fields;

    public TestTripir(Fields fields) {
        this.fields=fields;
    }

    public  void main() throws ExecutionException, IOException {
    fields.adaptDate();
        fields.setRow(fields.getRow()+1);
        fields.setReturnRow(fields.getReturnRow()+8);
        System.out.println("returnRow="+fields.getReturnRow());
        System.out.println("returnColumn="+fields.getReturnColumn());

        TripirService tripirService=new TripirService("https://www.trip.ir",4000,fields);
        tripirService.setSelectedDriver("firefox");
        tripirService.main();
    }
}