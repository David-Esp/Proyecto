package vrp.heuristics.constructive;

import vrp.Problem.VehicleRoutingProblem;
import vrp.heuristics.Utils.Statistical;

/**
 * Provides the methods to estimate features to characterize VRP instances and their variables.
 * <p>
 * @author David
 * @version 1.0
 */
public class VRPFeatureManager {

    private final double[] constraintWeights;
    private final VehicleRoutingProblem vrp;
    
    
  
    /**
     * Creates a new instance of <code>CSPFeatureManager</code>.
     * <p>
     * @param problem The VRP instance to be analyzed.
     */
    public VRPFeatureManager(VehicleRoutingProblem problem) {
        this.vrp = problem;
        constraintWeights = new double[problem.getCustomers().size()]; 
        for (int i = 0; i < constraintWeights.length; i++) {
            constraintWeights[i] = 1;
        }
    }
    
    
    
     /**
     * ---------------------------------------------------------------------------------------------
     * FEATURES OF VARIABLES
     * ---------------------------------------------------------------------------------------------
     */

    public double getDemand(int customerId){
        double demand;
        demand  =  vrp.getCustomers().get(customerId).getDemand();
        return demand;
    }
    
    public double getTimeWindowStart(int customerId){
        double timeWindowStart;
        timeWindowStart =  vrp.getCustomers().get(customerId).getTimeWindowStart();
        return timeWindowStart; 
    }
    
     
        
}
