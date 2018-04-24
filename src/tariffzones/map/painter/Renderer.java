package tariffzones.map.painter;

import java.awt.Graphics2D;

import org.jxmapviewer.JXMapViewer;

public interface Renderer<O> {
	public void paintObject(Graphics2D g, JXMapViewer map, O object);
}
