package tariffzones.model.sql.interfaces;

public interface EdgesOfStopDelete {
	public static final int PAR_START_NETWORK_ID = 1;
	public static final int PAR_START_STOP_ID = 2;
	public static final int PAR_END_STOP_ID = 3;
	public static final int PAR_END_NETWORK_ID = 4;
	
	public static final String SQL = "DELETE FROM edge WHERE start_stop_id"
			+ " in (select stop_id from stop where network_id = ? and stop_number = ?)"
			+ " or end_stop_id in (select stop_id from stop where network_id = ? and stop_number = ?)";
}
