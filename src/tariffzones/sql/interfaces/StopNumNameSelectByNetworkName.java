package tariffzones.sql.interfaces;

public interface StopNumNameSelectByNetworkName {

	public static final int PAR_NETWORK_NAME = 1;
	
	public static final String SQL = "SELECT stop_number, stop_name FROM stop s JOIN traffnetwork tn ON (s.network_id = tn.network_id)"
			+ "WHERE tn.network_name = ?";
}
