package tariffzones.model.sql.interfaces;

public interface NetworkSelectNetworkIdByName {
	
	public static final int PAR_NETWORK_NAME = 1;
	
	public static final String SQL = "SELECT network_id FROM traffnetwork WHERE network_name = ?";
}