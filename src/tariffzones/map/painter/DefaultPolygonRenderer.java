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

import tariffzones.model.Stop;
import tariffzones.tariffzonesprocessor.greedy.ConcaveHull;
import tariffzones.tariffzonesprocessor.greedy.GrahamScan;
import tariffzones.tariffzonesprocessor.greedy.QuickHull;
import tariffzones.tariffzonesprocessor.greedy.Zone;
import voronoi.Edge;
import voronoi.Site;

public class DefaultPolygonRenderer implements Renderer<Zone> {

	@Override
	public void paintO(Graphics2D g, JXMapViewer map, Zone zone) {
	
		java.awt.Polygon poly = new java.awt.Polygon();
		
		GrahamScan hull = new GrahamScan();
		
		ArrayList<GeoPosition> points;
		
		for(GeoPosition gp : zone.getGeoPositions()) {
			Point2D pt = null;
			
			pt = map.getTileFactory().geoToPixel(gp, map.getZoom());
			poly.addPoint((int)pt.getX(), (int)pt.getY());
		}
		
//		for (Stop stop : zone.getStopsInZone()) {
//			points = new ArrayList<>();
//			poly = new java.awt.Polygon();
//			
//			for (Site site : stop.getRegionSites()) {
//				points.add(new GeoPosition(site.getX(), site.getY()));
//			}
//			
//			if (points.size() > 2) {
//				for (GeoPosition geoPosition : hull.getConvexHull(points)) {
//
//					Point2D p = map.getTileFactory().geoToPixel(new GeoPosition(geoPosition.getLatitude(), geoPosition.getLongitude()), map.getZoom());
//					poly.addPoint((int)p.getX(), (int)p.getY());
//					
//				}
//				
//				Color color = zone.getColor();
//				g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 197));
//				g.fill(poly); 
//				g.draw(poly); 
		
			Color color = zone.getColor();
			g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 197));
			g.fill(poly); 
			g.draw(poly);
//			}
		}
		
//		GeoPosition west = new GeoPosition(0, 180);
//		GeoPosition east = new GeoPosition(0, -180);
//		GeoPosition south = new GeoPosition(180, 0);
//		GeoPosition north = new GeoPosition(-180, 0);
//		
//		
//		for(GeoPosition gp : polygon.getGeoPositions()) {
//			Point2D pt = null;
//			
//			pt = map.getTileFactory().geoToPixel(gp, map.getZoom());
//			poly.addPoint((int)pt.getX(), (int)pt.getY());
//		}
		
//		Point2D pt = null, pt2 = null, pt3 = null, pt4 = null;
//		
//		pt = map.getTileFactory().geoToPixel(west, map.getZoom());
//		pt2 = map.getTileFactory().geoToPixel(south, map.getZoom());
//		pt3 = map.getTileFactory().geoToPixel(east, map.getZoom());
//		pt4 = map.getTileFactory().geoToPixel(north, map.getZoom());
//		
//		poly.addPoint((int)pt.getX()-25, (int)pt2.getY()+25); //bottom left corner
//		poly.addPoint((int)pt3.getX()+25, (int)pt2.getY()+25); //bottom right corner
//		poly.addPoint((int)pt3.getX()+25, (int)pt4.getY()-25); //top right corner
//		poly.addPoint((int)pt.getX()-25, (int)pt4.getY()-25); //top left corner
		
//		Point p1 = new Point((int)pt.getX(), (int)pt2.getY());
//		Point p2 = new Point((int)pt.getX(), (int)pt2.getY());
		
		
//		g.drawLine((int)p1.getX(), (int)p1.getY(), (int)p2.getX(), (int)p2.getY());
//		GeoPosition gp1 = new GeoPosition(west.getLatitude(), south.getLongitude());
//		GeoPosition gp2 = new GeoPosition(east.getLatitude(), north.getLongitude());
		
//		pt = map.getTileFactory().geoToPixel(gp1, map.getZoom());
//		pt2 = map.getTileFactory().geoToPixel(gp2, map.getZoom());
//		
//		g.drawLine((int)pt.getX(), (int)pt.getY(), (int)pt2.getX(), (int)pt2.getY());
//		g.drawOval((int)pt.getX(), (int)pt.getY(), 150, 150);
//		g.drawOval((int)pt2.getX(), (int)pt2.getY(), 150, 150);
//		
//		Rectangle2D rec = new Rectangle2D.Double((int)p1.getX(), (int)p1.getY(), (int)p2.getX(), (int)p2.getY());
//		for(GeoPosition gp : polygon.getGeoPositions()) {
//			pt = map.getTileFactory().geoToPixel(west, map.getZoom());
//			poly.addPoint((int)pt.getX(),(int)pt.getY());
//			pt = map.getTileFactory().geoToPixel(south, map.getZoom()); 
//			poly.addPoint((int)pt.getX(),(int)pt.getY());
//			pt = map.getTileFactory().geoToPixel(east, map.getZoom());
//			poly.addPoint((int)pt.getX(),(int)pt.getY());
//			pt = map.getTileFactory().geoToPixel(north, map.getZoom());
//			poly.addPoint((int)pt.getX(),(int)pt.getY());
//		}
		
//	}
	
	private void sortGeoPositions(ArrayList<GeoPosition> geoPositions) {
		GeoPosition west = new GeoPosition(180, 180);
		GeoPosition east = new GeoPosition(-180, -180);
		GeoPosition north = new GeoPosition(180, 180);
		GeoPosition south = new GeoPosition(-180, -180);
		
		for(GeoPosition gp : geoPositions) { 
			if (west.getLongitude() < gp.getLongitude()) {
				west = gp;
			}
			if (east.getLongitude() > gp.getLongitude()) {
				east = gp;
			}
			if (north.getLongitude() < gp.getLatitude()) {
				north = gp;
			}
			if (south.getLongitude() > gp.getLatitude()) {
				south = gp;
			}
		}
	}

}
