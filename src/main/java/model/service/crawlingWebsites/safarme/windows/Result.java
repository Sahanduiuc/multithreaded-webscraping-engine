package model.service.crawlingWebsites.safarme.windows;

import java.text.SimpleDateFormat;
import java.util.List;

public class Result {

    private String startAirline;
    private String departureStartTime;
    private String arrivalStartTime;
    private String businessType;
    private String price;
    private String startFlightNumber;
    private String startWeighLimit;
    private String startPriceClass;//economy
    private String startCancelType; //ghabele esterdad
    private String timeStamp;
    private List<Result2> listOfResult2;
    private String startCapacity;
    private String startSellingType;//system vs charter
    private String startAirplaneType;

    public String getStartAirplaneType() {
        return startAirplaneType;
    }

    public void setStartAirplaneType(String startAirplaneType) {
        this.startAirplaneType = startAirplaneType;
    }

    public String getStartSellingType() {
        return startSellingType;
    }

    public void setStartSellingType(String startSellingType) {
        this.startSellingType = startSellingType;
    }



    public String getStartCancelType() {
        return startCancelType;
    }

    public void setStartCancelType(String startCancelType) {
        this.startCancelType = startCancelType;
    }




    public String getArrivalStartTime() {
        return arrivalStartTime;
    }

    public void setArrivalStartTime(String arrivalStartTime) {
        this.arrivalStartTime = arrivalStartTime;
    }



    public String getStartCapacity() {
        return startCapacity;
    }

    public void setStartCapacity(String startCapacity) {
        this.startCapacity = startCapacity;
    }



    public List<Result2> getResult2() {
        return listOfResult2;
    }

    public void setResult2(List<Result2> listOfResult2) {
        this.listOfResult2 = listOfResult2;
    }



    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp() {
        SimpleDateFormat time_formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss.SSS");
        String current_time_str = time_formatter.format(System.currentTimeMillis());
        this.timeStamp = current_time_str;
    }



    public String getStartFlightNumber() {

        return startFlightNumber;
    }

    public void setStartFlightNumber(String startFlightNumber) {
        this.startFlightNumber = startFlightNumber;
    }

    public String getStartWeighLimit() {
        return startWeighLimit;
    }

    public void setStartWeighLimit(String startWeighLimit) {
        this.startWeighLimit = startWeighLimit;
    }

    public String getStartPriceClass() {
        return startPriceClass;
    }

    public void setStartPriceClass(String startPriceClass) {
        this.startPriceClass = startPriceClass;
    }

    public String getStartAirline() {
        return startAirline;
    }

    public void setStartAirline(String startAirline) {
        this.startAirline = startAirline;
    }

    public String getDepartureStartTime() {
        return departureStartTime;
    }

    public void setDepartureStartTime(String departureStartTime) {
        this.departureStartTime = departureStartTime;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    Result(){
        setTimeStamp();
        System.out.println("-----------result------------------------");
        System.out.println("timeStamp="+timeStamp);
    }



}
