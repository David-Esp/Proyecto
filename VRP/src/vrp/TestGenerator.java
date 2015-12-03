package vrp;


import Frameworks.EvolutionaryFrameworks.EvaluationFunction;
import Frameworks.EvolutionaryFrameworks.GeneticAlgorithmFramework.VectorIndividual;
import Frameworks.EvolutionaryFrameworks.GeneticAlgorithmType;
import Frameworks.EvolutionaryFrameworks.GeneticProgrammingFramework.ComponentHeuristicGenerationFramework;
import Frameworks.EvolutionaryFrameworks.GeneticProgrammingFramework.ComponentIndividual;
import Frameworks.EvolutionaryFrameworks.Individual;
import Frameworks.EvolutionaryFrameworks.SelectionOperator;
import Frameworks.EvolutionaryFrameworks.TournamentSelectionOperator;
import HeuristicSelectors.VectorHeuristicSelector.VectorHeuristicSelector; 
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import vrp.Problem.VehicleRoutingProblem;
import vrp.heuristics.FunctionBasedHeuristic;
import vrp.heuristics.Heuristic_I1;
import vrp.heuristics.Heuristic_NNH;

/**
 * Tests the functionality of the minimum difference problem (a toy problem) and the RaHHD
 * Framework.
 * <p>
 * @author José Carlos Ortiz Bayliss (jcobayliss@gmail.com)
 * @version 1.0
 */
public class TestGenerator {
    
    public static void main(String[] args) {        
        VectorHeuristicSelector selector;        
        /*
         * Uncomment to produce a new heuristic selector and save it to a file.
         */
        //selector = generateHeuristicSelector(501434); // The seed for replication of the evolutionary process.
        //selector.save("selector.xml");
        /*
         * Uncomment to load a saved heuristic selector from a file.
         */
        selector = new VectorHeuristicSelector("selector.xml");
            
        List<String> results = new ArrayList<>();
            try {
                Files.walk(Paths.get("Instances/solomon_100")).forEach(filePath -> {
                    if (Files.isRegularFile(filePath)) {

                        results.add(filePath.getFileName().toString());
                  //  System.out.println(filePath); 

                    }
                });
            } catch (IOException ex) {
                Logger.getLogger(VRP.class.getName()).log(Level.SEVERE, null, ex);
            }
        /*
         * Test the four available methods on a set of randomly generated instances.
         */
        int nbInstances, maxValue;
        Random random;        
        VehicleRoutingProblem[] problems;
        StringBuilder string;
        nbInstances = 50;
        maxValue = 10;
        problems = new VehicleRoutingProblem[results.size()]; 
        //problems = new MinDifferenceProblem[nbInstances];
        random = new Random(12345); // The seed for the generation of the random instances to test the performance.
        System.out.println(" SELECTOR, H?");
        for (int i = 0; i <  results.size() ; i++) {
           // problems[i] = new MinDifferenceProblem(100, maxValue, random.nextLong());
            problems[i] = new VehicleRoutingProblem("Instances/solomon_100/" +   results.get(i));
            string = new StringBuilder(); 
            string.append(problems[i].solve(selector)).append(", ");
            string.append(problems[i].solve(new FunctionBasedHeuristic(" x ")));
            System.out.println(string.toString().trim());
        }
        
        System.out.println(generateHeuristic(501434)); // The seed for replication of the evolutionary process.
        
        
        
    }
     
    public static String generateHeuristic(long seed) {
        Random random;
        String[] componentNames;
        SelectionOperator selectionOperator;
        EvaluationFunction evaluationFunction;        
        random = new Random(seed);
        VectorIndividual.setRandomNumberGenerator(random.nextLong());             
        componentNames = new String[]{"x"};
        evaluationFunction = new EvaluatorGen();
        selectionOperator = new TournamentSelectionOperator(2, true, random.nextLong());          
        return ((ComponentIndividual) ComponentHeuristicGenerationFramework.run(componentNames, 10, 50, 1.0, 0.1, selectionOperator, evaluationFunction, GeneticAlgorithmType.STEADY_STATE, true, random.nextLong())).getExpression().toString();        
    }
    
    /**
     * Provides a class to evaluate the quality of chromosome representing a vector-based
     * hyper-heuristic on a set of instances.
     */
    private static class EvaluatorGen implements EvaluationFunction {
        
        private final VehicleRoutingProblem[] problems; 
        
        /**
         * Creates a new instance of <code>EvaluatorGen</code>.
         *  
         */
        public EvaluatorGen( ) {
            
            
            List<String> results = new ArrayList<>();
            try {
                Files.walk(Paths.get("Instances/solomon_100T")).forEach(filePath -> {
                    if (Files.isRegularFile(filePath)) { 
                        results.add(filePath.getFileName().toString()); 
                    }
                });
            } catch (IOException ex) {
                Logger.getLogger(VRP.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            problems = new VehicleRoutingProblem[results.size()];
           
             for (int i = 0; i < results.size(); i ++) {
                problems[i] = new VehicleRoutingProblem("Instances/solomon_100T/" +   results.get(i));
                  
                
            } 
            
             
        }

        @Override
        public double getEvaluation(Individual individual) {
            double cost;
            ComponentIndividual componentIndividual;
            componentIndividual = (ComponentIndividual) individual;
            cost = 0;
            for (VehicleRoutingProblem problem : problems) {                
                cost += problem.solve(new FunctionBasedHeuristic(componentIndividual.getExpression().toString()));
                //System.out.println(cost);
            }
            return cost;
        }
        
    }
     
    
}
