package tariffzones.model;

public class Way {
	private int id;
	private MyCoordinate startPoint, endPoint;
	private double mins;
	
	public Way(int id, MyCoordinate startPoint, MyCoordinate endPoint, double mins) {
		this.id = id;
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.mins = mins;
	}
	
	public int getId() {
		return this.id;
	}
	
	public MyCoordinate getStartPoint() {
		return this.startPoint;
	}
	
	public MyCoordinate getEndPoint() {
		return this.endPoint;
	}
}
