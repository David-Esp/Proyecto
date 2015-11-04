/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vrp.Solvers;

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
public class Solution_Tester {
    
    /**
     *
     * @param problem
     * @return
     */
    public boolean solution(VehicleRoutingProblem problem){
        
        List<Route> rutas = problem.getRoutes();
        ListIterator<Route> iterator;
        iterator = rutas.listIterator();
        Route route;
        
         while (iterator.hasNext()) {
            route = iterator.next();
            
            List<Edge> arcos = route.getEdges();
            ListIterator<Edge> iteratorA;
            iteratorA = arcos.listIterator();
            Edge edge;
            double currentTime = 0;
            double currentTimeEndOfService = 0;
            double waitingTime = 0;
            double distanceIJ = 0;
            int contador  = -1;
            
            while (iteratorA.hasNext()){
                contador ++;
                edge = iteratorA.next();
                
                distanceIJ = getDistanceFromTo(edge.getCustomer1(), edge.getCustomer2()) ;
                currentTime = distanceIJ + currentTimeEndOfService ;
                
                
                //En caso que la ventana de tiempo del cliente 2 sea menor a cuando llegue el vehiculo despues de atender al cliente 1
                if(edge.getCustomer2().getTimeWindowEnd() < currentTime){
                    return false;
                }else{
                    waitingTime = getWaitingTime(edge.getCustomer2(), currentTimeEndOfService, distanceIJ);
                    currentTimeEndOfService = currentTime + waitingTime + edge.getCustomer2().getServiceTime();
                }
                
                
                
                
            }
            
           
         }
        
          return true;
       
        
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
    
}
