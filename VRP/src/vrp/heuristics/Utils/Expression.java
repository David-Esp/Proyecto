package vrp.heuristics.Utils;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Provides the methods to create and handle mathematical expressions.
 * <p>
 * @author José Carlos Ortiz Bayliss (jcobayliss@gmail.com)
 * @version 1.0
 */
public class Expression {

    private final String label;
    private final Expression[] branches;
    private static ExpressionEvaluator evaluator = new ExpressionEvaluator();
        
    /**
     * Creates a new instance of <code>Expression</code>.
     * <p>     
     * @param string The string representation of the expression to create.
     */
    public Expression(String string) {
        int index;
        String[] operands;
        string = string.trim();
        /*
         * Removes unnecessary parenthesis.
         */        
        while(string.charAt(0) == '(') {
            if (string.charAt(string.length() - 1) == ')') {
                string  = string.substring(1, string.length() - 1);                
            } else {
                System.out.println("Unbalanced parenthesis.");
                System.out.println("The system will halt.");
                System.exit(1);
            }
        }
        index = string.indexOf("(");
        if (index > -1) {
            label = string.substring(0, index);
        } else {
            label = string;
        }
        branches = new Expression[evaluator.getNumberOfOperands(label)];        
        if (branches.length > 0) {
            string = string.substring(index + 1, string.length() - 1);
            operands = decomposeOperands(string, branches.length);                        
            for (int i = 0; i < branches.length; i++) {                
                branches[i] = new Expression(operands[i]);
            }
        }
    }                

    /**
     * Sets the value of a variable.
     * <p>
     * @param label The label of the variable.
     * @param value The value for the variable.
     */
    public void set(String label, double value) {
        evaluator.set(label, value);
    }
       
    /**
     * Evaluates this expression.
     * <p>
     * @return The evaluation of this expression.
     */
    public double evaluate() {
        double x =  evaluator.evaluate(this);
        if (Double.isNaN(x)) {
            System.out.println(this);
            System.out.println("p1 = " + evaluator.get("p1"));
            System.out.println("p2 = " + evaluator.get("p2"));
            System.out.println("k = " + evaluator.get("k"));
            System.out.println("d = " + evaluator.get("d"));
            System.out.println("c = " + evaluator.get("c"));
            System.out.println("wd = " + evaluator.get("wd"));
            System.out.println("\t = " + x);
            System.exit(1);
        }
        return x;        
     }        
    
    @Override
    /**
     * Clones this expression.
     * <p/>
     * @return A clone of this expression.
     */
    public Expression clone() {
        return new Expression(this);
    }

    /**
     * Returns the string representation of this expression.
     * <p>
     * @return The string representation of this expression.
     */
    public String toString() {
        StringBuilder string;
        string = new StringBuilder();
        string.append(label);
        if (branches.length != 0) {
            string.append("(");
            for (Expression node : branches) {
                string.append(node).append(", ");
            }
            string.delete(string.length() - 2, string.length());
            string.append(")");
        }
        return string.toString();
    }
    
    /**
     * Creates a new instance of <code>Expression</code> from an existing instance.
     * <p>
     * @param expression The expression that will be used to create the new instance.
     */
    private Expression(Expression expression) {
        this.label = expression.label;        
        branches = new Expression[expression.branches.length];
        for (int i = 0; i < branches.length; i++) {
            branches[i] = new Expression(expression.getBranches()[i]);
        }
    }
    
    /**
     * Creates a new instance of <code>Expression</code>.
     * <p>     
     * @param label The label of the root of this expression tree.
     */
    private Expression(String label, boolean unused) {        
        this.label = label;
        branches = new Expression[evaluator.getNumberOfOperands(label)];
    }
    
    /**
     * Decomposes a string into a given number of operands.
     * <p>
     * @param string The string where the operands will be extracted from.
     * @param numberOfOperands The number of operands to extract.
     * @return An array with the string representation of the operands.
     */
    private String[] decomposeOperands(String string, int numberOfOperands) {
        int operandCounter, openParenthesis;        
        StringBuilder text;
        String[] operands;        
        operands = new String[numberOfOperands];
        operandCounter = 0;
        openParenthesis = 0;        
        text = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            text.append(string.charAt(i));
            if (string.charAt(i) == '(') {
                openParenthesis++;
            }
            if (string.charAt(i) == ')') {
                openParenthesis--;                
            }
            if (string.charAt(i) == ',' && openParenthesis == 0) {                
                operands[operandCounter++] = text.substring(0, text.length() - 1).trim();
                text = new StringBuilder();
            }                                       
        }        
        operands[operandCounter++] = text.substring(0, text.length()).trim();                
        return operands;
    }            
    
    /**
     * Returns the label of the first operation or operand of this expression.
     * <p>
     * @return The label of the first operation or operand of this expression.
     */
    private String getLabel() {
        return label;
    }

    /**
     * Return the branches of the first operation or operand of this expression tree.
     * <p>
     * @return The branches of the first operation or operand of this expression tree.
     */
    private Expression[] getBranches() {
        return branches;
    }

    private static class ExpressionEvaluator {

        private final HashMap<String, Double> variables;
        private final String[] functions;

        /**
         * Creates a new instance of <code>ExpressionEvaluator</code>.
         */
        public ExpressionEvaluator() {
            functions = new String[]{"+", "-", "*", "/", "pow", "sqrt", "min", "max", "sin", "cos"};
            variables = new HashMap();
        }

        /**
         * Revises if the label provided corresponds to a terminal or not.
         * <p>
         * @param label The label of the element to revise.
         * @return <code>true</code> if and only if the label provided corresponds to a terminal,
         * otherwise the method returns <code>false</code>.
         */
        public boolean isTerminal(String label) {
            for (String function : functions) {
                if (function.equalsIgnoreCase(label)) {
                    return false;
                }
            }
            return true;
        }

        /**
         * Returns the number of operands for the function that matches the label provided. If the
         * label corresponds to a terminal, the method returns 0.
         * <p>
         * @param label The label of the function whose number of operants we want to retrieve.
         * @return The number of operands for the function that matches the label provided. If the
         * label corresponds to a terminal, the method returns 0.
         */
        public int getNumberOfOperands(String label) {
            switch (label) {
                case "sin":
                case "cos":
                case "sqrt":
                    return 1;
                case "+":
                case "-":
                case "*":
                case "/":
                case "pow":
                case "min":
                case "max":
                    return 2;
                default:
                    return 0;
            }
        }

        /**
         * Sets the value of a variable.
         * <p>
         * @param label The label of the variable.
         * @param value The value for the variable.
         */
        public void set(String label, double value) {
            if (Double.isInfinite(value)) {
                if (value > 0) {
                    value = Double.MAX_VALUE;
                } else {
                    value = -Double.MAX_VALUE;
                }
            }
            variables.put(label, value);            
        }
        
        /**
         * Returns the value of the variable.         
         * <p>
         * @param label The label of the variable.
         * @return The value of the variable.
         */
        public double get(String label) {
            return variables.get(label);
        }

        /**
         * Returns the evaluation of the expression tree provided as argument.
         * <p>
         * @param expressionTree The expression tree to evaluate.
         * @return The evaluation of the expression tree provided as argument.
         */
        public Double evaluate(Expression expressionTree) {
            return getEvaluation(expressionTree);
        }

        /**
         * Returns the evaluation of the expression tree provided as argument.
         * <p>
         * @param expression The expression tree to evaluate.
         * @return The evaluation of the expression tree provided as argument,
         */
        private double getEvaluation(Expression expression) {
            int i;
            double[] operands;
            Expression[] branches;
            branches = expression.getBranches();
            if (branches.length == 0) {
                if (variables.containsKey(expression.getLabel())) {
                    return variables.get(expression.getLabel());
                }
                try {
                    return Double.parseDouble(expression.getLabel());
                } catch (Exception e) {
                    System.out.println("Variable " + expression.getLabel() + " has not been assigned yet in expression \'" + expression + "\'. Zero is assumed.");
                    return 0;
                }
            } else {
                i = 0;
                operands = new double[branches.length];
                for (Expression branch : branches) {
                    operands[i++] = getEvaluation(branch);
                }
                return applyFunction(expression.getLabel(), operands);
            }
        }

        /**
         * Applies a function to the operands provided.
         * <p>
         * @param label The label of the function to apply.
         * @param operands The operands to be used by the function.
         * @return The result of the function.
         */
        private double applyFunction(String label, double[] operands) {
            double result;
            switch (label) {
                case "+":
                    result = operands[0] + operands[1];
                    break;
                case "-":
                    result = operands[0] - operands[1];
                    break;
                case "*":
                    result = operands[0] * operands[1];
                    break;
                case "/":
                    if (operands[1] == 0 || (Double.isInfinite(operands[0]) && Double.isInfinite(operands[1]))) {
                        result = 0;
                    } else {                        
                        result = operands[0] / operands[1];
                    }
                    break;
                case "pow":
                    if (operands[0] == 0) {
                        result = 0;
                    } else {
                        result = Math.pow(operands[0], Math.round(operands[1]));
                    }
                    break;
                case "sqrt":
                    result = Math.sqrt(Math.abs(operands[0]));
                    break;
                case "min":
                    result = Math.min(operands[0], operands[1]);
                    break;
                case "max":
                    result = Math.max(operands[0], operands[1]);
                    break;
                case "sin":
                    if (Double.isInfinite(operands[0])) {
                        if (operands[0] > 0) {
                            operands[0] = Double.MAX_VALUE;
                        } else {
                            operands[0] = -Double.MAX_VALUE;
                        }
                    }
                    result = Math.sin(operands[0]);
                    break;
                case "cos":
                    if (Double.isInfinite(operands[0])) {
                        if (operands[0] > 0) {
                            operands[0] = Double.MAX_VALUE;
                        } else {
                            operands[0] = -Double.MAX_VALUE;
                        }
                    }
                    result = Math.cos(operands[0]);
                    break;
                default:
                    result = Double.NaN;
                    System.out.println("The function \"" + label + "\" is not recognized by the system.");
                    System.out.println("The system will halt.");
                    System.exit(1);
            }
            if (Double.isInfinite(result)) {
                if (result > 0) {
                    result = Double.MAX_VALUE;
                } else {
                    result = -Double.MAX_VALUE;
                }
            }
            if (Double.isNaN(result)) {
                System.out.println(">> " + label + ": " + Arrays.toString(operands));
            }            
            return result;
        }
    }
}
