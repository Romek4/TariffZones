package tariffzones.map.painter;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.AbstractPainter;

public class PolygonPainter<P extends Polygon> extends AbstractPainter<JXMapViewer> {

	private Renderer polygonRenderer = new DefaultPolygonRenderer();
	private Set<P> polygons;
	
	public PolygonPainter() {
		polygons = new HashSet<>();
	}
	
	public PolygonPainter(Set<P> polygons) {
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

		for (P p : getPolygons())
		{
			polygonRenderer.paintO(g, map, p);
		}

		g.translate(viewportBounds.getX(), viewportBounds.getY());
	}
	
	public void setPolygonRenderer(Renderer polygonRenderer) {
		this.polygonRenderer = polygonRenderer;
	}
	
	public Set<P> getPolygons()	{
		if (polygons != null) {
			return Collections.unmodifiableSet(polygons);
		}
		
		return new HashSet<>();
	}

	public void setPolygons(Set<P> polygons) {
		this.polygons = polygons;
	}
}
