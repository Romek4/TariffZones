package tariffzones.map.painter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import org.jxmapviewer.JXMapViewer;

public class DefaultWayRenderer implements Renderer<tariffzones.basicobjects.Way> {

	protected Color color = Color.BLACK;; 
	protected int lineStroke = 1;

	public DefaultWayRenderer() {
	}
	
	public DefaultWayRenderer(Color color) {
		this.color = color;
	}

	@Override
	public void paintObject(Graphics2D g, JXMapViewer map, tariffzones.basicobjects.Way way) {
		Point2D startPoint = map.getTileFactory().geoToPixel(way.getStartPosition(), map.getZoom());
		Point2D endPoint = map.getTileFactory().geoToPixel(way.getEndPosition(), map.getZoom());
		
		Line2D line = new Line2D.Double(startPoint, endPoint);
		g.setStroke(new BasicStroke(lineStroke));
		g.setColor(color);
		g.draw(line);
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public void setLineStroke(int lineStroke) {
		this.lineStroke = lineStroke;
	}

}
