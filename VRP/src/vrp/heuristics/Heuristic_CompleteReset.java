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
public class Heuristic_CompleteReset extends VRPHeuristic{

    @Override
    public int getNextElement(VehicleRoutingProblem problem) {
         
        
        problem.getCustomers().addAll(problem.getAddedCustomers());
        problem.getAddedCustomers().clear();
        problem.getEdges().clear();
        problem.getRoutes().clear();
 
        return 1;
        
        
        
    }

    @Override
    public String toString() {
        return "Complete Reset Heuristic";
    }
    
}
