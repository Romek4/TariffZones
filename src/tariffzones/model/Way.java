package tariffzones.model;

import java.awt.geom.Line2D;

import org.jxmapviewer.viewer.GeoPosition;

import tariffzones.tariffzonesprocessor.djikstra.Edge;
import tariffzones.tariffzonesprocessor.djikstra.Node;

public class Way implements Edge, tariffzones.map.painter.Way, ObjectState {
	private int id;
	private Stop startPoint, endPoint;
	private double timeLength = 0;
	private double distance = 0;
	public static String PRICE_VALUE = "km";
	public static final String DISTANCE = "km";
	public static final String TIME = "time";
	
	private State state = State.DEFAULT;
	
	public Way(Stop startPoint, Stop endPoint, double distance, double timeLength) {
		this.id = id;
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.distance = distance;
		this.timeLength = timeLength;
		
		this.startPoint.addWay(this);
		this.endPoint.addWay(this);
	}
	
	public int getId() {
		return this.id;
	}
	
	public Stop getStartPoint() {
		return this.startPoint;
	}
	
	public Stop getEndPoint() {
		return this.endPoint;
	}
	
	public double getDistance() {
		return distance;
	}
	
	public double getTimeLength() {
		return timeLength;
	}
	
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public void setTimeLength(double timeLength) {
		this.timeLength = timeLength;
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
	public double getPrice() { //TODO: èo keï bude chcie ráta maticu vzdialeností pod¾a km a nie pod¾a èasov - viac konštruktorov a nejaký prepínaè, ktorý by urèoval èo vráti táto metóda ako price, ale èo keï budem ma k hrane aj km, aj èas, aj poèet cestujúcich... ?
		if (Way.PRICE_VALUE.equals(Way.DISTANCE)) {
			return this.distance;
		}
		else if (Way.PRICE_VALUE.equals(Way.TIME)) {
			return this.timeLength;
		}
		else {
			return -999999;
		}
	}
	
	
	public String toString() {
		return this.startPoint.getName() + " -> " + this.endPoint.getName();
	}

	@Override
	public GeoPosition getStartPosition() {
		if (startPoint != null) {
			return startPoint.getPosition();
		}
		return null;
	}

	@Override
	public GeoPosition getEndPosition() {
		if (endPoint != null) {
			return endPoint.getPosition();
		}
		return null;
	}
	
	@Override
	public State getState() {
		return state;
	}

	@Override
	public void setState(State state) {
		this.state = state;
	}
}
