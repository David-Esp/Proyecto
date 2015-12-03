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
public class RouteExchange {
    private int route;
    private int capacity;
    private int maxCapacity;

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    
    public RouteExchange(int route, int cap, int maxCap) {
        this.route = route;
        this.capacity = cap;
        this.maxCapacity = maxCap;
    }
    
    
    public int getRoute() {
        return route;
    }

    public void setRoute(int route) {
        this.route = route;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    
}
