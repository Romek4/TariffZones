package tariffzones.tariffzonesprocessor.greedy;

import java.util.ArrayList;
import java.util.Arrays;

import tariffzones.model.Stop;
import tariffzones.model.Way;

public class TariffZonesProblemSolver {
	private ArrayList<Zone> zones;
	private static double[][] distanceMatrix;
	private static ArrayList<Stop> stops;
	
	public TariffZonesProblemSolver() {
		this.zones = new ArrayList<>();
	}
	
	public void runGreedyAlgorithm(ArrayList<Stop> stops, int numberOfZones, double[][] distanceMatrix, boolean useOnlyNumberOfHabs, boolean useNumberOfHabs) {
		TariffZonesProblemSolver.distanceMatrix = distanceMatrix;
		TariffZonesProblemSolver.stops = stops;
		for (Stop busStop : stops) {
			Zone zone = new Zone();
			zone.addStop(busStop);
			zone.setE(countE(zone, useOnlyNumberOfHabs, useNumberOfHabs));
			zones.add(zone);
		}
		
		while (zones.size() != numberOfZones) {
//			System.out.println("Aktualne zony: ");
//			for (Zone zone : zones) {
//				System.out.println(zone.toString());
//			}
			
			Zone minZone1 = getZoneWithMinimumE(zones);
			zones.remove(minZone1);
			Zone minZone2 = getNearestZoneWithMinimumE(minZone1.getStopsConnectedWithZoneList());
			
//			System.out.println("Mergujem zónu " + minZone2.toString() + " so zonou " + minZone1.toString());
			minZone2.mergeWith(minZone1);
			minZone2.setE(countE(minZone2, useOnlyNumberOfHabs, useNumberOfHabs));
//			System.out.println("Nová zóna: " + minZone2.toString());
//			System.out.println("________________________________________________");
//			System.out.println("Aktualne zony: ");
//			for (Zone zone : zones) {
//				System.out.println(zone.toString());
//			}
//			for (BusStop busStop : stops) {
//				Zone zone = new Zone();
//				zone.addStop(busStop);
//				zone.countE();
////				zone.setE(busStop.getNumberOfCustomers());//TODO: iný výpoèet e - toto je príliš jednoduché a nefunguje to správne. Alebo že by som to zle nakódil? A èo v prípade mestskej siete, kde nemám poèty obyvate¾ov zastávok?
//				zones.add(zone);
//			}
		}
	}

	private Zone getNearestZoneWithMinimumE(ArrayList<Stop> stopsConnectedWithZoneList) {
		if (stopsConnectedWithZoneList == null || stopsConnectedWithZoneList.size() < 1) {
			return null;
		}
		
		Zone nearestMinZone = new Zone();
		nearestMinZone.setE(Double.MAX_VALUE);
		for (Stop busStop : stopsConnectedWithZoneList) {
			if (busStop.getZone().getE() < nearestMinZone.getE()) {
				nearestMinZone = busStop.getZone();
			}
		}
		return nearestMinZone;
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

	public double countE(Zone zone, boolean useOnlyNumberOfHabs, boolean useNumberOfHabs) {
		double sum = 0;
		for (Stop stopInsideZone : zone.getStopsInZone()) {
			for (Stop stopOutsideZone : zone.getStopsConnectedWithZoneList()) {
				if (useOnlyNumberOfHabs) {
					stopInsideZone.getNumberOfCustomers();
				}
				else {
					if (useNumberOfHabs) {
						sum += TariffZonesProblemSolver.getDistance(stopInsideZone, stopOutsideZone)*stopInsideZone.getNumberOfCustomers(); //d(i, j) z matice Djikstra
					}
					else { 
						sum += TariffZonesProblemSolver.getDistance(stopInsideZone, stopOutsideZone); //d(i, j) z matice Djikstra
					}
				}
			}
		}
		return sum/((double)zone.getStopsInZone().size()*zone.getStopsConnectedWithZoneList().size());
	}
	
	public ArrayList<Zone> getZones() {
		return this.zones;
	}
	
	public static double getDistance(Stop from, Stop to) throws IndexOutOfBoundsException {
		if (TariffZonesProblemSolver.distanceMatrix == null) {
			return -999999999;
		}
		
		int i = getStopListIndex(from);
		int j = getStopListIndex(to);
		
		return TariffZonesProblemSolver.distanceMatrix[i][j];
	}

	private static int getStopListIndex(Stop stop) {
		for (int i = 0; i < stops.size(); i++) {
			if (stops.get(i).equals(stop)) {
				return i;
			}
		}
		return -1;
	}
}
