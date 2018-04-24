package tariffzones.sql.interfaces;

public interface WayInsert {
	public static final int PAR_START_STOP_ID = 1;
	public static final int PAR_END_STOP_ID = 2;
	public static final int PAR_KM_LENGTH = 3;
	public static final int PAR_TIME_MINS = 4;
//	public static final int PAR_PASSENGERS = 5;
//	public static final int PAR_MOVES = 6;
	
	public static final String SQL = "INSERT INTO edge(start_stop_id, end_stop_id, km_length, time_length)"
			+ "VALUES(?, ?, ?, ?)";	
}
