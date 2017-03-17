package tariffzones.model;

public class BusStop {
	private int number;
	private String name;
	private MyCoordinate coordinate;
	
	public BusStop(int number, String name, MyCoordinate coordinate) {
		this.number = number;
		this.name = name;
		this.coordinate = coordinate;
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
}
