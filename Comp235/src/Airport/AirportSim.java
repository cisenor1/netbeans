/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Airport;

import java.util.ArrayList;

/**
 *  Airport flight control simulation.
 * @author Craig Isenor
 */
public class AirportSim {
    //class vars

    static final int MIN_PER_HOUR = 60;
    static final int AVG_ARRIVALS_PER_HOUR = 8;
    static final int AVG_DEPARTURES_PER_HOUR = 8;
    static final int LENGTH_OF_SIMULATION_IN_HRS = 8;
    static final int LANDING_WAIT_TIME = 2;
    static final int TAKEOFF_WAIT_TIME = 3;
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
    private String runwayFreeAtTime;

    public void run() {


        //print Header
        System.out.println("Minute\tPlane\tEvent\t\tRunway\t# Waiting\t# Waiting\tTotal Number\t  Total Number\t  Runway Free");
        System.out.println("\tNumber\tStatus\t\tStatus\t  To Land\t To Depart\t   Landed\t    Departed\t    At Time");


        for (minute = 1; minute <= LENGTH_OF_SIMULATION_IN_HRS * MIN_PER_HOUR; minute++) {

            // set the timestamp
            timeStamp = minuteToTime(minute);

            // init printed vars
            eventStatus = "-";
            eventPlane = "-";

            updateMaxQueues();
            checkForNewPlanes();
            if (isRunwayFree()){
                isAnyoneUsingTheRunway();
                // if the runway is free and there is a plane waiting to land, it gets to land, 
                if (landingQueue.size() > 0) {
                    doLanding();
                }
                else if (takeOffQueue.size() > 0) {
                    doTakeOff();
                }
            }
            
            
            
            
            
            
            printLine();
        }


        calculateAverages();
        showResults();




    }
    
    /**
     * Decides, using a random number, whether or not a plane is ready to land.
     * @return whether the random minute is less than the number of arrivals per hour.
     */
    public static boolean newPlaneToLand() {

        return ((Math.random() * MIN_PER_HOUR) < AVG_ARRIVALS_PER_HOUR);
    }
    /**
     * Decides, using a random number, whether or not a plane is ready to take off.
     * @return whether the random minute is less than the number of departures per hour.
     */
    public static boolean newPlaneToDepart() {
        return ((Math.random() * MIN_PER_HOUR) < AVG_DEPARTURES_PER_HOUR);
    }
    /**
     * Checks to see if any new planes have arrived.
     */
    public void checkForNewPlanes(){
        if (newPlaneToLand()) {
            landingQueue.add(new Airplane(planeNumber, minute));
            eventPlane = "" + planeNumber;
            eventStatus = eventPlane + " arrives";
            planeNumber++;

        } else if (newPlaneToDepart()) {
            takeOffQueue.add(new Airplane(planeNumber, minute));
            eventPlane = "" + planeNumber;
            eventStatus = eventPlane + " departs";
            planeNumber++;

        }
    }
    
    /** 
     * Checks to see if the current plane is still using the runway. If not, clears the runway status and increments the appropriate counter.
     */
    public void isAnyoneUsingTheRunway() {
        if (landing) {
                landing = false;
                runwayStatus = "-";
                planesLanded++;
        }
        
        else if (takingOff) {
                takingOff = false;
                runwayStatus = "-";
                planesDeparted++;
        }
        else{
            
        }
    }
    
    /**
     * Initiates the landing. Removes the plane from the queue, sets runwayFreeAt to show when the runway will be free next.
     */
    public void doLanding() {
        
        Airplane landingPlane = landingQueue.get(0);
        waitTime = (minute - landingPlane.getTimeReady());
        if (waitTime > maxWaitLanding) {
            maxWaitLanding = waitTime;
        }
        runwayStatus = landingPlane.getId() + "L";
        totalWaitTimeLanding += waitTime;
        runwayFreeAt = minute + LANDING_WAIT_TIME;
        landingQueue.remove(0);
        landing = true;
    }
    /**
     * Initiates the take off. Removes the plane from the queue, sets runwayFreeAt to show when the runway will be free next.
     */
    public void doTakeOff() {
        Airplane departingPlane = takeOffQueue.get(0);
        //System.out.println(timeStamp + "  -  Plane " +departingPlane.getId() + " is taking off.");
        waitTime = (minute - departingPlane.getTimeReady());
        runwayStatus = departingPlane.getId() + "D";
        if (waitTime > maxWaitDeparting) {
            maxWaitDeparting = waitTime;
        }
        totalWaitTimeDeparting += waitTime;
        runwayFreeAt = minute + TAKEOFF_WAIT_TIME;
        takingOff = true;
        takeOffQueue.remove(0);

    }
    
    
    /**
     * Calculates all the averages needed to be displayed
     */
    public void calculateAverages() {
        
        avgWaitTimeDepart = ((float) totalWaitTimeDeparting / planesDeparted) * 60;
        avgWaitTimeLanding = ((float) totalWaitTimeLanding / planesLanded) * 60;
        avgWaitMinutesDepart = (int) avgWaitTimeDepart / 60;
        avgWaitSecondsDepart = avgWaitTimeDepart % 60;
        avgWaitMinutesLanding = (int) avgWaitTimeLanding / 60;
        avgWaitSecondsLanding = avgWaitTimeLanding % 60;
    }
    
    
    /**
     * Updates the max number of flights waiting.
     */
    public void updateMaxQueues() {
        waitingToDepart = takeOffQueue.size();
        waitingToLand = landingQueue.size();

        if (waitingToDepart > maxWaitingToDepart) {
            maxWaitingToDepart = waitingToDepart;
        }

        if (waitingToLand > maxWaitingToLand) {
            maxWaitingToLand = waitingToLand;
        }
    }
    /**
     * Checks to see if the runway is free.
     */
    public boolean isRunwayFree(){
        
            if (minute >= runwayFreeAt) {
                return true;
            }
            return false;
    }
    
    /**
     * Outputs a new table row for each minute.
     * 
     **/
    public void printLine() {
        runwayFreeAtTime = minuteToTime(runwayFreeAt);
        if (eventStatus.equals("-")) {
            System.out.println(timeStamp + "\t  " + eventPlane + "\t  " + eventStatus + "\t\t  " + runwayStatus + "\t    " 
                    + waitingToLand + "\t\t    " + waitingToDepart
                    + "\t\t      " + planesLanded + "         \t" + planesDeparted + "\t     " + runwayFreeAtTime);
        } else {
            System.out.println(timeStamp + "\t  " + eventPlane + "\t  " + eventStatus + "\t  " + runwayStatus + "\t    " 
                    + waitingToLand + "\t\t    " + waitingToDepart
                    + "\t\t      " + planesLanded + "          \t" + planesDeparted + "\t     " + runwayFreeAtTime);
        }

    }
    /**
     * Outputs statistics in sentence form.
     */
    public void showResults() {
        System.out.println("\n\n" + "Length of simulation:" + LENGTH_OF_SIMULATION_IN_HRS+ " hours.\n"
                + "Average rate of arriving planes: " + AVG_ARRIVALS_PER_HOUR + ".\n"
                + "Average rate of departing planes: " + AVG_DEPARTURES_PER_HOUR + ".\n"
                + "Time for a plane to land: " + LANDING_WAIT_TIME + ".\n"
                + "Time for a plane to depart: " + TAKEOFF_WAIT_TIME +".\n"
                + planesLanded + " planes have landed. \n"
                + planesDeparted + " planes have left.\n"
                + "Max wait time for landing: " + maxWaitLanding + " minutes.\n"
                + "Max wait time for departure: " + maxWaitDeparting + " minutes.\n"
                + "Average wait time for landing : " + avgWaitMinutesLanding + " minutes " + avgWaitSecondsLanding + " seconds.\n"
                + "Average wait time for departure: " + avgWaitMinutesDepart + " minutes " + avgWaitSecondsDepart + " seconds.\n"
                + "Maximum number of flights waiting to land: " + maxWaitingToLand + ".\n"
                + "Maximum number of flights waiting to depart: " + maxWaitingToDepart + ".");
    }

    public String minuteToTime(int inTime) {
        String time;
        if (inTime % MIN_PER_HOUR < 10) {
            time = inTime / MIN_PER_HOUR + ":0" + inTime % MIN_PER_HOUR;
        } else {
            time = inTime / MIN_PER_HOUR + ":" + inTime % MIN_PER_HOUR;
        }
        return time;
    }
}
