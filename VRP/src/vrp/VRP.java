/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vrp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class VRP {

    /**
     * @param args timport vrp.heuristics.Heuristic_NNH;
he command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        
        
        Heuristic_Relocate relocate =  new Heuristic_Relocate();
        Heuristic_2OPT opt = new Heuristic_2OPT();
        
        
        VehicleRoutingProblem problem,problem2, problem3;
 
  
       problem = new VehicleRoutingProblem("Instances/solomon_100/R101.txt"); 
       Heuristic_NNH d =  new Heuristic_NNH();
       Heuristic_I1 i1 = new Heuristic_I1();
            Solution_Tester sol = new Solution_Tester();
       Problem_Analizer pAnal = new Problem_Analizer();
       
       
       while ( i1.getNextElement(problem) == 1){}
       System.out.println(problem.toString());
       System.out.println(pAnal.solution(problem));
       
             
       if(sol.solution(problem)) {
         System.out.println(problem.toString());
       
           System.out.println(pAnal.solution(problem));
       
       }
       else
            System.out.println("Mala Solucion 1");
       
       while (opt.getNextElement(problem) == 1 ){}
       System.out.println(pAnal.solution(problem));
       System.out.println(problem.toString());
  
      
       if(sol.solution(problem)) {
         System.out.println(problem.toString());
       
           System.out.println(pAnal.solution(problem));
       
       }
       else
            System.out.println("Mala Solucion");
       
       /*
       problem2 = new VehicleRoutingProblem("Instances/solomon_100/C101.txt"); 
       
       Heuristic_CWDT x =  new Heuristic_CWDT();
       while ( x.getNextElement(problem2) == 1)
       {
        x.getNextElement(problem2);
       }
               
       
        problem3 = new VehicleRoutingProblem("Instances/solomon_100/C101.txt"); 
       
       Heuristic_I1 y =  new Heuristic_I1();
       while ( y.getNextElement(problem3) == 1)
       {
        y.getNextElement(problem3);
       }
       
       */
       
       
       
       //-------------------------
       //Test del partial reset
       //--------------------------
       /* -------- 
       System.out.println(problem.toString());
       
       Heuristic_PartialReset parR = new Heuristic_PartialReset();
       parR.getNextElement(problem);
       */
       
       //-------------------------
       //Test del partial reset
       //--------------------------
       /* ------------- 
       
       System.out.println(problem.toString());
       
        Heuristic_CompleteReset compR = new Heuristic_CompleteReset();
        compR.getNextElement(problem);
       
        System.out.println(problem.toString());
        
        System.out.println(problem.getCustomers().toString());
        
        System.out.println(problem.getAddedCustomers().toString());
        
        
         while ( d.getNextElement(problem) == 1)
       {
        d.getNextElement(problem);
       }
         
        System.out.println(problem.toString()); 
        
        */
        
        
   
       
       /*
     //----------------------------------
     //-----Prueba del Relocate
     //----------------------------------
     
     Heuristic_Relocate rel =  new Heuristic_Relocate();
     
     rel.getNextElement(problem);
     
        
      
       while ( rel.getNextElement(problem) == 1)
       {
        rel.getNextElement(problem);
       }
       
       if(sol.solution(problem)) {
         System.out.println(problem.toString());
        System.out.println(pAnal.solution(problem));
     }
       else
            System.out.println("Mala Solucion");
       
       
       */
           
 /* -------------Prueba del 2OPT!!!!______________
   //-----------------------------------------
   //    ------------------------------------
   //    ------------------------------------
       
         
           
       Heuristic_2OPT opt2 = new Heuristic_2OPT();
       
       
         //   opt2.getNextElement(problem);
          while ( opt2.getNextElement(problem) == 1)
       {
            opt2.getNextElement(problem);
       }  
     
     if(sol.solution(problem)) {
         System.out.println(problem.toString());
     System.out.println(pAnal.solution(problem));
     }
       else
            System.out.println("Mala Solucion");
     
      
      */  
    }
    
    
    
}
