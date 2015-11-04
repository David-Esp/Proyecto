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
public class Heuristic_RelocateIntraRoute extends VRPHeuristic {
    
    /**
     *
     * @param edgeJ
     * @param customerInsertJ
     * @param customerK_1
     * @param customerK_2
     * @return
     */
    public double tieneAhorro(Edge edgeJ, Customer customerInsertJ, Customer customerK_1, Customer customerK_2){
          Customer customerJ_1 = edgeJ.getCustomer1();
          Customer customerJ_2 = edgeJ.getCustomer2();
         // Customer customerK_1 = edgeK.getCustomer1();
         // Customer customerK_2 = edgeK.getCustomer2();
          double distance1_before = edgeJ.getDistance();
          double distance2_before = getDistanceFromTo(customerK_1, customerInsertJ );
          double distance3_before = getDistanceFromTo(customerInsertJ, customerK_2 );
          
          
          double suma_before = (distance1_before + distance2_before + distance3_before);
          
          
          double distance1_after = getDistanceFromTo(customerK_1, customerK_2);
          double distance2_after = getDistanceFromTo(customerJ_1, customerInsertJ);
          double distance3_after = getDistanceFromTo(customerInsertJ, customerJ_2);
          double suma_after = (distance1_after + distance2_after + distance3_after);
          
         
          if( suma_after < suma_before )
             return suma_before - suma_after;
          else
            return 0;
         
    }
    
    //esPosible(Ruta a la que se le va a incertar, el index del arco en donde se va a insertar, y el cliente a insertar 
    
    private boolean esPosible(Route routeJ, Edge edgeCan, int indexEdge, Customer customerCan, int indexCustomer){
          
        List<Edge> edgesJ = routeJ.getEdges();
        
        
        if(indexCustomer < indexEdge){
            
            Edge edgeJ1 = edgesJ.get(indexCustomer -1);
            Edge edgeJ2 = edgesJ.get(indexCustomer);
            
            double distanceJ12 = getDistanceFromTo(edgeJ1.getCustomer1(), edgeJ2.getCustomer2());
            double eosJ1 = edgeJ1.getEndOfServiceCustomer1();
            double wtJ1 = 0;
             
            if(edgeJ2.getCustomer2().getTimeWindowStart() > (eosJ1 + distanceJ12)){
                  wtJ1 = edgeJ2.getCustomer2().getTimeWindowStart() - (eosJ1 + distanceJ12); 
            } 
             
            for(int i=(indexCustomer +1); i < indexEdge ; i ++){
                Edge newEdgeJ = edgesJ.get(i);
                
                eosJ1 =  eosJ1 + distanceJ12 + wtJ1 + newEdgeJ.getCustomer1().getServiceTime();
                if((eosJ1 + newEdgeJ.getDistance()) > newEdgeJ.getCustomer2().getTimeWindowEnd()){
                    return false;
                }else{
                    
                    if(newEdgeJ.getCustomer2().getTimeWindowStart() > (eosJ1 + newEdgeJ.getDistance())){
                        wtJ1 = newEdgeJ.getCustomer2().getTimeWindowStart() - (eosJ1 + newEdgeJ.getDistance()); 
                      } else{
                        wtJ1 = 0;
                    }
                    
                    distanceJ12 = newEdgeJ.getDistance();
                    
                }
                
            }
            
            //En este punto es donde vamos a agregar al cliente
            
            Edge newEdgeJ = edgesJ.get(indexEdge);
            eosJ1 =  eosJ1 + distanceJ12 + wtJ1 + newEdgeJ.getCustomer1().getServiceTime();
            double distanceJ2 = getDistanceFromTo(customerCan, newEdgeJ.getCustomer1());
            
             if((eosJ1 + distanceJ2) > customerCan.getTimeWindowEnd()){
                    return false;
                }else{
                    
                    if(customerCan.getTimeWindowStart() > (eosJ1 + distanceJ2)){
                        wtJ1 = customerCan.getTimeWindowStart() - (eosJ1 + distanceJ2); 
                      } else{
                        wtJ1 = 0;
                    }
             
                }
             
            eosJ1 =  eosJ1 + distanceJ2 + wtJ1 + customerCan.getServiceTime();
            distanceJ2 = getDistanceFromTo(customerCan, newEdgeJ.getCustomer2());
            
             if((eosJ1 + distanceJ2) > newEdgeJ.getCustomer2().getTimeWindowEnd()){
                    return false;
                }else{
                    
                    if(newEdgeJ.getCustomer2().getTimeWindowStart() > (eosJ1 + distanceJ2)){
                        wtJ1 = newEdgeJ.getCustomer2().getTimeWindowStart() - (eosJ1 + distanceJ2); 
                      } else{
                        wtJ1 = 0;
                    }
             
                }
             
       for(int i=(indexEdge +1); i < edgesJ.size() ; i ++){
                newEdgeJ = edgesJ.get(i);
                
                eosJ1 =  eosJ1 + distanceJ2 + wtJ1 + newEdgeJ.getCustomer1().getServiceTime();
                if((eosJ1 + newEdgeJ.getDistance()) > newEdgeJ.getCustomer2().getTimeWindowEnd()){
                    return false;
                }else{
                    
                    if(newEdgeJ.getCustomer2().getTimeWindowStart() > (eosJ1 + newEdgeJ.getDistance())){
                        wtJ1 = newEdgeJ.getCustomer2().getTimeWindowStart() - (eosJ1 + newEdgeJ.getDistance()); 
                      } else{
                        wtJ1 = 0;
                    }
                    
                    distanceJ2 = newEdgeJ.getDistance();
                    
                }
                
            }
            return true;
            
        }else{
            return false;
        }
        
        
        /*
        
        
          List<Edge> edgesJ = rutaJ.getEdges(); 
          Edge sEdgeJ = edgesJ.get(j); 
          
          Customer customerJ_1 = sEdgeJ.getCustomer1();
          Customer customerJ_2 = sEdgeJ.getCustomer2(); 
          
          double distanceJK_after = getDistanceFromTo(customerJ_1, customerCan);
          double distanceKJ_after = getDistanceFromTo(customerCan, customerJ_2);
          
          
          double eoSCustomerJ_1 = sEdgeJ.getEndOfServiceCustomer1(); 
          
          if(eoSCustomerJ_1 + distanceJK_after > customerCan.getTimeWindowEnd()){
              return false;
          } 
          else{
              //En caso de que se pueda llegar a tiempo en ambas rutas, se procede a hacer una revision de cada una, 
              double wtj; 
              
              if((eoSCustomerJ_1 + distanceJK_after) < customerCan.getTimeWindowStart()){
                  wtj = customerCan.getTimeWindowStart() - (eoSCustomerJ_1 + distanceJK_after);
                   
              }else{
                  wtj = 0;
              }
              
               double eoSCustomerJ_2 = eoSCustomerJ_1 + distanceJK_after + wtj + customerCan.getServiceTime();
               double wtk = 0;
               
               if(eoSCustomerJ_2 + distanceKJ_after > customerJ_2.getTimeWindowEnd()){
                 return false;
               } 
                
               if((eoSCustomerJ_2 + distanceKJ_after) < customerJ_2.getTimeWindowStart()){
                 wtk = customerJ_2.getTimeWindowStart() - (eoSCustomerJ_2 + distanceKJ_after);
               }else{
                 wtk = 0;
                }
   
            } 
        return true;
 */
      }

    @Override
    public int getNextElement(VehicleRoutingProblem problem) {
 
        int capacity = problem.getCapacity();
        Customer depot = problem.getDepot();
        List<Route> routes = problem.getRoutes();
         
        ListIterator<Route> iteratorRoutes;
        iteratorRoutes = routes.listIterator();
        routes.listIterator();
        
        
        //--Info necesaria para hacer el movimiento 
        Route routeJ_rel = null;
        int indexEdgeJ_rel = 0 ;
        
        int indexCustomerJ_rel = 0; 
        //------------------------
         
        double ahorro = 0;
        //En este caso es mejor irnos ruta por ruta, revisando en cada ruta si sus clientes pueden ponerse en los arcos de las rutas siguientes
        //O tambien si los clientes de las otras rutas pueden agregarse a alguno de sus arcos 
        for(int x = 0 ; x < routes.size(); x++){
            Route route = routes.get(x);
            List<Edge> edges = route.getEdges();
            List<Customer> clientes = route.getCustomers();
            
            
         for(int xc=1; xc< clientes.size()-1; xc++ ){
                Customer customerCan = clientes.get(xc);
                
                                    
                for(int xe=0;xe <(edges.size() - 1)  ; xe++){
                        Edge edgeCan = edges.get(xe);
                        double ahorroCan = tieneAhorro(edgeCan,customerCan, clientes.get(xc-1), clientes.get(xc+1) );
                        if(xc == xe || xc == (xe  + 1 ))
                            ahorroCan = 0;
                        
                        if( ahorroCan > 0){
                               //Guardar cuanto ahorro tiene Y en caso de ser Mayor que el anterior guardado, revisamos feasibility
                            if( esPosible(route, edgeCan, xe, customerCan, xc) ){
                                if( ahorroCan > ahorro){
                                    ahorro = ahorroCan;
                                    
                                    //Tenemos que guardar La ruta a al que se le va a agregar el cliente, y su arco,
                                    //Guardar la ruta donde se va a quitar el cliente, el cliente y los arcos que se van a remover
                                     routeJ_rel = route;
                                     indexEdgeJ_rel = xe; 
                                     indexCustomerJ_rel = xc ;
                                     
                                   //  esPosible(route, xe, customerCan);
                                }
                                //Entonces se guardan todos los parametros para hacer el cambio despues k
                            }
                        }
                        
                    }
                    
                 
                
            }
 
            
        } 
            
            //Se revisa que se haya encontrado algun arco y se toma el mejor encontrado.
            //Despues se realiza el cambio y se reajustan ambas rutas que se modificaron.
            
            //return 1 si se encontro algun 
            //return 0 si no se encuentra nada 
       
       if(ahorro > 0.001){
           
           List<Customer> listCustomersJ = routeJ_rel.getCustomers();
           Customer customerJ_rel = listCustomersJ.get(indexCustomerJ_rel); 
           if(indexCustomerJ_rel > indexEdgeJ_rel){
              
               routeJ_rel.getCustomers().remove(indexCustomerJ_rel);
               
               routeJ_rel.insertCustomerAt(customerJ_rel , indexEdgeJ_rel + 1);   
           }else{
              
               routeJ_rel.insertCustomerAt(customerJ_rel , indexEdgeJ_rel + 1); 
               routeJ_rel.getCustomers().remove(indexCustomerJ_rel);
           }
           routeJ_rel.getEdges().clear();
           
           double distance = 0;
           double eos = 0;
           double waitingTime = 0;
           double distanceRoute = 0;
           for(int i = 0 ; i <  (listCustomersJ.size() - 1) ; i++){
               Customer customer1 = listCustomersJ.get(i);
               Customer customer2 = listCustomersJ.get(i + 1);
               distance = getDistanceFromTo(customer1, customer2);
               
               if(customer2.getTimeWindowStart() > (distance + eos)){
                   waitingTime = customer2.getTimeWindowStart() - (distance + eos);
               }else{
                   waitingTime = 0;
               }
               
               //Customer 1 ,  customer 2, ruta, demanda (del cliente2), distancia, end of service cliente 1 , tiempo de espera para cliente 2
               Edge edge = new Edge(customer1, customer2, routeJ_rel, customer2.getDemand(), distance , eos, waitingTime);
               routeJ_rel.getEdges().add(edge);
               eos = eos + distance + waitingTime + customer2.getServiceTime();
               distanceRoute += distance;
           }
           routeJ_rel.setDistance(distanceRoute);
           // System.out.println("doneChange" + routeJ_rel + " e " + indexEdgeJ_rel + " xxs " + indexCustomerJ_rel);
            return 1;
       }else{
          // System.out.println("Ningun cambio posible");
            return 0;
       }
   
       

    }

    @Override
    public String toString() {
        return "RELINTRA";
    }

    private double getDistanceFromTo(Customer customerOrigin, Customer customerDestiny) {

        double xCoord = Math.abs(customerDestiny.getxCoord() - customerOrigin.getxCoord());
        double yCoord = Math.abs(customerDestiny.getyCoord() - customerOrigin.getyCoord());
        double distance = Math.sqrt((xCoord * xCoord) + (yCoord * yCoord));

        return distance;

    }
    
 

}
