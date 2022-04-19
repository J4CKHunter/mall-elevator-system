package com.erdemnayin.avm;

import java.util.Random;

public class AVMEntrance implements Runnable{

    private Floor floor0;
    private Random rand;
    private int count,floorNo;
    private String color;


    public AVMEntrance(Floor floor0, String color) {
        this.floor0 = floor0;
        rand = new Random();
        this.color = color;
    }

    @Override
    public void run() {

        while(true){

            count = rand.nextInt(10) + 1 ;
            floorNo = rand.nextInt(4) + 1 ;

            synchronized (floor0){
                floor0.getQueue().add(new Group(floorNo,count));
                System.out.println(toString());
            }


            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public String toString() {
        return color + "AVMEntrance => [" + count + ", " + floorNo + "]\n";
    }
}
