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
    //Clientes que se unen por este arco
    private Customer customer1, customer2;
    //Tiempo en que el cliente 1 termina su servicio
    private double endOfServiceCustomer1;
    //Tiempo de espera al llegar al cliente 2
    private double waitingTime;
    
    
    //Ruta a la que pertenece el arco
    private Route route;
    private double demand;
    /**
     *
     * @return
     */
    public double getWaitingTime() {
        return waitingTime;
    }

    /**
     *
     * @param waitingTime
     */
    public void setWaitingTime(double waitingTime) {
        this.waitingTime = waitingTime;
    }
    
    /**
     *
     * @return
     */
    public double getEndOfServiceCustomer1() {
        return endOfServiceCustomer1;
    }

    /**
     *
     * @param endOfServiceCustomer1
     */
    public void setEndOfServiceCustomer1(double endOfServiceCustomer1) {
        this.endOfServiceCustomer1 = endOfServiceCustomer1;
    }
    
    /**
     *
     * @param customer1
     * @param customer2
     * @param route
     * @param demand
     * @param distance
     * @param eoS
     * @param wTj
     */
    public Edge(Customer customer1, Customer customer2, Route route, double demand, double distance, double eoS, double wTj) {
        
        this.distance = distance;
        this.customer1 = customer1;
        this.customer2 = customer2;
        this.route = route;
        this.demand = demand;
        this.endOfServiceCustomer1 = eoS;
        this.waitingTime = wTj;
      
    }
    
    /**
     *
     * @param distance
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     *
     * @param customer1
     */
    public void setCustomer1(Customer customer1) {
        this.customer1 = customer1;
    }

    /**
     *
     * @param customer2
     */
    public void setCustomer2(Customer customer2) {
        this.customer2 = customer2;
    }

    /**
     *
     * @return
     */
    public double getDistance() {
        return distance;
    }

    /**
     *
     * @return
     */
    public Customer getCustomer1() {
        return customer1;
    }

    /**
     *
     * @return
     */
    public Customer getCustomer2() {
        return customer2;
    }
    
    /**
     *
     * @param route
     */
    public void setRoute(Route route) {
        this.route = route;
    }

    /**
     *
     * @param demand
     */
    public void setDemand(int demand) {
        this.demand = demand;
    }

    /**
     *
     * @return
     */
    public Route getRoute() {
        return route;
    }

    /**
     *
     * @return
     */
    public double getDemand() {
        return demand;
    }
    
}
