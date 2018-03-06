package tariffzones.tariffzonesprocessor.djikstra;

public interface Edge {

	public abstract Node getStartNode();
	public abstract Node getEndNode();
	public abstract double getPrice();
	
}
