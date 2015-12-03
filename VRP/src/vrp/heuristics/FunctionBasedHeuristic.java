package vrp.heuristics;

import vrp.heuristics.Utils.Expression; 
import vrp.Problem.VehicleRoutingProblem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Provides the methods to create and use function-based variable ordering heuristics.
 * <p>
 * @author José Carlos Ortiz Bayliss (jcobayliss@gmail.com)
 * @version 1.0
 */
public class FunctionBasedHeuristic extends VRPHeuristic {
    
    private final Expression expression;
    
    /**
     * Creates a new instance of <code>FunctionBasedHeuristic</code>.
     * <p>
     * @param string The string that represents the function to initialize this heuristic.
     */
    public FunctionBasedHeuristic(String string) {
        expression = new Expression(string);
    }
    
    @Override
    public int getNextElement(VehicleRoutingProblem problem) {                
        List<WeightedElement> weightedVariables;     
        if( problem.getCustomers().isEmpty()){
            return -1;
        }
        weightedVariables = new ArrayList(problem.getCustomers().size());
        //weightedVariables = new ArrayList(problem.getSequenceA().size());        
        
        for (int i = 0; i < problem.getCustomers().size(); i++) {        
            //expression.set("x", Math.abs(problem.getSequenceA().get(i) - problem.getAbsoluteDifference()));         
            expression.set("x", problem.getCustomers().get(i).getTimeWindowStart());
            weightedVariables.add(new WeightedElement(i, expression.evaluate()));
        }
        //System.out.println(weightedVariables);
        /*
         * Selects the next variable according to the given criterion.
         */        
        Collections.sort(weightedVariables, Collections.reverseOrder());
        System.out.println("Customer - " + weightedVariables.get(0).getId());
        return problem.getCustomers().remove(weightedVariables.get(0).getId()).getNumber();
        
//        return problem.getSequenceA().remove(weightedVariables.get(0).getId());        
    }
    
    @Override
    public String toString() {
        return "FH_" + expression.toString();
    }
    
}
