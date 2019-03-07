package model.service.concurrentCrawl;

import com.google.common.reflect.TypeToken;
import model.common.Fields;
import model.da.redis.OrderDA;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * The type Queue manager.
 * @author farshad noravesh
 */
public class QueueManager {


    private static int numberOfWorkers;
    private static LinkedBlockingQueue<Order> masterQueue;
    private static LinkedBlockingQueue<Order> serverQueue1;
    private static LinkedBlockingQueue<Order> serverQueue2;
    private static OrderDA orderDA;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws InterruptedException the interrupted exception
     */
    public static void main(String[] args) throws InterruptedException {
        numberOfWorkers=2;
        orderDA=new OrderDA();
        //orderDA.setFailedServerName("serverx");

        System.out.println("deleted server1="+orderDA.delete("server1"));
        System.out.println("deleted server2="+orderDA.delete("server2"));


        List<String> websiteOrders=new ArrayList<>();
        List<Fields> fieldOrders=new ArrayList<>();
        List<Order> listOfOrders=new ArrayList<>();
        masterQueue = new LinkedBlockingQueue<Order>();
        serverQueue1=new LinkedBlockingQueue<Order>();
        serverQueue2=new LinkedBlockingQueue<Order>();

        fieldOrders.add(new Fields("THR", "MHD", 27 , 29,"مهر", "آبان"));
        fieldOrders.add(new Fields("THR", "TBZ", 27 , 29,"مهر", "آبان"));
        fieldOrders.add(new Fields("THR", "TBZ", 27 , 29,"مهر", "آبان"));
        fieldOrders.add(new Fields("THR", "TBZ", 27 , 29,"مهر", "آبان"));
        fieldOrders.add(new Fields("THR", "SYZ", 27 , 29,"مهر", "آبان"));
        fieldOrders.add(new Fields("THR", "IFN", 27 , 29,"مهر", "آبان"));
        fieldOrders.add(new Fields("THR", "MHD", 27, 29,"مهر", "آبان"));
        fieldOrders.add(new Fields("THR", "MHD", 27 , 29,"مهر", "آبان"));
        fieldOrders.add(new Fields("THR", "MHD", 27 , 29,"مهر", "آبان"));
        fieldOrders.add(new Fields("THR", "MHD", 27 , 29,"مهر", "آبان"));
        fieldOrders.add(new Fields("THR", "MHD", 27 , 29,"مهر", "آبان"));
        fieldOrders.add(new Fields("THR", "MHD", 27 , 29,"مهر", "آبان"));
        fieldOrders.add(new Fields("THR", "MHD", 27 , 29,"مهر", "آبان"));


        fieldOrders.add(new Fields("THR", "MHD", 28 , 29,"مهر", "آبان"));
        fieldOrders.add(new Fields("THR", "TBZ", 28 , 29,"مهر", "آبان"));
        fieldOrders.add(new Fields("THR", "TBZ", 28 , 29,"مهر", "آبان"));
        fieldOrders.add(new Fields("THR", "TBZ", 28 , 29,"مهر", "آبان"));
        fieldOrders.add(new Fields("THR", "SYZ", 28 , 29,"مهر", "آبان"));
        fieldOrders.add(new Fields("THR", "IFN", 28 , 29,"مهر", "آبان"));
        fieldOrders.add(new Fields("THR", "MHD", 28, 29,"مهر", "آبان"));
        fieldOrders.add(new Fields("THR", "MHD", 28 , 29,"مهر", "آبان"));
        fieldOrders.add(new Fields("THR", "MHD", 28 , 29,"مهر", "آبان"));
        fieldOrders.add(new Fields("THR", "MHD", 28 , 29,"مهر", "آبان"));
        fieldOrders.add(new Fields("THR", "MHD", 28 , 29,"مهر", "آبان"));
        fieldOrders.add(new Fields("THR", "MHD", 28 , 29,"مهر", "آبان"));
        fieldOrders.add(new Fields("THR", "MHD", 28 , 29,"مهر", "آبان"));

        websiteOrders.add("tripir");
        websiteOrders.add("zoraq");
        websiteOrders.add("ghasedak24");
        websiteOrders.add("safarme");
        websiteOrders.add("flytoday");
        websiteOrders.add("mrbilit");
        websiteOrders.add("samtik");
        websiteOrders.add("flightio");
        websiteOrders.add("simorgh24");
        websiteOrders.add("irwego");
        websiteOrders.add("safarestan");
        websiteOrders.add("sepehr360");
        websiteOrders.add("alibaba");

        websiteOrders.add("tripir");
        websiteOrders.add("zoraq");
        websiteOrders.add("ghasedak24");
        websiteOrders.add("safarme");
        websiteOrders.add("flytoday");
        websiteOrders.add("mrbilit");
        websiteOrders.add("samtik");
        websiteOrders.add("flightio");
        websiteOrders.add("simorgh24");
        websiteOrders.add("irwego");
        websiteOrders.add("safarestan");
        websiteOrders.add("sepehr360");
        websiteOrders.add("alibaba");



        if (orderDA.getFailedServerName().equals("server1")){
            numberOfWorkers--;
            System.out.println("one of the workers left the job!");
        }
        if (orderDA.getFailedServerName().equals("server2")){
            numberOfWorkers--;
            System.out.println("one of the workers left the job!");
        }

        int chunkSize=websiteOrders.size()/numberOfWorkers;
        System.out.println("chunkSize="+chunkSize);

        for (int i=0;i<websiteOrders.size();i++){

            listOfOrders.add(new Order(fieldOrders.get(i),websiteOrders.get(i)));
            masterQueue.add(listOfOrders.get(i));
            if (i<chunkSize){
                serverQueue1.add(listOfOrders.get(i));
            }else{
                serverQueue2.add(listOfOrders.get(i));
            }
            System.out.println("adding to the Queue");
        }
        orderDA.sendServerNameAndQueue("masterServer",masterQueue);
        System.out.println("masterQueue.size()="+masterQueue.size());
        orderDA.sendServerNameAndQueue("server1",serverQueue1);
        orderDA.sendServerNameAndQueue("server2",serverQueue2);

        orderDA.sendServerNameMainStatus("server1MainStatus","mainnotdone");
        orderDA.sendServerNameMainStatus("server2MainStatus","mainnotdone");
        inProgress();
    }

    private static void inProgress() throws InterruptedException {
        while(true) {

            if ((!orderDA.getServerNameMainStatus("server1MainStatus").equals("mainnotdone"))&&
                    (!orderDA.getServerNameMainStatus("server2MainStatus").equals("mainnotdone"))) {
                //listen from servers if they pushed some queue to master server
                System.out.println("waiting in QueueManager to listen from servers");
                masterQueue = orderDA.getFromServerNameOneQueue("masterServer");

                while (masterQueue.size() > 1) {
                    System.out.println("taking from masterQueue");
                    if (!orderDA.getFailedServerName().equals("server1"))
                    {
                        serverQueue1.add(masterQueue.take());
                    }
                    if (!orderDA.getFailedServerName().equals("server2")) {
                        serverQueue2.add(masterQueue.take());
                    }
                }

                if (masterQueue.size()==1){
                    serverQueue1.add(masterQueue.take());
                }

                orderDA.appendServerNameOneQueue("masterServer", masterQueue);
                orderDA.appendServerNameOneQueue("server1", serverQueue1);
                orderDA.appendServerNameOneQueue("server2", serverQueue2);
            }
        }
    }
}
