package tariffzones.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class StopTableModel extends AbstractTableModel {

	private String[] columnNames = {"Stop Number", "Stop Name", "Customers"};
	private List<Stop> data;
	
	public StopTableModel(List<Stop> data) {
		this.data = data;
	}
	
	public StopTableModel(String[] columnNames, List<Stop> data) {
		this.columnNames = columnNames;
		this.data = data;
	}
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
	
	@Override
    public String getColumnName(int i) {
       return columnNames[i];
    }

	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	@Override
	public int getRowCount() {
		return data.size();
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
	    return true;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Stop stop = data.get(rowIndex);
		switch (columnIndex) {
			case 0: return stop.getNumber();
			case 1:	return stop.getName();
			case 2: return stop.getNumberOfCustomers();
		}
		return null;
	}
	
	public Stop getStopAt(int rowIndex) {
		if (rowIndex < data.size()) {
			return data.get(rowIndex);
		}
		return null;
	}
	
	public ArrayList<Stop> getStopsAt(int [] rowIndexes) {
		ArrayList<Stop> stops = new ArrayList<>();
		for (int i = 0; i < rowIndexes.length; i++) {
			stops.add(getStopAt(rowIndexes[i]));
		}
		return stops;
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		Stop stop = data.get(rowIndex);
		switch (columnIndex) {
			case 0: 
				stop.setNumber((int) aValue);
				break;
				
			case 1:
				stop.setName((String) aValue);
				break;
				
			case 2:
				stop.setNumberOfCustomers((int) aValue);;
				break;
		}
		fireTableDataChanged();
	}
}
