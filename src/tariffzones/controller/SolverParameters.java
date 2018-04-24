package tariffzones.controller;

import java.util.HashMap;

public class SolverParameters {
	int numberOfZones;
	boolean useDistance = true;
	boolean useNumberOfHabs = true;
	int[][] ODMatrix;
	HashMap<Double, Double> oldPrices;
	boolean useDistanceMatrixForPrices = true;
	boolean countODMatrix;
	double f1;
	double f2;
}
