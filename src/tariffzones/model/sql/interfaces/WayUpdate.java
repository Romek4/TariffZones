package tariffzones.model.sql.interfaces;

public interface WayUpdate {
	public static final int PAR_KM_LENGTH = 1;
	public static final int PAR_TIME_MINS = 2;
//	public static final int PAR_PASSENGERS = 3;
//	public static final int PAR_MOVES = 4;
	public static final int PAR_START_STOP_ID = 3;
	public static final int PAR_END_STOP_ID = 4;
	
	public static final String SQL = "UPDATE edge "
			+ "SET km_length = ?, time_length = ? "
			+ "WHERE start_stop_id = ? AND end_stop_id = ?";	
}
