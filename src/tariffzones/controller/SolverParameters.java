package tariffzones.controller;

import java.util.HashMap;

public class SolverParameters {
	int numberOfZones;
	boolean useDistance;
	boolean useNumberOfHabs;
	int[][] ODMatrix;
	HashMap<Double, Double> oldPrices;
	boolean useCountOfStopsForPrices;
	boolean countODMatrix;
	double f1;
	double f2;
	int eFormula;
}
