package tariffzones.model;

public class BusStop {
	private String name;
	private Coordinate coordinate;
	
	public BusStop(String name, Coordinate coordinate) {
		this.name = name;
		this.coordinate = coordinate;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Coordinate getCoordinate() {
		return this.coordinate;
	}
}
