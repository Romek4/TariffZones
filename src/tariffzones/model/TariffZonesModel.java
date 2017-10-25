package tariffzones.model;

import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.ResultSetRow;

import Splay_tree_package.Record;
import Splay_tree_package.SplayTree;
import tariffzones.model.sql.interfaces.NetworkSelectAllNames;
import tariffzones.model.sql.interfaces.NetworkSelectNetworkCountryIdByName;
import tariffzones.model.sql.interfaces.NetworkSelectNetworkIdByName;
import tariffzones.model.sql.interfaces.NetworkSelectNetworkTypeByName;
import tariffzones.model.sql.interfaces.NetworkTypesSelect;
import tariffzones.model.sql.interfaces.StopInsert;
import tariffzones.model.sql.interfaces.StopNumNameSelectByNetworkName;
import tariffzones.model.sql.interfaces.StopAllSelectByNetworkName;
import tariffzones.model.sql.interfaces.StopDelete;
import tariffzones.model.sql.interfaces.CountryIdSelect;
import tariffzones.model.sql.interfaces.EdgeInsert;
import tariffzones.model.sql.interfaces.EdgeSelectCoordsByNetworkName;
import tariffzones.model.sql.interfaces.EdgeSelectStopNamesAndTime;
import tariffzones.model.sql.interfaces.EdgesOfStopDelete;
import tariffzones.model.sql.interfaces.NetworkDelete;
import tariffzones.model.sql.interfaces.NetworkInsert;
import tariffzones.model.sql.processor.DataImporter;
import tariffzones.model.sql.processor.SQLProcessor;
import tariffzones.tariffzonesprocessor.djikstra.Djikstra;
import tariffzones.tariffzonesprocessor.djikstra.Edge;
import tariffzones.tariffzonesprocessor.djikstra.Graph;
import tariffzones.tariffzonesprocessor.djikstra.Node;


public class TariffZonesModel {
	private DataImporter dataImporter;
	private ArrayList<Node> stops;
	private ArrayList<Edge> ways;
	private Djikstra djikstra;
	
	private SQLProcessor sqlProcessor;
	private Object[] sqlParameters;
	
	public TariffZonesModel() {
		stops = new ArrayList<>();
		ways = new ArrayList<>();
		sqlProcessor = new SQLProcessor();
		sqlProcessor.connectDatabase("admin", "admin");
	}
	
	public void runDjikstra() {
		djikstra = new Djikstra(new Graph(stops, ways));
		djikstra.runDjikstra();
	}
	
	public double[][] getDistanceMatrix() {
		if (djikstra == null) {
			return null;
		}
		return djikstra.getDistanceMatrix();
	}
	
	public int[][] getNodeCountMatrix() {
		if (djikstra == null) {
			return null;
		}
		return djikstra.getNodeCountMatrix();
	}
	
	public ResultSet getNetworkTypes() {
		sqlProcessor.select(NetworkTypesSelect.SQL, null);
		return sqlProcessor.getResultSet();
	}
	
	public ResultSet getCountryIds() {
		sqlProcessor.select(CountryIdSelect.SQL, null);
		return sqlProcessor.getResultSet();
	}
	
	public ResultSet getStops(String networkName) {
		sqlParameters = new Object[2];
		sqlParameters[StopAllSelectByNetworkName.PAR_NETWORK_NAME] = networkName;
		sqlProcessor.select(StopAllSelectByNetworkName.SQL, sqlParameters);
		return sqlProcessor.getResultSet();
	}
	
	public ResultSet getWays(String networkName) {
		sqlParameters = new Object[2];
		sqlParameters[EdgeSelectCoordsByNetworkName.PAR_NETWORK_NAME] = networkName;
		sqlProcessor.select(EdgeSelectCoordsByNetworkName.SQL, sqlParameters);
		return sqlProcessor.getResultSet();
	}
	
	public ResultSet getWaysWithStopNames(String networkName) {
		sqlParameters = new Object[2];
		sqlParameters[EdgeSelectStopNamesAndTime.PAR_NETWORK_NAME] = networkName;
		sqlProcessor.select(EdgeSelectStopNamesAndTime.SQL, sqlParameters);
		return sqlProcessor.getResultSet();
	}
	
	public ResultSet getNetworkNames() {
		sqlProcessor.select(NetworkSelectAllNames.SQL, null);
		return sqlProcessor.getResultSet();
	}
	
	public ResultSet getStopNumAndName(String networkName) {
		sqlParameters = new Object[2];
		sqlParameters[StopNumNameSelectByNetworkName.PAR_NETWORK_NAME] = networkName;
		sqlProcessor.select(StopNumNameSelectByNetworkName.SQL, sqlParameters);
		return sqlProcessor.getResultSet();
	}
	
	public int getNetworkId(String networkName) {
		try {
			sqlParameters = new Object[2];
			sqlParameters[NetworkSelectNetworkIdByName.PAR_NETWORK_NAME] = networkName;
			sqlProcessor.select(NetworkSelectNetworkIdByName.SQL, sqlParameters);
			
			int network_id = -1;
			
			ResultSet resultSet = sqlProcessor.getResultSet();
			if (resultSet != null && resultSet.next()) {
				network_id = resultSet.getInt("network_id");
			}
			if (network_id != -1) {
				return network_id;
			}
			return -1;
		} catch (SQLException e) {
			System.err.println("Network with this name was not found!");
			e.printStackTrace();
			return -1;
		}
	}
	
	public String getNetworkCountryId(String networkName) {
		try {
			sqlParameters = new Object[2];
			sqlParameters[NetworkSelectNetworkCountryIdByName.PAR_NETWORK_NAME] = networkName;
			sqlProcessor.select(NetworkSelectNetworkCountryIdByName.SQL, sqlParameters);
			
			String country_id = null;
			
			ResultSet resultSet = sqlProcessor.getResultSet();
			if (resultSet != null && resultSet.next()) {
				country_id = resultSet.getString("country_id");
			}
			if (country_id != null) {
				return country_id;
			}
			return null;
		} catch (SQLException e) {
			System.err.println("Network with this name was not found!");
			e.printStackTrace();
			return null;
		}
	}
	
	public String getNetworkType(String networkName) {
		try {
			sqlParameters = new Object[2];
			sqlParameters[NetworkSelectNetworkTypeByName.PAR_NETWORK_NAME] = networkName;
			sqlProcessor.select(NetworkSelectNetworkTypeByName.SQL, sqlParameters);
			
			String networkType = null;
			
			ResultSet resultSet = sqlProcessor.getResultSet();
			if (resultSet != null && resultSet.next()) {
				networkType = resultSet.getString("network_type");
			}
			if (networkType != null) {
				return networkType;
			}
			return null;
		} catch (SQLException e) {
			System.err.println("Network with this name was not found!");
			e.printStackTrace();
			return null;
		}
	}
	
	public int[][] getODMatrix(String fileName, ArrayList<Node> nodes) {
		return getDataImporter().readODMatrix(fileName, nodes);
	}
	
//	public void readBusStops(String busStopFileName) {
//		getDataImporter().readBusStops(busStopFileName);
//	}
	
	public ArrayList readBusStops(String fileName, char delimiter) throws FileNotFoundException {
		stops = getDataImporter().readBusStops(fileName, delimiter);
		return stops;
	}
	
	public ArrayList readWays(String fileName, char delimiter) throws FileNotFoundException {
		ways = getDataImporter().readWays(fileName, getStops());
		return ways;
	}

	public void insertNetwork(String networkName, String networkType, String countryId) {
		sqlParameters = new Object[4];
		sqlParameters[NetworkInsert.PAR_COUNTRY_ID] = countryId;
		sqlParameters[NetworkInsert.PAR_NETWORK_TYPE] = networkType;
		sqlParameters[NetworkInsert.PAR_NETWORK_NAME] = networkName;
		
		sqlProcessor.insert(NetworkInsert.SQL, sqlParameters);
		
	}

	public void deleteNetwork(String networkName) {
		sqlParameters = new Object[2];
		sqlParameters[NetworkSelectNetworkIdByName.PAR_NETWORK_NAME] = networkName;
		sqlProcessor.select(NetworkSelectNetworkIdByName.SQL, sqlParameters);
		
		int networkId = -1;
		try {
			if (sqlProcessor.getResultSet().next()) {
				networkId = Integer.parseInt(sqlProcessor.getResultSet().getString("network_id"));
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (networkId < 0) { return; }
		
		String countryId = getNetworkCountryId(networkName);
		stops = getStops();
		deleteStops(networkId, countryId, stops);
		
		sqlParameters = new Object[2];
		sqlParameters[NetworkDelete.PAR_NETWORK_NAME] = networkName;
		sqlProcessor.delete(NetworkDelete.SQL, sqlParameters);
	}
	
	public void insertEdges(ArrayList<Way> ways) {
		for (Way way : ways) {
			insertEdge(way.getStartPoint(), way.getEndPoint(), way.getKm(), way.getMins());
		}
	}
	
	public void insertEdge(BusStop startStop, BusStop endStop, double km, double mins) {
		int startStopId = 0, endStopId = 0;
		
		try {
			sqlProcessor.select("SELECT stop_id from stop where stop_name = '" + startStop.getName() + "'", null);
    	
			if (sqlProcessor.getResultSet().next()) {
				startStopId = Integer.parseInt(sqlProcessor.getResultSet().getString("stop_id"));
			}
		
	    	sqlProcessor.select("SELECT stop_id from stop where stop_name = '" + endStop.getName() + "'", null);
	    	if (sqlProcessor.getResultSet().next()) {
	    		endStopId = Integer.parseInt(sqlProcessor.getResultSet().getString("stop_id"));
			}
	    	
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	if (startStopId == 0 || endStopId == 0) { return; }
    	
		sqlParameters = new Object[4];
		sqlParameters[EdgeInsert.PAR_START_STOP_ID] = startStopId;
		sqlParameters[EdgeInsert.PAR_END_STOP_ID] = endStopId;
		sqlParameters[EdgeInsert.PAR_KM_LENGTH] = km;
		sqlParameters[EdgeInsert.PAR_TIME_MINS] = mins;
		
		sqlProcessor.insert(EdgeInsert.SQL, sqlParameters);
		this.addWay(startStop.getNumber(), endStop.getNumber(), km, mins);
	}
	
	public void insertStops(int networkId, String countryId, ArrayList<BusStop> stops) {
		for (BusStop busStop : stops) {
			insertStop(networkId, countryId, busStop.getNumber(), busStop.getName(), busStop.getCoordinate(), busStop.getNumberOfCustomers());
		}
	}
	
	public void insertStop(int networkId, String countryID, int stopNumber, String stopName, MyCoordinate stopCoordinate, int numOfHabitants) {
		sqlParameters = new Object[8];
		sqlParameters[StopInsert.PAR_NETWORK_ID] = networkId;
		sqlParameters[StopInsert.PAR_COUNTRY_ID] = countryID;
		sqlParameters[StopInsert.PAR_STOP_NUMBER] = stopNumber;
		sqlParameters[StopInsert.PAR_STOP_NAME] = stopName;
		sqlParameters[StopInsert.PAR_LATITUDE] = stopCoordinate.getLatitude();
		sqlParameters[StopInsert.PAR_LONGITUDE] = stopCoordinate.getLongitude();
		sqlParameters[StopInsert.PAR_NUM_OF_HABITANTS] = numOfHabitants;
		
		sqlProcessor.insert(StopInsert.SQL, sqlParameters);
		this.addStop(stopNumber, stopName, stopCoordinate, numOfHabitants);
	}
	
	public boolean addStop(int stopNumber, String stopName, MyCoordinate stopCoordinate, int numOfHabitants) {
		//stops.insertData(new Record(stopNumber, new BusStop(stopNumber, stopName, stopCoordinate)));
		stops.add(new BusStop(stopNumber, stopName, stopCoordinate, numOfHabitants));
		return true;
	}
	
	public void deleteStops(int networkId, String countryID, ArrayList<Node> stops) {
		if (stops == null) {
			return;
		}
		
		for (Node stop : stops) {
			deleteStop(networkId, countryID, ((BusStop)stop).getNumber());
		}
	}
	
	public void deleteStop(int networkId, String countryID, int stopNumber) {
		deleteEdgesOfStop(networkId, countryID, stopNumber);
		
		sqlParameters = new Object[4];
		sqlParameters[StopInsert.PAR_NETWORK_ID] = networkId;
		sqlParameters[StopInsert.PAR_COUNTRY_ID] = countryID;
		sqlParameters[StopInsert.PAR_STOP_NUMBER] = stopNumber;
		
		sqlProcessor.delete(StopDelete.SQL, sqlParameters);
		this.removeStop(stopNumber);
	}
	
	private void deleteEdgesOfStop(int networkId, String countryID, int stopNumber) {
		sqlParameters = new Object[5];
		sqlParameters[EdgesOfStopDelete.PAR_START_NETWORK_ID] = networkId;
		sqlParameters[EdgesOfStopDelete.PAR_START_STOP_ID] = stopNumber;
		sqlParameters[EdgesOfStopDelete.PAR_END_STOP_ID] = stopNumber;
		sqlParameters[EdgesOfStopDelete.PAR_END_NETWORK_ID] = networkId;
		
		sqlProcessor.delete(EdgesOfStopDelete.SQL, sqlParameters);
		this.removeEdges(findStop(stopNumber));
	}

	private void removeEdges(BusStop stop) {
		for (Object o : getWays()) {
			Way way = (Way) o;
			if (way.getStartPoint().equals(stop) || way.getEndPoint().equals(stop)) {
				getWays().remove(way);
			}
		}
		
	}

	public boolean removeStop(int stopNumber) {
		if (!stops.remove(findStop(stopNumber))) {
			System.err.println("Stop with number " + stopNumber + " does not exist.");
			return false;
		}
		return true;
	}
	
	/**
	 * Adds a new way specified by two Coordinates in array.
	 * @param stopName
	 * @param wayCoordinates
	 * @return
	 */
	public boolean addWay(int startNumber, int endNumber, double km, double timeInMins) {
//		if (ways.findData(wayId) == null) {
//			BusStop startStop = (BusStop) stops.findData(startNumber).getValue();
//			BusStop endStop = (BusStop) stops.findData(endNumber).getValue();
//			
//			if (startStop == null || endStop == null) {
//				return false;
//			}
//			
//			ways.insertData(new Record(wayId, new Way(wayId, startStop, endStop, timeInMins)));
//			return true;
//		}
//		return false;
		
		BusStop startStop = this.findStop(startNumber);
		BusStop endStop = this.findStop(endNumber);
		
		if (startStop == null || endStop == null) {
			return false;
		}
		
		if (ways.add(new Way(startStop, endStop, km, timeInMins))) {
			return true;
		}
		return false;
		
	}
	
	public BusStop findStop(int busNumber) {
		for (Node node : stops) {
			BusStop busStop = (BusStop) node;
			if (busStop.getNumber() == busNumber) {
				return busStop;
			}
		}
		return null;
	}
	
//	public boolean removeWay(int wayId) {
//		if (!ways.deleteData(wayId)) {
//			System.err.println("Way with name " + wayId + " does not exist.");
//			return false;
//		}
//		return true;
//	}
	
	public ArrayList getStops() {
		return this.stops;
	}
	
	public ArrayList getWays() {
		return this.ways;
	}
	
	private DataImporter getDataImporter() {
		if (dataImporter == null) {
			dataImporter = new DataImporter();
		}
		return dataImporter;
	}

	public void resetLists() {
		stops = new ArrayList<>();
		ways = new ArrayList<>();
	}
}
