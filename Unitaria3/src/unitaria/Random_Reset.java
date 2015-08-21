/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unitaria;

import DataStructure.Instance;
import java.util.List;

/**
 *
 * @author rodrigo19x
 */
public class Random_Reset {
    /*
    private int NumberVehicle;
    private double TimeDistance;
    private int LastVehicle;
    private Map<Integer,Vehicle> Using_Vehicle;
    private Map<Integer,Customer> Using_Customer;
    private List<List<Integer>> Routes;
    */
    
    
    public int getNextElement(VRPTWProblem problem) {
        int randomNum = 1 + (int)(Math.random()*problem.getRoutes().size());
        
        for(int i=problem.getRoutes().size();i>randomNum;i--){
            this.reset_customers(problem.getRoutes().get(i),problem);
            problem.setTimeDistance(problem.getTimeDistance() - problem.getUsing_Vehicle().get(i).getTime_service());
            problem.getRoutes().remove(i-1);
            problem.getUsing_Vehicle().remove(i);
            problem.setLastVehicle(problem.getLastVehicle()-1);
            problem.setNumberVehicle(problem.getNumberVehicle()+1);
            
        }
        
        return 0;
    }
    
    private void reset_customers(List<Integer> customers, VRPTWProblem problem){
            /*
            private int demand;
            private int services;
            private double vehicle_arrived;
            private int capacity_arrived;
            */
        Instance Inst = problem.getInst();
        for(int i=0;i<customers.size();i++){
            problem.getUsing_Customer().get(customers.get(i)).setDemand(Inst.getGeneral_Customer().get(customers.get(i)).getDemand());
            problem.getUsing_Customer().get(customers.get(i)).setServices(Inst.getGeneral_Customer().get(customers.get(i)).getServices());
            problem.getUsing_Customer().get(customers.get(i)).setVehicle_arrived(0);
            problem.getUsing_Customer().get(customers.get(i)).setCapacity_arrived(0);
        }
    }
}
