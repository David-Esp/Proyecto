package vrp.heuristics.constructive;

import java.util.List;
import vrp.Problem.Customer;
import vrp.Problem.Route;
import vrp.Problem.VehicleRoutingProblem;

/**
 * Provides the methods to estimate features to characterize VRP instances and
 * their variables.
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
    public double getDemand(int customerId) {
        double demand;
        demand = vrp.getCustomers().get(customerId).getDemand();
        return demand;
    }

    public double getTimeWindowStart(int customerId) {
        double timeWindowStart;
        timeWindowStart = vrp.getCustomers().get(customerId).getTimeWindowStart();
        return timeWindowStart;
    }

    public double getClosenessToLast(VehicleRoutingProblem problem, int index) {
        List<Route> routes = problem.getRoutes();
        List<Customer> customers = problem.getCustomers();
        Customer candidateCustomer = customers.get(index);
        Customer depot = problem.getDepot();
        double closeness = 0;

        if (routes.isEmpty()) {
            closeness = getDistanceFromTo(depot, candidateCustomer);

        } else {
            Route lastRoute = routes.get(routes.size() - 1);
            Customer lastCustomer = lastRoute.getCustomers().get(lastRoute.getCustomers().size() - 1);

            closeness = getDistanceFromTo(lastCustomer, candidateCustomer);

        }
        return closeness;
    }

    private double getDistanceFromTo(Customer customerOrigin, Customer customerDestiny) {

        double xCoord = Math.abs(customerDestiny.getxCoord() - customerOrigin.getxCoord());
        double yCoord = Math.abs(customerDestiny.getyCoord() - customerOrigin.getyCoord());
        double distance = Math.sqrt((xCoord * xCoord) + (yCoord * yCoord));

        return distance;

    }
}
