package tariffzones.model.sql.interfaces;

public interface MhdStopSelect {

	public static final int PAR_CITY_NAME = 1;
	
	public static final String SQL = "SELECT stop_number, stop_name FROM mhd_stop s JOIN city c ON (s.city = c.city_id)"
			+ "WHERE c.city_name = ?";
}
