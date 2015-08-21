/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unitaria;

import DataStructure.Customer;
import DataStructure.Vehicle;
import java.util.List;

/**
 *
 * @author rodrigo19x
 */
public class TwoOptMod {

    /*********************************************************************************/
        //Método que calcula la distancia
    public double distance(double x1,double x2,double y1, double y2){
        return Math.sqrt((Math.pow(Math.abs(x2-x1),2))+(Math.pow(Math.abs(y2-y1),2)));
    }
   
    public int getNextElement(VRPTWProblem problem) {
        List<List<Integer>> Routes=problem.getRoutes();
        
        for(int i=0;i<Routes.size()-1;i++){
            for(int j=i+1;j<Routes.size()-1;j++){
                    
                        int penultimate_i=Routes.get(i).size()-3;
                        int penultimate_j=Routes.get(j).size()-3;
                        
                        Customer C_penultimate_i = problem.getUsing_Customer().get(Routes.get(i).get(penultimate_i));
                        Customer C_penultimate_i_plus = problem.getUsing_Customer().get(Routes.get(i).get(penultimate_i+1));
                        Customer C_penultimate_j=problem.getUsing_Customer().get(Routes.get(j).get(penultimate_j));
                        Customer C_penultimate_j_plus=problem.getUsing_Customer().get(Routes.get(j).get(penultimate_j+1));
                        /*****************************************************/
                        double cordx_i=C_penultimate_i.getX_coord();
                        double cordy_i=C_penultimate_i.getY_coord();
                        
                        double cordx_i_plus=C_penultimate_i_plus.getX_coord();
                        double cordy_i_plus=C_penultimate_i_plus.getY_coord();
                         /*****************************************************/                        
                        double cordx_j=C_penultimate_j.getX_coord();
                        double cordy_j=C_penultimate_j.getY_coord();
                        
                        double cordx_j_plus=C_penultimate_j_plus.getX_coord();
                        double cordy_j_plus=C_penultimate_j_plus.getY_coord();
                         /*****************************************************/
                        double distance_between_i_iplus=this.distance(cordx_i,cordx_i_plus,cordy_i,cordy_i_plus);
                        double distance_between_j_jplus=this.distance(cordx_j,cordx_j_plus,cordy_j,cordy_j_plus);
                        
                        double distance_between_i_jplus=this.distance(cordx_i,cordx_j_plus,cordy_i,cordy_j_plus);
                        double distance_between_j_iplus=this.distance(cordx_j,cordx_i_plus,cordy_j,cordy_i_plus);
                         /*****************************************************/
                        
                        if( (distance_between_i_iplus>distance_between_i_jplus) && (distance_between_j_jplus>distance_between_j_iplus) ){
                               double future_windows_1=C_penultimate_i.getVehicle_arrived()+C_penultimate_i.getServices()+distance_between_i_jplus;
                               double future_windows_2=C_penultimate_j.getVehicle_arrived()+C_penultimate_j.getServices()+distance_between_j_iplus;
                               //Restricción de ventana debe respetarse antes de realizar el Swap
                               if(future_windows_1< C_penultimate_j_plus.getDue_time() && future_windows_2< C_penultimate_i_plus.getDue_time()){

                                        
                                        /*
                                        private double TimeDistance;
                                        private Map<Integer,Vehicle> Using_Vehicle;
                                        private Map<Integer,Customer> Using_Customer;
                                        private List<List<Integer>> Routes;
                                        */

                                           Vehicle V_i = new Vehicle(); //Vehiculo de la ruta i posicionados en la penultima pos
                                           Vehicle V_j = new Vehicle(); //Vehiculo de la ruta j posicionados en la penultima pos

                                           V_i.setCapacity(C_penultimate_i.getCapacity_arrived());
                                           V_i.setId(i+1);
                                           V_i.setTime_service(C_penultimate_i.getVehicle_arrived());
                                           V_i.setX(C_penultimate_i.getX_coord());
                                           V_i.setY(C_penultimate_i.getY_coord());

                                           V_j.setCapacity(C_penultimate_j.getCapacity_arrived());
                                           V_j.setId(j+1);
                                           V_j.setTime_service(C_penultimate_j.getVehicle_arrived());
                                           V_j.setX(C_penultimate_j.getX_coord());
                                           V_j.setY(C_penultimate_j.getY_coord());
                                           
                                           //Antes de realizar el 2_0pt, se debe verificar además la restricción de demanda
                                           double future_demand1 = V_i.getCapacity()-C_penultimate_j_plus.getDemand();
                                           double future_demand2=  V_j.getCapacity()-C_penultimate_i_plus.getDemand();
                                           
                                           if(future_demand1>0 && future_demand2>0){

                                                    Routes.get(i).remove(Routes.get(i).size()-2);
                                                    Routes.get(i).remove(Routes.get(i).size()-1);
                                                    Routes.get(j).remove(Routes.get(j).size()-2);
                                                    Routes.get(j).remove(Routes.get(j).size()-1);
                                                  /*************Realizando nuevas uniones************************/

                                                     double distance_Final;

                                                     if(V_i.getTime_service() >  C_penultimate_j_plus.getReady_time()){ //Si no tiene que esperar
                                                         distance_Final=V_i.getTime_service()+C_penultimate_j_plus.getServices()+distance_between_i_jplus;
                                                     }
                                                     else{ //Caso contrario, se debe calcular el tiempo de espera (la sentencia se encuentra en el abs)
                                                         distance_Final=C_penultimate_j_plus.getServices()+distance_between_i_jplus+Math.abs(V_i.getTime_service() - C_penultimate_j_plus.getReady_time()) + V_i.getTime_service();
                                                     }

                                                     //Las siguientes secciones de códuigo actualizan al vehiculo en uso
                                                     problem.getUsing_Vehicle().get(V_i.getId()).setCapacity(V_i.getCapacity()-C_penultimate_j_plus.getDemand());//Se resta la capacidad al vehiculo.
                                                     problem.getUsing_Vehicle().get(V_i.getId()).setX(C_penultimate_j_plus.getX_coord());//Se actualiza la coordenada X para llegar al nuevo cliente.
                                                     problem.getUsing_Vehicle().get(V_i.getId()).setY(C_penultimate_j_plus.getY_coord());//Se actualiza la coordenada Y para llegar al nuevo cliente.
                                                     problem.getUsing_Vehicle().get(V_i.getId()).setTime_service(distance_Final);//Se actucaliza con la distancia final acumulada.


                                                     //Se debe actualizar el cliente para que no sea visitado
                                                     problem.getUsing_Customer().get(C_penultimate_j_plus.getId()).setDemand(0); //Demanda 0
                                                     problem.getUsing_Customer().get(C_penultimate_j_plus.getId()).setServices(0);//Tiempo de servicio 0


                                                     problem.getRoutes().get(V_i.getId()-1).add(C_penultimate_j_plus.getId()); //Se agrega el cliente a la ruta



                                                     if(V_j.getTime_service() >  C_penultimate_i_plus.getReady_time()){ //Si no tiene que esperar
                                                         distance_Final=V_j.getTime_service()+C_penultimate_i_plus.getServices()+distance_between_j_iplus;
                                                     }
                                                     else{ //Caso contrario, se debe calcular el tiempo de espera (la sentencia se encuentra en el abs)
                                                         distance_Final=C_penultimate_i_plus.getServices()+distance_between_j_iplus+Math.abs(V_j.getTime_service() - C_penultimate_i_plus.getReady_time()) + V_j.getTime_service();
                                                     }

                                                     //Las siguientes secciones de códuigo actualizan al vehiculo en uso
                                                     problem.getUsing_Vehicle().get(V_j.getId()).setCapacity(V_j.getCapacity()-C_penultimate_i_plus.getDemand());//Se resta la capacidad al vehiculo.
                                                     problem.getUsing_Vehicle().get(V_j.getId()).setX(C_penultimate_i_plus.getX_coord());//Se actualiza la coordenada X para llegar al nuevo cliente.
                                                     problem.getUsing_Vehicle().get(V_j.getId()).setY(C_penultimate_i_plus.getY_coord());//Se actualiza la coordenada Y para llegar al nuevo cliente.
                                                     problem.getUsing_Vehicle().get(V_j.getId()).setTime_service(distance_Final);//Se actucaliza con la distancia final acumulada.


                                                     //Se debe actualizar el cliente para que no sea visitado
                                                     problem.getUsing_Customer().get(C_penultimate_i_plus.getId()).setDemand(0); //Demanda 0
                                                     problem.getUsing_Customer().get(C_penultimate_i_plus.getId()).setServices(0);//Tiempo de servicio 0


                                                     problem.getRoutes().get(V_j.getId()-1).add(C_penultimate_i_plus.getId()); //Se agrega el cliente a la ruta


                                                   /************Cerrando rutas **********************************/ 
                                                     problem.getRoutes().get(V_i.getId()-1).add(0); //Se une la ruta del vehiculo con el deposito

                                                     distance_Final=this.distance(V_i.getX(),problem.getUsing_Customer().get(0).getX_coord(),V_i.getY(),problem.getUsing_Customer().get(0).getY_coord());
                                                     problem.getUsing_Vehicle().get(V_i.getId()).setTime_service(V_i.getTime_service()+distance_Final);//Se actualiza el tiempo actual del vehiculo

                                                     problem.getUsing_Vehicle().get(V_i.getId()).setX(problem.getUsing_Customer().get(0).getX_coord());//Se actualiza coordenada X del Vehículo con la del depósito
                                                     problem.getUsing_Vehicle().get(V_i.getId()).setY(problem.getUsing_Customer().get(0).getY_coord());//Se actualiza coordenada Y del Vehículo con la del depósito


                                                     problem.getRoutes().get(V_j.getId()-1).add(0); //Se une la ruta del vehiculo con el deposito

                                                     distance_Final=this.distance(V_j.getX(),problem.getUsing_Customer().get(0).getX_coord(),V_j.getY(),problem.getUsing_Customer().get(0).getY_coord());
                                                     problem.getUsing_Vehicle().get(V_j.getId()).setTime_service(V_j.getTime_service()+distance_Final);//Se actualiza el tiempo actual del vehiculo

                                                     problem.getUsing_Vehicle().get(V_j.getId()).setX(problem.getUsing_Customer().get(0).getX_coord());//Se actualiza coordenada X del Vehículo con la del depósito
                                                     problem.getUsing_Vehicle().get(V_j.getId()).setY(problem.getUsing_Customer().get(0).getY_coord());//Se actualiza coordenada Y del Vehículo con la del depósito

                                                  /***********************************************************/

                                                    problem.setRoutes(Routes);
                                                    double TimeDistance=problem.getTimeDistance()-(Math.abs(distance_between_i_iplus-distance_between_i_jplus)+Math.abs(distance_between_j_jplus-distance_between_j_iplus));
                                                    problem.setTimeDistance(TimeDistance);
                                   }
                                   
                                   
                               }                                
                        }
                }
            }
        return 1;
    }
}
