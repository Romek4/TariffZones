package tariffzones.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel {

	private ResultSet resultSet;
	
	@Override
	public int getColumnCount() {
		if (resultSet == null) {
			return 0;
		}
		try {
			return resultSet.getMetaData().getColumnCount();
		} catch (SQLException e) {
			Logger.getLogger(TableModel.class.getName()).log(Level.SEVERE, null, e);
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public int getRowCount() {
		int rowCount = 0;
        if (resultSet == null) {
            return 0;
        }
        try {
        	resultSet.last();
        	rowCount = resultSet.getRow();
            resultSet.first();
        } catch (SQLException e) {
            Logger.getLogger(TableModel.class.getName()).log(Level.SEVERE, null, e);
        }
        return rowCount;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object result = null;
        if (resultSet != null) {
            try {
            	resultSet.absolute(rowIndex + 1);
                result = resultSet.getObject(columnIndex + 1);
                return result;
            } catch (SQLException e) {
                Logger.getLogger(TableModel.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return result;
	}
	
	@Override
    public String getColumnName(int i) {
		try {
			return resultSet.getMetaData().getColumnName(i+1);
		} catch (SQLException e) {
			Logger.getLogger(TableModel.class.getName()).log(Level.SEVERE, null, e);
			e.printStackTrace();
		}
		return null;
    }
	
	@Override
	public boolean isCellEditable(int row, int column) {
		return true;
	}

	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}
}
