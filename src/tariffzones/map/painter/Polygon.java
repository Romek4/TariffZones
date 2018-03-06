package tariffzones.map.painter;

import java.awt.Color;
import java.util.ArrayList;

import org.jxmapviewer.viewer.GeoPosition;

public interface Polygon {
	public ArrayList<GeoPosition> getGeoPositions();
	public Color getColor();
}
