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
public class Heuristic_I1 extends VRPHeuristic{
    
        /**
     * Creates a new instance of <code>MaxValue</code>.
     */
    public Heuristic_I1() {
    }
    
         
    @Override
    public int getNextElement(VehicleRoutingProblem problem) {
 
        
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
        if(iterator.hasNext()){
            
            //Revisas si no hay ninguna ruta creada
            if(routes.isEmpty()){
                //Agregas una nueva ruta que parte del deposito, con la capacidad total del vehiculo
                Route route = new Route(depot, capacity);
                //Agregamos la ruta a la lista de rutas
                routes.add(route);
            }else{
                //Si ya hay rutas, revisas la ultima ruta que esta siendo utilizada
                Route lastRoute = routes.get(routes.size() -1 );
                
                //----------------------------------------------------
                //Aqui cambia con respecto a las heuristicas anteriores
                //-----------------------------------------------------
                 
                //Si la ruta ya no tiene capadidad disponible
                if(lastRoute.getDemand() == capacity)
                {
                    //Agregamos una nueva Ruta
                    Route route = new Route(depot, capacity);
                    routes.add(route);
                    //Actualizamos variable lastRoute 
                }        
              
            }
            
            
            
            //Actualizamos la ultima ruta que estamos utilizando, en caso de haber creado una nueva
            Route lastRoute = routes.get(routes.size() -1 );
            List<Edge> ed = lastRoute.getEdges();
            
            
            
            //---- 
            //Variables que se utilizan para revisar los clientes y asignar valores 
            
            double distanciaRecorrida = lastRoute.getDistance();
            Customer lastCustomer = lastRoute.getCustomers().get(lastRoute.getCustomers().size()-1);
            
            //Buscamos a un cliente que se pueda agregar a la ruta que tenemos disponible
            int availableCapacity = lastRoute.getVehicleCapacity();
            Customer customer;
                
                
            double nextFound = -1; 
            double bestFound = 1000000;
            int count = -1;
            int coin = -1;
            Customer candidateCustomer = null;
            Edge edge = null;
            
            //Si la ruta es nueva (solo contiene al deposito)
            if(lastRoute.getCustomers().size() == 1){
            //Se agrega el cliente sin ruta con el due time mas proximo
            
           
                //revisamos todos los clientes que tenemos sin ruta
                while (iterator.hasNext()) {
                    customer = iterator.next();
                    count ++;

                    nextFound = customer.getTimeWindowEnd();

                    //Si la suma es menor a la mejor encontrada
                    if(bestFound > nextFound){
                        //Agregamos a ese cliente como un cliente candidato y actualizamos el valor del mejor encontrado
                        candidateCustomer = customer;
                        bestFound = nextFound;  
                        coin = count;
                        }
                }
            
                //Forzosamente se va a encontrar un cliente para una ruta nueva
                //Se inserta el cliente a la ruta
                lastRoute.insertCustomer(candidateCustomer);
                
             
                    
                 //Se calculan valores necesarios   
                double distancia = getDistanceFromTo(candidateCustomer, depot);
                     
                //   double beginOfServiceJ;
                double waitingTimeJ;
                    
                //Si el vehiculo llega despues o cuando inicia la ventana de tiempo del cliente j
                if((distancia) >= candidateCustomer.getTimeWindowStart() ){
                    waitingTimeJ = 0;
                    //  beginOfServiceJ = (distanciaRecorrida + distancia) ;
                         
                }else{
                    //Si llega antes se asigna un tiempo de espera a que inicie la ventana de tiempo
                    waitingTimeJ = candidateCustomer.getTimeWindowStart() - (distancia);
                    //  beginOfServiceJ = customer.getTimeWindowStart() ;
                }
                
                
                //Se crean los arcos que se van a agregar deposito al nuevo cliente
                edge = new Edge(depot, candidateCustomer, lastRoute, candidateCustomer.getDemand(), distancia , distanciaRecorrida, waitingTimeJ );
                //Se agrega el arco creado a la lista de arcos
                edges.add(edge);
                ed.add(edge);
                
                //Se actualiza la distancia recorrida (distancia + tiempo de servicio) 
                //Esta faltando el tiempo de espera (si es que hay)
                distanciaRecorrida = distanciaRecorrida + edge.getDistance() + edge.getWaitingTime()  + edge.getCustomer2().getServiceTime();
                lastRoute.setDistance(distanciaRecorrida);
               

                //Se quita el cliente de la lista de clientes disponibles 
                customers.remove(coin);
                addedCustomers.add(candidateCustomer);
                
                
                //Se crean el arco del cliente al deposito (se cierra la subruta)
                edge = new Edge(candidateCustomer, depot, lastRoute, 0, distancia , distanciaRecorrida, 0 );
                 
                //Se agrega el arco creado a la lista de arcos
                edges.add(edge);
                ed.add(edge);
                //Como esta al depot la distanciaRecorrida solamente consiste de la distancia recorrida que se tenia mas la distancia al depot
                distanciaRecorrida = distanciaRecorrida + edge.getDistance();
                lastRoute.setDistance(distanciaRecorrida);
                lastRoute.insertCustomer(depot);
                
            }
            //En caso de que ya se tenga una subruta construida
            else{
                
                double minC2 = 999999999;
                double c2 = 999999;
                count = -1;
                coin = -1;
                int coinEE = -1;
                
                 while (iterator.hasNext()) {
                    customer = iterator.next();
                    count ++;
                    
                    
                    
                    
                    
                    int countRute = 0;
                    double bestSoFar = 9999999;
                    Edge newEdge;
                    
                    //ed.size();
                    ListIterator<Edge> iteratorEdge;
                    iteratorEdge = ed.listIterator();

                    
                    double minC12 = 9999999;
                    double c12 = 9999999;
                    int coinE = -1;
                    int countE = -1;    
                        
                if(customer.getDemand() <= availableCapacity){
                    //----------------------------------------------------------
                    //-----------------------C1---------------------------------
                    //----------------------------------------------------------
                    //Se revisan todos los arcos dodne puede insertarse el cliente
                    
                    while(iteratorEdge.hasNext()){
                        
                        countE++;
                        newEdge = iteratorEdge.next();
                        double bj = 0;
                        double bju = 0;
                        double bu = 0;
                        double wu = 0 ;
                        double wj = 0;
                        double eoSu = 0;
                        
                        
                        
                        //Cuenta el fin del servicio del cliente anterior + la distancia entre ambos + el tiempo de espera
                        bj = newEdge.getEndOfServiceCustomer1() + newEdge.getDistance() + newEdge.getWaitingTime();
                        
                        double distanceIU = getDistanceFromTo(newEdge.getCustomer1(), customer);
                        bu = newEdge.getEndOfServiceCustomer1() + distanceIU ;
                        if( bu >= customer.getTimeWindowStart()){
                            wu = 0;
                        }else{
                            wu = customer.getTimeWindowStart() - bu;    
                        }
                        
                        //Tiempo que termina el servicio en u
                        eoSu = bu + wu + customer.getServiceTime();
                        
                        double distanceUJ = getDistanceFromTo(customer, newEdge.getCustomer2());
                         
                        if( (eoSu + distanceUJ)  >= newEdge.getCustomer2().getTimeWindowStart()){
                            wj = 0;
                        }else{
                            wj = newEdge.getCustomer2().getTimeWindowStart() - (eoSu + distanceUJ);    
                        }
                        
                        bju = eoSu + distanceUJ + wj;
                        
                        //bju - bj
                        //bju = nuevo tiempo de servicio en el que empieza j dado que u esta en la ruta
                        //Si no es factlible se designa un valor que no se va a elegir
                      
                        //Revisamos la factibilidad de insertar el cliente (revisando todos los arcos de la ruta :s
                        List<Edge> x = lastRoute.getEdges();
                        
                        
                        if(checkFeasibility2(x, countE, customer))
                        {c12 = bju - bj;
                        }else
                        {c12 = 9999999;   
                        }     
                       /*     
                         if(!checkFeasibility(bu, bju, x, countE, customer)){ 
                        //if( bju > newEdge.getCustomer2().getTimeWindowEnd()  || bu > customer.getTimeWindowEnd() ){
                            c12 = 9999999;
                        }else{
                            c12 = bju - bj;
                        }
                        */
                            
                            
                        if(c12 < minC12){
                            minC12 = c12;
                            coinE = countE;
                        }
                         /*
                        if(minC12 > c12){
                            minC12 = c12;
                            coinE = countE;
                        }  
                        */
                    }
                     
                    
               
                    //----------------------------------------------------------
                    //-----------------------C2---------------------------------
                    //----------------------------------------------------------
                    //Se revisan todos los clientes para encontrar el mejor
                    
                    if(coinE >= 0){
                        //Lambda =  2 // C2 = lambda*d0u - C1
                        c2 = Math.abs(((2 * getDistanceFromTo(depot, customer))) - Math.abs(minC12));
                        
                        if(minC2 > c2){
                            minC2 = c2;
                            coin = count;
                            coinEE = coinE;
                        } 
                    }
                    
                }
                
                
              }      
                 //-------------------------------------------------------------
                 //---Despes de encontrar al cliente y la localizacion en la ruta, se pasa a insertarlo
                 //-------------------------------------------------------------
                 
                 //En caso de que no se haya encontrado ningun cliente para insertar dentro de la subruta
                 if(coin < 0){ 
                     //Se crea una nueva ruta, inicializandola con cliente cuyo do time sea el mas proximo
                     Route newRoute = new Route(depot, capacity);
                     routes.add(newRoute);
                     //Se podria inicializar la ruta, pero se puede dejar para la siguiente iteracion del algoritmo
                     
                 }else{
                 //En caso de que si se encuentre un cliente, se agrega donde se especificó
                     
                     candidateCustomer = customers.get(coin);
                     customers.remove(coin);
                     addedCustomers.add(candidateCustomer);
                     lastRoute.insertCustomerAt(candidateCustomer, coinEE + 1);
                     
                     Edge removedEdge = ed.get(coinEE);
                     double distance1  = getDistanceFromTo(removedEdge.getCustomer1(), candidateCustomer);
                     double wt1 = getWaitingTime(candidateCustomer,removedEdge.getEndOfServiceCustomer1()  , distance1);
                     Edge newEdge1 = new Edge(removedEdge.getCustomer1(), candidateCustomer, lastRoute, candidateCustomer.getDemand(), distance1, removedEdge.getEndOfServiceCustomer1(), wt1);
                     
                     double eos2 = removedEdge.getEndOfServiceCustomer1() + distance1 + candidateCustomer.getServiceTime() + wt1;
                     double distance2 = getDistanceFromTo(candidateCustomer, removedEdge.getCustomer2());
                    
                     double wt2 = getWaitingTime(removedEdge.getCustomer2(), eos2, distance2);
                     Edge newEdge2 = new Edge(candidateCustomer, removedEdge.getCustomer2(), lastRoute, removedEdge.getCustomer2().getDemand(),distance2, eos2, wt2);
                     
                     //Quitamos el arco que se pierde i-j
                     ed.remove(coinEE);
                     //Agregamos los dos nuevos arcos creados i-u, u-j
                      
                     ed.add(coinEE, newEdge2);
                     ed.add(coinEE, newEdge1);
                     
                    // ed = arreglarArcos(ed, coinEE);
                     
                     
                           
                           ListIterator<Edge> iteratorArreglarArcos;
                           iteratorArreglarArcos = ed.listIterator();
                           int counter  = -1;
                           Edge algunArco;

                           while(iteratorArreglarArcos.hasNext()){
                               counter ++;
                               algunArco = iteratorArreglarArcos.next();
                               if(counter > (coinEE+1)){
                                   Edge prevEdge = ed.get(counter - 1);
                                   double newEndServiceI = prevEdge.getDistance()+ prevEdge.getEndOfServiceCustomer1() + prevEdge.getWaitingTime()+prevEdge.getCustomer2().getServiceTime();
                                   double newWaitingTime;

                                   ed.get(counter).setEndOfServiceCustomer1(newEndServiceI);

                                   if((newEndServiceI + algunArco.getDistance()) >= algunArco.getCustomer2().getTimeWindowStart()){
                                       newWaitingTime = 0;
                                   }else{
                                       newWaitingTime = algunArco.getCustomer2().getTimeWindowStart() - (newEndServiceI + algunArco.getDistance());
                                   }
                                   ed.get(counter).setWaitingTime(newWaitingTime);
                               }

                           }
                           
                 }
           }   
                
           
           
        return 1; 
        }

        else{
            return 0;
            
        }
        
    }
    
    /**
     * Returns the string representation of this heuristic.
     * <p>
     * @return The string representation of this heuristic.
     */
     
    public String toString() {
        return "Solomon's Insertion Heuristic I1";
    }
    
    
 

    
   private double getDistanceFromTo(Customer customerOrigin, Customer customerDestiny){
   
       double xCoord = Math.abs( customerDestiny.getxCoord() - customerOrigin.getxCoord() );
       double yCoord = Math.abs( customerDestiny.getyCoord() - customerOrigin.getyCoord() );
       double distance = Math.sqrt((xCoord *  xCoord) + (yCoord * yCoord));
       
       return distance;
       
   }
   
   private double getWaitingTime( Customer customerDestiny, double endOfServiceI, double distance){
       
       double waitingTime = 0;
       if((endOfServiceI + distance) < customerDestiny.getTimeWindowStart() ){
           waitingTime = customerDestiny.getTimeWindowStart() - (endOfServiceI + distance);
       }
        
       return waitingTime;
       
   }
   
  private boolean checkFeasibility(double bu, double bju, List<Edge> edges, int index, Customer customer){
      
      Edge newEdge = edges.get(index);
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
  
  private List<Edge> arreglarArcos(List<Edge> ed, int index){
      List<Edge> edges = ed;
       //Edge newEdge = edges.get(index);
      ListIterator<Edge> iterator;
      iterator = edges.listIterator();
      int counter  = -1;
      Edge newEdge;
      
      while(iterator.hasNext()){
          counter ++;
          newEdge = iterator.next();
          if(counter > (index+1)){
              Edge prevEdge = edges.get(counter - 1);
              double newEndServiceI = prevEdge.getDistance()+ prevEdge.getEndOfServiceCustomer1() + prevEdge.getWaitingTime()+prevEdge.getCustomer2().getServiceTime();
              double newWaitingTime = 0;
              
              edges.get(counter).setEndOfServiceCustomer1(newEndServiceI);
              
              if((newEndServiceI + newEdge.getDistance()) >= newEdge.getCustomer2().getTimeWindowStart()){
                  newWaitingTime = 0;
              }else{
                  newWaitingTime = newEdge.getCustomer2().getTimeWindowStart() - (newEndServiceI + newEdge.getDistance());
              }
              edges.get(counter).setWaitingTime(newWaitingTime);
              
              
          }
          
      }
      
      return edges;
  }
  
  
    private boolean checkFeasibility2(  List<Edge> edges, int index, Customer customer){
      
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
