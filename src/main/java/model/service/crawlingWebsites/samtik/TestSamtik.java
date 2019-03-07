package model.service.crawlingWebsites.samtik;


import model.common.Fields;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class TestSamtik {
    private Fields fields;

    public TestSamtik(Fields fields) {
        this.fields=fields;
    }

    public  void main() throws ExecutionException, IOException {
     fields.adaptDate();
        System.out.println("fields.getRow()="+fields.getRow());
        System.out.println("fields.getColumn()="+fields.getColumn());

        System.out.println("fields.getReturnRow()="+fields.getReturnRow());
        System.out.println("fields.getReturnColumn()="+fields.getReturnColumn());


        SamtikService samtikService=new SamtikService("https://www.samtik.ir",4000,fields);
        samtikService.setSelectedDriver("firefox");
        samtikService.main();
    }
}