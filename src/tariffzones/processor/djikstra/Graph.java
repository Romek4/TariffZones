package tariffzones.processor.djikstra;

import java.util.ArrayList;

public class Graph {
	private ArrayList<Node> nodes;
	private ArrayList<Edge> edges;
	
	public Graph(ArrayList<Node> nodes, ArrayList<Edge> edges) {
		this.nodes = nodes;
		this.edges = edges;
	}
	
	public ArrayList<Node> getNodes() {
		return this.nodes;
	}
	
	public ArrayList<Edge> getEdges() {
		return this.edges;
	}
	
	public int getNumberOfNodes() {
		return this.nodes.size();
	}
	
	public int getNumberOfEdges() {
		return this.edges.size();
	}
}
