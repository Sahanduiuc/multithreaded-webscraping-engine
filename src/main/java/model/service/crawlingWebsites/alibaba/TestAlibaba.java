package model.service.crawlingWebsites.alibaba;


import model.common.Fields;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TestAlibaba {
    private Fields fields;

    public TestAlibaba(Fields fields) {
        this.fields=fields;

    }

    public  void main() throws ExecutionException, IOException {
        fields.adaptDate();
        fields.setRow(fields.getRow()+2);
        fields.setReturnRow(fields.getReturnRow()+2);
        System.out.println("row= "+fields.getRow()+ " ,column="+fields.getColumn());
        System.out.println("returnRow= "+fields.getReturnRow()+ " ,returnColumn="+fields.getReturnColumn());

        AlibabaService alibabaService=new AlibabaService("https://www.alibaba.ir",2000,fields);
        alibabaService.setSelectedDriver("chrome");
        alibabaService.main();
    }
}