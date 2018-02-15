package tariffzones.model;

import java.awt.Color;
import java.util.ArrayList;

import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;

import tariffzones.tariffzonesprocessor.djikstra.Node;
import tariffzones.tariffzonesprocessor.greedy.Zone;

public class Stop implements Node, Waypoint, ObjectState {
	private int id = -1; // id from db
	private int number;
	private String name;
	private int numberOfCustomers; //if it's representing city
	private GeoPosition geoPosition;
	private ArrayList<Way> partOfWays;
	private ArrayList<Stop> connectedWithStopList;
	private Zone zone;
	private Network network;
	private State state = State.DEFAULT;
	
	public Stop(Network network, int number, String name, int numberOfHabitants, GeoPosition geoPosition) {
		this.network = network;
		this.number = number;
		this.name = name;
		this.numberOfCustomers = numberOfHabitants;
		this.geoPosition = geoPosition;
	}
	
	public Stop(Network network, int number, String name, int numberOfHabitants, double latitude, double longitude) {
		this.network = network;
		this.number = number;
		this.name = name;
		this.numberOfCustomers = numberOfHabitants;
		this.geoPosition = new GeoPosition(latitude, longitude);
	}
	
	public Stop(int id, Network network, int number, String name, int numberOfHabitants, double latitude, double longitude) {
		this.id = id;
		this.network = network;
		this.number = number;
		this.name = name;
		this.numberOfCustomers = numberOfHabitants;
		this.geoPosition = new GeoPosition(latitude, longitude);
	}
	
	public Stop(int number, String name, int numberOfHabitants, double latitude, double longitude) {
		this.network = network;
		this.number = number;
		this.name = name;
		this.numberOfCustomers = numberOfHabitants;
		this.geoPosition = new GeoPosition(latitude, longitude);
	}

	@Override
	public Object getKey() {
		return this.number;
	}
	
	@Override
	public String toString() {
		return this.number + "-" + this.name; 
	}

	public void addWay(Way way) {
		if (this.equals(way.getStartPoint())) {
			this.getConnectedWithStopList().add(way.getEndPoint());
		}
		else {
			this.getConnectedWithStopList().add(way.getStartPoint());
		}
		
		getPartOfWays().add(way);
	}
	
	public ArrayList<Stop> getConnectedWithStopList() {
		if (connectedWithStopList == null) {
			connectedWithStopList = new ArrayList<>();
		}
		
		return connectedWithStopList;
	}
	
	public ArrayList<Way> getPartOfWays() {
		if (partOfWays == null) {
			partOfWays = new ArrayList<>();
		}
		
		return partOfWays;
	}
	
	public int getId() {
		return this.id;
	}
	
	public int getNumber() {
		return this.number;
	}
	
	public void setNumber(int number) {
		if (number > 0) {
			this.number = number;
		}
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getNumberOfCustomers() {
		return this.numberOfCustomers;
	}
	
	public void setNumberOfCustomers(int numberOfCustomers) {
		this.numberOfCustomers = numberOfCustomers;
	}
	
	public Zone getZone() {
		return zone;
	}
	
	public void setZone(Zone zone) {
		this.zone = zone;
	}

	@Override
	public GeoPosition getPosition() {
		return geoPosition;
	}

	public double getLatitude() {
		if (geoPosition != null) {
			return geoPosition.getLatitude();
		}
		return -999999;
	}
	
	public double getLongitude() {
		if (geoPosition != null) {
			return geoPosition.getLongitude();
		}
		return -999999;
	}
	
	public Network getNetwork() {
		return network;
	}

	@Override
	public State getState() {
		return state;
	}

	@Override
	public void setState(State state) {
		this.state = state;
	}
}
