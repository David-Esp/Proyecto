/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vrp.Problem;

import RaHHD.Exceptions.NoSuchFeatureException;
import RaHHD.Exceptions.NoSuchHeuristicException;
import RaHHD.FeatureManager;
import RaHHD.HeuristicManager;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import vrp.heuristics.Heuristic_CWDT;
import vrp.heuristics.Heuristic_NNH;
import vrp.heuristics.VRPHeuristic;


/**
 *
 * @author David
 */
public class VehicleRoutingProblem implements FeatureManager, HeuristicManager {

    
    @Override
    /**
     * NOTE: Features need to be normalized in the range [0, 1].
     */
    public double getValueOfFeature(String featureName) throws NoSuchFeatureException {
        switch (featureName) {
            case "DIF":
                 
                    return 0;
                
            default:
                throw new NoSuchFeatureException(featureName);
        }
    }

    @Override
    /**
     * NOTE: Heuristics must be implemented as classes in order to be used by the system.
     */
    public RaHHD.Heuristic getHeuristic(String heuristicName) throws NoSuchHeuristicException {        
        switch (heuristicName) {
            case "CWDT":
                return new Heuristic_CWDT();
            case "NNH":
                return new Heuristic_NNH(); 
            default:
                throw new NoSuchHeuristicException(heuristicName);
        }
    }
    
    private final int vehicles;
    private final int capacity;
    private final String instanceName ;
    
    private final Customer depot;
    
    private final List<Customer> customers;
    private final List<Customer> addedCustomers;
    
    private final List<Route> routes;
    
    private final List<Edge> edges;

    public List<Edge> getEdges() {
        return edges;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    
      Customer customer; 
     
    
    public VehicleRoutingProblem(String fileName) {
        
        
        
        
        String text;
        text = Utils.readFromFile(fileName);
        
        String[] result = text.split("\n");
        
        if(result.length < 10){
            System.out.println("The instance \'" + fileName + "\'cannot be loaded.");
            System.out.println("The system will halt.");
            System.exit(1);
            vehicles = -1;
            capacity = -1;
            depot = null;
            customers = null;
            instanceName = null;
        } else {
            instanceName = result[0].trim();
           // System.out.println(instanceName);
            
            String[] resultCap = result[4].trim().split("\\s+");
            vehicles = Integer.parseInt(resultCap[0].trim());
            capacity = Integer.parseInt(resultCap[1].trim());
            customers = new ArrayList<>(result.length);
               
            depot = new Customer(result[9]);
         for (int x=10; x<(result.length -1); x++)
            {
            customer = new Customer(result[x]);
            //System.out.println(result[x]);
            customers.add(customer);
            //System.out.println("" + customer.getTimeWindowStart());
            }
        }
        
        routes = new LinkedList();
        addedCustomers = new LinkedList();
        edges = new LinkedList();
    
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
        ListIterator<Route> iteratorRoute;
        
        string.append("Instance Name = ").append(instanceName).append("\n");
        string.append("Vehicles = ").append(vehicles).append("\n");
        string.append("Capacity = ").append(capacity).append("\n");
        
        /*
         * We add the clients
        
        iterator = customers.listIterator();
        while (iterator.hasNext()) {
            string.append(iterator.next().toString()).append("\n");
        }
         */
        iteratorRoute = routes.listIterator();
        while (iteratorRoute.hasNext()) {
            string.append(iteratorRoute.next().toString()).append("\n");
        }
        
        
        return string.toString().trim();
    }
    
    
    
    
        /**
     * Solves this problem by using the hyper-heuristic provided.
     * <p>
     * @param hyperHeuristic A hyper-heuristic to be used to solve this problem.
     */
    public void solve() {    
        
        Route route ;
        route = new Route(depot, capacity); 
        
        
        System.out.println(route.toString());
        
        
        
        while (!customers.isEmpty()) {
      
        addNextCustomer(); 
        
        }

        
    }
    
    
    
    /**
     * Solves this instance by using the heuristic provided.
     * <p>
     * @param heuristic The heuristic to solve this instance.
     * @return The cost of the solution.
     */
    public double solve(VRPHeuristic  heuristic) {
        
//        
//        int cycles;
//        
//        
//         Route route ;
//        route = new Route(depot, capacity); 
//        
//        
//        System.out.println(route.toString());
//        
//        
//        
//        while (!customers.isEmpty()) {
//      
//        addNextCustomer(); 
//        
//        }
//        
//        return 0;
//        
        heuristic.getNextElement(this);
        
        
        return 0;
    }        
    
    
    
      
        /**
     * Packs the next item by using the heuristic provided.
     * <p>
     * @param heuristic The heuristic to be used to pack the item.
     */
    public void addNextCustomer() {        
       Customer customer = null;          
          
        double nextFound = -1;  
        ListIterator<Customer> iterator;
        iterator = customers.listIterator();
        double bestFound = 1000000;
        int count = -1;
        int coin = -1;
        
        
        while (iterator.hasNext()) {
            
                customer = iterator.next();
                count ++;
                
                nextFound = getDistanceFromTo(depot, customer);
                
                if (nextFound < bestFound){
                bestFound = nextFound;
                coin = count;
                }
                
        }
        
        System.out.println(""+bestFound);
        
        
        customer = customers.remove(coin);
        
        nextFound = getDistanceFromTo(depot, customer);
            System.out.println(""+nextFound);
            
        firstFit(customer);                
             
        addedCustomers.add(customer);
    }
    
    private void firstFit(Customer customer) {
    
    Route route;   
    ListIterator<Route> iterator;
        /*
         * If there are no route to use, a new route is created.
         */ 
    
        if (routes.isEmpty()) {
            placeItem(customer, null);
        } else {
            iterator = routes.listIterator();
            while (iterator.hasNext()) {
                route = iterator.next();
                if (route.getDemand()+ customer.getDemand() <= route.getVehicleCapacity()){
                    placeItem( customer, route);
                    return;
                }
            }
            placeItem( customer, null);
        }
    }
    
    
    /**
     * Packs an item of the provided size into one specific bin.
     * <p>
     * @param itemSize The size of the item to pack.
     * @param bin The bin where the item will be packed into.
     */
    private void placeItem(Customer customer, Route route) {
        if (route != null) {            
            if (!route.insertCustomer(customer)){
                
                System.out.println("The item cannot be placed in the route.");
                System.out.println("The system will halt.");
                System.exit(1);
            }else{
                
            }        
   
        }else {
            /*
             * If no bin has enough capacity to contain the item, a new bin is opened and the item
             * is placed there.
             */
             Route bin;
             bin = new Route(depot, capacity);
             bin.insertCustomer(customer);
             routes.add(bin);
        }
    }
    
    
       /**
     * Opens a new bin.
     
    private Route openBin() {
        Route bin;
        bin = new Route(depot, capacity);
        //openBins.add(bin);
        return bin;
    }
*/
    
   private double getDistanceFromTo(Customer customerOrigin, Customer customerDestiny){
   
       double xCoord = Math.abs( customerDestiny.getxCoord() - customerOrigin.getxCoord() );
       double yCoord = Math.abs( customerDestiny.getyCoord() - customerOrigin.getyCoord() );
       double distance = Math.sqrt((xCoord *  xCoord) + (yCoord * yCoord));
       
       return distance;
       
   }
   
   
   public double CalculateObjectiveFunction(VehicleRoutingProblem problem)
   {
       int Vehicles = problem.getRoutes().size();
       double TotalDistance = 0;
       
       
       List<Route> rutas = problem.getRoutes();
       Route route;   
       ListIterator<Route> iterator;
        iterator = rutas.listIterator();
        
        while (iterator.hasNext()) {
        route = iterator.next(); 
         
        TotalDistance = route.getDistance() + TotalDistance;
        }
        
           
       
   return Vehicles + TotalDistance;
   }
   
    
    
    //----Getters
    
     public int getVehicles() {
        return vehicles;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public List<Customer> getAddedCustomers() {
        return addedCustomers;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public Customer getCustomer() {
        return customer;
    }
    
    public Customer getDepot() {
        return depot;
    }
}
