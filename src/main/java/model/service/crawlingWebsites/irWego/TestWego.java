package model.service.crawlingWebsites.irWego;


import model.common.Fields;
import model.service.crawlingWebsites.ghasedak24.Ghasedak24Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestWego{
    private Fields fields;

    public TestWego(Fields fields) {
        this.fields=fields;
    }

    public  void main() throws IOException {

        fields.adaptDate();

        WegoService wegoService=new WegoService("https://ir.wego.com",4000,fields);
        wegoService.setSelectedDriver("chrome");
        wegoService.main();



    }
}