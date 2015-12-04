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

/**
 *
 * @author David
 *
 * Coefficient Weighted Distance Time Heuristic
 */
public class Heuristic_NNH extends VRPHeuristic {

    /**
     * Creates a new instance of <code>MaxValue</code>.
     */
    public Heuristic_NNH() {

    }

    @Override
    public int getNextElement(VehicleRoutingProblem problem) {
        double teta1 = 0.33;
        double teta2 = 0.34;
        double teta3 = 0.33;

        int vehicles = problem.getVehicles();
        int capacity = problem.getCapacity();

        String instanceName = problem.getInstanceName();
        Customer depot = problem.getDepot();
        List<Customer> customers = problem.getCustomers();
        List<Customer> addedCustomers = problem.getAddedCustomers();
        List<Route> routes = problem.getRoutes();
        List<Edge> edges = problem.getEdges();

          //Customer customer = problem.getCustomer(); 
        ListIterator<Customer> iterator;
        iterator = customers.listIterator();

        //Mientras siga habiendo clientes
        //Se busca al cliente que tenga la distancia menor y el menor tiempo de entrega  (due date) y que la ruta lo soporte
        //Se agrega el cliente a la ruta 
        //Si no se encuentra un cliente con estas caracteristicas, se cierra la ruta y se abre una nueva ruta y se regresa a buscar clientes 
        //
        //Si hay mas clientes sin ruta
        if (iterator.hasNext()) {

            //Revisas si no hay ninguna ruta creada
            if (routes.isEmpty()) {
                //Agregas una nueva ruta que parte del deposito, con la capacidad total del vehiculo
                Route route = new Route(depot, capacity);
                //Agregamos la ruta a la lista de rutas
                routes.add(route);
            } else {
                //Si ya hay rutas, revisas la ultima ruta que esta siendo utilizada
                Route lastRoute = routes.get(routes.size() - 1);
                //Si esta ruta es de tamaño mayor a uno, revisamos que no sea una ruta que haya sido cerrada
                if (lastRoute.getCustomers().size() > 1) {
                    //Revisamos que el ultimo cliente no sea el deposito (ruta completa)
                    if (lastRoute.getCustomers().get(lastRoute.getCustomers().size() - 1).equals(depot)) {
                        //Si la ruta esta completa, agregamos una nueva Ruta
                        Route route = new Route(depot, capacity);
                        routes.add(route);
                        //Actualizamos variable lastRoute 
                    }
                }
            }
            //Salimos de las condiciones con una ruta que se pueda utilizar
            Route lastRoute = routes.get(routes.size() - 1);
            List<Edge> ed = lastRoute.getEdges();

            double distanciaRecorrida = lastRoute.getDistance();

            Customer lastCustomer = lastRoute.getCustomers().get(lastRoute.getCustomers().size() - 1);

            //Buscamos a un cliente que se pueda agregar a la ruta que tenemos disponible
            int availableCapacity = lastRoute.getVehicleCapacity();
            Customer customer;

            double nextFound = -1;
            double bestFound = 1000000;
            int count = -1;
            int coin = -1;
            Customer candidateCustomer = null;
            Edge edge = null;

           // double medidor = 999999999;
            //revisamos todos los clientes que tenemos sin ruta
            while (iterator.hasNext()) {
                customer = iterator.next();
                count++;
                //Si la demanda del cliente no supera la capacidad disponible de la ruta, lo revisamos
                if (customer.getDemand() <= availableCapacity) {

                    //Sumamos la distancia del ultimo cliente de la ruta al nuevo cliente y cuando termine la ventana de tiempo del nuevo cliente
                    double distancia = getDistanceFromTo(customer, lastCustomer);

                    if ((distanciaRecorrida + distancia) <= customer.getTimeWindowEnd()) {
                    //TiempoEntreServicios = Tij = bj - (bi +Si)
                        //double tiempoEntreServicios = customer.getTimeWindowStart() - (lastCustomer.getTimeWindowStart() + lastCustomer.getServiceTime());
                        //Se revisa cuando inicia el servicio en el cliente J (si tiene que esperar o si llega despues de que inicio su ventana de tiempo)

                        double beginOfServiceJ;
                        double waitingTimeJ;

                        if ((distanciaRecorrida + distancia) >= customer.getTimeWindowStart()) {
                            waitingTimeJ = 0;
                            beginOfServiceJ = (distanciaRecorrida + distancia);

                        } else {
                            waitingTimeJ = customer.getTimeWindowStart() - (distanciaRecorrida + distancia);
                            beginOfServiceJ = customer.getTimeWindowStart();
                        }
                        double tiempoEntreServicios = beginOfServiceJ - (distanciaRecorrida);
                    //UrgenciaEntrega = vij= lj - (bi + Si + tij)
                        //lj = End of service of j (fin de la ventana de tiempo de j)
                        //bi = inicio del servicio en i
                        //Si = tiempo que tarda el servicio en i
                        //tij = direct travel distance from i to j
                        double urgenciaEntrega = customer.getTimeWindowEnd() - (distanciaRecorrida + distancia);

                    //Se calcula segun la dormula teta1(distancia) + teta2(tiempoEntreSevicios) + teta3(UrgenciaEntrega)
                        // nextFound =  ( distancia + customer.getTimeWindowEnd());
                        nextFound = ((teta1 * distancia) + (teta2 * tiempoEntreServicios) + (teta3 * urgenciaEntrega));
                        //Si la suma es menor a la mejor encontrada
                        if (bestFound > nextFound) {
                            //Agregamos a ese cliente como un cliente candidato y actualizamos el valor del mejor encontrado
                            candidateCustomer = customer;
                            bestFound = nextFound;
                            edge = new Edge(lastCustomer, candidateCustomer, lastRoute, candidateCustomer.getDemand(), distancia, distanciaRecorrida, waitingTimeJ);

                            coin = count;
                        }
                    }
                }
            }

            //Revisamos si no se encontró algún cliente candidato
            if (candidateCustomer == null) {

                //Si no se encontro ningun cliente para esa ruta, se le agrega el depot (se cierra la ruta)
                lastRoute.insertCustomer(depot);
                double distancia = getDistanceFromTo(lastCustomer, depot);
                edge = new Edge(lastCustomer, depot, lastRoute, 0, distancia, distanciaRecorrida, 0);
                edges.add(edge);
                ed.add(edge);
                //Como esta al depot la distanciaRecorrida solamente consiste de la distancia recorrida que se tenia mas la distancia al depot
                distanciaRecorrida = distanciaRecorrida + edge.getDistance();
                lastRoute.setDistance(distanciaRecorrida);
                //Se abre una nueva ruta, ya que si no se encuentra cliente, quiere decir que la ruta ya no da capacidad para mas clientes
                Route route = new Route(depot, capacity);
                routes.add(route);
            } else {
                //Se inserta el cliente a la ruta
                lastRoute.insertCustomer(candidateCustomer);
                //Se agrega el arco creado a la lista de arcos
                edges.add(edge);
                ed.add(edge);
                //Se actualiza la distancia recorrida (distancia + tiempo de servicio) 
                //Esta faltando el tiempo de espera (si es que hay)
                distanciaRecorrida = distanciaRecorrida + edge.getDistance() + edge.getWaitingTime() + edge.getCustomer2().getServiceTime();
                lastRoute.setDistance(distanciaRecorrida);

                //Se quita el cliente de la lista de clientes disponibles
                customers.remove(coin);
                addedCustomers.add(candidateCustomer);

                //Si el cliente que se insertó era el último, se cierra la ruta que se estaba utilizando
                if (customers.isEmpty()) {

                    lastRoute.insertCustomer(depot);
                    double distancia = getDistanceFromTo(candidateCustomer, depot);
                    edge = new Edge(candidateCustomer, depot, lastRoute, 0, distancia, distanciaRecorrida, 0);
                    edges.add(edge);
                    //Como esta al depot la distanciaRecorrida solamente consiste de la distancia recorrida que se tenia mas la distancia al depot
                    distanciaRecorrida = distanciaRecorrida + edge.getDistance();
                    lastRoute.setDistance(distanciaRecorrida);

                    ed.add(edge);
                }
            }

            return 1;
        } else {
            return 0;

        }

    }

    /**
     * Returns the string representation of this heuristic.
     * <p>
     * @return The string representation of this heuristic.
     */
    public String toString() {
        return "NNH";
    }

    private double getDistanceFromTo(Customer customerOrigin, Customer customerDestiny) {

        double xCoord = Math.abs(customerDestiny.getxCoord() - customerOrigin.getxCoord());
        double yCoord = Math.abs(customerDestiny.getyCoord() - customerOrigin.getyCoord());
        double distance = Math.sqrt((xCoord * xCoord) + (yCoord * yCoord));

        return distance;

    }

}
