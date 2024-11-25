package logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Graph implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -7910866032838849710L;
	private ArrayList<LinkedList<Stop>> adjList;
    private Map<String, Route> routes;
    private ArrayList<Stop> stops;

    public Graph() {
        adjList = new ArrayList<>();
        routes = new HashMap<>();
        stops = new ArrayList<>();
    }

    public void addStop(Stop stop) {
        LinkedList<Stop> currentList = new LinkedList<>();
        currentList.add(stop);
        adjList.add(currentList);
        stops.add(stop);
        PTMS.getInstance().setStopIdGenerator(PTMS.getInstance().getStopIdGenerator()+1);
    }

    public void addRoute(Route route) {
        LinkedList<Stop> currentList = adjList.get(PTMS.getInstance().getGraph().getStopIndex(route.getSrc()));
        Stop dstStop = adjList.get(PTMS.getInstance().getGraph().getStopIndex(route.getDest())).get(0);
        currentList.add(dstStop);
        
        // Almacenar la ruta en el mapa
        String routeKey = route.getId();
        routes.put(routeKey, route);
        
        PTMS.getInstance().setRouteIdGenerator(PTMS.getInstance().getRouteIdGenerator()+1);
    }
    
    public void deleteStop(Stop stop) {
    	LinkedList<Stop> deletionList = null;
    	
    	for(LinkedList<Stop> currentList : adjList) {
    		if(currentList.indexOf(stop) == 0) {
				deletionList = currentList;
    		}
    		for(Stop s : currentList) {
    			if(s.equals(stop) && currentList.indexOf(stop) != 0) {
    					deleteRoute(currentList.get(0), stop);
    				}
    			}
    		}
    	
    	while(deletionList.isEmpty() == false && deletionList != null) {
    		if(deletionList.getLast() == stop) {
    			break;
    		}
    		String routeKey = searchRouteId(stop, deletionList.getLast());
    		routes.remove(routeKey);
    		deletionList.removeLast();
    	}
    	adjList.remove(deletionList);
    	stops.remove(stop);
    }
    
    // Eliminar una ruta en la lista de adyacencia y en el mapa
    public void deleteRoute(Stop src, Stop dest) {
    	LinkedList<Stop> currentList = adjList.get(getStopIndex(src));
    	currentList.remove(dest);
    	String routeKey = searchRouteId(src, dest);
    	routes.remove(routeKey);
    }
    
    // Se modifica una parada
    public void modifyStop(Stop update) {
    	for(LinkedList<Stop> currentList : adjList) {
            for(Stop stop : currentList) {
            	if(stop.getId().equals(update.getId())) {
            		stop = update;
            		return;
            	}
            }
    	}
    }
    
    public void modifyRoute(Route update) {
    	String routeKey = update.getId();
    	Route oldRoute = routes.get(routeKey);
    	if(oldRoute != null) {
    		routes.put(routeKey, update);
        	LinkedList<Stop> currentList = adjList.get(PTMS.getInstance().getGraph().getStopIndex(oldRoute.getSrc()));
        	Stop targetStop = adjList.get(PTMS.getInstance().getGraph().getStopIndex(oldRoute.getSrc())).get(0);
        	int index = currentList.indexOf(targetStop);
        	currentList = adjList.get(PTMS.getInstance().getGraph().getStopIndex(update.getSrc()));
        	targetStop = adjList.get(PTMS.getInstance().getGraph().getStopIndex(update.getDest())).get(0);
        	currentList.set(index, targetStop);
    	}	
    }
    
    public Route getRoute(Stop src, Stop dest) {
        String routeKey = searchRouteId(src, dest);
        return routes.get(routeKey);
    }
    
    public Map<String, Route> getRoutes() {
        return routes;
    }

    public Stop getStop(int id) {
        return stops.get(id);
    }

    public ArrayList<LinkedList<Stop>> getAdjList() {
        return adjList;
    }
    
    public Stop searchStop(String id) {
    	for(LinkedList<Stop> currentList : adjList) {
    		if(currentList.getFirst().getId().equals(id)) {
    			return currentList.getFirst();
    		}
    	}
    	return null;
    }
    
    public String searchRouteId(Stop src, Stop dest) {
    	for(Entry<String, Route> entry : routes.entrySet()) {
    		if(entry.getValue().getSrc() == src && entry.getValue().getDest() == dest) {
    			return entry.getKey();
    		}
    	}
    	return "0";
    }
/*
    public void print() {
        for(LinkedList<Stop> currentList : adjList) {
            for(Stop stop : currentList) {
            	System.out.print(stop.getLabel());
            	if(currentList.indexOf(stop) != currentList.size() - 1) System.out.print(" -> ");
            }
            System.out.println();
        }
    }
*/

	public ArrayList<Stop> getStops() {
		ArrayList<Stop> myStops = new ArrayList<>();
		
		for(Stop s : stops) {
			myStops.add(s);
		}
		
		return myStops;
	}
    
	public ArrayList<String> getStopsName() {
		ArrayList<String> stopNames = new ArrayList<>();
		
		for(Stop s : stops) {
			stopNames.add(s.getLabel());
		}
		
		return stopNames;
	}
	
	public ArrayList<String> getStopsId() {
		ArrayList<String> stopId = new ArrayList<>();
		
		for(Stop s : stops) {
			stopId.add(s.getId());
		}
		
		return stopId;
	}
	
	public int getStopIndex(Stop s) {
		int index = 0;	
		for(LinkedList<Stop> currentList : adjList) {
            if(s.equals(currentList.get(0))) {
            	return index;
            }
            index++;
        }
		return -1;
	}
	
    public void print() {
        System.out.println("Estado actual del sistema de transporte:");
        for(LinkedList<Stop> currentList : adjList) {
            for(Stop stop : currentList) {
                System.out.print(stop.getLabel());
                if(currentList.indexOf(stop) != currentList.size() - 1 && stop != currentList.get(0)) {
                    Route route = getRoute(currentList.get(0), stop);
                    if (route != null) {
                        System.out.print(" -> [" + route.getCurrentEvent() + "] -> ");
                    } else {
                        System.out.print(" -> ");
                    }
                }
            }
            System.out.println();
        }
    }

}