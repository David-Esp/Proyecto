/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unitaria;

import DataStructure.Customer;
import DataStructure.ReturnHC;
import DataStructure.Vehicle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rodrigo19x
 */
public class WTDH {
       
        /*********************************************************************************/
        //Método que calcula la distancia
        public double distance(double x1,double x2,double y1, double y2){
        return Math.sqrt((Math.pow(Math.abs(x2-x1),2))+(Math.pow(Math.abs(y2-y1),2)));
        }
        /*********************************************************************************/
        
        /*********************************************************************************/
        //Método que selecciona el siguiente cliente según las cuatro condiciones de la heurística WTDH
        //Necesita un objeto de la clase vehiculo y el Map de todos los clientes de la solución.
        //Retorna ReturnHC (Contiene un objeto cliente y la distancia)
        public ReturnHC  Selector_CustomersWTDH(Vehicle V, Map<Integer,Customer> Aux){
            List<Customer> Aux2 = new  ArrayList(); //Lista auxiliar de clientes
            List<Double> Aux3 = new  ArrayList(); //Lista auxiliar de double's que utilizaremos para seleccionar el de menor distancia
			List<Integer> due_times = new ArrayList();
            for(int i=1;i<Aux.size();i++){
                //Deposito no se encuentre en ninguna ruta (demanda no cubierta)
                //Capacidad del cliente no sea superior a la capacidad actual de la ruta
                //El cliente se ecuentre disponible en el tiempo de servicio.
                if((Aux.get(i).getDemand()!=0) && (Aux.get(i).getDemand()<V.getCapacity()) && ( V.getTime_service() + (this.distance(V.getX(),Aux.get(i).getX_coord(),V.getY(),Aux.get(i).getY_coord()))< Aux.get(i).getDue_time()) ){
                    Aux2.add(Aux.get(i));
                    Aux3.add(this.distance(V.getX(),Aux.get(i).getX_coord(),V.getY(),Aux.get(i).getY_coord())+Aux.get(i).getDue_time());
					due_times.add(Aux.get(i).getDue_time());
                }
            }
            //Si no encuentra nada, se debe retornar un cliente null
            if (Aux3.isEmpty()){
                return null;
            }

            //Se debe buscar la suma de la distancia entre el vehículo y el cliente, y el último tiempo permitido para atender sea el mínimo. 
            double distance=99999999;
            double f_distance=0;
            int index=0;
            for(int i=0;i<Aux3.size();i++){
                if(distance>Aux3.get(i)){
                    distance=Aux3.get(i);
					f_distance=Aux3.get(i);
                   // f_distance=Aux3.get(i)-Aux.get(i).getDue_time();
                    index=i;
                }
            }
            ReturnHC R = new ReturnHC();
            //Se retorna el cliente y la distancia para llegar a el (para no recalcularla)
            R.Cus=Aux2.get(index);
			R.distance=f_distance - due_times.get(index);
            //R.distance=f_distance;
            return R;
    }
    /*********************************************************************************/
        
     public int getNextElement(VRPTWProblem problem) {
        if(problem.customer_complets()){
            
           
                    int last=problem.getLastVehicle();
                    
                    Vehicle V = problem.getUsing_Vehicle().get(last);
                    if (problem.getRoutes().get(V.getId()-1).get(problem.getRoutes().get(V.getId()-1).size()-1) != 0 )
                    {
                     problem.getRoutes().get(V.getId()-1).add(0); //Se une la ruta del vehiculo con el deposito
                 //   problem.setLastVehicle(last+1);//Se actualiza el ultimo vehiculo.
                    problem.setNumberVehicle(problem.getNumberVehicle()-1); //Disminuye el ultimo vehiculo
                    
                   // Se calcula la distancia entre el deposito y la ubicación del vehiculo
                   double distance_Final=this.distance(V.getX(),problem.getUsing_Customer().get(0).getX_coord(),V.getY(),problem.getUsing_Customer().get(0).getY_coord());
                    problem.getUsing_Vehicle().get(V.getId()).setTime_service(V.getTime_service()+distance_Final);//Se actualiza el tiempo actual del vehiculo
                    problem.setTimeDistance(distance_Final+problem.getTimeDistance());//Se actualiza el tiempo global del problema
                    
                    problem.getUsing_Vehicle().get(V.getId()).setX(problem.getUsing_Customer().get(0).getX_coord());//Se actualiza coordenada X del Vehículo con la del depósito
                    problem.getUsing_Vehicle().get(V.getId()).setY(problem.getUsing_Customer().get(0).getY_coord());//Se actualiza coordenada Y del Vehículo con la del depósito
                 }
            return 0;
        }else{
            //Si quedan vehiculos disponibles 
            if(problem.getNumberVehicle()!=0){
                //Seleccionar Vehiculo Nuevo
                int last=problem.getLastVehicle();
                Vehicle V = problem.getUsing_Vehicle().get(last);
                //Seleccionar Cliente según heurística
                ReturnHC C =Selector_CustomersWTDH(V,problem.getUsing_Customer());
                
                //Si el cliente es null, se debe unir la ruta actual del problema con el deposito
                if(C == null){
                    problem.getRoutes().get(V.getId()-1).add(0); //Se une la ruta del vehiculo con el deposito
                    problem.setLastVehicle(last+1);//Se actualiza el ultimo vehiculo.
                    problem.setNumberVehicle(problem.getNumberVehicle()-1); //Disminuye el ultimo vehiculo
                    
                    //Se calcula la distancia entre el deposito y la ubicación del vehiculo
                    double distance_Final=this.distance(V.getX(),problem.getUsing_Customer().get(0).getX_coord(),V.getY(),problem.getUsing_Customer().get(0).getY_coord());
                    problem.getUsing_Vehicle().get(V.getId()).setTime_service(V.getTime_service()+distance_Final);//Se actualiza el tiempo actual del vehiculo
                    problem.setTimeDistance(distance_Final+problem.getTimeDistance());//Se actualiza el tiempo global del problema
                    
                    problem.getUsing_Vehicle().get(V.getId()).setX(problem.getUsing_Customer().get(0).getX_coord());//Se actualiza coordenada X del Vehículo con la del depósito
                    problem.getUsing_Vehicle().get(V.getId()).setY(problem.getUsing_Customer().get(0).getY_coord());//Se actualiza coordenada Y del Vehículo con la del depósito
                 
                }else{
                    double distance_Final;
                //    if(V.getTime_service() >  C.Cus.getReady_time()){ //Si no tiene que esperar
				      if((V.getTime_service() + C.distance) >=  C.Cus.getReady_time()){ 
                        distance_Final=V.getTime_service()+C.Cus.getServices()+C.distance;
                    }
                    else{ //Caso contrario, se debe calcular el tiempo de espera (la sentencia se encuentra en el abs)
                        distance_Final=C.Cus.getServices()+C.distance+Math.abs(V.getTime_service() - C.Cus.getReady_time()) + V.getTime_service();
                    }
                    System.out.println("Test123 " + distance_Final);
                    problem.setTimeDistance(distance_Final+problem.getTimeDistance() - V.getTime_service());//Se actualiza tiempo global del problema.
                    System.out.println("Test13 " +problem.getTimeDistance());
                    //Las siguientes secciones de códuigo actualizan al vehiculo en uso
                    problem.getUsing_Vehicle().get(V.getId()).setCapacity(V.getCapacity()-C.Cus.getDemand());//Se resta la capacidad al vehiculo.
                    problem.getUsing_Vehicle().get(V.getId()).setX(C.Cus.getX_coord());//Se actualiza la coordenada X para llegar al nuevo cliente.
                    problem.getUsing_Vehicle().get(V.getId()).setY(C.Cus.getY_coord());//Se actualiza la coordenada Y para llegar al nuevo cliente.
                    problem.getUsing_Vehicle().get(V.getId()).setTime_service(distance_Final);//Se actucaliza con la distancia final acumulada.
                    
                    
                    //Se debe actualizar el cliente para que no sea visitado
                    problem.getUsing_Customer().get(C.Cus.getId()).setDemand(0); //Demanda 0
                    problem.getUsing_Customer().get(C.Cus.getId()).setServices(0);//Tiempo de servicio 0
                     
                   
                    problem.getRoutes().get(V.getId()-1).add(C.Cus.getId()); //Se agrega el cliente a la ruta
                    
                }
                
            }
        }
        return 1;
    }

}
