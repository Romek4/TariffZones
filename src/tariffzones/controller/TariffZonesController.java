package tariffzones.controller;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;

import tariffzones.gui.AddBusStopDlg;
import tariffzones.gui.AddNetworkDlg;
import tariffzones.gui.TariffZonesView;
import tariffzones.model.BusStop;
import tariffzones.model.MyCoordinate;
import tariffzones.model.TableModel;

import tariffzones.model.TariffZonesModel;
import tariffzones.model.Way;
import tariffzones.tariffzonesprocessor.greedy.GreedyAlgorithm;
import tariffzones.tariffzonesprocessor.greedy.Zone;

public class TariffZonesController {
	private TariffZonesModel model;
	private TariffZonesView view;
	private String loadedNetworkName, loadedNetworkType; 

		
	public TariffZonesController() {
		model = new TariffZonesModel();
	}
	
	public void activate() {
		view.unregistryListeners();
		List networkNames = getNetworkNames();
		
		if (networkNames != null) {
			view.getCitiesCb().setModel((new DefaultComboBoxModel(networkNames.toArray())));
		}
		
		view.registryListeners();
	}
	
	public ArrayList<Zone> solveTariffZonesProblem() {
		GreedyAlgorithm greedy = new GreedyAlgorithm();
		greedy.runAlgorithm(model.getStops(), 4);
		return greedy.getZones();
	}
	
	public void paintZones(ArrayList<Zone> zones) {
//		for (Zone zone : zones) {
//			List<Coordinate> coordinates = new ArrayList();
//			for (BusStop busStop : zone.getStopsInZone()) {
//				coordinates.add(new Coordinate(busStop.getCoordinate().getLatitude(), busStop.getCoordinate().getLongitude()));
//			}
//			
//			MapPolygonImpl mapPolygon = new MapPolygonImpl(coordinates);//TODO: vykresæovaù inak, nejak˝ radius okolo bodu alebo Ëo
//			mapPolygon.setColor(Color.CYAN); //TODO: farbenie jednotliv˝ch zÛn - nech si uûÌvateæ zvolÌ poËet zÛn a ich farby
//			mapViewer.addMapPolygon(mapPolygon);
//		}
		
		Comparator<Coordinate> busStopComparator = (Coordinate c1, Coordinate c2)->Double.compare(c1.getLon(), c2.getLon());
		
		List<Coordinate> coordinates = new ArrayList();
		for (BusStop busStop : zones.get(0).getStopsInZone()) {
			coordinates.add(new Coordinate(busStop.getCoordinate().getLatitude(), busStop.getCoordinate().getLongitude()));
		}
		
		coordinates.sort(busStopComparator);
		MapPolygonImpl mapPolygon = new MapPolygonImpl(coordinates);
		mapPolygon.setColor(Color.CYAN);
		view.getMapViewer().addMapPolygon(mapPolygon);
		
		coordinates = new ArrayList();
		for (BusStop busStop : zones.get(1).getStopsInZone()) {
			coordinates.add(new Coordinate(busStop.getCoordinate().getLatitude(), busStop.getCoordinate().getLongitude()));
		}
		
		coordinates.sort(busStopComparator);
		mapPolygon = new MapPolygonImpl(coordinates);
		mapPolygon.setColor(Color.GREEN);
		view.getMapViewer().addMapPolygon(mapPolygon);
		
		coordinates = new ArrayList();
		for (BusStop busStop : zones.get(2).getStopsInZone()) {
			coordinates.add(new Coordinate(busStop.getCoordinate().getLatitude(), busStop.getCoordinate().getLongitude()));
		}
		
		coordinates.sort(busStopComparator);
		mapPolygon = new MapPolygonImpl(coordinates);
		mapPolygon.setColor(Color.RED);
		view.getMapViewer().addMapPolygon(mapPolygon);
	}
	
	public void addStopsInNetworkToMap(String networkName) {
		model.resetLists();
		this.loadedNetworkName = networkName;
		this.loadedNetworkType = model.getNetworkType(networkName);
		
		ResultSet resultSet = model.getStops(networkName);
		if (resultSet == null) {
			return;
		}
		
		try {
			Coordinate coordinate = null;
			while (resultSet.next()) {
				coordinate = new Coordinate(resultSet.getDouble("latitude"), resultSet.getDouble("longitude"));
				MapMarkerDot mapMarkerDot = new MapMarkerDot(resultSet.getString("stop_name"), coordinate);
				view.getMapViewer().addMapMarker(mapMarkerDot);
				
				MyCoordinate myCoordinate = new MyCoordinate(resultSet.getDouble("latitude"), resultSet.getDouble("longitude"));
				model.addStop(resultSet.getInt("stop_number"), resultSet.getString("stop_name"), myCoordinate, resultSet.getInt("num_of_habitants"));
			}
			
			if (coordinate != null) {
				view.getMapViewer().setDisplayPosition(coordinate, 13);
			}
			
			if (view.getStopCb().getSelectedItem().toString().equals("Bus stops")) {
				fillTableWithStops(view.getStopTable(), networkName);
			}
			else {
				fillTableWithWays(view.getStopTable(), networkName);
			}
			
			if (view.getWayCb().getSelectedItem().toString().equals("Bus stops")) {
				fillTableWithStops(view.getWayTable(), networkName);
			}
			else { 
				fillTableWithWays(view.getWayTable(), networkName);
			}
			
			addWaysBetweenStopsInCityToMap(networkName);
			
			paintZones(solveTariffZonesProblem());
		} catch (SQLException e) {
			System.err.println(this.getClass().getName() + " addDotMarkersStops method " + e);
			e.printStackTrace();
		}
	}
	
	public void addNetwork() {
		AddNetworkDlg dlg = new AddNetworkDlg(getNetworkTypes(), getCountryIds());
		
		int option = JOptionPane.showConfirmDialog(view.getRootPane(), dlg, "Add network", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == 2) {
            return;
        }
		
		String networkName = dlg.getNetworkNameTf().getText();
		if (networkName.equals("") || dlg.getCountryCb().getSelectedItem() == null) {
			return;
		}
		String networkType = dlg.getNetworkTypeCb().getSelectedItem().toString();
		String countryId = dlg.getCountryCb().getSelectedItem().toString();
		String stopsFilePath = dlg.getStopsFileLb().getText();
		String wayFilePath = dlg.getWayFileLb().getText();
		boolean dbImport = dlg.getDBImportChb().isSelected();
		
		ArrayList<BusStop> stops = null;
		ArrayList<Way> ways = null;
		try {
			stops = model.readBusStops(stopsFilePath, ',');
			ways = model.readWays(wayFilePath, ',');
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		if (dbImport) {
			model.insertNetwork(networkName, networkType, countryId);
			view.getCitiesCb().addItem(networkName);
			int networkId = model.getNetworkId(networkName);
			
			if (networkId < 0) {
				return;
			}
			
			if (stops != null) {
				model.insertStops(networkId, countryId, stops);
			}
			
			if (ways != null) {
				model.insertEdges(ways);
			}
		}
		else {
			if (stops != null) {
				fillTableWithStops(view.getStopTable(), stops);
			}
			//TODO: table change
			if (ways != null) {
				fillTableWithWays(view.getWayTable(), ways);
			}
		}
	}
	
	public void deleteNetwork(String networkName) {
		model.deleteNetwork(networkName);
	}
	
	public void addBusStop(int x, int y) {
		double lat = view.getMapViewer().getPosition(x ,y).getLat();
		double lon = view.getMapViewer().getPosition(x, y).getLon();
		
		
		AddBusStopDlg dlg = new AddBusStopDlg();
		
		int option = JOptionPane.showConfirmDialog(view.getRootPane(), dlg, "Add bus stop", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == 2) {
            return;
        }
		
		int stopNumber = Integer.parseInt(dlg.getStopNumberTf().getText());
		String stopName = dlg.getStopNameTf().getText();
		int numberOfCustomers = Integer.parseInt(dlg.getNumberOfCustomersTf().getText());
		
		addBusStop(view.getCitiesCb().getSelectedItem().toString(), stopNumber, stopName, lat, lon, numberOfCustomers);
		view.getMapViewer().addMapMarker(new MapMarkerDot(stopName, new MyCoordinate(lat, lon)));
		
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
	
	public void fillTableWithWays(JTable table, String networkName) {
		ResultSet resultSet = model.getWaysWithStopNames(networkName);
		if (resultSet == null) {
			return;
		}
		
		TableModel tableModel = new TableModel();
		tableModel.setResultSet(resultSet);
		table.setModel(tableModel);
	}
	
	public void fillTableWithStops(JTable table, ArrayList list) {
		String col[] = {"stop_number","stop_name"};
		DefaultTableModel tableModel = new DefaultTableModel(col, list.size());
		for (Object o : list.toArray()) {
			BusStop stop = (BusStop) o;
			tableModel.addRow(new Object[]{ stop.getNumber(), stop.getName()});
		}
		table.setModel(tableModel);
	}
	
	public void fillTableWithWays(JTable table, ArrayList list) {
		//TODO: mins/ km?
		String col[] = {"stop_name","stop_name", "time_length"};
		DefaultTableModel tableModel = new DefaultTableModel(col, list.size());
		for (Object o : list.toArray()) {
			Way way = (Way) o;
			tableModel.addRow(new Object[]{ way.getStartPoint().getNumber(), way.getEndPoint().getNumber(), way.getMins()});
		}
		table.setModel(tableModel);
	}
	
	public void addWaysBetweenStopsInCityToMap(String networkName) {
		ResultSet resultSet = model.getWays(networkName);
		if (resultSet == null) {
			return;
		}
		
		try {
			while (resultSet.next()) {
				Coordinate coordinate1 = new Coordinate(resultSet.getDouble("start_lat"), resultSet.getDouble("start_lon"));
				Coordinate coordinate2 = new Coordinate(resultSet.getDouble("end_lat"), resultSet.getDouble("end_lon"));
				view.getMapViewer().addMapPolygon(new MapPolygonImpl(Arrays.asList(coordinate1, coordinate2, coordinate2)));
				
				model.addWay(resultSet.getInt("start_number"), resultSet.getInt("end_number"), resultSet.getDouble("km_length"), resultSet.getDouble("time_length"));
			}
		} catch (SQLException e) {
			System.err.println(this.getClass().getName() + " addWaysBetweenStopsInCityToMap method " + e);
			e.printStackTrace();
		}
	}
	
//	public void addDotMarkersFromTree(JList list) {
//      Node actual = model.getStops().getRoot();
//      Stack stack = new Stack();
//      
//      while(!stack.isEmpty() || actual != null)
//      {
//          if (actual != null) {
//              stack.push(actual);
//              actual = actual.getLeftChild();
//          }
//          else {
//              Node node = (Node) stack.pop();
//              
//              BusStop busStop = (BusStop) node.getRecord().getValue();
//              list.add(new JCheckBox(busStop.getName()));
//              
//              actual = node.getRightChild();
//          }
//      }
//	}
	
	public List getNetworkNames() {
		ResultSet resultSet = model.getNetworkNames();
		List networkNames = new ArrayList<>();
		
		if (resultSet != null) {
			try {
				while (resultSet.next()) {
					networkNames.add(resultSet.getString("network_name"));
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return networkNames;
	}
	
	public List getNetworkTypes() {
		ResultSet resultSet = model.getNetworkTypes();
		List networkTypes = new ArrayList<>();
		
		if (resultSet != null) {
			try {
				while (resultSet.next()) {
					networkTypes.add(resultSet.getString("network_type"));
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return networkTypes;
	}
	
	public List getCountryIds() {
		ResultSet resultSet = model.getCountryIds();
		List countryIds = new ArrayList<>();
		
		if (resultSet != null) {
			try {
				while (resultSet.next()) {
					countryIds.add(resultSet.getString("country_id"));
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return countryIds;
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
	
	public void addBusStop(String networkName, int stopNumber, String stopName, double latitude, double longitude, int numOfHabitants) {
		int networkId = model.getNetworkId(networkName);
		String countryId = model.getNetworkCountryId(networkName);
		
		model.insertStop(networkId, countryId, stopNumber, stopName, new MyCoordinate(latitude, longitude), numOfHabitants);
		
//		if (model.insertStop(networkId, country_id, stopNumber, stopName, new MyCoordinate(latitude, longitude), numOfHabitants)) {
//			return false;
//		}
//		return true;
	}
	
	public void deleteBusStop(String networkName, int stopNumber) {
		int networkId = model.getNetworkId(networkName);
		String countryId = model.getNetworkCountryId(networkName);
		
//		System.out.println("deleting " + networkId + ", " + countryId + ", " + stopNumber);
		model.deleteStop(networkId, countryId, stopNumber);
	}
	
	public String getLoadedNetworkName() {
		return this.loadedNetworkName;
	}
	
	public String getLoadedNetworkType() {
		return this.loadedNetworkType;
	}
	
	public void setLoadedNetworkName(String networkName) {
		this.loadedNetworkName = networkName;
	}
	
	public void setLoadedNetworkType(String networkType) {
		this.loadedNetworkType = networkType;
	}

	public void setView(TariffZonesView tariffZonesView) {
		this.view = tariffZonesView;
	}

	private void dehighligthAllMarkerDots() {
		for (MapMarker dot : view.getMapViewer().getMapMarkerList()) {
			((MapMarkerDot) dot).setStyle(MapMarkerDot.getDefaultStyle());
		}
	}
	
	public void highligthBusStop(ArrayList<Integer> stopNumbers) {
//		int stopNumber = (int) object;
		dehighligthAllMarkerDots();
		if (stopNumbers == null) {
			return;
		}
		
		BusStop busStop = null;
		for (int stopNumber : stopNumbers) {
			busStop = model.findStop(stopNumber);
			if (busStop != null) {
				for (MapMarker dot : view.getMapViewer().getMapMarkerList()) {
					
					if (dot.getName().equals(busStop.getName())) {
							((MapMarkerDot) dot).setBackColor(Color.RED);
							break;
					}
				}
			}
		}
	}
}