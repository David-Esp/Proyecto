package vrp.heuristics;

/**
 * Provides the methods to create and use weighted elements (pairs of identifiers and a double
 * value given as weight).
 * <p>
 * @author José Carlos Ortiz Bayliss (jcobayliss@gmail.com)
 * @version 1.0
 */
public class WeightedElement implements Comparable<WeightedElement> {

    private final int id;
    private double weight;
    
    /**
     * Creates a new instance of <code>WeightedElement</code>.
     * <p>
     * @param id The identifier of the element within the CSP instance.
     * @param weight The weight of the element with the identifier provided.
     */
    public WeightedElement(int id, double weight) {
        this.id = id;
        this.weight = weight;
    }

    /**
     * Returns the identifier of this weighted element.
     * <p>
     * @return The identifier of this weighted element.
     */
    public int getId() {
        return id;
    }
    
    /**
     * Returns the weight of this weighted element.
     * <p>
     * @return The weight of this weighted element.
     */
    public double getWeight() {
        return weight;
    }
    
    /**
     * Sets the weight of this weighted element.
     * <p>
     * @param weight The weight of this weighted element.
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }
    
    @Override
    public int compareTo(WeightedElement weightedElement) {        
        if (this.getWeight() > weightedElement.getWeight()) {
            return 1;
        } else if (getWeight() == weightedElement.getWeight()) {
            return 0;
        } else {
            return -1;
        }
    }
    
    /**
     * Returns the string representation of this weighted element.
     * <p>
     * @return The string representation of this weighted element.
     */
    public String toString() {
        return "N" + id + " = " + weight;
    }
    
}
