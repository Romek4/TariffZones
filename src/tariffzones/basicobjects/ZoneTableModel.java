package tariffzones.basicobjects;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class ZoneTableModel extends AbstractTableModel {
	
	private String[] columnNames = { "Zones" };
	private List<Zone> data = new ArrayList<>();
	
	public ZoneTableModel(List<Zone> data) {
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
		if (data == null) {
			return null;
		}
		
		return getValueAt(0, c).getClass();
	}

	@Override
	public int getRowCount() {
		if (data != null) {
			return data.size();
		}
		return 0;
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
	    return true;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (data == null) {
			return null;
		}
		
		return data.get(rowIndex);
	}
	
}
