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
import RaHHD.HeuristicSelector;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import vrp.heuristics.Heuristic_2OPT;
import vrp.heuristics.Heuristic_CWDT;
import vrp.heuristics.Heuristic_I1;
import vrp.heuristics.Heuristic_NNH;
import vrp.heuristics.Heuristic_Relocate;
import vrp.heuristics.VRPHeuristic;

/**
 *
 * @author David
 */
public class VehicleRoutingProblem implements FeatureManager, HeuristicManager {

    private final int vehicles;
    private final int capacity;
    private final String instanceName;

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

        if (result.length < 10) {
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

            for (int x = 10; x < (result.length); x++) {
                try {
                    customer = new Customer(result[x]);
                    //System.out.println(result[x]);
                    customers.add(customer);
                    //System.out.println("" + customer.getTimeWindowStart());
                } catch (Exception exeption) {

                }
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

    private double getDistanceFromTo(Customer customerOrigin, Customer customerDestiny) {

        double xCoord = Math.abs(customerDestiny.getxCoord() - customerOrigin.getxCoord());
        double yCoord = Math.abs(customerDestiny.getyCoord() - customerOrigin.getyCoord());
        double distance = Math.sqrt((xCoord * xCoord) + (yCoord * yCoord));

        return distance;

    }

    public double CalculateObjectiveFunction(VehicleRoutingProblem problem) {
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
    
    public int CalculateVehiclesUsed(VehicleRoutingProblem problem){
        int Vehicles = problem.getRoutes().size();
        
        return Vehicles;
    }
    
   public double CalculateDistance(VehicleRoutingProblem problem) {
        
        List<Route> rutas = problem.getRoutes();    
        double totalDistancia = 0; 
        
            for (Route ruta : rutas) {
                 
                //totalDistancia += ruta.getDistance();
                totalDistancia += calculaDistanciaRuta(ruta);
               // totalTiempo += ruta.getTime();
            }
        return totalDistancia;
    }
   
        
    public double calculaDistanciaRuta(Route ruta){
        double dRuta = 0;
        List<Edge> arcos = ruta.getEdges();
        
        for (Edge arco : arcos) {
             double dis = getDistanceFromTo(arco.getCustomer1(), arco.getCustomer2());
             dRuta += dis;          
        } 
        return dRuta;
    }    
 
    
    public double MaxDistance(VehicleRoutingProblem problem){
            List<Customer> clientes = problem.addedCustomers;
            Customer cliente;
            ListIterator<Customer> iterator;
            iterator = clientes.listIterator();
            double distance = 0;
            while (iterator.hasNext()){
                cliente = iterator.next();
                distance += getDistanceFromTo(cliente, this.depot);
            }
            return distance;
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
    
    
    
        /**
     * Solves this instance by using the heuristic provided.
     * <p>
     * @param heuristic The heuristic to solve this instance.
     * @return The cost of the solution.
     */
    public double solve(VRPHeuristic heuristic) {
        int cycles;
       // setA = new ArrayList(elements);
       // setB = new ArrayList((int) Math.floor(elements.size() / 2));
       // cycles = (int) Math.floor(setA.size() / 2);
        cycles = 1;
        for (int i = 0; i < cycles; i++) {                    
             heuristic.getNextElement(this);
        }
        return CalculateDistance(this);
    }        

        // -------------------------------------------------------------------
    // Everything below this point is required to use the RaHHD Framework.
    // -------------------------------------------------------------------
    /**
     * This method is required to let your system know how to handle the
     * heuristic selection process.
     */
       /**
     * This method is required to let your system know how to handle the heuristic selection process.
     */    
    /**
     * Solves this instance by using the heuristic selector provided.
     * <p>
     * @param selector The heuristic selector to solve this instance.
     * @return The cost of the solution.
     */
    public double solve(HeuristicSelector selector) {
        int cycles;
        
        VRPHeuristic heuristic;
        //setA = new ArrayList(elements);
        //setB = new ArrayList((int) Math.floor(elements.size() / 2));
        //cycles = (int) Math.floor(setA.size() / 2);
        cycles = 1;
        for (int i = 0; i < cycles; i++) {
            heuristic = null;
            try {
                /*
                 * In this case the featuere and heuristic manager are implemented by this class.
                 */
                heuristic = (VRPHeuristic) selector.getHeuristic(this, this);
            } catch (NoSuchFeatureException | NoSuchHeuristicException e) {
                System.out.println(e);
                System.out.println("The system will halt.");
                System.exit(1);
            }
           // setB.add(heuristic.getNextElement(this));
            heuristic.getNextElement(this);
        }
        return CalculateDistance(this);
    }
    
    @Override
    /**
     * NOTE: Features need to be normalized in the range [0, 1].
     */
    public double getValueOfFeature(String featureName) throws NoSuchFeatureException {
        switch (featureName) {
            
//            case "Veh":
//                return this.getRoutes().size()/this.vehicles;
            case "DIF":
                return CalculateDistance(this)/MaxDistance(this);
            default:
                throw new NoSuchFeatureException(featureName);
        }
    }

    @Override
    /**
     * NOTE: Heuristics must be implemented as classes in order to be used by
     * the system.
     */
    public RaHHD.Heuristic getHeuristic(String heuristicName) throws NoSuchHeuristicException {
        switch (heuristicName) {
//            case "CWDT":
//                return new Heuristic_CWDT();
//            case "NNH":
//                return new Heuristic_NNH();
//            case "I1":
//                return new Heuristic_I1();
            case "2OPT":
                return new Heuristic_2OPT();
            case "REL":
                return new Heuristic_Relocate();
            default:
                throw new NoSuchHeuristicException(heuristicName);
        }
    }

}
