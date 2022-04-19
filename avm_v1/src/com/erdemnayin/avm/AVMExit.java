package com.erdemnayin.avm;

import java.util.ArrayList;
import java.util.Random;

public class AVMExit implements Runnable {

    ArrayList<Floor> floorArrayList;
    private Random rand;
    private int floorNo,bound,floorCount,random,queueCount;
    private String color;

    public AVMExit(ArrayList<Floor> floorArrayList, String color) {
        this.floorArrayList = floorArrayList;
        rand = new Random();
        bound = 5;
        this.color = color;
    }


    @Override
    public void run() {

        while(true){

            floorNo = rand.nextInt(4) + 1;

            synchronized (floorArrayList.get(floorNo)){
                floorCount = floorArrayList.get(floorNo).getPeopleCount();
                queueCount = floorArrayList.get(floorNo).getQueueCount();
            }



            while(floorCount == 0){

                floorNo = rand.nextInt(4) + 1 ;
                synchronized (floorArrayList.get(floorNo)){
                    floorCount = floorArrayList.get(floorNo).getPeopleCount();
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            if(floorCount - queueCount > 0 && floorCount - queueCount < bound ){

                random = rand.nextInt(floorCount - queueCount) + 1;

                synchronized (floorArrayList.get(floorNo)){
                    floorArrayList.get(floorNo).getQueue().add(new Group(0, random));
                    floorArrayList.get(floorNo).setQueueCount(floorArrayList.get(floorNo).getQueueCount() + random);
                    System.out.println(toString());
                    System.out.println(floorArrayList.get(floorNo).toString());
                }


            }else if(floorCount - queueCount > 0 && floorCount - queueCount >= bound){

                random = rand.nextInt(bound) + 1;

                synchronized (floorArrayList.get(floorNo)){
                    floorArrayList.get(floorNo).getQueue().add(new Group(0, random));
                    floorArrayList.get(floorNo).setQueueCount(floorArrayList.get(floorNo).getQueueCount() + random);
                    System.out.println(toString());
                    System.out.println(floorArrayList.get(floorNo).toString());
                }


            }


            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }

    }

    @Override
    public String toString() {
        return color + "AVMExit => [" + random + ", 0]" ;
    }
}
