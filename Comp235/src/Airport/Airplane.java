/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Airport;

/**
 *
 * @author Owner
 */
public class Airplane {
    
    private int planeId;
    private int timeReady;
    
    public Airplane(int planeNum, int readyTime){
        planeId = planeNum;
        timeReady = readyTime;
        
    }
    
    public int getId(){
        return planeId;
    }
    
    public int getTimeReady(){
        return timeReady;
    }
}
