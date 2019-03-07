package model.service.crawlingWebsites.simorgh24;


import model.common.Fields;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class TestSimorgh24 {

    private Fields fields;
    public TestSimorgh24(Fields fields) {
        this.fields=fields;
    }

    public  void main() throws ExecutionException, IOException {
        fields.adaptDate();
        Simorgh24Service simorgh24Service=new Simorgh24Service("https://simorgh24.com",4000,fields);
        simorgh24Service.setSelectedDriver("firefox");
        simorgh24Service.main();
    }
}