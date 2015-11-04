/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vrp.heuristics;

import java.util.List;
import vrp.Problem.Customer;
import vrp.Problem.Route;
import vrp.Problem.VehicleRoutingProblem;

/**
 *
 * @author David
 */
public class Heuristic_PartialReset extends VRPHeuristic {

    @Override
    public int getNextElement(VehicleRoutingProblem problem) {
        
        int x = problem.getRoutes().size();
        //double rnd = Math.random() * x ; 
       // System.out.println("Cantidad de rutas a borrar  = " + rnd);
        
        double half = x/2;
        
        
        for (int i = x; i>half; i--){
            List<Route> routes = problem.getRoutes();
            Route ruta = routes.get(i-1);
            List<Customer> customers = problem.getCustomers();
            
            for(int c = 1; c<=ruta.getCustomers().size()-2; c++){
                customers.add(ruta.getCustomers().get(c));
                problem.getAddedCustomers().remove(ruta.getCustomers().get(c));
                
            }
            
            routes.remove(i-1);     
        }
        
        
        
        return 0;
        
        
        
    }

    @Override
    public String toString() {
        return "PARTR";
    }
    
    
}
