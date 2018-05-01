package tariffzones.map.painter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import tariffzones.basicobjects.Way;

public class CustomersStreamsWayRenderer extends DefaultWayRenderer {
	
	private double maxStream;
	private double minStream;
	
	public CustomersStreamsWayRenderer(Color color) {
		super(color);
	}

	@Override
	public void paintObject(java.awt.Graphics2D g, org.jxmapviewer.JXMapViewer map, tariffzones.basicobjects.Way way) {
		Point2D startPoint = map.getTileFactory().geoToPixel(way.getStartPosition(), map.getZoom());
		Point2D endPoint = map.getTileFactory().geoToPixel(way.getEndPosition(), map.getZoom());
		endPoint.setLocation((startPoint.getX()+endPoint.getX())/2, (startPoint.getY()+endPoint.getY())/2);
		
		Line2D line = new Line2D.Double(startPoint, endPoint);
		g.setStroke(new BasicStroke(countStroke(way)));
		if (way.getStartPoint().getZone() != null) {
			g.setColor(way.getStartPoint().getZone().getColor());
		}
		else {
			g.setColor(super.color);
		}
		g.draw(line);
	}
	
	private float countStroke(Way way) {
		if (maxStream <= 0 && minStream <= 0) {
			return super.lineStroke;
		}
		
		float stroke = (float) (way.getDistance() - minStream);
		stroke = (float) ((stroke*100)/(maxStream-minStream));
		return stroke/10;
	}

	public void setMaxStrokeValue(double max) {
		this.maxStream = max;
	}
	
	public void setMinStrokeValue(double min) {
		this.minStream = min;
	}
}
