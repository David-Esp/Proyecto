/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataStructure;

/**
 *
 * @author rodrigo19x
 */
public class Vehicle {
    
    private int id;
    private int capacity; //Capacidad con la que se encuentra el vehiculo
    private double time_service; //Tiempo total de servicio del vehiculo
    private double x;
    private double y;
    
    public double getTime_service() {
        return time_service;
    }

    public void setTime_service(double time_service) {
        this.time_service = time_service;
    }


    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    
    public boolean decrement_Capacity(int q){
        int new_q= this.capacity - q;
        if(new_q<0){
            return false;
        }else{
            this.capacity=new_q;
            return true;
        }
    }
    
    
}
