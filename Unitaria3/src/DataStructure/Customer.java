/*
 * Customer.java
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

/**
 *
 * @author rodrigo19x
 */
public class Customer {
    //Datos estandar
    private int id;
    private double x_coord;
    private double y_coord;
    private int demand;
    private int ready_time;
    private int due_time;
    private int services;
    //Variables con las que llegó el vehiculo 
    private double vehicle_arrived; //Tiempo de servicio del chofer
    private int capacity_arrived; //Capacidad con la que llega el vehiculo

    public int getCapacity_arrived() {
        return capacity_arrived;
    }

    public void setCapacity_arrived(int capacity_arrived) {
        this.capacity_arrived = capacity_arrived;
    }

    public double getVehicle_arrived() {
        return vehicle_arrived;
    }

    public void setVehicle_arrived(double vehicle_arrived) {
        this.vehicle_arrived = vehicle_arrived;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    
    public double getX_coord() {
        return x_coord;
    }

    public void setX_coord(double x_coord) {
        this.x_coord = x_coord;
    }

    public double getY_coord() {
        return y_coord;
    }

    public void setY_coord(double y_coord) {
        this.y_coord = y_coord;
    }

    public int getDemand() {
        return demand;
    }

    public void setDemand(int demand) {
        this.demand = demand;
    }

    public int getReady_time() {
        return ready_time;
    }

    public void setReady_time(int ready_time) {
        this.ready_time = ready_time;
    }

    public int getDue_time() {
        return due_time;
    }

    public void setDue_time(int due_time) {
        this.due_time = due_time;
    }

    public int getServices() {
        return services;
    }

    public void setServices(int services) {
        this.services = services;
    }
}
