package model.service.concurrentCrawl;

import model.common.Fields;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Order.
 * @author farshad noravesh
 */
public class Order {
    private int numberOfFailedTriedStatus;
    private Fields fields;
    private String website;
    private int currentActiveThreads;

    /**
     * Gets fields.
     *
     * @return the fields
     */
    public Fields getFields() {
        return fields;
    }

    /**
     * Sets fields.
     *
     * @param fields the fields
     */
    public void setFields(Fields fields) {
        this.fields = fields;
    }

    /**
     * Gets website.
     *
     * @return the website
     */
    public String getWebsite() {
        return website;
    }

    /**
     * Sets website.
     *
     * @param website the website
     */
    public void setWebsite(String website) {
        this.website = website;
    }


    /**
     * Instantiates a new Order.
     *
     * @param fields  the fields
     * @param website the website
     */
    public Order(Fields fields, String website) {
        this.fields = fields;
        this.website = website;
        numberOfFailedTriedStatus=0;
    }

    /**
     * Gets number of failed tried status.
     *
     * @return the number of failed tried status
     */
    public int getNumberOfFailedTriedStatus() {
        return numberOfFailedTriedStatus;
    }

    /**
     * Sets number of failed tried status.
     *
     * @param numberOfFailedTriedStatus the number of failed tried status
     */
    public void setNumberOfFailedTriedStatus(int numberOfFailedTriedStatus) {
        this.numberOfFailedTriedStatus = numberOfFailedTriedStatus;
    }

    /**
     * Gets current active threads.
     *
     * @return the current active threads
     */
    public int getCurrentActiveThreads() {
        return currentActiveThreads;
    }

    /**
     * Sets current active threads.
     *
     * @param currentActiveThreads the current active threads
     */
    public void setCurrentActiveThreads(int currentActiveThreads) {
        this.currentActiveThreads = currentActiveThreads;
    }
}
