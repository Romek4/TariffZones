package tariffzones.tariffzonesprocessor.greedy;

import java.util.ArrayList;

import tariffzones.model.BusStop;
import tariffzones.model.Way;

public class Zone {
	private double e;
	private ArrayList<BusStop> stopsInZone, stopsConnectedWithZone;
	
	public Zone() {
		
	}
	
	public Zone(ArrayList<BusStop> stopsInZone) {
		this.stopsInZone = stopsInZone;
		e = stopsInZone.get(0).getNumberOfCustomers();
	}

	public void countE() {
		e = 0;
		int s = 0;
		double d = 0;
		for (BusStop busStop : stopsInZone) {
			for (int i = 0; i < busStop.getPartOfWays().size(); i++) {
				Way way = busStop.getPartOfWays().get(i);
				if (way.getStartPoint().equals(busStop)) {
					if (!stopsInZone.contains(way.getEndPoint())) {
						s++;
						d += way.getPrice();
					}
				}
				else if (way.getEndPoint().equals(busStop)) {
					if (!stopsInZone.contains(way.getStartPoint())) {
						s++;
						d += way.getPrice();
					}
				}
			}
		}
		e = d/((double)stopsInZone.size()*s);
	}
	
	public double getE() {
		return this.e;
	}
	
	public void setE(double e) {
		this.e = e;
	}
	
	public ArrayList<BusStop> getStopsInZone() {
		return this.stopsInZone;
	}
	
	public boolean addStop(BusStop busStop) {
		if (stopsInZone == null) {
			stopsInZone = new ArrayList<>();
		}
		busStop.setZone(this);
		return stopsInZone.add(busStop);
	}

	public void mergeWith(Zone zone2) {
		for (BusStop busStop : zone2.getStopsInZone()) {
			this.stopsInZone.add(busStop);
		}
		countE();
	}
}
