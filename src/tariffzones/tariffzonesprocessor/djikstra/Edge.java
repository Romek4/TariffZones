package tariffzones.tariffzonesprocessor.djikstra;

public interface Edge {

	public abstract Node getStartNode();
	public abstract Node getEndNode();
	public abstract double getPrice();
	
//	private Node startNode, endNode;
//	private double edgePrice;
//	
//	public Edge(Node startNode, Node endNode, double edgePrice) {
//		this.startNode = startNode;
//		this.endNode = endNode;
//		this.edgePrice = edgePrice;
//	}
//	
//	public Node getStartNode() {
//		return this.startNode;
//	}
//	
//	public Node getEndNode() {
//		return this.endNode;
//	}
//	
//	public double getEdgePrice() {
//		return this.edgePrice;
//	}
}
