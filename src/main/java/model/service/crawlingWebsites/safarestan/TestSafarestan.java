package model.service.crawlingWebsites.safarestan;


import model.common.Fields;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestSafarestan {

    private Fields fields;

    public TestSafarestan(Fields fields) {
        this.fields=fields;
    }

    public  void main() throws IOException {
        fields.adaptDate();
        //fields.setDepartureLocation("THR-0");
        //fields.setArrivalLocation("IFN-0");
        fields.setDepartureLocation(fields.getDepartureLocation()+"-0");
        fields.setArrivalLocation(fields.getArrivalLocation()+"-0");

        SafarestanService safarestanService=new SafarestanService("https://www.safarestan.com",3000,fields);
        safarestanService.setSelectedDriver("chrome");
        safarestanService.main();

    }
}