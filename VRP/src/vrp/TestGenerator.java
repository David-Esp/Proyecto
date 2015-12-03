package vrp;


import Frameworks.EvolutionaryFrameworks.EvaluationFunction;
import Frameworks.EvolutionaryFrameworks.GeneticAlgorithmFramework.VectorHeuristicSelectorFramework;
import Frameworks.EvolutionaryFrameworks.GeneticAlgorithmFramework.VectorIndividual;
import Frameworks.EvolutionaryFrameworks.GeneticAlgorithmType;
import Frameworks.EvolutionaryFrameworks.GeneticProgrammingFramework.ComponentHeuristicGenerationFramework;
import Frameworks.EvolutionaryFrameworks.GeneticProgrammingFramework.ComponentIndividual;
import Frameworks.EvolutionaryFrameworks.Individual;
import Frameworks.EvolutionaryFrameworks.SelectionOperator;
import Frameworks.EvolutionaryFrameworks.TournamentSelectionOperator;
import HeuristicSelectors.VectorHeuristicSelector.VectorHeuristicSelector; 
import RaHHD.HeuristicSelector; 
import java.util.Random;

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
                
        /*
         * Test the four available methods on a set of randomly generated instances.
         */
        int nbInstances, maxValue;
        Random random;        
        MinDifferenceProblem[] problems;
        StringBuilder string;
        nbInstances = 50;
        maxValue = 10;
        problems = new MinDifferenceProblem[nbInstances];
        random = new Random(12345); // The seed for the generation of the random instances to test the performance.
        System.out.println("MINV, MAXV, RAND, SELECTOR, H?");
        for (int i = 0; i < nbInstances; i++) {
            problems[i] = new MinDifferenceProblem(100, maxValue, random.nextLong());
            string = new StringBuilder();
            string.append(problems[i].solve(new MinValue())).append(", ");
            string.append(problems[i].solve(new MaxValue())).append(", ");
            string.append(problems[i].solve(new Rand())).append(", ");
            string.append(problems[i].solve(selector)).append(", ");
            string.append(problems[i].solve(new FunctionBasedHeuristic("-(-(/(min(x, x), -(x, 0.21975656707919722)), +(*(x, 0.5399358871597774), *(0.37953859333760953, x))), -(/(max(x, 0.6990468881936087), *(x, 0.6005735928166785)), -(max(x, 0.6571470566821448), -(x, 0.819032940416928))))\n" +
"")));
            System.out.println(string.toString().trim());
        }
        
        System.out.println(generateHeuristic(501434)); // The seed for replication of the evolutionary process.
        
        
        
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
        featureNames = new String[]{"DIF"};        
        heuristicNames = new String[]{"MINV", "MAXV"};
        evaluationFunction = new Evaluator(100, 100, 10, random.nextLong());
        selectionOperator = new TournamentSelectionOperator(2, true, random.nextLong());
        heuristicSelector = ((VectorIndividual) VectorHeuristicSelectorFramework.run(featureNames, heuristicNames, 100, 500, 1.0, 0.1, selectionOperator, evaluationFunction, GeneticAlgorithmType.STEADY_STATE, true, random.nextLong())).getHeuristicSelector();
        return ((VectorHeuristicSelector) heuristicSelector);
    }
    
    public static String generateHeuristic(long seed) {
        Random random;
        String[] componentNames;
        SelectionOperator selectionOperator;
        EvaluationFunction evaluationFunction;        
        random = new Random(seed);
        VectorIndividual.setRandomNumberGenerator(random.nextLong());             
        componentNames = new String[]{"x"};
        evaluationFunction = new EvaluatorGen(100, 100, 10, random.nextLong());
        selectionOperator = new TournamentSelectionOperator(2, true, random.nextLong());          
        return ((ComponentIndividual) ComponentHeuristicGenerationFramework.run(componentNames, 10, 50, 1.0, 0.1, selectionOperator, evaluationFunction, GeneticAlgorithmType.STEADY_STATE, true, random.nextLong())).getExpression().toString();        
    }
    
    /**
     * Provides a class to evaluate the quality of chromosome representing a vector-based
     * hyper-heuristic on a set of instances.
     */
    private static class EvaluatorGen implements EvaluationFunction {
    
        private final MinDifferenceProblem[] problems;
        
        /**
         * Creates a new instance of <code>Evaluator</code>.
         * <p>
         * @param nbInstances The number of instances in this evaluator.
         * @param n The number of elements in the instances generated.
         * @param maxValue The max value an element can get.
         * @param seed The seed to initialize the random number generator.
         */
        public EvaluatorGen(int nbInstances, int n, int maxValue, long seed) {
            Random random;
            problems = new MinDifferenceProblem[nbInstances];
            random = new Random(seed);
            for (int i = 0; i < nbInstances; i++) {
                problems[i] = new MinDifferenceProblem(n, maxValue, random.nextLong());
            }
        }

        @Override
        public double getEvaluation(Individual individual) {
            double cost;
            ComponentIndividual componentIndividual;
            componentIndividual = (ComponentIndividual) individual;
            cost = 0;
            for (MinDifferenceProblem problem : problems) {                
                cost += problem.solve(new FunctionBasedHeuristic(componentIndividual.getExpression().toString()));
                //System.out.println(cost);
            }
            return cost;
        }
        
    }
    
    /**
     * Provides a class to evaluate the quality of chromosome representing a vector-based
     * hyper-heuristic on a set of instances.
     */
    private static class Evaluator implements EvaluationFunction {
    
        private final MinDifferenceProblem[] problems;
        
        /**
         * Creates a new instance of <code>Evaluator</code>.
         * <p>
         * @param nbInstances The number of instances in this evaluator.
         * @param n The number of elements in the instances generated.
         * @param maxValue The max value an element can get.
         * @param seed The seed to initialize the random number generator.
         */
        public Evaluator(int nbInstances, int n, int maxValue, long seed) {
            Random random;
            problems = new MinDifferenceProblem[nbInstances];
            random = new Random(seed);
            for (int i = 0; i < nbInstances; i++) {
                problems[i] = new MinDifferenceProblem(n, maxValue, random.nextLong());
            }
        }

        @Override
        public double getEvaluation(Individual individual) {
            double cost;
            VectorIndividual featureBasedIndividual;
            featureBasedIndividual = (VectorIndividual) individual;
            cost = 0;
            for (MinDifferenceProblem problem : problems) {
                cost += problem.solve(featureBasedIndividual.getHeuristicSelector());
            }
            return cost;
        }
        
    }
    
}
