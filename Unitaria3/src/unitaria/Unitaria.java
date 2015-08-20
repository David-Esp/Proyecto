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
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       D.getNextElement(V);
       
       V.view_dates();
       
    }
    
}
