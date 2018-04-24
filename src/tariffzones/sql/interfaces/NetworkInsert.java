package tariffzones.sql.interfaces;

public interface NetworkInsert {
	public static final int PAR_COUNTRY_ID = 1;
	public static final int PAR_NETWORK_NAME = 2;
	public static final int PAR_NETWORK_TYPE = 3;
	
	public static final String SQL = "INSERT INTO traffnetwork(country_id, network_name, network_type) "
			+ "VALUES(?, ?, ?)";
}
