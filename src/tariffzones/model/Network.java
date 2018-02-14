package tariffzones.model;

import java.util.ArrayList;

import tariffzones.tariffzonesprocessor.djikstra.Edge;
import tariffzones.tariffzonesprocessor.djikstra.Node;

public class Network {
	
	private int networkID;
	private String networkName;
	private String networkType;
	private String countryID;
	
	private ArrayList<Stop> stops;
	private ArrayList<Way> ways;
	
	public Network(int networkID, String networkName, String networkType, String countryID) {
		this.networkID = networkID;
		this.networkName = networkName;
		this.networkType = networkType;
		this.countryID = countryID;
	}
	
	public boolean addWay(Way way) {
//		if (ways.findData(wayId) == null) {
//			BusStop startStop = (BusStop) stops.findData(startNumber).getValue();
//			BusStop endStop = (BusStop) stops.findData(endNumber).getValue();
//			
//			if (startStop == null || endStop == null) {
//				return false;
//			}
//			
//			ways.insertData(new Record(wayId, new Way(wayId, startStop, endStop, timeInMins)));
//			return true;
//		}
//		return false;
		
//		Stop startStop = this.findStop(startNumber);
//		Stop endStop = this.findStop(endNumber);
//		
//		if (startStop == null || endStop == null) {
//			return false;
//		}
		
//		Way way = new Way(startStop, endStop, km, timeInMins);
		if (ways.add(way)) {
			return true;
		}
		return false;
		
	}

	public boolean removeWay(Way way) {
		return getWays().remove(way);
	}

	public void removeStopEdges(int stopNumber) {
		Stop stop = findStop(stopNumber);
		for (Object o : getWays()) {
			Way way = (Way) o;
			if (way.getStartPoint().equals(stop) || way.getEndPoint().equals(stop)) {
				getWays().remove(way);
			}
		}
	}
	
	public void removeStopEdges(Stop stop) {
		if (stop != null) {
			for (Way way : stop.getPartOfWays()) {
				getWays().remove(way);
			}
		}
	}
	
	public Stop findStop(int stopNumber) {
		for (Node node : stops) {
			Stop stop = (Stop) node;
			if (stop.getNumber() == stopNumber) {
				return stop;
			}
		}
		return null;
	}
	
	public boolean addStop(Stop stop) {
		if (getStops().add(stop)) {
			return true;
		}
		return false;
	}
	
	public boolean removeStop(Stop stop) {
		return getStops().remove(stop);
	}
	
	public boolean removeStop(int stopNumber) {
		Stop stop = findStop(stopNumber);
		
		if (stop == null) {	return false; }
		
		if (!getStops().remove(stop)) {
			System.err.println("Stop with number " + stopNumber + " does not exist.");
			return false;
		}
		return true;
	}
	
	public String toString() {
		return this.networkName + ", " + this.countryID + "(" + this.networkType + ")";
	}
	
	public ArrayList<Stop> getStops() {
		return stops;
	}
	
	public ArrayList<Way> getWays() {
		return ways;
	}
	
	public void setStops(ArrayList<Stop> stops) {
		this.stops = stops;
	}
	
	public void setWays(ArrayList<Way> ways) {
		this.ways = ways;
	}
	
	public int getNetworkID() {
		return networkID;
	}
	
	public String getNetworkName() {
		return networkName;
	}
	
	public String getNetworkType() {
		return networkType;
	}
	
	public String getCountryID() {
		return countryID;
	}
}
