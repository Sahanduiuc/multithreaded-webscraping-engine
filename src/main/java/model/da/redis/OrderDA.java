package model.da.redis;

import com.google.gson.Gson;
import model.service.concurrentCrawl.Order;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.lang.reflect.Type;
import com.google.common.reflect.TypeToken;

import java.util.concurrent.*;

public class OrderDA {


    private ExecutorService newFixedThreadPool ;
    private  Jedis jedis;
    private LinkedBlockingQueue<Order> queue;

    public OrderDA() {
        newFixedThreadPool = Executors.newFixedThreadPool(4);
        jedis = new Jedis("192.168.150.202",6379);
        jedis.connect();
        jedis.auth("irsa");

    }

    public ExecutorService getNewFixedThreadPool() {
        return newFixedThreadPool;
    }

    public void setNewFixedThreadPool(ExecutorService newFixedThreadPool) {
        this.newFixedThreadPool = newFixedThreadPool;
    }

    public Jedis subscribe() throws ExecutionException, InterruptedException {

        Jedis jSubscriber = new Jedis("192.168.150.202",6379);
        jSubscriber.connect();
        jSubscriber.auth("irsa");
        return jSubscriber;
    }


    public void publish(String channelName,String message){
        Jedis jPublisher = new Jedis("192.168.150.202",6379);
        jPublisher.connect();
        jPublisher.auth("irsa");
        newFixedThreadPool.submit(new Runnable() {
            @Override
            public void run() {
                jPublisher.publish(channelName, message);
                System.out.println("publishing-------");
            }
        });
    }

    public void sendServerNameAndQueue(String serverName,LinkedBlockingQueue<Order> queue){

        Gson gson = new Gson();
        String json = gson.toJson(queue);
        jedis.set(serverName,json);
    }

    public void appendServerNameOneQueue(String serverName,LinkedBlockingQueue<Order> moreQueue) throws InterruptedException {
        //getting the oldqueue
        Gson gson = new Gson();
        String json2 = jedis.get(serverName);
        Type queueType = new TypeToken<LinkedBlockingQueue<Order>>(){}.getType();
        queue=gson.fromJson(json2, queueType);
        //appending to the oldqueue to get newqueue
        moreQueue=new LinkedBlockingQueue<Order>();
        while(moreQueue.size()>0){
            queue.add(moreQueue.take());
        }
        String json = gson.toJson(queue);
        jedis.set(serverName,json);
    }


    public LinkedBlockingQueue<Order> getFromServerNameOneQueue(String serverName){
        Gson gson = new Gson();
        String json2 = jedis.get(serverName);
        Type queueType = new TypeToken<LinkedBlockingQueue<Order>>(){}.getType();
        queue=gson.fromJson(json2, queueType);
        return queue;
    }

    public Long delete(String serverName){
        return jedis.del(serverName);
    }


    public void sendServerNameMainStatus(String serverxMainStatus, String status) {
       jedis.set(serverxMainStatus,status);
    }

    public String getServerNameMainStatus(String serverxMainStatus) {
        return(jedis.get(serverxMainStatus));
    }

    public String getFailedServerName(){
       String s= jedis.get("failedServerName");
        return s;
    }

    public void setFailedServerName(String name){

        jedis.set("failedServerName",name);
    }





}
