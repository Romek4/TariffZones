package tariffzones.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

import tariffzones.basicobjects.Network;
import tariffzones.basicobjects.Stop;
import tariffzones.basicobjects.Way;
import tariffzones.basicobjects.Zone;
import tariffzones.processor.djikstra.Djikstra;
import tariffzones.processor.djikstra.Graph;

public class TariffZonesProblemSolver {

	private ArrayList stops;
	private ArrayList ways;
	private Djikstra djikstra;
	private SolverParameters params;
	
	private double dev1, dev2, dev3;
	private int[][] used;
	
	public TariffZonesProblemSolver(Network network, SolverParameters params) {
		if (network != null) {
			this.stops = network.getStops();
			this.ways = network.getWays();
			this.djikstra = new Djikstra(new Graph(stops, ways));
			this.used = new int[stops.size()][stops.size()];
		}
		this.params = params;
	}
	
	public ArrayList<Zone> run() {
		try {
			if (params.oldPrices == null) {
				return runGreedyAlgorithm();
			}
			else {
				return runGreedyAlgorithmWithPrices();
			}
		} catch(Exception e) {
			return null;
		}
	}
	
	private ArrayList<Zone> runGreedyAlgorithm() {
		
		double[][] distanceMatrix = null;
		if (djikstra != null) {
			djikstra.runDjikstra();
			distanceMatrix = djikstra.getDistanceMatrix();
		}
		
		ArrayList<Zone> zones = new ArrayList<>();
		Stop stop;
		Zone zone;
		for (Object o : stops) {
			stop = (Stop) o;
			zone = new Zone();
			zone.addStop(stop);
			zone.setE(countE(zone, distanceMatrix, params));
			zones.add(zone);
		}
		
		while (zones.size() != params.numberOfZones) {
			Zone minZone1 = getZoneWithMinimumE(zones);
			zones.remove(minZone1);
			Zone minZone2 = getNearestZoneWithMinimumE(minZone1.getStopsConnectedWithZoneList());
			minZone2.mergeWith(minZone1);
			minZone2.setE(countE(minZone2, distanceMatrix, params));
		}
		
		return zones;
	}
	
	private ArrayList<Zone> runGreedyAlgorithmWithPrices() {
		
		double[][] distanceMatrix = null;
		double[][] oldPricesMatrix = null;
		
		if (djikstra != null) {
			djikstra.runDjikstra();
			distanceMatrix = djikstra.getDistanceMatrix();
			if (params.useDistanceMatrixForPrices) {
				oldPricesMatrix = countOldPriceMatrix(params.oldPrices, distanceMatrix);
			}
			else {
				oldPricesMatrix = countOldPriceMatrix(params.oldPrices, djikstra.getNodeCountMatrix());
			}
		}
		
		ArrayList<Zone> zones = new ArrayList<>();
		Stop stop;
		Zone zone;
		//STEP 1
		for (Object o : stops) {
			stop = (Stop) o;
			zone = new Zone();
			zone.addStop(stop);
			zone.setE(countE(zone, distanceMatrix, params));
			zones.add(zone);
		}
		
		int[] maxPriceDifIndexes = new int[2];
		Zone[] neighboringZones = new Zone[2];
		while (zones.size() != params.numberOfZones) {
			
			
			//STEP 2
			maxPriceDifIndexes = findMaximumPriceDifference(oldPricesMatrix, distanceMatrix, params);
			
			//STEP 3
			neighboringZones = findNeighboringZones(maxPriceDifIndexes[0], maxPriceDifIndexes[1]);
			
			if (neighboringZones[0] != null && neighboringZones[1] != null) {
				zones.remove(neighboringZones[0]);
				neighboringZones[1].mergeWith(neighboringZones[0]);
				neighboringZones[1].setE(countE(neighboringZones[1], distanceMatrix, params));
			}
		}
		
		countDevs(oldPricesMatrix, distanceMatrix, params);
		
		return zones;
	}
	
	public void countDevs(double[][] oldPricesMatrix, double[][] distanceMatrix, SolverParameters params) {
		dev1 = Double.MIN_VALUE;
		dev2 = 0;
		dev3 = 0;
		double odMatrixSum = 0;

		double priceDif = 0;
		double[][] newPrices = countNewPriceMatrix(oldPricesMatrix, params.f1, params.f2);
		for (int i = 0; i < oldPricesMatrix.length; i++) {
			for (int j = 0; j < oldPricesMatrix.length; j++) {
				
				if(i == j) { continue; }
				
				if (params.ODMatrix != null) {
					priceDif = Math.abs(oldPricesMatrix[i][j] - newPrices[i][j]);
					odMatrixSum += params.ODMatrix[i][j]; //dev2
					dev3 += Math.pow(priceDif, 2)*params.ODMatrix[i][j]; //dev3
					priceDif = priceDif*params.ODMatrix[i][j];
				}
				else {
					if (params.countODMatrix) {
						Stop stopI = (Stop)stops.get(i);
						Stop stopJ = (Stop)stops.get(j);
						double od = (stopI.getNumberOfCustomers()*stopJ.getNumberOfCustomers())/Math.pow(distanceMatrix[i][j], 2);
						priceDif = Math.abs(oldPricesMatrix[i][j] - newPrices[i][j]);
						odMatrixSum += od; //dev2
						dev3 += Math.pow(priceDif, 2)*od; //dev3
						priceDif = priceDif*od;
					}
					else {
						priceDif = Math.abs(oldPricesMatrix[i][j] - newPrices[i][j]);
						dev3 += Math.pow(priceDif, 2);
					}
				}
				
				//dev1
				if (priceDif > dev1) {
					dev1 = priceDif;
				}
				
				//dev2
				dev2 += priceDif;
				
			}
		}
		
		//dev2
		if (odMatrixSum != 0) {
			dev2 = dev2/odMatrixSum;
		}
		else {
			dev2 = -99999;
		}
		
		System.out.println("dev1 = " + dev1 + ", dev2 = " + dev2 + ", dev3 = " + dev3);
		
	}
	
	private double[][] countNewPriceMatrix(double[][] oldPrices, double f1, double f2) {
		double[][] newPrices = new double[oldPrices.length][oldPrices.length];
		for (int i = 0; i < oldPrices.length; i++) {
			for (int j = 0; j < oldPrices.length; j++) {
				newPrices[i][j] = countNewPrice(i, j, f1, f2);
			}
		}
		
		return newPrices;
	}
	
	private Zone[] findNeighboringZones(int from, int to) {
		double minESum = Double.MAX_VALUE;
		ArrayList<Way> usedWays = getUsedWays(djikstra.getEdgesOnShortestPath(from, to));
		Zone zoneR = null, zoneS = null;
		Zone[] resultZones = new Zone[2];
		if (usedWays != null) {
			for (Way way : usedWays) {
				zoneR = way.getStartPoint().getZone();
				zoneS = way.getEndPoint().getZone();
				
				if ((zoneR != null && zoneS != null) && !zoneR.equals(zoneS)) {
					if ((zoneR.getE() + zoneS.getE()) < minESum) {
						minESum = zoneR.getE() + zoneS.getE();
						resultZones[0] = zoneR;
						resultZones[1] = zoneS;
					}
				}
			}
		}
		
		return resultZones;
	}

	private int[] findMaximumPriceDifference(double[][] oldPricesMatrix, double[][] distanceMatrix, SolverParameters params) {
		double g = Double.MIN_VALUE;
		double priceDif = 0;
		int[] indexes = new int[2];
		
		for (int i = 0; i < oldPricesMatrix.length; i++) {
			for (int j = 0; j < oldPricesMatrix.length; j++) {
				
				if(i == j) { continue; }
				
				if (params.ODMatrix != null) {
					priceDif = Math.abs(oldPricesMatrix[i][j] - countNewPrice(i, j, params.f1, params.f2))*params.ODMatrix[i][j];
				}
				else {
					if (params.countODMatrix) {
						Stop stopI = (Stop)stops.get(i);
						Stop stopJ = (Stop)stops.get(j);
						double od = (stopI.getNumberOfCustomers()*stopJ.getNumberOfCustomers())/Math.pow(distanceMatrix[i][j], 2);
						priceDif = Math.abs(oldPricesMatrix[i][j]- countNewPrice(i, j, params.f1, params.f2))*od;
					}
					else {
						priceDif = Math.abs(oldPricesMatrix[i][j] - countNewPrice(i, j, params.f1, params.f2));
					}
				}
				
				if (priceDif > g && used[i][j] != 1) {
					indexes[0] = i;
					indexes[1]  = j;
					g = priceDif;
				}
			}
		}
		
		if (params.ODMatrix != null) {
			used[indexes[0]][indexes[1]] = 1;
		}
		else {
			used[indexes[0]][indexes[1]] = 1;
//			used[indexes[1]][indexes[0]] = 1;
		}
		
		return indexes;
	}
	
	private double countNewPrice(int i, int j, double f1, double f2) {
		
		double price = 0;
		if (i != j) {
			price = f1;
		}
		
		ArrayList<Way> usedWays = getUsedWays(djikstra.getEdgesOnShortestPath(i, j));
		for (Way way: usedWays) {
			if (!way.getStartPoint().getZone().equals(way.getEndPoint().getZone())) {
				
//				if (usedWays != null && usedWays.contains(way)) {
					price += f2;
//				}
			}
		}
		
		return price;
	}
	
	private ArrayList<Way> getUsedWays(Stack<Integer> nodesTraveled) {
		ArrayList<Way> usedWays = new ArrayList<>();
		if (nodesTraveled != null) {
			Stop startStop = (Stop) stops.get(nodesTraveled.pop());
			Stop endStop;
			
			int stackSize = nodesTraveled.size();
			for (int i = 0; i < stackSize; i++) {
				endStop = (Stop) stops.get(nodesTraveled.pop());
				for (Way way : startStop.getPartOfWays()) {
					if (way.getStartPoint().equals(endStop) || way.getEndPoint().equals(endStop)) {
						usedWays.add(way);
					}
				}
				startStop = endStop;
			}
		}
		
		return usedWays;
	}

	private Zone getNearestZoneWithMinimumE(ArrayList<Stop> stopsConnectedWithZoneList) {
		if (stopsConnectedWithZoneList == null || stopsConnectedWithZoneList.size() < 1) {
			return null;
		}
		
		Zone nearestMinZone = new Zone();
		nearestMinZone.setE(Double.MAX_VALUE);
		for (Stop stop : stopsConnectedWithZoneList) {
			if (stop.getZone().getE() < nearestMinZone.getE()) {
				nearestMinZone = stop.getZone();
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

	private double countE(Zone zone, double[][] distanceMatrix, SolverParameters params) {
		double sum = 0;
		for (Stop stopInsideZone : zone.getStopsInZone()) {
			if (!params.useDistance && params.useNumberOfHabs && params.ODMatrix == null) {
				sum += stopInsideZone.getNumberOfCustomers();
			}
			else {
				for (Stop stopOutsideZone : zone.getStopsConnectedWithZoneList()) {
					if (params.useDistance && !params.useNumberOfHabs && params.ODMatrix == null) {
						sum += getDistance(stopInsideZone, stopOutsideZone, distanceMatrix); //d(i, j) z matice Djikstra
					}
					else if (params.useDistance && params.useNumberOfHabs && params.ODMatrix == null) {
						sum += getDistance(stopInsideZone, stopOutsideZone, distanceMatrix)*stopInsideZone.getNumberOfCustomers(); //d(i, j) z matice Djikstra
					}
					//TODO: ODMatrix != null
				}
			}
		}
		return sum/((double)zone.getStopsInZone().size()*zone.getStopsConnectedWithZoneList().size());
	}
	
	private double[][] countOldPriceMatrix(HashMap<Double, Double> pricesMap, int[][] nodeCountMatrix) {
		
		double[][] priceMatrix = new double[nodeCountMatrix.length][nodeCountMatrix.length];
		
		double price = 0;
		for (int i = 0; i < priceMatrix.length; i++) {
			for (int j = 0; j < priceMatrix.length; j++) {
				price = 0;
				
				if (nodeCountMatrix[i][j] < 0) {
					price = -1;
				}
				else {
					//it is necessary to have key-value pairs order by key from lowest to highest to have this running properly
					ArrayList sortedKeys = new ArrayList(pricesMap.keySet());
					Collections.sort(sortedKeys);
					for (Object o : sortedKeys) {
						double key = (double) o;
						if (nodeCountMatrix[i][j] <= key) {
							price = pricesMap.get(key);
							break;
						}
					}
				}
				
				//TODO: if matrix of moves is available
//				if (moveMatrix[i][j] == 1) {
//					price += prices[2];
//				}
				
				priceMatrix[i][j] = price;
			}
		}
		
		return priceMatrix;
	}
	
	private double[][] countOldPriceMatrix(HashMap<Double, Double> pricesMap, double[][] distanceMatrix) {
		
		double[][] priceMatrix = new double[distanceMatrix.length][distanceMatrix.length];
		
		double price = 0;
		for (int i = 0; i < priceMatrix.length; i++) {
			for (int j = 0; j < priceMatrix.length; j++) {
				price = 0;
				
				if (distanceMatrix[i][j] < 0) {
					price = -1;
				}
				else {
					//it is necessary to have key-value pairs order by key from lowest to highest to have this running properly
					ArrayList sortedKeys = new ArrayList(pricesMap.keySet());
					Collections.sort(sortedKeys);
					for (Object o : sortedKeys) {
						double key = (double) o;
						if (distanceMatrix[i][j] <= key) {
							price = pricesMap.get(key);
							break;
						}
					}
				}
				
				priceMatrix[i][j] = price;
			}
		}
		
		return priceMatrix;
	}

	public double getDistance(Stop from, Stop to, double[][] distanceMatrix) throws IndexOutOfBoundsException {
		if (distanceMatrix == null) {
			return -999999999;
		}
		
		int i = getStopListIndex(from);
		int j = getStopListIndex(to);
		
		return distanceMatrix[i][j];
	}

	private int getStopListIndex(Stop stop) {
		for (int i = 0; i < stops.size(); i++) {
			if (stops.get(i).equals(stop)) {
				return i;
			}
		}
		return -1;
	}
}
