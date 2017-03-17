package tariffzones.model;

public class MyCoordinate {
	private double latitude;
	private double longitude;
	
	public MyCoordinate(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public double getLatitude() {
		return this.latitude;
	}
	
	public double getLongitude() {
		return this.longitude;
	}
}
