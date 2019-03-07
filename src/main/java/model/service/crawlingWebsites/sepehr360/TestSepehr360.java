package model.service.crawlingWebsites.sepehr360;


import model.common.Fields;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class TestSepehr360 {
    private Fields fields;

    public TestSepehr360(Fields fields) {
        this.fields=fields;
    }

    public void main() throws ExecutionException, IOException {
        fields.adaptDate();
        Sepehr360Service sepehr360Service=new Sepehr360Service("https://sepehr360.ir",4000,fields);
        sepehr360Service.setSelectedDriver("chrome");
        sepehr360Service.main();
    }
}