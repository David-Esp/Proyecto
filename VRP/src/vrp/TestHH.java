package vrp;

import HeuristicSelectors.VectorHeuristicSelector.VectorHeuristicSelector;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import vrp.Problem.Customer;
import vrp.Problem.Edge;
import vrp.Problem.Route;
import vrp.Problem.VehicleRoutingProblem;
import vrp.Solvers.Problem_Analizer;
import vrp.Solvers.Solution_Tester;
import vrp.heuristics.Heuristic_2OPT;
import vrp.heuristics.Heuristic_I1;
import vrp.heuristics.Heuristic_NNH;
import vrp.heuristics.Heuristic_Relocate;
import vrp.heuristics.Heuristic_RelocateIntraRoute;

/**
 * Tests the functionality of the minimum difference problem (a toy problem) and
 * the RaHHD Framework.
 * <p>
 * @author David
 */
public class TestHH {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        VectorHeuristicSelector selector;

        /*
         * Uncomment to load a saved heuristic selector from a file.
         */
        selector = new VectorHeuristicSelector("selector.xml");

        /*
         * Test the four available methods on a set of randomly generated instances.
         */
 
        Solution_Tester tester = new Solution_Tester();
       
         
        StringBuilder string; 
        Heuristic_RelocateIntraRoute relocateIntra = new Heuristic_RelocateIntraRoute();
        Heuristic_Relocate relocate =  new Heuristic_Relocate();
        Heuristic_2OPT opt = new Heuristic_2OPT();
        Heuristic_NNH nearestN = new Heuristic_NNH();
        Heuristic_I1 i1 = new Heuristic_I1(); 
        Problem_Analizer pAnal = new Problem_Analizer();
        VehicleRoutingProblem problem;
        problem = new VehicleRoutingProblem("Instances/solomon_100/RC101.txt"); 
         while (i1.getNextElement(problem) == 1){}
            string = new StringBuilder();
          //  double some = CalculateD
        
        
       
        
          System.out.println("GAP = " +  CalculateDistance(problem)    + " VEH = " 
                + (double) problem.getRoutes().size()/25 + " Rules = " + getSomething(problem)  + " BigEdges = " + getBigEdges(problem)   );      
             System.out.println(string.toString().trim());
            
            
            
            //string.append(problem.solve(selector));
         //  while(opt.getNextElement(problem) ==1){}
           //while(relocate.getNextElement(problem) ==1){}
          string.append(problem.solve(selector)); 
        
        System.out.println("GAP = " +  CalculateDistance(problem)    + " VEH = " 
                + (double) problem.getRoutes().size()/25 + " Rules = " + getSomething(problem)  + " BigEdges = " + getBigEdges(problem)   );      
             System.out.println(string.toString().trim());
        
        while(relocate.getNextElement(problem) ==1){}
           
           
        System.out.println("GAP = " +  CalculateDistance(problem)    + " VEH = " 
                + (double) problem.getRoutes().size()/25 + " Rules = " + getSomething(problem)  + " BigEdges = " + getBigEdges(problem)   );      
             System.out.println(string.toString().trim());
           while(opt.getNextElement(problem) ==1){}
           
      
       System.out.println("GAP = " +  CalculateDistance(problem)    + " VEH = " 
                + (double) problem.getRoutes().size()/25 + " Rules = " + getSomething(problem)  + " BigEdges = " + getBigEdges(problem)   );      
             System.out.println(string.toString().trim()); 
        
               while(relocateIntra.getNextElement(problem) ==1){}
           
      
       System.out.println("GAP = " +  CalculateDistance(problem)    + " VEH = " 
                + (double) problem.getRoutes().size()/25 + " Rules = " + getSomething(problem)  + " BigEdges = " + getBigEdges(problem)   );      
             System.out.println(string.toString().trim()); 
 
            

            if (tester.solution(problem)) {
              System.out.println( problem.toString() );
              System.out.println(pAnal.solution(problem));
                //  imprimirGraficas(problem);      
            } else {
                System.out.println("Something wrong");
            }

        

    }
 
        /**
     *
     * @param problem
     * @return
     */
    public static double CalculateDistance(VehicleRoutingProblem problem) {
        
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
       while (iteratorCustomer.hasNext()){
                unroutedCustomer = iteratorCustomer.next();
                distanceUnroutedCustomers += getDistanceFromTo(unroutedCustomer, problem.getDepot());
        }
         
        totalDistance += distanceUnroutedCustomers;
       
        
        return totalDistance;
    }
   
    /**
     *
     * @param ruta
     * @return
     */
    public static double CalculateRouteDistance(Route ruta){
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
    public static double MaxDistance(VehicleRoutingProblem problem){
            List<Customer> customersUnrouted =  problem.getCustomers();
            List<Customer> cutomersRouted = problem.getAddedCustomers();
            Customer customerRouted;
            Customer customerUnrouted;
            ListIterator<Customer> iteratorCustomerUnrouted;
            ListIterator<Customer> iteratorCustomerRouted;
            iteratorCustomerUnrouted = customersUnrouted.listIterator();
            iteratorCustomerRouted = cutomersRouted.listIterator();
            
            double distance = 0;
            while (iteratorCustomerUnrouted.hasNext()){
                customerUnrouted = iteratorCustomerUnrouted.next();
                distance += getDistanceFromTo(customerUnrouted, problem.getDepot());
            }
            
            while (iteratorCustomerRouted.hasNext()){
                customerRouted = iteratorCustomerRouted.next();
                distance += getDistanceFromTo(customerRouted, problem.getDepot());
            }
            
            return distance;
    }
    
        public static double getDistanceFromTo(Customer customerOrigin, Customer customerDestiny) {

        double xCoord = Math.abs(customerDestiny.getxCoord() - customerOrigin.getxCoord());
        double yCoord = Math.abs(customerDestiny.getyCoord() - customerOrigin.getyCoord());
        double distance = Math.sqrt((xCoord * xCoord) + (yCoord * yCoord));

        return distance;

    }
        
      public static double getSomething(VehicleRoutingProblem problem){
               //int x = problem.getRoutes().size();
        List<Route> routes = problem.getRoutes();
        int x = routes.size();
        double[][] distances = new double[x][3];
        double totalDistance = 0 ;
        double averageDistance = 0;
        
        for(int i = 0; i<x; i++){
            Route routeX = routes.get(i);
            double routeDistance = 0;
            List<Edge> edges = routeX.getEdges();
            int cantE = edges.size();
            double maxEdge = 0;
            for(int d = 0 ; d < cantE ; d++ ){
                double disEdge = edges.get(d).getDistance();
                if(maxEdge < disEdge){
                    maxEdge = disEdge;
                }
                routeDistance += disEdge;
            }
            double averageEdgeDist = routeDistance/cantE;
            
            distances[i][0] = routeDistance;
            distances[i][1] = maxEdge;
            distances[i][2] = averageEdgeDist;
            totalDistance += routeDistance;
        }
        
        averageDistance = totalDistance/x;
        
        
        List<Integer> rules;
        rules = new ArrayList<>(x);
        
       //rules.clear();
        int[] rutesToReset = new int[x];
        for(int y = 0 ; y < x ; y++){
            if(distances[y][0] > averageDistance){
                rules.add(y);
            }
        }
        
      return rules.size();
      }

    public static double getBigEdges(VehicleRoutingProblem problem){
               //int x = problem.getRoutes().size();
        List<Route> routes = problem.getRoutes();
        int x = routes.size();
        double[][] distances = new double[x][3];
        double totalDistance = 0 ;
        double averageDistance = 0;
        
        for(int i = 0; i<x; i++){
            Route routeX = routes.get(i);
            double routeDistance = 0;
            List<Edge> edges = routeX.getEdges();
            int cantE = edges.size();
            double maxEdge = 0;
            for(int d = 0 ; d < cantE ; d++ ){
                double disEdge = edges.get(d).getDistance();
                if(maxEdge < disEdge){
                    maxEdge = disEdge;
                }
                routeDistance += disEdge;
            }
            double averageEdgeDist = routeDistance/cantE;
            
            distances[i][0] = routeDistance;
            distances[i][1] = maxEdge;
            distances[i][2] = averageEdgeDist;
            totalDistance += routeDistance;
        }
        
        averageDistance = totalDistance/x;
        
        
        List<Integer> rules;
        rules = new ArrayList<>(x);
        
        for(int y = 0 ; y < x ; y++){
         Route routeX = routes.get(y);
            List<Edge> edgs = routeX.getEdges();
        
            for(int xx = 0 ; xx < edgs.size(); xx++){
                Edge ed = edgs.get(xx);
                if( ed.getDistance() > (distances[y][2] + 5) ){
                    rules.add(xx);
                }
            }
            
        }
        
      return rules.size();
      }
      
}
