package tariffzones.model;

import java.util.ArrayList;

import tariffzones.tariffzonesprocessor.djikstra.Node;
import tariffzones.tariffzonesprocessor.greedy.Zone;

public class BusStop extends Node{
	private int number;
	private String name;
	private MyCoordinate coordinate;
	private int numberOfCustomers; //if it's representing city
	private ArrayList<Way> partOfWays;
	private Zone zone;
	
	public BusStop(int number, String name, MyCoordinate coordinate, int numberOfHabitants) {
		this.number = number;
		this.name = name;
		this.coordinate = coordinate;
		this.numberOfCustomers = numberOfHabitants;
	}
	
	public int getNumber() {
		return this.number;
	}
	
	public String getName() {
		return this.name;
	}
	
	public MyCoordinate getCoordinate() {
		return this.coordinate;
	}
	
	public int getNumberOfCustomers() {
		return this.numberOfCustomers;
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
		if (partOfWays == null) {
			partOfWays = new ArrayList<>();
		}
		
		partOfWays.add(way);
	}
	
	public ArrayList<Way> getPartOfWays() {
		if (partOfWays == null) {
			partOfWays = new ArrayList<>();
		}
		
		return partOfWays;
	}
	
	public Zone getZone() {
		return zone;
	}
	
	public void setZone(Zone zone) {
		this.zone = zone;
	}
}
