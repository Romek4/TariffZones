package tariffzones.model;

import tariffzones.tariffzonesprocessor.djikstra.Edge;
import tariffzones.tariffzonesprocessor.djikstra.Node;

public class Way extends Edge {
	private int id;
	private BusStop startPoint, endPoint;
	private double mins;
	private double km;
	
	public Way(BusStop startPoint, BusStop endPoint, double km, double mins) {
		this.id = id;
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.km = km;
		this.mins = mins;
		
		this.startPoint.addWay(this);
		this.endPoint.addWay(this);
	}
	
	public int getId() {
		return this.id;
	}
	
	public BusStop getStartPoint() {
		return this.startPoint;
	}
	
	public BusStop getEndPoint() {
		return this.endPoint;
	}
	
	public double getKm() {
		return km;
	}
	
	public double getMins() {
		return mins;
	}

	@Override
	public Node getStartNode() {
		return this.startPoint;
	}

	@Override
	public Node getEndNode() {
		return this.endPoint;
	}

	@Override
	public double getPrice() { //TODO: �o ke� bude chcie� r�ta� maticu vzdialenost� pod�a km a nie pod�a �asov - viac kon�truktorov a nejak� prep�na�, ktor� by ur�oval �o vr�ti t�to met�da ako price, ale �o ke� budem ma� k hrane aj km, aj �as, aj po�et cestuj�cich... ?
		return this.mins;
	}
}
