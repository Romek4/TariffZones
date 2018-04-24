package tariffzones.sql.interfaces;

public interface StopInsert {
	public static final int PAR_NETWORK_ID = 1;
	public static final int PAR_COUNTRY_ID = 2;
	public static final int PAR_STOP_NUMBER = 3;
	public static final int PAR_STOP_NAME = 4;
	public static final int PAR_LATITUDE = 5;
	public static final int PAR_LONGITUDE = 6;
	public static final int PAR_NUM_OF_HABITANTS = 7;
	
	public static final String SQL = "INSERT INTO stop(network_id, country_id, stop_number, stop_name, latitude, longitude, num_of_habitants) "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?)";
}
