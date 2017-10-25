package tariffzones.model;

import org.openstreetmap.gui.jmapviewer.Coordinate;

public class MyCoordinate extends Coordinate {
	private double latitude;
	private double longitude;
	
	public MyCoordinate(double latitude, double longitude) {
		super(latitude, longitude);
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
