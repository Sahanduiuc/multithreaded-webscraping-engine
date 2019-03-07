package model.service.crawlingWebsites.zoraq;


import model.common.Fields;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestZoraq {
    private Fields fields;

    public TestZoraq(Fields fields) {
        this.fields=fields;

    }

    public  void main() throws IOException {
        fields.adaptDate();
        fields.setRow(fields.getRow()+2);
        System.out.println("row="+fields.getRow());
        System.out.println("column="+fields.getColumn());
        ZoraqService zoraqService=new ZoraqService("https://www.zoraq.com",4000,fields);
        zoraqService.setSelectedDriver("firefox");
        zoraqService.main();

    }
}