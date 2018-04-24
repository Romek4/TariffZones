package tariffzones.basicobjects;

public class Counter {

	private int[][] ODMatrix;
	private int[][] nodeCountMatrix; //number of nodes on path from i to j
	private double[][] distanceMatrix, priceMatrix;
	private double[] prices = new double[]{0.55, 0.65, 0.8}; //TODO: prices
	
	
	//public double[][] countPriceMatrix(int[][] ODMatrix, int[][] nodeCountMatrix, double[] prices) {
	public void countPriceMatrix() {
		
		double[][] priceMatrix = new double[nodeCountMatrix.length][nodeCountMatrix.length];
		
		
		double price = 0;
		for (int i = 0; i < priceMatrix.length; i++) {
			for (int j = 0; j < priceMatrix.length; j++) {
				price = 0;
				if (nodeCountMatrix[i][j] < 0) {
					price = -1;
				}
				else if (nodeCountMatrix[i][j] <= 5) {
					price = prices[0]; //path up to 5 stops
				}
				else if (nodeCountMatrix[i][j] > 5) {
					price = prices[1]; //path with more than 5 stops
				}
				
				if (ODMatrix[i][j] == 1) {
					price += prices[2];
				}
				
				priceMatrix[i][j] = price;
			}
		}
	}
}
