package tariffzones.map.mapComponents;

import java.awt.Graphics2D;

import org.jxmapviewer.JXMapViewer;

public interface Renderer<O> {
	public void paintO(Graphics2D g, JXMapViewer map, O object);
}
