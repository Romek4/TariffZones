package tariffzones.model.sql.processor;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import Splay_tree_package.Record;
import Splay_tree_package.SplayTree;
import au.com.bytecode.opencsv.CSVReader;
import tariffzones.model.BusStop;
import tariffzones.model.MyCoordinate;
import tariffzones.model.Way;
import tariffzones.model.sql.interfaces.StopInsert;
import tariffzones.model.sql.interfaces.EdgeInsert;
import tariffzones.tariffzonesprocessor.djikstra.Node;

public class DataImporter {

	private SQLProcessor mySQLAccess;
	private CSVReader reader;
	private SplayTree<String, BusStop> treeOfBusStops;
	
	public DataImporter() {
		SQLProcessor mySQLAccess = new SQLProcessor();
		mySQLAccess.connectDatabase("admin", "admin"); //TODO
	}
	
//	public void readWaysToDB(String waysFileName, ArrayList<BusStop> busStops) throws FileNotFoundException {
//		reader = new CSVReader(new FileReader(waysFileName), ',' , '"' , 0);
//        String[] nextLine;
////        ArrayList<String> busStopNames = readBusStopNames("d:\\Diplomová práca\\TariffZones\\DP data\\data_zv_25_uzly.dat");
//        int startStopId = 0, endStopId = 0;
//        
//        Object[] sqlParamValues = null;
//        try {
//	        while ((nextLine = reader.readNext()) != null) {
//	            if (nextLine != null) {
//	            	sqlParamValues = new Object[4];
//	            	//startStop = (BusStop) treeOfBusStops.findData(busStopNames.get(Integer.parseInt(nextLine[0])-1)).getValue();
//	            	//endStop = (BusStop) treeOfBusStops.findData(busStopNames.get(Integer.parseInt(nextLine[1])-1)).getValue();
//	            	
//	            	mySQLAccess.select("SELECT stop_id from stop where stop_name = '" + busStops.get(Integer.parseInt(nextLine[0])-1) + "'", null);
//	            	if (mySQLAccess.getResultSet().next()) {
//	            		startStopId = Integer.parseInt(mySQLAccess.getResultSet().getString("stop_id"));
//					}
//	            	//startStopId = Integer.parseInt(mySQLAccess.getResultSet().getString("stop_id"));
//	            	mySQLAccess.select("SELECT stop_id from stop where stop_name = '" + busStops.get(Integer.parseInt(nextLine[1])-1) + "'", null);
//	            	if (mySQLAccess.getResultSet().next()) {
//	            		endStopId = Integer.parseInt(mySQLAccess.getResultSet().getString("stop_id"));
//					}
//	            	//endStopId = Integer.parseInt(mySQLAccess.getResultSet().getString("stop_id"));
//	            	
//	            	if (startStopId == 0 || endStopId == 0) {
//						return;
//					}
//	                sqlParamValues[EdgeInsert.PAR_START_STOP_ID] = startStopId;
//	                sqlParamValues[EdgeInsert.PAR_END_STOP_ID] = endStopId;
//	                sqlParamValues[EdgeInsert.PAR_KM_LENGTH] = Integer.parseInt(nextLine[2]);
//	                                    
//	                mySQLAccess.insert(EdgeInsert.SQL, sqlParamValues);
//	            }
//	        }
//	        reader.close();
//	    } catch (Exception ex) {
//	        Logger.getLogger(DataImporter.class.getName()).log(Level.SEVERE, null, ex);
//	    }
//	}
	
	public ArrayList<Way> readWays(String waysFileName, ArrayList<BusStop> busStops) throws FileNotFoundException {
		reader = new CSVReader(new FileReader(waysFileName), ',' , '"' , 0);
        String[] nextLine;
        
        ArrayList<Way> ways = new ArrayList<>();
        BusStop startPoint, endPoint;
        
        try {
	        while ((nextLine = reader.readNext()) != null) {
	            if (nextLine != null) {
	            	startPoint = findStop(Integer.parseInt(nextLine[0]), busStops);
	            	endPoint = findStop(Integer.parseInt(nextLine[1]), busStops);
	            	Way way = new Way(startPoint, endPoint, Double.parseDouble(nextLine[2]), Double.parseDouble(nextLine[3])); //TODO: more attribs for Way
	            	ways.add(way);
	            }
	        }
	        reader.close();
	    } catch (Exception ex) {
	        Logger.getLogger(DataImporter.class.getName()).log(Level.SEVERE, null, ex);
	    }
        return ways;
	}
	
	public BusStop findStop(int busNumber, ArrayList<BusStop> busStops) {
		for (Node node : busStops) {
			BusStop busStop = (BusStop) node;
			if (busStop.getNumber() == busNumber) {
				return busStop;
			}
		}
		return null;
	}
	
	public ArrayList<Node> readBusStops(String fileName, char delimiter) throws FileNotFoundException {
		CSVReader reader = new CSVReader(new FileReader(fileName), delimiter , '"' , 0);
        String[] nextLine;
        ArrayList<Node> busStops = new ArrayList<>();
        try {
	        while ((nextLine = reader.readNext()) != null) {
	            if (nextLine != null) {
	            	MyCoordinate coordinate = new MyCoordinate(Double.parseDouble(nextLine[2]), Double.parseDouble(nextLine[3]));
	            	BusStop stop = new BusStop(Integer.parseInt(nextLine[0]), nextLine[1], coordinate, Integer.parseInt(nextLine[4]));
	            	busStops.add(stop);
	            }
	        }
	        reader.close();
	    } catch (Exception ex) {
	        Logger.getLogger(DataImporter.class.getName()).log(Level.SEVERE, null, ex);
	    }
        return busStops;
	}
	
//	public void readBusStops(String busStopFileName)
//    {
//		treeOfBusStops = new SplayTree<>();
//        try {
//            reader = new CSVReader(new FileReader(busStopFileName), ',' , '"' , 0);
//            String[] nextLine;
//            Object[] sqlParamValues = null;
//            
//            while ((nextLine = reader.readNext()) != null) {
//                if (nextLine != null) {
//                	sqlParamValues = new Object[8];
//                	MyCoordinate busCoordinate = new MyCoordinate(Double.parseDouble(nextLine[13].substring(0, nextLine[13].length() - 2)), Double.parseDouble(nextLine[12]));
//                    BusStop busStop = new BusStop(Integer.parseInt(nextLine[0]), nextLine[3], busCoordinate, 0);
//                    
//                    if (!treeOfBusStops.insertData(new Record(busStop.getName(), busStop))) {
//                    	Logger.getLogger(DataImporter.class.getName()).log(Level.SEVERE, "Cannot insert busStop" + busStop.getNumber() + ", " + busStop.getName());
//					}
//                    
//                    sqlParamValues[StopInsert.PAR_NETWORK_ID] = 1;
//                    sqlParamValues[StopInsert.PAR_COUNTRY_ID] = "SR";
//                    sqlParamValues[StopInsert.PAR_STOP_NUMBER] = busStop.getNumber();
//                    sqlParamValues[StopInsert.PAR_STOP_NAME] = busStop.getName();
//                    sqlParamValues[StopInsert.PAR_LATITUDE] = busCoordinate.getLatitude();
//                    sqlParamValues[StopInsert.PAR_LONGITUDE] = busCoordinate.getLongitude();
//                    sqlParamValues[StopInsert.PAR_NUM_OF_HABITANTS] = busStop.getNumberOfCustomers();
////                            
//                    mySQLAccess = new SQLProcessor();
//            		mySQLAccess.connectDatabase("admin", "admin");
//                    mySQLAccess.insert(StopInsert.SQL, sqlParamValues);
//                }
//            }
//            reader.close();
//            readWays("d:\\Diplomová práca\\TariffZones\\DP data\\data_zv_25_hrany.dat");
//        } catch (Exception ex) {
//            Logger.getLogger(DataImporter.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
	
	public int[][] readODMatrix(String fileName, ArrayList<Node> nodes) {
		int[][] odMatrix = new int[nodes.size()][nodes.size()];
		fillMatrixNegative(odMatrix);
		try {
			reader = new CSVReader(new FileReader(fileName), ',' , '"' , 0);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String[] nextLine;
		String[] firstLine; //in the first line of .csv are names of busStops, matrix should look like busStopsNames x busStopsNames and values 0 and 1 in cells
		try {
			firstLine = reader.readNext();
			int iMatrix, jMatrix;
			while ((nextLine = reader.readNext()) != null) {
				if (nextLine != null) {
					iMatrix = getIndex(nodes, nextLine[0]);
					for (int i = 0; i < firstLine.length; i++) {
						jMatrix = getIndex(nodes, firstLine[i]);
						if (iMatrix != -1 && jMatrix != -1) {
							odMatrix[iMatrix][jMatrix] = Integer.parseInt(nextLine[i+1]);
						}
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return odMatrix;
	}
	
	private int getIndex(ArrayList<Node> nodes, String busStopName) {
		for (int i = 0; i < nodes.size(); i++) {
			if (((BusStop)nodes.get(i)).getName().equals(busStopName)) {
				return i;
			}
		}
		return -1; //not found
	}
	
	private void fillMatrixNegative(int [][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				matrix[i][j] = -1;
			}
		}
	}
}
