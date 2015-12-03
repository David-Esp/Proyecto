/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vrp.heuristics;

import java.util.List;
import java.util.ListIterator;
import vrp.Problem.Customer;
import vrp.Problem.Edge;
import vrp.Problem.Route;
import vrp.Problem.VehicleRoutingProblem;
import vrp.heuristics.exchange.Candidate;
import vrp.heuristics.exchange.Exchange;
import vrp.heuristics.exchange.RouteExchange;

/**
 *
 * @author David
 */
public class Heuristic_Exchange extends VRPHeuristic {

    /**
     *
     * @param edgeJ
     * @param customerInsertJ
     * @param customerK_1
     * @param customerK_2
     * @return
     */
    public double tieneAhorro(Edge edgeJ, Customer customerInsertJ, Customer customerK_1, Customer customerK_2) {
        Customer customerJ_1 = edgeJ.getCustomer1();
        Customer customerJ_2 = edgeJ.getCustomer2();
        // Customer customerK_1 = edgeK.getCustomer1();
        // Customer customerK_2 = edgeK.getCustomer2();
        double distance1_before = edgeJ.getDistance();
        double distance2_before = getDistanceFromTo(customerK_1, customerInsertJ);
        double distance3_before = getDistanceFromTo(customerInsertJ, customerK_2);

        double suma_before = (distance1_before + distance2_before + distance3_before);

        double distance1_after = getDistanceFromTo(customerK_1, customerK_2);
        double distance2_after = getDistanceFromTo(customerJ_1, customerInsertJ);
        double distance3_after = getDistanceFromTo(customerInsertJ, customerJ_2);
        double suma_after = (distance1_after + distance2_after + distance3_after);

        if (suma_after < suma_before) {
            return suma_before - suma_after;
        } else {
            return 0;
        }

    }

    private boolean seemsFeasible(Customer customer, Edge edge) {
        Customer customerK_1 = edge.getCustomer1();
        Customer customerK_2 = edge.getCustomer2();
        double earliestEOSK1 = customerK_1.getTimeWindowStart() + customerK_1.getServiceTime();

        double distanceK1_Cust = getDistanceFromTo(customer, customerK_1);
        double distanceCust_K2 = getDistanceFromTo(customerK_2, customer);
        double earliestEOSCust = customer.getTimeWindowStart() + customer.getServiceTime();

        if ((earliestEOSK1 + distanceK1_Cust) > customer.getTimeWindowEnd()) {
            return false;
        }

        if ((earliestEOSCust + distanceCust_K2) > customerK_2.getTimeWindowEnd()) {
            return false;
        }

        if ((earliestEOSK1 + distanceK1_Cust + customer.getServiceTime() + distanceCust_K2) > customerK_2.getTimeWindowEnd()) {
            return false;
        }

        return true;
    }

    public boolean isValidRoute(Route route) {

        double distanceJK = 0;
        double currentDistance = 0;
        double currentEndOfService = 0;
        double waitingTime = 0;
        double totalDemand = 0;
        List<Customer> customers = route.getCustomers();

        for (int x = 0; x < customers.size() - 1; x++) {

            Customer customerJ = customers.get(x);
            Customer customerK = customers.get(x + 1);
            totalDemand += customerJ.getDemand();
            
            distanceJK = getDistanceFromTo(customerJ, customerK);
            currentDistance = distanceJK + currentEndOfService;

            if (currentDistance >  customerK.getTimeWindowEnd() ) {
                return false;
            } else {
                if ((currentEndOfService + distanceJK) < customerK.getTimeWindowStart()) {
                    waitingTime = customerK.getTimeWindowStart() - (currentEndOfService + distanceJK);
                } else {
                    waitingTime = 0;
                }
                currentEndOfService = currentDistance + waitingTime + customerK.getServiceTime();
            }

        }
        
        if(totalDemand > route.getVehicleCapacity())
            return false;
                   
        return true;
    }
    
    private boolean realSavings(Route routeA, Route routeB, Route tempRouteA, Route tempRouteB){
        double distanceRA = calcDistRoute(routeA);
        double distanceRB = calcDistRoute(routeB);
        double distanceTempA = calcDistRoute(tempRouteA);
        double distanceTempB = calcDistRoute(tempRouteB);
        
        if( (distanceRA + distanceRB) <= (distanceTempA + distanceTempB + 0.001))
            return false;
  
        return true;
    }
    
    private double calcDistRoute(Route route){
        double totalDistance = 0;
        List<Customer> customers = route.getCustomers();
        
        for(int x = 0 ; x < customers.size()-1; x++){
            totalDistance += getDistanceFromTo(customers.get(x), customers.get(x+1));
  
        }
        return totalDistance;
        
    }

    //esPosible(Ruta a la que se le va a incertar, el index del arco en donde se va a insertar, y el cliente a insertar 
    private boolean esPosible(Route rutaJ, int j, Customer customerCan) {

        //Si la capacidad total del vehiculo es excedida por alguna de las nuevas rutas, no es factible hacer el cambio
        if (rutaJ.getVehicleCapacity() < customerCan.getDemand()) {
            return false;
        }

        List<Edge> edgesJ = rutaJ.getEdges();
        Edge sEdgeJ = edgesJ.get(j);

        Customer customerJ_1 = sEdgeJ.getCustomer1();
        Customer customerJ_2 = sEdgeJ.getCustomer2();

        double distanceJK_after = getDistanceFromTo(customerJ_1, customerCan);
        double distanceKJ_after = getDistanceFromTo(customerCan, customerJ_2);

        double eoSCustomerJ_1 = sEdgeJ.getEndOfServiceCustomer1();

        if (eoSCustomerJ_1 + distanceJK_after > customerCan.getTimeWindowEnd()) {
            return false;
        } else {
            //En caso de que se pueda llegar a tiempo en ambas rutas, se procede a hacer una revision de cada una, 
            double wtj;

            if ((eoSCustomerJ_1 + distanceJK_after) < customerCan.getTimeWindowStart()) {
                wtj = customerCan.getTimeWindowStart() - (eoSCustomerJ_1 + distanceJK_after);

            } else {
                wtj = 0;
            }

            double eoSCustomerJ_2 = eoSCustomerJ_1 + distanceJK_after + wtj + customerCan.getServiceTime();
            double wtk = 0;

            if (eoSCustomerJ_2 + distanceKJ_after > customerJ_2.getTimeWindowEnd()) {
                return false;
            }

            if ((eoSCustomerJ_2 + distanceKJ_after) < customerJ_2.getTimeWindowStart()) {
                wtk = customerJ_2.getTimeWindowStart() - (eoSCustomerJ_2 + distanceKJ_after);
            } else {
                wtk = 0;
            }

            if (wtk == 0) {
                //--------------------------------------------------
                //-----------Ruta J---------------------------------
                //--------------------------------------------------
                //En esta ruta revisamos los arcos de K desde el arco que se desharia en delante
                revisionRutaK: //Etiqueta para luego salir del loop
                for (int temK = j + 1; temK < edgesJ.size(); temK++) { //Aqui va bien
                    Edge newEdgeK = edgesJ.get(temK);
                    double newEOS1 = (eoSCustomerJ_2 + distanceKJ_after) + wtk + newEdgeK.getCustomer1().getServiceTime();
                    if (newEOS1 + newEdgeK.getDistance() > newEdgeK.getCustomer2().getTimeWindowEnd()) {
                        //*********************************** En caso de que alguna ventana de tiempo se viole
                        return false;
                    } else {
                        if (newEdgeK.getCustomer2().getTimeWindowStart() > newEOS1 + newEdgeK.getDistance()) {
                            wtk = newEdgeK.getCustomer2().getTimeWindowStart() - (newEOS1 + newEdgeK.getDistance());
                            break revisionRutaK;
                        } else {
                            wtk = 0;
                        }

                        eoSCustomerJ_2 = newEOS1;
                        distanceKJ_after = newEdgeK.getDistance();

                    }
                }

            }
        }
        return true;

    }

    @Override
    public int getNextElement(VehicleRoutingProblem problem) {

        int capacity = problem.getCapacity();
        Customer depot = problem.getDepot();
        List<Route> routes = problem.getRoutes();
        Exchange exchange = new Exchange();

        //Recorremos todas las rutas
        for (int routeAIndex = 0; routeAIndex < routes.size(); routeAIndex++) {

            Route routeA = routes.get(routeAIndex);
            List<Customer> customersA = routeA.getCustomers();
            List<Edge> edgesA = routeA.getEdges();
            RouteExchange routeEx = new RouteExchange(routeAIndex, routeA.getVehicleCapacity(), capacity);
            exchange.getRoutes().add(routeEx);

            //Recorremos todos los clientes de la rutaA, viendo en que arco de las otras rutas puede ser insertado
            for (int customerAIndex = 1; customerAIndex < customersA.size() - 1; customerAIndex++) {
                Customer customerA = customersA.get(customerAIndex);

                for (int routeBIndex = routeAIndex + 1; routeBIndex < routes.size(); routeBIndex++) {
                    Route routeB = routes.get(routeBIndex);
                    List<Edge> edgesB = routeB.getEdges();

                    for (int edgeBIndex = 0; edgeBIndex < edgesB.size(); edgeBIndex++) {
                        Edge edgeB = edgesB.get(edgeBIndex);

                        if (seemsFeasible(customerA, edgeB)) {
                            Customer customerA_1 = customersA.get(customerAIndex - 1);
                            Customer customerA_2 = customersA.get(customerAIndex + 1);
                            Double savings = tieneAhorro(edgeB, customerA, customerA_1, customerA_2);
                            if (savings > 0) {
                                Candidate candidateA = new Candidate(true, routeAIndex, routeBIndex, customerAIndex, (int) customerA.getDemand(), edgeBIndex, savings);
                                exchange.getCandidates().add(candidateA);

                            }

                        }

                    }
                }

            }

            //Recorremos todos los arcos de la rutaA, viendo cual cliente de las otras rutas puede ser insertado 
            for (int edgeAIndex = 0; edgeAIndex < edgesA.size(); edgeAIndex++) {
                Edge edgeA = edgesA.get(edgeAIndex);

                for (int routeBIndex = routeAIndex + 1; routeBIndex < routes.size(); routeBIndex++) {
                    Route routeB = routes.get(routeBIndex);
                    List<Customer> customersB = routeB.getCustomers();
                    for (int customerBIndex = 1; customerBIndex < customersB.size() - 1; customerBIndex++) {
                        Customer customerB = customersB.get(customerBIndex);

                        if (seemsFeasible(customerB, edgeA)) {
                            Customer customerB_1 = customersB.get(customerBIndex - 1);
                            Customer customerB_2 = customersB.get(customerBIndex + 1);
                            Double savings = tieneAhorro(edgeA, customerB, customerB_1, customerB_2);
                            if (savings > 0) {
                                Candidate candidateB = new Candidate(true, routeBIndex, routeAIndex, customerBIndex, (int) customerB.getDemand(), edgeAIndex, savings);
                                exchange.getCandidates().add(candidateB);

                            }
                        }
                    }

                }

            }

        }

       // System.out.println(exchange.toString());
        boolean onegood = false;

        while (exchange.existActive()) {

            int candidateIndex = exchange.getMaxActive();
            Candidate candidate = exchange.getCandidates().get(candidateIndex);

            List<Candidate> listPossible = exchange.getBackCandidates(candidate);
          //  listPossible.sort((Candidate object1, Candidate object2) -> {                return (int) (object2.getSavings() - object1.getSavings());    });
            Route tempRouteA = new Route(depot, capacity);
            Route tempRouteB = new Route(depot, capacity);

            while (!listPossible.isEmpty()) {
                Candidate candidateB = listPossible.get(0);
                Route routeB = routes.get(candidateB.getRouteOrigin());
                Route routeA = routes.get(candidate.getRouteOrigin());

                tempRouteA.getCustomers().clear();
                tempRouteB.getCustomers().clear();
                
                tempRouteA.getCustomers().addAll(routeA.getCustomers());
                tempRouteB.getCustomers().addAll(routeB.getCustomers());
                

                tempRouteA.getCustomers().add(candidateB.getEdge() + 1, routeB.getCustomers().get(candidateB.getCustomer()));
                tempRouteB.getCustomers().add(candidate.getEdge() + 1, routeA.getCustomers().get(candidate.getCustomer()));

                if (candidate.getCustomer() <= candidateB.getEdge()) {
                    tempRouteA.getCustomers().remove(candidate.getCustomer());
                } else {
                    tempRouteA.getCustomers().remove(candidate.getCustomer() + 1);
                }
                if (candidateB.getCustomer() <= candidate.getEdge()) {
                    tempRouteB.getCustomers().remove(candidateB.getCustomer());
                } else {
                    tempRouteB.getCustomers().remove(candidateB.getCustomer() + 1);
                }

     //           if(realSavings(routeA,routeB,tempRouteA,tempRouteB)){
                
                
                if (isValidRoute(tempRouteA) && isValidRoute(tempRouteB) && realSavings(routeA,routeB,tempRouteA,tempRouteB)) {
                    onegood = true;
                    break;
                } 
      //          }
                else {
                    listPossible.remove(0);
                }

            }

            if (!onegood) {
                // exchange.getCandidates().get(candidateIndex).setActive(false);
                candidate.setActive(false);
            } else {

                if (candidate.getRouteDestiny() > candidate.getRouteOrigin()) {
                    problem.getRoutes().remove(candidate.getRouteDestiny());
                    problem.getRoutes().remove(candidate.getRouteOrigin());

                } else {
                    problem.getRoutes().remove(candidate.getRouteOrigin());
                    problem.getRoutes().remove(candidate.getRouteDestiny());

                }

                tempRouteA.getEdges().clear();

                double distance = 0;
                double eos = 0;
                double waitingTime = 0;
                double distanceRoute = 0;
                for (int i = 0; i < (tempRouteA.getCustomers().size() - 1); i++) {
                    Customer customer1 = tempRouteA.getCustomers().get(i);
                    Customer customer2 = tempRouteA.getCustomers().get(i + 1);
                    distance = getDistanceFromTo(customer1, customer2);

                    if (customer2.getTimeWindowStart() > (distance + eos)) {
                        waitingTime = customer2.getTimeWindowStart() - (distance + eos);
                    } else {
                        waitingTime = 0;
                    }

                    //Customer 1 ,  customer 2, ruta, demanda (del cliente2), distancia, end of service cliente 1 , tiempo de espera para cliente 2
                    Edge edge = new Edge(customer1, customer2, tempRouteA, customer2.getDemand(), distance, eos, waitingTime);
                    tempRouteA.getEdges().add(edge);
                    eos = eos + distance + waitingTime + customer2.getServiceTime();
                    distanceRoute += distance;
                }
                tempRouteA.setDistance(distanceRoute);

                tempRouteB.getEdges().clear();

                double distanceB = 0;
                double eosB = 0;
                double waitingTimeB = 0;
                double distanceRouteB = 0;
                for (int i = 0; i < (tempRouteB.getCustomers().size() - 1); i++) {
                    Customer customer1 = tempRouteB.getCustomers().get(i);
                    Customer customer2 = tempRouteB.getCustomers().get(i + 1);
                    distanceB = getDistanceFromTo(customer1, customer2);

                    if (customer2.getTimeWindowStart() > (distanceB + eosB)) {
                        waitingTimeB = customer2.getTimeWindowStart() - (distanceB + eosB);
                    } else {
                        waitingTimeB = 0;
                    }

                    //Customer 1 ,  customer 2, ruta, demanda (del cliente2), distancia, end of service cliente 1 , tiempo de espera para cliente 2
                    Edge edge = new Edge(customer1, customer2, tempRouteB, customer2.getDemand(), distanceB, eosB, waitingTimeB);
                    tempRouteB.getEdges().add(edge);
                    eosB = eosB + distanceB + waitingTimeB + customer2.getServiceTime();
                    distanceRouteB += distanceB;
                }
                tempRouteB.setDistance(distanceRouteB);

                problem.getRoutes().add(tempRouteB);
                problem.getRoutes().add(tempRouteA);
                return 1;

            }

        }

        return 0;

    }

    @Override
    public String toString() {
        return "REL";
    }

    private double getDistanceFromTo(Customer customerOrigin, Customer customerDestiny) {

        double xCoord = Math.abs(customerDestiny.getxCoord() - customerOrigin.getxCoord());
        double yCoord = Math.abs(customerDestiny.getyCoord() - customerOrigin.getyCoord());
        double distance = Math.sqrt((xCoord * xCoord) + (yCoord * yCoord));

        return distance;

    }

    /*
     private boolean checkFeasibility(  List<Edge> edges, int index, Customer customer){
      
     Edge newEdge = edges.get(index);
      
     //bj es el tiempo en el que comenzaba originalmente el servicio del cliente 2 en el arco seleccionado para insercion
     double bj = newEdge.getEndOfServiceCustomer1()+newEdge.getDistance()+newEdge.getWaitingTime();
      
     double distance1 = getDistanceFromTo(customer, newEdge.getCustomer1());
     double distance2 = getDistanceFromTo(customer, newEdge.getCustomer2());
     double wT1 = 0;
      
     if(newEdge.getEndOfServiceCustomer1()+distance1 > customer.getTimeWindowStart()){
     wT1 =0;
     }else{
     wT1 =customer.getTimeWindowStart() - (newEdge.getEndOfServiceCustomer1()+distance1);
     }
     double bu = newEdge.getEndOfServiceCustomer1()+distance1  + wT1;
     double eoSu = bu + customer.getServiceTime();
      
     double wT2 = 0;
      
     if(eoSu + distance2 > newEdge.getCustomer2().getTimeWindowStart()){
     wT2 = 0;
     }else{
     wT2 = newEdge.getCustomer2().getTimeWindowStart() - (eoSu + distance2 );
     }
      
     //bju es el nuevo tiempo en que iniciaria el servicio en el cliente dos, si se agrega antes el cliente u
     double bju = eoSu + distance2 +wT2;
              
     //Se necesita definir el push forfard
     double pushForward = bju - bu;
     ListIterator<Edge> iterator;
     iterator = edges.listIterator();
     int counter  = -1;
      
     if( bju > newEdge.getCustomer2().getTimeWindowEnd()  || bu > customer.getTimeWindowEnd() ){
     return false;
     }else{
     while(iterator.hasNext()){
     counter++;
     newEdge = iterator.next();
     if(counter > index){
     //  pushForward = pushForward - newEdge.getWaitingTime();
                      
     if(pushForward > newEdge.getWaitingTime()){
                       
     double twend =  newEdge.getCustomer2().getTimeWindowEnd();
     double dist = newEdge.getDistance();
     double servi = newEdge.getEndOfServiceCustomer1();
     pushForward = pushForward - newEdge.getWaitingTime();
                         
     if((servi + dist + pushForward) <= twend ){
     return true;
     }else{
     return false;
     }
     }else{
     return true;
     }
     }
                  
     }
               
     return true;    
     }
     
     }
    
     */
}
