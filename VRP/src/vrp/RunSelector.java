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
import vrp.Problem.Edge;
import vrp.Problem.Route;
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
        StringBuilder string;
        nbInstances = 56;
        maxValue = 10;
        problems = new VehicleRoutingProblem[results.size()]; 
        random = new Random(12345); // The seed for the generation of the random instances to test the performance.
        Heuristic_NNH nearestN =  new Heuristic_NNH();
        Heuristic_I1 i1 = new Heuristic_I1();
        //System.out.println("2OPT, REL, SELECTOR");
        System.out.println("  SELECTOR");
        for (int i = 0; i < results.size(); i++) {
            problems[i] = new VehicleRoutingProblem("Instances/solomon_100/" +   results.get(i));
              while ( nearestN.getNextElement(problems[i]) == 1){}
             // problems2[i] = problems[i] ;
            string = new StringBuilder();
      //    string.append(problems2[i].solve(new Heuristic_2OPT()));//.append(", ");
      //     string.append(problems2[i].solve(new Heuristic_Relocate()));//.append(", "); 
           // string.append(problems[i].solve(new Rand())).append(", ");
//          problems2[i].solve(selector);

   
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
        featureNames = new String[]{    "BRO",   "BED" , "CLOSE"};   
        heuristicNames = new String[]{"RELINTRA", "REL"  , "2OPT" ,"EXCHANGE" ,"REMAKE" };
        evaluationFunction = new Evaluator(100,100, 10, random.nextLong());
        selectionOperator = new TournamentSelectionOperator(2, true, random.nextLong()); //True = Minimizing 
        heuristicSelector = ((VectorIndividual) VectorHeuristicSelectorFramework.run(featureNames, heuristicNames, 100, 100, 1, 0.2, selectionOperator, evaluationFunction, GeneticAlgorithmType.STEADY_STATE, true, random.nextLong())).getHeuristicSelector();
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
                Files.walk(Paths.get("Instances/solomon_100T")).forEach(filePath -> {
                    if (Files.isRegularFile(filePath)) { 
                        results.add(filePath.getFileName().toString()); 
                    }
                });
            } catch (IOException ex) {
                Logger.getLogger(VRP.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            Random random;
            problems = new VehicleRoutingProblem[results.size()];
           // problems = new VehicleRoutingProblem[26];
            random = new Random(seed);
            ///for (int i = 0; i < 26; i ++) {
             for (int i = 0; i < results.size(); i ++) {
                problems[i] = new VehicleRoutingProblem("Instances/solomon_100T/" +   results.get(i));
                 Heuristic_NNH nearestN =  new Heuristic_NNH();
                 Heuristic_I1 i1 = new Heuristic_I1();
                 while ( nearestN.getNextElement(problems[i]) == 1){}
                
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
                cost += (problem.solve(featureBasedIndividual.getHeuristicSelector())    +    (getBigEdges(problem) * 10)   ) ;
            }
            
            return cost;
        }
    
    public double getBigEdges(VehicleRoutingProblem problem){
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
                if (ed.getDistance() > (distances[y][2] + 5)) {
                    rules.add(xx);
                }
            }

        }

        return rules.size();
    }     
        
    }
    
   
    
}
