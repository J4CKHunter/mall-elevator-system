package com.erdemnayin.avm;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {


        Floor floor0 = new Floor(0);
        Floor floor1 = new Floor(1);
        Floor floor2 = new Floor(2);
        Floor floor3 = new Floor(3);
        Floor floor4 = new Floor(4);

        ArrayList<Floor> floorArrayList = new ArrayList<>();

        floorArrayList.add(floor0);
        floorArrayList.add(floor1);
        floorArrayList.add(floor2);
        floorArrayList.add(floor3);
        floorArrayList.add(floor4);



        Thread avmEntrance = new Thread(new AVMEntrance(floor0, Color.ANSI_CYAN));
        Thread avmExit = new Thread(new AVMExit(floorArrayList,  Color.ANSI_RESET));
        Thread elevatorController = new Thread(new ElevatorController(floorArrayList));



        avmEntrance.start();
        elevatorController.start();
        avmExit.start();



    }
}
