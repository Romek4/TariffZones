package tariffzones.model.sql.interfaces;

public interface MhdStopInsert {
	public static final int PAR_STOP_NUMBER = 1;
	public static final int PAR_STOP_NAME = 2;
	public static final int PAR_CITY = 3;
	public static final int PAR_COUNTRY = 4;
	public static final int PAR_LATITUDE = 5;
	public static final int PAR_LONGITUDE = 6;
	
	public static final String SQL = "INSERT INTO mhd_stop(stop_number, stop_name, city, country, latitude, longitude) "
			+ "VALUES(?, ?, ?, ?, ?, ?)";
}
