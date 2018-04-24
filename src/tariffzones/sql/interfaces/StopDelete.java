package tariffzones.sql.interfaces;

public interface StopDelete {
	public static final int PAR_NETWORK_ID = 1;
	public static final int PAR_COUNTRY_ID = 2;
	public static final int PAR_STOP_NUMBER = 3;
	
	public static final String SQL = "DELETE FROM STOP WHERE network_id = ? and country_id = ? and stop_number = ?";
}
