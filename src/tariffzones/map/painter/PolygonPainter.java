package tariffzones.map.painter;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.AbstractPainter;

public class PolygonPainter<Zone> extends AbstractPainter<JXMapViewer> {

	private Renderer polygonRenderer = new DefaultPolygonRenderer();
	private Set<Zone> polygons;
	
	public PolygonPainter() {
		polygons = new HashSet<>();
	}
	
	public PolygonPainter(Set<Zone> polygons) {
		this.polygons = polygons;
	}
	
	@Override
	protected void doPaint(Graphics2D g, JXMapViewer map, int width, int heigth) {
		if (polygonRenderer == null)
		{
			return;
		}
		
		Rectangle viewportBounds = map.getViewportBounds();

		g.translate(-viewportBounds.getX(), -viewportBounds.getY());

		for (Zone p : getPolygons())
		{
			polygonRenderer.paintObject(g, map, p);
		}

		g.translate(viewportBounds.getX(), viewportBounds.getY());
	}
	
	public void setPolygonRenderer(Renderer polygonRenderer) {
		this.polygonRenderer = polygonRenderer;
	}
	
	public Set<Zone> getPolygons()	{
		if (polygons != null) {
			return Collections.unmodifiableSet(polygons);
		}
		
		return new HashSet<>();
	}

	public void setPolygons(Set<Zone> polygons) {
		this.polygons = polygons;
	}
}
