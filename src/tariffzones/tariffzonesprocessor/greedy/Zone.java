package tariffzones.tariffzonesprocessor.greedy;

import java.awt.Color;
import java.util.ArrayList;

import org.jxmapviewer.viewer.GeoPosition;

import tariffzones.map.painter.Polygon;
import tariffzones.model.Stop;
import tariffzones.model.Way;

public class Zone implements Polygon {
	private double e;
	private ArrayList<Stop> stopsInZone, stopsConnectedWithZone;
	private ArrayList<GeoPosition> geoPositions;
	private Color color;
	
	public Zone() {
	}
	
	public Zone(ArrayList<Stop> stopsInZone) {
		this.stopsInZone = stopsInZone;
		e = stopsInZone.get(0).getNumberOfCustomers();
	}
	
	public double getE() {
		return this.e;
	}
	
	public void setE(double e) {
		this.e = e;
	}
	
	public ArrayList<Stop> getStopsInZone() {
		return this.stopsInZone;
	}
	
	public void addStop(Stop busStop) {
		if (stopsInZone == null) {
			stopsInZone = new ArrayList<>();
		}
		
		busStop.setZone(this);
		stopsInZone.add(busStop);
		getGeoPositions().add(busStop.getPosition());
		
		if (getStopsConnectedWithZoneList().contains(busStop)) {
			getStopsConnectedWithZoneList().remove(busStop);
		}
		
		for (Stop stop : busStop.getConnectedWithStopList()) {
			if (!stopsInZone.contains(stop) && !getStopsConnectedWithZoneList().contains(stop)) {
				getStopsConnectedWithZoneList().add(stop);
			}
		}
	}

	public void mergeWith(Zone zone2) {
		for (Stop busStop : zone2.getStopsInZone()) {
			this.addStop(busStop);
		}
	}
	
	public String toString() {
		String s = "Zone e = " + e + ", pocet stanic zony = " + stopsInZone.size();
		for (Stop busStop : stopsInZone) {
			s += ", " + busStop.toString();
		}
		return s;
	}
	
	public ArrayList<Stop> getStopsConnectedWithZoneList() {
		if (stopsConnectedWithZone == null) {
			stopsConnectedWithZone = new ArrayList<>();
		}
		return stopsConnectedWithZone;
	}

	@Override
	public ArrayList<GeoPosition> getGeoPositions() {
		if (geoPositions == null) {
			geoPositions = new ArrayList<>();
		}
		return geoPositions;
	}

	@Override
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public void preparePolygonGeoPositions() {
		
//		GeoPosition west = new GeoPosition(0, 180);
//		GeoPosition east = new GeoPosition(0, -180);
//		GeoPosition south = new GeoPosition(180, 0);
//		GeoPosition north = new GeoPosition(-180, 0);
//		
//		for(GeoPosition gp : getGeoPositions()) { 
//			if (west.getLongitude() > gp.getLongitude()) {
//				west = gp;
//			}
//			if (east.getLongitude() < gp.getLongitude()) {
//				east = gp;
//			}
//			if (north.getLatitude() < gp.getLatitude()) {
//				north = gp;
//			}
//			if (south.getLatitude() > gp.getLatitude()) {
//				south = gp;
//			}
//		}
		
		ArrayList<GeoPosition> positions = getGeoPositions();
		
		for (Stop stop : getStopsConnectedWithZoneList()) {
			for (Way way : stop.getPartOfWays()) {
				Stop stopInZone = null;
				if (stopsInZone.contains(way.getStartPoint())) {
					stopInZone = way.getStartPoint();
				}
				else if (stopsInZone.contains(way.getEndPoint())) {
					stopInZone = way.getEndPoint();
				}
				
				if (stopInZone != null) {
					double latitude = (stop.getLatitude() + stopInZone.getLatitude())/2;
					double longitude = (stop.getLongitude() + stopInZone.getLongitude())/2;
					
					geoPositions.add(new GeoPosition(latitude, longitude));
				}
			}
		}
		
		for (Stop stop : getStopsInZone()) {
			geoPositions.add(new GeoPosition(stop.getLatitude()-0.015, stop.getLongitude()+0.015));
			geoPositions.add(new GeoPosition(stop.getLatitude()+0.015, stop.getLongitude()+0.015));
			geoPositions.add(new GeoPosition(stop.getLatitude()+0.015, stop.getLongitude()-0.015));
			geoPositions.add(new GeoPosition(stop.getLatitude()-0.015, stop.getLongitude()-0.015));
//			geoPositions.add(new GeoPosition(stop.getLatitude(), stop.getLongitude()-0.0015));
//			geoPositions.add(new GeoPosition(stop.getLatitude(), stop.getLongitude()+0.0015));
		}
		
		ConcaveHull hull = new ConcaveHull();
		geoPositions = hull.calculateConcaveHull(geoPositions, 5);
		
	}
	
	

}
