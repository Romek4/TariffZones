package tariffzones.model.sql.interfaces;

public interface WayDelete {
	public static final int PAR_START_STOP_ID = 1;
	public static final int PAR_END_STOP_ID = 2;
	
	public static final String SQL = "DELETE FROM EDGE WHERE start_stop_id = ? AND end_stop_id = ?";
}
