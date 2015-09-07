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
    
    private boolean esPosible(Route rutaJ, int j, Customer customerCan){
         
        
          //Si la capacidad total del vehiculo es excedida por alguna de las nuevas rutas, no es factible hacer el cambio
          if(rutaJ.getVehicleCapacity() < customerCan.getDemand()){
            return false;
          }
        
       
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
  
              if(wtk == 0){
              //--------------------------------------------------
              //-----------Ruta J---------------------------------
              //--------------------------------------------------
                //En esta ruta revisamos los arcos de K desde el arco que se desharia en delante
                revisionRutaK: //Etiqueta para luego salir del loop
                for(int temK = j + 1; temK < edgesJ.size() ; temK++){ //Aqui va bien
                    Edge newEdgeK = edgesJ.get(temK);
                    double newEOS1 = (eoSCustomerJ_2 + distanceKJ_after) + wtk + newEdgeK.getCustomer1().getServiceTime();
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
         
        ListIterator<Route> iteratorRoutes;
        iteratorRoutes = routes.listIterator();
        routes.listIterator();
        
        
        //--Info necesaria para hacer el movimiento 
        Route routeJ_rel = null;
        int indexEdgeJ_rel = 0 ;
        Route routeK_rel = null;
        int indexCustomerK_rel = 0; 
        //------------------------
        
      //  int cantRoutes =routes.size();
        double ahorro = 0;
        //En este caso es mejor irnos ruta por ruta, revisando en cada ruta si sus clientes pueden ponerse en los arcos de las rutas siguientes
        //O tambien si los clientes de las otras rutas pueden agregarse a alguno de sus arcos 
        for(int x = 0 ; x < routes.size(); x++){
            Route rutaA = routes.get(x);
            List<Edge> arcos = rutaA.getEdges();
            List<Customer> clientes = rutaA.getCustomers();
            
            for(int xe=0;xe < arcos.size(); xe++){
                Edge edgeCan = arcos.get(xe);
                
                for(int x2 = (x+1); x2<routes.size(); x2 ++){
                    Route rutaB = routes.get(x2);
                    List<Customer> clientesCand = rutaB.getCustomers();
                    
                    for(int xc2=1;xc2 <(clientesCand.size() - 1)  ; xc2++){
                        Customer customerCan = clientesCand.get(xc2);
                        double ahorroCan = tieneAhorro(edgeCan,customerCan, clientesCand.get(xc2-1), clientesCand.get(xc2+1) );
                        if( ahorroCan > 0){
                           // ahorro = ahorroCan;
                           // System.out.println("Ahorro " + ahorro);
                            //Guardar cuanto ahorro tiene Y en caso de ser Mayor que el anterior guardado, revisamos feasibility
                            //System.out.println("Es casi " + rutaA +  " del arco " + xe + " el cliente " + customerCan );
                            if( esPosible(rutaA, xe, customerCan) ){
                                // System.out.println("Es factible1" + rutaA +  " del arco " + xe + " el cliente " + customerCan );
                                if( ahorroCan > ahorro){
                                    ahorro = ahorroCan;
                                   // System.out.println("Es factible2" + rutaA +  " del arco " + xe + " el cliente " + customerCan );
                                     routeJ_rel = rutaA;
                                     indexEdgeJ_rel = xe;
                                     routeK_rel = rutaB;
                                     indexCustomerK_rel = xc2 ;
                                     
                                     System.out.println("Es factible 222" + routeJ_rel +  " del arco " + indexEdgeJ_rel + " el cliente  de la ruta " + routeK_rel + " cliente " + indexCustomerK_rel );
                                        esPosible(rutaA, xe, customerCan);
                                }
                                //Entonces se guardan todos los parametros para hacer el cambio despues 
                            }
                        }
                    }
                    
                }
                
                
            }
            
            for(int xc=1; xc< clientes.size()-1; xc++ ){
                Customer customerCan = clientes.get(xc);
                
                for(int x2 = (x+1); x2<routes.size(); x2 ++){
                    Route rutaB = routes.get(x2);
                    List<Edge> edgesCan = rutaB.getEdges();
                    
                    for(int xe=0;xe <(edgesCan.size() - 1)  ; xe++){
                        Edge edgeCan = edgesCan.get(xe);
                        double ahorroCan = tieneAhorro(edgeCan,customerCan, clientes.get(xc-1), clientes.get(xc+1) );
                     //  System.out.println("Ahorro 22 " + ahorroCan);
                        if( ahorroCan > 0){
                           // System.out.println("Es casi " + rutaA +  " del arco " + xe + " el cliente " + customerCan );
                           // ahorro = ahorroCan;
                           // System.out.println("Ahorro 2 " + ahorro);
                            //Guardar cuanto ahorro tiene Y en caso de ser Mayor que el anterior guardado, revisamos feasibility
                           // System.out.println("Es casi2 " + rutaB +  " del arco " + xe + " el cliente " + customerCan );
                            if( esPosible(rutaB, xe, customerCan) ){
                              //  System.out.println("Es factible3" + rutaB +  " del arco " + xe + " el cliente " + customerCan );
                                if( ahorroCan > ahorro){
                                    ahorro = ahorroCan;
                                   // System.out.println("Es factible4" + rutaB +  " del arco " + xe + " el cliente " + customerCan );
                                    
                                    //Tenemos que guardar La ruta a al que se le va a agregar el cliente, y su arco,
                                    //Guardar la ruta donde se va a quitar el cliente, el cliente y los arcos que se van a remover
                                     routeJ_rel = rutaB;
                                     indexEdgeJ_rel = xe;
                                     routeK_rel = rutaA;
                                     indexCustomerK_rel = xc ;
                                     
                                     System.out.println("Es factible 221" + routeJ_rel +  " del arco " + indexEdgeJ_rel + " el cliente  de la ruta " + routeK_rel + " cliente " + indexCustomerK_rel );
                                     esPosible(rutaB, xe, customerCan);
                                }
                                //Entonces se guardan todos los parametros para hacer el cambio despues 
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
       
       if(ahorro > 0){
           
           //Arreglamos la ruta donde se arregla el cliente
           
          List<Customer> listaCustomersK = routeK_rel.getCustomers();
          Customer customerK_rel = listaCustomersK.get(indexCustomerK_rel);
          
          List<Edge> listaArcosJ = routeJ_rel.getEdges();
          Edge arcoJ_rel = listaArcosJ.get(indexEdgeJ_rel); 
          
          double distanceJ_K_rel = getDistanceFromTo(customerK_rel, arcoJ_rel.getCustomer1());
          double distanceK_J_rel = getDistanceFromTo(customerK_rel, arcoJ_rel.getCustomer2());
          
          double eosCJ = arcoJ_rel.getEndOfServiceCustomer1();
          
          double wtj; 
          if((eosCJ + distanceJ_K_rel) < customerK_rel.getTimeWindowStart()){
            wtj = customerK_rel.getTimeWindowStart() - (eosCJ + distanceJ_K_rel);         
          }else{
            wtj = 0;
          }
          double eosCK = eosCJ + wtj + distanceJ_K_rel + customerK_rel.getServiceTime();
          
          double wtk; 
          if((eosCK + distanceK_J_rel) < arcoJ_rel.getCustomer2().getTimeWindowStart()){
            wtk = arcoJ_rel.getCustomer2().getTimeWindowStart() - (eosCK + distanceK_J_rel);         
          }else{
            wtk = 0;
          }     
           Edge tempEdge1 = new Edge(arcoJ_rel.getCustomer1(), customerK_rel, routeJ_rel, customerK_rel.getDemand(), distanceJ_K_rel, eosCJ, wtj);
          
           Edge tempEdge2 = new Edge(customerK_rel, arcoJ_rel.getCustomer2(), routeJ_rel, arcoJ_rel.getCustomer2().getDemand(), distanceK_J_rel, eosCK, wtk);
           
           listaArcosJ.remove(indexEdgeJ_rel);
           listaArcosJ.add(indexEdgeJ_rel, tempEdge2);
           listaArcosJ.add(indexEdgeJ_rel, tempEdge1);
           //listaCustomersJ.add(indexEdgeJ_rel + 1, customerK_rel);
           routeJ_rel.insertCustomerAt(customerK_rel , indexEdgeJ_rel + 1); 
           
           double newEosCJ = tempEdge2.getEndOfServiceCustomer1()+ tempEdge2.getDistance() + tempEdge2.getWaitingTime() + tempEdge2.getCustomer2().getServiceTime();
           
           for( int m = indexEdgeJ_rel +2 ; m < listaArcosJ.size(); m++){
              Edge someE = listaArcosJ.get(m);
              someE.setEndOfServiceCustomer1(newEosCJ);
              double newWtJ;
              if((newEosCJ + someE.getDistance()) < someE.getCustomer2().getTimeWindowStart()){
                newWtJ = someE.getCustomer2().getTimeWindowStart() - (newEosCJ + someE.getDistance());         
              }else{
                newWtJ = 0;
              }  
              someE.setWaitingTime(newWtJ);
              newEosCJ = newEosCJ + newWtJ + someE.getDistance() + someE.getCustomer2().getServiceTime();
           }
           
           
           //Arreglamos la ruta a la que se le quita el cliente
 
           List<Edge> listaArcosK = routeK_rel.getEdges();
           Edge arcoK1 = listaArcosK.get(indexCustomerK_rel - 1);
           Edge arcoK2 = listaArcosK.get(indexCustomerK_rel);
           
           double newDistanceK = getDistanceFromTo(arcoK1.getCustomer1(), arcoK2.getCustomer2());
           double distSav = arcoK1.getDistance() + arcoK2.getDistance() - newDistanceK;
           double newWtk;
           double eoSC1 = arcoK1.getEndOfServiceCustomer1();
           if((eoSC1 + newDistanceK) < arcoK2.getCustomer2().getTimeWindowStart() ){
               newWtk = arcoK2.getCustomer2().getTimeWindowStart() - arcoK2.getCustomer2().getTimeWindowStart() ;
           }else{
               newWtk = 0;
           }
           Edge newArcoK = new Edge(arcoK1.getCustomer1(), arcoK2.getCustomer2(), routeK_rel, arcoK2.getDemand(), newDistanceK, eoSC1, newWtk);
           
           listaArcosK.remove(arcoK1);
           listaArcosK.remove(arcoK2);
           listaArcosK.add(indexCustomerK_rel -1, newArcoK);
           
           routeK_rel.getCustomers().remove(customerK_rel);
           routeK_rel.setDistance(routeK_rel.getDistance() - distSav);
           routeK_rel.setDemand(routeK_rel.getDemand() - customerK_rel.getDemand());
           routeK_rel.setVehicleCapacity(routeK_rel.getVehicleCapacity() + (int)customerK_rel.getDemand());
           
           double newEosCK = eoSC1 + newDistanceK + newWtk + newArcoK.getCustomer2().getServiceTime();
           
           for( int m = indexCustomerK_rel -1 ; m < listaArcosK.size(); m++){
              Edge someE = listaArcosK.get(m);
              someE.setEndOfServiceCustomer1(newEosCK);
              double newWtK;
              if((newEosCK + someE.getDistance()) < someE.getCustomer2().getTimeWindowStart()){
                newWtK = someE.getCustomer2().getTimeWindowStart() - (newEosCK + someE.getDistance());         
              }else{
                newWtK = 0;
              }  
              someE.setWaitingTime(newWtK);
              newEosCK = newEosCK + newWtK + someE.getDistance() + someE.getCustomer2().getServiceTime();
           }        
           
            System.out.println("doneChange");
            return 1;
       }else{
           System.out.println("Ningun cambio posible");
            return 0;
       }
   
       

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
