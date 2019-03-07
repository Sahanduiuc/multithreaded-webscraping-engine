package model.service.crawlingWebsites.flytoday;


import model.common.Fields;
import model.service.crawlingWebsites.flightio.FlightioService;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class TestFlytoday {
    private Fields fields;
    public TestFlytoday(Fields fields) {
        this.fields=fields;
    }

    public  void main() throws ExecutionException, IOException {
        fields.adaptDate();
        FlytodayService flytodayService=new FlytodayService("https://www.flytoday.ir",4000,fields);
        flytodayService.setSelectedDriver("firefox");
        flytodayService.main();
    }
}