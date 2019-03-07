package model.service.crawlingWebsites.mrbilit;


import model.common.Fields;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TestMrbilit {
    private Fields fields;

    public TestMrbilit(Fields fields) {
        this.fields = fields;
    }

    public void main() throws ExecutionException, IOException {

        fields.setDepartureLocation(fields.convertToPersian(fields.getDepartureLocation()));
        fields.setArrivalLocation(fields.convertToPersian(fields.getArrivalLocation()));


        fields.adaptDate();
        fields.setStartDate(fields.getStartDate()+2);

        System.out.println("fields.getStartDate()="+fields.getStartDate());

        MrbilitService mrbilitService=new MrbilitService("https://mrbilit.com",4000,fields);
        mrbilitService.setSelectedDriver("firefox");



        mrbilitService.main();
    }


}