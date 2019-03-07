package model.service.crawlingWebsites.irWego;

import java.text.SimpleDateFormat;

public class Result2 {
    private String returnAirline;
    private String departureReturnTime;
    private String returnBusinessType;
    private String returnPrice;
    private String returnFlightNumber;
    private String returnWeighLimit;
    private String returnPriceClass;
    private String timeStamp;
    public boolean flag;

    Result2(){
        flag=false;
    }

    public String getReturnAirline() {
        return returnAirline;
    }

    public void setReturnAirline(String returnAirline) {
        this.returnAirline = returnAirline;
    }

    public String getDepartureReturnTime() {
        return departureReturnTime;
    }

    public void setDepartureReturnTime(String departureReturnTime) {
        this.departureReturnTime = departureReturnTime;
    }

    public String getReturnBusinessType() {
        return returnBusinessType;
    }

    public void setReturnBusinessType(String returnBusinessType) {
        this.returnBusinessType = returnBusinessType;
    }

    public String getReturnPrice() {
        return returnPrice;
    }

    public void setReturnPrice(String returnPrice) {
        this.returnPrice = returnPrice;
    }

    public String getReturnFlightNumber() {
        return returnFlightNumber;
    }

    public void setReturnFlightNumber(String returnFlightNumber) {
        this.returnFlightNumber = returnFlightNumber;
    }

    public String getReturnWeighLimit() {
        return returnWeighLimit;
    }

    public void setReturnWeighLimit(String returnWeighLimit) {
        this.returnWeighLimit = returnWeighLimit;
    }

    public String getReturnPriceClass() {
        return returnPriceClass;
    }

    public void setReturnPriceClass(String returnPriceClass) {
        this.returnPriceClass = returnPriceClass;
    }




    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp() {
        SimpleDateFormat time_formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss.SSS");
        String current_time_str = time_formatter.format(System.currentTimeMillis());
        this.timeStamp = current_time_str;
    }



}
