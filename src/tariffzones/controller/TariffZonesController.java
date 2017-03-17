package tariffzones.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Stroke;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.table.TableCellEditor;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.Layer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import tariffzones.model.MyCoordinate;
import tariffzones.model.TableModel;
import Splay_tree_package.Node;
import tariffzones.model.BusStop;

import tariffzones.model.TariffZonesModel;

public class TariffZonesController {
	private TariffZonesModel model;
	
	public TariffZonesController() {
		model = new TariffZonesModel();
	}
	
	public void addStopsInCityToMap(JMapViewer mapViewer, JTable stopTable, JTable waysTable, String cityName) {
		ResultSet resultSet = model.getMhdStops(cityName);
		if (resultSet == null) {
			return;
		}
		
		try {
			Coordinate coordinate = null;
			while (resultSet.next()) {
				coordinate = new Coordinate(resultSet.getDouble("latitude"), resultSet.getDouble("longitude"));
				MapMarkerDot mapMarkerDot = new MapMarkerDot(resultSet.getString("stop_name"), coordinate);
				mapViewer.addMapMarker(mapMarkerDot);
				
				MyCoordinate myCoordinate = new MyCoordinate(resultSet.getDouble("latitude"), resultSet.getDouble("longitude"));
				model.addStop(resultSet.getInt("stop_number"), resultSet.getString("stop_name"), myCoordinate);
			}
			mapViewer.setDisplayPosition(coordinate, 13);
			
			fillTableWithStops(stopTable, cityName);
			fillTableWithWays(waysTable, cityName);
			
			addWaysBetweenStopsInCityToMap(mapViewer, cityName);
		} catch (SQLException e) {
			System.err.println(this.getClass().getName() + " addDotMarkersStops method " + e);
			e.printStackTrace();
		}
	}
	
	public void fillTableWithStops(JTable table, String cityName) {
		ResultSet resultSet = model.getStopNumAndName(cityName);
		if (resultSet == null) {
			return;
		}
		
		TableModel tableModel = new TableModel();
		tableModel.setResultSet(resultSet);
		table.setModel(tableModel);
	}
	
	public void fillTableWithWays(JTable table, String cityName) {
		ResultSet resultSet = model.getWaysWithStopNames(cityName);
		if (resultSet == null) {
			return;
		}
		
		TableModel tableModel = new TableModel();
		tableModel.setResultSet(resultSet);
		table.setModel(tableModel);
	}
	
	public void addWaysBetweenStopsInCityToMap(JMapViewer mapViewer, String cityName) {
		ResultSet resultSet = model.getWays(cityName);
		if (resultSet == null) {
			return;
		}
		
		try {
			while (resultSet.next()) {
				Coordinate coordinate1 = new Coordinate(resultSet.getDouble("start_lat"), resultSet.getDouble("start_lon"));
				Coordinate coordinate2 = new Coordinate(resultSet.getDouble("end_lat"), resultSet.getDouble("end_lon"));
				mapViewer.addMapPolygon(new MapPolygonImpl(Arrays.asList(coordinate1, coordinate2, coordinate2)));
				
				MyCoordinate startCoordinate = new MyCoordinate(resultSet.getDouble("start_lat"), resultSet.getDouble("start_lon"));
				MyCoordinate endCoordinate = new MyCoordinate(resultSet.getDouble("end_lat"), resultSet.getDouble("end_lon"));
				model.addWay(resultSet.getInt("way_id"), new MyCoordinate[]{startCoordinate, endCoordinate}, resultSet.getDouble("time_length"));
			}
		} catch (SQLException e) {
			System.err.println(this.getClass().getName() + " addDotMarkersStops method " + e);
			e.printStackTrace();
		}
	}
	
	public void addDotMarkersFromTree(JList list) {
      Node actual = model.getStops().getRoot();
      Stack stack = new Stack();
      
      while(!stack.isEmpty() || actual != null)
      {
          if (actual != null) {
              stack.push(actual);
              actual = actual.getLeftChild();
          }
          else {
              Node node = (Node) stack.pop();
              
              BusStop busStop = (BusStop) node.getRecord().getValue();
              list.add(new JCheckBox(busStop.getName()));
              
              actual = node.getRightChild();
          }
      }
	}
	
	public void getCityNames(JComboBox comboBox) {
		ResultSet resultSet = model.getCityNames();
		if (resultSet == null) {
			return;
		}
		
		try {
			while (resultSet.next()) {
				comboBox.addItem(resultSet.getString("city_name"));
			}
		} catch (SQLException e) {
			System.err.println(this.getClass().getName() + " addDotMarkersStops method " + e);
			e.printStackTrace();
		}
	}
	
//	public void addDotMarkersFromTree(JMapViewer jMapViewer) {
//        Node actual = model.getStops().getRoot();
//        Stack stack = new Stack();
//        
//        while(!stack.isEmpty() || actual != null)
//        {
//            if (actual != null) {
//                stack.push(actual);
//                actual = actual.getLeftChild();
//            }
//            else {
//                Node node = (Node) stack.pop();
//                
//                BusStop busStop = (BusStop) node.getRecord().getValue();
//                MapMarkerDot mapMarkerDot = new MapMarkerDot(busStop.getName(), new org.openstreetmap.gui.jmapviewer.Coordinate(busStop.getCoordinate().getLatitude(), busStop.getCoordinate().getLongitude()));
//                jMapViewer.addMapMarker(mapMarkerDot);
//                
//                actual = node.getRightChild();
//            }
//        }
//	}

	public void readBusStops(String busStopFileName) {
		model.readBusStops(busStopFileName);
	}
	
	public boolean addBusStop(int stopNumber, String stopName, double latitude, double longitude) {
		if (!model.addStop(stopNumber, stopName, new MyCoordinate(latitude, longitude))) {
			return false;
		}
		return true;
	}
}
