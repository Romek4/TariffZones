package tariffzones.model.sql.interfaces;

public interface CitySelectCityIdByCityName {
	
	public static final int PAR_CITY_NAME = 1;
	
	public static final String SQL = "SELECT city_id FROM city WHERE city_name = '?'";
}
