package tariffzones.processor.djikstra;

import java.util.ArrayList;
import java.util.Stack;

public class Djikstra {
	private Graph graph;
	private double[] aT; //storing prices
    private int[] aX;
    private boolean[] aDef;
    private double [][] distanceMatrix;
    private int [][] nodeCountMatrix;
    
	public Djikstra(Graph graph) {
		this.graph = graph;
		aT = new double[ graph.getNumberOfNodes() + 1];
        aX = new int[ graph.getNumberOfNodes() + 1];
        aDef = new boolean[ graph.getNumberOfNodes() + 1];
	}

	public void runDjikstra() {
		distanceMatrix = new double [graph.getNumberOfNodes()][graph.getNumberOfNodes()];
		nodeCountMatrix = new int [graph.getNumberOfNodes()][graph.getNumberOfNodes()];
		double[] results = new double[2];
		
		for (int i = 0; i < graph.getNumberOfNodes(); i++) {
			for (int j = 0; j < graph.getNumberOfNodes(); j++) {
				results = getShortestPath(i, j);
				if (results == null) {
					distanceMatrix[i][j] = -1;
					nodeCountMatrix[i][j] = -1;
				} else {
					distanceMatrix[i][j] = results[0];
					nodeCountMatrix[i][j] = (int) results[1];
				}
			}
		}
	}
	
	public double[] getShortestPath(int startNodeIndex, int endNodeIndex) {
		double[] results = new double[2];
		
		for ( int i = 0; i < graph.getNumberOfNodes(); i++ )
        {
            aT[i] = Double.MAX_VALUE;
            aX[i] = 0;
            aDef[i] = false;
        }

		aT[startNodeIndex] = 0;
		aDef[startNodeIndex] = true;
		
        Node helpNode = graph.getNodes().get(startNodeIndex);
        Node endNode = graph.getNodes().get(endNodeIndex);
        
        int index = startNodeIndex;
        while ( !helpNode.getKey().equals(endNode.getKey()))
        {
            aDef[index] = true;
            
            for (int i = 0; i < graph.getNumberOfEdges(); i++)
            {
            	Edge edge = graph.getEdges().get(i);
                if (helpNode.getKey().equals(edge.getStartNode().getKey()))
                {
                    int j = graph.getNodes().indexOf(edge.getEndNode());
                    if (aT[j] > aT[index] + edge.getPrice() && !aDef[j])
                    {
                        aT[j] = aT[index] + edge.getPrice();
                        aX[j] = index;
                    }
                } 
                else if ( helpNode.getKey().equals(edge.getEndNode().getKey()))
                {
                    int j = graph.getNodes().indexOf(edge.getStartNode());
                    if (aT[j] > aT[index] + edge.getPrice() && !aDef[j])
                    {
                        aT[j] = aT[index] + edge.getPrice();
                        aX[j] = index;
                    }
                }
            }
            
            double min = Double.MAX_VALUE;
            for (int i = 0; i < graph.getNumberOfNodes(); i++)
            {
                if (aDef[i] == false && aT[i] < min)
                {
                    min = aT[i];
                    index = i;
                    helpNode = graph.getNodes().get(index);
                }
            }
            
            if (min == Double.MAX_VALUE) {
                return null; // edge does not exist
            }
        }
        
        if (helpNode.getKey().equals(endNode.getKey())) {
            results[0] = aT[endNodeIndex];
            results[1] = countNodesOnPath(startNodeIndex, endNodeIndex, aX);
            return results;
        }
		return null; //some error
	}
	
	public Stack<Integer> getEdgesOnShortestPath(int startNodeIndex, int endNodeIndex) {

		for ( int i = 0; i < graph.getNumberOfNodes(); i++ )
        {
            aT[i] = Double.MAX_VALUE;
            aX[i] = 0;
            aDef[i] = false;
        }

		aT[startNodeIndex] = 0;
		aDef[startNodeIndex] = true;
		
        Node helpNode = graph.getNodes().get(startNodeIndex);
        Node endNode = graph.getNodes().get(endNodeIndex);
        
        int index = startNodeIndex;
        while ( !helpNode.getKey().equals(endNode.getKey()))
        {
            aDef[index] = true;
            
            for (int i = 0; i < graph.getNumberOfEdges(); i++)
            {
            	Edge edge = graph.getEdges().get(i);
                if (helpNode.getKey().equals(edge.getStartNode().getKey()))
                {
                    int j = graph.getNodes().indexOf(edge.getEndNode());
                    if (aT[j] > aT[index] + edge.getPrice() && !aDef[j])
                    {
                        aT[j] = aT[index] + edge.getPrice();
                        aX[j] = index;
                    }
                } 
                else if ( helpNode.getKey().equals(edge.getEndNode().getKey()))
                {
                    int j = graph.getNodes().indexOf(edge.getStartNode());
                    if (aT[j] > aT[index] + edge.getPrice() && !aDef[j])
                    {
                        aT[j] = aT[index] + edge.getPrice();
                        aX[j] = index;
                    }
                }
            }
            
            double min = Double.MAX_VALUE;
            for (int i = 0; i < graph.getNumberOfNodes(); i++)
            {
                if (aDef[i] == false && aT[i] < min)
                {
                    min = aT[i];
                    index = i;
                    helpNode = graph.getNodes().get(index);
                }
            }
            
            if (min == Double.MAX_VALUE) {
                return null; // edge does not exist
            }
        }
        
        if (helpNode.getKey().equals(endNode.getKey())) {
        	Stack<Integer> nodesTraveled = new Stack<>();
        	int i = endNodeIndex;
        	nodesTraveled.push(i);
    		while (i != startNodeIndex) {
    			i = aX[i];
    			nodesTraveled.push(i);
    		}
            return nodesTraveled;
        }
		return null; //some error
	}
	
	private double countNodesOnPath(int startNodeIndex, int endNodeIndex, int[] paths) {
		int count = 0;
		int i = endNodeIndex;
		while (i != startNodeIndex) {
			i = paths[i];
			count++;
		}
		
		return count;
	}
	
	public static String matrixToString(double[][] matrix) {
		String s = "";
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				s += matrix[i][j] + " ";
			}
			s += "\n";
		}
		
		return s;
	}
	
	public double[][] getDistanceMatrix() {
		return this.distanceMatrix;
	}
	
	public int[][] getNodeCountMatrix() {
		return this.nodeCountMatrix;
	}
}
