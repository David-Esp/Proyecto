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
public class Problem_Analizer {
  
    /**
     *
     * @param problem
     * @return
     */
    public String solution(VehicleRoutingProblem problem){
        
            
        List<Route> rutas = problem.getRoutes();    
        double totalDistancia = 0;
        double totalTiempo = 0;
        
            for (Route ruta : rutas) {
                 
                //totalDistancia += ruta.getDistance();
                totalDistancia += calculaDistanciaRuta(ruta);
               // totalTiempo += ruta.getTime();
            }
            
        StringBuilder string;
        string = new StringBuilder();
       
        
       // string.append("Cantidad de Rutas: ").append(rutas.size()).append("\n");
        string.append(" ").append(rutas.size()).append(" ");
       // string.append("Total de distancia Recorrida: ").append(totalDistancia).append("\n");
        string.append(" ").append(totalDistancia).append(" ");
       // string.append("Total de tiempo recorrido: ").append(totalTiempo);
             
        return string.toString().trim();
  
        
    }
        
    /**
     *
     * @param ruta
     * @return
     */
    public double calculaDistanciaRuta(Route ruta){
        double dRuta = 0;
        List<Edge> arcos = ruta.getEdges();
        
        for (Edge arco : arcos) {
            
            double dis = getDistanceFromTo(arco.getCustomer1(), arco.getCustomer2());
            
            if(dis != arco.getDistance()){
                System.out.println("Some diff " + arco.toString());
            }else{
               // System.out.println("good!");
            }
            
            dRuta += dis;
                    
        }
        
        
        
        return dRuta;
    }    
     
       private double getDistanceFromTo(Customer customerOrigin, Customer customerDestiny){
   
       double xCoord = Math.abs( customerDestiny.getxCoord() - customerOrigin.getxCoord() );
       double yCoord = Math.abs( customerDestiny.getyCoord() - customerOrigin.getyCoord() );
       double distance = Math.sqrt((xCoord *  xCoord) + (yCoord * yCoord));
       
       return distance;
       
   }
}
