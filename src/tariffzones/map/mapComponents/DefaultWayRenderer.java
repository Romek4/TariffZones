package tariffzones.map.mapComponents;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import org.jxmapviewer.JXMapViewer;

public class DefaultWayRenderer implements Renderer<tariffzones.model.Way> {

	@Override
	public void paintO(Graphics2D g, JXMapViewer map, tariffzones.model.Way way) {
		Point2D startPoint = map.getTileFactory().geoToPixel(way.getStartPosition(), map.getZoom());
		Point2D endPoint = map.getTileFactory().geoToPixel(way.getEndPosition(), map.getZoom());
		
		Line2D line = new Line2D.Double(startPoint, endPoint);
		g.draw(line);
	}

}
