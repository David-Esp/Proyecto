/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vrp.heuristics;

import RaHHD.Heuristic;

import vrp.Problem.VehicleRoutingProblem;
/**
 *
 * @author David
 */
public abstract class VRPHeuristic  extends Heuristic {
   
    /**
     * Returns the next element to be removed from the initial set in the problem provided.
     * <p>
     * @param problem A problem to be solved.
     * @return The next element to be removed from the initial set in the problem provided.
     */
    public abstract int getNextElement(VehicleRoutingProblem problem);
    
    
}

 