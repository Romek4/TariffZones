package tariffzones.map.painter;

import org.jxmapviewer.viewer.GeoPosition;

public interface Way {
	public GeoPosition getStartPosition();
	public GeoPosition getEndPosition();
}
