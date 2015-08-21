/*
 * Instance.java
 * This file is part of VRPTW_HHD
 *
 * Copyright (C) 2015 - Carlos Rey Barra <carlos.rey.barra@gmail.com>
 *
 * VRPTW_HHD is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 *
 * VRPTW_HHD is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with VRPTW_HHD; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package DataStructure;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author rodrigo19x
 */
public class Instance {
    //En esta clase se declararon variables constantes del problema
    //No se deben utilizar estos datos para generar la solución, ya que se evita leer el archivo.
    private String name; //Nombre del archivo
    private int vehicle_number; //Numero de vehiculos de la instancia
    private int capacity; //Capacidad
    private Map<Integer,Vehicle> General_Vehicle; //Mapa de vehiculos
    private Map<Integer,Customer> General_Customer; //Mapa de clientes

    public Map<Integer, Vehicle> getGeneral_Vehicle() {
        return General_Vehicle;
    }

    public void setGeneral_Vehicle(Map<Integer, Vehicle> General_Vehicle) {
        this.General_Vehicle = General_Vehicle;
    }

    public Map<Integer, Customer> getGeneral_Customer() {
        return General_Customer;
    }

    public void setGeneral_Customer(Map General_Customer) {
        this.General_Customer = General_Customer;
    }

     public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public int getVehicle_number() {
        return vehicle_number;
    }

    public void setVehicle_number(int vehicle_number) {
        this.vehicle_number = vehicle_number;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void charge_vehicle(){
        this.General_Vehicle = new   HashMap();
        for(int i=0;i<=this.vehicle_number;i++){
            Vehicle V = new Vehicle();
            V.setId(i+1);
            V.setCapacity(this.capacity);
            V.setX(this.General_Customer.get(0).getX_coord());
            V.setY(this.General_Customer.get(0).getY_coord());
            V.setTime_service(0);
            this.General_Vehicle.put(i+1, V);
        }
    }
    
    public Instance(String path){
        File file = new File(path);
        FileReader fr;
        try {
            fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            int l=1;
            //********************************//
            int number;
            int xcoord;
            int ycoord;
            int demand;
            int ready;
            int due;
            int service;
            this.General_Customer = new  HashMap(); 
            //********************************// 
            while((line=br.readLine())!=null){
                
              if(!"".equals(line.trim()) || !line.trim().isEmpty()){
                if(l==1){
                   this.name=line.trim();
                }else if(l==5){
                    this.vehicle_number= Integer.parseInt(line.split("         ")[0].trim());
                    this.capacity = Integer.parseInt(line.split("         ")[1].trim());
                    
                }else if(l>=10){
                    
                    /******************************************************/
                    number=Integer.parseInt(line.split("      ")[0].trim());
                    xcoord=Integer.parseInt(line.split("      ")[1].trim());
                    ycoord=Integer.parseInt(line.split("      ")[2].trim());
                    demand=Integer.parseInt(line.split("      ")[3].trim());
                    ready=Integer.parseInt(line.split("      ")[4].trim());
                    due=Integer.parseInt(line.split("      ")[5].trim());
                    service=Integer.parseInt(line.split("      ")[6].trim());
                     /******************************************************/
                    Customer Ins = new Customer();
                    Ins.setId(number);
                    Ins.setDemand(demand);
                    Ins.setDue_time(due);
                    Ins.setReady_time(ready);
                    Ins.setX_coord(xcoord);
                    Ins.setY_coord(ycoord);
                    Ins.setServices(service);
                    Ins.setVehicle_arrived(0);
                    Ins.setCapacity_arrived(0);
                    /******************************************************/
                    this.General_Customer.put(number, Ins);
                }
                
            }
              l++;
            }
            charge_vehicle();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Instance.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Instance.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void View(){
        System.out.println("Instance Name= " + this.name);
        System.out.println("Vehicule Number= "+ this.vehicle_number);
        System.out.println("Customers=" +this.General_Customer.size());
        System.out.println("Matrix");
        for(int i=0;i<this.General_Customer.size();i++){
            Customer Aux = new Customer();
            Aux = this.General_Customer.get(i);
            System.out.println("Customer "+ i +" "+Aux.getX_coord()+" "+Aux.getY_coord()+" "+Aux.getDemand()+" "+Aux.getReady_time()+" "+Aux.getDue_time()+" "+Aux.getServices());
        }
    }
    
}
