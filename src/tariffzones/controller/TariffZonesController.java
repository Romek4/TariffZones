package tariffzones.controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JToolTip;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.painter.AbstractPainter;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactory;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.WaypointPainter;

import tariffzones.gui.AddStopDlg;
import tariffzones.gui.AddWayDlg;
import tariffzones.gui.AddNetworkDlg;
import tariffzones.gui.ColorZoneChooserPl;
import tariffzones.gui.ComboBoxRenderer;
import tariffzones.gui.OpenNetworkFromFilesDlg;
import tariffzones.gui.StopInfoPanel;
import tariffzones.gui.TariffZonesView;
import tariffzones.map.DefaultTileFactory;
import tariffzones.map.LandscapeTileFactoryInfo;
import tariffzones.map.ImageTextComboBoxItem;
import tariffzones.map.MapViewer;
import tariffzones.map.MapnikGrayScaleTileFactoryInfo;
import tariffzones.map.MapnikNoLabelsTileFactoryInfo;
import tariffzones.map.TileFactoryManager;
import tariffzones.map.TransportTileFactoryInfo;
import tariffzones.map.mapComponents.StopWaypointRenderer;
import tariffzones.map.mapComponents.WayPainter;
import tariffzones.model.Stop;
import tariffzones.model.StopTableModel;
import tariffzones.model.Network;
import tariffzones.model.ResultSetTableModel;
import tariffzones.model.State;
import tariffzones.model.TariffZonesModel;
import tariffzones.model.Way;
import tariffzones.model.WayTableModel;
import tariffzones.tariffzonesprocessor.greedy.TariffZonesProblemSolver;
import tariffzones.tariffzonesprocessor.greedy.Zone;

public class TariffZonesController {
	private TariffZonesModel model;
	private TariffZonesView view;
	private PainterManager painterManager;
	private TileFactoryManager tileFactoryManager;
	private Network loadedNetwork;
	private Stop lastPickedStop;
	private Way lastPickedWay;
	
	private boolean connectedToDB = false;

	private final String IC_NETWORK_TYPE = "IC";
	private final String OC_NETWORK_TYPE = "OC";
		
	public TariffZonesController() {
		model = new TariffZonesModel();
	}
	
	public void activate() {

		List<TileFactory> tileFactories = new ArrayList<>(5);
		DefaultTileFactory osmTileFactory = new DefaultTileFactory(new OSMTileFactoryInfo());
		DefaultTileFactory mapnikNoLabelsTileFactory = new DefaultTileFactory(new MapnikNoLabelsTileFactoryInfo());
		DefaultTileFactory mapnikGrayScaleTileFactory = new DefaultTileFactory(new MapnikGrayScaleTileFactoryInfo());
		DefaultTileFactory transportTileFactory = new DefaultTileFactory(new TransportTileFactoryInfo());
		DefaultTileFactory humanitarianTileFactory = new DefaultTileFactory(new LandscapeTileFactoryInfo());
		
		tileFactories.add(osmTileFactory);
		tileFactories.add(mapnikNoLabelsTileFactory);
		tileFactories.add(mapnikGrayScaleTileFactory);
		tileFactories.add(transportTileFactory);
		tileFactories.add(humanitarianTileFactory);
		
		painterManager = new PainterManager(view.getMapViewer());
		view.getTileServersCb().addItem(osmTileFactory);
		view.getTileServersCb().addItem(mapnikNoLabelsTileFactory);
		view.getTileServersCb().addItem(mapnikGrayScaleTileFactory);
		view.getTileServersCb().addItem(transportTileFactory);
		view.getTileServersCb().addItem(humanitarianTileFactory);
		
		view.unregistryListeners();
		view.registryListeners();
		updateBtns();
	}
	
	public void solveTariffZonesProblem() {
		
		ColorZoneChooserPl dlg = new ColorZoneChooserPl();
		int option = JOptionPane.showConfirmDialog(view.getRootPane(), dlg, "Tariff Zones Problem Solving", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == 2) {
            return;
        }
        
        int numberOfZones = (int) dlg.getNumberOfZonesCb().getSelectedItem();
        ArrayList<Color> colorList = dlg.getSelectedColors();
		boolean useOnlyNumberOfHabs = dlg.getUseOnlyNumberOfHabitantsChb().isSelected();
		boolean useNumberOfHabs = dlg.getUseNumberOfHabitantsChb().isSelected();
		boolean useTimeLength = dlg.getUseTimeLengthRb().isSelected(); //if false, then dlg.getUseDistanceRb().isSelected() is true
		
		TariffZonesProblemSolver solver = new TariffZonesProblemSolver();
		if (useOnlyNumberOfHabs) {
			solver.runGreedyAlgorithm(model.getNetworkStops(), numberOfZones, model.getDistanceMatrix(), useOnlyNumberOfHabs, true);
		}
		else {
			if (useTimeLength) {
				Way.PRICE_VALUE = Way.TIME;
			}
			else {
				Way.PRICE_VALUE = Way.DISTANCE;
			}
			
			model.runDjikstra();
			solver.runGreedyAlgorithm(model.getNetworkStops(), numberOfZones, model.getDistanceMatrix(), useOnlyNumberOfHabs, useNumberOfHabs);
		}
        
		ArrayList<Zone> zones = solver.getZones();
		for (int i = 0; i < zones.size(); i++) {
			painterManager.addZoneWaypointPainter(zones.get(i).getStopsInZone(), colorList.get(i));
			zones.get(i).setColor(colorList.get(i));
		}
		
		painterManager.getPolygonPainter().setPolygons(new HashSet<>(zones));
		painterManager.repaintPainters();
	}
	
	public void addStopsInNetworkToMap(String networkName) {
		//reset
		painterManager = new PainterManager(view.getMapViewer());
		
		model.setNetwork((Network) view.getCitiesCb().getSelectedItem());
		model.resetLists();
		
		ResultSet resultSet = model.getStops(networkName);
		if (resultSet == null) {
			return;
		}
		
		try {
			while (resultSet.next()) {
				int id = resultSet.getInt("stop_id");
				int stopNumber = resultSet.getInt("stop_number");
				String stopName = resultSet.getString("stop_name");
				int numberOfHabitants = resultSet.getInt("num_of_habitants");
				double latitude = resultSet.getDouble("latitude");
				double longitude = resultSet.getDouble("longitude");
				
				model.addStop(id, stopNumber, stopName, numberOfHabitants, latitude, longitude);
			}
			
			if (view.getStopCb().getSelectedItem().toString().equals("Stops")) {
				fillTableWithStops(view.getStopTable(), model.getNetworkStops());
			}			
			if (view.getWayCb().getSelectedItem().toString().equals("Stops")) {
				fillTableWithStops(view.getWayTable(), model.getNetworkStops());
			}
			
			if (model.getNetworkStops() != null || !model.getNetworkStops().isEmpty()) {
				view.getMapViewer().setAddressLocation(((ArrayList<Stop>)model.getNetworkStops()).get(0).getPosition());
				view.getMapViewer().setZoom(9);
			}
			
			painterManager.getWaypointPainter().setWaypoints(new HashSet<>(model.getNetworkStops()));
			painterManager.repaintPainters();
			updateBtns();
			
		} catch (SQLException e) {
			System.err.println(this.getClass().getName() + " addStopsInNetworkToMap method " + e);
			e.printStackTrace();
		}
	}
	
	public void addBusStop(Point point) {
		AddStopDlg dlg = new AddStopDlg();
		Image img;
		try {
			img = ImageIO.read(new FileInputStream("resources/images/busStopBtnImg.png"));
			dlg.setIconLabelImg(img);
			dlg.setIconLabelText("Add stop");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int option = JOptionPane.showConfirmDialog(view.getMapViewer(), dlg, "Add Stop", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == 2) {
            return;
        }
		
		int stopNumber = Integer.parseInt(dlg.getStopNumberTf().getText());
		String stopName = dlg.getStopNameTf().getText();
		int numberOfCustomers = Integer.parseInt(dlg.getNumberOfCustomersTf().getText());
		GeoPosition geoPosition = view.getMapViewer().convertPointToGeoPosition(point);
		
		model.addAndRememberStop(stopNumber, stopName, numberOfCustomers, geoPosition.getLatitude(), geoPosition.getLongitude());
		
		//TODO: reduce complexity and add just last added stop from list to table/s
		//TODO: maybe create a method for this short code - same logic is used in addWaysBetweenStopsInCityToMap method
		if (view.getStopCb().getSelectedItem().toString().equals("Stops")) {
			fillTableWithStops(view.getStopTable(), model.getNetworkStops());
		}
		
		if (view.getWayCb().getSelectedItem().toString().equals("Stops")) {
			fillTableWithStops(view.getWayTable(), model.getNetworkStops());
		}
		
		painterManager.getWaypointPainter().setWaypoints(new HashSet<>(model.getNetworkStops()));
		painterManager.repaintPainters();
		updateBtns();
	}
	
	public void addWay(Point startPoint, Point endPoint) {
		Stop startStop = checkMousePositionForStop(startPoint);
		Stop endStop = checkMousePositionForStop(endPoint);
		
		Image img = null;
		try {
			img = ImageIO.read(new FileInputStream("resources/images/wayBtnImg.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (startStop.equals(endStop)) {
			JOptionPane.showMessageDialog(view.getMapViewer(), "The start and end point could not be same!", "Add Way", JOptionPane.ERROR_MESSAGE, new ImageIcon(img.getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
			return;
		}
		
		AddWayDlg dlg = new AddWayDlg();
		dlg.setIconLabelImg(img);
		dlg.setIconLabelText("Add way");
		
		int option = JOptionPane.showConfirmDialog(view.getMapViewer(), dlg, "Add Way", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == 2) {
            return;
        }
		
		double wayLength = Double.parseDouble(dlg.getWayLengthTf().getText());
		double wayTimeLength = Double.parseDouble(dlg.getWayTimeLengthTf().getText());
		
		model.addAndRememberWay(startStop, endStop, wayLength, wayTimeLength);
		
		//TODO: reduce complexity and add just last added stop from list to table/s
		//TODO: maybe create a method for this short code - same logic is used in addWaysBetweenStopsInCityToMap method
		if (view.getStopCb().getSelectedItem().toString().equals("Ways")) {
			fillTableWithWays(view.getStopTable(), model.getNetworkWays());
		}
		
		if (view.getWayCb().getSelectedItem().toString().equals("Ways")) {
			fillTableWithWays(view.getWayTable(), model.getNetworkWays());
		}
		
		painterManager.getWayPainter().setWays(new HashSet<>(model.getNetworkWays()));
		painterManager.repaintPainters();
		updateBtns();
	}
	
	public void addWaysBetweenStopsInCityToMap(String networkName) {
		ResultSet resultSet = model.getWays(networkName);
		if (resultSet == null) {
			return;
		}
		
		try {
			while (resultSet.next()) {
//				GeoPosition coordinate1 = new GeoPosition(resultSet.getDouble("start_lat"), resultSet.getDouble("start_lon"));
//				GeoPosition coordinate2 = new GeoPosition(resultSet.getDouble("end_lat"), resultSet.getDouble("end_lon"));
				
				model.addWay(resultSet.getInt("start_number"), resultSet.getInt("end_number"), resultSet.getDouble("km_length"), resultSet.getDouble("time_length"));
		}
			
			if (view.getStopCb().getSelectedItem().toString().equals("Ways")) {
				fillTableWithWays(view.getStopTable(), model.getNetworkWays());
			}
			
			if (view.getWayCb().getSelectedItem().toString().equals("Ways")) {
				fillTableWithWays(view.getWayTable(), model.getNetworkWays());
			}
			
			painterManager.getWayPainter().setWays(new HashSet<>(model.getNetworkWays()));
		} catch (SQLException e) {
			System.err.println(this.getClass().getName() + " addWaysBetweenStopsInCityToMap method " + e);
			e.printStackTrace();
		}
	}
	
	public void addNetwork() throws IOException, FileNotFoundException {
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
		
		ArrayList<Stop> stops = null;
		ArrayList<Way> ways = null;
			
		stops = model.readStops(stopsFilePath);
		ways = model.readWays(wayFilePath, stops);
		
//		if (dbImport) {
//			model.insertNetwork(networkName, networkType, countryId);
//			view.getCitiesCb().addItem(networkName);
//			int networkId = model.getNetworkId(networkName);
//			
//			if (networkId < 0) {
//				return;
//			}
//			
//			if (stops != null) {
//				model.insertStops(networkId, countryId, stops);
//			}
//			
//			if (ways != null) {
//				model.insertEdges(ways);
//			}
//		}
//		else {
//			if (stops != null) {
//				fillTableWithStops(view.getStopTable(), stops);
//			}
//			//TODO: table change
//			if (ways != null) {
//				fillTableWithWays(view.getWayTable(), ways);
//			}
//		}
	}
	
	public void openNetworkFromFiles() throws IOException, FileNotFoundException {
		OpenNetworkFromFilesDlg dlg = new OpenNetworkFromFilesDlg();
		Image img;
		try {
			img = ImageIO.read(new FileInputStream("resources/images/networkIcon.png"));
			dlg.setIconLabelImg(img);
			dlg.setIconLabelText("Open network from files");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int option = JOptionPane.showConfirmDialog(view.getRootPane(), dlg, "Open network", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == 2) {
            return;
        }
		String stopsFilePath = dlg.getStopsFileLb().getText();
		String wayFilePath = dlg.getWayFileLb().getText();
		
		ArrayList<Stop> stops = null;
		ArrayList<Way> ways = null;
		
		stops = model.readStops(stopsFilePath);
		ways = model.readWays(wayFilePath, stops);
		
		//dummy network
		model.setNetwork(new Network(0, "", "", ""));
		model.getNetwork().setStops(stops);
		model.getNetwork().setWays(ways);
		
		//FIXME: duplicity?
		if (view.getStopCb().getSelectedItem().toString().equals("Stops")) {
			fillTableWithStops(view.getStopTable(), model.getNetworkStops());
		}
		
		if (view.getWayCb().getSelectedItem().toString().equals("Stops")) {
			fillTableWithStops(view.getWayTable(), model.getNetworkStops());
		}
		
		if (view.getStopCb().getSelectedItem().toString().equals("Ways")) {
			fillTableWithWays(view.getStopTable(), model.getNetworkWays());
		}
		
		if (view.getWayCb().getSelectedItem().toString().equals("Ways")) {
			fillTableWithWays(view.getWayTable(), model.getNetworkWays());
		}
	
		model.setUnsavedStops(model.getNetworkStops());
		model.setUnsavedWays(model.getNetworkWays());
		
		painterManager.getWaypointPainter().setWaypoints(new HashSet<>(model.getNetworkStops()));
		painterManager.getWayPainter().setWays(new HashSet<>(model.getNetworkWays()));
		painterManager.repaintPainters();
	}

	public void deleteWays(ArrayList<Way> ways) {
		for (Way way : ways) {
			model.removeAndRememberWay(way);
		}
		
		//FIXME: redundant code
		if (view.getStopCb().getSelectedItem().toString().equals("Ways")) {
			fillTableWithWays(view.getStopTable(), model.getNetworkWays());
		}
		
		if (view.getWayCb().getSelectedItem().toString().equals("Ways")) {
			fillTableWithWays(view.getWayTable(), model.getNetworkWays());
		}
		
		painterManager.getWayPainter().setWays(new HashSet<>(model.getNetworkWays()));
		painterManager.repaintPainters();
		updateBtns();
	}

	public void deleteStops(ArrayList<Stop> stops) {
		for (Stop stop : stops) {
			model.removeAndRememberStop(stop);
		}
		
		//FIXME: redundant code
		if (view.getStopCb().getSelectedItem().toString().equals("Stops")) {
			fillTableWithStops(view.getStopTable(), model.getNetworkStops());
		}
		
		if (view.getWayCb().getSelectedItem().toString().equals("Stops")) {
			fillTableWithStops(view.getWayTable(), model.getNetworkStops());
		}
		
		painterManager.getWaypointPainter().setWaypoints(new HashSet<>(model.getNetworkStops()));
		painterManager.repaintPainters();
		updateBtns();
	}
	
	public void deleteStop(Stop stop) {
//		int networkId = model.getNetworkId(networkName);
//		String countryId = model.getNetworkCountryId(networkName);
//		System.out.println("deleting " + networkId + ", " + countryId + ", " + stopNumber);
		
		model.removeAndRememberStop(stop);
		
		//FIXME: redundant code
		if (view.getStopCb().getSelectedItem().toString().equals("Stops")) {
			fillTableWithStops(view.getStopTable(), model.getNetworkStops());
		}
		
		if (view.getWayCb().getSelectedItem().toString().equals("Stops")) {
			fillTableWithStops(view.getWayTable(), model.getNetworkStops());
		}
		
		painterManager.getWaypointPainter().setWaypoints(new HashSet<>(model.getNetworkStops()));
		painterManager.repaintPainters();
		updateBtns();
	}
	
	public void editStop(Stop stop) {
		AddStopDlg dlg = new AddStopDlg();
		Image img;
		try {
			img = ImageIO.read(new FileInputStream("resources/images/editIcon.png"));
			dlg.setIconLabelImg(img);
			dlg.setIconLabelText("Edit Stop");
		} catch (IOException e) {
			e.printStackTrace();
		}
		dlg.getStopNumberTf().setText(String.valueOf(stop.getNumber()));
		dlg.getStopNameTf().setText(stop.getName());
		dlg.getNumberOfCustomersTf().setText(String.valueOf(stop.getNumberOfCustomers()));
		
		int option = JOptionPane.showConfirmDialog(view.getRootPane(), dlg, "Edit Stop", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == 2) {
            return;
        }
		
		stop.setNumber(Integer.parseInt(dlg.getStopNumberTf().getText()));
		stop.setName(dlg.getStopNameTf().getText());
		stop.setNumberOfCustomers(Integer.parseInt(dlg.getNumberOfCustomersTf().getText()));
		
		if (stop.getState() == State.ADDED) {
			return;
		}
		
		stop.setState(State.MODIFIED);
		model.getUnsavedStops().add(stop);
		updateBtns();
		
		((AbstractTableModel) view.getStopTable().getModel()).fireTableDataChanged();
		((AbstractTableModel) view.getWayTable().getModel()).fireTableDataChanged();
	}
	
	public void editWay(Way way) {
		AddWayDlg dlg = new AddWayDlg();
		Image img;
		try {
			img = ImageIO.read(new FileInputStream("resources/images/editIcon.png"));
			dlg.setIconLabelImg(img);
			dlg.setIconLabelText("Edit Way");
		} catch (IOException e) {
			e.printStackTrace();
		}
		dlg.getWayLengthTf().setText(String.valueOf(way.getDistance()));
		dlg.getWayTimeLengthTf().setText(String.valueOf(way.getTimeLength()));
		
		int option = JOptionPane.showConfirmDialog(view.getRootPane(), dlg, "Edit Way", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == 2) {
            return;
        }
		
		way.setDistance(Double.parseDouble(dlg.getWayLengthTf().getText()));
		way.setTimeLength(Double.parseDouble(dlg.getWayTimeLengthTf().getText()));
		
		if (way.getState() == State.ADDED) {
			return;
		}
		
		way.setState(State.MODIFIED);
		model.getUnsavedWays().add(way);
		updateBtns();
		
		((AbstractTableModel) view.getStopTable().getModel()).fireTableDataChanged();
		((AbstractTableModel) view.getWayTable().getModel()).fireTableDataChanged();
	}
	
	public void deleteWay(Way way) {
		
		model.removeAndRememberWay(way);
		
		//FIXME: redundant code
		if (view.getStopCb().getSelectedItem().toString().equals("Stops")) {
			fillTableWithStops(view.getStopTable(), model.getNetworkStops());
		}
		
		if (view.getWayCb().getSelectedItem().toString().equals("Stops")) {
			fillTableWithStops(view.getWayTable(), model.getNetworkStops());
		}
		
		painterManager.getWayPainter().setWays(new HashSet<>(model.getNetworkWays()));
		painterManager.repaintPainters();
		updateBtns();
	}
	
	public void deleteNetwork(String networkName) {
		model.deleteNetwork(networkName);
	}
	
	public boolean connectToDB() {
		connectedToDB = model.connectToDB("jdbc:mysql://localhost/traffic_networks_db?characterEncoding=UTF-8", "admin", "admin");
		return connectedToDB;
	}
	
	public boolean saveChangesToDatabase() {
		return model.saveChangesToDatabase();
	}
	
	public void fillTableWithStops(JTable table) {
		fillTableWithStops(table, model.getNetworkStops());
//		ResultSet resultSet = model.getStopNumAndName(cityName);
//		if (resultSet == null) {
//			return;
//		}
//		
//		ResultSetTableModel tableModel = new ResultSetTableModel();
//		tableModel.setResultSet(resultSet);
//		table.setModel(tableModel);
	}
	
	public void fillTableWithWays(JTable table) {
		fillTableWithWays(table, model.getNetworkWays());
//		ResultSet resultSet = model.getWaysWithStopNames(networkName);
//		if (resultSet == null) {
//			return;
//		}
//		
//		ResultSetTableModel tableModel = new ResultSetTableModel();
//		tableModel.setResultSet(resultSet);
//		table.setModel(tableModel);
	}
	
	public void fillTableWithStops(JTable table, ArrayList<Stop> list) {
		if (list == null) {
			return;
		}
		
		StopTableModel stopTableModel = new StopTableModel(list);
		table.setModel(stopTableModel);
		table.getModel().addTableModelListener(new TableModelListener() {
			
			@Override
			public void tableChanged(TableModelEvent e) {
				if (e.getType() == TableModelEvent.UPDATE) {
					Stop stop = (Stop) list.get(e.getFirstRow());
					if (stop != null) {
						stop.setState(State.MODIFIED);
						model.getUnsavedStops().add(stop);
					}
				}
			}
		});
		
//		for (Object o : list.toArray()) {
//			Stop stop = (Stop) o;
//			tableModel.addRow(new Object[]{ stop.getNumber(), stop.getName()});
//		}
	}
	
	public void fillTableWithWays(JTable table, ArrayList<Way> list) {
		if (list == null) {
			return;
		}
		//TODO: mins/ km?
		WayTableModel wayTableModel = new WayTableModel(list);
		table.setModel(wayTableModel);
		table.getModel().addTableModelListener(new TableModelListener() {
			
			@Override
			public void tableChanged(TableModelEvent e) {
				if (e.getType() == TableModelEvent.UPDATE) {
					Way way = (Way) list.get(e.getFirstRow());
					if (way != null) {
						way.setState(State.MODIFIED);
						model.getUnsavedWays().add(way);
					}
				}
			}
		});
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

	public boolean checkForStopAndShowTooltip(Point point) {
		Stop stop = checkMousePositionForStop(point);
		JToolTip mapToolTip = ((MapViewer) view.getMapViewer()).getToolTip();
		
		if (stop == null) {
			mapToolTip.setVisible(false);
			return false;
		}
		
		((MapViewer) view.getMapViewer()).setLayout(null);
		String s = stop.toString();
		mapToolTip.setTipText(s);
		mapToolTip.setBounds((int)point.getX()+8, (int)point.getY()+8, s.length()*8, 20);
		mapToolTip.setVisible(true);
		return true;
	}

	public boolean checkForStopAndShowPopup(Point point) {
		Stop stop = checkMousePositionForStop(point);
		if (stop == null) {
			return false;
		}
		
		lastPickedStop = stop; //remember right clicked stop for possible delete or edit(to avoid it's finding again) 
		view.getStopMapPopupMenu().show(view.getMapViewer(), (int) point.getX(), (int) point.getY());
		return true;
	}
	
	public boolean checkForWayAndShowTooltip(Point point) {
		Way way = checkMousePositionForWay(point);
		JToolTip mapToolTip = ((MapViewer) view.getMapViewer()).getToolTip();
		
		if (way == null) {
			mapToolTip.setVisible(false);
			return false;
		}
		
		((MapViewer) view.getMapViewer()).setLayout(null);
		String s = way.toString();
		mapToolTip.setTipText(s);
		mapToolTip.setBounds((int)point.getX()+8, (int)point.getY()+8, s.length()*8, 20);
		mapToolTip.setVisible(true);
		return true;
	}
	
	public Stop checkMousePositionForStop(Point point) {
		if (model.getNetwork().getStops() == null) {
			return null;
		}
		
		for (Stop stop : model.getNetwork().getStops())
		{
			Point2D p = view.getMapViewer().convertGeoPositionToPoint(stop.getPosition());
			if (point.distance(p) <= 5) { //TODO: make a constant or something
				return stop;
			}
		}
		
		return null;
	}

	public boolean checkForWayAndShowPopup(Point point) {
		Way way = checkMousePositionForWay(point);
		if (way == null) {
			return false;
		}
		
		lastPickedWay = way; //remember right clicked way for possible delete or edit(to avoid it's finding again) 
		view.getWayMapPopupMenu().show(view.getMapViewer(), (int) point.getX(), (int) point.getY());
		return true;
	}

	public int getClickedStopIndex(Point point) {
		if (model.getNetwork().getStops() == null) {
			return -1;
		}
		
		ArrayList<Stop> stops = model.getNetwork().getStops();
		for (int i = 0; i < stops.size(); i++) {
			Point2D p = view.getMapViewer().convertGeoPositionToPoint(stops.get(i).getPosition());
			if (point.distance(p) <= 5) { //TODO: make a constant or something
				return i;
			}
		}
		
		return -1;
	}
	
	public Way checkMousePositionForWay(Point point) {
		if (model.getNetwork().getWays() == null) {
			return null;
		}
		
		for (Way way : model.getNetwork().getWays()) {
			
			Point2D startPoint = view.getMapViewer().convertGeoPositionToPoint(way.getStartPosition());
			Point2D endPoint = view.getMapViewer().convertGeoPositionToPoint(way.getEndPosition());
			
			if (point.distance(startPoint) + point.distance(endPoint) < startPoint.distance(endPoint) + 2) {
				return way;
			}
		}
		
		return null;
	}
	
	public int getClickedWayIndex(Point point) {
		if (model.getNetwork().getWays() == null) {
			return -1;
		}
		
		ArrayList<Way> ways = model.getNetwork().getWays();
		for (int i = 0; i < ways.size(); i++) {
			
			Point2D startPoint = view.getMapViewer().convertGeoPositionToPoint(ways.get(i).getStartPosition());
			Point2D endPoint = view.getMapViewer().convertGeoPositionToPoint(ways.get(i).getEndPosition());
			
			if (point.distance(startPoint) + point.distance(endPoint) < startPoint.distance(endPoint) + 2) {
				return i;
			}
		}
		
		return -1;
	}
	
	public void updateBtns() {
		if (model.getNetwork() == null) {
			view.getMapToolboxPl().enableBtns(false);
		}
		else {
			view.getMapToolboxPl().enableBtns(true);
			view.getMapToolboxPl().getSaveBtn().setEnabled(false);
		}
		
		if (isUnsavedChanges()) {
			view.getMapToolboxPl().getSaveBtn().setEnabled(true);
		}
		else {
			view.getMapToolboxPl().getSaveBtn().setEnabled(false);
		}
	}

	public boolean isUnsavedChanges() {
		if (model.getUnsavedStops().isEmpty() && model.getUnsavedWays().isEmpty()) {
			return false;
		}
		return true;
	}
	
	public void clearUnsaved() {
		model.getUnsavedStops().clear();
		model.getUnsavedWays().clear();
	}

	public void setView(TariffZonesView tariffZonesView) {
		this.view = tariffZonesView;
	}

	public String getLoadedNetworkName() {
		if (model.getNetwork() != null) {
			return model.getNetwork().getNetworkName();
		}
		return null;
	}

	public Stop getLastPickedStop() {
		return lastPickedStop;
	}
	
	public void resetLastPickedStop() {
		lastPickedStop = null;
	}

	public Way getLastPickedWay() {
		return lastPickedWay;
	}
	
	public void resetLastPickedWay() {
		lastPickedWay = null;
	}

	public void exportStops() {
		if (model.getNetworkStops() == null || model.getNetworkStops().isEmpty()) {
			JOptionPane.showMessageDialog(view, "Nothing to export", "Export To CSV", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		File file = selectFile();
		if (file != null) {
			try {
				model.getDataImporter().writeStops((ArrayList<Stop>)model.getNetworkStops(), file.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void exportWays() {
		if (model.getNetworkWays() == null || model.getNetworkWays().isEmpty()) {
			JOptionPane.showMessageDialog(view, "Nothing to export", "Export To CSV", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		File file = selectFile();
		if (file != null) {
			try {
				model.getDataImporter().writeWays((ArrayList<Way>)model.getNetworkWays(), file.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private File selectFile() {
		JFileChooser fileChooser = new JFileChooser();
		int returnVal = fileChooser.showOpenDialog(view.getParent());
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			if (file != null) {
				return file;
			}
		}
		return null;
	}
	
	public TileFactoryManager getTileFactoryManager() {
		return tileFactoryManager;
	}

	public void highligthStops(ArrayList<Stop> stops, Color red) {
		if (stops == null || stops.isEmpty()) {
			return;
		}
		view.getMapViewer().setAddressLocation(stops.get(0).getPosition());
		painterManager.getSelectedWaypointPainter().setWaypoints(new HashSet<>(stops));
		painterManager.repaintPainters();
	}

	public void highligthWays(ArrayList<Way> ways, Color red) {
		if (ways == null || ways.isEmpty()) {
			return;
		}
		view.getMapViewer().setAddressLocation(ways.get(0).getStartPosition());
		painterManager.getSelectedWayPainter().setWays(new HashSet<>(ways));
		painterManager.repaintPainters();
	}
	
	public void resetSelected() {
		painterManager.getSelectedWayPainter().setWays(new HashSet<>());
		painterManager.getSelectedWaypointPainter().setWaypoints(new HashSet<>());
		painterManager.repaintPainters();
	}

	public List getNetworks() {
		return model.getNetworks();
	}
}
