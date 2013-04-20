/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Airport;

import java.util.ArrayList;

/**
 *
 * @author Owner
 */
public class AirportSim {
    //class vars

    static final int MIN_PER_HOUR = 60;
    static final int AVG_ARRIVALS_PER_HOUR = 8;
    static final int LENGTH_OF_SIMULATION_IN_HRS = 8;
    static final int LANDING_WAIT_TIME = 2;
    static final int TAKEOFF_WAIT_TIME = 3;
    static boolean runwayIsFree = true;
    static boolean landing = false;
    static boolean takingOff = false;
    private ArrayList<Airplane> landingQueue = new ArrayList();
    private ArrayList<Airplane> takeOffQueue = new ArrayList();
    private int planesLanded = 0;
    private int planesDeparted = 0;
    private int planeNumber = 1;
    private int waitTime = 0;
    private int maxWaitLanding = 0;
    private int maxWaitDeparting = 0;
    private int totalWaitTimeDeparting = 0;
    private int totalWaitTimeLanding = 0;
    private int minute;
    private String timeStamp;
    private float avgWaitTimeDepart;
    private float avgWaitTimeLanding;
    private int avgWaitMinutesDepart;
    private float avgWaitSecondsDepart;
    private int avgWaitMinutesLanding;
    private float avgWaitSecondsLanding;
    private int waitingToLand;
    private int waitingToDepart;
    private int maxWaitingToLand;
    private int maxWaitingToDepart;
    private int runwayFreeAt = 0;
    private String eventStatus = "-";
    private String eventPlane;
    private String runwayStatus = "-";

    public void run() {
        
        
        //print Header
        System.out.println("Minute\tPlane\tEvent\t\tRunway\t# Waiting\t# Waiting\tTotal Number\tTotal Number\tRunway Free");
        System.out.println("\tNumber\tStatus\t\tStatus\t  To Land\t To Depart\t   Landed\t  Departed\t  At Time");
        
        
        for (minute = 1; minute <= LENGTH_OF_SIMULATION_IN_HRS * MIN_PER_HOUR; minute++) {
            // set the timestamp
            timeStamp = minuteToTime(minute);
           
            // init printed vars
            
            eventStatus = "-";
            eventPlane = "-";
            
            checkQueues();
            
            runwayIsFree = (minute >= runwayFreeAt);

            if (!runwayIsFree) {
                clearRunway();
            }else{
                runwayStatus = "-";
            }
            
            


            // is there a plane about to land?
            if (readyToLand()) {
                landingQueue.add(new Airplane(planeNumber, minute));
                //System.out.println(timeStamp + "  -  Plane " + planeNumber + " is ready to land.");
                eventPlane = "" + planeNumber;
                eventStatus = eventPlane + " arrives";
                runwayStatus = eventPlane + "L";
                planeNumber++;
            }else if (readyToTakeOff()) {
                takeOffQueue.add(new Airplane(planeNumber, minute));
                eventPlane = "" + planeNumber;
                eventStatus = eventPlane + " departs";
                runwayStatus = eventPlane + "D";
                //System.out.println(timeStamp + "  -  Plane " + planeNumber + " is ready to take off.");
                planeNumber++;
            }
            
            // if the runway is free and there is a plane waiting to land, it gets to land, 
            
            if (runwayIsFree && landingQueue.size() > 0){
                doLanding();
            }else if (runwayIsFree && takeOffQueue.size() > 0){
                doTakeOff();
            }
            
            
            printLine();
        }
        
        
        calculateAverages();
        showResults();
        
        
        

    }

    public static boolean readyToLand() {

        return ((Math.random() * MIN_PER_HOUR) < AVG_ARRIVALS_PER_HOUR);
    }

    public static boolean readyToTakeOff() {
        return ((Math.random() * MIN_PER_HOUR) < AVG_ARRIVALS_PER_HOUR);
    }
    
    public void clearRunway(){
        // is a plane still landing?
                if (landing) {
                    if (minute == runwayFreeAt) {
                        //System.out.println(timeStamp + "  -  Plane " + landingQueue.get(0).getId() + " has landed. Runway is free again");
                        landing = false;
                        runwayStatus = "-";
                        planesLanded++;
                        landingQueue.remove(0);
                    }
                }

                //is a plane still taking off?
                if (takingOff) {
                    if (minute == runwayFreeAt) {
                        //System.out.println(timeStamp + "  -  Plane " + takeOffQueue.get(0).getId() + " has taken off. Runway is free again");
                        takingOff = false; 
                        runwayStatus = "-";
                        planesDeparted++;
                        takeOffQueue.remove(0);
                    }
                }
    }
    
    public void doLanding(){
        Airplane landingPlane = landingQueue.get(0);
                //System.out.println(timeStamp + "  -  Plane " + landingPlane.getId() + " is landing.");
                waitTime = (minute - landingPlane.getTimeReady());
                if(waitTime > maxWaitLanding){
                    maxWaitLanding = waitTime;
                }
                totalWaitTimeLanding += waitTime;
                runwayFreeAt += LANDING_WAIT_TIME;
                landing = true;
    }
    
    public void doTakeOff(){
        Airplane departingPlane = takeOffQueue.get(0);
                //System.out.println(timeStamp + "  -  Plane " +departingPlane.getId() + " is taking off.");
                waitTime = (minute - departingPlane.getTimeReady());
                if(waitTime > maxWaitDeparting){
                    maxWaitDeparting = waitTime;
                }
                totalWaitTimeDeparting += waitTime;
                runwayFreeAt += TAKEOFF_WAIT_TIME;
                takingOff = true;
                
    }
    public void calculateAverages(){
        avgWaitTimeDepart = ((float) totalWaitTimeDeparting / planesDeparted) * 60;
        avgWaitTimeLanding = ((float) totalWaitTimeLanding / planesLanded) * 60;
        avgWaitMinutesDepart = (int) avgWaitTimeDepart / 60;
        avgWaitSecondsDepart = avgWaitTimeDepart % 60;
        avgWaitMinutesLanding = (int) avgWaitTimeLanding / 60;
        avgWaitSecondsLanding = avgWaitTimeLanding % 60;
    }
    
    public void checkQueues(){
        waitingToDepart = takeOffQueue.size();
        waitingToLand   = landingQueue.size();
        
        if(waitingToDepart > maxWaitingToDepart){
            maxWaitingToDepart = waitingToDepart;
        }
        
        if(waitingToLand > maxWaitingToLand){
            maxWaitingToLand = waitingToLand;
        }
    }
    
    public void printLine(){
        String runwayFreeAtTime = minuteToTime(runwayFreeAt);
        if (eventStatus.equals("-")){
            System.out.println(timeStamp + "\t  " + eventPlane + "\t  " + eventStatus +"\t\t  " + runwayStatus + "\t  "+ waitingToLand + "\t  " + waitingToDepart +
                "\t  " + planesLanded + "\t  "+ planesDeparted + "\t  " + runwayFreeAtTime);
        }else{
            System.out.println(timeStamp + "\t  " + eventPlane + "\t  " + eventStatus +"\t  " + runwayStatus + "\t  "+ waitingToLand + "\t  " + waitingToDepart +
                "\t  " + planesLanded + "\t  "+ planesDeparted + "\t  " + runwayFreeAtTime);
        }
        
    }
    public void showResults(){
        System.out.println(planesLanded + " planes have landed. \n" 
                + planesDeparted + " planes have left.\n" 
                + "Max wait time for landing: "+ maxWaitLanding+ " minutes.\n"
                + "Max wait time for departure: " + maxWaitDeparting+ " minutes.\n"
                + "Average wait time for landing : "+ avgWaitMinutesLanding + " minutes " + avgWaitSecondsLanding + " seconds.\n"
                + "Average wait time for departure: " + avgWaitMinutesDepart + " minutes " + avgWaitSecondsDepart +  " seconds.\n"
                + "Maximum number of flights waiting to land: " + maxWaitingToLand + ".\n"
                + "Maximum number of flights waiting to depart: " + maxWaitingToDepart +".");
    }
    
    public String minuteToTime(int inTime){
        String time;
        if (minute % MIN_PER_HOUR < 10) {
                time = inTime / MIN_PER_HOUR + ":0" + minute % MIN_PER_HOUR; 
            } else {
                time = inTime / MIN_PER_HOUR + ":" + minute % MIN_PER_HOUR;
            }
        return time;
    }
}
