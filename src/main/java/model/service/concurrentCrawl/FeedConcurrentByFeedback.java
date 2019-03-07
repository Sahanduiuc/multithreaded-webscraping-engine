package model.service.concurrentCrawl;

import model.common.Fields;
import model.da.redis.OrderDA;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author farshadnsh
 * The type Feed concurrent by feedback.
 */
public class FeedConcurrentByFeedback {

    private static Executor executor;
    private static int numberOfAvailableThreads;
    private static int currentActiveThreads;
    private static int threadPoolSize;
    private static int failedThreadshold;

    private static LinkedBlockingQueue<Order> queue;
    private static LinkedBlockingQueue<Order> fetchedQueue;

    private static List<CompletableFuture<Order>> listOfCompletableFutures;
    private static Order finalOrder;
    private static CompletableFuture<String> getFuture;

    private static String serverName;
    private static int queueCapacity;
    private static int remained;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws ExecutionException   the execution exception
     * @throws InterruptedException the interrupted exception
     */
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        serverName="server2";
        threadPoolSize=10;
        failedThreadshold=2;
        queueCapacity=threadPoolSize+5;

        executor = Executors.newFixedThreadPool(threadPoolSize);

        listOfCompletableFutures = new ArrayList<>();
        queue = new LinkedBlockingQueue<Order>();
        fetchedQueue=new LinkedBlockingQueue<Order>();
        numberOfAvailableThreads=threadPoolSize;

        OrderDA orderDA=new OrderDA();
        queue=orderDA.getFromServerNameOneQueue(serverName);
        while(queue.size()>0) {
            System.out.println("--queue.size()="+queue.size());
            System.out.println("--numberOfAvailableThreads="+numberOfAvailableThreads);
            currentActiveThreads=((ThreadPoolExecutor)executor).getActiveCount();

            //pull from master server if you think your queue is small
            if (queue.size()<currentActiveThreads){
                //we need to fetch orders from the master Server
                fetchedQueue.clear();
                fetchedQueue=orderDA.getFromServerNameOneQueue(serverName);
                while(fetchedQueue.size()>0){
                    queue.add(fetchedQueue.take());
                    System.out.println("---queue.size()="+queue.size());
                }
            }

            //take from queue as long as you have available threads
            listOfCompletableFutures.clear();
            System.out.println("listOfCompletableFutures.size()="+listOfCompletableFutures.size());
            for (int i=0;i<numberOfAvailableThreads;i++) {
                if (queue.size()>0) {
                    listOfCompletableFutures.add(
                            CompletableFuture.supplyAsync(() -> {
                                        try {
                                            finalOrder = new CallableWithFieldsAndWebsites(queue.take()).main();
                                            System.out.println("----queue.size()="+queue.size());

                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        return finalOrder;
                                    }
                        ,executor)
                    );
                }
                Thread.sleep(1000);
            }

            //push to master server to distribute among servers
             remained=queueCapacity-numberOfAvailableThreads;
            if (queue.size()>remained)
            {
                orderDA.appendServerNameOneQueue("masterServer",queue);
                queue.clear();
            }

            //loop for getting the result of multiThreading crawls
            for (int j = 0; j < listOfCompletableFutures.size(); j++) {
                getFuture= listOfCompletableFutures.get(j).thenApply(order -> {
                    currentActiveThreads=((ThreadPoolExecutor)executor).getActiveCount();
                    numberOfAvailableThreads = threadPoolSize - currentActiveThreads;
                    System.out.println("numberOfAvailableThreads for analysis is:" + numberOfAvailableThreads);
                    if (order.getFields().getStatus().equals("failed")) {
                        order.setNumberOfFailedTriedStatus(order.getNumberOfFailedTriedStatus()+1);
                        if (order.getNumberOfFailedTriedStatus()<failedThreadshold){
                            System.out.println("order.getNumberOfFailedTriedStatus()="+order.getNumberOfFailedTriedStatus());
                            System.out.println("adding to queue provider:" + order.getWebsite());
                            queue.add(order);
                        }
                    } else if (order.getFields().getStatus().equals("done")) {
                        System.out.println("wow for "+order.getWebsite());
                    }
                    return "currently numberOfAvailableThreads is:" + numberOfAvailableThreads;
                });
                System.out.println("getFuture.get()="+getFuture.get());
            }
            orderDA.sendServerNameMainStatus(serverName+"MainStatus","done");
        }
        System.out.println("--finally queue.size()="+queue.size());
        System.out.println("-----------finished and there is no more order in queue to do----------------------");
    }
}
