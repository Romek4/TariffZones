package tariffzones.model;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


public class TariffZonesModel {
	private Map<String, BusStop> stops;
	private Map<String, Way> ways;
	
	public TariffZonesModel() {
		stops = new HashMap<String, BusStop>();
		ways = new HashMap<String, Way>();
	}
	
	public boolean addStop(String stopName, Coordinate stopCoordinate) {
		if (!stops.containsKey(stopName)) {
			stops.put(stopName, new BusStop(stopName, stopCoordinate));
			return true;
		}
		return false;
	}
	
	public boolean removeStop(String stopName) {
		if (stops.remove(stopName) == null) {
			System.err.println("Stop with name " + stopName + " does not exist.");
			return false;
		}
		return true;
	}
	
	public BusStop getStopCoordinate(String stopName) {
		return stops.get(stopName);
	}
	
	/**
	 * Adds a new way specified by two Coordinates in array.
	 * @param stopName
	 * @param wayCoordinates
	 * @return
	 */
	public boolean addWay(String wayName, Coordinate[] wayCoordinates) {
		if (wayCoordinates == null || wayCoordinates.length != 2) {
			System.err.println("Way is specified by two Coordinates.");
			return false;
		}
		if (!ways.containsKey(wayName)) {
			ways.put(wayName, new Way(wayName, wayCoordinates[0], wayCoordinates[1]));
			return true;
		}
		return false;
	}
	
	public boolean removeWay(String wayName) {
		if (ways.remove(wayName) == null) {
			System.err.println("Way with name " + wayName + " does not exist.");
			return false;
		}
		return true;
	}
	
	public Way getWayCoordinates(String wayName) {
		return ways.get(wayName);
	}
}
