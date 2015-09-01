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
 */
public class Heuristic_Relocate extends VRPHeuristic {

    @Override
    public int getNextElement(VehicleRoutingProblem problem) {
 
        int capacity = problem.getCapacity();
        Customer depot = problem.getDepot();
        List<Route> routes = problem.getRoutes();
         
        ListIterator<Route> iteratorRoutes;
        iteratorRoutes = routes.listIterator();
        routes.listIterator();
        
        Route routeJ;
        Route routeK;
        int cantRoutes =routes.size();
        
        
           for(int i = 0; i < (cantRoutes);i++){
               if(i == (cantRoutes-1)){
                   routeJ = routes.get(i);
                   routeK = routes.get(0);
               }else{
                   routeJ = routes.get(i);
                   routeK = routes.get(i + 1);
               }
               
               //Si la ruta ya no tiene capacidad para otro cliente mas (vehiculo lleno)
               if(routeK.getVehicleCapacity() > 0){ 
                   
                List<Customer> customersJ = routeJ.getCustomers();
                //ListIterator<Customer> iteratorCustomers = customersJ.listIterator();
                int cantCustomersJ = customersJ.size();
                Customer candidateCustomer;

                List<Edge> edgesK = routeK.getEdges();
                ListIterator<Edge> iteratorEdges = edgesK.listIterator();
                Edge candidateEdge;
                int counterEdges = -1;

                //x=1 y cantCustomersJ-1 para no tomar en cuenta los depositos de la ruta
                for(int x=1; x< (cantCustomersJ -1 );x++){
                    candidateCustomer = customersJ.get(x);
                    
                    if(candidateCustomer.getDemand() <= routeK.getVehicleCapacity()){

                        double distanceJ1 = getDistanceFromTo(candidateCustomer, customersJ.get(x-1));
                        double distanceJ2 = getDistanceFromTo(candidateCustomer, customersJ.get(x+1));
                        double distanceK1 = getDistanceFromTo(customersJ.get(x-1), customersJ.get(x+1));

                        while(iteratorEdges.hasNext()){
                            candidateEdge = iteratorEdges.next();
                            counterEdges++;

                            double distanceK2 = getDistanceFromTo(candidateCustomer, candidateEdge.getCustomer1());
                            double distanceK3 = getDistanceFromTo(candidateCustomer, candidateEdge.getCustomer2());
                            double distanceJ3 = candidateEdge.getDistance();

                            //Si hay algun ahorro en la distancia recorrida por el vehiculo en caso de que se cambie el cliente de lugar, se revisa que se pueda cambiar 
                            if((distanceJ1 + distanceJ2 + distanceJ3) > (distanceK1 + distanceK2 + distanceK3)){

                                if( (candidateEdge.getEndOfServiceCustomer1() + distanceK2) > candidateCustomer.getTimeWindowEnd() ||  
                                (candidateEdge.getEndOfServiceCustomer1() + distanceK2 + candidateCustomer.getServiceTime() + distanceK3) > candidateEdge.getCustomer2().getTimeWindowEnd() ){

                                    //Si la insercion parece que puede ser posible, se pasa a revisar todo el movimiento en los arcos de la nueva ruta

                                    if(checkFeasibility(edgesK, counterEdges, candidateCustomer)){
                                        System.out.println("Se puede hacer cambio en el cliente " + candidateCustomer.getNumber() + "  En la ruta = " + edgesK.toString() + " con la llave " + counterEdges  );
                                        return 1;
                                    }


                                }

                            }

                        }

                    }
                }
            }
           }
         
        System.out.println("Done"); 
        return 0;
       

    }

    @Override
    public String toString() {
        return "Heuristica Operacion Relocate";
    }

    private double getDistanceFromTo(Customer customerOrigin, Customer customerDestiny) {

        double xCoord = Math.abs(customerDestiny.getxCoord() - customerOrigin.getxCoord());
        double yCoord = Math.abs(customerDestiny.getyCoord() - customerOrigin.getyCoord());
        double distance = Math.sqrt((xCoord * xCoord) + (yCoord * yCoord));

        return distance;

    }
    
      
  private boolean checkFeasibility(  List<Edge> edges, int index, Customer customer){
      
      Edge newEdge = edges.get(index);
      
      //bj (not a Blow Job) es el tiempo en el que comenzaba originalmente el servicio del cliente 2 en el arco seleccionado para insercion
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

}
