package tariffzones.model;

import java.util.ArrayList;

import tariffzones.tariffzonesprocessor.djikstra.Node;

public class Network {
	
	private int networkID;
	private String networkName;
	private String networkType;
	private String countryID;
	
	private ArrayList<Stop> stops;
	private ArrayList<Way> ways;
	
	public Network(String networkName, String networkType, String countryID) {
		this.networkName = networkName;
		this.networkType = networkType;
		this.countryID = countryID;
	}
	
	public Network(int networkID, String networkName, String networkType, String countryID) {
		this.networkID = networkID;
		this.networkName = networkName;
		this.networkType = networkType;
		this.countryID = countryID;
	}
	
	public boolean addWay(Way way) {
		if (getWays().add(way)) {
			return true;
		}
		return false;
		
	}

	public boolean removeWay(Way way) {
		way.getStartPoint().getPartOfWays().remove(way);
		way.getEndPoint().getPartOfWays().remove(way);
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
		for (Node node : getStops()) {
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
		if (stops == null) {
			stops = new ArrayList<>();
		}
		return stops;
	}
	
	public ArrayList<Way> getWays() {
		if (ways == null) {
			ways = new ArrayList<>();
		}
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
	
	public void setNetworkID(int networkID) {
		this.networkID = networkID;
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
