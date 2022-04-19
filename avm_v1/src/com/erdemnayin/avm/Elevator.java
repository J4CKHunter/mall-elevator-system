package com.erdemnayin.avm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class Elevator extends Thread {


    private String mode;
    private final int CAPACITY = 10;
    private int floorNo;
    private int destination;
    private int countInside;
    private String direction;
    private Queue<Group> insideQueue;
    ArrayList<Floor> floorArrayList;
    ElevatorController elevatorController;
    private String color;
    private boolean isWaiting = true;


    public Elevator(ArrayList<Floor> floorArrayList, ElevatorController elevatorController, String color) {

        floorNo = 0;
        destination = 0;
        countInside = 0;
        direction = "up";
        insideQueue = new LinkedList<>();
        this.floorArrayList = floorArrayList;
        mode = "idle";
        this.elevatorController = elevatorController;
        this.color = color;

    }



    @Override
    public void run() {

        try {

            while (true) {

                doAction();

            }

        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void welcome(){

        int count = 0;
        int x = 0;


            synchronized (floorArrayList.get(0)) {

                Iterator<Group> iterator = floorArrayList.get(0).getQueue().iterator();

                while (iterator.hasNext()) {

                    Group temp = iterator.next();
                    x = temp.getCount();

                    if (count + x <= 10) {
                        count += x;
                        getInsideQueue().add(temp);
                        iterator.remove();
                        continue;
                    }

                    break;

                }

            }

            setCountInside(count);

            if(count !=  0){
                setMode("working");
                System.out.println(toString());
                distribute();
            }

            else{
                System.out.println(toString());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }




    }

    public void distribute(){


            Iterator<Group> iterator = insideQueue.iterator();
            int targetFloor, groupSize;


                while (iterator.hasNext()) {

                    Group temp = iterator.next();
                    targetFloor = temp.getFloorNo();
                    groupSize = temp.getCount();

                    setDestination(targetFloor);
                    checkFloorNumsAndSetDirection();

                    elevatorMove();

                    synchronized (floorArrayList.get(getFloorNo())) {
                        int targetFloorCount = floorArrayList.get(getFloorNo()).getPeopleCount();
                        floorArrayList.get(getFloorNo()).setPeopleCount(targetFloorCount + groupSize);
                    }
                    setCountInside(getCountInside() - groupSize);

                    iterator.remove();

                }


        System.out.println(toString());

    }

    public void evacuate(){
        
        ArrayList<Integer> floorsHaveQueue = new ArrayList<>();


            for (int i = 1; i <= 4; i++) {
                synchronized (floorArrayList.get(i)){
                    if (!floorArrayList.get(i).getQueue().isEmpty())
                        floorsHaveQueue.add(floorArrayList.get(i).getFloorLabel());
                }

            }


        for (int floorNum:floorsHaveQueue) {

            if(getCountInside() == CAPACITY)
                break;

            setDestination(floorNum);
            checkFloorNumsAndSetDirection();

            elevatorMove();

            Iterator<Group> iterator;
            synchronized (floorArrayList.get(floorNum)){
                iterator = floorArrayList.get(floorNum).getQueue().iterator();
            }

            int queueGroupSize, count;

            while (iterator.hasNext()){

                Group temp = iterator.next();
                queueGroupSize = temp.getCount();
                count = getCountInside();

                if(count + queueGroupSize <= 10){
                    count += queueGroupSize;
                    getInsideQueue().add(temp);
                    setCountInside(count);
                    synchronized (floorArrayList.get(floorNum)){
                        floorArrayList.get(floorNum).setPeopleCount(floorArrayList.get(floorNum).getPeopleCount() - queueGroupSize);
                        floorArrayList.get(floorNum).setQueueCount(floorArrayList.get(floorNum).getQueueCount() - queueGroupSize);
                    }

                    iterator.remove();
                    continue;
                }

                break;

            }

        }

        System.out.println(toString());
        goodbye();


        // giriş katı hariç diğer katlarda aşağıya inmeyi bekleyen queue var mı kontrol et.
        // sırayla aşağı inmeyi bekleyen queue ya sahip olan katlara git
        // gittiğin katta queueda bekleyenleri sırayla sayısını kontrol ederek ve kapasiteyi aşmayacak şekilde al
        // aştırmıyorsa içeri al, kapasiteyi aştırıyorsa diğer katlara bak
        // kapasite dolana tüm katların kontrolü bitince aşağı in

    }

    public void goodbye(){

        setDestination(0);
        checkFloorNumsAndSetDirection();

        elevatorMove();

        Iterator<Group> iterator = insideQueue.iterator();
        int groupSize;

        while(iterator.hasNext()){

            Group temp = iterator.next();
            groupSize = temp.getCount();

            setCountInside(getCountInside() - groupSize);
            getElevatorController().setExitCount(getElevatorController().getExitCount() + groupSize);

            iterator.remove();

        }

        System.out.println(toString());


    }

    public void checkFloorNumsAndSetDirection(){

        if(getFloorNo() - getDestination() < 0){
            setDirection("up");
            setMode("working");
        }

        else if(getFloorNo() - getDestination() > 0 ){
            setDirection("down");
            setMode("working");
        }

        else{
            setDirection("none");
            setMode("idle");
        }

    }

    public void elevatorMove(){

        int currentFloor = getFloorNo();
        int targetFloor = getDestination();

        if(targetFloor > currentFloor){

            for (int i = currentFloor; i < targetFloor; i++) {

                System.out.println(toString());

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                setFloorNo(i+1);
                checkFloorNumsAndSetDirection();
            }

            System.out.println(toString());
        }

        else if(currentFloor > targetFloor){

            for (int i = currentFloor ; i > targetFloor; i--) {

                System.out.println(toString());

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                setFloorNo(i-1);
                checkFloorNumsAndSetDirection();
            }

            System.out.println(toString());
        }

    }

    public  void pause(){
        isWaiting = true;
    }

    public  void  play(){
        isWaiting = false;
        synchronized (this){
            notify();
        }
    }

    private synchronized void doAction() throws InterruptedException {

        synchronized (this){

            while (isWaiting) {
                wait();
            }

            if (getFloorNo() == 0 && getMode() == "idle") {
                welcome();
            } else if (getFloorNo() != 0) {
                evacuate();
            }

        }

    }

    public int getCAPACITY() {
        return CAPACITY;
    }

    public int getFloorNo() {
        return floorNo;
    }

    public void setFloorNo(int floorNo) {
        this.floorNo = floorNo;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public int getCountInside() {
        return countInside;
    }

    public void setCountInside(int countInside) {
        this.countInside = countInside;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Queue<Group> getInsideQueue() {
        return insideQueue;
    }

    public void setInsideQueue(Queue<Group> insideQueue) {
        this.insideQueue = insideQueue;
    }

    public ArrayList<Floor> getFloorArrayList() {
        return floorArrayList;
    }

    public void setFloorArrayList(ArrayList<Floor> floorArrayList) {
        this.floorArrayList = floorArrayList;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public ElevatorController getElevatorController() {
        return elevatorController;
    }

    public void setElevatorController(ElevatorController elevatorController) {
        this.elevatorController = elevatorController;
    }



    @Override
    public String toString() {
        return  color
                + getElevatorController().toString() + "\n"
                +"mode : " + getMode() + "\n"
                + "floor : " + getFloorNo() + "\n"
                + "destination : " + getDestination() + "\n"
                + "direction : " + getDirection() + "\n"
                + "capacity : " + getCAPACITY()  + "\n"
                + "count inside : " + getCountInside()  + "\n"
                + "inside queue : " + getInsideQueue()  + "\n";
    }
}
