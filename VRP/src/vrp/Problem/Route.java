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
    
    private double distance;
    private int time;
    private double demand;
    private int vehicleCapacity;
    private List<Customer> customers;
    private List<Edge> edges;
    
    
       /**
     *
     * @param depot
     * @param vehicleCapacity
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
    
    
    
        public boolean insertCustomerAt(Customer customer, int index){
        
         customers.add(index, customer);
          
         vehicleCapacity = vehicleCapacity - (int) customer.getDemand();
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
    
     public double getDistance() {
//        distance = 0;
//        
//         
//        ListIterator<Customer> iterator; 
//         
//        Customer customerOrigin = null,customerDestiny = null, customerDeposit  = null;
//        
//                
//        /*
//         * We add the customers
//         */
//        iterator = customers.listIterator();
//        while (iterator.hasNext()) {
//            if(customerOrigin == (null)){
//                customerDeposit =   iterator.next();
//                 customerDestiny = customerDeposit;
//                 customerOrigin = customerDeposit;
//                 
//         }else{
//                customerDestiny = iterator.next();
//                distance = distance + getDistanceFromTo(customerOrigin, customerDestiny);
//                customerOrigin = customerDestiny;
//            }
//            
//            
//            
//            
//        }
//          distance = distance + getDistanceFromTo(customerOrigin, customerDeposit);
//         
        //computeDistance();
         return distance;
    }
     
    private double getDistanceFromTo(Customer customerOrigin, Customer customerDestiny){
   
       double xCoord = Math.abs( customerDestiny.getxCoord() - customerOrigin.getxCoord() );
       double yCoord = Math.abs( customerDestiny.getyCoord() - customerOrigin.getyCoord() );
       double distance = Math.sqrt((xCoord *  xCoord) + (yCoord * yCoord));
       
       return distance;
       
   }

    public int getTime() {
        return time;
    }

    public double getDemand() {
        return demand;
    }
    
    public void setDistance(double distance) {
        this.distance = distance;
        
    }

    public int getVehicleCapacity() {
        return vehicleCapacity;
    }

    public List<Customer> getCustomers() {
        return customers;
    }
    
    public List<Edge> getEdges() {
        return edges;
    }
}
