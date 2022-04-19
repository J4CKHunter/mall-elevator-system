package com.erdemnayin.avm;

import java.util.LinkedList;
import java.util.Queue;

public class Floor {

    private int floorLabel;
    private int peopleCount;
    private Queue<Group> queue;
    private int queueCount;


    public Floor(int floorLabel) {
        this.floorLabel = floorLabel;
        queue = new LinkedList<>();
        this.queueCount = 0;
    }

    public synchronized int getFloorLabel() {
        return floorLabel;
    }

    public synchronized void setFloorLabel(int floorLabel) {
        this.floorLabel = floorLabel;
    }

    public synchronized int getPeopleCount() {
        return peopleCount;
    }

    public synchronized void setPeopleCount(int peopleCount) {
        this.peopleCount = peopleCount;
    }

    public synchronized Queue<Group> getQueue() {
        return queue;
    }

    public synchronized void setQueue(Queue<Group> queue) {
        this.queue = queue;
    }

    public synchronized int getQueueCount() {
        return queueCount;
    }

    public synchronized void setQueueCount(int queueCount) {
        this.queueCount = queueCount;
    }

    @Override
    public synchronized String toString() {

        if(getFloorLabel() == 0)
            return getFloorLabel() + ". floor => Queue : " + getQueue() + "\n" ;

        else
            return getFloorLabel() + ". floor => all : " + peopleCount + " Queue Count : " + queueCount +" Queue : " + getQueue() + "\n" ;

    }
}
