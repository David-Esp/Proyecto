/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vrp.Problem;

import java.util.List;

/**
 *
 * @author David
 */
public class Customer {
    
    private int number;
    private double xCoord;
    private double yCoord;
    private double demand;
    
    private int serviceTime;  //How long it takes to complete the service
    
    private int timeWindowStart;  //Ready Time
    private int timeWindowEnd;   //Due date
    
    
    private boolean isTimeWindowFlexible;
    private List<Double> distances;
    private List<Integer> times;
    
    /**
     *
     * @param customerTxt
     */
    public Customer(String customerTxt) {
        
        String[] caract = customerTxt.trim().split("\\s+");
        
        number = Integer.parseInt(caract[0]);
        xCoord = Integer.parseInt(caract[1]);
        yCoord = Integer.parseInt(caract[2]);
        demand = Integer.parseInt(caract[3]);
        serviceTime = Integer.parseInt(caract[6]);
        timeWindowStart = Integer.parseInt(caract[4]);
        timeWindowEnd = Integer.parseInt(caract[5]);
        isTimeWindowFlexible = false;
        distances = null;
        times = null;
        
    }
    
     public final String toString() {
     
        StringBuilder string;
        string = new StringBuilder();
        
        string.append(number).append(" ");
        string.append(xCoord).append(" ");
        string.append(yCoord).append(" ");
        string.append(demand).append(" ");
        string.append(timeWindowStart).append(" ");
        string.append(timeWindowEnd).append(" ");
        string.append(serviceTime).append(" ");
        
        
        return string.toString().trim();
     
     }
     
    // ----------------------    Getters And Setters    -------------------------

    /**
     *
     * @return
     */
    
    public int getNumber() {
        return number;
    }

    /**
     *
     * @param number
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     *
     * @return
     */
    public double getxCoord() {
        return xCoord;
    }

    /**
     *
     * @param xCoord
     */
    public void setxCoord(double xCoord) {
        this.xCoord = xCoord;
    }

    /**
     *
     * @return
     */
    public double getyCoord() {
        return yCoord;
    }

    /**
     *
     * @param yCoord
     */
    public void setyCoord(double yCoord) {
        this.yCoord = yCoord;
    }

    /**
     *
     * @return
     */
    public double getDemand() {
        return demand;
    }

    /**
     *
     * @param demand
     */
    public void setDemand(double demand) {
        this.demand = demand;
    }

    /**
     *
     * @return
     */
    public int getServiceTime() {
        return serviceTime;
    }

    /**
     *
     * @param serviceTime
     */
    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    /**
     *
     * @return
     */
    public int getTimeWindowStart() {
        return timeWindowStart;
    }

    /**
     *
     * @param timeWindowStart
     */
    public void setTimeWindowStart(int timeWindowStart) {
        this.timeWindowStart = timeWindowStart;
    }

    /**
     *
     * @return
     */
    public int getTimeWindowEnd() {
        return timeWindowEnd;
    }

    /**
     *
     * @param timeWindowEnd
     */
    public void setTimeWindowEnd(int timeWindowEnd) {
        this.timeWindowEnd = timeWindowEnd;
    }

    /**
     *
     * @return
     */
    public boolean isIsTimeWindowFlexible() {
        return isTimeWindowFlexible;
    }

    /**
     *
     * @param isTimeWindowFlexible
     */
    public void setIsTimeWindowFlexible(boolean isTimeWindowFlexible) {
        this.isTimeWindowFlexible = isTimeWindowFlexible;
    }

    /**
     *
     * @return
     */
    public List<Double> getDistances() {
        return distances;
    }

    /**
     *
     * @param distances
     */
    public void setDistances(List<Double> distances) {
        this.distances = distances;
    }

    /**
     *
     * @return
     */
    public List<Integer> getTimes() {
        return times;
    }

    /**
     *
     * @param times
     */
    public void setTimes(List<Integer> times) {
        this.times = times;
    }
   
    
}
