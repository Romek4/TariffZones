package tariffzones.model.sql.interfaces;

public interface MhdWaySelectStopNamesAndTime {

	public static final int PAR_CITY_NAME = 1;
	
	public static final String SQL = "SELECT s1.stop_name as Stop_1, s2.stop_name as Stop_2, w.time_length as Time from mhd_way w "
			+ "JOIN mhd_stop s1 on(w.mhd_stop_start = s1.stop_number) "
			+ "JOIN mhd_stop s2 on(w.mhd_stop_end = s2.stop_number) "
			+ "JOIN city c on(s1.city = c.city_id) "
			+ "WHERE c.city_name = ?";
}