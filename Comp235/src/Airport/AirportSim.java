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
    private int landTime;
    private int takeOffTime;
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
    float avgWaitTimeDepart;
    float avgWaitTimeLanding;
    int avgWaitMinutesDepart;
    float avgWaitSecondsDepart;
    int avgWaitMinutesLanding;
    float avgWaitSecondsLanding;
    int waitingToLand;
    int waitingToDepart;
    int maxWaitingToLand;
    int maxWaitingToDepart;

    public void run() {
        //main body

        for (minute = 1; minute <= LENGTH_OF_SIMULATION_IN_HRS * MIN_PER_HOUR; minute++) {
            // Print the time.
            if (minute % MIN_PER_HOUR < 10) {
                timeStamp = minute / MIN_PER_HOUR + ":0" + minute % MIN_PER_HOUR; 
            } else {
                timeStamp = minute / MIN_PER_HOUR + ":" + minute % MIN_PER_HOUR;
            }
            
            checkQueues();
            

            if (!runwayIsFree) {
                clearRunway();
            }
            
            


            // is there a plane about to land?
            if (readyToLand()) {
                landingQueue.add(new Airplane(planeNumber, minute));
                //System.out.println(timeStamp + "  -  Plane " + planeNumber + " is ready to land.");
                planeNumber++;
            }





            if (readyToTakeOff()) {
                takeOffQueue.add(new Airplane(planeNumber, minute));
                //System.out.println(timeStamp + "  -  Plane " + planeNumber + " is ready to take off.");
                planeNumber++;
            }
            
            // if the runway is free and there is a plane waiting to land, it gets to land, 
            
            if (runwayIsFree && landingQueue.size() > 0){
                doLanding();
            }
            
            if (runwayIsFree && takeOffQueue.size() > 0){
                doTakeOff();
            }
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
                    if (minute == landTime + LANDING_WAIT_TIME) {
                        //System.out.println(timeStamp + "  -  Plane " + landingQueue.get(0).getId() + " has landed. Runway is free again");
                        runwayIsFree = true;
                        landing = false;
                        planesLanded++;
                        landingQueue.remove(0);
                    }
                }

                //is a plane still taking off?
                if (takingOff) {
                    if (minute == takeOffTime + TAKEOFF_WAIT_TIME) {
                        //System.out.println(timeStamp + "  -  Plane " + takeOffQueue.get(0).getId() + " has taken off. Runway is free again");
                        takingOff = false; 
                        runwayIsFree = true;
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
                runwayIsFree = false;
                landing = true;
                landTime = minute;
    }
    
    public void doTakeOff(){
        Airplane departingPlane = takeOffQueue.get(0);
                //System.out.println(timeStamp + "  -  Plane " +departingPlane.getId() + " is taking off.");
                waitTime = (minute - departingPlane.getTimeReady());
                if(waitTime > maxWaitDeparting){
                    maxWaitDeparting = waitTime;
                }
                totalWaitTimeDeparting += waitTime;
                runwayIsFree = false;
                takingOff = true;
                takeOffTime = minute;
                
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
}
