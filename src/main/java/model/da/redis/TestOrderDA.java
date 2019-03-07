package model.da.redis;

import com.google.gson.Gson;
import model.common.Fields;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.concurrent.ExecutionException;

public class TestOrderDA {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        OrderDA orderDA=new OrderDA();
        Jedis jSubscriber1=orderDA.subscribe();
        Jedis jSubscriber2=orderDA.subscribe();
        orderDA.getNewFixedThreadPool().submit(new Runnable() {
            @Override
            public void run() {
                try {
                    jSubscriber1.subscribe(new JedisPubSub() {
                        @Override
                        public void onMessage(String channel, String message) {
                            super.onMessage(channel, message);
                            System.out.println("availableThreadsForServer1="+message);
                        }
                    },"availableThreadsForServer1");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        orderDA.getNewFixedThreadPool().submit(new Runnable() {
            @Override
            public void run() {
                try {
                    jSubscriber2.subscribe(new JedisPubSub() {
                        @Override
                        public void onMessage(String channel, String message) {
                            super.onMessage(channel, message);
                            System.out.println("availableThreadsForServer2="+message);
                        }
                    },"availableThreadsForServer2");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        orderDA.publish("availableThreadsForServer1","1");
        orderDA.publish("availableThreadsForServer2","4");
        orderDA.publish("availableThreadsForServer2","7");

        //**************Test Object serialization in redis ********************

        Fields fields=new Fields("THR", "MHD", 23 , 26,"مرداد", "شهريور");

        Jedis jedis = new Jedis("192.168.150.202",6379);
        jedis.connect();
        jedis.auth("irsa");

        Gson gson = new Gson();
        String json = gson.toJson(fields);
        jedis.set("server1",json);

        String json2 = jedis.get("server1");
        Fields object=gson.fromJson(json2, Fields.class);
        System.out.println("object.getStartDate()="+object.getStartDate());

    }
}
