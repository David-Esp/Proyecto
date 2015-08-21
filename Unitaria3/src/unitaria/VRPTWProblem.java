/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unitaria;

import DataStructure.Customer;
import DataStructure.Instance;
import DataStructure.Vehicle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rodrigo19x
 */
public class VRPTWProblem {
    //Las siguientes variables van almacenando la información a medida que genera la solución.
    private int NumberVehicle; //Cantidad de vehiculos ocupados
    private double TimeDistance; //Tiempo Total
    private int LastVehicle; //Último vehiculo utilizado
    private Map<Integer,Vehicle> Using_Vehicle; //Mapa de vehiculos que se han utilizado en la solución
    private Map<Integer,Customer> Using_Customer; // Mapa de clientes que se han utilizado en la solución.
    private List<List<Integer>> Routes; //Matriz de ruta (Ojo, el indice 0 es equivalente a la ruta del vehiculo 1)
    private Instance Inst; // Datos de pila (este objeto se puede utilizar en caso de querer resetear alguna estructura del problema)
    
    public void view_dates(){
        System.out.println("Numero de  Vehiculos: " + this.NumberVehicle);
        System.out.println("Tiempo actual de Distancia: " + this.TimeDistance);
        System.out.println("Ultimo vehiculo utilizado: " + this.LastVehicle);
        
        System.out.println("------------------------------------------------");
        System.out.println("Ruta: ");
            for(int i=0;i<this.Routes.size();i++){
                System.out.println("Ruta de vehiculo: "+(i+1));
                for(int j=0;j<this.Routes.get(i).size();j++){
                    System.out.print(this.Routes.get(i).get(j)+" ");
                }
                System.out.println("");
            }
            
        System.out.println("------------------------------------------------");
        System.out.println("Datos de vehiculos Actualmente");
        for(Map.Entry<Integer, Vehicle> entry : Using_Vehicle.entrySet()) {
            System.out.println("Vehiculo número: "+entry.getValue().getId());
            System.out.println("Coordenada X: "+entry.getValue().getX());
            System.out.println("Coordenada Y: "+entry.getValue().getY());
            System.out.println("Capacidad : "+entry.getValue().getCapacity());
            System.out.println("Tiempo de ruta : "+entry.getValue().getTime_service());
        }
        System.out.println("------------------------------------------------");
        System.out.println("Datos de Clientes Actualmente");
        for(Map.Entry<Integer, Customer> entry : Using_Customer.entrySet()) {
            System.out.println("Cliente número: "+entry.getValue().getId());
            System.out.println("Coordenada X: "+entry.getValue().getX_coord());
            System.out.println("Coordenada Y: "+entry.getValue().getY_coord());
            System.out.println("Inicio Ventana: "+entry.getValue().getReady_time());
            System.out.println("Fin Ventana: "+entry.getValue().getDue_time());
            System.out.println("Servicio: "+entry.getValue().getServices());
            System.out.println("Demanda: "+entry.getValue().getDemand());
        }
        System.out.println("------------------------------------------------");
    }
    

    
    public Map<Integer, Vehicle> getUsing_Vehicle() {
        return Using_Vehicle;
    }

    public void setUsing_Vehicle(Map<Integer, Vehicle> Using_Vehicle) {
        this.Using_Vehicle = Using_Vehicle;
    }

    public Map<Integer, Customer> getUsing_Customer() {
        return Using_Customer;
    }

    public void setUsing_Customer(Map<Integer, Customer> Using_Customer) {
        this.Using_Customer = Using_Customer;
    }


    public Instance getInst() {
        return Inst;
    }

    public void setInst(Instance Inst) {
        this.Inst = Inst;
    }

    public int getLastVehicle() {
        return LastVehicle;
    }

    public void setLastVehicle(int LastVehicle) {
        this.LastVehicle = LastVehicle;
    }
    public int getNumberVehicle() {
        return NumberVehicle;
    }

    public void setNumberVehicle(int NumberVehicle) {
        this.NumberVehicle = NumberVehicle;
    }

    public double getTimeDistance() {
        return TimeDistance;
    }

    public void setTimeDistance(double TimeDistance) {
        this.TimeDistance = TimeDistance;
    }

    public List<List<Integer>> getRoutes() {
        return Routes;
    }

    public void setRoutes(List<List<Integer>> Routes) {
        this.Routes = Routes;
    }
    
    public VRPTWProblem(String name_file){
        this.TimeDistance=0; 
        this.Inst= new Instance(name_file);
        this.NumberVehicle=this.Inst.getVehicle_number();
        this.LastVehicle=1;
        this.Using_Customer=this.Inst.getGeneral_Customer();
        this.Using_Vehicle=this.Inst.getGeneral_Vehicle();
        inicialize_routes();
    }
        
   //Methods
    

    public void inicialize_routes(){
        this.Routes = new ArrayList<List<Integer>>();
        for(int i=0;i<this.NumberVehicle;i++){
            this.Routes.add(i,new ArrayList<Integer>());
            this.Routes.get(i).add(0);
        }
    }


    public boolean decrement_Vehicle(){
        if(this.NumberVehicle<0){
            return false;
        }
        this.NumberVehicle--;
        return true;
    }

    public boolean customer_complets(){
        Map<Integer,Customer> Aux = (Map<Integer,Customer>) this.getUsing_Customer();
        for(int i=0;i<Aux.size();i++){
            if(Aux.get(i).getDemand()!=0){
                return false;
            }
        }
        return true;
    }
    

    
    
}
