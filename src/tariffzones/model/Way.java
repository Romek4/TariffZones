package tariffzones.model;

public class Way {
	private String name;
	private Coordinate startPoint, endPoint;
	
	public Way(String name, Coordinate startPoint, Coordinate endPoint) {
		this.name = name;
		this.startPoint = startPoint;
		this.endPoint = endPoint;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Coordinate getStartPoint() {
		return this.startPoint;
	}
	
	public Coordinate getEndPoint() {
		return this.endPoint;
	}
}
