package tariffzones.basicobjects;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tariffzones.processor.djikstra.Djikstra;
import tariffzones.processor.djikstra.Graph;
import tariffzones.processor.djikstra.Node;
import tariffzones.processor.voronoi.Voronoi;
import tariffzones.sql.SQLProcessor;
import tariffzones.sql.interfaces.CountryIdSelect;
import tariffzones.sql.interfaces.EdgeSelectCoordsByNetworkName;
import tariffzones.sql.interfaces.EdgeSelectStopNamesAndTime;
import tariffzones.sql.interfaces.NetworkDelete;
import tariffzones.sql.interfaces.NetworkInsert;
import tariffzones.sql.interfaces.NetworkSelectAllNames;
import tariffzones.sql.interfaces.NetworkSelectByName;
import tariffzones.sql.interfaces.NetworkSelectNetworkCountryIdByName;
import tariffzones.sql.interfaces.NetworkSelectNetworkIdByName;
import tariffzones.sql.interfaces.NetworkSelectNetworkTypeByName;
import tariffzones.sql.interfaces.NetworkTypesSelect;
import tariffzones.sql.interfaces.StopAllSelectByNetworkName;
import tariffzones.sql.interfaces.StopDelete;
import tariffzones.sql.interfaces.StopInsert;
import tariffzones.sql.interfaces.StopNumNameSelectByNetworkName;
import tariffzones.sql.interfaces.StopUpdate;
import tariffzones.sql.interfaces.WayDelete;
import tariffzones.sql.interfaces.WayInsert;
import tariffzones.sql.interfaces.WayUpdate;


public class TariffZonesModel {
	private DataImporter dataImporter;
	private Network network;
	private ArrayList<Network> networks;	
	
	private ArrayList<Stop> unsavedStops;
	private ArrayList<Way> unsavedWays;
	
	private SQLProcessor sqlProcessor;
	private Object[] sqlParameters;
	
	private boolean connectedToDB = false;
	
	public TariffZonesModel() {
		sqlProcessor = new SQLProcessor();
	}
	
	public boolean connectToDB(String dbUrl, String username, String password) {
		connectedToDB = sqlProcessor.connectDatabase(dbUrl, username, password);getClass();
		return connectedToDB;
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
		sqlParameters = new Object[1];
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
	
	public int[][] getODMatrixFromFile(String fileName, ArrayList<Stop> stops) throws IOException, FileNotFoundException {
		return getDataImporter().readODMatrix(fileName, stops);
	}
	
	public HashMap<Double, Double> getPricesFromFile(String fileName) throws IOException, FileNotFoundException {
		return getDataImporter().readPrices(fileName);
	}
	
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
		
			if (networkId < 0) { return; }
			
			String countryId = getNetworkCountryId(networkName);
			deleteWays(getNetworkWays());
			deleteStops(networkId, countryId, getNetworkStops());
			
			sqlParameters = new Object[2];
			sqlParameters[NetworkDelete.PAR_NETWORK_NAME] = networkName;
			sqlProcessor.delete(NetworkDelete.SQL, sqlParameters);

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void insertWay(int networkId, Stop startStop, Stop endStop, double distance, double timeLength) {
		int startStopId = 0, endStopId = 0;
		
		try {
			sqlProcessor.select("SELECT stop_id from stop where stop_name = '" + startStop.getName() + "' AND network_id = " + networkId, null);
    	
			if (sqlProcessor.getResultSet().next()) {
				startStopId = Integer.parseInt(sqlProcessor.getResultSet().getString("stop_id"));
			}
		
			sqlProcessor.select("SELECT stop_id from stop where stop_name = '" + endStop.getName() + "' AND network_id = " + networkId, null);
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
		if (getNetwork() == null) {
			return false;
		}
		
		if (getNetwork().addStop(new Stop(network, stopNumber, stopName, numOfHabitants, latitude, longitude))) {
			return true;
		}
		return false;
	}
	
	public boolean addStop(int id, int stopNumber, String stopName, int numOfHabitants, double latitude, double longitude) {
		if (getNetwork() == null) {
			return false;
		}
		
		if (getNetwork().addStop(new Stop(id, network, stopNumber, stopName, numOfHabitants, latitude, longitude))) {
			return true;
		}
		return false;
	}
	
	public boolean addAndRememberStop(int stopNumber, String stopName, int numOfHabitants, double latitude, double longitude) {
		Stop stop = new Stop(network, stopNumber, stopName, numOfHabitants, latitude, longitude);
		stop.setState(State.ADDED);
		if (getNetwork() != null && getNetwork().addStop(stop) && getUnsavedStops().add(stop)) {
			return true;
		}
		return false;
	}
	
	public boolean removeAndRememberStop(Stop stop) {
		if (getNetwork() == null) {
			return false;
		}
		
		if (stop != null) {
			if (getUnsavedStops().contains(stop) && stop.getState().equals(State.ADDED)) {
				getUnsavedStops().remove(stop);
				return true;
			}
			stop.setState(State.REMOVED);
			getUnsavedStops().add(stop);
			for (int i = 0; i < stop.getPartOfWays().size(); i++) {
				removeAndRememberWay(stop.getPartOfWays().get(i));
			}
//			for (Way way : stop.getPartOfWays()) {
//				removeAndRememberWay(way);
//			}
			getNetwork().removeStop(stop);
			if (stop.getZone() != null) {
				stop.getZone().getStopsInZone().remove(stop);
				
			}
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
	
	public void deleteStop(int networkId, String countryID, int stopNumber) {
		sqlParameters = new Object[4];
		sqlParameters[StopInsert.PAR_NETWORK_ID] = networkId;
		sqlParameters[StopInsert.PAR_COUNTRY_ID] = countryID;
		sqlParameters[StopInsert.PAR_STOP_NUMBER] = stopNumber;
		
		sqlProcessor.delete(StopDelete.SQL, sqlParameters);
	}
	
	public void deleteWays(ArrayList<Way> ways) {
		if (ways == null) {
			return;
		}
		
		for (Way way : ways) {
			deleteWay(way.getStartPoint(), way.getEndPoint());;
		}
	}

	public boolean removeAndRememberWay(Way way) {
		if (getNetwork() == null) {
			return false;
		}
		
		if (way != null) {
			if (getUnsavedWays().contains(way) && way.getState().equals(State.ADDED)) {
				getUnsavedWays().remove(way);
			}
			way.setState(State.REMOVED);
			getUnsavedWays().add(way);
			getNetwork().removeWay(way);
			if (way.getStartPoint().getZone() != null) {
				way.getStartPoint().getZone().getWaysInZone().remove(way); //TODO: look TODO in getWaysInZone()
			}
			return true;
		}
		return false;
	}

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
		if (getNetwork() == null) {
			return false;
		}
		
		Way way = new Way(startStop, endStop, wayLength, wayTimeLength);
		way.setState(State.ADDED);
		if (getNetwork().addWay(way) && getUnsavedWays().add(way)) {
			startStop.addWay(way);
			endStop.addWay(way);
			return true;
		}
		return false;
	}

	public boolean stopExists(int stopNumber, String stopName) {
		if (network == null && network.getStops() == null) {
			return false;
		}
		
		for (Stop stop : network.getStops()) {
			if (stop.getNumber() == stopNumber || stop.getName().equals(stopName)) {
				return true;
			}
		}
		return false;
	}

	public boolean wayExists(Stop startStop, Stop endStop) {
		for (Way way : startStop.getPartOfWays()) {
			if (way.getStartPoint().equals(endStop) || way.getEndPoint().equals(endStop)) {
				return true;
			}
		}
		return false;
	}
	
	public void runVoronoi() {
		double westBorder = 180; //max longitude
		double eastBorder = 0; //min longitude
		double northBorder = 0; //min latitude
		double southBorder = 90; //max latitude
		
		for(Object o : getNetworkStops()) {
			Stop stop = (Stop) o;
			if (westBorder > stop.getLongitude()) {
				westBorder = stop.getLongitude();
			}
			if (eastBorder < stop.getLongitude()) {
				eastBorder = stop.getLongitude();
			}
			if (northBorder < stop.getLatitude()) {
				northBorder = stop.getLatitude();
			}
			if (southBorder > stop.getLatitude()) {
				southBorder = stop.getLatitude();
			}
		}
		
		Voronoi voronoi = new Voronoi(getNetworkStops(), westBorder, eastBorder, northBorder, southBorder);
		voronoi.generateVoronoi();
	}
	
	/**
	 * Saved stops from unsavedStops and ways from unsavedWays lists to database.
	 */
	public boolean saveChangesToDatabase() {
		if (getNetwork() == null) {
			return false;
		}
		
		try {
			//save stops
			for (Stop stop : getUnsavedStops()) {
				if (stop.getState() == State.ADDED) {
					insertStop(getNetwork().getNetworkID(), getNetwork().getCountryID(), stop.getNumber(), stop.getName(), stop.getLatitude(), stop.getLongitude(), stop.getNumberOfCustomers());
				}
				else if (stop.getState() == State.MODIFIED) {
					updateStop(stop.getId(), stop.getNumber(), stop.getName(), stop.getLatitude(), stop.getLongitude(), stop.getNumberOfCustomers());
				}
			}
			
			//save ways
			for (Way way : getUnsavedWays()) {
				if (way.getState() == State.ADDED) {
					insertWay(getNetwork().getNetworkID(), way.getStartPoint(), way.getEndPoint(), way.getDistance(), way.getTimeLength());
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
			e.printStackTrace();
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
		if (getNetwork() == null) {
			return null;
		}
		return this.getNetwork().getStops();
	}
	
	public ArrayList getNetworkWays() {
		if (getNetwork() == null) {
			return null;
		}
		return this.getNetwork().getWays();
	}
	
	public DataImporter getDataImporter() {
		if (dataImporter == null) {
			dataImporter = new DataImporter();
		}
		return dataImporter;
	}

	public void resetLists() {
		if (getNetwork() != null) {
			getNetwork().setStops(new ArrayList<>());
			getNetwork().setWays(new ArrayList<>());
		}
		unsavedStops = null;
		unsavedWays = null;
	}
	
	public Network getNetwork() {
		if (network == null) {
			//dummy network
//			network = new Network(0, "", "", "");
		}
		return network;
	}
	
	public void setNetwork(Network network) {
		this.network = network;
	}
}
