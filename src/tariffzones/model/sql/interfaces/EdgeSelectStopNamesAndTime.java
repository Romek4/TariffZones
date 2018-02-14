package tariffzones.model.sql.interfaces;

public interface EdgeSelectStopNamesAndTime {

	public static final int PAR_NETWORK_NAME = 1;
	
	public static final String SQL = "SELECT s1.stop_name as stop_1, s2.stop_name as stop_2, e.time_length as time, e.km_length as distance from edge e "
			+ "JOIN stop s1 on(e.start_stop_id = s1.stop_id) "
			+ "JOIN stop s2 on(e.end_stop_id = s2.stop_id) "
			+ "JOIN traffnetwork tn on(s1.network_id = tn.network_id) "
			+ "WHERE tn.network_name = ?";
}