package tariffzones.controller;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.WaypointPainter;

import tariffzones.map.mapComponents.DefaultPolygonRenderer;
import tariffzones.map.mapComponents.PolygonPainter;
import tariffzones.map.mapComponents.StopWaypointRenderer;
import tariffzones.map.mapComponents.Way;
import tariffzones.map.mapComponents.WayPainter;
import tariffzones.model.Stop;
import tariffzones.tariffzonesprocessor.greedy.Zone;

public class PainterManager {
	private JXMapViewer map;
	private WaypointPainter<Stop> waypointPainter;
	private List<WaypointPainter<Stop>> zoneWaypointPainters;
	private WayPainter<Way> wayPainter;
	private PolygonPainter<Zone> polygonPainter;
	private CompoundPainter<Painter<JXMapViewer>> compoundPainter;
	private List<Painter<JXMapViewer>> painters;
	
	public PainterManager(JXMapViewer map) {
		this.map = map;
		
		waypointPainter = new WaypointPainter<>();
		waypointPainter.setRenderer(new StopWaypointRenderer(Color.YELLOW));
		
		wayPainter = new WayPainter<>();
		
		polygonPainter = new PolygonPainter<>();
		polygonPainter.setPolygonRenderer(new DefaultPolygonRenderer());
		
		painters = new ArrayList<Painter<JXMapViewer>>();
		painters.add(waypointPainter);
		painters.add(wayPainter);
		painters.add(polygonPainter);
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
			map.setZoom(9);
			map.setAddressLocation(waypointPainter.getWaypoints().iterator().next().getPosition());
		}
	}
	
	public void setZoneWaypointPainters(List<WaypointPainter<Stop>> zoneWaypointPainters) {
		this.zoneWaypointPainters = zoneWaypointPainters;
	}
	
	public CompoundPainter getCompoundPainter() {
		return compoundPainter;
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
}
