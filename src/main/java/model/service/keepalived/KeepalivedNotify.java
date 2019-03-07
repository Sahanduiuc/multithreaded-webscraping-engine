package model.service.keepalived;

import model.da.redis.OrderDA;

public class KeepalivedNotify {

    public static void main(String[] args) {
        OrderDA orderDA=new OrderDA();
        orderDA.setFailedServerName(args[0]);
    }
}
