package tariffzones.model;


import java.awt.geom.Point2D;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;

public class GeoToPointHelpConverter {
	
	public static JXMapViewer mapViewer = new JXMapViewer();
	
	public static Point2D convertGeoPositionToPoint(GeoPosition pos) {
		return mapViewer.getTileFactory().geoToPixel(pos, mapViewer.getZoom());
//		return mapViewer.convertGeoPositionToPoint(pos);
	}
}
