/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vrp;

import vrp.Problem.VehicleRoutingProblem;
import vrp.Solvers.Problem_Analizer;
import vrp.Solvers.Solution_Tester;
import vrp.heuristics.Heuristic_2OPT;
import vrp.heuristics.Heuristic_Exchange;
import vrp.heuristics.Heuristic_I1;
import vrp.heuristics.Heuristic_NNH;
import vrp.heuristics.Heuristic_Relocate;
import vrp.heuristics.Heuristic_RelocateIntraRoute;
import vrp.heuristics.Heuristic_SelectiveReMake;

/**
 *
 * @author David
 */
public class VRP {

    /**
     * @param args import vrp.heuristics.Heuristic_NNH; he command line
     * arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        

        VehicleRoutingProblem problem, problem2, problem3;

        problem = new VehicleRoutingProblem("Instances/solomon_100/C102.txt");
        Heuristic_NNH nnh = new Heuristic_NNH();
        Heuristic_I1 i1 = new Heuristic_I1();
        Heuristic_Exchange exchange = new Heuristic_Exchange();
        Heuristic_RelocateIntraRoute rint = new Heuristic_RelocateIntraRoute();
        Heuristic_2OPT opt = new Heuristic_2OPT();
        Heuristic_Relocate relocate = new Heuristic_Relocate();
        Heuristic_SelectiveReMake remake = new Heuristic_SelectiveReMake();
        
        
        Solution_Tester sol = new Solution_Tester();
        Problem_Analizer pAnal = new Problem_Analizer();

        while (i1.getNextElement(problem) == 1) {}
        //while (opt.getNextElement(problem) == 1) {}
        //while (relocate.getNextElement(problem)== 1) {}
        //while (rint.getNextElement(problem) == 1){}
        while ( exchange.getNextElement(problem) == 1){}
        while (opt.getNextElement(problem) == 1) {}
        while (relocate.getNextElement(problem)== 1) {}
        while (rint.getNextElement(problem) == 1){}
        while ( exchange.getNextElement(problem) == 1){}
        while (opt.getNextElement(problem) == 1) {}
        while (relocate.getNextElement(problem)== 1) {}
        while (rint.getNextElement(problem) == 1){}
        remake.getNextElement(problem);
        while ( exchange.getNextElement(problem) == 1){}
        while (opt.getNextElement(problem) == 1) {}
       // while (relocate.getNextElement(problem)== 1) {}
        while (rint.getNextElement(problem) == 1){}
        while ( exchange.getNextElement(problem) == 1){}
        while (opt.getNextElement(problem) == 1) {}
        while (relocate.getNextElement(problem)== 1) {}
        while (rint.getNextElement(problem) == 1){}
        while (opt.getNextElement(problem) == 1) {}
         
        
        
      // exchange.getNextElement(problem);
        //  exchange.getNextElement(problem);
       // while ( exchange.getNextElement(problem) == 1){}
        //   while (rint.getNextElement(problem) == 1){}
  /*     
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
         */
        if (sol.solution(problem)) {
            System.out.println(problem.toString());

            System.out.println(pAnal.solution(problem));

        } else {
            System.out.println("Mala Solucion");
        }

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
