package tariffzones.map.painter;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.AbstractPainter;

import tariffzones.processor.voronoi.Edge;

public class VoronoiEdgePainter extends AbstractPainter<JXMapViewer> {
	
	private ArrayList<Edge> edges;
	private VoronoiEdgeRenderer voronoiEdgeRenderer = new VoronoiEdgeRenderer();
	
	public VoronoiEdgePainter(ArrayList<Edge> edges) {
		this.edges = edges;
	}

	@Override
	protected void doPaint(Graphics2D g, JXMapViewer map, int width, int heigth) {
		if (voronoiEdgeRenderer == null)
		{
			return;
		}
		
		Rectangle viewportBounds = map.getViewportBounds();

		g.translate(-viewportBounds.getX(), -viewportBounds.getY());

		for (Edge edge : edges)
		{
			voronoiEdgeRenderer.paintObject(g, map, edge);
		}

		g.translate(viewportBounds.getX(), viewportBounds.getY());
		
	}

}
