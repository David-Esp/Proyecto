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
import vrp.Problem.VehicleRoutingProblem;

/**
 *
 * @author David
 */
public class Heuristic_2OPT extends VRPHeuristic{

    @Override
    public int getNextElement(VehicleRoutingProblem problem) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toString() {
        return "2-OPT*";
        
    }
    
 

    
   private double getDistanceFromTo(Customer customerOrigin, Customer customerDestiny){
   
       double xCoord = Math.abs( customerDestiny.getxCoord() - customerOrigin.getxCoord() );
       double yCoord = Math.abs( customerDestiny.getyCoord() - customerOrigin.getyCoord() );
       double distance = Math.sqrt((xCoord *  xCoord) + (yCoord * yCoord));
       
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
