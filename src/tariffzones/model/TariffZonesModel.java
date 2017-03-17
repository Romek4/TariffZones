package tariffzones.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.ResultSetRow;

import Splay_tree_package.Record;
import Splay_tree_package.SplayTree;
import tariffzones.model.sql.interfaces.CitySelectAllName;
import tariffzones.model.sql.interfaces.CitySelectCityIdByCityName;
import tariffzones.model.sql.interfaces.MhdStopInsert;
import tariffzones.model.sql.interfaces.MhdStopSelect;
import tariffzones.model.sql.interfaces.MhdStopSelectByCityName;
import tariffzones.model.sql.interfaces.MhdWaySelectCoordsOfWayInCity;
import tariffzones.model.sql.interfaces.MhdWaySelectStopNamesAndTime;
import tariffzones.model.sql.processor.DataImporter;
import tariffzones.model.sql.processor.SQLProcessor;


public class TariffZonesModel {
	private DataImporter dataImporter;
	private SplayTree<Integer, BusStop> stops;
	private SplayTree<Integer, Way> ways;
	
	private SQLProcessor sqlProcessor;
	private Object[] sqlParameters;
	
	public TariffZonesModel() {
		stops = new SplayTree<>();
		ways = new SplayTree<>();
		sqlProcessor = new SQLProcessor();
		sqlProcessor.connectDatabase("admin", "admin");
	}
	
	public ResultSet getMhdStops(String cityName) {
		sqlParameters = new Object[2];
		sqlParameters[MhdStopSelectByCityName.PAR_CITY_NAME] = cityName;
		sqlProcessor.select(MhdStopSelectByCityName.SQL, sqlParameters);
		return sqlProcessor.getResultSet();
	}
	
	public ResultSet getWays(String cityName) {
		sqlParameters = new Object[2];
		sqlParameters[MhdWaySelectCoordsOfWayInCity.PAR_CITY_NAME] = cityName;
		sqlProcessor.select(MhdWaySelectCoordsOfWayInCity.SQL, sqlParameters);
		return sqlProcessor.getResultSet();
	}
	
	public ResultSet getWaysWithStopNames(String cityName) {
		sqlParameters = new Object[2];
		sqlParameters[MhdWaySelectStopNamesAndTime.PAR_CITY_NAME] = cityName;
		sqlProcessor.select(MhdWaySelectStopNamesAndTime.SQL, sqlParameters);
		return sqlProcessor.getResultSet();
	}
	
	public ResultSet getCityNames() {
		sqlProcessor.select(CitySelectAllName.SQL, null);
		return sqlProcessor.getResultSet();
	}
	
	public ResultSet getStopNumAndName(String cityName) {
		sqlParameters = new Object[2];
		sqlParameters[MhdStopSelect.PAR_CITY_NAME] = cityName;
		sqlProcessor.select(MhdStopSelect.SQL, sqlParameters);
		return sqlProcessor.getResultSet();
	}
	
	private int getCityId(String cityName) {
		try {
			sqlParameters = new Object[2];
			sqlParameters[CitySelectCityIdByCityName.PAR_CITY_NAME] = cityName;
			sqlProcessor.select(CitySelectCityIdByCityName.SQL, sqlParameters);
			
			int city_id = -1;
			
			ResultSet resultSet = sqlProcessor.getResultSet();
			if (resultSet != null && resultSet.next()) {
				city_id = resultSet.getInt("city_id");
			}
			if (city_id != -1) {
				return city_id;
			}
			return -1;
		} catch (SQLException e) {
			System.err.println("City with this name was not found!");
			e.printStackTrace();
			return -1;
		}
	}
	
	
	public void readBusStops(String busStopFileName) {
		if (dataImporter == null) {
			dataImporter = new DataImporter();
		}
		dataImporter.readBusStops(busStopFileName);
	}
	
	public boolean addStop(int stopNumber, String stopName, MyCoordinate stopCoordinate) {
		if (stops.findData(stopNumber) != null) {
			return false;
		}
		sqlParameters = new Object[7];
		sqlParameters[MhdStopInsert.PAR_STOP_NUMBER] = stopNumber;
		sqlParameters[MhdStopInsert.PAR_STOP_NAME] = stopName;
		sqlParameters[MhdStopInsert.PAR_CITY] = "";
		sqlParameters[MhdStopInsert.PAR_COUNTRY] = "";
		sqlParameters[MhdStopInsert.PAR_LATITUDE] = stopCoordinate.getLatitude();
		sqlParameters[MhdStopInsert.PAR_LONGITUDE] = stopCoordinate.getLongitude();
		
		stops.insertData(new Record(stopNumber, new BusStop(stopNumber, stopName, stopCoordinate)));
		return true;
	}
	
	public boolean removeStop(int stopNumber) {
		if (!stops.deleteData(stopNumber)) {
			System.err.println("Stop with number " + stopNumber + " does not exist.");
			return false;
		}
		return true;
	}
	
	public BusStop getStopCoordinate(int stopNumber) {
		return (BusStop) stops.findData(stopNumber).getValue();
	}
	
	/**
	 * Adds a new way specified by two Coordinates in array.
	 * @param stopName
	 * @param wayCoordinates
	 * @return
	 */
	public boolean addWay(int wayId, MyCoordinate[] wayCoordinates, double timeInMins) {
		if (wayCoordinates == null || wayCoordinates.length != 2) {
			System.err.println("Way is specified by two Coordinates.");
			return false;
		}
		if (ways.findData(wayId) == null) {
			ways.insertData(new Record(wayId, new Way(wayId, wayCoordinates[0], wayCoordinates[1], timeInMins)));
			return true;
		}
		return false;
	}
	
	public boolean removeWay(int wayId) {
		if (!ways.deleteData(wayId)) {
			System.err.println("Way with name " + wayId + " does not exist.");
			return false;
		}
		return true;
	}
	
	public Way getWayCoordinates(int wayId) {
		return (Way) ways.findData(wayId).getValue();
	}
	
	public SplayTree getStops() {
		return this.stops;
	}
	
	public SplayTree getWays() {
		return this.ways;
	}
}
