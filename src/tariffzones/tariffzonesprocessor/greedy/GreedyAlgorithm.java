package tariffzones.tariffzonesprocessor.greedy;

import java.util.ArrayList;
import java.util.Arrays;

import tariffzones.model.BusStop;
import tariffzones.model.Way;

public class GreedyAlgorithm {
	private ArrayList<Zone> zones;
	
	public GreedyAlgorithm() {
		this.zones = new ArrayList<>();
	}
	
	public void runAlgorithm(ArrayList<BusStop> stops, int numberOfZones) {
		for (BusStop busStop : stops) {
			Zone zone = new Zone();
			zone.addStop(busStop);
			zone.countE();
//			zone.setE(busStop.getNumberOfCustomers());//TODO: iný výpoèet e - toto je príliš jednoduché a nefunguje to správne. Alebo že by som to zle nakódil? A èo v prípade mestskej siete, kde nemám poèty obyvate¾ov zastávok?
			zones.add(zone);
		}
		
		while (zones.size() != numberOfZones) {
			Zone minZone1 = getZoneWithMinimumE(zones);
			zones.remove(minZone1);
			Zone minZone2 = getZoneWithMinimumE(zones);
			
			minZone2.mergeWith(minZone1);
		}
	}

	private Zone getZoneWithMinimumE(ArrayList<Zone> zones) {
		Zone minZone = zones.get(0);
		for (Zone zone : zones) {
			if (zone.getE() < minZone.getE()) {
				minZone = zone;
			}
		}
		return minZone;
	}
	
	public ArrayList<Zone> getZones() {
		return this.zones;
	}
}
