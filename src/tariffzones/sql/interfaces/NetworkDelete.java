package tariffzones.sql.interfaces;

public interface NetworkDelete {
	public static final int PAR_NETWORK_NAME = 1;
	
	public static final String SQL = "DELETE FROM traffnetwork WHERE network_name = ?";
}
