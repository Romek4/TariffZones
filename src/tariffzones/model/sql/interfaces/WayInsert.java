package tariffzones.model.sql.interfaces;

public interface WayInsert {
	public static final int PAR_START_STOP_NUMBER = 1;
	public static final int PAR_END_STOP_NUMBER = 2;
	public static final int PAR_TIME_MINS = 3;
	
	public static final String SQL = "INSERT INTO mhd_way(mhd_stop_start, mhd_stop_end, time_length)"
			+ "VALUES(?, ?, ?)";	
}
