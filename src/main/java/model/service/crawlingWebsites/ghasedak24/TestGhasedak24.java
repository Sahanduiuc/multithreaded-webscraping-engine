package model.service.crawlingWebsites.ghasedak24;


import model.common.Fields;
import model.service.crawlingWebsites.flytoday.FlytodayService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TestGhasedak24 {
    private Fields fields;

    public TestGhasedak24(Fields fields) {
        this.fields=fields;
    }

    public void main() throws ExecutionException, IOException {
        fields.adaptDate();
        Ghasedak24Service ghasedak24Service=new Ghasedak24Service("https://www.ghasedak24.com",4000,fields);
        ghasedak24Service.setSelectedDriver("firefox");
        ghasedak24Service.main();

    }
}