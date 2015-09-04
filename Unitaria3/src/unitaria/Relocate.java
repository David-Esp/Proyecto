/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unitaria;

import DataStructure.Customer;

/**
 *
 * @author David
 */
public class Relocate {
    
     public int getNextElement(VRPTWProblem problem) {
       
         
         
         return 1;
         
         
     }
    
     
       private double getDistanceFromTo(Customer customerOrigin, Customer customerDestiny) {

        double xCoord = Math.abs(customerDestiny.getX_coord()  - customerOrigin.getX_coord());
        double yCoord = Math.abs(customerDestiny.getY_coord() - customerOrigin.getY_coord());
        double distance = Math.sqrt((xCoord * xCoord) + (yCoord * yCoord));

        return distance;

    }
     
}
