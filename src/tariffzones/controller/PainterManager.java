package tariffzones.controller;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.DefaultWaypointRenderer;
import org.jxmapviewer.viewer.WaypointPainter;

import tariffzones.map.painter.DefaultPolygonRenderer;
import tariffzones.map.painter.DefaultWayRenderer;
import tariffzones.map.painter.PolygonPainter;
import tariffzones.map.painter.StopWaypointRenderer;
import tariffzones.map.painter.VoronoiEdgePainter;
import tariffzones.map.painter.Way;
import tariffzones.map.painter.WayPainter;
import tariffzones.model.Network;
import tariffzones.model.Stop;
import tariffzones.tariffzonesprocessor.greedy.Zone;

public class PainterManager {
	private JXMapViewer map;
	private WaypointPainter<Stop> selectedWaypointPainter;
	private WayPainter<Way> selectedWayPainter;
	private WaypointPainter<Stop> waypointPainter;
	private List<WaypointPainter<Stop>> zoneWaypointPainters;
	private WayPainter<Way> wayPainter;
	private PolygonPainter<Zone> polygonPainter;
	private CompoundPainter<Painter<JXMapViewer>> compoundPainter;
	private List<Painter<JXMapViewer>> painters;
	
	StopWaypointRenderer stopWaypointRenderer;
	StopWaypointRenderer selectedWaypointRenderer;
	
	public PainterManager(JXMapViewer map) {
		this.map = map;
		
		waypointPainter = new WaypointPainter<>();
		stopWaypointRenderer = new StopWaypointRenderer(Color.YELLOW);
		stopWaypointRenderer.setEllipseDiameter(10);
		waypointPainter.setRenderer(stopWaypointRenderer);
		
		selectedWaypointPainter = new WaypointPainter<>();
		selectedWaypointRenderer = new StopWaypointRenderer(Color.RED);
		selectedWaypointRenderer.setEllipseDiameter(20);
		selectedWaypointPainter.setRenderer(selectedWaypointRenderer);
		
		wayPainter = new WayPainter<>();
		
		DefaultWayRenderer wayRenderer = new DefaultWayRenderer(Color.RED);
		wayRenderer.setLineStroke(3);
		selectedWayPainter = new WayPainter<>();
		selectedWayPainter.setWayRenderer(wayRenderer);
		
		polygonPainter = new PolygonPainter<>();
		polygonPainter.setPolygonRenderer(new DefaultPolygonRenderer());
		
		painters = new ArrayList<Painter<JXMapViewer>>();
		painters.add(polygonPainter);
		painters.add(selectedWaypointPainter);
		painters.add(waypointPainter);
		painters.add(wayPainter);
		painters.add(selectedWayPainter);
		
		compoundPainter = new CompoundPainter<>((List<? extends Painter<Painter<JXMapViewer>>>) painters);
	}
	
	public void addVoronoiEdgePainter(VoronoiEdgePainter voronoiEdgePainter) {
		painters.add(voronoiEdgePainter);
		compoundPainter = new CompoundPainter<>((List<? extends Painter<Painter<JXMapViewer>>>) painters);
	}
	
	public void addZoneWaypointPainter(List<Stop> stops, Color color) {
		if (zoneWaypointPainters == null) {
			zoneWaypointPainters = new ArrayList<>();
		}
		
		WaypointPainter<Stop> painter = new WaypointPainter<>();
		painter.setRenderer(new StopWaypointRenderer(color));
		painter.setWaypoints(new HashSet<>(stops));
		zoneWaypointPainters.add(painter);
		painters.add(painter);
		compoundPainter = new CompoundPainter<>((List<? extends Painter<Painter<JXMapViewer>>>) painters);
	}
	
	public void repaintPainters() {
		if (map != null) {
			map.setOverlayPainter(getCompoundPainter());
//			if (!waypointPainter.getWaypoints().isEmpty()) {
//				map.setAddressLocation(waypointPainter.getWaypoints().iterator().next().getPosition());
//			}
		}
	}
	
	public void setZoneWaypointPainters(List<WaypointPainter<Stop>> zoneWaypointPainters) {
		this.zoneWaypointPainters = zoneWaypointPainters;
	}
	
	public CompoundPainter getCompoundPainter() {
		return compoundPainter;
	}
	
	public WaypointPainter getSelectedWaypointPainter() {
		return selectedWaypointPainter;
	}
	
	public WaypointPainter getWaypointPainter() {
		return waypointPainter;
	}
	
	public WayPainter getWayPainter() {
		return wayPainter;
	}
	
	public PolygonPainter getPolygonPainter() {
		return polygonPainter;
	}

	public WayPainter getSelectedWayPainter() {
		return selectedWayPainter;
	}
	
	public int getWaypointDiameter() {
		if (stopWaypointRenderer != null) {
			return stopWaypointRenderer.getEllipseDiameter();
		}
		return 0;
	}
}
