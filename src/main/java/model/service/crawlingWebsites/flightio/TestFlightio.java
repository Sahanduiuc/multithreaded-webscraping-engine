package model.service.crawlingWebsites.flightio;


import model.common.Fields;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class TestFlightio {
    private Fields fields;

    public TestFlightio(Fields fields) {
        this.fields=fields;
    }

    public  void main() throws ExecutionException, IOException {
        fields.adaptDate();
        FlightioService flightioService=new FlightioService("https://flightio.com",4000,fields);
        flightioService.setSelectedDriver("firefox");
        flightioService.main();
    }
}