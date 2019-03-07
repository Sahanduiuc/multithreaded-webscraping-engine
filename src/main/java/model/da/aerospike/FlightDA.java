package model.da.aerospike;

import com.aerospike.client.*;
import com.aerospike.client.policy.ClientPolicy;
import com.aerospike.client.policy.QueryPolicy;
import com.aerospike.client.policy.RecordExistsAction;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.client.query.Filter;
import com.aerospike.client.query.PredExp;
import com.aerospike.client.query.RecordSet;
import com.aerospike.client.query.Statement;
import model.service.crawlingWebsites.mrbilit.windows.Result2;

import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


public class FlightDA {

    private AerospikeClient client;
    private  Record userRecord ;
    private Key key ;
    private String namespace;
    private String set;
    private WritePolicy wPolicy;
    private String uniqueID;

    private String startAirline;
    private String departureStartTime;
    private String businessType;
    private String price;
/*
    private String startTime,returnTime;
    private String startDate,returnDate;
    private String startMonth,returnMonth;
    private String startCompany,returnCompany;
*/

    public FlightDA(String namespace,String set) {

        Host[] hosts = new Host[] {
                new Host("192.168.150.201", 3000),
                new Host("192.168.150.201", 3001)

        };

        this.client = new AerospikeClient(new ClientPolicy(), hosts);
        this.namespace=namespace;
        this.set=set;



    }

    public void closeAerospikeConnection(){
        client.close();
    }




    /*
        public void addFlight(String startTime,String returnTime,String startDate,String returnDate,
                              String startMonth,String returnMonth) throws AerospikeException {
    */
     public void addFlight(String startAirline, String departureStartTime,
                           String businessType, String price,
                           String startFlightNumber, String startWeighLimit,
                           String startPriceClass, String timeStamp,String crawlingFrom,
                           List<Result2> listOfResult2) throws AerospikeException {

         uniqueID = UUID.randomUUID().toString();
         this.startAirline=startAirline;
         this.departureStartTime=departureStartTime;
         key = new Key(this.namespace, this.set,this.uniqueID);
         wPolicy = new WritePolicy();
         wPolicy.recordExistsAction = RecordExistsAction.UPDATE;
         Bin bin1 = new Bin("startAirline", startAirline);
         Bin bin2 = new Bin("departureSTime", departureStartTime);
         Bin bin3 = new Bin("uniqueID",uniqueID);
         Bin bin4 = new Bin("myinteger", 9);
         Bin bin5=  new Bin("businessType", businessType);
         Bin bin6=  new Bin("price", price);
         Bin bin7=  new Bin("sFlightNumber", startFlightNumber);
         Bin bin8=  new Bin("sWeighLimit", startWeighLimit);
         Bin bin9=  new Bin("sPriceClass", startPriceClass);
         Bin bin10=  new Bin("timestamp", timeStamp);
         Bin bin11=  new Bin("crawlingFrom", crawlingFrom);

         if(client.isConnected()) {
             System.out.println("connection to aerospike client sucessful");
             client.put(wPolicy, key, bin1, bin2,bin3,bin4,bin5,bin6,bin7,
                     bin8,bin9,bin10,bin11);
         }

         Record record = client.get(null, key);
         System.out.println("startAirline in database is:"+record.getValue("startAirline"));
         System.out.println("departureStartTime in database is:"+record.getValue("departureSTime"));
         System.out.println("businessType in database is:"+record.getValue("businessType"));
         System.out.println("price in database is:"+record.getValue("price"));
         System.out.println("sFlightNumber in database is:"+record.getValue("sFlightNumber"));
         System.out.println("sWeighLimit in database is:"+record.getValue("sWeighLimit"));
         System.out.println("sPriceClass in database is:"+record.getValue("sPriceClass"));
         System.out.println("timestamp in database is:"+record.getValue("timestamp"));

     }



    public void getFlight(String uniqueID) throws AerospikeException {
        this.uniqueID=uniqueID;
        key = new Key(this.namespace, this.set,this.uniqueID);
        userRecord = client.get(null, key);
        System.out.println("startAirline="+userRecord.getValue("startAirline"));
        System.out.println("departureStartTime="+userRecord.getValue("departureSTime"));
    }


    public void deleteFlight(String uniqueID){
         this.uniqueID=uniqueID;
         key = new Key(this.namespace, this.set,this.uniqueID);
         client.delete(wPolicy,key);
         System.out.println("deleted uniqueID:"+uniqueID);
    }


    public void selectAll(){
        Statement stmt = new Statement();
        stmt.setNamespace(this.namespace);
        stmt.setSetName(this.set);
        stmt.setBinNames("startAirline","departureSTime");
        QueryPolicy queryPolicy=null;
        RecordSet recordSet=client.query(queryPolicy, stmt);
        try {
            while (recordSet.next()) {
                Key key = recordSet.getKey();
                Record record = recordSet.getRecord();
                System.out.println("found uniqueID="+record.getValue("uniqueID"));
                System.out.println("found startAirline="+record.getValue("startAirline"));
                System.out.println("found departureStartTime="+record.getValue("departureSTime"));
            }
        }
        catch (Exception e){
            System.out.println("cause="+e.getCause());
        }
    }

    public void findOne(String startAirline, String departureStartTime){

        Statement stmt = new Statement();
        stmt.setNamespace(this.namespace);
        stmt.setSetName(this.set);
        stmt.setBinNames("startAirline","startAirline","myinteger");
        stmt.setPredExp(PredExp.stringBin("startAirline"),
                PredExp.stringValue(startAirline),

                PredExp.stringEqual());


        stmt.setPredExp(PredExp.stringBin("departureSTime"),
                PredExp.stringValue(departureStartTime),
                PredExp.stringEqual());
        QueryPolicy queryPolicy=null;

        RecordSet recordSet=client.query(queryPolicy, stmt);
        try {
            while (recordSet.next()) {
                Key key = recordSet.getKey();

                Record record = recordSet.getRecord();

                System.out.println("key="+recordSet.getKey());
                System.out.println("found only one startAirline="+record.getValue("startAirline"));
                System.out.println("found only one departureStartTime="+record.getValue("departureSTime"));

            }
        }
        catch (Exception e){
            System.out.println("cause="+e.getCause());
        }
    }
}
