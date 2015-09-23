/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vrp;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import vrp.Problem.Customer;
import vrp.Problem.Route;
import vrp.Problem.VehicleRoutingProblem;
import vrp.Solvers.Problem_Analizer;
import vrp.Solvers.Solution_Tester;
import vrp.heuristics.Heuristic_2OPT;
import vrp.heuristics.Heuristic_CWDT;
import vrp.heuristics.Heuristic_I1;
import vrp.heuristics.Heuristic_NNH;
import vrp.heuristics.Heuristic_Relocate;

/**
 *
 * @author David
 */
public class Test {

   
    public static void imprimirGraficas(VehicleRoutingProblem problem){
        Writer writer = null;
        StringBuilder stringI1;
        stringI1 = new StringBuilder();
        
        double depotX = problem.getDepot().getxCoord() * 10;
        double depotY = problem.getDepot().getyCoord() * 10;
        
        stringI1.append("graph [ hierarchic 1 directed 1").append("\n");
        stringI1.append("node [ id ").append(problem.getDepot().getNumber()).append(" graphics [ x ").append(depotX).
                append(" y ").append(depotY).append(" w 11  h 11 type \"rectangle\"] LabelGraphics [text  \"").
                append(problem.getDepot().getNumber()).append("\" fontSize 7 ] ]").append("\n");
                
                //graph [ hierarchic 1 directed 1
                //node [ id 2 graphics [ x 360 y 200 w 11  h 11 type "roundrectangle"] LabelGraphics [text  "2" fontSize 7 ] ]
        for(Customer customer: problem.getAddedCustomers()){
            double cusX = customer.getxCoord() * 10;
            double cusY = customer.getyCoord() * 10;
            stringI1.append("node [ id ").append(customer.getNumber()).append(" graphics [ x ").append(cusX).
                append(" y ").append(cusY).append(" w 11  h 11 type \"elipse\"] LabelGraphics [text  \"").
                append(customer.getNumber()).append("\" fontSize 7 ] ]").append("\n");
        }
        
         Random rand = new Random();
            //edge [ source  105 target 2 graphics [ fill "#00FFFF" targetArrow "standard" ] ]
        for(Route ruta:problem.getRoutes()){
            
            

    // nextInt is normally exclusive of the top value,
    // so add 1 to make it inclusive
            int randomNum = rand.nextInt(11);
            String color;
            
            switch(randomNum){
                case 1: color = "FF0000";
                    break;
                case 2: color = "CC9900";
                    break;
                case 3: color = "00FF00";
                    break;
                case 4: color = "0033FF";
                    break;
                case 5: color = "0000FF";
                    break;   
                case 6: color = "660066";
                    break;
                case 7: color = "000000";
                    break;    
                case 8: color = "9999CC";
                    break; 
                case 9: color = "999999";
                    break;
                case 10: color = "003300";
                    break; 
                default: color = "000000";
                    break;
            }
            
            
            ruta.getEdges().stream().forEach((arcos) -> {
                stringI1.append("edge [ source  ").append(arcos.getCustomer1().getNumber()).append(" target ").append(arcos.getCustomer2().getNumber()).
                        append(" graphics [ fill \"#").append(color).append("\" targetArrow \"standard\" ] ]").append("\n");
            });
            
        }
        
        
        //----Escribir en un archivo
        //------------------------------
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("solutions/i1/25/" + problem.getInstanceName() + ".gml"), "utf-8"));
            writer.write("" + stringI1);
        } catch (IOException ex) {
            // report
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {/*ignore*/

            }
        }

                //---- Fin de Escribir en un archivo
                //------------------------------

        
    }
    
    
    public static void main(String[] args) {
        // TODO code application logic here
        
        StringBuilder string;
        string = new StringBuilder();
        StringBuilder stringI1;
        //stringI1 = new StringBuilder();
         
 
        Heuristic_NNH nearestN =  new Heuristic_NNH();
        Heuristic_CWDT cwdt = new Heuristic_CWDT();
        Heuristic_I1 i1 = new Heuristic_I1();
        Heuristic_Relocate relocate =  new Heuristic_Relocate();
        Heuristic_2OPT opt = new Heuristic_2OPT();
        Solution_Tester tester = new Solution_Tester();
        Problem_Analizer pAnal = new Problem_Analizer();
        VehicleRoutingProblem problem ;
 
         List<String> results = new ArrayList<>();
        
         
         
        try {
            Files.walk(Paths.get("Instances/solomon_100")).forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    
                     results.add(filePath.getFileName().toString());
                  //  System.out.println(filePath); 
                    
                }
            });     } catch (IOException ex) {
            Logger.getLogger(VRP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
        //-------------------------------------------------
        // -------------------NNH
        //-------------------------------------------------

        /*
        */
        //System.out.println("" + nearestN.toString());
          string.append(nearestN.toString()).append("\n");
          for (String result:results){
            problem = new VehicleRoutingProblem("Instances/solomon_100/" +   result);
            long start = System.currentTimeMillis();  
            while ( nearestN.getNextElement(problem) == 1){}
            
            long elapsedTime = System.currentTimeMillis() - start;
           
            if(tester.solution(problem)) {
                stringI1 = new StringBuilder();
                stringI1.append(elapsedTime).append(" ");
                stringI1.append(problem.getInstanceName()).append(" ");
                stringI1.append(pAnal.solution(problem)).append("\n");
                string.append(stringI1);
                
              //  imprimirGraficas(problem);      
            }
            else
              System.out.println("Mala Solucion"); 
        }
        
        
        
        
        //-------------------------------------------------
        // -------------------CWTD 
        //-------------------------------------------------
        /*
         //System.out.println("" + cwdt.toString()); 
          string.append(cwdt.toString()).append("\n");
          for (String result:results){
            problem = new VehicleRoutingProblem("Instances/solomon_100/" +   result);
            long start = System.currentTimeMillis();  
            while ( cwdt.getNextElement(problem) == 1){}
            
      long elapsedTime = System.currentTimeMillis() - start;
           
            if(tester.solution(problem)) {
                stringI1 = new StringBuilder();
                stringI1.append(elapsedTime).append(" ");
                stringI1.append(problem.getInstanceName()).append(" ");
                stringI1.append(pAnal.solution(problem)).append("\n");
                string.append(stringI1);
                
                imprimirGraficas(problem);      
            }
            else
              System.out.println("Mala Solucion"); 
        }
         
         */
         
         /*    
         //-------------------------------------------------
        // -------------------Solomon I1
        //-------------------------------------------------
          //System.out.println("" + i1.toString());
          string.append(i1.toString()).append("\n");
          for (String result:results){
            problem = new VehicleRoutingProblem("Instances/solomon_25/" +   result);
            long start = System.currentTimeMillis();  
            while ( i1.getNextElement(problem) == 1){}
            
            long elapsedTime = System.currentTimeMillis() - start;
           
            if(tester.solution(problem)) {
                stringI1 = new StringBuilder();
                stringI1.append(elapsedTime).append(" ");
                stringI1.append(problem.getInstanceName()).append(" ");
                stringI1.append(pAnal.solution(problem)).append("\n");
                string.append(stringI1);
                
                imprimirGraficas(problem);      
            }
            else
              System.out.println("Mala Solucion"); 
        }
          
       */
        System.out.println(" " + string); 
    }
    
    
    
}
