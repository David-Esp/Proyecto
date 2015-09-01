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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getxCoord() {
        return xCoord;
    }

    public void setxCoord(double xCoord) {
        this.xCoord = xCoord;
    }

    public double getyCoord() {
        return yCoord;
    }

    public void setyCoord(double yCoord) {
        this.yCoord = yCoord;
    }

    public double getDemand() {
        return demand;
    }

    public void setDemand(double demand) {
        this.demand = demand;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public int getTimeWindowStart() {
        return timeWindowStart;
    }

    public void setTimeWindowStart(int timeWindowStart) {
        this.timeWindowStart = timeWindowStart;
    }

    public int getTimeWindowEnd() {
        return timeWindowEnd;
    }

    public void setTimeWindowEnd(int timeWindowEnd) {
        this.timeWindowEnd = timeWindowEnd;
    }

    public boolean isIsTimeWindowFlexible() {
        return isTimeWindowFlexible;
    }

    public void setIsTimeWindowFlexible(boolean isTimeWindowFlexible) {
        this.isTimeWindowFlexible = isTimeWindowFlexible;
    }

    public List<Double> getDistances() {
        return distances;
    }

    public void setDistances(List<Double> distances) {
        this.distances = distances;
    }

    public List<Integer> getTimes() {
        return times;
    }

    public void setTimes(List<Integer> times) {
        this.times = times;
    }
   
    
}
