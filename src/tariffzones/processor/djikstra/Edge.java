package tariffzones.processor.djikstra;

public interface Edge {

	public abstract Node getStartNode();
	public abstract Node getEndNode();
	public abstract double getPrice();
	
}
