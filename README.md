# Opensource Multithreaded Webscraping Engine
I used completablefutures and multithreading to create a webscraping engine.

## databases
* Aerospike: to put flight ticket prices inside it
* redis: to communicate between servers to say which thread of which server is not busy
and is ready to take tasks.


## QueueManager
I created this class to distribute tasks to other servers:
```java
masterQueue = new LinkedBlockingQueue<Order>();
        serverQueue1=new LinkedBlockingQueue<Order>();
        serverQueue2=new LinkedBlockingQueue<Order>();

        fieldOrders.add(new Fields("THR", "MHD", 27 , 29,"مهر", "آبان"));
        websiteOrders.add("alibaba");
```
## FeedConcurrentByFeedback
This class recives feedback from servers 
```java
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
```
Now you can crawl any site such as booking.com and aribnb concurrently.
I appreciate if anyone is interested to send a pull request to work on this nice
opensource platform for concurrent webscraping. My interests are:
* using appropriate design pattern for better encapsulation and polymorphism
* using beanFactory in Spring boot to implement abstract factory pattern of spring
* healthcheking using keepalived or anything like that
* clean architecture is necessary
* please use @Configuration to manage your beans! 
