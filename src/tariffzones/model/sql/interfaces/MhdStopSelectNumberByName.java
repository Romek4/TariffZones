package tariffzones.model.sql.interfaces;

public interface MhdStopSelectNumberByName {
	public static final int PAR_STOP_NAME = 1;
	
	public static final String SQL = "SELECT mhd_stop.stop_number FROM mhd_stop WHERE mhd_stop.stop_name = ?";
}
