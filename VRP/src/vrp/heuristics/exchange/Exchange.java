/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vrp.heuristics.exchange;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author David
 */
public class Exchange {
    private List<Candidate> candidates;
    private List<RouteExchange> routes;
    
    
    public Exchange( ){
        candidates = new ArrayList<>(100);
        routes = new ArrayList<>(100);
        
    }
    
    
    //Regresa una lista con los candidatos posibles que pueden moverse a la ruta del cliente con mejor movimiento 
    //Si cliente A se mueve a la ruta 1, se busca un cliente de la ruta 1 a moverse a la ruta del cliente A
    //Aqui se regresarian todos los clientes posibles de la ruta 1 
    public List<Candidate> getBackCandidates(Candidate cand){
        int origin = cand.getRouteOrigin();
        int destiny = cand.getRouteDestiny() ;
        RouteExchange routeO = routes.get(  origin);
        RouteExchange routeD = routes.get(destiny);
        int demandCandidateO = cand.getDemand();
         //Si el max son 200, la capacidad actual esta en 20 y va a entrar uno nuevo con demanda X
        //El que va a salir tiene que tener una demanda igual o superior a ( X - 20 )
        int min = demandCandidateO - routeD.getCapacity();
        List<Candidate> auxCandidates;
        auxCandidates = new ArrayList<>(100);
        for( int x = 0; x < candidates.size();x++){
            Candidate candidateB = candidates.get(x);
            
            if(candidateB.isActive() && candidateB.getRouteDestiny() ==  origin && candidateB.getRouteOrigin() == destiny && candidateB.getDemand() >= min){
                auxCandidates.add(candidateB);
                
            }
            
            
        }
        
        return auxCandidates;
    }
    
    public boolean existActive(){
        
         if(candidates.isEmpty())
            return false;
         
        for (Candidate candidate : candidates) {
            if (candidate.isActive()) {
                return true;
            }
        }
        return false;
        
    }
    
    public int getMaxActive(){
        double max = -1;
        int maxIndex = -1;
        if(candidates.isEmpty())
            return -1;
        
        for(int x = 0 ; x < candidates.size() ; x++){
            Candidate candidate = candidates.get(x);
            
            if (candidate.isActive()) {
                if(candidate.getSavings() > max){
                    max = candidate.getSavings();
                    maxIndex = x;
                }
                
            }
            
        }
        
        return maxIndex;
    }
    
    public int getMaxAll(){
                double max = -1;
        int maxIndex = -1;
        if(candidates.isEmpty())
            return -1;
        
        for(int x = 0 ; x < candidates.size() ; x++){
            Candidate candidate = candidates.get(x);
        
                if(candidate.getSavings() > max){
                    max = candidate.getSavings();
                    maxIndex = x;
                }
                 
        }
        
        return maxIndex;
    }
    
    
    public boolean insertRoute(RouteExchange route){
        routes.add(route);
        return true;
    }
    
    public boolean insertCandidate(Candidate cand){
        candidates.add(cand);
        return true;
    }
    

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
    }

    public List<RouteExchange> getRoutes() {
        return routes;
    }

    public void setRoutes(List<RouteExchange> routes) {
        this.routes = routes;
    }
    
}

