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
//			zone.setE(busStop.getNumberOfCustomers());//TODO: in� v�po�et e - toto je pr�li� jednoduch� a nefunguje to spr�vne. Alebo �e by som to zle nak�dil? A �o v pr�pade mestskej siete, kde nem�m po�ty obyvate�ov zast�vok?
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
