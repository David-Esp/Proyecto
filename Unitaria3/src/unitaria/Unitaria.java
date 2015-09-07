/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unitaria;

import DataStructure.Instance;

/**
 *
 * @author rodrigo19x
 */
public class Unitaria {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {        
       //Instance A = new Instance("instance/C101.txt");
       //A.View();
       
       
       VRPTWProblem V = new VRPTWProblem("instance/C101.txt");
       //V.getInst().View();
       //V.view_dates();
       WTDH D = new WTDH();
        while ( D.getNextElement(V) == 1){
        D.getNextElement(V);
       }
        
         
//        Parcial_Reset P = new Parcial_Reset();
//            P.getNextElement(V);
           
            
        Complete_Reset C = new Complete_Reset();
            C.getNextElement(V);
            
       /*
        
        TwoOptMod T = new TwoOptMod();
         T.getNextElement(V);
        */ 
        
       V.view_dates();
       
    }
    
}
