package tariffzones.model.sql.interfaces;

public interface StopUpdate {
	public static final int PAR_STOP_NUMBER = 1;
	public static final int PAR_STOP_NAME = 2;
	public static final int PAR_LATITUDE = 3;
	public static final int PAR_LONGITUDE = 4;
	public static final int PAR_NUM_OF_HABITANTS = 5;
	public static final int PAR_STOP_ID = 6;
	
	public static final String SQL = "UPDATE stop "
			+ "SET stop_number = ?, stop_name = ?, latitude = ?, longitude = ?, num_of_habitants = ? "
			+ "WHERE stop_id = ?";
}
