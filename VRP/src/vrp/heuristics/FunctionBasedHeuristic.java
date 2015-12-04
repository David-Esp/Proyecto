package vrp.heuristics;

import vrp.heuristics.Utils.WeightedElement;
import vrp.heuristics.Utils.Expression;
import vrp.Problem.VehicleRoutingProblem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import vrp.Problem.Customer;
import vrp.Problem.Edge;
import vrp.Problem.Route;
import vrp.heuristics.constructive.VRPFeatureManager;

/**
 * Provides the methods to create and use function-based variable ordering
 * heuristics.
 * <p>
 * @author José Carlos Ortiz Bayliss (jcobayliss@gmail.com)
 * @version 1.0
 */
public class FunctionBasedHeuristic extends VRPHeuristic {

    private final Expression expression;

    /**
     * Creates a new instance of <code>FunctionBasedHeuristic</code>.
     * <p>
     * @param string The string that represents the function to initialize this
     * heuristic.
     */
    public FunctionBasedHeuristic(String string) {
        expression = new Expression(string);
    }

    @Override
    public int getNextElement(VehicleRoutingProblem problem) {
        List<WeightedElement> weightedVariables;
        if (problem.getCustomers().isEmpty()) {
            return -1;
        }
        VRPFeatureManager featureManager = new VRPFeatureManager(problem);
        
        weightedVariables = new ArrayList(problem.getCustomers().size());
        //weightedVariables = new ArrayList(problem.getSequenceA().size());        

        for (int i = 0; i < problem.getCustomers().size(); i++) {
            //expression.set("x", Math.abs(problem.getSequenceA().get(i) - problem.getAbsoluteDifference()));         
            expression.set("twStart", problem.getCustomers().get(i).getTimeWindowStart());
            expression.set("demand", problem.getCustomers().get(i).getDemand());
            expression.set("twEnd", problem.getCustomers().get(i).getTimeWindowEnd());
            expression.set("close", featureManager.getClosenessToLast(problem, i));  
            weightedVariables.add(new WeightedElement(i, expression.evaluate()));
        }
        //System.out.println(weightedVariables);
        /*
         * Selects the next variable according to the given criterion.
         */
        Collections.sort(weightedVariables, Collections.reverseOrder());
        //System.out.println("Customer - " + problem.getCustomers().get(weightedVariables.get(0).getId()).getNumber());
        insertCustomer(problem, weightedVariables.get(0).getId());
        //problem.getCustomers().remove(weightedVariables.get(0).getId()).getNumber()
        return 1;

//        return problem.getSequenceA().remove(weightedVariables.get(0).getId());        
    }

    @Override
    public String toString() {
        return "FH_" + expression.toString();
    }

    public int insertCustomer(VehicleRoutingProblem problem, int idCustomerCandidate) {

        int vehicleCapacity = problem.getCapacity();
        Customer depot = problem.getDepot();
        List<Customer> customers = problem.getCustomers();
        List<Customer> addedCustomers = problem.getAddedCustomers();
        List<Route> routes = problem.getRoutes();
        Customer candidateCustomer = customers.get(idCustomerCandidate);

        if (routes.isEmpty()) {
            //If there are no route, we add a new one
            Route route = new Route(depot, vehicleCapacity);
            //Add the new route to the list
            routes.add(route);
        } else {
            //If there´s a route, we take the last one added to the list
            Route lastRoute = routes.get(routes.size() - 1);
            Customer lastCustomer = lastRoute.getCustomers().get(lastRoute.getCustomers().size() - 1);
            List<Edge> lastEdgesList = lastRoute.getEdges();
            double eoSLastCustomer = 0;
            double distancia;

            if (lastEdgesList.isEmpty()) {
                distancia = getDistanceFromTo(lastCustomer, candidateCustomer);
            } else {
                Edge lastEdge = lastEdgesList.get(lastEdgesList.size() - 1);
                eoSLastCustomer = lastEdge.getEndOfServiceCustomer1() + lastEdge.getDistance() + lastEdge.getWaitingTime() + lastCustomer.getServiceTime();
                distancia = getDistanceFromTo(lastCustomer, candidateCustomer);
            }

            //If the candidate customer can´t be added to the route due Problem Constraints
            if ((lastRoute.getVehicleCapacity() < candidateCustomer.getDemand()) || (candidateCustomer.getTimeWindowEnd() < (distancia + eoSLastCustomer))) {

                //If the capacity of the last route is less than the demand of the candidate customer, 
                // double eoSLastCustomer = lastEdge.getEndOfServiceCustomer1() + lastEdge.getDistance() + lastEdge.getWaitingTime() + lastCustomer.getServiceTime();
                lastRoute.insertCustomer(depot);
                distancia = getDistanceFromTo(lastCustomer, depot);
                Edge edge = new Edge(lastCustomer, depot, lastRoute, 0, distancia, eoSLastCustomer, 0);
                lastEdgesList.add(edge);
                //Como esta al depot la distanciaRecorrida solamente consiste de la distancia recorrida que se tenia mas la distancia al depot
                // eoSLastCustomer = eoSLastCustomer + edge.getDistance() + edge.getWaitingTime() + edge.getCustomer2().getServiceTime();
                double routeDistance = lastRoute.getDistance() + distancia;
                lastRoute.setDistance(routeDistance);

                //we create a new route
                Route route = new Route(depot, vehicleCapacity);
                //Add the new route to the list
                routes.add(route);
                //Return -1, that says the customer couldn´t be added to the route
                return -1;
            } else {

                double waitingTime = 0;

                if (candidateCustomer.getTimeWindowStart() > (distancia + eoSLastCustomer)) {
                    waitingTime = candidateCustomer.getTimeWindowStart() - (distancia + eoSLastCustomer);
                }

                lastRoute.insertCustomer(candidateCustomer);
                addedCustomers.add(candidateCustomer);
                customers.remove(candidateCustomer);
                // eoSLastCustomer = eoSLastCustomer + distancia + waitingTime + candidateCustomer.getServiceTime();
                Edge newEdge = new Edge(lastCustomer, candidateCustomer, lastRoute, candidateCustomer.getDemand(), distancia, eoSLastCustomer, waitingTime);
                lastEdgesList.add(newEdge);
                double routeDistance = lastRoute.getDistance() + distancia;
                lastRoute.setDistance(routeDistance);

                if (customers.isEmpty()) {

                    lastRoute.insertCustomer(depot);
                    distancia = getDistanceFromTo(candidateCustomer, depot);
                    eoSLastCustomer = eoSLastCustomer + distancia + newEdge.getWaitingTime() + candidateCustomer.getServiceTime();
                    Edge edge = new Edge(candidateCustomer, depot, lastRoute, 0, distancia, eoSLastCustomer, 0);
                    lastEdgesList.add(edge);
                    //Como esta al depot la distanciaRecorrida solamente consiste de la distancia recorrida que se tenia mas la distancia al depot
                    routeDistance = lastRoute.getDistance() + distancia;
                    lastRoute.setDistance(routeDistance);

                }

            }

        }
        return 1;
    }

    private double getDistanceFromTo(Customer customerOrigin, Customer customerDestiny) {

        double xCoord = Math.abs(customerDestiny.getxCoord() - customerOrigin.getxCoord());
        double yCoord = Math.abs(customerDestiny.getyCoord() - customerOrigin.getyCoord());
        double distance = Math.sqrt((xCoord * xCoord) + (yCoord * yCoord));

        return distance;

    }

}
