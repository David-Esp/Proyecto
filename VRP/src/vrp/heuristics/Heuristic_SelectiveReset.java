/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vrp.heuristics;

import java.util.ArrayList;
import java.util.List;
import vrp.Problem.Customer;
import vrp.Problem.Edge;
import vrp.Problem.Route;
import vrp.Problem.VehicleRoutingProblem;

/**
 *
 * @author David
 */
public class Heuristic_SelectiveReset extends VRPHeuristic{

     @Override
    public int getNextElement(VehicleRoutingProblem problem) {
        
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
        
        if(!rules.isEmpty()){
            for(int c = rules.size() - 1; c >=0; c--){
                int index = rules.get(c);
                Route route = routes.get(index);
                List<Customer> customers = problem.getCustomers();
                 
                for(int cd = 1; cd<=route.getCustomers().size()-2; cd++){
                    customers.add(route.getCustomers().get(cd));
                    problem.getAddedCustomers().remove(route.getCustomers().get(cd));

                }
                
                routes.remove(index);
                 
            }
        }
        
       /* 
        double rnd = Math.random() * x ; 
        //System.out.println("Cantidad de rutas a borrar  = " + rnd);
         
        for (int i = x; i > rnd; i--){
            List<Route> routes = problem.getRoutes();
            Route ruta = routes.get(i-1);
            List<Customer> customers = problem.getCustomers();
            
            for(int c = 1; c<=ruta.getCustomers().size()-2; c++){
                customers.add(ruta.getCustomers().get(c));
                problem.getAddedCustomers().remove(ruta.getCustomers().get(c));
                
            }
            
            routes.remove(i-1);     
        }
        */
        
        
        return 0;
        
        
        
    }

    @Override
    public String toString() {
        return "SELR";
    }
    
    
}
