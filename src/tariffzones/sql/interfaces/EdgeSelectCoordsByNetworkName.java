package tariffzones.sql.interfaces;

public interface EdgeSelectCoordsByNetworkName {

	public static final int PAR_NETWORK_NAME = 1;
	
	public static final String SQL = "select e.edge_id, e.time_length, e.km_length, "
			+ "s1.stop_number as start_number, s1.latitude as start_lat, s1.longitude as start_lon, "
			+ "s2.stop_number as end_number, s2.latitude as end_lat, s2.longitude as end_lon "
			+ "FROM stop s1 "
			+ "JOIN edge e on(s1.stop_id = e.start_stop_id) "
			+ "JOIN stop s2 on(e.end_stop_id = s2.stop_id) "
			+ "JOIN traffnetwork tn on(s1.network_id = tn.network_id)"
			+ "WHERE tn.network_name = ?";
	
}
