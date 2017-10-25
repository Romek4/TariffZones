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
	public double getPrice() { //TODO: èo keï bude chcie ráta maticu vzdialeností pod¾a km a nie pod¾a èasov - viac konštruktorov a nejakı prepínaè, ktorı by urèoval èo vráti táto metóda ako price, ale èo keï budem ma k hrane aj km, aj èas, aj poèet cestujúcich... ?
		return this.mins;
	}
}
