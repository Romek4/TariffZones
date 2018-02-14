package tariffzones.map.mapComponents;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.AbstractPainter;

public class WayPainter<W extends Way> extends AbstractPainter<JXMapViewer> {

	private Renderer wayRenderer = new DefaultWayRenderer();
	private Set<W> ways;
	
	public WayPainter() {
		ways = new HashSet<>();
	}
	
	public WayPainter(Set<W> ways) {
		this.ways = ways;
	}
	
	@Override
	protected void doPaint(Graphics2D g, JXMapViewer map, int width, int heigth) {
		if (wayRenderer == null)
		{
			return;
		}

		Rectangle viewportBounds = map.getViewportBounds();

		g.translate(-viewportBounds.getX(), -viewportBounds.getY());

		for (W w : getWays())
		{
			wayRenderer.paintO(g, map, w);
		}

		g.translate(viewportBounds.getX(), viewportBounds.getY());

	}
	
	public void setWayRenderer(Renderer wayRenderer) {
		this.wayRenderer = wayRenderer;
	}
	
	public Set<W> getWays()	{
		if (ways != null) {
			return Collections.unmodifiableSet(ways);
		}
		
		return new HashSet<>();
	}

	public void setWays(Set<W> ways) {
		this.ways = ways;
	}
	
}
