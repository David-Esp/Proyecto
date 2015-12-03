/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vrp.heuristics;

import com.sun.xml.internal.ws.addressing.ProblemAction;
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
public class Heuristic_2OPT extends VRPHeuristic{
 
        /**
     * Creates a new instance of <code>Heuristic_2OPT</code>.
     */
    public Heuristic_2OPT() {
    }
    
      private double haveSavings(Edge edgeJ, Edge edgeK){
          Customer customerJ_1 = edgeJ.getCustomer1();
          Customer customerJ_2 = edgeJ.getCustomer2();
          Customer customerK_1 = edgeK.getCustomer1();
          Customer customerK_2 = edgeK.getCustomer2();
          double distance1_before = edgeJ.getDistance();
          double distance2_before = edgeK.getDistance();
          double suma_before = (distance1_before + distance2_before);
          double distance1_after = getDistanceFromTo(customerJ_1, customerK_2);
          double distance2_after = getDistanceFromTo(customerK_1, customerJ_2);
          double suma_after = (distance1_after + distance2_after);
          
         
          if( suma_after < suma_before )
             return suma_before - suma_after;
          else
            return 0;
        }
      
      private boolean feasibleToDo(Route rutaJ, int j, Route rutaK, int k){
          int capacity = (int) rutaJ.getDemand() +  rutaJ.getVehicleCapacity();
          
          List<Edge> edgesJ = rutaJ.getEdges();
          List<Edge> edgesK = rutaK.getEdges();
          Edge sEdgeJ = edgesJ.get(j);
          Edge sEdgeK = edgesK.get(k);
          
          Customer customerJ_1 = sEdgeJ.getCustomer1();
          Customer customerJ_2 = sEdgeJ.getCustomer2();
          Customer customerK_1 = sEdgeK.getCustomer1();
          Customer customerK_2 = sEdgeK.getCustomer2();
          
          double distanceJK_after = getDistanceFromTo(customerJ_1, customerK_2);
          double distanceKJ_after = getDistanceFromTo(customerK_1, customerJ_2);
          
          
          double eoSCustomerJ_1 = sEdgeJ.getEndOfServiceCustomer1();
          double eoSCustomerK_1 = sEdgeK.getEndOfServiceCustomer1();
          
          int capacityJ = 0;
          int capacityK = 0;
          
          //Revisamos las nuevas demandas de las rutas
          for(int x = 0 ; x < edgesJ.size(); x++){
              if( x < j){
                  capacityJ += edgesJ.get(x).getDemand();
              }else{
                  capacityK += edgesJ.get(x).getDemand();
              }
          }
          
          for(int y = 0 ; y < edgesK.size(); y++){
              if( y < k){
                  capacityK += edgesK.get(y).getDemand();
              }else{
                  capacityJ += edgesK.get(y).getDemand();
              }
          }
          
          //Si la capacidad total del vehiculo es excedida por alguna de las nuevas rutas, no es factible hacer el cambio
          if(capacityJ > capacity || capacityK > capacity){
              return false;
          }
          
          if( ((eoSCustomerJ_1 + distanceJK_after) > customerK_2.getTimeWindowEnd()) || ((eoSCustomerK_1 + distanceKJ_after) > customerJ_2.getTimeWindowEnd() )) {
              return false;
          }
          else{
              //En caso de que se pueda llegar a tiempo en ambas rutas, se procede a hacer una revision de cada una, 
              double wtj;
              double wtk;
              int casos = 0;
              if((eoSCustomerJ_1 + distanceJK_after) < customerK_2.getTimeWindowStart()){
                  wtj = customerK_2.getTimeWindowStart() - (eoSCustomerJ_1 + distanceJK_after);
                  casos = 1;
              }else{
                  wtj = 0;
              }
              
              if((eoSCustomerK_1 + distanceKJ_after)  < customerJ_2.getTimeWindowStart()){
                  wtk = customerJ_2.getTimeWindowStart() - (eoSCustomerK_1 + distanceKJ_after) ;
                  casos += 2;
              }else{
                  wtk = 0;
              }
              
              //Pasamos a revisar cada ruta, si tienen un tiempo de espera mayor a cero, no hay problema de que se descomponga la ruta despues
             
              
              if(wtj == 0){
              //--------------------------------------------------
              //-----------Ruta J---------------------------------
              //--------------------------------------------------
                //En esta ruta revisamos los arcos de K desde el arco que se desharia en delante
                revisionRutaJ: //Etiqueta para luego salir del loop
                for(int temJ = k + 1; temJ < edgesK.size() ; temJ++){
                    Edge newEdgeJ = edgesK.get(temJ);
                    double newEOS1 = (eoSCustomerJ_1 + distanceJK_after) + wtj + newEdgeJ.getCustomer1().getServiceTime();
                    if(newEOS1 + newEdgeJ.getDistance() > newEdgeJ.getCustomer2().getTimeWindowEnd()){
                        //*********************************** En caso de que alguna ventana de tiempo se viole
                        return false;
                    }else{
                        if(newEdgeJ.getCustomer2().getTimeWindowStart() > newEOS1 + newEdgeJ.getDistance()){
                            wtj = newEdgeJ.getCustomer2().getTimeWindowStart()  - (newEOS1 + newEdgeJ.getDistance());
                            break revisionRutaJ;
                        }else{
                            wtj = 0;
                        }
                        
                        eoSCustomerJ_1 = newEOS1;
                        distanceJK_after = newEdgeJ.getDistance();

                    }
                }
              
              }
  
              
              if(wtk == 0){
                 //--------------------------------------------------
                 //-----------Ruta K---------------------------------
                 //--------------------------------------------------
                //En esta ruta revisamos los arcos de J desde el arco que se desharia en delante
                revisionRutaK: //Etiqueta para luego salir del loop
                for(int temK = j + 1; temK < edgesJ.size() ; temK++){
                    Edge newEdgeK = edgesJ.get(temK);
                    double newEOS1 = (eoSCustomerK_1 + distanceKJ_after) + wtk + newEdgeK.getCustomer1().getServiceTime();
                    if(newEOS1 + newEdgeK.getDistance() > newEdgeK.getCustomer2().getTimeWindowEnd()){
                        //*********************************** En caso de que alguna ventana de tiempo se viole
                        return false;
                    }else{
                        if(newEdgeK.getCustomer2().getTimeWindowStart() > newEOS1 + newEdgeK.getDistance()){
                            wtk = newEdgeK.getCustomer2().getTimeWindowStart()  - (newEOS1 + newEdgeK.getDistance());
                            break revisionRutaK;
                        }else{
                            wtk = 0;
                        }
                        
                        eoSCustomerK_1 = newEOS1;
                        distanceKJ_after = newEdgeK.getDistance();

                    }
                }
              }   
              return true;
          }  
          
          
      }
    

    @Override
    public int getNextElement(VehicleRoutingProblem problem) {
        
        
        int capacity = problem.getCapacity();
        Customer depot = problem.getDepot();
        List<Route> routes = problem.getRoutes();
         
        ListIterator<Route> iteratorRoutes;
        iteratorRoutes = routes.listIterator();
        routes.listIterator();
        
        Route routeJ;
        Edge edgeJ;
        Route routeK;
        Edge edgeK;
        int cantRoutes =routes.size();
        
        //-------------------------
        //-- Variables para control de que arcos se van a cambiar
        double savings = 0;
        Route selRutaJ = null;
        int selIndexEdgeJ = 0;
        Route selRutaK = null;
        int selIndexEdgeK  = 0 ;
        //-------------------------------------------------------
        
        //Si son 5 rutas, vamos a revisar de la 0 a la 3, compararemos 
        //0 con 1, 1 con 2, 2 con 3 y 3 con 4, abarcando todas las posibles combinaciones de esta forma
        
        for(int i = 0; i < (cantRoutes -1 );i++){
            routeJ = routes.get(i);
            List<Edge> edgesJ = routeJ.getEdges();
            int cantEdgesJ = edgesJ.size();
            
            //Revisamos cada arco de la ruta J
            for(int j=0 ; j< (cantEdgesJ); j++){
                edgeJ = edgesJ.get(j);
                
                //Comparamos ese arco con cada ruta K
                for(int rK = i+1; rK < (cantRoutes); rK ++){
                    routeK = routes.get(rK);
                    List<Edge> edgesK = routeK.getEdges();
                    int cantEdgesK = edgesK.size();
                    
                    //Dentro de cada ruta K, comparamos con todos los arcos de la ruta
                    for(int k = 0; k< cantEdgesK ; k++){
                        edgeK = edgesK.get(k);
                        
                         
                        //revisamos si el intercambio nos traeria algun beneficio
                        double candSavings = haveSavings(edgeJ, edgeK);
                        if(candSavings > savings){
                            
                            //Si nos da algun beneficio, revisamos si es posible hacerse
                            
                            if( feasibleToDo(routeJ,j, routeK, k) ){
                                //En caso de que sea posible, guardamos que arcos son los que se pueden cambiar y el ahorro que nos daria (para despues hacer el mejor cambio posible
                                  
                                    savings = candSavings;
                                    selRutaJ = routeJ ;
                                    selIndexEdgeJ = j;
                                    selRutaK = routeK;
                                    selIndexEdgeK = k;
                                    //Guardamos los datos de las rutas y los arcos con mayor ahorro de ruta :) 
                                  
                                
                            }
                        }
                            
                            
                        
                    }
                    
                }
            }

            
        }
         
            
            //Se revisa que se haya encontrado algun arco y se toma el mejor encontrado.
            //Despues se realiza el cambio y se reajustan ambas rutas que se modificaron.
            
            //return 1 si se encontro algun 
            //return 0 si no se encuentra nada 
       
       if(savings > 0){
            Route tempRouteJ = new Route(depot, capacity);
            Route tempRouteK = new Route(depot, capacity);
            List<Edge> listaArcosJ = selRutaJ.getEdges();
            List<Edge> tempEdgesJ = tempRouteJ.getEdges();
            double distanceJ = 0;
           // for(int xy = 0; xy < listaArcosJ.size(); xy++){
             for(int xy = 0; xy < selIndexEdgeJ; xy++){
                tempRouteJ.insertCustomer(listaArcosJ.get(xy).getCustomer2());
                Edge newEdgeJ = listaArcosJ.get(xy);
                tempEdgesJ.add(newEdgeJ);
                distanceJ += newEdgeJ.getDistance() + newEdgeJ.getWaitingTime()  + newEdgeJ.getCustomer2().getServiceTime(); 
                tempRouteJ.setDistance(distanceJ);
                
            }
            
             
            List<Edge> listaArcosK = selRutaK.getEdges();
            List<Edge> tempEdgesK = tempRouteK.getEdges();
            double distanceK = 0; 
             
             for(int xyz = 0; xyz < selIndexEdgeK; xyz++){
                tempRouteK.insertCustomer(listaArcosK.get(xyz).getCustomer2());
                Edge newEdgeK = listaArcosK.get(xyz);
                tempEdgesK.add(newEdgeK);
                distanceK += newEdgeK.getDistance() + newEdgeK.getWaitingTime()  + newEdgeK.getCustomer2().getServiceTime(); 
                tempRouteK.setDistance(distanceK);
                
            }
             
            Customer customerJ_1 = listaArcosJ.get(selIndexEdgeJ).getCustomer1();
            Customer customerJ_2 = listaArcosJ.get(selIndexEdgeJ).getCustomer2();
            
            Customer customerK_1 = listaArcosK.get(selIndexEdgeK).getCustomer1();
            Customer customerK_2 = listaArcosK.get(selIndexEdgeK).getCustomer2();

            double distanceJK_after = getDistanceFromTo(customerJ_1, customerK_2);
            double distanceKJ_after = getDistanceFromTo(customerK_1, customerJ_2);
            
             double wtj;
              double wtk; 
              if((listaArcosJ.get(selIndexEdgeJ).getEndOfServiceCustomer1() + distanceJK_after) < customerK_2.getTimeWindowStart()){
                  wtj = customerK_2.getTimeWindowStart() - (listaArcosJ.get(selIndexEdgeJ).getEndOfServiceCustomer1() + distanceJK_after);
                   
              }else{
                  wtj = 0;
              }
              
              if((listaArcosK.get(selIndexEdgeK).getEndOfServiceCustomer1() + distanceKJ_after) < customerJ_2.getTimeWindowStart()){
                  wtk = customerJ_2.getTimeWindowStart() - (listaArcosK.get(selIndexEdgeK).getEndOfServiceCustomer1() + distanceKJ_after) ;
              
              }else{
                  wtk = 0;
              }
            //Agregamos a la ruta J el nuevo arco
            tempRouteJ.insertCustomer(customerK_2);
            Edge newEdgeJ = new Edge(customerJ_1, customerK_2, tempRouteJ, customerK_2.getDemand(), distanceJK_after,listaArcosJ.get(selIndexEdgeJ).getEndOfServiceCustomer1() , wtj);
            tempEdgesJ.add(newEdgeJ);
            distanceJ += newEdgeJ.getDistance() + newEdgeJ.getWaitingTime()  + newEdgeJ.getCustomer2().getServiceTime(); 
            tempRouteJ.setDistance(distanceJ);
            //Edge newEdgeJ = 
            
            //Agregamos a la ruta K el nuevo arco
            tempRouteK.insertCustomer(customerJ_2);
            Edge newEdgeK = new Edge(customerK_1, customerJ_2, tempRouteK, customerJ_2.getDemand(), distanceKJ_after,listaArcosK.get(selIndexEdgeK).getEndOfServiceCustomer1() , wtk);
            tempEdgesK.add(newEdgeK);
            distanceK += newEdgeK.getDistance() + newEdgeK.getWaitingTime()  + newEdgeK.getCustomer2().getServiceTime(); 
            tempRouteK.setDistance(distanceK);
            
            //Incertamos a la ruta J la cola de la ruta K
            double newEoSJK = newEdgeJ.getEndOfServiceCustomer1() + wtj + newEdgeJ.getDistance() + newEdgeJ.getCustomer2().getServiceTime();
            for(int xy = selIndexEdgeK + 1; xy < listaArcosK.size(); xy++){
                tempRouteJ.insertCustomer(listaArcosK.get(xy).getCustomer2());
               // Edge newEdgeJK = listaArcosK.get(xy);
                
                               if((newEoSJK+ listaArcosK.get(xy).getDistance()) < listaArcosK.get(xy).getCustomer2().getTimeWindowStart()){
                                    wtj = listaArcosK.get(xy).getCustomer2().getTimeWindowStart() - (newEoSJK+ listaArcosK.get(xy).getDistance()) ;

                                }else{
                                    wtj = 0;
                                }
                Edge someNewEdgeJK = new Edge(listaArcosK.get(xy).getCustomer1(), listaArcosK.get(xy).getCustomer2(), listaArcosK.get(xy).getRoute(), listaArcosK.get(xy).getDemand(), listaArcosK.get(xy).getDistance(), newEoSJK, wtj);
                newEoSJK = someNewEdgeJK.getDistance() + someNewEdgeJK.getEndOfServiceCustomer1() + wtj + someNewEdgeJK.getCustomer2().getServiceTime();
                
                tempEdgesJ.add(someNewEdgeJK);
                distanceJ += someNewEdgeJK.getDistance() + someNewEdgeJK.getWaitingTime()  + someNewEdgeJK.getCustomer2().getServiceTime(); 
                tempRouteJ.setDistance(distanceJ);
                
            } 
            
            //Inertamos a la ruta K la cola de la anterior ruta J 
            double newEoSKJ = newEdgeK.getEndOfServiceCustomer1() + wtk + newEdgeK.getDistance() + newEdgeK.getCustomer2().getServiceTime();
            for(int xy = selIndexEdgeJ + 1; xy < listaArcosJ.size(); xy++){
                tempRouteK.insertCustomer(listaArcosJ.get(xy).getCustomer2());
               // Edge newEdgeKJ = listaArcosJ.get(xy);
                              if((newEoSKJ+ listaArcosJ.get(xy).getDistance()) < listaArcosJ.get(xy).getCustomer2().getTimeWindowStart()){
                                    wtk = listaArcosJ.get(xy).getCustomer2().getTimeWindowStart() - (newEoSKJ+ listaArcosJ.get(xy).getDistance()) ;

                                }else{
                                    wtk = 0;
                                }
                Edge someNewEdgeKJ = new Edge(listaArcosJ.get(xy).getCustomer1(), listaArcosJ.get(xy).getCustomer2(), listaArcosJ.get(xy).getRoute(), listaArcosJ.get(xy).getDemand(), listaArcosJ.get(xy).getDistance(), newEoSKJ, wtk);
                newEoSKJ = someNewEdgeKJ.getDistance() + someNewEdgeKJ.getEndOfServiceCustomer1() + wtk + someNewEdgeKJ.getCustomer2().getServiceTime();
                tempEdgesK.add(someNewEdgeKJ);
                distanceK += someNewEdgeKJ.getDistance() + someNewEdgeKJ.getWaitingTime()  + someNewEdgeKJ.getCustomer2().getServiceTime(); 
                tempRouteK.setDistance(distanceJ);
                
            }
            
            problem.getRoutes().remove(selRutaJ);
            problem.getRoutes().remove(selRutaK);
            
            
            
            if(tempRouteJ.getEdges().size() > 1)
                problem.getRoutes().add(tempRouteJ);
            
            if(tempRouteK.getEdges().size() > 1)
                problem.getRoutes().add(tempRouteK);
            
         
            return 1;
       }else{
          // System.out.println("Ningun cambio posible");
            return 0;
       }
        
        
    }

    @Override
    public String toString() {
        return "2OPT";
        
    }
    
 

    
   private double getDistanceFromTo(Customer customerOrigin, Customer customerDestiny){
   
       double xCoord = Math.abs( customerDestiny.getxCoord() - customerOrigin.getxCoord() );
       double yCoord = Math.abs( customerDestiny.getyCoord() - customerOrigin.getyCoord() );
       double distance = Math.sqrt((xCoord *  xCoord) + (yCoord * yCoord));
       
       return distance;
       
   }
   
   /*
     private boolean checkFeasibility(  List<Edge> edges, int index, Customer customer){
      
      Edge newEdge = edges.get(index);
      
      //bj   es el tiempo en el que comenzaba originalmente el servicio del cliente 2 en el arco seleccionado para insercion
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
