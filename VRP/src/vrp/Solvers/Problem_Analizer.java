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
  
        public String solution(VehicleRoutingProblem problem){
        
            
        List<Route> rutas = problem.getRoutes();    
        double totalDistancia = 0;
        double totalTiempo = 0;
        
            for (Route ruta : rutas) {
                 
                totalDistancia += ruta.getDistance();
                totalTiempo += ruta.getTime();
            }
            
        StringBuilder string;
        string = new StringBuilder();
       
        
        string.append("Cantidad de Rutas: ").append(rutas.size()).append("\n");
        string.append("Total de distancia Recorrida: ").append(totalDistancia).append("\n");
        string.append("Total de tiempo recorrido: ").append(totalTiempo);
             
        return string.toString().trim();
  
        
    }
     
}
