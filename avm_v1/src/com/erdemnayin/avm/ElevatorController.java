package com.erdemnayin.avm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ElevatorController implements Runnable {

    private Elevator elevator0;
    private Elevator elevator1;
    private Elevator elevator2;
    private Elevator elevator3;
    private Elevator elevator4;

    ArrayList<Floor> floorArrayList;

    private int workingElevatorCount;
    private int exitCount;

    public ElevatorController(ArrayList<Floor> floorArrayList) {

        this.floorArrayList = floorArrayList;

        elevator0 = new Elevator(   this.floorArrayList, this, Color.ANSI_RED);
        elevator1 = new Elevator(   this.floorArrayList, this, Color.ANSI_GREEN);
        elevator2 = new Elevator(   this.floorArrayList, this, Color.ANSI_YELLOW);
        elevator3 = new Elevator(   this.floorArrayList, this, Color.ANSI_BLUE);
        elevator4 = new Elevator(   this.floorArrayList, this, Color.ANSI_PURPLE);

        this.workingElevatorCount = 0;

        exitCount = 0;

    }

    @Override
    public void run() {

        elevator0.start();
        elevator0.play();

        elevator1.start();
        elevator2.start();
        elevator3.start();
        elevator4.start();


        while(true){

            if(checkFloorsQueueCount() > 20){

                switch (workingElevatorCount){

                    case 0:
                        workingElevatorCount++;
                        elevator1.play();
                        break;

                    case 1:
                        workingElevatorCount++;
                        elevator2.play();
                        break;

                    case 2:
                        workingElevatorCount++;
                        elevator3.play();
                        break;
                    case 3:
                        workingElevatorCount++;
                        elevator4.play();
                        break;

                    default: break;
                }

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            else if(checkFloorsQueueCount() < 10)  {
                switch (workingElevatorCount){

                    case 4:
                        workingElevatorCount--;
                        elevator4.pause();
                        break;

                    case 3:
                        workingElevatorCount--;
                        elevator3.pause();
                        break;

                    case 2:
                        workingElevatorCount--;
                        elevator2.pause();
                        break;

                    case 1:
                        workingElevatorCount--;
                        elevator1.pause();
                        break;

                    default: break;
                }
            }

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }




    }


    public int checkFloorsQueueCount(){

        int count = 0;
        Iterator<Group> iterator;

        for (int i = 0; i < 5; i++) {

            synchronized (getFloorArrayList().get(i)) {

            Floor temp = getFloorArrayList().get(i);

                iterator = temp.getQueue().iterator();

                while (iterator.hasNext()) {
                    count += iterator.next().getCount();
                }
            }
        }

        return count;
    }

    public ArrayList<Floor> getFloorArrayList() {
        return floorArrayList;
    }

    public void setFloorArrayList(ArrayList<Floor> floorArrayList) {
        this.floorArrayList = floorArrayList;
    }

    public int getExitCount() {
        return exitCount;
    }

    public void setExitCount(int exitCount) {
        this.exitCount = exitCount;
    }

    @Override
    public String toString() {

        StringBuilder sB = new StringBuilder();

        synchronized (floorArrayList){
            for (Floor temp : floorArrayList) {
                sB.append(temp.toString());
            }
        }

        return sB.toString() + "Exit Count : " + getExitCount() + "\n";

    }


}
