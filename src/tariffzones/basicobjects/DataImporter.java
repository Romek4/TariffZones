package tariffzones.basicobjects;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import tariffzones.processor.djikstra.Node;
import tariffzones.sql.SQLProcessor;

public class DataImporter {

	public DataImporter() {
		SQLProcessor mySQLAccess = new SQLProcessor();
//		mySQLAccess.connectDatabase("admin", "admin"); //TODO
	}
	
	public ArrayList<Node> readStops(String fileName) throws IOException, FileNotFoundException {
		InputStreamReader fileReader = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
		CSVReader csvReader = new CSVReader(fileReader, ';');

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
		CSVReader csvReader = new CSVReader(fileReader, ';');//new CSVReaderBuilder(fileReader).build();
		String[] nextLine;
		
        ArrayList<Way> ways = new ArrayList<>();
        Stop startPoint, endPoint;
        
        try {
	        while ((nextLine = csvReader.readNext()) != null) {
	            if (nextLine != null) {
	            	startPoint = findStop(Integer.parseInt(nextLine[0]), busStops);
	            	endPoint = findStop(Integer.parseInt(nextLine[1]), busStops);
	            	Way way = new Way(startPoint, endPoint, Double.parseDouble(nextLine[2]), Double.parseDouble(nextLine[3]));
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
                ';',
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
        		';',
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
//				line[4] = Integer.toString(way.getComuters());
//				line[5] = Integer.toString(way.getOppositeDirectionComuters());
				
				csvWriter.writeNext(line);
			}
        	csvWriter.close();
	    } catch (Exception ex) {
	        Logger.getLogger(DataImporter.class.getName()).log(Level.SEVERE, null, ex);
	        csvWriter.close();
	    }
	}
	
	public int[][] readODMatrix(String fileName, ArrayList<Stop> stops) throws IOException, FileNotFoundException {
		int[][] odMatrix = new int[stops.size()][stops.size()];
		fillMatrixNegative(odMatrix);
		
		FileReader fileReader = new FileReader(fileName);
		CSVReader csvReader = new CSVReader(fileReader, ';');
		String[] nextLine;
		String[] firstLine;
		try {
			firstLine = csvReader.readNext();
			int iMatrix, jMatrix, k;
			while ((nextLine = csvReader.readNext()) != null) {
				if (nextLine != null) {
					iMatrix = getIndex(stops, Integer.parseInt(nextLine[0]));
					k = 1;
					for (int i = 0; i < firstLine.length; i++) {
						jMatrix = getIndex(stops, Integer.parseInt(firstLine[i]));
						if (iMatrix != -1 && jMatrix != -1) {
							odMatrix[iMatrix][jMatrix] = Integer.parseInt(nextLine[k++]);
						}
					}
				}
			}
			
			csvReader.close();
		} catch (IOException e) {
			Logger.getLogger(DataImporter.class.getName()).log(Level.SEVERE, null, e);
			csvReader.close();
		}
		return odMatrix;
	}
	
	public HashMap<Double, Double> readPrices(String fileName) throws IOException, FileNotFoundException {
		FileReader fileReader = new FileReader(fileName);
		CSVReader csvReader = new CSVReader(fileReader, ';');
		String[] areasLine;
		String[] pricesLine;
		HashMap<Double, Double> prices = new HashMap<>();
		try {
			
			areasLine = csvReader.readNext();
			pricesLine = csvReader.readNext();
			
			for (int i = 0; i < areasLine.length; i++) {
				prices.put(Double.parseDouble(areasLine[i]), Double.parseDouble(pricesLine[i]));
			}
			
			csvReader.close();
		} catch (IOException e) {
			Logger.getLogger(DataImporter.class.getName()).log(Level.SEVERE, null, e);
			System.out.println(e.getStackTrace());
			csvReader.close();
		}
		
		return prices;
	}
	
//	public void writeODMatrix(ArrayList<Way> ways, String fileName) throws IOException, FileNotFoundException {
//		
//		Writer writer = Files.newBufferedWriter(Paths.get(fileName));
//        CSVWriter csvWriter = new CSVWriter(writer,
//        		';',
//                CSVWriter.NO_QUOTE_CHARACTER,
//                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
//                CSVWriter.DEFAULT_LINE_END);
//		
//        String[] line = new String [4];
//        try {
//        	
//			
//			csvWriter.close();
//		} catch (Exception e) {
//			Logger.getLogger(DataImporter.class.getName()).log(Level.SEVERE, null, e);
//			System.out.println(e.getStackTrace());
//			csvWriter.close();
//		}
//		
//	}
	
	private Way findWay(ArrayList<Way> ways, int startStopNumber, int endStopNumber) {
		for (Way way : ways) {
			if (way.getStartPoint().getNumber() == startStopNumber && way.getEndPoint().getNumber() == endStopNumber) {
				return way;
			}
			else if (way.getStartPoint().getNumber() == endStopNumber && way.getEndPoint().getNumber() == startStopNumber) {
				return way;
			}
		}
		return null;
	}
	
	private int getIndex(ArrayList<Stop> stops, int stopNumber) {
		for (int i = 0; i < stops.size(); i++) {
			if ((stops.get(i)).getNumber() == stopNumber) {
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
