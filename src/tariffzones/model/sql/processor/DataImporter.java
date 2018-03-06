package tariffzones.model.sql.processor;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;

import tariffzones.model.State;
import tariffzones.model.Stop;
import tariffzones.model.Way;
import tariffzones.tariffzonesprocessor.djikstra.Node;

public class DataImporter {

	public DataImporter() {
		SQLProcessor mySQLAccess = new SQLProcessor();
//		mySQLAccess.connectDatabase("admin", "admin"); //TODO
	}
	
	public ArrayList<Node> readStops(String fileName) throws IOException, FileNotFoundException {
		InputStreamReader fileReader = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
		CSVReader csvReader = new CSVReaderBuilder(fileReader).build();
		String[] nextLine;
		
        ArrayList<Node> stops = new ArrayList<>();
        try {
	        while ((nextLine = csvReader.readNext()) != null) {
	            if (nextLine != null) {
	            	Stop stop = new Stop(Integer.parseInt(nextLine[0]), nextLine[1], Integer.parseInt(nextLine[4]), Double.parseDouble(nextLine[2]), Double.parseDouble(nextLine[3]));
	            	stop.setState(State.ADDED);
	            	stops.add(stop);
	            }
	        }
	        csvReader.close();
	    } catch (Exception ex) {
	        Logger.getLogger(DataImporter.class.getName()).log(Level.SEVERE, null, ex);
	        csvReader.close();
	    }
        return stops;
	}
	
	public ArrayList<Way> readWays(String fileName, ArrayList<Stop> busStops) throws IOException, FileNotFoundException {
		InputStreamReader fileReader = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
		CSVReader csvReader = new CSVReaderBuilder(fileReader).build();
		String[] nextLine;
		
        ArrayList<Way> ways = new ArrayList<>();
        Stop startPoint, endPoint;
        
        try {
	        while ((nextLine = csvReader.readNext()) != null) {
	            if (nextLine != null) {
	            	startPoint = findStop(Integer.parseInt(nextLine[0]), busStops);
	            	endPoint = findStop(Integer.parseInt(nextLine[1]), busStops);
	            	Way way = new Way(startPoint, endPoint, Double.parseDouble(nextLine[2]), Double.parseDouble(nextLine[3])); //TODO: more attribs for Way
	            	way.setState(State.ADDED);
	            	ways.add(way);
	            }
	        }
	        csvReader.close();
	    } catch (Exception ex) {
	        Logger.getLogger(DataImporter.class.getName()).log(Level.SEVERE, null, ex);
	        csvReader.close();
	    }
        return ways;
	}
	
	public Stop findStop(int busNumber, ArrayList<Stop> busStops) {
		for (Node node : busStops) {
			Stop busStop = (Stop) node;
			if (busStop.getNumber() == busNumber) {
				return busStop;
			}
		}
		return null;
	}
	
	public void writeStops(ArrayList<Stop> stops, String fileName) throws IOException, FileNotFoundException {
		
		Writer writer = Files.newBufferedWriter(Paths.get(fileName));
        CSVWriter csvWriter = new CSVWriter(writer,
                CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END);
        
        String[] line = new String [5];
        try {
        	for (Stop stop : stops) {
        		
        		line[0] = Integer.toString(stop.getNumber());
        		line[1] = stop.getName();
        		line[2] = Double.toString(stop.getPosition().getLatitude());
        		line[3] = Double.toString(stop.getPosition().getLongitude());
        		line[4] = Integer.toString(stop.getNumberOfCustomers());
				
				csvWriter.writeNext(line);
			}
        	csvWriter.close();
	    } catch (Exception ex) {
	        Logger.getLogger(DataImporter.class.getName()).log(Level.SEVERE, null, ex);
	        csvWriter.close();
	    }
	}
	
	public void writeWays(ArrayList<Way> ways, String fileName) throws IOException, FileNotFoundException {
		
		Writer writer = Files.newBufferedWriter(Paths.get(fileName));
        CSVWriter csvWriter = new CSVWriter(writer,
                CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END);
        
        String[] line = new String [4];
        try {
        	for (Way way : ways) {
        		line[0] = Integer.toString(way.getStartPoint().getNumber());
				line[1] = Integer.toString(way.getEndPoint().getNumber());
				line[2] = Double.toString(way.getDistance());
				line[3] = Double.toString(way.getTimeLength());
				
				csvWriter.writeNext(line);
			}
        	csvWriter.close();
	    } catch (Exception ex) {
	        Logger.getLogger(DataImporter.class.getName()).log(Level.SEVERE, null, ex);
	        csvWriter.close();
	    }
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
	
	public int[][] readODMatrix(String fileName, ArrayList<Node> nodes) throws IOException, FileNotFoundException {
		int[][] odMatrix = new int[nodes.size()][nodes.size()];
		fillMatrixNegative(odMatrix);
		
		FileReader fileReader = new FileReader(fileName);
		CSVReader csvReader = new CSVReaderBuilder(fileReader).build();
		String[] nextLine;
		String[] firstLine; //in the first line of .csv are names of busStops, matrix should look like busStopsNames x busStopsNames and values 0 and 1 in cells
		try {
			firstLine = csvReader.readNext();
			int iMatrix, jMatrix;
			while ((nextLine = csvReader.readNext()) != null) {
				if (nextLine != null) {
					iMatrix = getIndex(nodes, nextLine[0]);
					for (int i = 0; i < firstLine.length; i++) {
						jMatrix = getIndex(nodes, firstLine[i]);
						if (iMatrix != -1 && jMatrix != -1) {
							odMatrix[iMatrix][jMatrix] = Integer.parseInt(nextLine[i+1]);
						}
					}
				}
				csvReader.close();
			}
		} catch (IOException e) {
			Logger.getLogger(DataImporter.class.getName()).log(Level.SEVERE, null, e);
			csvReader.close();
		}
		return odMatrix;
	}
	
	private int getIndex(ArrayList<Node> nodes, String busStopName) {
		for (int i = 0; i < nodes.size(); i++) {
			if (((Stop)nodes.get(i)).getName().equals(busStopName)) {
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
