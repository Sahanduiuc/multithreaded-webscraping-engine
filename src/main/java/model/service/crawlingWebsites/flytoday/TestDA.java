package model.service.crawlingWebsites.flytoday;

import model.da.aerospike.FlightDA;


public class TestDA {

    public static void main(String[] args) throws InterruptedException {
        FlightDA flightDA=new FlightDA("flytoday","flights");


        //flightDA.addFlight("20","21"); //flightid=1
        //flightDA.getFlight("2");
        Thread.sleep(3000);
       // flightDA.deleteFlight("1");
        //flightDA.selectAll();
        flightDA.findOne("10.12","13");
        flightDA.closeAerospikeConnection();
    }
}
