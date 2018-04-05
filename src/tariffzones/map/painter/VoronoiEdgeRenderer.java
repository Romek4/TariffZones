package tariffzones.map.painter;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;

import voronoi.Edge;
import voronoi.StdDraw;

public class VoronoiEdgeRenderer implements Renderer<Edge> {

	public VoronoiEdgeRenderer() {
	}

	@Override
	public void paintO(Graphics2D g, JXMapViewer map, Edge e) {

		if (e.getStart() == null || e.getEnd() == null) {
			return;
		}
		
		Point2D point = map.getTileFactory().geoToPixel(new GeoPosition(e.getStart().getX(), e.getStart().getY()), map.getZoom());
		Point2D point2 = map.getTileFactory().geoToPixel(new GeoPosition(e.getEnd().getX(), e.getEnd().getY()), map.getZoom());
		
		Line2D line = new Line2D.Double(point, point2);
		g.draw(line);
		
	}

}
