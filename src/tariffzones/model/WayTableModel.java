package tariffzones.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class WayTableModel extends AbstractTableModel {

	private String[] columnNames = {"Stop Name", "Stop Name", "Distance", "Time Length"};
	private List<Way> data;
	
	public WayTableModel(List<Way> data) {
		this.data = data;
	}
	
	public WayTableModel(String[] columnNames, List<Way> data) {
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
		//cannot edit stops in way table
		if (columnIndex < 1) {
			return false;
		}
	    return true;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Way way = data.get(rowIndex);
		switch (columnIndex) {
			case 0: return way.getStartPoint().getName();
			case 1:	return way.getEndPoint().getName();
			case 2: return way.getDistance();
			case 3: return way.getTimeLength();
		}
		return null;
	}
	
	public Way getWayAt(int rowIndex) {
		if (rowIndex < data.size()) {
			return data.get(rowIndex);
		}
		return null;
	}
	
	public ArrayList<Way> getWaysAt(int [] rowIndexes) {
		ArrayList<Way> ways = new ArrayList<>();
		for (int i = 0; i < rowIndexes.length; i++) {
			ways.add(getWayAt(rowIndexes[i]));
		}
		return ways;
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		Way way = data.get(rowIndex);
		switch (columnIndex) {
			case 2: 
				way.setDistance((double) aValue);
				break;
				
			case 3:
				way.setTimeLength((double) aValue);
				break;
		}
		fireTableDataChanged();
	}
}
