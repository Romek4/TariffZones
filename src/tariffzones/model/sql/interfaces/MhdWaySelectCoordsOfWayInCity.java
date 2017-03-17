package tariffzones.model.sql.interfaces;

public interface MhdWaySelectCoordsOfWayInCity {

	public static final int PAR_CITY_NAME = 1;
	
	public static final String SQL = "select w.way_id, w.time_length, s1.latitude as start_lat, s1.longitude as start_lon, s2.latitude as end_lat, s2.longitude as end_lon "
			+ "FROM mhd_stop s1 "
			+ "JOIN mhd_way w on(s1.stop_number = w.mhd_stop_start) "
			+ "JOIN mhd_stop s2 on(w.mhd_stop_end = s2.stop_number) "
			+ "JOIN city c on(s1.city = c.city_id)"
			+ "WHERE c.city_name = ?";
	
}
