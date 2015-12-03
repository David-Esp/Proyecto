/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vrp.heuristics.exchange;

/**
 *
 * @author David
 */
public class Candidate {

    private boolean active;
    private int routeOrigin, routeDestiny, customer, demand, edge;
    private double savings;
    
    
    public int getEdge() {
        return edge;
    }

    public void setEdge(int edge) {
        this.edge = edge;
    }
    

    public Candidate(boolean active, int routeOrigin, int routeDestiny, int customer, int demand, int edge, double savings){
        this.active = active;
        this.customer = customer;
        this.demand = demand;
        this.routeDestiny = routeDestiny;
        this.routeOrigin = routeOrigin;
        this.edge = edge;
        this.savings = savings;
        
    }
    
    
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getRouteOrigin() {
        return routeOrigin;
    }

    public void setRouteOrigin(int routeOrigin) {
        this.routeOrigin = routeOrigin;
    }

    public int getRouteDestiny() {
        return routeDestiny;
    }

    public void setRouteDestiny(int routeDestiny) {
        this.routeDestiny = routeDestiny;
    }

    public int getCustomer() {
        return customer;
    }

    public void setCustomer(int customer) {
        this.customer = customer;
    }

    public int getDemand() {
        return demand;
    }

    public void setDemand(int demand) {
        this.demand = demand;
    }

    public double getSavings() {
        return savings;
    }

    public void setSavings(double savings) {
        this.savings = savings;
    }
    
    
}
