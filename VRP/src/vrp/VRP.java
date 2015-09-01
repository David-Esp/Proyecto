/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vrp;

import vrp.Problem.VehicleRoutingProblem;
import vrp.Solvers.Solution_Tester;
import vrp.heuristics.Heuristic_2OPT;
import vrp.heuristics.Heuristic_CompleteReset;
import vrp.heuristics.Heuristic_I1;
import vrp.heuristics.Heuristic_NNH;
import vrp.heuristics.Heuristic_PartialReset;
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
      /*
        
        problem = new VehicleRoutingProblem("Instances/solomon_25/C101.txt"); 
         
       // System.out.println(problem.toString());
        
       // problem.solve();
      
        
       Heuristic_CWDT D = new Heuristic_CWDT();
       while ( D.getNextElement(problem) == 1)
       {
        D.getNextElement(problem);
       }
     */ 
        
       problem = new VehicleRoutingProblem("Instances/solomon_25/C101.txt"); 
       
       Heuristic_NNH d =  new Heuristic_NNH();
       while ( d.getNextElement(problem) == 1)
       {
        d.getNextElement(problem);
       }
        
       problem2 = new VehicleRoutingProblem("Instances/solomon_25/RC105.txt"); 
       
       Heuristic_NNH x =  new Heuristic_NNH();
       while ( x.getNextElement(problem2) == 1)
       {
        x.getNextElement(problem2);
       }
               
       
        problem3 = new VehicleRoutingProblem("Instances/solomon_25/RC105.txt"); 
       
       Heuristic_I1 y =  new Heuristic_I1();
       while ( y.getNextElement(problem3) == 1)
       {
        y.getNextElement(problem3);
       }
       
       
       Solution_Tester sol = new Solution_Tester();
       
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
       /* ------------- */
       
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
        
        
        
        
       /*
       if(sol.solution(problem)) 
         System.out.println(problem.toString());
       else
            System.out.println("Mala Solucion");
       
       
       while ( relocate.getNextElement(problem) == 1)
       {
        relocate.getNextElement(problem);
       }
       
       
       
       if(sol.solution(problem2)) 
         //System.out.println(problem2.toString());
       else
            System.out.println("Mala Solucion");
       
       
        while ( relocate.getNextElement(problem2) == 1)
       {
        relocate.getNextElement(problem2);
       }
       
       
       if(sol.solution(problem3)) 
         System.out.println(problem3.toString());
       else
         System.out.println("Mala Solucion"); 
       
       */
         //System.out.println(problem3.toString());
         
       /* 
       while ( relocate.getNextElement(problem3) == 1)
       {
        relocate.getNextElement(problem3);
       }
       
      
       while ( opt.getNextElement(problem3) == 1)
       {
        opt.getNextElement(problem3);
       }
       */
       
       
    }
    
    
    
}
