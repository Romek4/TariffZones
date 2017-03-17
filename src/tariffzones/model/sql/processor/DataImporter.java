package tariffzones.model.sql.processor;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import Splay_tree_package.Record;
import Splay_tree_package.SplayTree;
import au.com.bytecode.opencsv.CSVReader;
import tariffzones.model.BusStop;
import tariffzones.model.MyCoordinate;
import tariffzones.model.sql.interfaces.MhdStopInsert;
import tariffzones.model.sql.interfaces.WayInsert;

public class DataImporter {

	private SQLProcessor mySQLAccess;
	private CSVReader reader;
	private SplayTree<String, BusStop> treeOfBusStops;
	
	public DataImporter() {
		SQLProcessor mySQLAccess = new SQLProcessor();
		mySQLAccess.connectDatabase("admin", "admin");
	}
	
	public void readWays(String waysFileName) throws FileNotFoundException {
		reader = new CSVReader(new FileReader(waysFileName), ',' , '"' , 0);
        String[] nextLine;
        ArrayList<String> busStopNames = readBusStopNames("d:\\Diplomová práca\\TariffZones\\DP data\\data_zilina_uzly.txt");
        BusStop startStop, endStop;
        
        Object[] sqlParamValues = null;
        try {
	        while ((nextLine = reader.readNext()) != null) {
	            if (nextLine != null) {
	            	sqlParamValues = new Object[4];
	            	startStop = (BusStop) treeOfBusStops.findData(busStopNames.get(Integer.parseInt(nextLine[0])-1)).getValue();
	            	endStop = (BusStop) treeOfBusStops.findData(busStopNames.get(Integer.parseInt(nextLine[1])-1)).getValue();
	            	
	                sqlParamValues[WayInsert.PAR_START_STOP_NUMBER] = startStop.getNumber();
	                sqlParamValues[WayInsert.PAR_END_STOP_NUMBER] = endStop.getNumber();
	                sqlParamValues[WayInsert.PAR_TIME_MINS] = Integer.parseInt(nextLine[2]);
	                                    
	                mySQLAccess.insert(WayInsert.SQL, sqlParamValues);
	            }
	        }
	        reader.close();
	    } catch (Exception ex) {
	        Logger.getLogger(DataImporter.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
	
	private ArrayList<String> readBusStopNames(String fileName) throws FileNotFoundException {
		CSVReader reader = new CSVReader(new FileReader(fileName), '\t' , '"' , 0);
        String[] nextLine;
        ArrayList<String> busStopNames = new ArrayList<>();
        try {
	        while ((nextLine = reader.readNext()) != null) {
	            if (nextLine != null) {
	            	busStopNames.add(nextLine[2]);
	            }
	        }
	        reader.close();
	    } catch (Exception ex) {
	        Logger.getLogger(DataImporter.class.getName()).log(Level.SEVERE, null, ex);
	    }
        return busStopNames;
	}
	
	public void readBusStops(String busStopFileName)
    {
		treeOfBusStops = new SplayTree<>();
        try {
            reader = new CSVReader(new FileReader(busStopFileName), ',' , '"' , 0);
            String[] nextLine;
            Object[] sqlParamValues = null;
            
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine != null) {
                	sqlParamValues = new Object[7];
                	MyCoordinate busCoordinate = new MyCoordinate(Double.parseDouble(nextLine[13].substring(0, nextLine[13].length() - 2)), Double.parseDouble(nextLine[12]));
                    BusStop busStop = new BusStop(Integer.parseInt(nextLine[0]), nextLine[3], busCoordinate);
                    
                    if (!treeOfBusStops.insertData(new Record(busStop.getName(), busStop))) {
                    	Logger.getLogger(DataImporter.class.getName()).log(Level.SEVERE, "Cannot insert busStop" + busStop.getNumber() + ", " + busStop.getName());
					}
                    
                    sqlParamValues[MhdStopInsert.PAR_STOP_NUMBER] = busStop.getNumber();
                    sqlParamValues[MhdStopInsert.PAR_STOP_NAME] = busStop.getName();
                    sqlParamValues[MhdStopInsert.PAR_CITY] = 1;
                    sqlParamValues[MhdStopInsert.PAR_COUNTRY] = "SR";
                    sqlParamValues[MhdStopInsert.PAR_LATITUDE] = busCoordinate.getLatitude();
                    sqlParamValues[MhdStopInsert.PAR_LONGITUDE] = busCoordinate.getLongitude();
                            
                    mySQLAccess = new SQLProcessor();
            		mySQLAccess.connectDatabase("admin", "admin");
                    mySQLAccess.insert(MhdStopInsert.SQL, sqlParamValues);
                }
            }
            reader.close();
            readWays("d:\\Diplomová práca\\TariffZones\\DP data\\data_zilina_hrany.dat");
        } catch (Exception ex) {
            Logger.getLogger(DataImporter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
