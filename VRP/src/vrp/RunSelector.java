package vrp;


import Frameworks.EvolutionaryFrameworks.EvaluationFunction;
import Frameworks.EvolutionaryFrameworks.GeneticAlgorithmFramework.VectorHeuristicSelectorFramework;
import Frameworks.EvolutionaryFrameworks.GeneticAlgorithmFramework.VectorIndividual;
import Frameworks.EvolutionaryFrameworks.GeneticAlgorithmType;
import Frameworks.EvolutionaryFrameworks.Individual;
import Frameworks.EvolutionaryFrameworks.SelectionOperator;
import Frameworks.EvolutionaryFrameworks.TournamentSelectionOperator;
import HeuristicSelectors.VectorHeuristicSelector.VectorHeuristicSelector;
import RaHHD.HeuristicSelector; 
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import vrp.Problem.VehicleRoutingProblem;
import vrp.Solvers.Solution_Tester;
import vrp.VRP;
import vrp.heuristics.Heuristic_2OPT;
import vrp.heuristics.Heuristic_I1;
import vrp.heuristics.Heuristic_NNH;
import vrp.heuristics.Heuristic_Relocate;

/**
 * Tests the functionality of the minimum difference problem (a toy problem) and the RaHHD
 * Framework.
 * <p>
 * @author David
 */
public class RunSelector {
    
    /**
     *
     * @param args
     */
    public static void main(String[] args) {        
        VectorHeuristicSelector selector;        
        /*
         * Uncomment to produce a new heuristic selector and save it to a file.
         */  
        selector = generateHeuristicSelector(501434); // The seed for replication of the evolutionary process.
        selector.save("selector.xml");
        /*
         * Uncomment to load a saved heuristic selector from a file.
         */ 
        selector = new VectorHeuristicSelector("selector.xml");
               
        /*
         * Test the four available methods on a set of randomly generated instances.
        */
        
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
        
        Solution_Tester tester = new Solution_Tester();
        int nbInstances, maxValue;
        Random random;        
        VehicleRoutingProblem[] problems;
        VehicleRoutingProblem[] problems2;
        StringBuilder string;
        nbInstances = 56;
        maxValue = 10;
        problems = new VehicleRoutingProblem[results.size()];
        problems2 = new VehicleRoutingProblem[results.size()];
        random = new Random(12345); // The seed for the generation of the random instances to test the performance.
        Heuristic_NNH nearestN =  new Heuristic_NNH();
        Heuristic_I1 i1 = new Heuristic_I1();
        //System.out.println("2OPT, REL, SELECTOR");
        System.out.println("  SELECTOR");
        for (int i = 0; i < results.size(); i++) {
            problems[i] = new VehicleRoutingProblem("Instances/solomon_100/" +   results.get(i));
              while ( i1.getNextElement(problems[i]) == 1){}
             // problems2[i] = problems[i] ;
            string = new StringBuilder();
      //    string.append(problems2[i].solve(new Heuristic_2OPT()));//.append(", ");
      //     string.append(problems2[i].solve(new Heuristic_Relocate()));//.append(", "); 
           // string.append(problems[i].solve(new Rand())).append(", ");
//          problems2[i].solve(selector);

          
            
           
       /*     
            
        double actual = problems[i].solve(selector);
        double improve;
        int some = 1;
        int coin;
        coin = 10;
         //while(some == 1  ){
         while(  coin > 1  ){
    
//            
            improve = problems[i].solve(selector);
            if( improve < (actual - 0.001)){
                coin = 10;
                actual = improve;
            }else{
                coin = -1;
            }
        }
         */   
            string.append(problems[i].solve(selector));
            
            
            System.out.println(string.toString().trim());
             
            
            
             if(tester.solution(problems[i])) {
             //  System.out.println( problems2[i].toString() );
                
              //  imprimirGraficas(problem);      
            }else{
                 System.out.println("Something wrong");
             }
            
            
            
        }
         
    }
    
    /**
     * Generates a vector-based heuristic selector.
     * <p>
     * @param seed The seed to initialize the random number generator.
     * @return A vector-based heuristic selector.
     */
    public static VectorHeuristicSelector generateHeuristicSelector(long seed) { 
        Random random;
        String[] featureNames, heuristicNames;
        SelectionOperator selectionOperator;
        EvaluationFunction evaluationFunction;
        HeuristicSelector heuristicSelector;
        random = new Random(seed);
        VectorIndividual.setRandomNumberGenerator(random.nextLong());
        //featureNames = new String[]{"GAP", "DIF", "CUS", "VEH"}; 
        featureNames = new String[]{  "DIF", "BRO", "BED"};   
        heuristicNames = new String[]{"RELINTRA", "REL" , "2OPT"  };
        evaluationFunction = new Evaluator(100,100, 10, random.nextLong());
        selectionOperator = new TournamentSelectionOperator(4, true, random.nextLong()); //True = Minimizing 
        heuristicSelector = ((VectorIndividual) VectorHeuristicSelectorFramework.run(featureNames, heuristicNames, 150, 300, 1, 0.1, selectionOperator, evaluationFunction, GeneticAlgorithmType.STEADY_STATE, true, random.nextLong())).getHeuristicSelector();
        return ((VectorHeuristicSelector) heuristicSelector);
    }
    
    /**
     * Provides a class to evaluate the quality of chromosome representing a vector-based
     * hyper-heuristic on a set of instances.
     */
    private static class Evaluator implements EvaluationFunction {
    
        private final VehicleRoutingProblem[] problems;
        
        /**
         * Creates a new instance of <code>Evaluator</code>.
         * <p>
         * @param nbInstances The number of instances in this evaluator.
         * @param n The number of elements in the instances generated.
         * @param maxValue The max value an element can get.
         * @param seed The seed to initialize the random number generator.
         */
        public Evaluator(int nbInstances, int n, int maxValue, long seed) {
            
            List<String> results = new ArrayList<>();
            try {
                Files.walk(Paths.get("Instances/solomon_100")).forEach(filePath -> {
                    if (Files.isRegularFile(filePath)) { 
                        results.add(filePath.getFileName().toString()); 
                    }
                });
            } catch (IOException ex) {
                Logger.getLogger(VRP.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            Random random;
            problems = new VehicleRoutingProblem[results.size()];
            random = new Random(seed);
            for (int i = 0; i < results.size(); i++) {
            //for (String result:results){
                problems[i] = new VehicleRoutingProblem("Instances/solomon_100/" +   results.get(i));
                //Heuristic_NNH nearestN =  new Heuristic_NNH();
                 Heuristic_I1 i1 = new Heuristic_I1();
                 while ( i1.getNextElement(problems[i]) == 1){}
                
            } 
        }

        @Override
        public double getEvaluation(Individual individual) {
            double cost;
            VectorIndividual featureBasedIndividual;
            featureBasedIndividual = (VectorIndividual) individual;
            cost = 0; 
            for (VehicleRoutingProblem problem : problems) {
              //  System.out.println(" cost " + cost);
                cost += problem.solve(featureBasedIndividual.getHeuristicSelector());
            }
            
            return cost;
        }
        
    }
    
}
