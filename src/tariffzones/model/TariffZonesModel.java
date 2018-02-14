package tariffzones.model;

import java.awt.geom.Ellipse2D;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jxmapviewer.viewer.GeoPosition;

import com.mysql.jdbc.ResultSetRow;

import tariffzones.model.sql.interfaces.NetworkSelectAllNames;
import tariffzones.model.sql.interfaces.NetworkSelectByName;
import tariffzones.model.sql.interfaces.NetworkSelectNetworkCountryIdByName;
import tariffzones.model.sql.interfaces.NetworkSelectNetworkIdByName;
import tariffzones.model.sql.interfaces.NetworkSelectNetworkTypeByName;
import tariffzones.model.sql.interfaces.NetworkTypesSelect;
import tariffzones.model.sql.interfaces.StopInsert;
import tariffzones.model.sql.interfaces.StopNumNameSelectByNetworkName;
import tariffzones.model.sql.interfaces.StopUpdate;
import tariffzones.model.sql.interfaces.WayDelete;
import tariffzones.model.sql.interfaces.StopAllSelectByNetworkName;
import tariffzones.model.sql.interfaces.StopDelete;
import tariffzones.model.sql.interfaces.CountryIdSelect;
import tariffzones.model.sql.interfaces.WayInsert;
import tariffzones.model.sql.interfaces.WayUpdate;
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
	private Djikstra djikstra;
	private Network network;
	private ArrayList<Network> networks;	
	
	private ArrayList<Stop> unsavedStops;
	private ArrayList<Way> unsavedWays;
	
	private SQLProcessor sqlProcessor;
	private Object[] sqlParameters;
	
	public TariffZonesModel() {
		sqlProcessor = new SQLProcessor();
		sqlProcessor.connectDatabase("admin", "admin");
	}
	
	public void runDjikstra() {
		djikstra = new Djikstra(new Graph(getNetworkStops(), getNetworkWays()));
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
	
	public List getNetworks() {
		sqlProcessor.select(NetworkSelectByName.SQL, sqlParameters);
		
		networks = new ArrayList<>();
		ResultSet resultSet = sqlProcessor.getResultSet();
		if (resultSet != null) {
			try {
				while (resultSet.next()) {
					networks.add(new Network(resultSet.getInt("network_id"), resultSet.getString("network_name"), resultSet.getString("network_type"), resultSet.getString("country_id")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return networks;
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
	
	public int[][] getODMatrix(String fileName, ArrayList<Node> nodes) throws IOException, FileNotFoundException {
		return getDataImporter().readODMatrix(fileName, nodes);
	}
	
//	public void readBusStops(String busStopFileName) {
//		getDataImporter().readBusStops(busStopFileName);
//	}
	
	public ArrayList readStops(String fileName) throws IOException, FileNotFoundException {
		return getDataImporter().readStops(fileName);
	}
	
	public ArrayList readWays(String fileName, ArrayList stops) throws IOException, FileNotFoundException {
		return getDataImporter().readWays(fileName, stops);
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
		deleteWays(getNetworkWays());
		deleteStops(networkId, countryId, getNetworkStops());
		
		sqlParameters = new Object[2];
		sqlParameters[NetworkDelete.PAR_NETWORK_NAME] = networkName;
		sqlProcessor.delete(NetworkDelete.SQL, sqlParameters);
	}
	
	private void insertWay(Stop startStop, Stop endStop, double distance, double timeLength) {
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
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
    	if (startStopId == 0 || endStopId == 0) { return; }
    	
		sqlParameters = new Object[5];
		sqlParameters[WayInsert.PAR_START_STOP_ID] = startStopId;
		sqlParameters[WayInsert.PAR_END_STOP_ID] = endStopId;
		sqlParameters[WayInsert.PAR_KM_LENGTH] = distance;
		sqlParameters[WayInsert.PAR_TIME_MINS] = timeLength;
		
		sqlProcessor.insert(WayInsert.SQL, sqlParameters);
	}
	
	private void updateWay(Stop startStop, Stop endStop, double distance, double timeLength) {
		sqlParameters = new Object[5];
		sqlParameters[WayUpdate.PAR_START_STOP_ID] = startStop.getId();
		sqlParameters[WayUpdate.PAR_END_STOP_ID] = endStop.getId();
		sqlParameters[WayUpdate.PAR_KM_LENGTH] = distance;
		sqlParameters[WayUpdate.PAR_TIME_MINS] = timeLength;
		
		sqlProcessor.update(WayUpdate.SQL, sqlParameters);
	}
	
	private void deleteWay(Stop startStop, Stop endStop) {
		sqlParameters = new Object[3];
		sqlParameters[WayDelete.PAR_START_STOP_ID] = startStop.getId();
		sqlParameters[WayDelete.PAR_END_STOP_ID] = endStop.getId();
		
		sqlProcessor.delete(WayDelete.SQL, sqlParameters);
	}
	
	private void insertStop(int networkId, String countryID, int stopNumber, String stopName, double latitude, double longitude, int numOfHabitants) {
		sqlParameters = new Object[8];
		sqlParameters[StopInsert.PAR_NETWORK_ID] = networkId;
		sqlParameters[StopInsert.PAR_COUNTRY_ID] = countryID;
		sqlParameters[StopInsert.PAR_STOP_NUMBER] = stopNumber;
		sqlParameters[StopInsert.PAR_STOP_NAME] = stopName;
		sqlParameters[StopInsert.PAR_LATITUDE] = latitude;
		sqlParameters[StopInsert.PAR_LONGITUDE] = longitude;
		sqlParameters[StopInsert.PAR_NUM_OF_HABITANTS] = numOfHabitants;
		
		sqlProcessor.insert(StopInsert.SQL, sqlParameters);
	}
	
	private void updateStop(int stopID, int stopNumber, String stopName, double latitude, double longitude, int numOfHabitants) {
		sqlParameters = new Object[8];
		sqlParameters[StopUpdate.PAR_STOP_NUMBER] = stopNumber;
		sqlParameters[StopUpdate.PAR_STOP_NAME] = stopName;
		sqlParameters[StopUpdate.PAR_LATITUDE] = latitude;
		sqlParameters[StopUpdate.PAR_LONGITUDE] = longitude;
		sqlParameters[StopUpdate.PAR_NUM_OF_HABITANTS] = numOfHabitants;
		sqlParameters[StopUpdate.PAR_STOP_ID] = stopID;
		
		sqlProcessor.update(StopUpdate.SQL, sqlParameters);
	}
	
	public boolean addStop(int stopNumber, String stopName, int numOfHabitants, double latitude, double longitude) {
		if (getNetwork().addStop(new Stop(network, stopNumber, stopName, numOfHabitants, latitude, longitude))) {
			return true;
		}
		return false;
	}
	
	public boolean addStop(int id, int stopNumber, String stopName, int numOfHabitants, double latitude, double longitude) {
		if (getNetwork().addStop(new Stop(id, network, stopNumber, stopName, numOfHabitants, latitude, longitude))) {
			return true;
		}
		return false;
	}
	
	public boolean addAndRememberStop(int stopNumber, String stopName, int numOfHabitants, double latitude, double longitude) {
		Stop stop = new Stop(network, stopNumber, stopName, numOfHabitants, latitude, longitude);
		stop.setState(State.ADDED);
		if (getNetwork().addStop(stop) && getUnsavedStops().add(stop)) {
			return true;
		}
		return false;
	}
	
	public void deleteStops(int networkId, String countryID, ArrayList<Stop> stops) {
		if (stops == null) {
			return;
		}
		
		for (Stop stop : stops) {
			deleteStop(networkId, countryID, stop.getNumber());
		}
	}
	
	public void deleteWays(ArrayList<Way> ways) {
		if (ways == null) {
			return;
		}
		
		for (Way way : ways) {
			deleteWay(way.getStartPoint(), way.getEndPoint());;
		}
	}
	
	public boolean removeAndRememberStop(Stop stop) {
		if (stop != null) {
			if (getUnsavedStops().contains(stop)) {
				getUnsavedStops().remove(stop);
				return true;
			}
			stop.setState(State.REMOVED);
			getUnsavedStops().add(stop);
			for (Way way : stop.getPartOfWays()) {
				removeAndRememberWay(way);
			}
			getNetwork().removeStop(stop);
			return true;
		}
		return false;
	}

	public boolean removeAndRememberWay(Way way) {
		if (way != null) {
			if (getUnsavedWays().contains(way)) {
				getUnsavedWays().remove(way);
				return true;
			}
			way.setState(State.REMOVED);
			getUnsavedWays().add(way);
			getNetwork().removeWay(way);
			return true;
		}
		return false;
	}
	
//	public boolean removeAndRememberStop(int stopNumber) {
//		Stop stop = getNetwork().findStop(stopNumber);
//		if (stop != null) {
//			if (getUnsavedStops().contains(stop)) {
//				getUnsavedStops().remove(stop);
//				return true;
//			}
//			stop.setState(State.REMOVED);
//			getUnsavedStops().add(stop);
//			getNetwork().removeStop(stop);
//			return true;
//		}
//		return false;
//	}
	
	public void deleteStop(int networkId, String countryID, int stopNumber) {
//		deleteEdgesOfStop(networkId, countryID, stopNumber);
		
		sqlParameters = new Object[4];
		sqlParameters[StopInsert.PAR_NETWORK_ID] = networkId;
		sqlParameters[StopInsert.PAR_COUNTRY_ID] = countryID;
		sqlParameters[StopInsert.PAR_STOP_NUMBER] = stopNumber;
		
		sqlProcessor.delete(StopDelete.SQL, sqlParameters);
	}
	
//	private void deleteEdgesOfStop(int networkId, String countryID, int stopNumber) {
//		sqlParameters = new Object[5];
//		sqlParameters[EdgesOfStopDelete.PAR_START_NETWORK_ID] = networkId;
//		sqlParameters[EdgesOfStopDelete.PAR_START_STOP_ID] = stopNumber;
//		sqlParameters[EdgesOfStopDelete.PAR_END_STOP_ID] = stopNumber;
//		sqlParameters[EdgesOfStopDelete.PAR_END_NETWORK_ID] = networkId;
//		
//		sqlProcessor.delete(EdgesOfStopDelete.SQL, sqlParameters);
//		this.getNetwork().removeStopEdges(stopNumber);
//	}

	public boolean addWay(int startNumber, int endNumber, double km, double timeInMins) {
		Stop startStop = getNetwork().findStop(startNumber);
		Stop endStop = getNetwork().findStop(endNumber);
		
		if (startStop != null && endStop != null)
		{
			if (getNetwork().addWay(new Way(startStop, endStop, km, timeInMins))) {
				return true;
			}
		}
		return false;
	}

	public boolean addAndRememberWay(Stop startStop, Stop endStop, double wayLength, double wayTimeLength) {
		Way way = new Way(startStop, endStop, wayLength, wayTimeLength);
		way.setState(State.ADDED);
		if (getNetwork().addWay(way) && getUnsavedWays().add(way)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Saved stops from unsavedStops and ways from unsavedWays lists to database.
	 */
	public boolean saveChangesToDatabase() {
		try {
		//save stops
			for (Stop stop : getUnsavedStops()) {
				if (stop.getState() == State.ADDED) {
					insertStop(getNetwork().getNetworkID(), getNetwork().getCountryID(), stop.getNumber(), stop.getName(), stop.getLatitude(), stop.getLongitude(), stop.getNumberOfCustomers());
				}
				else if (stop.getState() == State.MODIFIED) {
					updateStop(stop.getId(), stop.getNumber(), stop.getName(), stop.getLatitude(), stop.getLongitude(), stop.getNumberOfCustomers());
				}
				getUnsavedStops().remove(stop);
			}
			
			//save ways
			for (Way way : getUnsavedWays()) {
				if (way.getState() == State.ADDED) {
					insertWay(way.getStartPoint(), way.getEndPoint(), way.getDistance(), way.getTimeLength());
				}
				else if (way.getState() == State.MODIFIED) {
					updateWay(way.getStartPoint(), way.getEndPoint(), way.getDistance(), way.getTimeLength());
				}
				else if (way.getState() == State.REMOVED) {
					deleteWay(way.getStartPoint(), way.getEndPoint());
				}
			}
			
			//this order is important - first i need to delete way between two stops, then I can delete stops themselves
			for (Stop stop : getUnsavedStops()) {
				if (stop.getState() == State.REMOVED) {
					deleteStop(getNetwork().getNetworkID(), getNetwork().getCountryID(), stop.getNumber());
				}
			}
			
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	public ArrayList<Stop> getUnsavedStops() {
		if (unsavedStops == null) {
			unsavedStops = new ArrayList<Stop>();
		}
		return unsavedStops;
	}
	
	public ArrayList<Way> getUnsavedWays() {
		if (unsavedWays == null) {
			unsavedWays = new ArrayList<Way>();
		}
		return unsavedWays;
	}
	
	public void setUnsavedStops(ArrayList<Stop> unsavedStops) {
		this.unsavedStops = unsavedStops;
	}
	
	public void setUnsavedWays(ArrayList<Way> unsavedWays) {
		this.unsavedWays = unsavedWays;
	}
	
	public ArrayList getNetworkStops() {
		return this.getNetwork().getStops();
	}
	
	public ArrayList getNetworkWays() {
		return this.getNetwork().getWays();
	}
	
	public DataImporter getDataImporter() {
		if (dataImporter == null) {
			dataImporter = new DataImporter();
		}
		return dataImporter;
	}

	public void resetLists() {
		getNetwork().setStops(new ArrayList<>());
		getNetwork().setWays(new ArrayList<>());
		unsavedStops = null;
		unsavedWays = null;
	}
	
	public Network getNetwork() {
		if (network == null) {
			//dummy network
			network = new Network(0, "", "", "");
		}
		return network;
	}
	
	public void setNetwork(Network network) {
		this.network = network;
	}
}
