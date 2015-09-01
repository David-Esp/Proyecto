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
public class Edge {
    
    //Distancia que existe entre los clientes
    private double distance;
    private Route route;


  
    private double demand;
    //Clientes que se unen por este arco
    private Customer customer1, customer2;
    private double endOfServiceCustomer1;
    private double waitingTime;

    public double getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(double waitingTime) {
        this.waitingTime = waitingTime;
    }
    

    public double getEndOfServiceCustomer1() {
        return endOfServiceCustomer1;
    }

    public void setEndOfServiceCustomer1(double endOfServiceCustomer1) {
        this.endOfServiceCustomer1 = endOfServiceCustomer1;
    }
    
    
    
    public Edge(Customer customer1, Customer customer2, Route route, double demand, double distance, double eoS, double wTj) {
        
        this.distance = distance;
        this.customer1 = customer1;
        this.customer2 = customer2;
        this.route = route;
        this.demand = demand;
        this.endOfServiceCustomer1 = eoS;
        this.waitingTime = wTj;
      
    }
    
    public void setDistance(double distance) {
        this.distance = distance;
    }

  

    public void setCustomer1(Customer customer1) {
        this.customer1 = customer1;
    }

    public void setCustomer2(Customer customer2) {
        this.customer2 = customer2;
    }

    public double getDistance() {
        return distance;
    }

  

    public Customer getCustomer1() {
        return customer1;
    }

    public Customer getCustomer2() {
        return customer2;
    }
    
        public void setRoute(Route route) {
        this.route = route;
    }

    public void setDemand(int demand) {
        this.demand = demand;
    }

    public Route getRoute() {
        return route;
    }

    public double getDemand() {
        return demand;
    }
    
}
