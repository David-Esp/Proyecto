/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vrp.Problem;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author David
 */
  public class Route {
    
    private double distance;              //Distancia total de la ruta 
    private double demand;                //Demanda total de los clientes en la ruta 
    private List<Customer> customers;     //Lista de los clientes de la ruta
    private List<Edge> edges;             //Lista de los arcos que conforman la ruta
    
    private int vehicleCapacity; //Capacidad del vehiculo
    
    private int time;
       /**
     *
     * @param depot
     * @param capacity
     */
    public Route(Customer depot, int capacity) {
        
        customers = new ArrayList<>(100);
        edges = new ArrayList<>(100);
        customers.add(depot);
        distance = 0;
        time = 0;
        demand = 0;
        vehicleCapacity = capacity;
    }
    
    /**
     *
     * @param customer
     * @return
     */
    public boolean insertCustomer(Customer customer){
        
         customers.add(customer);
         vehicleCapacity = vehicleCapacity - (int) customer.getDemand();
         return true;
//        customers.add(customer);
//         
//        computeDemand();    
//        
//         if (isFeasible()) {
//            vehicleCapacity = vehicleCapacity - (int) customer.getDemand();
//            
//            return true;
//        } else {
//            customers.remove(customers.size() - 1);
//            return false;
//        }
        
        
        
    }
    
    /**
     *
     * @param customer
     * @param index
     * @return
     */
    public boolean insertCustomerAt(Customer customer, int index){
        
         customers.add(index, customer);
          
         vehicleCapacity = vehicleCapacity - (int) customer.getDemand();
         demand = demand + (int) customer.getDemand();
         distance  = computeDistance();
         return true;   
    }
    
        //This will check any condition that should be met by the routes
    private boolean isFeasible() {
        //TODO
       // System.err.println("demand= " + demand + " vehiclecap= " + vehicleCapacity);
        //Check vehicle capacity is not exceeded by demand
        if (demand > vehicleCapacity) {
            return false;
        }else
            return true;
        
    }
    
//        // To compute the distance we use the fact that when a client is added some data is stored in the client as bookkeeping.
//    private void computeDistance() {
//        ClientLog lastClientInRoute = clientLogs.get(getClientCount());
//        distance = lastClientInRoute.getDistanceToHereInRoute() + lastClientInRoute.getDistanceTo(0);
//    }
//
//    // To compute the time we use the data stored in the clients.
//    private void computeTime() {
//        ClientLog lastClientInRoute = clientLogs.get(getClientCount());
//        time = lastClientInRoute.getBeginOfServiceTime() + lastClientInRoute.getServiceTime() + lastClientInRoute.getTimeTo(0);
//    }

    // To compute the time we use the data stored in the clients.
    private void computeDemand() {
        demand = 0;
        for (int index = 1; index < customers.size(); index++) {
            demand += customers.get(index).getDemand();
        }
    }
    
   // To compute the time we use the data stored in the clients.
    private double computeDistance() {
        double distancia = 0;
        //distancia = edges.stream().map((edge) -> edge.getDistance()).reduce(distancia, (accumulator, _item) -> accumulator + _item); //  demand += customers.get(index).getDemand();
        for (Edge edge : edges) {
            //  demand += customers.get(index).getDemand();
            distancia += getDistanceFromTo(edge.getCustomer1(), edge.getCustomer2());
            
        }
        distance = distancia;
        return distancia;
    }
    
    private double computeTravelTime(){
         double distancia = 0;
        for (Edge edge : edges) {
            //  demand += customers.get(index).getDemand();
            distancia += edge.getDistance();
        }
        
        return distancia;
    }
    
    
        /**
     * Returns the string representation of this problem.
     * <p>
     * @return The string representation of this problem.
     */
    public final String toString() {
        StringBuilder string;
        string = new StringBuilder();
        ListIterator<Customer> iterator;
        computeDemand();
        string.append("Demanda de la ruta = ").append(demand).append("\n");
        double distanciaRecorrida = computeDistance();
        
        string.append("Distancia recorrida de la ruta = ").append(distanciaRecorrida).append("\n");
        /*
         * We add the customers
         */
        iterator = customers.listIterator();
        while (iterator.hasNext()) {
            string.append(iterator.next().getNumber()).append(" -> ");
        }
        
        
        return string.toString().trim();
    }
    
    
    
    
    // ------------------ Getters
    
    /**
     *
     * @return
     */
        
     public double getDistance() {
         return distance;
    }
     
    private double getDistanceFromTo(Customer customerOrigin, Customer customerDestiny){
   
       double xCoord = Math.abs( customerDestiny.getxCoord() - customerOrigin.getxCoord() );
       double yCoord = Math.abs( customerDestiny.getyCoord() - customerOrigin.getyCoord() );
       double distance = Math.sqrt((xCoord *  xCoord) + (yCoord * yCoord));
       
       return distance;
       
   }

    /**
     *
     * @return
     */
    public int getTime() {
        return time;
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
     * @param distance
     */
    public void setDistance(double distance) {
        this.distance = distance;
        
    }
    
    /**
     *
     * @param capacity
     */
    public void setVehicleCapacity( int capacity){
        this.vehicleCapacity = capacity;
    }
    
    /**
     *
     * @param demand
     */
    public void setDemand(double demand){
        this.demand = demand;
        
    }

    /**
     *
     * @return
     */
    public int getVehicleCapacity() {
        return vehicleCapacity;
    }

    /**
     *
     * @return
     */
    public List<Customer> getCustomers() {
        return customers;
    }
    
    /**
     *
     * @return
     */
    public List<Edge> getEdges() {
        return edges;
    }
}
