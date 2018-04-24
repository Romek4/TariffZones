package tariffzones.map.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;

import tariffzones.basicobjects.Stop;
import tariffzones.basicobjects.Zone;
import tariffzones.processor.hull.GrahamScan;
import tariffzones.processor.voronoi.Site;

public class DefaultPolygonRenderer implements Renderer<Zone> {

	@Override
	public void paintObject(Graphics2D g, JXMapViewer map, Zone zone) {
	
		java.awt.Polygon poly = new java.awt.Polygon();
		
		GrahamScan hull = new GrahamScan();
		
		ArrayList<GeoPosition> points;
		
//		for(GeoPosition gp : zone.getGeoPositions()) {
//			Point2D pt = null;
//			
//			pt = map.getTileFactory().geoToPixel(gp, map.getZoom());
//			poly.addPoint((int)pt.getX(), (int)pt.getY());
//		}
		
		for (Stop stop : zone.getStopsInZone()) {
			points = new ArrayList<>();
			poly = new java.awt.Polygon();
			
			for (Site site : stop.getRegionSites()) {
				points.add(new GeoPosition(site.getX(), site.getY()));
			}
			
			if (points.size() > 2) {
				for (GeoPosition geoPosition : hull.getConvexHull(points)) {

					Point2D p = map.getTileFactory().geoToPixel(new GeoPosition(geoPosition.getLatitude(), geoPosition.getLongitude()), map.getZoom());
					poly.addPoint((int)p.getX(), (int)p.getY());
					
				}
				
				Color color = zone.getColor();
				g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 140));
				g.fill(poly); 
				g.draw(poly); 
			}
		}
	}

}
