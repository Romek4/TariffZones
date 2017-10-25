package tariffzones.model.sql.interfaces;

public interface StopSelectNumberByName {
	public static final int PAR_STOP_NAME = 1;
	
	public static final String SQL = "SELECT stop_number FROM stop WHERE stop_name = ?";
}
