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
import vrp.heuristics.Heuristic_CompleteReset;
import vrp.heuristics.Heuristic_Exchange;
import vrp.heuristics.Heuristic_I1;
import vrp.heuristics.Heuristic_NNH;
import vrp.heuristics.Heuristic_PartialReset;
import vrp.heuristics.Heuristic_RandomReset;
import vrp.heuristics.Heuristic_Relocate;
import vrp.heuristics.Heuristic_RelocateIntraRoute;
import vrp.heuristics.Heuristic_SelectiveReMake;
import vrp.heuristics.Heuristic_SelectiveReset;
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

    /**
     *
     * @return
     */
    public List<Edge> getEdges() {
        return edges;
    }

    /**
     *
     * @param customer
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    Customer customer;

    /**
     *
     * @param fileName
     */
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
                    //System.out.println("" + customerRouted.getTimeWindowStart());
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
        
         iteratorCustomerUnrouted = customers.listIterator();
         while (iteratorCustomerUnrouted.hasNext()) {
         string.append(iteratorCustomerUnrouted.next().toString()).append("\n");
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

    /**
     *
     * @param problem
     * @return
     */
    public double CalculateDistance(VehicleRoutingProblem problem) {

        List<Route> routesIn = problem.getRoutes();
        double totalDistance = 0;

        for (Route route : routesIn) {
            totalDistance += CalculateRouteDistance(route);
        }

        List<Customer> unroutedCustomers = problem.getCustomers();
        Customer unroutedCustomer;
        ListIterator<Customer> iteratorCustomer;
        iteratorCustomer = unroutedCustomers.listIterator();
        double distanceUnroutedCustomers = 0;
        while (iteratorCustomer.hasNext()) {
            unroutedCustomer = iteratorCustomer.next();
            distanceUnroutedCustomers += getDistanceFromTo(unroutedCustomer, this.depot);
        }

        totalDistance += distanceUnroutedCustomers;

        return totalDistance;
    }

    /**
     *
     * @param ruta
     * @return
     */
    public double CalculateRouteDistance(Route ruta) {
        double dRuta = 0;
        List<Edge> arcos = ruta.getEdges();

        for (Edge arco : arcos) {
            double dis = getDistanceFromTo(arco.getCustomer1(), arco.getCustomer2());
            dRuta += dis;
        }
        return dRuta;
    }

    /**
     *
     * @param problem
     * @return
     */
    public double MaxDistance(VehicleRoutingProblem problem) {
        List<Customer> customersUnrouted = problem.customers;
        List<Customer> cutomersRouted = problem.addedCustomers;
        Customer customerRouted;
        Customer customerUnrouted;
        ListIterator<Customer> iteratorCustomerUnrouted;
        ListIterator<Customer> iteratorCustomerRouted;
        iteratorCustomerUnrouted = customersUnrouted.listIterator();
        iteratorCustomerRouted = cutomersRouted.listIterator();

        double distance = 0;
        while (iteratorCustomerUnrouted.hasNext()) {
            customerUnrouted = iteratorCustomerUnrouted.next();
            distance += getDistanceFromTo(customerUnrouted, this.depot);
        }

        while (iteratorCustomerRouted.hasNext()) {
            customerRouted = iteratorCustomerRouted.next();
            distance += getDistanceFromTo(customerRouted, this.depot);
        }

        return distance;
    }
    //----Getters

    /**
     *
     * @return
     */
    public int getVehicles() {
        return vehicles;
    }

    /**
     *
     * @return
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     *
     * @return
     */
    public String getInstanceName() {
        return instanceName;
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
    public List<Customer> getAddedCustomers() {
        return addedCustomers;
    }

    /**
     *
     * @return
     */
    public List<Route> getRoutes() {
        return routes;
    }

    /**
     *
     * @return
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     *
     * @return
     */
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

        double actual = CalculateDistance(this);
        double improve;
        int some = 1;
        int coin;
        coin = 10;
        //  while(some != 0    ){
        while (some == 1 || coin > 1) {
            some = heuristic.getNextElement(this);

            improve = CalculateDistance(this);
            if (improve < actual) {
                coin = 10;
                actual = improve;
            } else {
                coin--;
            }
        }

        return CalculateDistance(this);
    }

    // -------------------------------------------------------------------
    // Everything below this point is required to use the RaHHD Framework.
    // -------------------------------------------------------------------
    /**
     * This method is required to let your system know how to handle the
     * heuristic selection process. Solves this instance by using the heuristic
     * selector provided.
     * <p>
     * @param selector The heuristic selector to solve this instance.
     * @return The cost of the solution.
     */
    public double solve(HeuristicSelector selector) {

        VRPHeuristic heuristic;
        double actual = CalculateDistance(this);
        double improve;
        int some = 1;
        int coin;
        coin = 10;
        //while(some == 1  ){
        while (some == 1 || coin > 1) {
            heuristic = null;
            try {
                /*
                 * In this case the featuere and heuristic manager are implemented by this class.
                 */
                heuristic = (VRPHeuristic) selector.getHeuristic(this, this);
                // System.out.println(" " + heuristic + " coin = " + coin);
            } catch (NoSuchFeatureException | NoSuchHeuristicException e) {
                System.out.println(e);
                System.out.println("The system will halt.");
                System.exit(1);
            }
//           // setB.add(heuristic.getNextElement(this));
            some = heuristic.getNextElement(this);
            // System.out.println(" " + some);
            improve = CalculateDistance(this);
            if (improve < actual) {
                coin = 10;
                actual = improve;
            } else {
                coin -= 1;
            }
        }

        return CalculateDistance(this); // +  ( 10  * (  (1  + this.getCustomers().size())  /( 1 + this.getAddedCustomers().size()  )) ) ;  
    }

    @Override
    /**
     * NOTE: Features need to be normalized in the range [0, 1].
     */
    public double getValueOfFeature(String featureName) throws NoSuchFeatureException {
        switch (featureName) {

            case "CUS":
                return this.addedCustomers.size() / 100;// + 1/this.getAddedCustomers().size() + 1;
            case "GAP":
                return (MaxDistance(this) - CalculateDistance(this)) / MaxDistance(this);
            case "VEH":
                return this.routes.size();
            case "DIF":
                return (CalculateDistance(this)) / MaxDistance(this);
            case "DIS":
                return CalculateDistance(this);
            case "BED":
                return getBigEdges(this);
            case "BRO":
                return getBigRoutes(this);
            case "CLOSE":
                return getCloseness(this);
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
            case "NNH":
                return new Heuristic_NNH();
            case "I1":
                return new Heuristic_I1();
            case "CWDT":
                return new Heuristic_CWDT();
            case "PARTR":
                return new Heuristic_PartialReset();
            case "RANR":
                return new Heuristic_RandomReset();
            case "COMR":
                return new Heuristic_CompleteReset();
            case "2OPT":
                return new Heuristic_2OPT();
            case "REL":
                return new Heuristic_Relocate();
            case "RELINTRA":
                return new Heuristic_RelocateIntraRoute();
            case "EXCHANGE":
                return new Heuristic_Exchange();
            case "SELR":
                return new Heuristic_SelectiveReset();
            case "REMAKE":
                return new Heuristic_SelectiveReMake();
            default:
                throw new NoSuchHeuristicException(heuristicName);
        }
    }

    public static double getBigRoutes(VehicleRoutingProblem problem) {
        //int x = problem.getRoutes().size();
        List<Route> routes = problem.getRoutes();
        int x = routes.size();
        double[][] distances = new double[x][3];
        double totalDistance = 0;
        double averageDistance = 0;

        for (int i = 0; i < x; i++) {
            Route routeX = routes.get(i);
            double routeDistance = 0;
            List<Edge> edges = routeX.getEdges();
            int cantE = edges.size();
            double maxEdge = 0;
            for (int d = 0; d < cantE; d++) {
                double disEdge = edges.get(d).getDistance();
                if (maxEdge < disEdge) {
                    maxEdge = disEdge;
                }
                routeDistance += disEdge;
            }
            double averageEdgeDist = routeDistance / cantE;

            distances[i][0] = routeDistance;
            distances[i][1] = maxEdge;
            distances[i][2] = averageEdgeDist;
            totalDistance += routeDistance;
        }

        averageDistance = totalDistance / x;

        List<Integer> rules;
        rules = new ArrayList<>(x);

        //rules.clear();
        int[] rutesToReset = new int[x];
        for (int y = 0; y < x; y++) {
            if (distances[y][0] > averageDistance) {
                rules.add(y);
            }
        }

        return rules.size();
    }

    public static double getBigEdges(VehicleRoutingProblem problem) {
        //int x = problem.getRoutes().size();
        List<Route> routes = problem.getRoutes();
        int x = routes.size();
        double[][] distances = new double[x][3];
        double totalDistance = 0;
        double averageDistance = 0;

        for (int i = 0; i < x; i++) {
            Route routeX = routes.get(i);
            double routeDistance = 0;
            List<Edge> edges = routeX.getEdges();
            int cantE = edges.size();
            double maxEdge = 0;
            for (int d = 0; d < cantE; d++) {
                double disEdge = edges.get(d).getDistance();
                if (maxEdge < disEdge) {
                    maxEdge = disEdge;
                }
                routeDistance += disEdge;
            }
            double averageEdgeDist = routeDistance / cantE;

            distances[i][0] = routeDistance;
            distances[i][1] = maxEdge;
            distances[i][2] = averageEdgeDist;
            totalDistance += routeDistance;
        }

        averageDistance = totalDistance / x;

        List<Integer> rules;
        rules = new ArrayList<>(x);

        for (int y = 0; y < x; y++) {
            Route routeX = routes.get(y);
            List<Edge> edgs = routeX.getEdges();

            for (int xx = 0; xx < edgs.size(); xx++) {
                Edge ed = edgs.get(xx);
                if (ed.getDistance() > (distances[y][2] + 2)) {
                    rules.add(xx);
                }
            }

        }

        return rules.size();
    }

    public static double getCloseness(VehicleRoutingProblem problem) {

        List<Route> routes = problem.getRoutes();
        int x = routes.size();
        double[][] distances = new double[x][3];
        double totalDistance = 0;
        double averageDistance = 0;
        double routeDistance = 0;
          double averageEdgeDist = 0;
        for (int i = 0; i < x; i++) {
            Route routeX = routes.get(i);

            List<Edge> edges = routeX.getEdges();
            int cantE = edges.size();
            double maxEdge = 0;
            for (int d = 0; d < cantE; d++) {

                Edge edge = edges.get(d);

                routeDistance += edge.getWaitingTime();
            }
            averageEdgeDist = routeDistance / cantE;

        }

       // return routeDistance;
        return averageEdgeDist;
    }

}
