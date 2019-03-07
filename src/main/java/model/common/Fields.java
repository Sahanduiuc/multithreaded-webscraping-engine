package model.common;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Fields.
 *
 * @author farshadnsh
 * @version 1.0
 * @since 2018 -06-10
 */
public class Fields {

    private String departureLocation,arrivalLocation;

    private int startDate,returnDate;

    private int offset,returnOffset;

    private String status;

    private String startMonth, returnMonth;

    private int row,column, returnRow,returnColumn;

    private HashMap<String,String> hm;

    public void usePersianDictionary(){
        hm=new HashMap<String,String>();
        hm.put("TBZ","تبریز");
        hm.put("THR","تهران");
        hm.put("MHD","مشهد");
        hm.put("KIH","کیش");  //kish
        hm.put("IFN","اصفهان");
        hm.put("BND","بندرعباس"); //bandarabas
        hm.put("ZAH","زاهدان"); //zahedan
        hm.put("SRY","ساری"); //sari
        hm.put("KER","کرمان");  //kerman
        hm.put("ZBR","چابهار"); //chabahar
        hm.put("AZD","یزد"); //yazd
        hm.put("GBT","گرگان"); //gorgan
        hm.put("RAS","رشت"); //rasht


      /*
        for(Map.Entry m:hm.entrySet()){
            System.out.println(m.getKey()+" "+m.getValue());
            }
    */
        //System.out.println(hm.get("TBZ"));
    }


    public String convertToPersian(String city){
       return hm.get(city);
    }

    /**
     * Instantiates a new Fields.
     *
     * @param departureLocation this is the departure location of the flight
     * @param arrivalLocation   the arrival location
     * @param startDate         the start date
     * @param returnDate        the return date
     * @param startMonth        the start month
     * @param returnMonth       the return month
     */
    public Fields(String departureLocation, String arrivalLocation, int startDate, int returnDate,String startMonth,String returnMonth){
        this.departureLocation=departureLocation;
        this.arrivalLocation=arrivalLocation;
        this.startDate=startDate;
        this.returnDate=returnDate;
        this.startMonth=startMonth;
        this.returnMonth=returnMonth;
        usePersianDictionary();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void adaptDate(){

          calculateOffsetforStartMonth();
          calculateOffsetforReturnMonth();
        System.out.println("returnOffset="+returnOffset);
        System.out.println("Offset="+offset);


        int remainder=startDate%7;
          int remainder2=returnDate%7;
          System.out.println("remainder="+remainder);
          System.out.println("remainder2="+remainder2);


        int temp1=(startDate/7);
          int temp2=(returnDate/7);

          if (offset<=5){
              row=temp1+1;
          }else{
              row=temp1+2;
          }
          column=remainder+offset;

          if (returnOffset<5){
              returnRow=temp2+1;
          }else {
              returnRow=temp2+2;
          }
          returnColumn=remainder2+returnOffset;
          if (returnColumn>7){
              returnColumn=returnColumn%7;
              System.out.println("returnColumn in here:"+returnColumn);
          }


    }

    private void calculateOffsetforReturnMonth() {
        if ((returnMonth=="مرداد")){
            returnOffset=2;
        //}else if(returnMonth=="شهريور") {
        //    returnOffset=5;
        }else if(returnMonth.equals("شهريور")) {
            returnOffset=5;
        }else if (returnMonth=="مهر"){
            returnOffset=1;
        }else if (returnMonth=="آبان"){
            returnOffset=3;
        }else if (returnMonth=="آذر"){
            returnOffset=5;
        }else if (returnMonth=="دی"){
            returnOffset=0;
        }else if (returnMonth=="بهمن"){
            returnOffset=2;
        }else if (returnMonth=="اسفند"){
            returnOffset=4;
        }else if (returnMonth=="فروردین"){
            returnOffset=5;
        }else if (returnMonth=="اردیبهشت"){
            returnOffset=1;
        }else if (startMonth=="خرداد"){
            returnOffset=4;
        }else {
            returnOffset=0; //tir  1397
        }
    }

    public void calculateOffsetforStartMonth(){
        offset=0;
        returnOffset=0;
        if ((startMonth=="مرداد")){
            offset=2; //mordad=2
        }else if(startMonth=="شهریور") {
            //           shahrivar=5   //mehr=1  aban=3  azar=5 dey=0
            //bahman=2 esfand=4  farvardin=5  ordibehesht=1  khordad=4
            //tir=0
            offset=5;

        }else if (startMonth=="مهر"){
            offset=1;
        }else if (startMonth=="آبان"){
            offset=3;
        }else if (startMonth=="آذر"){
            offset=5;
        }else if (startMonth=="دی"){
            offset=0;
        }else if (startMonth=="بهمن"){
            offset=2;
        }else if (startMonth=="اسفند"){
            offset=4;
        }else if (startMonth=="فروردین"){
            offset=5;
        }else if (startMonth=="اردیبهشت"){
            offset=1;
        }else if (startMonth=="خرداد"){
            offset=4;
        }else {
            offset=0; //tir  1397
        }



    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getReturnOffset() {
        return returnOffset;
    }

    public void setReturnOffset(int returnOffset) {
        this.returnOffset = returnOffset;
    }

    public int getReturnRow() {
        return returnRow;
    }

    public void setReturnRow(int returnRow) {
        this.returnRow = returnRow;
    }

    public int getReturnColumn() {
        return returnColumn;
    }

    public void setReturnColumn(int returnColumn) {
        this.returnColumn = returnColumn;
    }
    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
    /**
     * Gets start month.
     *
     * @return the start month
     */
    public String getStartMonth() {
        return startMonth;
    }

    /**
     * Sets start month.
     *
     * @param startMonth the start month
     */
    public void setStartMonth(String startMonth) {
        this.startMonth = startMonth;
    }

    /**
     * Gets return month.
     *
     * @return the return month
     */
    public String getReturnMonth() {
        return returnMonth;
    }

    /**
     * Sets return month.
     *
     * @param returnMonth the return month
     */
    public void setReturnMonth(String returnMonth) {
        this.returnMonth = returnMonth;
    }


    /**
     * Gets start date.
     *
     * @return the start date
     */
    public int getStartDate() {
        return startDate;
    }

    /**
     * Sets start date.
     *
     * @param startDate the start date
     */
    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets return date.
     *
     * @return the return date
     */
    public int getReturnDate() {
        return returnDate;
    }

    /**
     * Sets return date.
     *
     * @param returnDate the return date
     */
    public void setReturnDate(int returnDate) {
        this.returnDate = returnDate;
    }


    /**
     * Gets departure location.
     *
     * @return the departure location
     */
    public String getDepartureLocation() {
        return departureLocation;
    }

    /**
     * Sets departure location.
     *
     * @param departureLocation the departure location
     */
    public void setDepartureLocation(String departureLocation) {
        this.departureLocation = departureLocation;
    }

    /**
     * Gets arrival location.
     *
     * @return the arrival location
     */
    public String getArrivalLocation() {
        return arrivalLocation;
    }

    /**
     * Sets arrival location.
     *
     * @param arrivalLocation the arrival location
     */
    public void setArrivalLocation(String arrivalLocation) {
        this.arrivalLocation = arrivalLocation;
    }





}
